package com.geeker.marketing.controller;

import com.geeker.marketing.response.Response;
import com.geeker.marketing.response.ResponseUtils;
import com.geeker.marketing.service.OpDeviceCmdService;
import com.geeker.marketing.service.OpDeviceReportService;
import com.geeker.marketing.vo.OpDeviceCmdVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
* @Author TangZhen
* @Date 2018/4/10 0010 13:34
* @Description  下发指令--系统业务
*/
@Slf4j
@RestController
@RequestMapping("cmd/sys")
public class OpDeviceCmdSysController {
    @Resource
    private OpDeviceCmdService opDeviceCmdService;

    @Resource
    private OpDeviceReportService opDeviceReportService;


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
     * 绑定设备
     *
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping("/boundDevice")
    @ResponseBody
    public Response boundDevice(@RequestBody String json) throws Exception {
        log.info("绑定设备指令下发--》【{}】", json);
        return opDeviceCmdService.boundDevice(json);
    }

    /**
     * 解除绑定
     *
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping("/removeBound")
    @ResponseBody
    public Response removeBound(@RequestBody String json) throws Exception {
        log.info("解除绑定指令下发--》【{}】", json);
        return opDeviceCmdService.removeBound(json);
    }

    /**
     * 登录指令
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping("/loginGeeker")
    @ResponseBody
    public Response loginGeeker(@RequestBody String json) throws Exception {
        log.info("登录指令指令上报--》【{}】", json);
        opDeviceReportService.loginGeeker(json);
        return ResponseUtils.success();
    }

}
