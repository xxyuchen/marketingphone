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
        DeviceCmdVo cmdVo = new DeviceCmdVo();
        cmdVo.setCmdCd(CmdEnum.CmdCdEnum.call_out.getCode());
        cmdVo.setCmdId(id);
        cmdVo.setCmdParm(mobile);
        cmdVo.setCmdTypeCd(CmdEnum.TypeCdEnum.CALL.getCode());
        ChannelFuture channelFuture = NettyUtil.sendMessage(channel, JSON.toJSONString(cmdVo));
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
        vo.setCmdParm(JSON.toJSONString(map));
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
        if (wxLocationVo.isOpen()) {
            if (wxLocationVo.getLatitude() == null || wxLocationVo.getLongtitude() == null) {
                throw new Exception("经纬度不呢为空！");
            } else if (wxLocationVo.getRange() <= 0 || wxLocationVo.getRange() >= 2000) {
                throw new Exception("半径暂支持0-2000米！");
            }
            map.put("latitude", wxLocationVo.getLatitude());
            map.put("longtitude", wxLocationVo.getLongtitude());
            map.put("lac", wxLocationVo.getLac());
            map.put("cid", wxLocationVo.getCid());
            map.put("range", wxLocationVo.getRange());
        }
        map.put("open", wxLocationVo.isOpen());
        OpDeviceCmdVo vo = new OpDeviceCmdVo();
        System.out.println(JSON.toJSONString(map));
        vo.setCmdParm(map.toString());
        vo.setDeviceId(wxLocationVo.getDeviceId());
        vo.setComId(wxLocationVo.getComId());
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.WX.getCode());
        vo.setCmdCd(CmdEnum.CmdCdEnum.wx_location.getCode());
        Response response = opDeviceCmdService.issueCmd(vo);
        return response;
    }

    /**
     * 附件加人
     *
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping("/wxAddRoundFans")
    @ResponseBody
    public Response wxAddRoundFans(String json) throws Exception {
        log.info("附件加人--》【{}】", json);
        WxLocationVo wxLocationVo = JSON.parseObject(json, WxLocationVo.class);
        //指令入库
        Map<String, Object> map = new HashMap<>(6);
        if (wxLocationVo.getLatitude() == null || wxLocationVo.getLongtitude() == null) {
            throw new Exception("经纬度不呢为空！");
        }
        map.put("latitude", wxLocationVo.getLatitude());
        map.put("longtitude", wxLocationVo.getLongtitude());
        map.put("helloMessage", wxLocationVo.getHelloMessage());
        OpDeviceCmdVo vo = new OpDeviceCmdVo();
        System.out.println(JSON.toJSONString(map));
        vo.setCmdParm(map.toString());
        vo.setDeviceId(wxLocationVo.getDeviceId());
        vo.setComId(wxLocationVo.getComId());
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.WX.getCode());
        vo.setCmdCd(CmdEnum.CmdCdEnum.wx_add_round_fans.getCode());
        Response response = opDeviceCmdService.issueCmd(vo);
        return response;
    }

    /**
     * 自动通过好友请求
     *
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping("/wxNewFriends")
    @ResponseBody
    public Response wxNewFriends(String json) throws Exception {
        log.info("好友自动通过--》【{}】", json);
        WxNewFriendsVo wxNewFriendsVo = JSON.parseObject(json, WxNewFriendsVo.class);
        //指令入库
        Map<String, Object> map = new HashMap<>(1);
        map.put("open", wxNewFriendsVo.isOpen());
        OpDeviceCmdVo vo = new OpDeviceCmdVo();
        System.out.println(map.toString());
        vo.setCmdParm(map.toString());
        vo.setDeviceId(wxNewFriendsVo.getDeviceId());
        vo.setComId(wxNewFriendsVo.getComId());
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.WX.getCode());
        vo.setCmdCd(CmdEnum.CmdCdEnum.wx_new_friends.getCode());
        Response response = opDeviceCmdService.issueCmd(vo);
        return response;
    }
    /**
     * 运动、朋友圈点赞
     *
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping("/wxLike")
    @ResponseBody
    public Response wxLike(String json) throws Exception {
        log.info("运动、朋友圈点赞--》【{}】", json);
        WxLikeVo wxLikeVo = JSON.parseObject(json, WxLikeVo.class);
        //指令入库
        Map<String, Object> map = new HashMap<>(1);
        map.put("sum", wxLikeVo.getSum());
        map.put("type",wxLikeVo.getType());
        OpDeviceCmdVo vo = new OpDeviceCmdVo();
        vo.setCmdParm(JSON.toJSONString(map));
        vo.setDeviceId(wxLikeVo.getDeviceId());
        vo.setComId(wxLikeVo.getComId());
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.WX.getCode());
        if(wxLikeVo.getType().equals("friends")){
            vo.setCmdCd(CmdEnum.CmdCdEnum.wx_friends_like.getCode());
        }else if (wxLikeVo.getType().equals("sports")){
            vo.setCmdCd(CmdEnum.CmdCdEnum.wx_sports_like.getCode());
        }else {
            throw new Exception("只可以在运动圈或朋友圈点赞哟！");
        }
        Response response = opDeviceCmdService.issueCmd(vo);
        return response;
    }

    /**
     * 好友、群、朋友圈消息
     *
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping("/wxSendMsg")
    @ResponseBody
    public Response wxSendMsg(String json) throws Exception {
        log.info("好友、群、朋友圈消息发送--》【{}】", json);
        WxSendMsgVo wxSendMsgVo = JSON.parseObject(json, WxSendMsgVo.class);
        if(null!=wxSendMsgVo.getImgUrls()&&wxSendMsgVo.getImgUrls().size()>9){
            throw new Exception("图片数量超出限制！ ");
        }
        //指令入库
        Map<String, Object> map = new HashMap<>(1);
        map.put("text", wxSendMsgVo.getText());
        map.put("imgUrls",wxSendMsgVo.getImgUrls());
        map.put("vidUrls",wxSendMsgVo.getVidUrls());
        map.put("channel",wxSendMsgVo.getChannel());
        OpDeviceCmdVo vo = new OpDeviceCmdVo();
        vo.setCmdParm(JSON.toJSONString(map));
        vo.setDeviceId(wxSendMsgVo.getDeviceId());
        vo.setComId(wxSendMsgVo.getComId());
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.WX.getCode());
        if(wxSendMsgVo.getChannel().contains("friends")){
            vo.setCmdCd(CmdEnum.CmdCdEnum.wx_friends_msg.getCode());
        }else if (wxSendMsgVo.getChannel().contains("fans")||wxSendMsgVo.getChannel().contains("group")){
            vo.setCmdCd(CmdEnum.CmdCdEnum.wx_send_msg.getCode());
        }else {
            throw new Exception("只可以在好友、群或朋友圈发消息哟！");
        }
        Response response = opDeviceCmdService.issueCmd(vo);
        return response;
    }

}
