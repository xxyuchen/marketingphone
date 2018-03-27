package com.geeker.marketing.utils;

import java.util.Date;
import java.util.Random;

/**
 * @author xuzao
 * @description smslog id 生成类
 * @create 2017-06-23 14:53
 **/
public class FactoryIdUtils {

    //id 生成规则  ‘YYYYMMDDHHMI’+时间随机4位
    public synchronized static final String createId() {
        Random random = new Random();
        return new StringBuilder(DateUtils.format(new Date(), "yyyyMMddHHmm")).append(random.nextInt(9999)).toString();
    }
}
