package com.roshine.matrixsphere.base.client.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2020-03-21 12:11
 * @Description 钉钉消息配置工具类
 */
@Slf4j
@Component
public class DingDingUtils {

    @Value("${dingding.webhock.url}")
    private String dingDingWebHockUrl;

    @Value("${dingding.send}")
    private boolean dingDingSendAble;

    public void sendMsg(String title, String content) {
        JSONObject data = new JSONObject();
        data.put("msgtype", "markdown");
        JSONObject markDownContent = new JSONObject();
        markDownContent.put("title", title);
        markDownContent.put("text", "### " + title + "\n ### 描述：<font color='#DC143C'>" + content + "</font>");
        data.put("markdown", markDownContent);
        try {
            HttpUtil.createPost(dingDingWebHockUrl)
                    .body(JSONUtil.toJsonStr(data))
                    .execute();
        }catch (Exception e) {
            log.error("发送钉钉消息异常", e);
        }
    }
}
