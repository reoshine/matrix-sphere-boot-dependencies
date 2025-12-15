package com.roshine.matrixsphere.base.client.response;

import com.github.pagehelper.Page;
import com.roshine.matrixsphere.base.client.dto.PageResponse;
import com.roshine.matrixsphere.base.client.req.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-04-30 19:05
 * @Description 封装pageHelper
 */
@Data
public class PageHelper<T> implements Serializable {

    private static final long serialVersionUID = 4363835485260121020L;
    /**
     * 页码 当前页
     */
    private int pageNum;

    /**
     * 每页的大小
     */
    private int pageCount;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 所有数据
     */
    private List<T> data;

    /**
     * 分页结果
     */
    private Page<T> page;

    /**
     * 私有化构造器
     * @param pageCount
     * @param pageNum
     */
    private PageHelper(int pageCount, int pageNum) {
        this.pageCount = pageCount;
        this.pageNum = pageNum;
    }

    public static <T> PageHelper<T> init(PageRequest request) {
        PageHelper<T> dto = new PageHelper<>(request.getPageNum(), request.getPageSize());
        if (request.isPaging()) {
            dto.page = com.github.pagehelper.PageHelper.startPage(request.getPageNum(), request.getPageSize());
        }else {
            dto.page = null;
        }
        return dto;
    }

    public PageHelper<T> call(AbstractDataCallback<T> callback) {
        this.data = callback.dataProcessing();
        return this;
    }

    public PageResponse<T> result() {
        PageResponse<T> dto = new PageResponse<>();
        dto.setPageSize(this.page == null ? Integer.MIN_VALUE : this.page.getPageSize());
        dto.setPageNum(this.page == null ? Integer.MIN_VALUE : this.page.getPageNum());
        dto.setTotal(this.page == null ? Integer.MIN_VALUE : this.page.getTotal());
        dto.setList(this.data == null ? this.page.getResult() : this.data);
        return dto;
    }
}
