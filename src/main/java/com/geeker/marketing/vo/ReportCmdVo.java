package com.geeker.marketing.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Administrator on 2018/3/30 0030.
 */
@Data
public class ReportCmdVo {

    private String deviceId;

    private String cmdId;

    private Integer comId;

    @NotNull(message = "指令编码类型不能为空！")
    private String cmdTypeCd;

    @NotNull(message = "指令编码不能为空！")
    private String cmdCd;

    @NotNull(message = "指令类型不能为空！")
    private String rspAction;

    private String data;

    @NotNull(message = "执行时间不能为空！")
    private Date finish;

    private Integer code;

    private Date queueTime;

    private String queue;

    private String messageId;
}
