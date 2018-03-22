package com.geeker.marketing.rocketmq;

/**
 * Created by Lubin.Xuan on 2017-08-24.
 * RocketMQ 消息消费幂等检查接口
 */
public interface Idempotent {

    boolean consume(String topic, String messageId);

    void submit(String topic, String messageId);

    void rollback(String topic, String messageId);

    class Default implements me.robin.spring.rocketmq.Idempotent {
        @Override
        public boolean consume(String topic, String messageId) {
            return true;
        }

        @Override
        public void submit(String topic, String messageId) {
        }

        @Override
        public void rollback(String topic, String messageId) {

        }
    }

}
