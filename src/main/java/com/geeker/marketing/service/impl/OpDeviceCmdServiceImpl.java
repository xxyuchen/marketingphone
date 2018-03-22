package com.geeker.marketing.service.impl;

import com.geeker.marketing.dao.micro.generator.mapper.OpDeviceCmdMapper;
import com.geeker.marketing.dao.micro.generator.model.OpDeviceCmd;
import com.geeker.marketing.dao.micro.generator.model.OpDeviceCmdExample;
import com.geeker.marketing.enums.CmdEnum;
import com.geeker.marketing.service.OpDeviceCmdService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @Author TangZhen
* @Date 2018/3/22 0022 10:04
* @Description  消息指令业务（下发--回调--消息队列）
*/
@Service
public class OpDeviceCmdServiceImpl implements OpDeviceCmdService {

    @Resource
    private OpDeviceCmdMapper opDeviceCmdMapper;
    /**
     * 查找未下发指令
     * @param deviceId
     * @return
     */
    @Override
    public List<OpDeviceCmd> notIssuedCmd(String deviceId) {
        OpDeviceCmdExample example = new OpDeviceCmdExample();
        //TODO 过期指令处理....
        example.createCriteria().andDeviceIdEqualTo(deviceId);
        example.createCriteria().andDeliverStatusEqualTo(CmdEnum.DeliverStatusEnum.UNDO.getCode());
        List<OpDeviceCmd> opDeviceCmds = opDeviceCmdMapper.selectByExample(example);
        return opDeviceCmds;
    }
}
