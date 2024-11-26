package com.roshine.matrixsphere.base.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2022-08-05 23:56
 */
@Slf4j
@Component
public class MpMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时的填充策略
     *
     * @param metaObject 元数据
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.fillStrategy(metaObject, "createBy", 1L);
        this.fillStrategy(metaObject, "createByName", "roshine");
        this.fillStrategy(metaObject, "gmtCreate", LocalDateTime.now());
        this.fillStrategy(metaObject, "modifyBy", 1L);
        this.fillStrategy(metaObject, "modifyByName", "roshine");
        this.fillStrategy(metaObject, "gmtModify", LocalDateTime.now());
        this.fillStrategy(metaObject, "isDeleted", 0);
        this.fillStrategy(metaObject, "version", 0L);
    }

    /**
     * 更新时的填充策略
     *
     * @param metaObject 元数据
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.fillStrategy(metaObject, "modifyBy", 1L);
        this.fillStrategy(metaObject, "modifyByName", "roshine");
        this.fillStrategy(metaObject, "gmtModify", LocalDateTime.now());
    }
}