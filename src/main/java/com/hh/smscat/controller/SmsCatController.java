package com.hh.smscat.controller;

import com.hh.smscat.base.common.BaseController;
import com.hh.smscat.server.SMSCatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/smsCat")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class SmsCatController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(SmsCatController.class);
    @Autowired
   private SMSCatServer smsCatServer;

    @RequestMapping("/command")
    public String login(String phone,String command) {
        logger.info("发送命令["+phone+"]["+command+"]");
        return smsCatServer.sendMessage(phone,command);
    }

}