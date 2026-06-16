package com.roshine.matrixsphere.base.client.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author roshine
 * @version 2.0.0
 * 通用分页请求参数 (Client DTO)
 */
@Data
public class PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 2384453720573764952L;

    @NotNull(message = "当前页不能为空")
    @Min(value = 1, message = "当前页最小为 1")
    private Integer pageNum = 1;

    @NotNull(message = "每页数量不能为空")
    @Min(value = 1, message = "每页数量最小为 1")
    private Integer pageSize = 10;

    /**
     * 是否执行分页查询 (某些场景需要查全量数据)
     */
    private boolean paging = true;
}