package com.geeker.marketing.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 基础的响应消息实现
 *
 * @author caoquanlong
 */
@Getter
@Setter
@EqualsAndHashCode
public class CamelResponse implements Response {

    /**
     * 是否成功
     */
    protected boolean success;

    /**
     * 响应码，当success为false时，code值代表服务端错误状态
     */
    protected Integer code;

    /**
     * 响应的消息，当success为false时，message值为服务端的错误信息内容
     */
    protected String message;

    /**
     * 响应的数据内容，success为false，该字段不会有值
     */
    protected Object data;

    /**
     * 默认不成功的构造方法
     */
    CamelResponse() {
        this(false);
    }

    /**
     * 响应成功的数据结果
     *
     * @param data 要响应的数据
     */
    CamelResponse(Object data) {
        this(true, 200, "操作成功！", data);
    }

    /**
     * 初始化状态的构造方法
     *
     * @param success 是否成功
     */
    CamelResponse(boolean success) {
        this(success, success ? 0 : -1, null, null);
    }

    /**
     * 构造一个响应错误的结果
     *
     * @param code    错误码
     * @param message 错误信息
     */
    CamelResponse(Integer code, String message) {
        this(false, code, message, null);
    }

    /**
     * 初始化构造方法
     *
     * @param success 是否成功
     * @param code    状态码
     * @param message 响应消息
     * @param data    响应的数据对象
     */
    CamelResponse(boolean success, Integer code, String message, Object data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @Override
    public Object getData(String dataName) {
        return this.data != null && this.data instanceof Map ? ((Map) this.data).get(dataName) : null;
    }

    @Override
    public void putData(String dataName, Object dataObj) {
        if (this.data == null) {
            this.data = new HashMap(10);
        }

        if (!(this.data instanceof Map)) {
            throw new RuntimeException("data对象已有值且不是Map类型！");
        }

        ((Map) this.data).put(dataName, dataObj);
    }
}