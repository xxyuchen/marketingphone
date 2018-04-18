package com.geeker.marketing.service;

import com.geeker.marketing.dao.micro.generator.model.OpDeviceCmd;
import com.geeker.marketing.response.Response;
import com.geeker.marketing.vo.OpDeviceCmdVo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2018/3/22 0022.
 */
public interface OpDeviceCmdService {
    List<OpDeviceCmd> notIssuedCmd(String deviceId);

    OpDeviceCmd getByCmdId(String cmdId);

    void updateDeliverStatus(String id,Integer status,String result);

    int update(OpDeviceCmd opDeviceCmd);

    int insert(OpDeviceCmd opDeviceCmd);

    Response issueCmd(OpDeviceCmdVo vo) throws Exception;

    Response removeBound(String json) throws Exception;

    Response boundDevice(String json) throws Exception;

    Response cmdCall(String json) throws ExecutionException, InterruptedException;

}
