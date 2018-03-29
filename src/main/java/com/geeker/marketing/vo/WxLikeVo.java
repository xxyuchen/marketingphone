package com.geeker.marketing.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2018/3/29 0029.
 */
@Data
public class WxLikeVo extends CommonVo{
    @NotNull(message = "类型不能为空！")
    private String type;
    @NotNull(message = "数量不能为空！")
    private Integer sum;
}
