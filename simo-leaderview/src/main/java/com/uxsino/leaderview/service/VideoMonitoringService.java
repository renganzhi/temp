package com.uxsino.leaderview.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.uxsino.leaderview.dao.INetworkEntityDao;
import com.uxsino.leaderview.entity.NetworkEntity;
import com.uxsino.leaderview.handler.VideoProduceHandler;
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
import java.util.concurrent.*;
import java.util.function.Consumer;

@Service
public class VideoMonitoringService {
    private static final Logger log = LoggerFactory.getLogger(VideoMonitoringService.class);
    //用于决定采用从采集器获取帧数据还是大屏自己去获取帧数据
    private static final boolean isFromCollector = false;

    @Autowired
    @Qualifier("default_reactorq_factory")
    private ReactorQFactory rqFactory;

    @Autowired
    @Qualifier("outbox_lv")
    private EventSubscriber outbox;

    @Autowired
    private INetworkEntityDao networkEntityDao;

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

    private static ConcurrentHashMap<String, CopyOnWriteArrayList<VideoConsumer<byte[]>>> connectionParams = new ConcurrentHashMap<>();
    public static final TopicProcessor<Runnable> VIDEO_PROCESSOR = ProcessUtil.createTopicProcessor("video", 32, false);
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void init(){
        //如果采用大屏这边模仿采集器的VideoProducer，则不用对updateState设置定时，因为VideoProducerHandler会在while循环中
        //不断通过传入的Predictor<String>来判断connectionParams中对应的Consumer是否还存在，因此此时updateState方法的作用
        //就转化为了仅仅在新摄像头通道介入时新建一个对应摄像头连接的线程用于捕捉帧数据
        if (isFromCollector) {
            VIDEO_PROCESSOR.subscribe(Runnable::run);
            try{
                rqFactory.createTopicFlux(EventTopicConstants.SIMO_VIDEO_BYTE, JSONObject.class).subscribe(jsonObject -> {
                    VIDEO_PROCESSOR.onNext(new Runnable() {
                        @Override
                        public void run() {
                            consume(jsonObject);
                        }
                    });
                });
            }catch (JMSException e){
                log.error("LEADERVIEW -> 从采集器接收帧数据抛出异常：{}", e.getMessage());
            }
            scheduler.scheduleAtFixedRate(()->this.updateState(null), 0, 30, TimeUnit.SECONDS);
        }
    }

    public void register(VideoConsumer<byte[]> videoConsumer) throws NullPointerException{
        String id = videoConsumer.getNeId() + "_" + videoConsumer.getStream() + "_" + videoConsumer.getChannel();
        boolean isNewConnection = false;
        if(!connectionParams.containsKey(id)){
            connectionParams.put(id, new CopyOnWriteArrayList<>());
            isNewConnection = true;
        }
        CopyOnWriteArrayList<VideoConsumer<byte[]>> consumers = connectionParams.get(id);
        consumers.add(videoConsumer);

        if(isFromCollector || isNewConnection)
            this.updateState(id);
    }

    public void remove(String sessionId, String key){
        CopyOnWriteArrayList<VideoConsumer<byte[]>> videoConsumerArrayList = connectionParams.get(key);
        if(videoConsumerArrayList!=null && videoConsumerArrayList.size()>0){
            videoConsumerArrayList.removeIf(videoConsumer -> sessionId.equals(videoConsumer.getSessionId()));
        }
        if(videoConsumerArrayList==null || videoConsumerArrayList.size() == 0) {
            connectionParams.remove(key);
        }
        //同init处注解，当不通过采集器时，VideoProduceHandler将会通过其Predicator<String>时时刻刻对connectionParams进行检查
        //因此每一次删除其连接线程都能够感受到变化，就不用再调用updateState方法了
        if(isFromCollector)
            this.updateState(key);
    }

    /**
     *
     * @param id 这里的id仅用于大屏直接获取数据模式下时，定位哪一个通道
     */
    private void updateState(String id){
        Map<String, Set<String>> conf = new HashMap<>();
        if (isFromCollector) {
            for(ConcurrentHashMap.Entry<String, CopyOnWriteArrayList<VideoConsumer<byte[]>>> connectionParam : connectionParams.entrySet()){
                if(connectionParam.getValue()==null)
                    continue;
                //{neId, stream, channel}
                String[] key = connectionParam.getKey().split("_");
                if (!conf.containsKey(key[0])) {
                    conf.put(key[0], new HashSet<>());
                }
                conf.get(key[0]).add(key[1] + "_" + key[2]);
                try {
                    outbox.sendStringOnTopic(EventTopicConstants.SIMO_VIDEO_STATE, JSON.toJSONString(conf));
                } catch (JMSException e) {
                    log.error("LEADERVIEW -> 向采集器发送消息抛出异常：{}", e.getMessage());
                }
            }
        }else {
            if(id == null) {
                log.error("LEADERVIEW -> 找不到对应监控设备！");
                throw new NullPointerException("传入id为空，找不到对应监控设备");
            }
            String[] key = id.split("_");
            Optional<NetworkEntity> ne = networkEntityDao.findById(key[0]);
            if (ne.isPresent()) {
                new VideoProduceHandler(ne.get(), key[1], key[2], "HCNET", this::consume,
                        k -> connectionParams.containsKey(k) && connectionParams.get(k).size()>0,
                        ()->{
                            CopyOnWriteArrayList<VideoConsumer<byte[]>> videoConsumerArrayList = connectionParams.get(id);
                            for(VideoConsumer<byte[]> consumer: videoConsumerArrayList){
                                remove(consumer.getSessionId(), consumer.getNeId()+"_"+consumer.getStream()+"_"+consumer.getChannel());
                            }
                        }
                ).start();
            } else {
                throw new NullPointerException("LEADERVIEW -> 传入neId有误，没有发现该摄像头平台！");
            }
        }
    }

    private void consume(JSONObject jsonObject){
        String neId = jsonObject.getString("neId");
        String stream = jsonObject.getString("stream");
        String channel = jsonObject.getString("channel");
        byte[] data = jsonObject.getBytes("data");

        String key = neId + "_" + stream + "_" + channel;
        CopyOnWriteArrayList<VideoConsumer<byte[]>> videoConsumerArrayList = connectionParams.get(key);
        for(VideoConsumer<byte[]> videoConsumer: videoConsumerArrayList){
            videoConsumer.append(data);
        }
    }
}
