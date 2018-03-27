package com.geeker.marketing.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Administrator on 2018/3/27 0027.
 */
@Data
public class PhoneBookVo {

    @NotNull(message = "通讯录不能为空！")
    private List<PhoneVo> mobiles;

    @NotNull(message = "设备id不能为空！")
    private String deviceId;

    @NotNull(message = "组织id不能为空！")
    private Integer comId;
}
