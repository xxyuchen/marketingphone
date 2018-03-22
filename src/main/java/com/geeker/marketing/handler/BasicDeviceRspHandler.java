package com.geeker.marketing.handler;

import com.geeker.marketing.netty.ServerHandler;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by Lubin.Xuan on 2017-11-06.
 * {desc}
 */
@Lazy(false)
public abstract class BasicDeviceRspHandler implements DeviceRspHandler {

    @Resource
    private ServerHandler serverHandler;

    @PostConstruct
    private void init() {
        this.serverHandler.addHandler(actionName(), this);
    }

    public abstract String actionName();

}
