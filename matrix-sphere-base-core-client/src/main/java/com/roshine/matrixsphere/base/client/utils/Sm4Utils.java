package com.roshine.matrixsphere.base.client.utils;

import org.apache.commons.codec.binary.Base64;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author roshine
 * @version 1.0.0
 * @since 2024-12-22 23:05
 */
public class Sm4Utils {

    /**
     * 默认 SECRET_KEY
     * 当时用ECB模式的时候，和前端key一致
     * secretKey 必须为16位，可包含字母、数字、标点
     */
    private static final String SECRET_KEY = "BKgx!#Soft123...";

    /**
     * 默认 IV
     * 当时用CBC模式的时候，SECRET_KEY和IV都需要传值，解密要和加密的SECRET_KEY和IV一致，更加安全
     * iv 必须为 16 位，可包含字母、数字、标点
     */
    private static final String IV = "ZkR_SiNoSOFT=568";

    private static final boolean HEX_STRING = false;

    public Sm4Utils() {

    }

    /**
     * 不要在方法里定义正则表达式规则,应定义为常量或字段,能加快正则匹配速度
     */
    private static final Pattern P = Pattern.compile("\\s*|\t|\r|\n");

    /**
     * ECB模式加密，自定义密钥，加解密密钥需一致
     *
     * @param plainText 要加密的数据
     * @param secretKey 密钥，必须为 16 位，可包含字母、数字、标点
     * @return String
     */
    public static String encryptData_ECB(String plainText, String secretKey) {
        try {
            Sm4Context ctx = new Sm4Context();
            ctx.isPadding = true;
            ctx.mode = Sm4.SM4_ENCRYPT;

            byte[] keyBytes;
            keyBytes = secretKey.getBytes();
            Sm4 sm4 = new Sm4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4_crypt_ecb(ctx, plainText.getBytes(StandardCharsets.UTF_8));
            String cipherText = Base64.encodeBase64String(encrypted);
            if (cipherText != null && !cipherText.trim().isEmpty()) {
                Matcher m = P.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * ECB模式加密，默认密钥
     *
     * @param plainText 要加密的数据
     * @return String
     */
    public static String encryptData_ECB(String plainText) {
        try {
            Sm4Context ctx = new Sm4Context();
            ctx.isPadding = true;
            ctx.mode = Sm4.SM4_ENCRYPT;

            byte[] keyBytes;
            keyBytes = SECRET_KEY.getBytes();
            Sm4 sm4 = new Sm4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4_crypt_ecb(ctx, plainText.getBytes(StandardCharsets.UTF_8));
            String cipherText = Base64.encodeBase64String(encrypted);
            if (cipherText != null && !cipherText.trim().isEmpty()) {
                Matcher m = P.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * ECB模式解密，自定义密钥，加解密密钥需一致
     *
     * @param cipherText 要解密的数据
     * @param secretKey 密钥，必须为 16 位，可包含字母、数字、标点
     * @return String
     */
    public static String decryptData_ECB(String cipherText, String secretKey) {
        try {
            Sm4Context ctx = new Sm4Context();
            ctx.isPadding = true;
            ctx.mode = Sm4.SM4_DECRYPT;

            byte[] keyBytes;
            keyBytes = secretKey.getBytes();
            Sm4 sm4 = new Sm4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_ecb(ctx, Base64.decodeBase64(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * ECB模式解密，默认密钥
     *
     * @param cipherText 要解密的数据
     * @return String
     */
    public static String decryptData_ECB(String cipherText) {
        try {
            Sm4Context ctx = new Sm4Context();
            ctx.isPadding = true;
            ctx.mode = Sm4.SM4_DECRYPT;

            byte[] keyBytes;
            keyBytes = SECRET_KEY.getBytes();
            Sm4 sm4 = new Sm4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_ecb(ctx, Base64.decodeBase64(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * CBC模式加密，SECRET_KEY和IV都需要传值，解密要和加密的SECRET_KEY和IV一致，更加安全
     *
     * @param plainText 要加密的数据
     * @param secretKey 密钥一，必须为 16 位，可包含字母、数字、标点
     * @param iv 密钥二，必须为 16 位，可包含字母、数字、标点
     * @return String
     */
    public static String encryptData_CBC(String plainText, String secretKey, String iv) {
        try {
            Sm4Context ctx = new Sm4Context();
            ctx.isPadding = true;
            ctx.mode = Sm4.SM4_ENCRYPT;

            byte[] keyBytes;
            byte[] ivBytes;

            keyBytes = secretKey.getBytes();
            ivBytes = iv.getBytes();

            Sm4 sm4 = new Sm4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, plainText.getBytes(StandardCharsets.UTF_8));
            String cipherText = Base64.encodeBase64String(encrypted);
            if (cipherText != null && !cipherText.trim().isEmpty()) {
                Matcher m = P.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * CBC模式加密，SECRET_KEY和IV都需要传值，解密要和加密的SECRET_KEY和IV一致，更加安全
     *
     * @param plainText
     * @return String
     */
    public static String encryptData_CBC(String plainText) {
        try {
            Sm4Context ctx = new Sm4Context();
            ctx.isPadding = true;
            ctx.mode = Sm4.SM4_ENCRYPT;

            byte[] keyBytes;
            byte[] ivBytes;

            keyBytes = SECRET_KEY.getBytes();
            ivBytes = IV.getBytes();

            Sm4 sm4 = new Sm4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, plainText.getBytes(StandardCharsets.UTF_8));
            String cipherText = Base64.encodeBase64String(encrypted);
            if (cipherText != null && !cipherText.trim().isEmpty()) {
                Matcher m = P.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * CBC模式解密，SECRET_KEY和IV都需要传值，解密要和加密的SECRET_KEY和IV一致，更加安全
     *
     * @param cipherText 要解密的数据
     * @param secretKey 密钥一，必须为 16 位，可包含字母、数字、标点
     * @param iv 密钥二，必须为 16 位，可包含字母、数字、标点
     * @return
     */
    public static String decryptData_CBC(String cipherText, String secretKey, String iv) {
        try {
            Sm4Context ctx = new Sm4Context();
            ctx.isPadding = true;
            ctx.mode = Sm4.SM4_DECRYPT;

            byte[] keyBytes;
            byte[] ivBytes;
            if (HEX_STRING) {
                keyBytes = Util.hexStringToBytes(secretKey);
                ivBytes = Util.hexStringToBytes(iv);
            } else {
                keyBytes = secretKey.getBytes();
                ivBytes = iv.getBytes();
            }

            Sm4 sm4 = new Sm4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, Base64.decodeBase64(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * CBC模式解密，SECRET_KEY和IV都需要传值，解密要和加密的SECRET_KEY和IV一致，更加安全
     *
     * @param cipherText 要解密的数据
     * @return
     */
    public static String decryptData_CBC(String cipherText) {
        try {
            Sm4Context ctx = new Sm4Context();
            ctx.isPadding = true;
            ctx.mode = Sm4.SM4_DECRYPT;

            byte[] keyBytes;
            byte[] ivBytes;
            if (HEX_STRING) {
                keyBytes = Util.hexStringToBytes(SECRET_KEY);
                ivBytes = Util.hexStringToBytes(IV);
            } else {
                keyBytes = SECRET_KEY.getBytes();
                ivBytes = IV.getBytes();
            }

            Sm4 sm4 = new Sm4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, Base64.decodeBase64(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println("经过ECB加密的密文为：" + Sm4Utils.encryptData_ECB("亦凡真帅@123456"));
        System.out.println("经过ECB解密的密文为：" + Sm4Utils.decryptData_ECB("Ig4X8tonZYIQhKrkolzzNA=="));
        System.out.println("经过CBC加密的密文为：" + Sm4Utils.encryptData_CBC("123456", "asdfghjklzxcvbnm", "1234567890123456"));
        System.out.println("经过CBC解密的密文为：" + Sm4Utils.decryptData_CBC("RZUhE8Zeobfkn/sqnPXA+g==", "asdfghjklzxcvbnm", "1234567890123456"));
    }
}
