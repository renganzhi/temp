package com.uxsino.leaderview.handler;

import com.alibaba.fastjson.JSONObject;
import com.uxsino.commons.utils.DESUtil;
import com.uxsino.commons.utils.StringUtils;
import com.uxsino.leaderview.dao.INetworkEntityDao;
import com.uxsino.leaderview.entity.NetworkEntity;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class VideoProduceHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(VideoProduceHandler.class);

    private NetworkEntity ne;
    private String username;
    private String password;
    private String ip;
    private String port;
    private String stream;
    private String channel;
    private Consumer<JSONObject> consumer;
    private Predicate<String> isStopped;
    private Runnable onClose;

    private INetworkEntityDao networkEntityDao;

    public VideoProduceHandler(NetworkEntity ne, String stream, String channel, String protocol, Consumer<JSONObject> consumer, Predicate<String> isStopped, Runnable onClose){
        this.ne = ne;
        this.stream = stream;
        this.channel = channel;
        this.consumer = consumer;
        this.isStopped = isStopped;
        this.onClose = onClose;
        JSONObject protocols;
        if(ne!=null){
            protocols = ne.j.getJSONObject("protocols");
            if(protocols.containsKey(protocol) || protocols.containsKey(protocol.toLowerCase())){
                protocol = protocols.containsKey(protocol)? protocol: protocol.toLowerCase();
                JSONObject protocolConfig = protocols.getJSONObject(protocol);
                ip = protocolConfig.getString("host");
                port = protocolConfig.getString("port");
                username = protocolConfig.getString("username");
                try {
                    password = DESUtil.decrypt(protocolConfig.getString("password"));
                }catch (Exception e){
                    log.error("解析该设备登录密码错误！");
                }
            }
        }
    }

    private FFmpegFrameGrabber grabber;
    private FFmpegFrameRecorder recorder;
    private ByteArrayOutputStream os;

    @Override
    public void run(){
        try {
            this.init();
        }catch (Exception e){
            if (e instanceof NullPointerException){
                log.error(e.getMessage());
            }else if(e instanceof FrameGrabber.Exception){
                log.error("捕捉器启动失败！");
            }else if(e instanceof FrameRecorder.Exception){
                log.error("记录器启动失败！");
            }
            this.close();
            return;
        }

        Frame frame;
        int count = 0;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("neId", ne.id);
        jsonObject.put("stream", stream);
        jsonObject.put("channel", channel);
        while(isStopped.test(ne.id+"_"+stream+"_"+channel)){
            try {
                frame = grabber.grabFrame();
                if(frame == null){
                    count++;
                    if(count == 3){
                        log.error("丢失帧数据过多，断开连接");
                        break;
                    }
                }else {
                    count = 0;
                    recorder.setTimestamp(grabber.getTimestamp());
                    recorder.record(frame);
                    if(consumer != null){
                        jsonObject.put("data", os.toByteArray());
                        consumer.accept(jsonObject);
                        os.reset();
                    }else {
                        throw new NullPointerException("帧数据没有消费渠道！");
                    }
                }
            }catch (Exception e){
                if (e instanceof NullPointerException){
                    log.error(e.getMessage());
                }else if(e instanceof FrameGrabber.Exception){
                    log.error("捕捉器捕捉失败！");
                }else if(e instanceof FrameRecorder.Exception){
                    log.error("记录器记录失败！");
                }
                break;
            }
        }
        this.close();
    }

    private void init() throws Exception {
        os = new ByteArrayOutputStream();
        if(ip==null || port==null || username==null || password==null)
            throw new NullPointerException("摄像头设备登录信息不足，无法连接");
        grabber = new FFmpegFrameGrabber(this.getRstpUrl());
        grabber.setOption("rtsp_transport", "tcp");
        grabber.start();

        recorder = new FFmpegFrameRecorder(os, grabber.getImageWidth(), grabber.getImageHeight());
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG1VIDEO);
        recorder.setGopSize((int) (grabber.getVideoFrameRate() * 2));
        recorder.setFrameRate(grabber.getVideoFrameRate());
        recorder.setFormat("mpegts");
        recorder.setVideoQuality(4);
        recorder.start();
    }

    private void close(){
        if(grabber!=null){
            try {
                grabber.close();
            }catch (FrameGrabber.Exception e){
                log.error("关闭捕捉器失败！");
            }
        }
        if(recorder!=null){
            try {
                recorder.close();
            }catch (FrameRecorder.Exception e){
                log.error("关闭记录器失败！");
            }
        }
        onClose.run();
    }

    private String getRstpUrl(){
        if(stream==null || StringUtils.isEmpty(stream) || channel==null || StringUtils.isEmpty(channel))
            return null;
        if(!StringUtils.isInteger(channel))
            return null;
        else{
            int channelNo = Integer.parseInt(channel) + 32;
            return "rtsp://" + username + ":" + password + "@" + ip + ":" + "554/h264/ch" + channelNo + "/" + stream + "/av_stream";
        }
    }

}
