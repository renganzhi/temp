package com.uxsino.leaderview.websocket;

import com.google.common.base.Strings;
import com.uxsino.commons.baseclass.Constant;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class SocketInterceptor extends HttpSessionHandshakeInterceptor {
    public static final String REQ_PARAMER_KEY = "REQ_PARAMER_KEY";

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if(request instanceof ServletServerHttpRequest){
            Map<String, Object> params = new HashMap<>();
            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest)request;
            HttpServletRequest httpServletRequest = servletServerHttpRequest.getServletRequest();
            HttpSession session = httpServletRequest.getSession();
            if(session != null){
                attributes.put(Constant.HTTP_SESSION, session);
            }
            Enumeration<String> keys = httpServletRequest.getParameterNames();
            while(keys.hasMoreElements()){
                String key = keys.nextElement();
                params.put(key, httpServletRequest.getParameter(key));
            }
            attributes.put(REQ_PARAMER_KEY, params);
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        HttpServletRequest req = ((ServletServerHttpRequest)request).getServletRequest();
        HttpServletResponse resp = ((ServletServerHttpResponse)response).getServletResponse();

        String mk = req.getHeader("Sec-WebSocket-Protocol");
        if(!Strings.isNullOrEmpty(mk)){
            resp.addHeader("Sec-WebSocket-Protocol", mk);
        }
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
