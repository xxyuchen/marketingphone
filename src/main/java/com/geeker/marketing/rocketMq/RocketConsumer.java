package com.geeker.marketing.rocketMq;

import com.geeker.marketing.conf.RocketConf;
import com.geeker.marketing.enums.CmdEnum;
import com.geeker.marketing.netty.ClientHolder;
import com.geeker.marketing.netty.NettyUtil;
import com.geeker.marketing.service.OpDeviceCmdService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import me.robin.spring.rocketmq.ConsumerConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/3/22 0022.
 */
@Configuration
@Slf4j
public class RocketConsumer {
    @Resource
    private RocketConf rocketConf;

    private MQPushConsumer pushConsumer;

    @Resource
    private OpDeviceCmdService opDeviceCmdService;

    @Resource
    private ClientHolder clientHolder;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.profiles.active:default}")
    private String activeProfiles;

    /**
     * 初始化rocketmq消息监听方式的消费者
     */
    @Bean(destroyMethod = "shutdown")
    @Lazy(false)
    @ConditionalOnMissingBean(value = MQPushConsumer.class, name = "cmdConsumer")
    public MQPushConsumer cmdConsumer() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(rocketConf.getConsumer().getGroup(applicationName) + "_" + activeProfiles);
        consumer.setNamesrvAddr(rocketConf.getNamesrvAddr());
        consumer.setMessageModel(rocketConf.getConsumer().getMessageModel());
        if (StringUtils.isNotBlank(rocketConf.getConsumer().getInstanceName())) {
            consumer.setInstanceName(rocketConf.getConsumer().getInstanceName());
        }
        ConsumerConfig consumerConfig = rocketConf.getConsumer();
        //consumer.setConsumeMessageBatchMaxSize(1);//设置批量消费，以提升消费吞吐量，默认是1
        consumer.setConsumeThreadMin(consumerConfig.getMinConsumers());
        consumer.setConsumeThreadMax(consumerConfig.getMaxConsumers());

        consumer.subscribe("op_device_cmd", "*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageExt msg = list.get(0);
                String body = new String(msg.getBody());
                String[] keys = msg.getKeys().split("#");
                String deviceId = keys[1];
                String id = keys[0];
                log.info("从队列中取出topIc:【{}】,deviceId:【{}】,tags:【{}】,body:【{}】", msg.getTopic(), deviceId, msg.getTags(), body);
                Channel channel = clientHolder.getClient(deviceId);
                if (null != channel) {
                    NettyUtil.sendMessage(channel, body).addListener(future -> {
                        Integer status = null;
                        //更新指令状态
                        if (future.isSuccess()) {
                            status = CmdEnum.DeliverStatusEnum.DO.getCode();
                        } else {
                            status = CmdEnum.DeliverStatusEnum.FAIL.getCode();
                        }
                        log.info("op_device_cmd 指令状态更新【{}】-》【{}】", status, future.cause().getLocalizedMessage());
                        opDeviceCmdService.updateDeliverStatus(id, status, future.cause().getLocalizedMessage());
                    });
                } else {
                    log.info("客户端不在线【{}】", deviceId);
                    //return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        log.info("cmdConsumer started....");
        this.pushConsumer = consumer;
        return this.pushConsumer;

    }
}
