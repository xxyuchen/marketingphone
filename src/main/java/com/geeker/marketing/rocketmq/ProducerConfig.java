package com.geeker.marketing.rocketmq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author jiangjb
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProducerConfig {
    private String instanceName;
	private String tranInstanceName;
    private String topic;
    private String group;

    public String getGroup(String defaultVal) {
        return StringUtils.isBlank(group)?defaultVal:group;
    }
}
