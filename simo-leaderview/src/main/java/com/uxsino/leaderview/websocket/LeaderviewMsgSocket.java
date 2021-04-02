package com.uxsino.leaderview.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class LeaderviewMsgSocket implements WebSocketConfigurer {
    @Autowired
    SocketInterceptor interceptor;

    @Autowired
    VideoSocketHandler videoHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(videoHandler, "/collector/videoMonitoring").setAllowedOrigins("*").addInterceptors(interceptor);
    }
}
