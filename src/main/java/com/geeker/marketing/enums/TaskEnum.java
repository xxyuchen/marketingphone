package com.geeker.marketing.enums;

/**
* @Author TangZhen
* @Date 2018/2/2 0002 11:06
* @Description  任务枚举类
*/
public class TaskEnum {
    /**
     * 任务状态
     */
    public enum TaskStatusEnum {
        STAGING(0, "暂存"),
        DOING(2, "执行中"),
        STOP(3, "中断"),
        COMPLETE(5, "完成"),
        DELETE(9, "删除");


        private Integer code;
        private String value;

        TaskStatusEnum(int key, String value) {
            this.code = key;
            this.value = value;
        }

        public Integer getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static TaskStatusEnum valueOf(Integer code) {
            TaskStatusEnum[] enums = TaskStatusEnum.values();
            for (TaskStatusEnum e : enums) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }
            return null;
        }
    }

    /**
     * 任务渠道
     */
    public enum TaskChannelEnum {
        FIRENDS("firends", "朋友圈"),
        FANS("fans", "微信好友"),
        GROUP("group", "微信群");


        private String code;
        private String value;

        TaskChannelEnum(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static TaskChannelEnum getValue(String key) {
            for (TaskChannelEnum examType : TaskChannelEnum.values()) {
                if (examType.getCode().equals(key)) {
                    return examType;
                }
            }
            return null;
        }
    }
}
