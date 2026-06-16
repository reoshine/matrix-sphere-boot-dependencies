package com.roshine.matrixsphere.base.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.roshine.matrixsphere.base.client.login.LoginInfoHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author roshine
 * @version 2.0.0
 * MyBatis-Plus 自动填充字段策略
 */
@Slf4j
@Component
public class MpMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 从 ThreadLocal 中获取当前登录用户的 ID (如果没有，默认为 "system")
        String userId = LoginInfoHelper.getUserId() != null ? LoginInfoHelper.getUserId() : "system";

        // 统一使用 String 类型的用户ID (适配 SSO 重构)
        this.strictInsertFill(metaObject, "createBy", String.class, userId);
        this.strictInsertFill(metaObject, "gmtCreate", LocalDateTime.class, LocalDateTime.now());

        // 我们没有 modify 相关的必须字段可以在插入时不填，看业务需要
        this.strictInsertFill(metaObject, "modifyBy", String.class, userId);
        this.strictInsertFill(metaObject, "gmtModify", LocalDateTime.class, LocalDateTime.now());

        this.strictInsertFill(metaObject, "isDeleted", Integer.class, 0);
        this.strictInsertFill(metaObject, "version", Long.class, 0L);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String userId = LoginInfoHelper.getUserId() != null ? LoginInfoHelper.getUserId() : "system";

        this.strictUpdateFill(metaObject, "modifyBy", String.class, userId);
        this.strictUpdateFill(metaObject, "gmtModify", LocalDateTime.class, LocalDateTime.now());
    }
}