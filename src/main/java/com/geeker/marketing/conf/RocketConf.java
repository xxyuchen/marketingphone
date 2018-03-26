package com.geeker.marketing.conf;

import lombok.Data;
import me.robin.spring.rocketmq.ConsumerConfig;
import me.robin.spring.rocketmq.ProducerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/3/22 0022.
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.rocketmq")
public class RocketConf {
    private String namesrvAddr;
    private String instanceName;
    private String clientIP;
    private ProducerConfig producer;
    private ConsumerConfig consumer;
}
