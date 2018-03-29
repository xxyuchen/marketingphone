package com.geeker.marketing.listener;

import com.alibaba.fastjson.JSONObject;
import com.geeker.marketing.dao.micro.generator.model.OpDeviceCmd;
import com.geeker.marketing.enums.CmdEnum;
import com.geeker.marketing.service.OpDeviceCmdService;
import com.geeker.marketing.vo.DeviceCmdVo;
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
public class IssueCmdListener implements ApplicationListener<PublicEvent.IssueCmdEvent> {
    @Resource
    private OpDeviceCmdService opDeviceCmdService;

    @Resource(name = "cmdProducer")
    private DefaultMQProducer cmdProducer;

    @Value("${spring.rocketmq.topic.issue-topic}")
    private String issueTopic;


    @Override
    public void onApplicationEvent(PublicEvent.IssueCmdEvent issueCmdEvent) {
        try {
            String cmdId = issueCmdEvent.getCmdId();
            OpDeviceCmd opDeviceCmd = opDeviceCmdService.getByCmdId(cmdId);
            if(null==opDeviceCmd){
                log.info("MarketingPhone:指令不存在！【{}】",cmdId);
                return;
            }
            if(!opDeviceCmd.getDeliverStatus().equals(CmdEnum.DeliverStatusEnum.UNDO.getCode())){
                log.info("MarketingPhone:指令已下发！【{}】",cmdId);
                return;
            }
            log.info("MarketingPhone:下发指令【{}】#【{}】...", cmdId, opDeviceCmd.getDeviceId());
            DeviceCmdVo cmdVo = new DeviceCmdVo();
            cmdVo.setCmdCd(opDeviceCmd.getCmdCd());
            cmdVo.setCmdId(cmdId);
            cmdVo.setCmdParm(opDeviceCmd.getCmdParm());
            cmdVo.setCmdTypeCd(opDeviceCmd.getCmdTypeCd());
            Message message = new Message(issueTopic, opDeviceCmd.getDeviceId(), opDeviceCmd.getId(), JSONObject.toJSON(cmdVo).toString().getBytes());
            cmdProducer.send(message);
            log.info("MarketingPhone:指令入队列【{}】【{}】【{}】【{}】...",opDeviceCmd.getId(),opDeviceCmd.getDeviceId(),opDeviceCmd.getCmdCd(),opDeviceCmd.getCmdParm());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

