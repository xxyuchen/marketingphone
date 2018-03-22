package com.geeker.marketing.rocketmq;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Administrator on 2017-06-15.
 */
@Getter
@Setter
@ConfigurationProperties(RocketMQProperties.PREFIX)
public class RocketMQProperties {

    public static final String PREFIX = "rocketMQ";

    private String namesrvAddr;
    private String instanceName;
    private String clientIP;
    private ProducerConfig producer;
    private ConsumerConfig consumer;
}
