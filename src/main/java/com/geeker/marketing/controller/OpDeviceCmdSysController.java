package com.geeker.marketing.controller;

import com.geeker.marketing.netty.ClientHolder;
import com.geeker.marketing.response.Response;
import com.geeker.marketing.service.OpDeviceCmdService;
import com.geeker.marketing.vo.OpDeviceCmdVo;
import lombok.extern.slf4j.Slf4j;
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

}
