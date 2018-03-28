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
import com.geeker.marketing.vo.CustVo;
import com.geeker.marketing.vo.OpDeviceCmdVo;
import com.geeker.marketing.vo.PhoneBookVo;
import com.geeker.marketing.vo.WxLocationVo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author TangZhen
 * @Date 2018/3/26 0026 16:16
 * @Description 指令下发
 */
@Controller
@Slf4j
@RequestMapping("/cmd")
public class OpDeviceCmdController {

    @Resource
    private OpDeviceCmdService opDeviceCmdService;

    @Resource
    private ClientHolder clientHolder;

    /**
     * 下发指令
     *
     * @param vo
     * @return
     * @throws Exception
     */
    @RequestMapping("/issueCmd")
    @ResponseBody
    public Response issueCmd(OpDeviceCmdVo vo) throws Exception {
        return opDeviceCmdService.issueCmd(vo);
    }

    /**
     * 同步通讯录
     *
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping("/synPhoneBook")
    @ResponseBody
    public Response synPhoneBook(String json) throws Exception {
        log.info("同步通讯录指令--》【{}】", json);
        PhoneBookVo phoneBook = JSON.parseObject(json, PhoneBookVo.class);
        //指令入库
        Map<String, Object> map = new HashMap<>(2);
        map.put("delete", phoneBook.getDelMobiles());
        map.put("update", phoneBook.getMobiles());
        OpDeviceCmdVo vo = new OpDeviceCmdVo();
        System.out.println(map.toString());
        vo.setCmdParm(map.toString());
        vo.setDeviceId(phoneBook.getDeviceId());
        vo.setComId(phoneBook.getComId());
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.CALL.getCode());
        vo.setCmdCd(CmdEnum.CmdCdEnum.call_book.getCode());
        //vo.setDeliverTime(new Date());
        //vo.setDeliverStatus(CmdEnum.DeliverStatusEnum.DO.getCode());
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

        Map<String, Object> map = new HashMap<>(8);
        map.put("cmdId", id);
        map.put("cmdParm", mobile);
        map.put("cmdCd", CmdEnum.CmdCdEnum.call_out.getCode());
        map.put("cmdTypeCd", CmdEnum.TypeCdEnum.CALL.getCode());
        map.put("deviceId", deviceId);
        ChannelFuture channelFuture = NettyUtil.sendMessage(channel, map.toString());
        if (channelFuture.isSuccess()) {
            return ResponseUtils.success();

        } else {
            return ResponseUtils.error(500, "指令下发失败！");
        }
    }

    /**
     * 精准加人
     *
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping("/addAccurate")
    @ResponseBody
    public Response addAccurate(String json) throws Exception {
        log.info("精准加人--》【{}】", json);
        CustVo custVo = JSON.parseObject(json, CustVo.class);
        //指令入库
        Map<String, Object> map = new HashMap<>(1);
        if (null == custVo.getMobiles() || custVo.getMobiles().size() <= 0) {
            throw new Exception("电话号码不能为空！");
        } else if (custVo.getMobiles().size() > 20) {
            throw new Exception("添加个数超出限制！");
        }
        map.put("mobiles", custVo.getMobiles());
        OpDeviceCmdVo vo = new OpDeviceCmdVo();
        System.out.println(map.toString());
        vo.setCmdParm(map.toString());
        vo.setDeviceId(custVo.getDeviceId());
        vo.setComId(custVo.getComId());
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.WX.getCode());
        vo.setCmdCd(CmdEnum.CmdCdEnum.wx_add_fans.getCode());
        Response response = opDeviceCmdService.issueCmd(vo);
        return response;
    }

    /**
     * 定位站街
     *
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping("/wxLocation")
    @ResponseBody
    public Response wxLocation(String json) throws Exception {
        log.info("定位站街--》【{}】", json);
        WxLocationVo wxLocationVo = JSON.parseObject(json, WxLocationVo.class);
        //指令入库
        Map<String, Object> map = new HashMap<>(6);
        if(wxLocationVo.isOpen()){
            if(wxLocationVo.getLatitude()==null||wxLocationVo.getLongtitude()==null){
                throw new Exception("经纬度不呢为空！");
            }else if(wxLocationVo.getRange()<=0||wxLocationVo.getRange()>=2000){
                throw new Exception("半径暂支持0-2000米！");
            }
            map.put("latitude",wxLocationVo.getLatitude());
            map.put("longtitude",wxLocationVo.getLongtitude());
            map.put("lac",wxLocationVo.getLac());
            map.put("cid",wxLocationVo.getCid());
            map.put("range",wxLocationVo.getRange());
        }
        map.put("open", wxLocationVo.isOpen());
        OpDeviceCmdVo vo = new OpDeviceCmdVo();
        System.out.println(map.toString());
        vo.setCmdParm(map.toString());
        vo.setDeviceId(wxLocationVo.getDeviceId());
        vo.setComId(wxLocationVo.getComId());
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.WX.getCode());
        vo.setCmdCd(CmdEnum.CmdCdEnum.wx_location.getCode());
        Response response = opDeviceCmdService.issueCmd(vo);
        return response;
    }

}
