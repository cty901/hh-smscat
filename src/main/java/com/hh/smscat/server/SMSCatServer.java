package com.hh.smscat.server;

/**
 * 2019/7/9 19:29
 *
 * @author owen pan
 * <p>
 * Copyright(C) 2012 GZ ISCAS ALL Rights Reserved
 */

import com.hh.smscat.base.common.SnowflakeIdWorker;
import com.hh.smscat.controller.UserController;
import com.hh.smscat.entity.SmsCatSendLog;
import com.hh.smscat.service.ISmsCatSendLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.AGateway;
import org.smslib.InboundMessage;
import org.smslib.Message.MessageEncodings;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.modem.SerialModemGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 描述：SMS Cat服务类
 */
@Component
public class SMSCatServer implements Runnable {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private static ConcurrentLinkedQueue<InboundMessage> msgList = new ConcurrentLinkedQueue<>();
    /**
     * 是否开启服务
     */
    private AtomicBoolean isStartService = new AtomicBoolean(false);
    @Value("${smscat.start.type}")
    private String startType = "X64";
    /**
     * 串口号
     */
    @Value("${smscat.comNo}")
    private String comNo;
    /**
     * 波特率
     */
    @Value("${smscat.baudRate}")
    private Integer baudRate;
    /**
     * sim卡锁，一般默认为0000或1234
     */
    @Value("${smscat.simPin}")
    private String simPin;
    @Autowired
    private MyInboundNotification myInboundNotification;
    @Autowired
    private ISmsCatSendLogService iSmsCatSendLogService;

    @Override
    public void run() {
        //获得当前所有可用串口
        ArrayList<String> portNameList = new ArrayList<>();
        if("X64".equals(startType.toUpperCase())) {
            Enumeration<gnu.io.CommPortIdentifier> portList = gnu.io.CommPortIdentifier.getPortIdentifiers();
            while (portList.hasMoreElements()) {
                String portName = portList.nextElement().getName();
                portNameList.add(portName);
            }
            logger.info("x64可用串口：" + portNameList);
        }
        if("X86".equals(startType.toUpperCase())) {
//            Enumeration<javax.comm.CommPortIdentifier> portList = javax.comm.CommPortIdentifier.getPortIdentifiers();
//            while (portList.hasMoreElements()) {
//                String portName = portList.nextElement().getName();
//                portNameList.add(portName);
//            }
//            System.out.println("x86可用串口：" + portNameList);
        }
        if(portNameList.size()<=0){
            logger.info("无可用串口，不开启短信猫接收服务");
        }else {
            startService();
        }
    }

    public void startService() {
        logger.info("开始初始化SMS服务x86！[modem.com" + comNo + "][COM" + comNo + "][" + baudRate + "][Huihang][CT]");
        // 初始化网关，参数信息依次为：COMID,COM号,比特率,制造商,Modem模式
        SerialModemGateway gateway = new SerialModemGateway(
                "modem.com" + comNo,
                "COM" + comNo,
                baudRate,
                "Huihang",
                "CT"
        );
        //设置网关协议
        gateway.setProtocol(AGateway.Protocols.PDU);
        gateway.setInbound(true);
        gateway.setOutbound(true);
        gateway.setSimPin(simPin);

        Service service = Service.getInstance();
        if (service == null) {
            System.out.println("初始化SMS服务失败！");
            return;
        }
        try {
//            service.getSettings().SERIAL_POLLING = true;
            service.setOutboundMessageNotification(new MyOutboundNotification());
            service.setInboundMessageNotification(myInboundNotification);
            service.addGateway(gateway);
            service.startService();
            logger.info("初始化SMS服务成功!");
            logger.info("Modem设备信息:");
            logger.info("  Manufacturer: " + gateway.getManufacturer());
            logger.info("  Model: " + gateway.getModel());
            logger.info("  Serial No: " + gateway.getSerialNo());
            logger.info("  SIM IMSI: " + gateway.getImsi());
            logger.info("  Signal Level: " + gateway.getSignalLevel() + " dBm");
            logger.info("  Battery Level: " + gateway.getBatteryLevel() + "%");
            isStartService.set(true);

            Executors.newFixedThreadPool(1).submit(new Runnable() {
                @Override
                public void run() {
                    while (isStartService.get()) {
                        System.out.println("\nheart check,message size: " + msgList.size());
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            while (isStartService.get()) {
                System.out.print(">");
                try {
                    while (service.readMessages(msgList, InboundMessage.MessageClasses.ALL) > 0) {

                        while (msgList.size() > 0) {
                            InboundMessage msg = msgList.poll();
                            myInboundNotification.process(gateway, msg.getType(), msg);
                        }
                    }
                } catch (Exception e) {
                    logger.error("receive message catch a exception: " + e.getMessage());
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("开启SMS服务异常:" + e.getMessage());
        } finally {
            try {
                service.stopService();
            } catch (Exception e) {
                logger.warn("关闭SMS服务异常:" + e.getMessage());
            }
            isStartService.set(false);
        }
    }


    /**
     * 发送短信
     *
     * @param phone   手机号码
     * @param message 短信内容
     */
    public String sendMessage(String phone, String message) {
        SmsCatSendLog smsCatSendLog = new SmsCatSendLog();
        smsCatSendLog.setId(SnowflakeIdWorker.newInstance().nextStringId());
        smsCatSendLog.setMsg(message);
        smsCatSendLog.setPhoneid(phone);
        if (!isStartService.get()) {
            logger.error("尚未开启SMS服务！");
            smsCatSendLog.setResult("{\"code\":500,\"msg\":\"尚未开启SMS-Cat服务\"}");
        } else {
            // 封装信息
            OutboundMessage msg = new OutboundMessage(phone, message);
            msg.setEncoding(MessageEncodings.ENCUCS2);
            try {
                // 发送信息
                Service.getInstance().sendMessage(msg);
                logger.info("<<< 发送短信[" + phone + "]: " + message);
                smsCatSendLog.setResult("{\"code\":200,\"msg\":\"\"}");
            } catch (Exception e) {
                logger.error("SMS服务发送信息发生异常:" + e.getMessage());
                smsCatSendLog.setResult("{\"code\":500,\"msg\":\"" + e.getMessage() + "\"}");
            }
        }
        iSmsCatSendLogService.save(smsCatSendLog);
        return smsCatSendLog.getResult();
    }


}