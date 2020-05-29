package com.hh.smscat.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.AGateway;
import org.smslib.IOutboundMessageNotification;
import org.smslib.OutboundMessage;


public class MyOutboundNotification implements IOutboundMessageNotification {
	private Logger log = LoggerFactory.getLogger(MyOutboundNotification.class);

	public void process(AGateway gateway, OutboundMessage msg) {
		  log.info("发出成功后的函数回调");
	}

}