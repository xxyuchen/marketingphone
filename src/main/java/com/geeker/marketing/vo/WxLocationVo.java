package com.geeker.marketing.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/3/28 0028.
 */
@Data
public class WxLocationVo extends CommonVo{

    //纬度
    private BigDecimal latitude;

    //经度
    private BigDecimal longtitude;

    private int lac = 0;

    private int cid = 0;

    private Integer range;

    private boolean open = true;


}
