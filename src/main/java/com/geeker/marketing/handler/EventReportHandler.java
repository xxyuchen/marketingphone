package com.geeker.marketing.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by Lubin.Xuan on 2018-03-22.
 * {desc}
 */
@Component
@Slf4j
public class EventReportHandler extends BasicDeviceRspHandler {

    @Override
    public void process(Channel channel, String clientId, JSONObject deviceMessage) {
        String eventType = deviceMessage.getString("event");
        String date = deviceMessage.getString("data");
        log.info("接收到事件消息[{}:{}]", eventType, date);
    }

    @Override
    public String actionName() {
        return "reportEvent";
    }
}
