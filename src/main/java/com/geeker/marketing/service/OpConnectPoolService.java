package com.geeker.marketing.service;

import com.geeker.marketing.dao.micro.generator.model.OpConnectPool;

/**
 * Created by Administrator on 2018/3/13 0013.
 */
public interface OpConnectPoolService {
    OpConnectPool getByDeviceId(String deviceId);

    int insert(OpConnectPool opConnectPool);

    int update(OpConnectPool opConnectPool);

    int updateById(OpConnectPool opConnectPool);

    void registerConnect(String clientId);

    void cancellation(String deviceId);
}
