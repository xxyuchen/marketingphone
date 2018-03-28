package com.geeker.marketing.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2018/3/28 0028.
 */
@Data
public class CustVo extends CommonVo {

    private List<String> mobiles;

    private List<String> wxIds;

    private List<String> nicknames;

}
