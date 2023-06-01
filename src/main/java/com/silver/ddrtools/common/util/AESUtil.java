package com.silver.ddrtools.common.util;

/**
 * @ClassName AESUtill
 * @Description TODO
 * @Author silver
 * @Date 2022/7/17 13:40
 * @Version 1.0
 **/
import com.alibaba.fastjson.JSON;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;


public class AESUtil {
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";



    public static Key getKey(){
        String password = "silvernb666";     //加解密的密码
        byte[] secretKey = new byte[0];
        try {
            secretKey = getSecretKey(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Key key = toKey(secretKey);
        return key;
    }

    /**
     * 指定随机字符串（密码）生成密钥
     *
     * @param randomKey 加解密的密码
     * @throws Exception
     */
    public static byte[] getSecretKey(String randomKey) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM); //秘钥生成器，指定秘钥算法

        //初始化此密钥生成器，指定AES的秘钥长度为128
        if (StringUtils.isBlank(randomKey)) {   //不指定密码
            keyGenerator.init(128);
        } else {    //指定密码
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(randomKey.getBytes());
            keyGenerator.init(128, random);
        }

        SecretKey secretKey = keyGenerator.generateKey();   //生成密钥
        return secretKey.getEncoded();
    }

    /**
     * 加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[]   加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, Key key) throws Exception {
        return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
    }

    /**
     * 加密
     *
     * @param data 待加密数据
     * @param key  二进制密钥
     * @return byte[]   加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
    }

    /**
     * 加密
     *
     * @param data            待加密数据
     * @param key             二进制密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]   加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] key, String cipherAlgorithm) throws Exception {
        Key k = toKey(key);
        return encrypt(data, k, cipherAlgorithm);
    }

    /**
     * 加密
     *
     * @param data            待加密数据
     * @param key             密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]   加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);    //获取算法
        cipher.init(Cipher.ENCRYPT_MODE, key);                  //设置加密模式，并指定秘钥
        return cipher.doFinal(data);                            //加密数据
    }

    /**
     * 解密
     *
     * @param data 待解密数据
     * @param key  二进制密钥
     * @return byte[]   解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
    }

    /**
     * 解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[]   解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, Key key) throws Exception {
        return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
    }

    /**
     * 解密
     *
     * @param data            待解密数据
     * @param key             二进制密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]   解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key, String cipherAlgorithm) throws Exception {
        Key k = toKey(key);
        return decrypt(data, k, cipherAlgorithm);
    }

    /**
     * 解密
     *
     * @param data            待解密数据
     * @param key             密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]   解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);     //获取算法
        cipher.init(Cipher.DECRYPT_MODE, key);                   //设置解密模式，并指定秘钥
        return cipher.doFinal(data);                             //解密数据
    }

    /**
     * 转换密钥
     *
     * @param secretKey 二进制密钥
     * @return 密钥
     */
    public static Key toKey(byte[] secretKey) {
        return new SecretKeySpec(secretKey, KEY_ALGORITHM);   //生成密钥
    }


    public static void main(String[] args) throws Exception {


//        String data = "AES 对称加密算法";
//        System.out.println("明文 ：" + data);
//        UmUserActivate userActivate = new UmUserActivate();
//        userActivate.setUserid(123l);
//        userActivate.setUsername("张三");
//        userActivate.setCode("张三");
//        String s = JSON.toJSONString(userActivate);
//        System.out.println(s);
//
//
//        byte[] encryptData = encrypt(s.getBytes(), getKey());
//        String encryptDataHex = Hex.encodeHexString(encryptData);   //把密文转为16进制
//        System.out.println("加密 : " + encryptDataHex);
//
//
//
//        byte[] decryptData = decrypt(Hex.decodeHex(encryptDataHex.toCharArray()), getKey());
//        System.out.println("解密 : " + new String(decryptData));
    }
}
