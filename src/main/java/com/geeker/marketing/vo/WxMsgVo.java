package com.geeker.marketing.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Administrator on 2018/3/30 0030.
 */
@Data
public class WxMsgVo {

    private String wxUinId;

    private Integer comId;

    private String wxId;

    private String wxGroupId;

    private String msgImageUri;

    private String msgText;

    private String msgType;

    private String fansType;

    private Boolean msgDirect;

    private Boolean msgPlatform;

    private String microSerivce;

    @NotNull(message = "发送状态不能为空！")
    private Integer status;

    private Date receiveTime;

    private Date sendTime;
}
