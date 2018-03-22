package com.geeker.marketing.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Lubin.Xuan on 2017-11-06.
 * {desc}
 */
@Getter
@Setter
public abstract class ReplyRequest implements Serializable {
    @JSONField(serialize = false)
    @NotNull(message = "{reply.to.null}")
    private String replyTo;
}
