package com.roshine.adp.base.client.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-05-01 22:44
 * @Description
 */
@Data
public class PageResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 6293794348752145352L;

    private Long total;

    private Integer pageCount;

    private Integer pageNum;

    private List<T> data;
}
