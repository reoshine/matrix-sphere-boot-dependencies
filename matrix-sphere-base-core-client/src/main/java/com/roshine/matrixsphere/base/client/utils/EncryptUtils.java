package com.roshine.matrixsphere.base.client.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.antherd.smcrypto.sm2.Keypair;
import com.antherd.smcrypto.sm2.Sm2;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.legacy.math.linearalgebra.ByteUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * @author roshine
 * @version 1.0.0
 * @since 2024-12-22 22:58
 */
@SuppressWarnings("DanglingJavadoc")
public class EncryptUtils {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 脱敏最短长度2
     */
    private final static int NUM = 2;
    /**
     * 字符集
     */
    public static final String ENCODING = "UTF-8";
    /**
     * 加密方式
     */
    public static final String RSA_ALGORITHM = "RSA";

    /**
     * 算法名称
     */
    private static final String ALGORITHM_NAME = "SM4";

    /**
     * PKCS5Padding  NoPadding 补位规则，PKCS5Padding缺位补0，NoPadding不补
     */
    private static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";


    /**
     * 普通字符串脱敏
     *
     * @param source 源字符串
     * @param replacement 替换符
     * @param prefix 前缀保留
     * @param suffix 后缀保留
     * @return String 脱敏后字符串
     */
    public static String getMask(String source, String replacement, int prefix, int suffix) {
        if (null != source && !source.isEmpty()) {
            int len = source.length();
            if (len <= prefix + suffix) {
                return source;
            }
            String target = DesensitizedUtil.idCardNum(source, prefix, suffix);
            target = target.replace("*", replacement);
            return target;
        } else {
            return "";
        }

    }

    /**
     * 字符串全脱敏
     *
     * @param source 源字符串
     * @param replacement 替换符
     * @return String 脱敏后字符串
     */
    public static String getFullMask(String source, String replacement) {
        if (null != source && !source.isEmpty()) {
            return String.valueOf(replacement).repeat(6);
        } else {
            return "";
        }
    }

    /**
     * 姓名脱敏
     *
     * @param source 源字符串
     * @param replacement 替换符
     * @return String 脱敏后字符串
     */
    public static String getNameMask(String source, String replacement) {
        if (null != source && !source.isEmpty()) {
            int len = source.length();
            String target;
            if (len <= NUM) {
                target = DesensitizedUtil.chineseName(source);
            } else {
                target = DesensitizedUtil.idCardNum(source, 1, 1);
            }
            target = target.replace("*", replacement);
            return target;
        } else {
            return "";
        }
    }
/***********************MD5加密*****************************************************************************************/
    /**
     * md5摘要加密
     *
     * @param source 源字符串
     * @return String 加密后字符串
     */
    public static String encryptMd5(String source) {
        return DigestUtil.md5Hex(source);
    }
/***********************SM2加密自定义密钥加密*****************************************************************************************/

    /**
     * 加密，使用公钥
     *
     * @param publicKey 公钥
     * @param data 数据
     * @return
     */
    public static String encryptSm2Public(String publicKey, String data) throws Exception {
        if (StrUtil.isBlank(publicKey)) {
            throw new Exception("密钥不能为空...");
        }
        if (StrUtil.isBlank(data)) {
            throw new Exception("明文不能为空...");
        }

        try {
            //HexUtil.encodeHexStr(cipherText); // 加密结果
            return Sm2.doEncrypt(data, publicKey);
        } catch (Exception e) {
            throw new Exception("加密错误：密钥不正确...");
        }
    }

    /**
     * 解密，使用私钥
     *
     * @param privateKey 私钥
     * @param data 数据
     * @return
     */
    public static String decryptSm2Private(String privateKey, String data) throws Exception {
        if (StrUtil.isBlank(privateKey)) {
            throw new Exception("密钥不能为空...");
        }
        if (StrUtil.isBlank(data)) {
            throw new Exception("明文不能为空...");
        }
        try {
            // new String(sm2.decrypt(sourceData,prvKey)); // 解密结果
            return Sm2.doDecrypt(data, privateKey);
        } catch (Exception e) {
            throw new Exception("解密错误：密钥不正确...");
        }
    }

    /**
     * 获取sm2密钥对
     * @return map
     */
    public static Map<String,String> getSm2Key() throws Exception {
        Map<String,String> map = new HashMap<>();
        try {
            Keypair keypair = Sm2.generateKeyPairHex();
            String publicKey = keypair.getPublicKey();
            String privateKey = keypair.getPrivateKey();
            map.put("publicKey",publicKey);
            map.put("privateKey",privateKey);
            return map;
        } catch (Exception e) {
            throw new Exception("生成密钥对失败...");
        }
    }



/***********************SM3加密自定义密钥加密及校验*****************************************************************************************/

    /**
     * 加密
     *
     * @param source 加密钱字符串
     * @param key 密钥
     * @return  加密后字符串
     * @throws Exception 字符串转换16进制异常
     */
    public static String encryptSm3(String source, String key) throws Exception {
        return ByteUtils.toHexString(getEncryptByKey(source, key));
    }


    /**
     * SM3加密方式之： 根据自定义密钥进行加密，返回加密后长度为32位的16进制字符串
     *
     * @param src 源数据
     * @param key 密钥
     * @return 加密后字符串
     * @throws Exception 字符串转换16进制异常
     */
    public static byte[] getEncryptByKey(String src, String key) throws Exception {
        byte[] srcByte = src.getBytes(ENCODING);
        byte[] keyByte = key.getBytes(ENCODING);
        KeyParameter keyParameter = new KeyParameter(keyByte);
        SM3Digest sm3 = new SM3Digest();
        HMac hMac = new HMac(sm3);
        hMac.init(keyParameter);
        hMac.update(srcByte, 0, srcByte.length);
        byte[] result = new byte[hMac.getMacSize()];
        hMac.doFinal(result, 0);
        return result;
    }

    /**
     * 利用源数据+密钥校验与密文是否一致
     *
     * @param src       源数据
     * @param key       密钥
     * @param sm3HexStr 密文
     * @return boolean 校验是否成功
     * @throws Exception 字符串转换16进制异常
     */
    public static boolean verifySm3(String src, String key, String sm3HexStr) throws Exception {
        byte[] sm3HashCode = ByteUtils.fromHexString(sm3HexStr);
        byte[] newHashCode = getEncryptByKey(src, key);
        return Arrays.equals(newHashCode, sm3HashCode);
    }
    /***********************SM3无密钥的加密及校验*****************************************************************************************/


    /**
     * SM3加密方式之：不提供密钥的方式 SM3加密，返回加密后长度为64位的16进制字符串
     *
     * @param src 明文
     * @return 加密后字符串
     */
    public static String encryptSm3(String src) {
        return ByteUtils.toHexString(getEncryptBySrcByte(src.getBytes()));
    }

    /**
     * 返回长度为32位的加密后的byte数组
     *
     * @param srcByte 字符串字节数组
     * @return 加密后字节数组
     */
    public static byte[] getEncryptBySrcByte(byte[] srcByte) {
        SM3Digest sm3 = new SM3Digest();
        sm3.update(srcByte, 0, srcByte.length);
        byte[] encryptByte = new byte[sm3.getDigestSize()];
        sm3.doFinal(encryptByte, 0);
        return encryptByte;
    }

    /**
     * 校验源数据与加密数据是否一致
     *
     * @param src       源数据
     * @param sm3HexStr 16进制的加密数据
     * @return boolean 返回校验是否成功
     * @throws Exception 字符串转换16进制异常
     */
    public static boolean verifySm3(String src, String sm3HexStr) throws Exception {
        byte[] sm3HashCode = ByteUtils.fromHexString(sm3HexStr);
        byte[] newHashCode = getEncryptBySrcByte(src.getBytes(ENCODING));
        return Arrays.equals(newHashCode, sm3HashCode);
    }


/***********************AES对称加密*****************************************************************************************/
    /**
     * AES 随机生成密钥
     * return byte[]
     */
    public static byte[] getAesKey() {
        return SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
    }

    /**
     * AES 加密
     *
     * @param source 源字符串
     * @param key 密钥
     * @return String 加密后字符串
     */
    public static String encryptAes(String source, byte[] key) {
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        return aes.encryptHex(source);
    }

    /**
     * AES 解密
     *
     * @param source 源字符串
     * @param key 密钥
     * @return String 解密后字符串
     */
    public static String decryptAes(String source, byte[] key) {
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        return aes.decryptStr(source, CharsetUtil.CHARSET_UTF_8);
    }

    /***********************DES对称加密*****************************************************************************************/
    /**
     * DES 随机生成密钥
     * return byte[]
     */
    public static byte[] getDesKey() {
        return SecureUtil.generateKey(SymmetricAlgorithm.DESede.getValue()).getEncoded();
    }

    /**
     * DES 加密
     *
     * @param source 源字符串
     * @param key 密钥
     * @return String 加密后字符串
     */
    public static String encryptDes(String source, byte[] key) {
        SymmetricCrypto des = new SymmetricCrypto(SymmetricAlgorithm.DESede, key);
        return des.encryptHex(source);
    }

    /**
     * DES 解密
     *
     * @param source 源字符串
     * @param key 密钥
     * @return String 解密后字符串
     */
    public static String decryptDes(String source, byte[] key) {
        SymmetricCrypto des = new SymmetricCrypto(SymmetricAlgorithm.DESede, key);
        return des.decryptStr(source, CharsetUtil.CHARSET_UTF_8);
    }

    /***********************SM4加密ECB*******************************************************************************************/
    /**
     * sm4加密
     * 加密模式：ECB 密文长度不固定，会随着被加密字符串长度的变化而变化
     *
     * @param hexKey    16进制密钥（忽略大小写）
     * @param plainText 源字符串
     * @return 返回16进制的加密字符串
     */
    public static String encryptSm4Ecb(String hexKey, String plainText) {
        try {
            String cipherText = "";
            // 16进制字符串-->byte[]
            byte[] keyData = ByteUtils.fromHexString(hexKey);
            // String-->byte[]
            //当加密数据为16进制字符串时使用这行
            byte[] srcData = plainText.getBytes(ENCODING);
            // 加密后的数组
            byte[] cipherArray = encryptEcbPadding(keyData, srcData);
            // byte[]-->hexString
            cipherText = ByteUtils.toHexString(cipherArray);
            return cipherText;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * sm4解密
     * <p>
     * 解密模式：采用ECB
     *
     * @param hexKey     16进制密钥
     * @param cipherText 16进制的加密字符串（忽略大小写）
     * @return 解密后的字符串
     */
    public static String decryptSm4Ecb(String hexKey, String cipherText) {
        try {
            // 用于接收解密后的字符串
            String decryptStr = "";
            // hexString-->byte[]
            byte[] keyData = ByteUtils.fromHexString(hexKey);
            // hexString-->byte[]
            byte[] cipherData = ByteUtils.fromHexString(cipherText);
            // 解密
            byte[] srcData = decryptEcbPadding(keyData, cipherData);
            // byte[]-->String
            decryptStr = new String(srcData, ENCODING);
            return decryptStr;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * ECB加密模式，无向量
     *
     * @param mode 模式
     * @param key  key
     * @return 结果
     */
    private static Cipher generateEcbCipher(int mode, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(EncryptUtils.ALGORITHM_NAME_ECB_PADDING, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }

    /**
     * 加密模式之Ecb
     *
     * @param key  秘钥
     * @param data 待加密的数据
     * @return 字节数组
     */
    public static byte[] encryptEcbPadding(byte[] key, byte[] data) {
        try {
            //声称Ecb暗号,通过第二个参数判断加密还是解密
            Cipher cipher = generateEcbCipher(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 解密
     *
     * @param key        秘钥
     * @param cipherText 密文
     * @return 结果
     * @since 0.0.5
     */
    public static byte[] decryptEcbPadding(byte[] key, byte[] cipherText) {
        try {
            //生成Ecb暗号,通过第二个参数判断加密还是解密
            Cipher cipher = generateEcbCipher(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(cipherText);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 验证数据
     *
     * @param hexKey     16进制密钥
     * @param cipherText 16进制的加密字符串（忽略大小写）
     * @param plainText  加密前字符串
     * @return 结果
     */
    public static boolean verifySm4Ecb(String hexKey, String cipherText, String plainText) {
        try {
            // 用于接收校验结果
            boolean flag = false;
            // hexString-->byte[]
            byte[] keyData = ByteUtils.fromHexString(hexKey);
            // 将16进制字符串转换成数组
            byte[] cipherData = ByteUtils.fromHexString(cipherText);
            // 解密
            byte[] decryptData = decryptEcbPadding(keyData, cipherData);
            // 将原字符串转换成byte[]
            byte[] srcData = plainText.getBytes(ENCODING);
            // 判断2个数组是否一致
            flag = Arrays.equals(decryptData, srcData);
            return flag;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /***********************RSA非对称加密*****************************************************************************************/

    /**
     * 创建公钥私钥
     *
     * @param keySize 秘钥长度
     * @return map 公钥私钥map
     */
    public static Map<String, String> getRsaKey(int keySize) {
        //为RSA算法创建一个KeyPairGenerator对象（KeyPairGenerator，密钥对生成器，用于生成公钥和私钥对）
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }

        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();
        //返回一个publicKey经过二次加密后的字符串
        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        //返回一个privateKey经过二次加密后的字符串
        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());

        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);

        return keyPairMap;
    }

    /**
     * 得到公钥
     *
     * @param publicKey 密钥字符串（经过base64编码）
     * @throws NoSuchAlgorithmException,InvalidKeySpecException 异常
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        return (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
    }

    /**
     * 得到私钥
     *
     * @param privateKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
    }

    /**
     * 公钥加密
     *
     * @param data 源字符串
     * @param publicKey 公钥
     * @return 加密后字符串
     */
    public static String encryptRsaPublic(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(ENCODING), publicKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥解密
     *
     * @param data 源字符串
     * @param privateKey 私钥
     * @return 解密后字符串
     */

    public static String decryptRsaPrivate(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), ENCODING);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥加密
     *
     * @param data 源字符串
     * @param privateKey 私钥
     * @return 加密后字符串
     */

    public static String encryptRsaPrivate(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(ENCODING), privateKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 公钥解密
     *
     * @param data 源字符串
     * @param publicKey 公钥
     * @return 解密后字符串
     */

    public static String decryptRsaPublic(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), publicKey.getModulus().bitLength()), ENCODING);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = 0;
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDatas;
    }

    public static void main(String[] args) throws Exception{
        String key = "aaDJL2023fhLZO0z";
        String data  = "zxyy@123";
        System.out.println(encryptAes(data,key.getBytes()));



/*        System.out.println("开始****************************");
        String plainText = "96C63180C2806ED1F47B859DE501215B";
        System.out.println("加密前：" + plainText);
//自定义的32位16进制秘钥
        String key = "86C63180C2806ED1F47B859DE501215B";
        String cipher = encryptSm4Ecb(key, plainText);//sm4加密
        System.out.println("加密后：" + cipher);
//校验加密前后是否为同一数据
        System.out.println("校验：" + verifySm4Ecb(key, cipher, plainText));
        plainText = decryptSm4Ecb(key, cipher);//解密
        System.out.println("解密后：" + plainText);
        System.out.println("结束****************************");*/
/*        Map<String,String> map = getSm2Key();
        String publicKey = map.get("publicKey");
        String privateKey = map.get("privateKey");
        System.out.println(publicKey);
        System.out.println(privateKey);
        String data = encryptSm2Public(publicKey,"123");
        System.out.println(encryptSm2Public(publicKey,"123"));
        System.out.println(decryptSm2Private(privateKey,data));*/
    }

}

