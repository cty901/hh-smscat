import com.hh.smscat.entity.BroadCastItem;
import com.hh.smscat.enums.SMSCatTypeEnum;
import com.hh.smscat.utils.DomXmlUtil;
import com.hh.smscat.utils.OkHttpUtil;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.http.MediaType;
import org.w3c.dom.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 2019/7/11 14:00
 *
 * @author owen pan
 */
public class MainTest {
    public static void main(String[] args) {

//        //post
//        RequestBody requestBody = new FormBody.Builder()
//                .add("type", SMSCatTypeEnum.HANGBIAO.getType())
//                .add("phone","13456816223")
//                .add("msg","AIN+2040+2378+0+0+0+2704+456+621mV$GPRMC,042611.00,A,2930.12237,N,11851.71412,E,0.002,,110719,,,A* 55359 24")
//                .build();
//       String  result = OkHttpUtil.newInstance().sendBySync(new Request.Builder().url("http://192.168.1.128:8002/smscat/hangbiao").post(requestBody).build());
//
//        System.out.println(result);
        new MainTest().broadCast(SMSCatTypeEnum.HANGBIAO,"13456816223","AIN+2040+2378+0+0+0+2704+456+621mV$GPRMC,042611.00,A,2930.12237,N,11851.71412,E,0.002,,110719,,,A* 55359 24");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void broadCast(SMSCatTypeEnum type, final String phone, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getBroadCastItems().forEach(it -> {
                    if (it.getEnable()) {
                        String str = OkHttpUtil.newInstance().sendBySync(
                                new Request.Builder()
                                        .url(it.getUrl())
                                        .post(
                                                new FormBody.Builder().add("type",type.getType()).add("phone", phone).add("msg", msg).build()
                                        ).build()
                        );
                        System.out.println("broadcast [" + phone + "]: " + str);
                    }
                });
            }
        }).start();
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
