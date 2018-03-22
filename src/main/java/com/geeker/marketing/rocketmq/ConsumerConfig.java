package com.geeker.marketing.rocketmq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiangjb
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerConfig {
    private String instanceName;
    private MessageModel messageModel = MessageModel.CLUSTERING;
    private List<String> subscribe;
    private int reConsumeTimes = -1;
    private int maxConsumers = 32;
    private int minConsumers = 16;
    private int defaultWorkers = 4;
    private String group;
    private Map<String, Integer> workerMaxSubscribe = new HashMap<>();

    public String getGroup(String defaultVal) {
        return StringUtils.isBlank(group)?defaultVal:group;
    }
}
