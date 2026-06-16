package com.roshine.matrixsphere.base.client.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author roshine
 * @version 2.0.0
 * 钉钉机器人告警工具类 (已移除 Fastjson，基于 Hutool 极致重构)
 */
@Slf4j
public class DingDingUtils {

    /**
     * 发送钉钉文本消息
     *
     * @param webhook 钉钉机器人 Webhook 地址
     * @param content 消息内容
     * @param isAtAll 是否 @ 所有人
     */
    public static void sendTextMessage(String webhook, String content, boolean isAtAll) {
        try {
            // 1. 构建请求体 (使用标准的 Map 构建，交由 Hutool 序列化)
            Map<String, Object> textMap = new HashMap<>();
            textMap.put("content", content);

            Map<String, Object> atMap = new HashMap<>();
            atMap.put("isAtAll", isAtAll);

            Map<String, Object> reqBody = new HashMap<>();
            reqBody.put("msgtype", "text");
            reqBody.put("text", textMap);
            reqBody.put("at", atMap);

            // 2. 转换为 JSON 字符串 (使用 Hutool 内置的安全 JSON 工具)
            String jsonStr = JSONUtil.toJsonStr(reqBody);

            // 3. 发送 POST 请求并设置超时时间 (3000ms)
            String result = HttpUtil.post(webhook, jsonStr, 3000);

            log.info("钉钉告警发送完成，响应结果: {}", result);
        } catch (Exception e) {
            log.error("钉钉告警发送失败, webhook: {}", webhook, e);
        }
    }
}