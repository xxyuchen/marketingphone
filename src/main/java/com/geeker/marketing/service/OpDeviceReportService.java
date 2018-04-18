package com.geeker.marketing.service;

import com.geeker.marketing.dao.micro.generator.model.OpDeviceReport;
import com.geeker.marketing.vo.ReportCmdVo;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * Created by Administrator on 2018/3/26 0026.
 */
public interface OpDeviceReportService {

    int insert(OpDeviceReport opDeviceReport);

    void dealReportCmd(ReportCmdVo vo);

    void upLoadVocie(String json) throws InterruptedException, RemotingException, MQClientException, MQBrokerException;
}
