package com.geeker.marketing.rocketmq;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.rocketmq.client.consumer.MQConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.context.ApplicationEvent;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017-06-15.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class RocketMQEvent extends ApplicationEvent {
    private static final long serialVersionUID = -4468405250074063206L;

    private MQConsumer consumer;
    private MessageExt messageExt;
    private String topic;
    private String tag;
    private byte[] body;

    public RocketMQEvent(MessageExt msg, MQConsumer consumer) {
        super(msg);
        this.topic = msg.getTopic();
        this.tag = msg.getTags();
        this.body = msg.getBody();
        this.consumer = consumer;
        this.messageExt = msg;
    }

    public RocketMQEvent(MessageExt msg) {
        this(msg, null);
    }

    public String getMsg() {
        try {
            return new String(this.body, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public String getMsg(String code) {
        try {
            return new String(this.body, code);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

}