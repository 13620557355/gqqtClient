package com.dekalong.gqqtmonitor.util.encrypt;

import java.io.PrintStream;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;

public class AESUtil
{
  public static final String KEY = "dekalong_secret.";
  private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

  public static String base64Encode(byte[] bytes)
  {
    return Base64.encodeBase64String(bytes);
  }

  public static byte[] base64Decode(String base64Code)
    throws Exception
  {
    return StringUtils.isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);
  }

  public static byte[] aesEncryptToBytes(String content, String encryptKey)
    throws Exception
  {
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(1, new SecretKeySpec(encryptKey.getBytes(), "AES"));

    return cipher.doFinal(content.getBytes("utf-8"));
  }

  public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey)
    throws Exception
  {
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(2, new SecretKeySpec(decryptKey.getBytes(), "AES"));
    byte[] decryptBytes = cipher.doFinal(encryptBytes);
    return new String(decryptBytes);
  }

  public static String aesEncrypt(String content, String encryptKey)
    throws Exception
  {
    return base64Encode(aesEncryptToBytes(content, encryptKey));
  }
  public static String aesDecryptNotExcption(String content) {
    try {
      return base64Encode(aesEncryptToBytes(content, "dekalong_secret."));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String aesDecrypt(String encryptStr, String decryptKey)
    throws Exception
  {
    return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
  }

  public static void main(String[] args)
    throws Exception
  {
    System.out.println(aesDecryptNotExcption("123456"));
    System.out.println(aesDecrypt(aesDecryptNotExcption("123456"), "dekalong_secret."));
  }
}