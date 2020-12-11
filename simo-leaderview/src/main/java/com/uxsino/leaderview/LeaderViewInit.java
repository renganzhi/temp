package com.uxsino.leaderview;

import javax.jms.JMSException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uxsino.leaderview.handler.LeaderViewAuthorityHandler;
import com.uxsino.leaderview.service.AuthorityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.uxsino.leaderview.handler.HomeDataApiHandler;
import com.uxsino.leaderview.service.HomeTemplateService;
import com.uxsino.reactorq.commons.JMSFlux;
import com.uxsino.reactorq.commons.ReactorQFactory;
import com.uxsino.reactorq.constant.EventTopicConstants;
import com.uxsino.reactorq.event.HeartbeatEvent;
import com.uxsino.reactorq.event.ModuleOnLineEvent;
import com.uxsino.reactorq.subscriber.EventSubscriber;

@Component
@Configuration
@EnableScheduling
public class LeaderViewInit implements InitializingBean {

	private static Logger logger = LoggerFactory.getLogger(LeaderViewInit.class);

	@Autowired
	@Qualifier("outbox_lv")
	private EventSubscriber _outbox;

	@Autowired
	@Qualifier("default_reactorq_factory")
	private ReactorQFactory rqFactory;

	@Autowired
	private HomeDataApiHandler homeDataApiHandler;

	@Autowired
	private HomeTemplateService homeTemplateService;

	@Autowired
	private LeaderViewAuthorityHandler authorityHandler;

	@Autowired
	private AuthorityService authorityService;

	public void init() {
		_outbox.setFrom("leaderview");
		ModuleOnLineEvent ev = new ModuleOnLineEvent();
		ev.Module = "viewer";
		ev.id = "";
		ev.onLineTimeMillis = System.currentTimeMillis();
		_outbox.onNext(ev);
		// 初始化主页大屏的模板信息
		homeTemplateService.delAll();
		homeTemplateService.init();
		// 订阅大屏展示API注册
		try {
			rqFactory.createTopicFlux(EventTopicConstants.SIMO_LEADERVIEW_API, String.class).subscribe(
					JMSFlux.Catch(homeDataApiHandler::register, LoggerFactory.getLogger(HomeDataApiHandler.class)));
		} catch (JMSException e) {
			logger.error("leader view conn mq error:{}", e);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
		subscribeAuthority();
	}

	/**
	 * Sent heartbeat in fixed rate
	 */
	@Scheduled(fixedRateString = "${heartbeat.send_interval:30000}")
	public void sendHeartbeat() {
		HeartbeatEvent heartbeatEvent = new HeartbeatEvent();
		heartbeatEvent.module = "viewer";
		heartbeatEvent.id = "viewer";
		heartbeatEvent.heartbeatMillis = System.currentTimeMillis();
		_outbox.onNext(heartbeatEvent);
	}

	private void subscribeAuthority() {
		try {
			// 域信息变更广播
			rqFactory.createTopicFlux(EventTopicConstants.SIMO_DOMAIN_NOTIFY, JSONArray.class).subscribe(
					JMSFlux.Catch(authorityHandler::handle, LoggerFactory.getLogger(LeaderViewAuthorityHandler.class))
			);

			// 资源域变更广播
			rqFactory.createTopicFlux(EventTopicConstants.SIMO_NE_DOMAIN_NOTIFY, JSONObject.class).subscribe(
					JMSFlux.Catch(authorityHandler::handleDomainChange, LoggerFactory.getLogger(LeaderViewAuthorityHandler.class))
			);

			// 订阅工作移交
			rqFactory.createTopicFlux(EventTopicConstants.SIMO_HANDOVER_NOTIFY, String.class).subscribe(
					JMSFlux.Catch(authorityService::handOver, LoggerFactory.getLogger(AuthorityService.class)));
		} catch (JMSException e) {
			logger.error("mq 域变更订阅异常: {}", e);
		}
	}


}
