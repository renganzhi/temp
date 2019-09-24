package com.uxsino.leaderview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.uxsino.reactorq.commons.ReactorQFactory;
import com.uxsino.reactorq.subscriber.EventSubscriber;

@Configuration
public class LeaderVIewConf {
	@Autowired
	@Qualifier("default_reactorq_factory")
	private ReactorQFactory rqFactory;

	@Bean(name = "outbox_lv")
	public EventSubscriber outBox() {
		return new EventSubscriber(rqFactory);
	}

}
