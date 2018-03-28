package com.geeker.marketing.listener;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
* @Author TangZhen
* @Date 2018/3/28 0028 16:40
* @Description  事件
*/
@Component
public class PublicEvent {

    public static class AddClientEvent extends ApplicationEvent {
        @Getter
        private String deviceId;

        public AddClientEvent(Object source,String deviceId) {
            super(source);
            this.deviceId = deviceId;
        }
    }

    public static class IssueCmdEvent extends ApplicationEvent {
        @Getter
        private String cmdId;

        public IssueCmdEvent(Object source,String cmdId) {
            super(source);
            this.cmdId = cmdId;
        }
    }
}
