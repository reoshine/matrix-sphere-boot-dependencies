package com.roshine.matrixsphere.base.client.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * @author roshine
 * @version 2.0.0
 * 国密 SM4 对称加密工具类 (基于 Hutool 极致重构)
 */
public class Sm4Utils {

    /**
     * 加密为 16 进制字符串
     *
     * @param plainText 明文
     * @param key       16字节（128位）的密钥
     */
    public static String encryptHex(String plainText, String key) {
        SymmetricCrypto sm4 = SmUtil.sm4(key.getBytes(CharsetUtil.CHARSET_UTF_8));
        return sm4.encryptHex(plainText);
    }

    /**
     * 从 16 进制字符串解密
     *
     * @param cipherTextHex 密文
     * @param key           16字节（128位）的密钥
     */
    public static String decryptStr(String cipherTextHex, String key) {
        SymmetricCrypto sm4 = SmUtil.sm4(key.getBytes(CharsetUtil.CHARSET_UTF_8));
        return sm4.decryptStr(cipherTextHex, CharsetUtil.CHARSET_UTF_8);
    }
}