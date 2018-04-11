package com.geeker.marketing.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2018/4/9 0009.
 */
@Data
public class WxCreateGroupVo extends CommonVo {

    private String groupName;

    private List<String> wxIds;

}
