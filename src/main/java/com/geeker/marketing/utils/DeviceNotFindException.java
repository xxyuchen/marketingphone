package com.geeker.marketing.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Lubin.Xuan on 2018-01-16.
 * {desc}
 */
@Setter
@Getter
public class DeviceNotFindException extends RuntimeException {
    private int code;

    public DeviceNotFindException(int code) {
        this.code = code;
    }

    public DeviceNotFindException(String message, int code) {
        super(message);
        this.code = code;
    }
}
