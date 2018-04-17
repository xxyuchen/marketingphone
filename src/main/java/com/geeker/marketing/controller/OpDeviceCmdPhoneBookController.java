package com.geeker.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.geeker.marketing.dao.micro.generator.model.OpDeviceReport;
import com.geeker.marketing.enums.CmdEnum;
import com.geeker.marketing.response.Response;
import com.geeker.marketing.response.ResponseUtils;
import com.geeker.marketing.service.OpDeviceCmdService;
import com.geeker.marketing.service.OpDeviceReportService;
import com.geeker.marketing.vo.GroupBookVo;
import com.geeker.marketing.vo.OpDeviceCmdVo;
import com.geeker.marketing.vo.PhoneBookVo;
import com.geeker.marketing.vo.SmsSendMsgVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private OpDeviceReportService opDeviceReportService;

    /**
     * 同步通讯录
     *
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping("/phoneBook")
    @ResponseBody
    public Response phoneBook(@RequestBody String json) throws Exception {
        log.info("同步通讯录指令--》【{}】", json);
        PhoneBookVo phoneBook = JSON.parseObject(json, PhoneBookVo.class);
        //指令入库
        Map<String, Object> map = new HashMap<>(3);
        map.put("delete", phoneBook.getDelMobiles());
        map.put("update", phoneBook.getMobiles());
        map.put("dateTime", new Date());
        OpDeviceCmdVo vo = new OpDeviceCmdVo();
        vo.setCmdParm(JSON.toJSONString(map));
        vo.setDeviceId(phoneBook.getDeviceId());
        vo.setComId(phoneBook.getComId());
        vo.setUserId(phoneBook.getUserId());
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.CALL.getCode());
        vo.setCmdCd(CmdEnum.CmdCdEnum.call_book.getCode());
        //vo.setDeliverTime(new Date());
        OpDeviceReport opDeviceReport = new OpDeviceReport();
        opDeviceReport.setCmdCd(vo.getCmdCd());
        opDeviceReport.setCmdTypeCd(vo.getCmdTypeCd());
        opDeviceReport.setComId(phoneBook.getComId());
        opDeviceReport.setDeviceId(phoneBook.getDeviceId());
        opDeviceReport.setCreateTime(new Date());
        if (opDeviceReportService.insert(opDeviceReport) <= 0) {
            return ResponseUtils.error(500,"数据异常！");
        }
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
    @RequestMapping(value = "/call")
    @ResponseBody
    public Response cmdCall(@RequestBody String json) throws Exception {
        log.info("拨打电话指令--》【{}】", json);
        return opDeviceCmdService.cmdCall(json);
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
    public Response smsSendMsg(@RequestBody String json) throws Exception {
        log.info("同步通讯录指令--》【{}】", json);
        SmsSendMsgVo smsSendMsgVo = JSON.parseObject(json, SmsSendMsgVo.class);
        //指令入库
        Map<String, Object> map = new HashMap<>(2);
        map.put("smsContent", smsSendMsgVo.getParm());
        map.put("secureNumber", smsSendMsgVo.getSecureNumber());
        OpDeviceCmdVo vo = new OpDeviceCmdVo();
        vo.setCmdParm(JSON.toJSONString(map));
        vo.setDeviceId(smsSendMsgVo.getDeviceId());
        vo.setComId(smsSendMsgVo.getComId());
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.SMS.getCode());
        vo.setCmdCd(CmdEnum.CmdCdEnum.sms_send_msg.getCode());
        vo.setUserId(smsSendMsgVo.getUserId());
        Response response = opDeviceCmdService.issueCmd(vo);
        return response;
    }

    /**
     * 同步群组
     *
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping("/group")
    @ResponseBody
    public Response group(@RequestBody String json) throws Exception {
        log.info("同步群组指令--》【{}】", json);
        GroupBookVo groupBookVo = JSON.parseObject(json, GroupBookVo.class);
        //指令入库
        Map<String, Object> map = new HashMap<>(3);
        map.put("delete", groupBookVo.getDelGroups());
        map.put("update", groupBookVo.getGroups());
        map.put("dateTime", new Date());
        OpDeviceCmdVo vo = new OpDeviceCmdVo();
        vo.setCmdParm(JSON.toJSONString(map));
        vo.setDeviceId(groupBookVo.getDeviceId());
        vo.setComId(groupBookVo.getComId());
        vo.setUserId(groupBookVo.getUserId());
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.CALL.getCode());
        vo.setCmdCd(CmdEnum.CmdCdEnum.call_group.getCode());
        //vo.setDeliverTime(new Date());
        OpDeviceReport opDeviceReport = new OpDeviceReport();
        opDeviceReport.setCmdCd(vo.getCmdCd());
        opDeviceReport.setCmdTypeCd(vo.getCmdTypeCd());
        opDeviceReport.setComId(groupBookVo.getComId());
        opDeviceReport.setDeviceId(groupBookVo.getDeviceId());
        opDeviceReport.setCreateTime(new Date());
        if (opDeviceReportService.insert(opDeviceReport) <= 0) {
            return ResponseUtils.error(500,"数据异常！");
        }
        Response response = opDeviceCmdService.issueCmd(vo);
        return response;
    }

}
