package com.geeker.marketing.vo;

import com.geeker.marketing.dao.micro.generator.model.OpConnectPool;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Created by Lubin.Xuan on 2018-01-24.
 * {desc}
 */
@Getter
@Setter
public class DeviceVO extends OpConnectPool {
    private String bindAccount;
    private Set<String> bindSessionIds;
    private String deviceType;
}
