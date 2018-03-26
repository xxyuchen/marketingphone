package com.geeker.marketing.listener;

import com.geeker.marketing.dao.micro.generator.model.OpDeviceCmd;
import com.geeker.marketing.netty.ClientHolder;
import com.geeker.marketing.service.OpDeviceCmdService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author TangZhen
 * @Date 2018/3/22 0022 09:53
 * @Description 设备连接校验是否有未发送指令事件
 */
@Component
@Slf4j
public class DeviceConnectListener implements ApplicationListener<ClientHolder.AddClientEvent> {
    @Resource
    private OpDeviceCmdService opDeviceCmdService;

    @Resource(name = "cmdProducer")
    private DefaultMQProducer cmdProducer;


    @Override
    public void onApplicationEvent(ClientHolder.AddClientEvent addClientEvent) {
        try {
            String deviceId = addClientEvent.getDeviceId();
            List<OpDeviceCmd> opDeviceCmds = opDeviceCmdService.notIssuedCmd(deviceId);
            for (OpDeviceCmd cmd : opDeviceCmds) {
                Message message = new Message("op_device_cmd", cmd.getCmdCd(),cmd.getId()+"#"+cmd.getDeviceId(),cmd.getCmdParm().getBytes());
                cmdProducer.send(message);
                log.info("MarketingPhone:向【{}】发送的类型为【{}】指令【{}】入队列...",cmd.getDeviceId(),cmd.getCmdCd(),cmd.getCmdParm());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

