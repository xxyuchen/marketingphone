package com.geeker.marketing.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2018/3/30 0030.
 */
@Data
public class WxSendMsgVo extends CommonVo{

    private String channel;

    private String text;

    private List<String> imgUrls;

    private List<String> vidUrls;
}
