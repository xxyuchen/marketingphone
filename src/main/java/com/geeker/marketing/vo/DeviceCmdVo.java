package com.geeker.marketing.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * Created by Administrator on 2018/3/29 0029.
 */
@Data
public class DeviceCmdVo {

    private String cmdId;

    private String cmdTypeCd;

    private String cmdCd;

    private String cmdParm;

    @NotNull(message = "用户id不能为空")
    private Integer userId;
}
