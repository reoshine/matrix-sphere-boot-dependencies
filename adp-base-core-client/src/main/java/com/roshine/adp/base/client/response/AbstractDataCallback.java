package com.roshine.adp.base.client.response;

import java.util.List;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-04-30 19:40
 * @Description
 */
public abstract class AbstractDataCallback<T> {

    /**
     * 处理返回结果
     * @return
     */
    public abstract List<T> dataProcessing();
}
