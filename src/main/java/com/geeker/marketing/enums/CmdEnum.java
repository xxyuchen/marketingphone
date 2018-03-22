package com.geeker.marketing.enums;

/**
* @Author TangZhen
* @Date 2018/2/2 0002 11:06
* @Description  指令枚举类
*/
public class CmdEnum {
    /**
     * 发送状态
     */
    public enum DeliverStatusEnum {
        UNDO(0, "未下发"),
        DO(1, "已下发"),
        FAIL(2, "失败");

        private Integer code;
        private String value;

        DeliverStatusEnum(int key, String value) {
            this.code = key;
            this.value = value;
        }

        public Integer getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static DeliverStatusEnum valueOf(Integer code) {
            DeliverStatusEnum[] enums = DeliverStatusEnum.values();
            for (DeliverStatusEnum e : enums) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }
            return null;
        }
    }

    /**
     * 接受状态
     */
    public enum ReceiveStatusEnum {
        HAVE(0, "已接收"),
        NORES(1 , "未响应");


        private Integer code;
        private String value;

        ReceiveStatusEnum(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

        public Integer getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static ReceiveStatusEnum getValue(String key) {
            for (ReceiveStatusEnum examType : ReceiveStatusEnum.values()) {
                if (examType.getCode().equals(key)) {
                    return examType;
                }
            }
            return null;
        }
    }
}
