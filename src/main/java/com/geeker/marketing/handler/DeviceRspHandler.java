package com.geeker.marketing.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;

/**
 * Created by Lubin.Xuan on 2017-11-06.
 * {desc}
 */
public interface DeviceRspHandler {
    void process(Channel channel, String clientId, JSONObject deviceMessage);
}
