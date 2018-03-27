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
import com.geeker.marketing.vo.OpDeviceCmdVo;
import com.geeker.marketing.vo.PhoneBookVo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public Response synPhoneBook(String json) throws Exception {
        PhoneBookVo phoneBook = (PhoneBookVo) JSON.parse(json);
        //指令入库
        OpDeviceCmdVo vo = new OpDeviceCmdVo();
        vo.setCmdParm(phoneBook.getMobiles().toString());
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
    public Response cmdCall(String json) throws Exception {
        JSONObject data = JSON.parseObject(json);
        String mobile = data.getString("mobile");
        if(StringUtils.isEmpty(mobile)){
            return ResponseUtils.error(500,"电话号码不能为空！");
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
        if(opDeviceCmdService.insert(vo)<=0){
            return ResponseUtils.error(500,"操作失败！");
        }
        Channel channel = clientHolder.getClient(deviceId);
        if(null == channel){
            return ResponseUtils.error(500,"客户端不在线！");
        }
        log.info("拨打电话指令下发【{}】->【{}】",id,mobile);

        Map<String,Object> map = new HashMap<>(8);
        map.put("cmdId",id);
        map.put("cmdParm",mobile);
        map.put("cmdCd",CmdEnum.CmdCdEnum.call_out.getCode());
        map.put("cmdTypeCd",CmdEnum.TypeCdEnum.CALL.getCode());
        map.put("deviceId",deviceId);
        ChannelFuture channelFuture = NettyUtil.sendMessage(channel, map.toString());
        if(channelFuture.isSuccess()){
            return ResponseUtils.success();

        }else {
            return ResponseUtils.error(500,"指令下发失败！");
        }
    }

}
