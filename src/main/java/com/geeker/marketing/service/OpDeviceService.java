package com.geeker.marketing.service;

import com.geeker.marketing.dao.micro.generator.model.OpDevice;

/**
 * Created by Administrator on 2018/4/23 0023.
 */
public interface OpDeviceService {

    OpDevice selectById(String id);
}
