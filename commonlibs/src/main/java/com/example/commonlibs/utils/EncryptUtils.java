package com.example.commonlibs.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Dell
 * @time 2018/10/18 14:34
 */
public class EncryptUtils {

    private static final char[] base64EncodeChars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', '+', '/'};

    private static byte[] base64DecodeChars = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54,
            55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1,
            -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44,
            45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1};

    private static final String Algorithm = "DESede/ECB/NoPadding";

    private static final String DES = "DES/ECB/NoPadding";


    /**
     * DES加密
     *
     * @param data     需要加密的数据
     * @param password 密钥
     * @return
     */
    public static byte[] desEncrypt(byte[] data, byte[] password) {

        try {
            //DES算法要求一个可信任的随机数据源
            SecureRandom random = new SecureRandom();

            DESKeySpec keySpec = new DESKeySpec(password);
            //创建一个密钥工厂，用来将DESedeKeySpec进行转换
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);

            //使用Cipher进行实际的加密过程
            Cipher des = Cipher.getInstance(DES);
            des.init(Cipher.ENCRYPT_MODE, secretKey, random);

            //进行加密操作
            return des.doFinal(data);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return null;
    }


    /**
     * DES解密，将数据还原到DES加密的状态
     *
     * @param data     需要解密的数据
     * @param password 密钥
     * @return
     */
    public static byte[] desDecrypt(byte[] data, byte[] password) {
        try {
            //安全随机数
            SecureRandom random = new SecureRandom();
            DESKeySpec keySpec = new DESKeySpec(password);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            Cipher des = Cipher.getInstance(DES);
            des.init(Cipher.DECRYPT_MODE, secretKey, random);
            return des.doFinal(data);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }


    /**
     * 3DES加密
     *
     * @param data     待加密数据
     * @param password 密钥
     * @return
     */
    public static byte[] tripleDesEncrypt(byte[] data, byte[] password) {
        try {
            SecretKey secretKey = new SecretKeySpec(password, Algorithm);
            Cipher cipher = Cipher.getInstance(Algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(data);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    /**
     * 3DES解密
     *
     * @param data     需要进行解密的原始数据
     * @param password 密钥
     * @return
     */
    public static byte[] tripleDesDecrypt(byte[] data, byte[] password) {
        try {
            SecretKey secretKey = new SecretKeySpec(password, Algorithm);
            //创建密码器
            Cipher cipher = Cipher.getInstance(Algorithm);
            //用密钥初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            //获取解密后的数据
            return cipher.doFinal(data);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }


    /**
     * 若不是8的倍数，补0
     *
     * @param arg_text
     * @return
     */
    public static byte[] padding(String arg_text) {
        byte[] encrypt = arg_text.getBytes();

        if (encrypt.length % 8 != 0) { //not a multiple of 8
            //create a new array with a size which is a multiple of 8
            byte[] padded = new byte[encrypt.length + 8 - (encrypt.length % 8)];

            //copy the old array into it
            System.arraycopy(encrypt, 0, padded, 0, encrypt.length);
            encrypt = padded;
        }
        return encrypt;
    }

    /**
     * @throws
     * @Title: getMac
     * @Description: 根据报文拼接字符串计算MAC
     * @param: @param inputStr
     * @param: @return
     * @return: String
     */
    public static String getMac(String inputStr) {

        //DES加密取前16位
        String keyStr = "C9EEDBDAB5C2BFA8";
        byte[] key = HexUtils.hexStringToBytes(keyStr);
        byte[] Input = HexUtils.hexStringToBytes(inputStr);
        byte[] macArr = calcMac(key, Input);
        return new String(macArr);
    }


    /**
     * mac计算
     *
     * @param key   mac秘钥
     * @param Input 待加密数据
     * @return
     */
    public static byte[] calcMac(byte[] key, byte[] Input) {
        int length = Input.length;
        int x = length % 8;
        // 需要补位的长度
        int addLen = 0;
        if (x != 0) {
            addLen = 8 - length % 8;
        }
        int pos = 0;
        // 原始数据补位后的数据
        byte[] data = new byte[length + addLen];
        System.arraycopy(Input, 0, data, 0, length);
        byte[] oper1 = new byte[8];
        System.arraycopy(data, pos, oper1, 0, 8);
        pos += 8;
        //按每8个字节做异或（不管信息中的字符格式），如果最后不满8个字节，则添加“0X00”。
        for (int i = 1; i < data.length / 8; i++) {
            byte[] oper2 = new byte[8];
            System.arraycopy(data, pos, oper2, 0, 8);
            byte[] t = bytesXOR(oper1, oper2);
            oper1 = t;
            pos += 8;
        }
        // 将异或运算后的最后8个字节（RESULT BLOCK）转换成16个HEXDECIMAL：
        byte[] resultBlock = HexUtils.toHexString(oper1).getBytes();
        // 取前8个字节MAK加密
        byte[] front8 = new byte[8];
        System.arraycopy(resultBlock, 0, front8, 0, 8);
        byte[] behind8 = new byte[8];
        System.arraycopy(resultBlock, 8, behind8, 0, 8);
//        byte[] desfront8 = desDecrypt(front8, key);
        byte[] desfront8 = desEncrypt(front8, key);
        // 将加密后的结果与后8 个字节异或：
        byte[] resultXOR = bytesXOR(desfront8, behind8);
        // 用异或的结果TEMP BLOCK 再进行一次单倍长密钥算法运算
        byte[] buff = desEncrypt(resultXOR, key);
        // 将运算后的结果（ENC BLOCK2）转换成16 个HEXDECIMAL asc
        byte[] retBuf = new byte[8];
        // 取8个长度字节就是mac值
        System.arraycopy(HexUtils.toHexString(buff).getBytes(), 0, retBuf, 0, 8);
        return retBuf;
    }


    /**
     * 单字节异或
     *
     * @param src1
     * @param src2
     * @return
     */
    private static byte byteXOR(byte src1, byte src2) {
        return (byte) ((src1 & 0xFF) ^ (src2 & 0xFF));
    }

    /**
     * 字节数组异或
     *
     * @param src1
     * @param src2
     * @return
     */
    private static byte[] bytesXOR(byte[] src1, byte[] src2) {
        int length = src1.length;
        if (length != src2.length) {
            return null;
        }
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = byteXOR(src1[i], src2[i]);
        }
        return result;
    }


}
