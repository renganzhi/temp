package com.uxsino.leaderview;

import javax.jms.JMSException;

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

	public void init() {
		_outbox.setFrom("leaderview");
		ModuleOnLineEvent ev = new ModuleOnLineEvent();
		ev.Module = "viewer";
		ev.id = "";
		ev.onLineTimeMillis = System.currentTimeMillis();
		_outbox.onNext(ev);
		// 初始化主页大屏的模板信息
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

}
