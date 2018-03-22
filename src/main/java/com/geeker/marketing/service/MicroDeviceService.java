package com.geeker.marketing.service;

import com.geeker.marketing.dao.micro.generator.model.MicroDevice;

import java.util.List;

/**
 * Created by Administrator on 2018/2/2 0002.
 */
public interface MicroDeviceService {

    List<MicroDevice> getDeviceList(Integer comId) throws Exception;

    MicroDevice getbyDeviceId(String deviceId);

    boolean isMicroDevice(String clientId);
}
