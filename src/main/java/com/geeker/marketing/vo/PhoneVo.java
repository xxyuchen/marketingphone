package com.geeker.marketing.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2018/3/27 0027.
 */
@Data
public class PhoneVo {
    @NotNull(message = "通讯昵称不能为空！")
    private String nickname;

    @NotNull(message = "通讯展示号码不能为空！")
    private String secureNumber;

    @NotNull(message = "通信号码不能为空！")
    private String phone;
}
