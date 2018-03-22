package com.geeker.marketing.rocketmq;

import me.robin.spring.rocketmq.Idempotent;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by Lubin.Xuan on 2017-08-24.
 * {desc}
 */
public class RedisIdempotent implements Idempotent {

    private static final byte[] EMPTY = new byte[0];

    private RedisTemplate<?, ?> template;

    private int db;

    public RedisIdempotent(RedisTemplate template, int db) {
        this.template = template;
        this.db = db;
    }

    @Override
    public boolean consume(String topic, String messageId) {
        return template.execute((RedisCallback<Boolean>) connection -> {
            connection.select(db);
            byte[] pk = template.getStringSerializer().serialize(topic);
            byte[] temp = template.getStringSerializer().serialize(topic + "%" + messageId);
            byte[] id = template.getStringSerializer().serialize(messageId);
            if (connection.sIsMember(pk, id) || connection.exists(temp)) {
                return false;
            } else {
                connection.setEx(temp, 900, EMPTY);
                return true;
            }
        });
    }

    @Override
    public void submit(String topic, String messageId) {
        byte[] pk = template.getStringSerializer().serialize(topic);
        byte[] id = template.getStringSerializer().serialize(messageId);
        byte[] temp = template.getStringSerializer().serialize(topic + "%" + messageId);
        template.executePipelined((RedisCallback<Boolean>) connection -> {
            connection.select(db);
            connection.sAdd(pk, id);
            connection.del(temp);
            return null;
        });
    }

    @Override
    public void rollback(String topic, String messageId) {
        byte[] temp = template.getStringSerializer().serialize(topic + "%" + messageId);
        template.executePipelined((RedisCallback<Boolean>) connection -> {
            connection.select(db);
            connection.del(temp);
            return null;
        });
    }
}
