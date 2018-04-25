package com.geeker.marketing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geeker.marketing.dao.micro.custom.mapper.CustomOpDeviceCmdMapper;
import com.geeker.marketing.dao.micro.generator.mapper.OpDeviceCmdMapper;
import com.geeker.marketing.dao.micro.generator.model.OpDeviceCmd;
import com.geeker.marketing.dao.micro.generator.model.OpDeviceCmdExample;
import com.geeker.marketing.enums.CmdEnum;
import com.geeker.marketing.listener.PublicEvent;
import com.geeker.marketing.netty.ClientHolder;
import com.geeker.marketing.netty.NettyUtil;
import com.geeker.marketing.response.Response;
import com.geeker.marketing.response.ResponseUtils;
import com.geeker.marketing.service.OpDeviceCmdService;
import com.geeker.marketing.utils.FactoryIdUtils;
import com.geeker.marketing.vo.DeviceCmdVo;
import com.geeker.marketing.vo.OpDeviceCmdVo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
* @Author TangZhen
* @Date 2018/3/22 0022 10:04
* @Description  消息指令业务（下发--回调--消息队列）
*/
@Service
@Slf4j
public class OpDeviceCmdServiceImpl implements OpDeviceCmdService {

    @Resource
    private OpDeviceCmdMapper opDeviceCmdMapper;

    @Resource
    private CustomOpDeviceCmdMapper customOpDeviceCmdMapper;

    @Resource
    private ApplicationEventPublisher eventPublisher;

    @Resource
    private ClientHolder clientHolder;

    @Resource(name = "cmdProducer")
    private DefaultMQProducer cmdProducer;

    @Value("${spring.rocketmq.topic.report-topic}")
    private String reportTopic;

    /**
     * 查找未下发指令
     * @param deviceId
     * @return
     */
    @Override
    public List<OpDeviceCmd> notIssuedCmd(String deviceId) {
        OpDeviceCmd example = new OpDeviceCmd();
        //TODO 过期指令处理....
        example.setDeliverStatus(CmdEnum.DeliverStatusEnum.UNDO.getCode());
        example.setDeviceId(deviceId);
        List<OpDeviceCmd> opDeviceCmds = customOpDeviceCmdMapper.getUnDoCmd(example);
          return opDeviceCmds;
    }

    @Override
    public OpDeviceCmd getByCmdId(String cmdId) {
        return opDeviceCmdMapper.selectByPrimaryKey(cmdId);
    }

    @Override
    public void updateDeliverStatus(String id, Integer status, String result) {
        OpDeviceCmd opDeviceCmd = new OpDeviceCmd();
        opDeviceCmd.setId(id);
        opDeviceCmd.setDeliverStatus(status);
        //opDeviceCmd.setReceiveResult(result);
        opDeviceCmd.setDeliverTime(new Date());
        opDeviceCmdMapper.updateByPrimaryKeySelective(opDeviceCmd);
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
        opDeviceCmd.setDeliverStatus(CmdEnum.DeliverStatusEnum.UNDO.getCode());
        log.info("下发指令入库【{}】",id);
        if(opDeviceCmdMapper.insert(opDeviceCmd)<=0){
            return ResponseUtils.error(500,"操作失败！");
        }
        Channel channel = clientHolder.getClient(vo.getDeviceId());
        if(null == channel){
            log.error("设备【{}】未连接...",vo.getDeviceId());
            return ResponseUtils.error(500,"设备未连接，会在设备连接后下发该指令！");
        }
        eventPublisher.publishEvent(new PublicEvent.IssueCmdEvent("",id));
        return ResponseUtils.success();
    }

    /**
     * 解除绑定
     * @param json
     * @return
     */
    @Override
    public Response removeBound(String json) throws Exception {
        Map<String,Object> data = JSON.parseObject(json);
        String deviceId = data.get("deviceId").toString();
        if(StringUtils.isEmpty(deviceId)){
            throw new Exception("设备id不能为空！");
        }
        //指令入库
        OpDeviceCmd vo = new OpDeviceCmd();
        //生成指令id
        String id = FactoryIdUtils.createId();
        vo.setId(id);
        vo.setDeviceId(deviceId);
        vo.setCmdParm(data.get("deviceId").toString());
        vo.setComId((Integer) data.get("comId"));
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.SYS.getCode());
        vo.setCmdCd(CmdEnum.CmdCdEnum.sys_remove_bound.getCode());
        vo.setDeliverTime(new Date());
        vo.setUserId((Integer) data.get("userId"));
        DeviceCmdVo cmdVo = new DeviceCmdVo();
        cmdVo.setCmdCd(CmdEnum.CmdCdEnum.sys_remove_bound.getCode());
        cmdVo.setCmdId(id);
        cmdVo.setCmdParm(null);
        cmdVo.setCmdTypeCd(CmdEnum.TypeCdEnum.SYS.getCode());

        return boundCmd(vo,deviceId,cmdVo);
    }

    /**
     * 绑定设备
     * @param json
     * @return
     */
    @Override
    public Response boundDevice(String json) throws Exception {
        Map<String,Object> data = JSON.parseObject(json);
        String deviceId = data.get("deviceId").toString();
        //指令入库
        OpDeviceCmd vo = new OpDeviceCmd();
        //生成指令id
        String id = FactoryIdUtils.createId();
        vo.setId(id);
        vo.setDeviceId(deviceId);
        vo.setCmdParm(data.get("deviceId").toString());
        vo.setComId((Integer) data.get("comId"));
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.SYS.getCode());
        vo.setCmdCd(CmdEnum.CmdCdEnum.sys_bound.getCode());
        vo.setDeliverTime(new Date());
        vo.setDeliverStatus(CmdEnum.DeliverStatusEnum.DO.getCode());
        vo.setUserId((Integer) data.get("userId"));

        Map<String,Object> map = new HashMap<>(3);
        map.put("sysId",1);
        map.put("comId", data.get("comId"));
        map.put("userId",data.get("userId"));
        map.put("userLoginName",data.get("userLoginName"));
        map.put("userName",data.get("userName"));
        log.info("绑定指令下发【{}】->【{}】", id, deviceId);
        DeviceCmdVo cmdVo = new DeviceCmdVo();
        cmdVo.setCmdCd(CmdEnum.CmdCdEnum.sys_bound.getCode());
        cmdVo.setCmdId(id);
        cmdVo.setCmdParm(JSON.toJSONString(map));
        cmdVo.setCmdTypeCd(CmdEnum.TypeCdEnum.SYS.getCode());

        return boundCmd(vo,deviceId,cmdVo);
    }

    /**
     * 拨打电话
     * @param json
     * @return
     */
    @Override
    public Response cmdCall(String json) throws ExecutionException, InterruptedException {
        if(StringUtils.isEmpty(json)){
            return ResponseUtils.error(500, "上传数据异常！");
        }
        JSONObject data = JSON.parseObject(json);
        String mobile = data.getString("mobile");
        if (StringUtils.isEmpty(mobile)) {
            return ResponseUtils.error(500, "电话号码不能为空！");
        }
        String deviceId = data.getString("deviceId");
        //指令入库
        OpDeviceCmd vo = new OpDeviceCmd();
        //生成指令id
        String id = FactoryIdUtils.createId();
        vo.setId(id);
        vo.setCmdParm(mobile);
        vo.setDeviceId(deviceId);
        vo.setComId(data.getInteger("comId"));
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.CALL.getCode());
        vo.setCmdCd(CmdEnum.CmdCdEnum.call_out.getCode());
        vo.setUserId(data.getInteger("userId"));
        vo.setDeliverTime(new Date());
        vo.setDeliverStatus(CmdEnum.DeliverStatusEnum.DO.getCode());

        log.info("拨打电话指令下发【{}】->【{}】", vo.getId(), vo.getCmdParm());
        Map<String,String> map = new HashMap<>(1);
        map.put("secureNumber",vo.getCmdParm());
        DeviceCmdVo cmdVo = new DeviceCmdVo();
        cmdVo.setCmdCd(CmdEnum.CmdCdEnum.call_out.getCode());
        cmdVo.setCmdId(vo.getId());
        cmdVo.setCmdParm(JSON.toJSONString(map));
        cmdVo.setCmdTypeCd(CmdEnum.TypeCdEnum.CALL.getCode());
        return directIssueCmd(vo, deviceId,cmdVo);
    }

    /**
     * 直接下发指令
     * @param vo
     * @param deviceId
     * @return
     */
    private Response directIssueCmd(OpDeviceCmd vo,String deviceId,DeviceCmdVo cmdVo) throws ExecutionException, InterruptedException {
        Channel channel = clientHolder.getClient(deviceId);
        if (null == channel) {
            return ResponseUtils.error(500, "客户端不在线！");
        }
        if (opDeviceCmdMapper.insert(vo) <= 0) {
            return ResponseUtils.error(500, "操作失败！");
        }
        ChannelFuture channelFuture = NettyUtil.sendMessage(channel, JSON.toJSONString(cmdVo)).addListener(future -> {
        });
        channelFuture.get();
        if (channelFuture.isSuccess()) {
            return ResponseUtils.success();
        } else {
            OpDeviceCmd cmd = new OpDeviceCmd();
            cmd.setId(vo.getId());
            cmd.setDeliverStatus(CmdEnum.DeliverStatusEnum.FAIL.getCode());
            opDeviceCmdMapper.updateByPrimaryKeySelective(cmd);
            return ResponseUtils.error(500, "指令下发失败！");
        }
    }

    /**
     * 绑定业务
     * @param vo
     * @param deviceId
     * @return
     */
    private Response boundCmd(OpDeviceCmd vo,String deviceId,DeviceCmdVo cmdVo) throws ExecutionException, InterruptedException {
        Channel channel = clientHolder.getClient(deviceId);
        if(null == channel){
            log.info("设备未连接！");
            vo.setDeliverStatus(CmdEnum.DeliverStatusEnum.UNDO.getCode());
        }else {
            vo.setDeliverStatus(CmdEnum.DeliverStatusEnum.DO.getCode());
            ChannelFuture channelFuture = NettyUtil.sendMessage(channel, JSON.toJSONString(cmdVo)).addListener(future -> {
            });
            channelFuture.get();
            if (!channelFuture.isSuccess()) {
                vo.setDeliverStatus(CmdEnum.DeliverStatusEnum.FAIL.getCode());
            }
        }
        if (opDeviceCmdMapper.insert(vo) <= 0) {
            return ResponseUtils.error(500, "操作失败！");
        }
        return ResponseUtils.success();
    }
}
