package com.uxsino.leaderview.security.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import com.uxsino.commons.db.repository.impl.CustomRepositoryImpl;

/**
 * @description spring security权限服务配置类
 * 
 * @date 2017年4月13日
 */
@Order(13)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableJpaRepositories(repositoryBaseClass = CustomRepositoryImpl.class)
public class LeaderViewWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(LeaderViewWebSecurityConfig.class);

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.info("开始加载spring security 配置项");
        http.csrf().disable();
    }

}
