package com.geeker.marketing.service;

import com.geeker.marketing.dao.micro.generator.model.OpDeviceReport;
import com.geeker.marketing.vo.ReportCmdVo;

/**
 * Created by Administrator on 2018/3/26 0026.
 */
public interface OpDeviceReportService {

    int insert(OpDeviceReport opDeviceReport);

    void dealReportCmd(ReportCmdVo vo);
}
