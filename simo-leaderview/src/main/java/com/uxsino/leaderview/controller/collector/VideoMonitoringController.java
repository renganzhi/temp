package com.uxsino.leaderview.controller.collector;

import com.uxsino.commons.utils.StringUtils;
import com.uxsino.leaderview.service.VideoMonitoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/collector/videoMonitoring")
@Component
public class VideoMonitoringController {
    private static final Logger log = LoggerFactory.getLogger(VideoMonitoringController.class);

    private static ConcurrentHashMap<String, Session> webSocketMap = new ConcurrentHashMap<>();

    private static VideoMonitoringService videoMonitoringService;

    @Autowired
    public void setVideoMonitoringService(VideoMonitoringService videoMonitoringService){
        VideoMonitoringController.videoMonitoringService = videoMonitoringService;
    }

    @OnOpen
    public void onOpen(Session session){
        String[] params = session.getQueryString().split("&");
        String neId = params[0].split("=")[1];
        String stream = params[1].split("=")[1];
        String channel = params[2].split("=")[1];
        log.info("{}连接到摄像头", neId + "_" + stream + "_" + channel);
        webSocketMap.put(session.getId(), session);
        if(neId==null || StringUtils.isEmpty(neId)
                || stream==null || StringUtils.isEmpty(stream)
                || channel==null || StringUtils.isEmpty(channel)) {

            log.warn("摄像头设备参数有误，不能连接！");
            return;
        }
        try {
            videoMonitoringService.register(new VideoMonitoringService.VideoConsumer<byte[]>() {
                @Override
                public void accept(byte[] data) {
                    try {
                        if(session.isOpen()) {
                            session.getBasicRemote().sendBinary(ByteBuffer.wrap(data));
                        }
                    }catch (IOException e){
                        log.error("向客户端推送帧数据抛出异常：", e.getMessage());
                    }
                }
            }.set(session.getId(), neId, stream, channel));
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            try {
                session.close();
            } catch (IOException ex) {
                log.error("关闭websocket连接异常！");
            }
        }
    }

    @OnClose
    public void onClose(Session session){
        String sessionId = session.getId();
        webSocketMap.remove(sessionId);
        videoMonitoringService.remove(sessionId);
    }

    @OnMessage
    public void onMessage(Session session, String message){

    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        onClose(session);
        if (session.isOpen())
            try {
                session.close();
            } catch (IOException e) {
                log.error("WebSocket传输过程出错，且关闭session失败！抛出异常：{}", e.getMessage());
            }
        log.error("WebSocket传输过程出错抛出异常：{}", throwable.getMessage());
    }
}
