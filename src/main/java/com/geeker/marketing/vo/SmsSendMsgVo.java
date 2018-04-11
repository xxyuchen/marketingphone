package com.geeker.marketing.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2018/4/10 0010.
 */
@Data
public class SmsSendMsgVo extends CommonVo {
    @NotNull(message = "短信内容不能为空！")
    private String parm;

    @NotNull(message = "发送号码不能为空！")
    private String secureNumber;
}
