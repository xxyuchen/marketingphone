package com.geeker.marketing.service.impl;

import com.geeker.marketing.dao.micro.generator.mapper.OpDeviceReportMapper;
import com.geeker.marketing.dao.micro.generator.model.OpDeviceReport;
import com.geeker.marketing.service.OpDeviceReportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/3/26 0026.
 */
@Service
public class OpDeviceReportServiceImpl implements OpDeviceReportService {

    @Resource
    private OpDeviceReportMapper opDeviceReportMapper;
    @Override
    public int insert(OpDeviceReport opDeviceReport) {
        return opDeviceReportMapper.insert(opDeviceReport);
    }
}
