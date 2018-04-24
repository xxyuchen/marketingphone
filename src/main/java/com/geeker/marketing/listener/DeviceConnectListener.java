package com.geeker.marketing.listener;

import com.alibaba.fastjson.JSONObject;
import com.geeker.marketing.dao.micro.generator.model.OpDeviceCmd;
import com.geeker.marketing.netty.ClientHolder;
import com.geeker.marketing.service.OpDeviceCmdService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Value;
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
public class DeviceConnectListener implements ApplicationListener<PublicEvent.AddClientEvent> {
    @Resource
    private OpDeviceCmdService opDeviceCmdService;

    @Resource(name = "cmdProducer")
    private DefaultMQProducer cmdProducer;

    @Value("${spring.rocketmq.topic.issue-topic}")
    private String issueTopic;


    @Override
    public void onApplicationEvent(PublicEvent.AddClientEvent addClientEvent) {
        try {
            String deviceId = addClientEvent.getDeviceId();
            List<OpDeviceCmd> opDeviceCmds = opDeviceCmdService.notIssuedCmd(deviceId);
            log.info("MarketingPhone:设备【{}】有【{}】条指令未下发...",deviceId,opDeviceCmds.size());
            for (OpDeviceCmd cmd : opDeviceCmds) {
                Map<String,Object> map = new HashMap<>(8);
                map.put("cmdId",cmd.getId());
                map.put("cmdParm",cmd.getCmdParm());
                map.put("cmdCd",cmd.getCmdCd());
                map.put("cmdTypeCd",cmd.getCmdTypeCd());
                map.put("deviceId",cmd.getDeviceId());
                map.put("userId",cmd.getUserId());
                Message message = new Message(issueTopic, cmd.getDeviceId(),cmd.getId(), JSONObject.toJSONString(map).getBytes());
                cmdProducer.send(message);
                log.info("MarketingPhone:指令入队列【{}】【{}】【{}】【{}】...",cmd.getId(),cmd.getDeviceId(),cmd.getCmdCd(),cmd.getCmdParm());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

