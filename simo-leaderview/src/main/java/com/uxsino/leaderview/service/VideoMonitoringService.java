package com.uxsino.leaderview.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.uxsino.reactorq.commons.ProcessUtil;
import com.uxsino.reactorq.commons.ReactorQFactory;
import com.uxsino.reactorq.constant.EventTopicConstants;
import com.uxsino.reactorq.subscriber.EventSubscriber;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.TopicProcessor;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Service
public class VideoMonitoringService {
    private static final Logger log = LoggerFactory.getLogger(VideoMonitoringService.class);

    @Autowired
    @Qualifier("default_reactorq_factory")
    private ReactorQFactory rqFactory;

    @Autowired
    @Qualifier("outbox_lv")
    private EventSubscriber outbox;

    @Getter
    public static abstract class VideoConsumer<T> implements Consumer<T>{
        private String sessionId;
        private String neId;
        private String stream;
        private String channel;
        private TopicProcessor<T> processor = null;

        public VideoConsumer<T> set(String sessionId, String neId, String stream, String channel){
            this.sessionId = sessionId;
            this.neId = neId;
            this.stream = stream;
            this.channel = channel;
            processor = ProcessUtil.createTopicProcessor("VIDEO_CONSUMER_" + neId, 8, false);
            processor.subscribe(this);

            return this;
        }

        public void append(T data){
            processor.onNext(data);
        }
    }

    private static ConcurrentHashMap<String, ArrayList<VideoConsumer<byte[]>>> connectionParams = new ConcurrentHashMap<>();
    public static final TopicProcessor<Runnable> VIDEO_PROCESSOR = ProcessUtil.createTopicProcessor("video", 32, false);
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void init(){
        VIDEO_PROCESSOR.subscribe(Runnable::run);
        try{
            rqFactory.createTopicFlux(EventTopicConstants.SIMO_VIDEO_BYTE, JSONObject.class).subscribe(jsonObject -> {
                VIDEO_PROCESSOR.onNext(new Runnable() {
                    @Override
                    public void run() {
                        String neId = jsonObject.getString("neId");
                        String stream = jsonObject.getString("stream");
                        String channel = jsonObject.getString("channel");
                        byte[] data = jsonObject.getBytes("data");

                        String key = neId + "_" + stream + "_" + channel;
                        ArrayList<VideoConsumer<byte[]>> videoConsumerArrayList = connectionParams.get(key);
                        for(VideoConsumer<byte[]> videoConsumer: videoConsumerArrayList){
                            videoConsumer.append(data);
                        }
                    }
                });
            });
        }catch (JMSException e){
            log.error("从采集器接收帧数据抛出异常：{}", e.getMessage());
        }
        scheduler.scheduleAtFixedRate(this::updateState, 0, 30, TimeUnit.SECONDS);
    }

    public void register(VideoConsumer<byte[]> videoConsumer){
        String id = videoConsumer.getNeId() + "_" + videoConsumer.getStream() + "_" + videoConsumer.getChannel();
        if(!connectionParams.containsKey(id)){
            connectionParams.put(id, new ArrayList<>());
        }
        connectionParams.get(id).add(videoConsumer);

        this.updateState();
    }

    private void remove(VideoConsumer<byte[]> videoConsumer){
        remove(videoConsumer.getSessionId());
    }

    public void remove(String sessionId){
        ArrayList<VideoConsumer<byte[]>> videoConsumerArrayList = connectionParams.get(sessionId);
        if(videoConsumerArrayList==null || videoConsumerArrayList.size()==0){
            connectionParams.remove(sessionId);
        }else{
            videoConsumerArrayList.removeIf(videoConsumer -> sessionId.equals(videoConsumer.getSessionId() ));
        }
        this.updateState();
    }

    private void updateState(){
        Map<String, Set<String>> conf = new HashMap<>();
        for(ConcurrentHashMap.Entry<String, ArrayList<VideoConsumer<byte[]>>> connectionParam : connectionParams.entrySet()){
            if(connectionParam.getValue()==null)
                continue;
            //{neId, stream, channel}
            String[] key = connectionParam.getKey().split("_");
            if(!conf.containsKey(key[0])){
                conf.put(key[0], new HashSet<>());
            }
            conf.get(key[0]).add(key[1] + "_" + key[2]);
        }

        try {
            outbox.sendStringOnTopic(EventTopicConstants.SIMO_VIDEO_STATE, JSON.toJSONString(conf));
        } catch (JMSException e) {
            log.error("向采集器发送消息抛出异常：{}", e.getMessage());
        }
    }
}
