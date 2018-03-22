package com.geeker.marketing.enums;

/**
* @Author TangZhen
* @Date 2018/3/13 0013 13:40
* @Description  连接枚举
*/
public class ConnectEnum {

    /**
     * 连接状态
     */
    public enum ConnectStatusEnum {
        ONLINE(1, "在线"),
        OFFLINE(0,"离线");


        private Integer code;
        private String value;

        ConnectStatusEnum(int key, String value) {
            this.code = key;
            this.value = value;
        }

        public Integer getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static ConnectStatusEnum valueOf(Integer code) {
            ConnectStatusEnum[] enums = ConnectStatusEnum.values();
            for (ConnectStatusEnum e : enums) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }
            return null;
        }
    }
}
