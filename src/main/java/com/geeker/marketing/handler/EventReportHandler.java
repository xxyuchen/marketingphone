package com.geeker.marketing.handler;

import com.alibaba.fastjson.JSONObject;
import com.geeker.marketing.vo.ReportCmdVo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Lubin.Xuan on 2018-03-22.
 * {desc}
 */
@Component
@Slf4j
public class EventReportHandler extends BasicDeviceRspHandler {

    @Resource(name = "cmdProducer")
    private DefaultMQProducer cmdProducer;

    @Value("${spring.rocketmq.topic.report-topic}")
    private String reportTopic;

    @Override
    public void process(Channel channel, String clientId, JSONObject deviceMessage) {
        log.info("ReportCmd:消息上报==》【{}】",deviceMessage);
        ReportCmdVo vo = deviceMessage.toJavaObject(ReportCmdVo.class);
        vo.setQueueTime(new Date());
        //丢入队列
        log.info("ReportCmd:上报消息入队列==》【{}】：【{}】：【{}】",vo.getRspAction(),vo.getCmdCd(),vo.getData());
        Message message = new Message(reportTopic, vo.getRspAction(),clientId,JSONObject.toJSON(vo).toString().getBytes());
        try {
            cmdProducer.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String actionName() {
        return "reportEvent";
    }
}
