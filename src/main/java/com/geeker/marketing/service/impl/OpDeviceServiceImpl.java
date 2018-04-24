package com.geeker.marketing.service.impl;

import com.geeker.marketing.dao.micro.generator.mapper.OpDeviceMapper;
import com.geeker.marketing.dao.micro.generator.model.OpDevice;
import com.geeker.marketing.service.OpDeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/4/23 0023.
 */
@Service
public class OpDeviceServiceImpl implements OpDeviceService {

    @Resource
    private OpDeviceMapper opDeviceMapper;

    @Override
    public OpDevice selectById(String id) {
        return opDeviceMapper.selectByPrimaryKey(id);
    }
}
