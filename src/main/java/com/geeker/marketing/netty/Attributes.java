package com.geeker.marketing.netty;

import io.netty.util.AttributeKey;

/**
 * Created by Lubin.Xuan on 2018-02-07.
 * {desc}
 */
public interface Attributes {
    AttributeKey<Boolean> AUTHENTICATED_ATTR = AttributeKey.valueOf("AUTHENTICATED");
    AttributeKey<String> DEVICE_ID_ATTR = AttributeKey.valueOf("deviceId");
    AttributeKey<String> DEVICE_TYPE = AttributeKey.valueOf("deviceType");
}
