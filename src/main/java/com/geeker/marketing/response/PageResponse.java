package com.geeker.marketing.response;

/**
 * 分页的返回结果
 *
 * @author caoquanlong
 */
public interface PageResponse extends Response {

    /**
     * 获取分页信息
     *
     * @return 返回分页信息的对象
     */
    Page getPage();
}