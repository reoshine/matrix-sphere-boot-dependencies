package com.roshine.matrixsphere.base.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-05-01 22:44
 * @Description
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PageResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 6293794348752145352L;

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 总数
     */
    private long total;

    /**
     * 当前页
     */
    private long pageNum;

    /**
     * 每页大小
     */
    private long pageSize;

    /**
     * 【核心实现】返回一个空的 DomainPageResult 对象
     * 泛型方法，自动适配调用方的类型
     *
     * @param <T> 目标类型
     * @return 空分页对象
     */
    public static <T> PageResponse<T> empty() {
        // 返回：空列表，总数0，默认第1页，默认每页10条(或者0，视业务定义而定)
        return new PageResponse<>(
                Collections.emptyList(),
                0L,
                1L,
                10L
        );
    }

    /**
     * 可选：顺便提供一个标准构建方法
     */
    public static <T> PageResponse<T> of(List<T> list, long total, long pageNum, long pageSize) {
        return new PageResponse<>(list, total, pageNum, pageSize);
    }
}
