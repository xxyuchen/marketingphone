package com.geeker.marketing.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPullConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * Created by Administrator on 2017-06-15.
 */
@Configuration
@EnableConfigurationProperties(RocketMQProperties.class)
@ConditionalOnProperty(prefix = RocketMQProperties.PREFIX, value = "namesrvAddr")
@Slf4j
public class RocketMQAutoConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger errorMsgLogger = LoggerFactory.getLogger("RocketMQConsumeErrorLogger");

    @Resource
    private RocketMQProperties properties;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.profiles.active:default}")
    private String activeProfiles;

    @Resource
    private ApplicationEventPublisher publisher;

    private MQPushConsumer pushConsumer;

    private volatile boolean pushConsumerStart = false;
    private volatile boolean applicationStart = false;

    /**
     * 初始化向rocketmq发送普通消息的生产者
     */
    @Bean(destroyMethod = "shutdown")
    @Lazy
    @ConditionalOnMissingBean(value = MQProducer.class, name = "mqProducer")
    public DefaultMQProducer mqProducer() throws MQClientException {
        /**
         * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ProducerGroupName需要由应用来保证唯一<br>
         * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
         * 因为服务器会回查这个Group下的任意一个Producer
         */
        DefaultMQProducer producer = new DefaultMQProducer(properties.getProducer().getGroup(applicationName) + "_" + activeProfiles);
        producer.setNamesrvAddr(properties.getNamesrvAddr());
        if (StringUtils.isNotBlank(properties.getProducer().getInstanceName())) {
            producer.setInstanceName(properties.getProducer().getInstanceName());
        }
        if (StringUtils.isNotBlank(properties.getProducer().getGroup())) {
            producer.setProducerGroup(properties.getProducer().getGroup());
        }
        producer.setVipChannelEnabled(false);

        /**
         * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br>
         * 注意：切记不可以在每次发送消息时，都调用start方法
         */
        producer.start();
        log.info("RocketMq defaultProducer Started.");
        return producer;
    }

    /**
     * 初始化向rocketmq发送事务消息的生产者
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(value = TransactionMQProducer.class, name = "transactionMQProducer")
    @ConditionalOnProperty(prefix = RocketMQProperties.PREFIX, value = "producer.tranInstanceName")
    public TransactionMQProducer transactionMQProducer() throws MQClientException {
        /**
         * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ProducerGroupName需要由应用来保证唯一<br>
         * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
         * 因为服务器会回查这个Group下的任意一个Producer
         */
        TransactionMQProducer producer = new TransactionMQProducer("TransactionProducerGroupName");

        producer.setNamesrvAddr(properties.getNamesrvAddr());
        if (StringUtils.isNotBlank(properties.getProducer().getTranInstanceName())) {
            producer.setInstanceName(properties.getProducer().getTranInstanceName());
        }

        if (StringUtils.isNotBlank(properties.getProducer().getGroup())) {
            producer.setProducerGroup(properties.getProducer().getGroup());
        }

        // 事务回查最小并发数
        producer.setCheckThreadPoolMinSize(2);
        // 事务回查最大并发数
        producer.setCheckThreadPoolMaxSize(2);
        // 队列数
        producer.setCheckRequestHoldMax(2000);

        //TODO 由于社区版本的服务器阉割调了消息回查的功能，所以这个地方没有意义
        //TransactionCheckListener transactionCheckListener = new TransactionCheckListenerImpl();
        //producer.setTransactionCheckListener(transactionCheckListener);

        /**
         * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br>
         * 注意：切记不可以在每次发送消息时，都调用start方法
         */
        producer.start();

        log.info("RocketMq TransactionMQProducer Started.");
        return producer;
    }


    @Bean
    @ConditionalOnMissingBean(Idempotent.class)
    public Idempotent idempotent() {
        return (Idempotent) new Idempotent.Default();
    }


    /**
     * 初始化rocketmq消息监听方式的消费者
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(value = MQPushConsumer.class, name = "pushConsumer")
    @ConditionalOnProperty(prefix = RocketMQProperties.PREFIX, value = "consumer.subscribe")
    public MQPushConsumer pushConsumer(Idempotent idempotent) throws MQClientException {
        /**
         * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ConsumerGroupName需要由应用来保证唯一
         */
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(properties.getConsumer().getGroup(applicationName) + "_" + activeProfiles);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setNamesrvAddr(properties.getNamesrvAddr());

        ConsumerConfig consumerConfig = properties.getConsumer();

        consumer.setMessageModel(consumerConfig.getMessageModel());
        if (StringUtils.isNotBlank(consumerConfig.getInstanceName())) {
            consumer.setInstanceName(consumerConfig.getInstanceName());
        }
        consumer.setConsumeMessageBatchMaxSize(1);//设置批量消费，以提升消费吞吐量，默认是1
        consumer.setConsumeThreadMin(consumerConfig.getMinConsumers());
        consumer.setConsumeThreadMax(consumerConfig.getMaxConsumers());

        /**
         * 订阅指定topic下tags
         */
        List<String> subscribeList = consumerConfig.getSubscribe();

        Map<String, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

        for (Map.Entry<String, Integer> entry : consumerConfig.getWorkerMaxSubscribe().entrySet()) {
            int max = entry.getValue();
            if (max < 1) {
                max = consumerConfig.getDefaultWorkers();
            } else if (max > consumerConfig.getMaxConsumers()) {
                max = consumerConfig.getMaxConsumers();
            }
            semaphoreMap.put(entry.getKey(), new Semaphore(max, true));
        }

        for (String subscribe : subscribeList) {
            String[] pair = subscribe.split(":");
            consumer.subscribe(pair[0], pair.length == 2 ? pair[1] : "*");
            log.info("RocketMQ subscribe [{}]", subscribe);
        }

        consumer.registerMessageListener((List<MessageExt> msgs, ConsumeConcurrentlyContext context) -> {
            MessageExt msg = msgs.get(0);
            Semaphore semaphore = semaphoreMap.get(msg.getTopic() + ":" + msg.getTags());
            if (null == semaphore) {
                semaphore = semaphoreMap.computeIfAbsent(msg.getTopic(), s -> new Semaphore(consumerConfig.getDefaultWorkers()));
            }
            try {
                semaphore.acquire();
                //默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
                log.debug("Receive New Messages: {}", msgs.size());
                //发布消息到达的事件，以便分发到每个tag的监听方法
                if (idempotent.consume(msg.getTopic(), msg.getMsgId())) {
                    try {
                        this.publisher.publishEvent(new RocketMQEvent(msg, consumer));
                        log.debug("消息到达事件已经发布成功！");
                        idempotent.submit(msg.getTopic(), msg.getMsgId());
                    } catch (Throwable e) {
                        idempotent.rollback(msg.getTopic(), msg.getMsgId());
                        throw e;
                    }
                } else {
                    log.warn("消息重复消费:{} {}", msg.getTopic(), msg.getMsgId());
                }
            } catch (Exception e) {
                if (e instanceof RetryLaterException) {
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                log.error("消息处理异常 [{}:{}]", msg.getTopic(), msg.getMsgId(), e);
                if (consumerConfig.getReConsumeTimes() == -1 || msg.getReconsumeTimes() <= consumerConfig.getReConsumeTimes()) {//重复消费3次
                    //TODO 进行日志记录
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                } else {
                    //TODO 消息消费失败，进行日志记录
                    errorMsgLogger.debug("消息 Topic:{} MsgId:{} 消费失败请注意检查", msg.getTopic(), msg.getMsgId());
                }
            } finally {
                semaphore.release();
            }
            //如果没有return success，consumer会重复消费此信息，直到success。
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        this.pushConsumer = consumer;
        return this.pushConsumer;
    }

    /**
     * 初始化rocketmq消息监听方式的消费者
     */
    @Bean(destroyMethod = "shutdown")
    @Lazy
    @ConditionalOnMissingBean(value = MQPullConsumer.class, name = "pullConsumer")
    @ConditionalOnProperty(prefix = RocketMQProperties.PREFIX, value = "consumer.pull", havingValue = "true")
    public MQPullConsumer pullConsumer() throws MQClientException {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer(properties.getConsumer().getGroup(applicationName) + "_pull_" + activeProfiles);
        consumer.setNamesrvAddr(properties.getNamesrvAddr());
        consumer.setMessageModel(properties.getConsumer().getMessageModel());
        if (StringUtils.isNotBlank(properties.getConsumer().getInstanceName())) {
            consumer.setInstanceName(properties.getConsumer().getInstanceName());
        }
        consumer.start();
        return consumer;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!this.applicationStart) {
            this.applicationStart = true;
        }

        if (null == this.pushConsumer || pushConsumerStart) {
            return;
        }
        log.info("ContextRefreshedEvent:{} {} {}", event.getTimestamp(), event.getApplicationContext(), event.getSource());
        try {
            this.pushConsumer.start();
            this.pushConsumerStart = true;
            log.info("RocketMq pushConsumer Started.");
        } catch (Exception e) {
            log.error("RocketMq pushConsumer Start failure!!!.", e);
        }
    }

}
