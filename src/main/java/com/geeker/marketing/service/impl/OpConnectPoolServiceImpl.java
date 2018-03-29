package com.geeker.marketing.service.impl;

import com.geeker.marketing.dao.micro.generator.mapper.OpConnectPoolMapper;
import com.geeker.marketing.dao.micro.generator.model.OpConnectPool;
import com.geeker.marketing.dao.micro.generator.model.OpConnectPoolExample;
import com.geeker.marketing.enums.ConnectEnum;
import com.geeker.marketing.service.OpConnectPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @Author TangZhen
* @Date 2018/3/13 0013 13:28
* @Description  注册连接
*/

@Service
@Slf4j
public class OpConnectPoolServiceImpl implements OpConnectPoolService {
    @Resource
    private OpConnectPoolMapper opConnectPoolMapper;

    /**
     * 根据设备号查询连接
     * @param deviceId
     * @return
     */
    @Override
    public OpConnectPool getByDeviceId(String deviceId) {
        OpConnectPoolExample example = new OpConnectPoolExample();
        example.createCriteria().andDeviceIdEqualTo(deviceId);
        List<OpConnectPool> opConnectPools = opConnectPoolMapper.selectByExample(example);
        if(null!=opConnectPools&&opConnectPools.size()>0){
            return opConnectPools.get(0);
        }else {
            log.error("注册连接不存在【{}】",deviceId);
            return null;
        }
    }

    @Override
    public int insert(OpConnectPool opConnectPool) {
        opConnectPool.setConnectPoolId("0");//本地
        return opConnectPoolMapper.insert(opConnectPool);
    }

    @Override
    public int update(OpConnectPool opConnectPool) {
        OpConnectPoolExample example = new OpConnectPoolExample();
        example.createCriteria().andDeviceIdEqualTo(opConnectPool.getDeviceId());
        return opConnectPoolMapper.updateByExample(opConnectPool,example);
    }

    @Override
    public int updateById(OpConnectPool opConnectPool) {
        return opConnectPoolMapper.updateByPrimaryKey(opConnectPool);
    }

    /**
     * 注册连接
     * @param deviceId
     */
    @Override
    public void registerConnect(String deviceId) {
        //先查询是否有连接
        OpConnectPool opConnectPool = getByDeviceId(deviceId);
        if(null==opConnectPool){
            OpConnectPool connectPool = new OpConnectPool();
            connectPool.setDeviceId(deviceId);
            connectPool.setStatus(ConnectEnum.ConnectStatusEnum.ONLINE.getCode());
            insert(connectPool);
        }else {
            opConnectPool.setStatus(ConnectEnum.ConnectStatusEnum.ONLINE.getCode());
            updateById(opConnectPool);
        }

    }

    /**
     * 注销连接
     * @param deviceId
     */
    @Override
    public void cancellation(String deviceId) {
        OpConnectPool opConnectPool = getByDeviceId(deviceId);
        if(null==opConnectPool){
            OpConnectPool connectPool = new OpConnectPool();
            connectPool.setDeviceId(deviceId);
            connectPool.setStatus(ConnectEnum.ConnectStatusEnum.OFFLINE.getCode());
            insert(connectPool);
        }else {
            opConnectPool.setStatus(ConnectEnum.ConnectStatusEnum.OFFLINE.getCode());
            updateById(opConnectPool);
        }
    }
}
