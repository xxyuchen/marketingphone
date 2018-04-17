package com.geeker.marketing.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17 0017.
 */
@Data
public class GroupBookVo extends CommonVo{

    private List<GroupVo> groups;

    private List<Integer> delGroups;
}
