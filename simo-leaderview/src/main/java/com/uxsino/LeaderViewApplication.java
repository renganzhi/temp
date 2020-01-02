package com.uxsino;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.uxsino.commons.db.repository.impl.CustomRepositoryImpl;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomRepositoryImpl.class)
@EnableRedisHttpSession
@EnableEurekaClient
@EnableFeignClients
@EnableScheduling
@EnableOAuth2Sso
@EnableSwagger2
public class LeaderViewApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeaderViewApplication.class, args);
    }

}
