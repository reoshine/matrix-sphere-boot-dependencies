package com.roshine.adp.base.client.utils;


import cn.hutool.core.util.RandomUtil;

import java.util.UUID;

public class UUIDUtils {

    /**
     * 生成指定位数的随机字符串  (纯数字)
     *
     * @param number :
     * @return
     */
    public static String getRandomNumber(Integer number) {
        return RandomUtil.randomNumbers(number);
    }


    /**
     * 生成随机[a-z]字符串，包含大小写
     *
     * @param number :
     * @return
     */
    public static String getRandomString(Integer number) {
        return RandomUtil.randomNumbers(number);
    }


    /**
     * 生成从ASCII 32到126组成的随机字符串
     *
     * @param number :
     * @return
     */
    public static String getRandomAscii(Integer number) {
        return RandomUtil.randomNumbers(number);
    }


    /**
     * 生成没有 "-" 的uuid随机字符串  (包含字母和数字)
     *
     * @return
     */
    public static String getUuid() {
        //replace("-","") : 把 "-"  该为空字符串  ""
        return UUID.randomUUID().toString().replace("-", "");
    }

}
