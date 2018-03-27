package com.geeker.marketing.response;

import com.github.pagehelper.PageInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author caoquanlong
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class CamelPageResponse<T> extends CamelResponse implements PageResponse {

    protected Boolean success = true;
    protected String message = "操作成功！";
    protected Integer code = 200;
    protected PageData data;

    /**
     * 默认初始化构造方法
     * @param pageInfo 返回的分页数据
     */
    CamelPageResponse(PageInfo<T> pageInfo) {
        Page page = new Page(pageInfo.getPageNum(), pageInfo.getPageSize(),
                pageInfo.getStartRow(), pageInfo.getEndRow(),
                pageInfo.getTotal(), pageInfo.getPages());
        data = new PageData(page, pageInfo.getList());
    }

    @Override
    public Page getPage() {
        return data != null ? data.getPage() : null;
    }

    @Override
    public Object getData() {
        return data != null ? data.getDataList() : null;
    }

    @Getter
    @Setter
    private class PageData {
        private Page page;
        private List<T> dataList;

        PageData(Page page, List<T> dataList) {
            this.page = page;
            this.dataList = dataList;
        }
    }
}