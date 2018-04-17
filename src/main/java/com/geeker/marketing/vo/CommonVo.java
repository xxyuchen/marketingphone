package com.geeker.marketing.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2018/3/28 0028.
 */
@Data
public class CommonVo {

    @NotNull(message = "设备id不能为空！")
    private String deviceId;

    @NotNull(message = "组织id不能为空！")
    private Integer comId;

    @NotNull(message = "用户id不能为空！")
    private Integer userId;
}
