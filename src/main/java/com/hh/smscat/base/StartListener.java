package com.hh.smscat.base;

import com.hh.smscat.server.SMSCatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

@Component
public class StartListener implements ApplicationListener<ContextRefreshedEvent> {
    private Logger log = LoggerFactory.getLogger(StartListener.class);
    @Autowired
    private SMSCatServer smsCatServer;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            System.out.println("项目已启动，可以进行初始化操作");
            Executors.newFixedThreadPool(1).submit(smsCatServer);

        } catch (Exception e) {
            log.error("启动执行失败，请检查Spring版本是否支持:"+e.getMessage(),e);
        }
    }

}