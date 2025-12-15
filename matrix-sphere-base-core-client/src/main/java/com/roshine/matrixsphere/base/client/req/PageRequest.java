package com.roshine.matrixsphere.base.client.req;

import com.roshine.matrixsphere.base.client.response.ServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-04-30 19:19
 * @Description 分页请求参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageRequest extends ServiceRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 2384453720573764952L;
    /**
     * 当前页
     */
    @NotNull(message = "当前页 [必填]")
    private Integer pageNum = 1;

    /**
     * 每页查询数量
     */
    @NotNull(message = "每页查询数量[必填]")
    private Integer pageSize = 10;

    /**
     * 分页标识 是否分页
     */
    private boolean paging = true;
}
