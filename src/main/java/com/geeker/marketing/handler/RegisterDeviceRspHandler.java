package com.geeker.marketing.handler;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by Lubin.Xuan on 2017-11-06.
 * {desc}
 */
@Component
@Slf4j
public class RegisterDeviceRspHandler extends BasicDeviceRspHandler {

    @Override
    public void process(Channel channel, String clientId, String deviceMessage) {

    }

    @Override
    public String actionName() {
        return "REGISTER";
    }
}
