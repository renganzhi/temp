package com.uxsino.leaderview.websocket;

import com.uxsino.commons.utils.StringUtils;
import com.uxsino.leaderview.service.VideoMonitoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VideoSocketHandler implements WebSocketHandler, InitializingBean, ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(VideoSocketHandler.class);

    private static ConcurrentHashMap<String, WebSocketSession> webSocketMap = new ConcurrentHashMap<>();

    @Autowired
    private VideoMonitoringService videoMonitoringService;

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> params =(Map<String, Object>) session.getAttributes().get(SocketInterceptor.REQ_PARAMER_KEY);
        String neId = (String)params.get("neId");
        String stream = (String)params.get("stream");
        String channel = (String)params.get("channel");
        if(neId==null || StringUtils.isEmpty(neId)
                || stream==null || StringUtils.isEmpty(stream)
                || channel==null || StringUtils.isEmpty(channel)) {

            log.warn("LEADERVIEW -> 摄像头设备参数有误，不能连接！");
            return;
        }
        log.info("LEADERVIEW -> {}准备连接到摄像头", neId + "_" + stream + "_" + channel);
        webSocketMap.put(session.getId(), session);
        try {
            videoMonitoringService.register(new VideoMonitoringService.VideoConsumer<byte[]>() {
                @Override
                public void accept(byte[] data) {
                    try {
                        if(session.isOpen()) {
                            BinaryMessage binaryMessage = new BinaryMessage(data);
                            session.sendMessage(binaryMessage);
                        }
                    }catch (IOException e){
                        log.error("LEADERVIEW -> 向客户端推送帧数据抛出异常：", e.getMessage());
                    }
                }
            }.set(session.getId(), neId, stream, channel));
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            try {
                session.close();
            } catch (IOException ex) {
                log.error("LEADERVIEW -> 关闭websocket连接异常！");
            }
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        String sessionId = session.getId();
        Map<String, Object> params =(Map<String, Object>) session.getAttributes().get(SocketInterceptor.REQ_PARAMER_KEY);
        String neId = (String)params.get("neId");
        String stream = (String)params.get("stream");
        String channel = (String)params.get("channel");
        if (session.isOpen())
            try {
                session.close();
            } catch (IOException e) {
                log.error("LEADERVIEW -> WebSocket传输过程出错，且关闭session失败！抛出异常：{}", e.getMessage());
            }
        log.error("LEADERVIEW -> WebSocket传输过程出错抛出异常：{}", exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String sessionId = session.getId();
        Map<String, Object> params =(Map<String, Object>) session.getAttributes().get(SocketInterceptor.REQ_PARAMER_KEY);
        String neId = (String)params.get("neId");
        String stream = (String)params.get("stream");
        String channel = (String)params.get("channel");
        webSocketMap.remove(sessionId);
        videoMonitoringService.remove(sessionId, neId+"_"+stream+"_"+channel);
        log.info("LEADERVIEW -> Websocket session关闭连接！sessionID={}", sessionId);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
