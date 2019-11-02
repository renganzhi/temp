package com.uxsino.leaderview.feign.conf;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Configuration
public class LeaderViewFeignClientsConf {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                return;
            }
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String name = "Cookie";
            Enumeration<String> values = request.getHeaders(name);
            if (values == null) {
                return;
            }
            while (values.hasMoreElements()) {
                String value = values.nextElement();
                requestTemplate.header(name, value);
            }
        };
    }
}
