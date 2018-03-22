package com.geeker.marketing.listener;

import com.geeker.marketing.dao.micro.generator.model.OpDeviceCmd;
import com.geeker.marketing.netty.ClientHolder;
import com.geeker.marketing.service.OpDeviceCmdService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


/**
* @Author TangZhen
* @Date 2018/3/22 0022 09:53
* @Description  设备连接校验是否有未发送指令事件
*/
@Component
public class DeviceConnectListener implements ApplicationListener<ClientHolder.AddClientEvent> {
    @Resource
    private OpDeviceCmdService opDeviceCmdService;

    @Override
    public void onApplicationEvent(ClientHolder.AddClientEvent addClientEvent) {
        List<OpDeviceCmd> opDeviceCmds = opDeviceCmdService.notIssuedCmd(addClientEvent.getDeviceId());
        //TODO 下发指令入队列
    }
}

