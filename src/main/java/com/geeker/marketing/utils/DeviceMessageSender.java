package com.geeker.marketing.utils;

import com.geeker.marketing.netty.NettyUtil;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by Lubin.Xuan on 2018-02-06.
 * {desc}
 */
@Component
public class DeviceMessageSender {

    public String send(Channel channel, String action, String data) {
        //指令id
        String id = "";

        NettyUtil.sendMessage(channel, action, id, data);
        return id;
    }
}
