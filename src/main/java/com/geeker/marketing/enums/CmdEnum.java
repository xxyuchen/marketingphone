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

    /**
     * 指令类别编码
     */
    public enum TypeCdEnum {
        SYS("sys", "系统级指令"),
        CALL("call", "电话指令"),
        SMS("sms", "短信指令"),
        WX("wx" , "微信指令");


        private String code;
        private String value;

        TypeCdEnum(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static TypeCdEnum getValue(String key) {
            for (TypeCdEnum examType : TypeCdEnum.values()) {
                if (examType.getCode().equals(key)) {
                    return examType;
                }
            }
            return null;
        }
    }

    /**
     * 指令类别编码
     */
    public enum CmdCdEnum {
        sys_update_app("sys_update_app", "更新app"),
        sys_delete_app("sys_delete_app", "删除指定app应用"),
        sys_open_screen("sys_open_screen", "点亮手机屏幕"),
        sys_close_screen("sys_close_screen", "关闭手机屏幕"),
        sys_unlock("sys_unlock", "解锁"),
        sys_lock("sys_lock", "加密"),
        sys_reboot("sys_reboot", "重启"),
        sys_shutdown("sys_shutdown", "关机"),
        sys_bound("sys_bound", "绑定"),
        sys_remove_bound("sys_remove_bound", "解绑"),
        sys_open_gps("sys_open_gps", "开启GPS"),
        sys_login_geeker("sys_login_geeker", "登录数聚客"),
        wx_send_msg("wx_send_msg", "微信好友、群消息"),
        wx_friends_msg("wx_friends_msg", "微信朋友圈消息"),
        wx_create_group("wx_create_group", "自动拉群"),
        wx_friends_like("wx_friends_like", "朋友圈点赞"),
        wx_sports_like("wx_sports_like", "运动圈点赞"),
        wx_hello_conf("wx_hello_conf", "微信招呼语设置"),
        wx_location("wx_location", "微信定位站街"),
        wx_add_fans("wx_add_fans", "精准加人"),
        wx_add_round_fans("wx_add_round_fans", "添加附近人群"),
        wx_sync_fans_info("wx_sync_fans_info", "微信好友、群全量同步"),
        wx_find_zombie_fans("wx_find_zombie_fans", "查找设备僵尸粉"),
        wx_send_visiting_card("wx_send_visiting_card", "发送个人名片"),
        wx_send_money("wx_send_money", "发送红包"),
        call_book("call_book", "同步通讯录"),
        call_group("call_group", "同步群组"),
        wx_new_friends("wx_new_friends", "通过新好友"),
        wx_new_group_fans("wx_new_group_fans", "通过一个新群友"),
        wx_say_hello("wx_say_hello", "陌生人打招呼"),
        call_in("call_in", "新客户呼入事件"),
        call_out("call_out", "终端呼出事件"),
        call_in_missed("call_in_missed", "客户呼入未接"),
        call_out_missed("call_out_missed", "终端呼出未接"),
        call_upLoad_voice("call_upload_record", "上传通讯录"),
        sms_rec_new_msg("sms_rec_new_msg", "接收到新短信信息"),
        sms_send_msg("sms_send_msg", "发送短信"),
        sys_device_info("sys_device_info", "上报硬件参数、设备标示信息"),
        sys_device_gps("sys_device_gps", "上报设备gps数据"),
        sys_warn_black_app("sys_warn_black_app", "警告，发现禁用APP"),
        sys_warn_change_sim("sys_warn_change_sim", "警告，发现变更sim卡"),
        sys_warn_change_wx("sys_warn_change_wx", "警告，发现变更微信账号"),
        sys_warn_app("sys_warn_app", "应用发生错误");


        private String code;
        private String value;

        CmdCdEnum(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static CmdCdEnum getValue(String key) {
            for (CmdCdEnum examType : CmdCdEnum.values()) {
                if (examType.getCode().equals(key)) {
                    return examType;
                }
            }
            return null;
        }
    }

    /**
     * 指令类型
     */
    public enum CmdStatusEnum {
        REPORT("report", "主动上标指令"),
        ISSUE("issue", "下发指令上报");

        private String key;
        private String value;

        CmdStatusEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public static CmdStatusEnum valueOf(Integer code) {
            CmdStatusEnum[] enums = CmdStatusEnum.values();
            for (CmdStatusEnum e : enums) {
                if (e.getKey().equals(code)) {
                    return e;
                }
            }
            return null;
        }
    }
}
