package com.geeker.marketing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geeker.marketing.dao.micro.custom.mapper.CustomOpDeviceCmdMapper;
import com.geeker.marketing.dao.micro.generator.mapper.OpDeviceCmdMapper;
import com.geeker.marketing.dao.micro.generator.mapper.OpDeviceReportMapper;
import com.geeker.marketing.dao.micro.generator.mapper.WxEventMapper;
import com.geeker.marketing.dao.micro.generator.mapper.WxMsgMapper;
import com.geeker.marketing.dao.micro.generator.model.OpDeviceCmd;
import com.geeker.marketing.dao.micro.generator.model.OpDeviceReport;
import com.geeker.marketing.dao.micro.generator.model.WxEvent;
import com.geeker.marketing.dao.micro.generator.model.WxMsg;
import com.geeker.marketing.enums.CmdEnum;
import com.geeker.marketing.service.OpDeviceReportService;
import com.geeker.marketing.vo.ReportCmdVo;
import com.geeker.marketing.vo.WxEventVo;
import com.geeker.marketing.vo.WxMsgVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Administrator on 2018/3/26 0026.
 */
@Service
@Slf4j
public class OpDeviceReportServiceImpl implements OpDeviceReportService {

    @Resource
    private OpDeviceReportMapper opDeviceReportMapper;

    @Resource
    private OpDeviceCmdMapper opDeviceCmdMapper;

    @Resource
    private WxMsgMapper wxMsgMapper;

    @Resource
    private WxEventMapper wxEventMapper;

    @Resource(name = "cmdProducer")
    private DefaultMQProducer cmdProducer;

    @Value("${spring.rocketmq.topic.voice-topic}")
    private String voiceTopic;

    @Override
    public int insert(OpDeviceReport opDeviceReport) {
        return opDeviceReportMapper.insert(opDeviceReport);
    }

    @Override
    public void dealReportCmd(ReportCmdVo vo) {
        try {
            switch (vo.getRspAction()){
                case "report":
                    log.info("此消息为主动上报消息【{}】",vo.getData());
                    OpDeviceReport opDeviceReport = new OpDeviceReport();
                    opDeviceReport.setComId(vo.getComId());
                    opDeviceReport.setDeviceId(vo.getDeviceId());
                    opDeviceReport.setCmdTypeCd(vo.getCmdTypeCd());
                    opDeviceReport.setCmdCd(vo.getCmdCd());
                    opDeviceReport.setReceiveResult(vo.getData());
                    opDeviceReport.setReceiveTime(vo.getFinish());
                    opDeviceReport.setReceiveStatus(vo.getCode()==200?1:0);
                    opDeviceReport.setCreateTime(new Date());
                    opDeviceReport.setQueue(vo.getQueue());
                    opDeviceReport.setMessageId(vo.getMessageId());
                    opDeviceReport.setQueueTime(vo.getQueueTime());
                    opDeviceReportMapper.insert(opDeviceReport);
                    break;
                case "issue" :
                    log.info("此消息为下发指令执行消息【{}】【{}】",vo.getCmdCd(),vo.getData());
                    OpDeviceCmd opDeviceCmd = new OpDeviceCmd();
                    opDeviceCmd.setId(vo.getCmdId());
                    opDeviceCmd.setReceiveResult(vo.getData());
                    opDeviceCmd.setReceiveStatus(vo.getCode()==200?1:0);
                    opDeviceCmd.setReceiveTime(vo.getFinish());
                    opDeviceCmd.setQueue(vo.getQueue());
                    opDeviceCmd.setQueueMsgId(vo.getMessageId());
                    opDeviceCmd.setQueueTime(vo.getQueueTime());
                    opDeviceCmdMapper.updateByPrimaryKey(opDeviceCmd);
                    break;
                default:
                    log.info("ReportCmd:未能识别消息类型【{}】",vo.getRspAction());
            }
            if(vo.getCmdCd().equals("wx_send_msg")){
                log.info("微信信息【{}】",vo.getData());
                WxMsgVo wxMsgVo = JSONObject.parseObject(vo.getData(),WxMsgVo.class);
                WxMsg wxMsg = new WxMsg();
                BeanUtils.copyProperties(wxMsgVo,wxMsg);
                wxMsg.setCreateTime(new Date());
                wxMsg.setComId(vo.getComId());
                wxMsg.setComId(vo.getComId());
                wxMsgMapper.insert(wxMsg);
            }else if("wx_add_friend,wx_send_visiting_card,wx_delete".contains(vo.getCmdCd())){
                log.info("微信事件信息【{}】",vo.getData());
                WxEventVo wxEventVo = JSONObject.parseObject(vo.getData(),WxEventVo.class);
                WxEvent wxEvent = new WxEvent();
                BeanUtils.copyProperties(wxEventVo,wxEvent);
                wxEvent.setCreateTime(new Date());
                wxEventMapper.insert(wxEvent);
            }
        }catch (Exception e){
            log.info("数据异常！",e.getMessage());
        }
    }

    @Override
    public void upLoadVocie(String json) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        if(StringUtils.isEmpty(json)){
            log.info("上报音频指令为空！！！");
            return;
        }
        Date date = new Date();
        JSONObject data = JSON.parseObject(json);
        String deviceId = data.getString("deviceId");
        String url = data.getString("parm");
        //入队列
        Message message = new Message(voiceTopic, deviceId,url.getBytes());
        cmdProducer.send(message);
        OpDeviceReport opDeviceReport = new OpDeviceReport();
        opDeviceReport.setComId(data.getInteger("comId"));
        opDeviceReport.setDeviceId(deviceId);
        opDeviceReport.setCmdTypeCd(CmdEnum.TypeCdEnum.CALL.getCode());
        opDeviceReport.setCmdCd(CmdEnum.CmdCdEnum.call_upLoad_voice.getCode());
        opDeviceReport.setReceiveResult(url);
        opDeviceReport.setReceiveTime(date);
        opDeviceReport.setReceiveStatus(1);
        opDeviceReport.setCreateTime(date);
        opDeviceReport.setQueue(voiceTopic);
        opDeviceReport.setMessageId(message.getBuyerId());
        opDeviceReport.setQueueTime(date);
        opDeviceReportMapper.insert(opDeviceReport);
    }
}
