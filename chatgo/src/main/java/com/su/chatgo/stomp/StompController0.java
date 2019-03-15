package com.su.chatgo.stomp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Created by XiuYin.Cui on 2018/5/2.
 */
@RestController
public class StompController0 {


    private static final Logger LOGGER = LoggerFactory.getLogger(StompController0.class);

    /**
     * 默认情况下，帧所发往的目的地会与触发处理器方法的目的地相同，只不过会添加上“/topic”前缀。
     *
     * @param shout
     */
//    @MessageMapping("/marco")
//    @SendTo("/topic/marco") //可重写目的地，@MessageMapping 会发送到消息代理，客户端再从消息代理订阅
//    public LocalDateTime stompHandle(Shout shout) {
//        LOGGER.info("接收到消息：" + shout.getMessage());
//        Shout s = new Shout();
//        s.setMessage("Polo!");
//        LocalDateTime localDateTime = LocalDateTime.now();
//        return localDateTime;
//    }

//    https://blog.csdn.net/yingxiake/article/details/51224569
    @MessageMapping("/marco") //MessageMapping默认直接回复，加了sendto则会经过代理
//    @SendToUser("/topic/marco") //可重写目的地，@MessageMapping 会发送到消息代理，客户端再从消息代理订阅
//    @SendTo("/topic/marco")
    public LocalDateTime stompHandle0(Shout shout) {
        LOGGER.info("接收到消息：" + shout.getMessage());
        Shout s = new Shout();
        s.setMessage("Polo!");
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime;
    }


    @Autowired
    private SimpMessagingTemplate template;

    @GetMapping("/test")
    public Object test(String sha) {
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", sha);
        array.add(jsonObject);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("name", "ddd");
        array.add(jsonObject1);

//        template.convertAndSend("/topic/marco", array);
        template.convertAndSendToUser("123456789", "/topic/marco", array);

        return LocalDateTime.now();
    }

    /**
     * @return
     * @SubscribeMapping 的主要应用场景是实现请求-回应模式。
     * 在请求-回应模式中，客户端订阅某一个目的地，然后预期在这个目的地上获得一个一次性的响应。
     */
    @SubscribeMapping("/getShout")
    //@SendTo("/app/marco") // @SubscribeMapping 默认直接返回给客户端,如果你加了SendTo的话则要经过代理
    public Object getShout() {
        return "Hello STOMP";
    }
}
