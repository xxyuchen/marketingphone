package com.geeker.marketing.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by Administrator on 2018/3/30 0030.
 */
@Data
public class WxEventVo {

    private String wxUinId;

    private String wxId;

    private String eventCd;

    private Integer status;

    private Date receiveTime;
}
