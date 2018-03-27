package com.geeker.marketing.service.impl;

import com.geeker.marketing.dao.micro.generator.mapper.OpDeviceCmdMapper;
import com.geeker.marketing.dao.micro.generator.model.OpDeviceCmd;
import com.geeker.marketing.dao.micro.generator.model.OpDeviceCmdExample;
import com.geeker.marketing.enums.CmdEnum;
import com.geeker.marketing.netty.ClientHolder;
import com.geeker.marketing.response.Response;
import com.geeker.marketing.response.ResponseUtils;
import com.geeker.marketing.service.OpDeviceCmdService;
import com.geeker.marketing.utils.FactoryIdUtils;
import com.geeker.marketing.vo.OpDeviceCmdVo;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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

    @Resource
    private ApplicationEventPublisher eventPublisher;

    @Resource
    private ClientHolder clientHolder;
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

    @Override
    public void updateDeliverStatus(String id, Integer status, String result) {
        OpDeviceCmd opDeviceCmd = new OpDeviceCmd();
        opDeviceCmd.setId(id);
        opDeviceCmd.setDeliverStatus(status);
        //opDeviceCmd.setReceiveResult(result);
        opDeviceCmd.setDeliverTime(new Date());
        opDeviceCmdMapper.updateByPrimaryKey(opDeviceCmd);
    }

    @Override
    public int update(OpDeviceCmd opDeviceCmd) {
        return opDeviceCmdMapper.updateByPrimaryKey(opDeviceCmd);
    }

    @Override
    public int insert(OpDeviceCmd opDeviceCmd) {
        return opDeviceCmdMapper.insert(opDeviceCmd);
    }

    /**
     * 下发指令
     * @param vo
     * @return
     */
    @Override
    public Response issueCmd(OpDeviceCmdVo vo) throws Exception {
        if(StringUtils.isEmpty(vo.getDeviceId())){
            throw new Exception("设备id不能为空！");
        }
        //生成指令id
        String id = FactoryIdUtils.createId();
        OpDeviceCmd opDeviceCmd = new OpDeviceCmd();
        BeanUtils.copyProperties(vo,opDeviceCmd);
        opDeviceCmd.setCreateTime(new Date());
        opDeviceCmd.setId(id);
        if(opDeviceCmdMapper.insert(opDeviceCmd)<=0){
            return ResponseUtils.error(500,"操作失败！");
        }
        Channel channel = clientHolder.getClient(id);
        if(null == channel){
            return ResponseUtils.error(500,"设备未连接，会在设备连接后下发该指令！");
        }
        eventPublisher.publishEvent(new ClientHolder.AddClientEvent("",vo.getDeviceId()));
        return ResponseUtils.success();
    }
}
