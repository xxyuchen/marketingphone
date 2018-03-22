package com.geeker.marketing.enums;

/**
 * Created by Administrator on 2018/2/2 0002.
 */
public class DeviceEnum {

    /**
     * 任务状态
     */
    public enum deviceStatusEnum {
        STAGING(0, "暂存"),
        WAITING(1, "等待中"),
        DODING(2, "执行中"),
        STOP(3, "中断"),
        OVER(5, "完成"),
        DELETE(9,"伪删除");


        private Integer code;
        private String value;

        deviceStatusEnum(int key, String value) {
            this.code = key;
            this.value = value;
        }

        public Integer getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static deviceStatusEnum valueOf(Integer code) {
            deviceStatusEnum[] enums = deviceStatusEnum.values();
            for (deviceStatusEnum e : enums) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }
            return null;
        }
    }
}
