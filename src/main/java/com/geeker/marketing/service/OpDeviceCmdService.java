package com.geeker.marketing.service;

import com.geeker.marketing.dao.micro.generator.model.OpDeviceCmd;

import java.util.List;

/**
 * Created by Administrator on 2018/3/22 0022.
 */
public interface OpDeviceCmdService {
    List<OpDeviceCmd> notIssuedCmd(String deviceId);
}
