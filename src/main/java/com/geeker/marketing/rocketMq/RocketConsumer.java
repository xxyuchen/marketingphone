package com.geeker.marketing.rocketMq;

import com.alibaba.fastjson.JSON;
import com.geeker.marketing.conf.RocketConf;
import com.geeker.marketing.dao.micro.generator.model.OpDeviceCmd;
import com.geeker.marketing.dao.micro.generator.model.OpDeviceReport;
import com.geeker.marketing.enums.CmdEnum;
import com.geeker.marketing.netty.ClientHolder;
import com.geeker.marketing.netty.NettyUtil;
import com.geeker.marketing.service.OpDeviceCmdService;
import com.geeker.marketing.service.OpDeviceReportService;
import com.geeker.marketing.vo.ReportCmdVo;
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
import java.util.Date;
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
    private OpDeviceReportService opDeviceReportService;

    @Resource
    private ClientHolder clientHolder;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.profiles.active:default}")
    private String activeProfiles;

    @Value("${spring.rocketmq.topic.report-topic}")
    private String reportTopic;

    @Value("${spring.rocketmq.topic.issue-topic}")
    private String issueTopic;

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

        consumer.subscribe(issueTopic, "*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageExt msg = list.get(0);
                String body = new String(msg.getBody());
                String deviceId = msg.getTags();
                String id = msg.getKeys();
                log.info("从队列中取出topIc:【{}】,deviceId:【{}】,id:【{}】,body:【{}】", msg.getTopic(), deviceId, id, body);
                Channel channel = clientHolder.getClient(deviceId);
                if (null != channel) {
                    NettyUtil.sendMessage(channel, body).addListener(future -> {
                        Integer status = null;
                        String result = null;
                        //更新指令状态
                        if (future.isSuccess()) {
                            status = CmdEnum.DeliverStatusEnum.DO.getCode();
                        } else {
                            log.info("指令下发失败【{}】...",id);
                            status = CmdEnum.DeliverStatusEnum.FAIL.getCode();
                            result = "指令下发失败";
                        }
                        log.info("op_device_cmd 指令状态更新【{}】", status);
                        opDeviceCmdService.updateDeliverStatus(id, status, result);
                    });
                } else {
                    log.info("客户端不在线【{}】", deviceId);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        log.info("cmdConsumer started....");
        this.pushConsumer = consumer;
        return this.pushConsumer;

    }
    /**
     * 初始化rocketmq消息监听方式的消费者(上报消息处理)
     */
    @Bean(destroyMethod = "shutdown")
    @Lazy(false)
    @ConditionalOnMissingBean(value = MQPushConsumer.class, name = "reporstConsumer")
    public MQPushConsumer reporstConsumer() throws MQClientException {
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

        consumer.subscribe(reportTopic, "*");

        consumer.registerMessageListener((List<MessageExt> msgs, ConsumeConcurrentlyContext context) -> {
            MessageExt msg = msgs.get(0);
            try {
                ReportCmdVo vo = JSON.parseObject(new String(msg.getBody()),ReportCmdVo.class);
                vo.setDeviceId(msg.getKeys());
                vo.setQueue(msg.getTopic());
                vo.setMessageId(msg.getMsgId());
                opDeviceReportService.dealReportCmd(vo);
            } catch (Exception e) {
                log.error("消息处理异常 [{}:{}]", msg.getTopic(), msg.getMsgId(), e);
                if (msg.getReconsumeTimes()>=3) {//重复消费3次
                    log.warn("该消息重复消费（失败）超过3次【{}】",msg.getMsgId());
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            //如果没有return success，consumer会重复消费此信息，直到success。
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        log.info("reporstConsumer started....");
        this.pushConsumer = consumer;
        return this.pushConsumer;

    }
}
