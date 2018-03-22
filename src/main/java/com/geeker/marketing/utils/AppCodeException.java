package com.geeker.marketing.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Lubin.Xuan on 2018-01-16.
 * {desc}
 */
@Setter
@Getter
public class AppCodeException extends RuntimeException {
    private int code;

    public AppCodeException(int code) {
        this.code = code;
    }

    public AppCodeException(String message, int code) {
        super(message);
        this.code = code;
    }
}
