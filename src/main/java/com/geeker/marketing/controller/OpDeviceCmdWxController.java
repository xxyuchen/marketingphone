package com.geeker.marketing.controller;

import com.alibaba.fastjson.JSON;
import com.geeker.marketing.enums.CmdEnum;
import com.geeker.marketing.response.Response;
import com.geeker.marketing.service.OpDeviceCmdService;
import com.geeker.marketing.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
* @Author TangZhen
* @Date 2018/4/10 0010 13:28
* @Description 下发指令--微信业务
*/
@Slf4j
@RestController
@RequestMapping("cmd/wx")
public class OpDeviceCmdWxController {
    @Resource
    private OpDeviceCmdService opDeviceCmdService;
    /**
     * 精准加人
     *
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping("/addAccurate")
    public Response addAccurate(@RequestBody String json) throws Exception {
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
        vo.setUserId(custVo.getUserId());
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
    public Response wxLocation(@RequestBody String json) throws Exception {
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
        vo.setCmdParm(JSON.toJSONString(map));
        vo.setDeviceId(wxLocationVo.getDeviceId());
        vo.setComId(wxLocationVo.getComId());
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.WX.getCode());
        vo.setCmdCd(CmdEnum.CmdCdEnum.wx_location.getCode());
        vo.setUserId(wxLocationVo.getUserId());
        Response response = opDeviceCmdService.issueCmd(vo);
        return response;
    }

    /**
     * 附近加人
     *
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping("/wxAddRoundFans")
    public Response wxAddRoundFans(@RequestBody String json) throws Exception {
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
        vo.setCmdParm(JSON.toJSONString(map));
        vo.setDeviceId(wxLocationVo.getDeviceId());
        vo.setComId(wxLocationVo.getComId());
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.WX.getCode());
        vo.setUserId(wxLocationVo.getUserId());
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
    public Response wxNewFriends(@RequestBody String json) throws Exception {
        log.info("好友自动通过--》【{}】", json);
        WxNewFriendsVo wxNewFriendsVo = JSON.parseObject(json, WxNewFriendsVo.class);
        //指令入库
        Map<String, Object> map = new HashMap<>(1);
        map.put("open", wxNewFriendsVo.isOpen());
        OpDeviceCmdVo vo = new OpDeviceCmdVo();
        vo.setCmdParm(JSON.toJSONString(map));
        vo.setDeviceId(wxNewFriendsVo.getDeviceId());
        vo.setComId(wxNewFriendsVo.getComId());
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.WX.getCode());
        vo.setCmdCd(CmdEnum.CmdCdEnum.wx_new_friends.getCode());
        vo.setUserId(wxNewFriendsVo.getUserId());
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
    public Response wxLike(@RequestBody String json) throws Exception {
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
        vo.setUserId(wxLikeVo.getUserId());
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
    public Response wxSendMsg(@RequestBody String json) throws Exception {
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
        vo.setUserId(wxSendMsgVo.getUserId());
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

    /**
     * 自动拉群
     *
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping("/wxCreateGroup")
    public Response wxCreateGroup(@RequestBody String json) throws Exception {
        log.info("自动拉群--》【{}】", json);
        WxCreateGroupVo wxCreateGroupVo = JSON.parseObject(json, WxCreateGroupVo.class);
        if(null!=wxCreateGroupVo.getWxIds()&&wxCreateGroupVo.getWxIds().size()<=0){
            throw new Exception("请选择组群好友！");
        }
        //指令入库
        Map<String, Object> map = new HashMap<>(1);
        map.put("groupName", wxCreateGroupVo.getGroupName());
        map.put("wxIds",wxCreateGroupVo.getWxIds());
        OpDeviceCmdVo vo = new OpDeviceCmdVo();
        vo.setCmdParm(JSON.toJSONString(map));
        vo.setDeviceId(wxCreateGroupVo.getDeviceId());
        vo.setComId(wxCreateGroupVo.getComId());
        vo.setCmdTypeCd(CmdEnum.TypeCdEnum.WX.getCode());
        vo.setCmdCd(CmdEnum.CmdCdEnum.wx_create_group.getCode());
        vo.setUserId(wxCreateGroupVo.getUserId());
        Response response = opDeviceCmdService.issueCmd(vo);
        return response;
    }
}
