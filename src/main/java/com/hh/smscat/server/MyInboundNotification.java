package com.hh.smscat.server;

import com.hh.smscat.base.common.SnowflakeIdWorker;
import com.hh.smscat.entity.BroadCastItem;
import com.hh.smscat.entity.SmsCatReceiveLog;
import com.hh.smscat.enums.SMSCatTypeEnum;
import com.hh.smscat.service.ISmsCatReceiveLogService;
import com.hh.smscat.utils.DomXmlUtil;
import com.hh.smscat.utils.OkHttpUtil;
import okhttp3.FormBody;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smslib.AGateway;
import org.smslib.IInboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.Message.MessageTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Component
public class MyInboundNotification implements IInboundMessageNotification {
    public static Logger log = LoggerFactory.getLogger(MyInboundNotification.class);
    @Autowired
    private ISmsCatReceiveLogService iSmsCatReceiveLogService;
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private OkHttpUtil okHttpUtil = OkHttpUtil.newInstance();

    @Override
    public void process(AGateway gateway, MessageTypes msgType, InboundMessage msg) {
        log.debug("=============================================================");
        if (msgType == MessageTypes.INBOUND) {
            log.debug(">>> 收到短信[" + msg.getOriginator() + "]: " + msg.getText().trim());
            try {
                inController(msg.getOriginator(), msg.getText());
            } catch (Exception r) {
                log.debug(">>> 内容未知,抛弃:" + r.getMessage(), r);
            }
        }
        try {
            gateway.deleteMessage(msg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void inController(String phoneid, String mesgtxt) {
        SmsCatReceiveLog smsCatReceiveLog = new SmsCatReceiveLog();
        smsCatReceiveLog.setId(SnowflakeIdWorker.newInstance().nextStringId());
        smsCatReceiveLog.setMsg(mesgtxt);
        smsCatReceiveLog.setPhoneid(phoneid);
        if (mesgtxt.contains("GPRMC")) {
            if(mesgtxt.contains("AIN")){
                System.out.print("H");
                smsCatReceiveLog.setType(SMSCatTypeEnum.HANGBIAO.getType());
                broadCast(SMSCatTypeEnum.HANGBIAO,phoneid,mesgtxt);
            }else {
                System.out.print("M");
                smsCatReceiveLog.setType(SMSCatTypeEnum.MOTUOTING.getType());
                broadCast(SMSCatTypeEnum.MOTUOTING, phoneid, mesgtxt);
            }
        } else if (mesgtxt.contains("GPRMC2")) {
            System.out.print("H");
            smsCatReceiveLog.setType(SMSCatTypeEnum.HANGBIAO.getType());
            broadCast(SMSCatTypeEnum.HANGBIAO,phoneid,mesgtxt);
        } else {
            System.out.print("?");
            smsCatReceiveLog.setType(SMSCatTypeEnum.WEIZHI.getType());
            broadCast(SMSCatTypeEnum.WEIZHI,phoneid,mesgtxt);
        }
        iSmsCatReceiveLogService.save(smsCatReceiveLog);
    }

    private void broadCast(SMSCatTypeEnum type, final String phone, final String msg) {
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                getBroadCastItems().forEach(it -> {
                    if (it.getEnable()) {
                        String str = okHttpUtil.sendBySync(
                                new Request.Builder()
                                        .url(it.getUrl())
                                        .post(
                                                new FormBody.Builder().add("type",type.getType()).add("phone", phone).add("msg", msg).build()
                                        ).build()
                        );
                        log.debug("broadcast [" + phone + "]: " + str);
                    }
                });
            }
        });
    }

    private List<BroadCastItem> getBroadCastItems() {
        String str = null;
        try {
            str = new File("").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DomXmlUtil domXmlUtil = new DomXmlUtil(new File(str.replaceAll("\\\\", "/") + "/broadcast.xml"));
        return domXmlUtil.parseXml(new DomXmlUtil.BaseDomXmlParser<List<BroadCastItem>>() {
            @Override
            protected List<BroadCastItem> parse(Document doc) {
                List<BroadCastItem> list = new ArrayList<>();
                Element root = doc.getDocumentElement();
                NodeList items = root.getElementsByTagName("broadcast-item");
                for (int i = 0; i < items.getLength(); i++) {
                    Element item = (Element) items.item(i);
                    if (item.hasAttributes()) {
                        list.add(new BroadCastItem(Boolean.parseBoolean(item.getAttribute("enable")), item.getAttribute("url")));
                    }

                }
                return list;
            }
        });
    }

}