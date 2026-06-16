package com.roshine.matrixsphere.base.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * @author roshine
 * @version 2.0.0
 * 统一分页响应体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private long total;
    private List<T> list;

    public static <T> PageResponse<T> empty() {
        return new PageResponse<>(0L, Collections.emptyList());
    }

    public static <T> PageResponse<T> of(long total, List<T> list) {
        return new PageResponse<>(total, list);
    }
}