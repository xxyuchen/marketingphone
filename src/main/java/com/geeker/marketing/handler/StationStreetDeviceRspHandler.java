package com.geeker.marketing.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author TangZhen
 * @Date 2018/3/9 0009 16:21
 * @Description 定位站街任务
 */
@Component
@Slf4j
public class StationStreetDeviceRspHandler extends BasicDeviceRspHandler {

    @Override
    public void process(Channel channel, String clientId, JSONObject deviceMessage) {
        //todo 保存站街信息
    }

    @Override
    public String actionName() {
        return "StationStreet";
    }

}
