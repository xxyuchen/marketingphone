package com.geeker.marketing.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2018/3/27 0027.
 */
@Data
public class PhoneBookVo extends CommonVo{

    private List<PhoneVo> mobiles;

    private List<String> delMobiles;
}
