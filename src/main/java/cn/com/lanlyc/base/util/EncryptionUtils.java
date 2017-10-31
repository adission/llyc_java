package cn.com.lanlyc.base.util;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * 字符串加密类
 * @author 
 */
public class EncryptionUtils {
    
    /**
     * 密码默认加密算法【用于V5加密算法】
     * @param code
     * @return 返回加密后的密文
     */
    public static String encryptV5(String code) {
        return encryptV5(code, 0);
    }
    
    /**
     * 密码加密算法【用于V5加密算法】
     * @param code 需要加密的字符串
     * @param open 加密算法加长开关
     * @return 返回加密后的密文
     */
    public static String encryptV5(String code, int open) {
        String is_list;
        String[] is_thm = new String[20];
        String is_result = "";
        String is_char;
        String is_sort = "";
        int i;
        int j;
        // int k;
        int[] is_count = new int[70];
        is_list = "abcdefghijklmnopqrstuvw-xyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        is_thm[1] = "qM3NwB-eV1CrXtZ2LyKuJ4HiGoFpDaS6AsPdOfIgUhY8TjRkE7WlQzx9cv5bn0m";
        is_thm[2] = "mQnA8ZbW0SvXcExD9CzRlFkV6TjGhB7YgHfNd-UsJ5MaIpKo3OiLuP1ytr4ew2q";
        is_thm[3] = "1qQaAzW2SwEsDxR3FeTdGcY4HrUfJvIK5tg-OLbP6ZyXhCn7uVjBmNM8ik9ol0p";
        is_thm[4] = "1QqAwZeXrStW-y2u3iEoDpCaVsFdRf4g5hTjGkBlmNnHbYv6c7xUzJMKI89OLP0";
        is_thm[5] = "0Pz9xOcLv8bInKmlMkJjUh7g6fYdHsNaBpGoTi-5u4yRtFrVCDEe3w2qWSXZAQ1";
        is_thm[6] = "AqCwXe0dsNaTz9xcHrRtyfgVhvK3bnMuG-io1jkPlmpQW8ESD5ZY7F2BU6IOJ4L";
        is_thm[7] = "PLq2wOKI6erJUHty3YGT-uiFR5DES1opWAlkQMNjhB8VCXgfZds0a7zxc4vb9nm";
        is_thm[8] = "pDlmASFkVo09iPjnKLbhZJNu87yXCgvBOHcft-YUI65rdxRTzsWEe43waq21qGM";
        is_thm[9] = "sDxR1qQadG-cbP6Z8ik9ol0pyXhCnTfJvIK5tgOLjBmNM7uVY4HrUAzW2SwE3Fe";
        for (i = 0; i < is_list.length(); i++) {
            is_count[i] = 0;
        }
        for (i = 0; i < code.length(); i++) {
            is_char = code.substring(i, i + 1);
            j = is_list.indexOf(is_char);
            if (j == -1) {
                is_result = null;
                return is_result;
            }
            is_count[j] = is_count[j] + 1;
            is_char = is_thm[is_count[j]].substring(j, j + 1);
            is_result = is_result + is_char;
            is_sort = is_sort + is_count[j];
            if (is_count[j] == 9) {
                is_count[j] = 0;
            }
        }
        if (open != 0) {
            is_result = is_result + "|" + is_sort;
        }
        return is_result;
    }
    
    /**
     * 32位MD5加密算法（使用UTF-8编码）
     * @param plainText
     * @return 返回32位MD5密文
     */
    public static String MD5(String plainText) {
        return MD5(plainText, null);
    }
    
    /**
     * 32位MD5加密算法
     * @param plainText
     * @param encode 编码格式，为null或者""则使用UTF-8
     * @return 返回32位MD5密文
     * @author 
     */
    public static String MD5(String plainText, String encode) {
        StringBuffer buf = new StringBuffer();
        plainText = DataUtils.defaultString(plainText);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (DataUtils.isNullOrEmpty(encode)) encode = "UTF-8";
            md.update(plainText.getBytes(encode));
            byte b[] = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16) buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }
    
    /**
     * SHA1加密算法
     * @param plainText
     * @return 返回SHA1密文
     * @author 
     */
    public static String SHA1(String plainText) {
        StringBuffer buf = new StringBuffer();
        plainText = DataUtils.defaultString(plainText);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(plainText.getBytes("UTF-8"));
            byte b[] = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16) buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }
    
    /**
     * 16位MD5加密算法（使用UTF-8编码）
     * @param plainText
     * @return 返回16位MD5密文
     */
    public static String shortMD5(String plainText) {
        return shortMD5(plainText, null);
    }
    
    /**
     * 16位MD5加密算法
     * @param plainText
     * @param encode 编码格式
     * @return 返回16位MD5密文
     * @author 
     */
    public static String shortMD5(String plainText, String encode) {
        return MD5(plainText, encode).substring(8, 24);
    }
    
    /**
     * AES + Base64加密
     * @param content 需要加密的内容
     * @param password 加密密钥
     * @param encode 编码类型 null或""则默认UTF-8
     * @return 返回加密后的密文
     */
    public static String encryptAESBase64(String content, String password, String encode) {
        try {
            if (DataUtils.isNullOrEmpty(encode)) encode = "UTF-8";
            // KeyGenerator kgen =
            // KeyGenerator.getInstance("AES/CBC/NoPadding");
            // kgen.init(128, new SecureRandom(password.getBytes()));
            // SecretKey secretKey = kgen.generateKey();
            // byte[] enCodeFormat = secretKey.getEncoded();
            // 为了和.NET加密一致修改为
            byte[] enCodeFormat = shortMD5(password, encode).getBytes(encode);
            // ///////////////////////以上已经修改/////////////////////////
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes(encode);
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return Base64.encodeBase64String(result); // 加密
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    /**
     * AES + Base64加密（使用UTF-8编码）
     * @param content 需要加密的内容
     * @param password 加密密钥
     * @return 返回加密后的密文
     */
    public static String encryptAESBase64(String content, String password) {
        return encryptAESBase64(content, password, null);
    }
    
    /**
     * AES + Base64解密
     * @param content 待解密内容
     * @param password 解密密钥
     * @param encode 编码类型 null或""则默认UTF-8
     * @return 返回被加密的原文
     */
    public static String decryptAESBase64(String content, String password, String encode) {
        try {
            if (DataUtils.isNullOrEmpty(encode)) encode = "UTF-8";
            // KeyGenerator kgen = KeyGenerator.getInstance("AES");
            // kgen.init(128, new SecureRandom(password.getBytes()));
            // SecretKey secretKey = kgen.generateKey();
            // byte[] enCodeFormat = secretKey.getEncoded();
            // 为了和.NET加密一致修改为
            byte[] enCodeFormat = shortMD5(password, encode).getBytes(encode);
            // ///////////////////////以上已经修改/////////////////////////
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(Base64.decodeBase64(content));// 解密
            return new String(result, encode);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    /**
     * AES + Base64解密（使用UTF-8编码）
     * @param content 待解密内容
     * @param password 解密密钥
     * @return 返回被加密的原文
     */
    public static String decryptAESBase64(String content, String password) {
        return decryptAESBase64(content, password, null);
    }
}
