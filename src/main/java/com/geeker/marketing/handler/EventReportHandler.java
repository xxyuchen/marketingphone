package com.geeker.marketing.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
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

    @Override
    public void process(Channel channel, String clientId, JSONObject deviceMessage) {
        String eventType = deviceMessage.getString("event");
        String date = deviceMessage.getString("data");
        JSONObject json = JSON.parseObject(date);
        json.put("queueTime",new Date());
        log.info("接收到事件消息[{}:{}]", eventType, date);
        //丢入队列
        Message message = new Message("report_cmd", eventType,clientId,json.toJSONString().getBytes());
/*        if(eventType.equals("")){ //主动上报指令
            message = new Message("init_report_cmd", eventType,clientId,json.toJSONString().getBytes());
        }else if (eventType.equals("")){ //下发指令执行状况回调
            message = new Message("issue_report_cmd", eventType,clientId,json.toJSONString().getBytes());
        }*/
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
