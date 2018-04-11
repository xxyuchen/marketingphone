package com.geeker.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geeker.marketing.dao.micro.generator.model.OpDeviceCmd;
import com.geeker.marketing.enums.CmdEnum;
import com.geeker.marketing.netty.ClientHolder;
import com.geeker.marketing.netty.NettyUtil;
import com.geeker.marketing.response.Response;
import com.geeker.marketing.response.ResponseUtils;
import com.geeker.marketing.service.OpDeviceCmdService;
import com.geeker.marketing.utils.FactoryIdUtils;
import com.geeker.marketing.vo.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author TangZhen
 * @Date 2018/3/26 0026 16:16
 * @Description 指令下发--通讯录业务
 */
@Controller
@Slf4j
@RequestMapping("/cmd/phoneBook")
public class OpDeviceCmdPhoneBookController {

    @Resource
    private OpDeviceCmdService opDeviceCmdService;

    @Resource
    private ClientHolder clientHolder;

    private boolean isSuccess;

    /**
     * 同步通讯录
     *
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping("/phoneBook")
    @ResponseBody
    public Response phoneBook(String json) throws Exception {
        log.info("同步通讯录指令--》【{}】", json);
        PhoneBookVo phoneBook = JSON.parseObject(json, PhoneBookVo.class);
        //指令入库
        Map<String, Object> map = new HashMap<>(2);
        map.put("delete", phoneBook.getDelMobiles());
        map.put("update", phoneBook.getMobiles());
        OpDeviceCmdVo vo = new OpDeviceCmdVo();
        vo.setCmdParm(JSON.toJSONString(map));
        vo.setDeviceId(phoneBook.getDeviceId());
        vo.setComId(phoneBook.getComId());
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.CALL.getCode());
        vo.setCmdCd(CmdEnum.CmdCdEnum.call_book.getCode());
        //vo.setDeliverTime(new Date());
        Response response = opDeviceCmdService.issueCmd(vo);
        return response;
    }

    /**
     * 拨打电话
     *
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping("/call")
    @ResponseBody
    public Response cmdCall(String json) throws Exception {
        log.info("拨打电话指令--》【{}】", json);
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
        vo.setComId(data.getInteger("comId"));
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.CALL.getCode());
        vo.setCmdCd(CmdEnum.CmdCdEnum.call_out.getCode());

        vo.setDeliverTime(new Date());
        vo.setDeliverStatus(CmdEnum.DeliverStatusEnum.DO.getCode());
        if (opDeviceCmdService.insert(vo) <= 0) {
            return ResponseUtils.error(500, "操作失败！");
        }
        Channel channel = clientHolder.getClient(deviceId);
        if (null == channel) {
            return ResponseUtils.error(500, "客户端不在线！");
        }
        log.info("拨打电话指令下发【{}】->【{}】", id, mobile);
        Map<String,String> map = new HashMap<>(1);
        map.put("secureNumber",mobile);
        DeviceCmdVo cmdVo = new DeviceCmdVo();
        cmdVo.setCmdCd(CmdEnum.CmdCdEnum.call_out.getCode());
        cmdVo.setCmdId(id);
        cmdVo.setCmdParm(JSON.toJSONString(map));
        cmdVo.setCmdTypeCd(CmdEnum.TypeCdEnum.CALL.getCode());
        NettyUtil.sendMessage(channel, JSON.toJSONString(cmdVo)).addListener(future -> {
            isSuccess = future.isSuccess();
        });
        if (isSuccess) {
            return ResponseUtils.success();
        } else {
            return ResponseUtils.error(500, "指令下发失败！");
        }
    }
    /**
     * 发送短信
     *
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping("/smsSendMsg")
    @ResponseBody
    public Response smsSendMsg(String json) throws Exception {
        log.info("同步通讯录指令--》【{}】", json);
        SmsSendMsgVo smsSendMsgVo = JSON.parseObject(json, SmsSendMsgVo.class);
        //指令入库
        Map<String, Object> map = new HashMap<>(2);
        map.put("parm", smsSendMsgVo.getParm());
        map.put("secureNumber", smsSendMsgVo.getSecureNumber());
        OpDeviceCmdVo vo = new OpDeviceCmdVo();
        vo.setCmdParm(JSON.toJSONString(map));
        vo.setDeviceId(smsSendMsgVo.getDeviceId());
        vo.setComId(smsSendMsgVo.getComId());
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.SMS.getCode());
        vo.setCmdCd(CmdEnum.CmdCdEnum.sms_send_msg.getCode());
        Response response = opDeviceCmdService.issueCmd(vo);
        return response;
    }

}
