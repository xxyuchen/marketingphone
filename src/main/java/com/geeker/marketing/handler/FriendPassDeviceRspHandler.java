package com.geeker.marketing.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
* @Author TangZhen
* @Date 2018/3/9 0009 16:21
* @Description  好友自动通过
*/
@Component
@Slf4j
public class FriendPassDeviceRspHandler extends BasicDeviceRspHandler {

    @Override
    public void process(Channel channel, String clientId, String deviceMessage) {
        JSONObject json = JSON.parseObject(deviceMessage);
        log.info("设备【{}】--通过好友信息：【{}】",clientId,json);

        //todo 存储好友信息

    }

    @Override
    public String actionName() {
        return "FriendPass";
    }
}
