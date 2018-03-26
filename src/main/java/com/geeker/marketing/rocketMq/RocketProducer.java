package com.geeker.marketing.rocketMq;

import com.geeker.marketing.conf.RocketConf;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/3/22 0022.
 */
@Configuration
@Slf4j
public class RocketProducer {

    @Resource
    private RocketConf rocketConf;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.profiles.active:default}")
    private String activeProfiles;

    /**
     * 初始化向rocketmq下发指令的生产者
     */
    @Bean(destroyMethod = "shutdown")
    @Lazy
    @ConditionalOnMissingBean(value = MQProducer.class, name = "cmdProducer")
    public DefaultMQProducer cmdProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(rocketConf.getConsumer().getGroup(applicationName) + "_" + activeProfiles);
        producer.setNamesrvAddr(rocketConf.getNamesrvAddr());
        if (StringUtils.isNotBlank(rocketConf.getProducer().getInstanceName())) {
            producer.setInstanceName(rocketConf.getProducer().getInstanceName());
        }
        /*if (StringUtils.isNotBlank(rocketConf.getProducer().getGroup())) {
            producer.setProducerGroup(rocketConf.getProducer().getGroup());
        }*/
        producer.setVipChannelEnabled(false);
        //producer.setRetryTimesWhenSendFailed(3);//失败的重新发送

        producer.start();
        log.info("RocketMq cmdProducer Started.");
        return producer;
    }
}
