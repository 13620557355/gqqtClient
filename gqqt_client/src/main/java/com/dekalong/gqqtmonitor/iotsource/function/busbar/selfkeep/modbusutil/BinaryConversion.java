/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.iotsource.function.busbar.selfkeep.modbusutil;

/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年7月21日
 * 
 */
public class BinaryConversion {
	/**
	   * 把16进制字符串转换成字节数组
	   * @param hexString
	   * @return byte[]
	   */
	  public static byte[] hexStringToByte(String hex) {
	   hex=hex.replaceAll(" ", "");
	   int len = (hex.length() / 2);
	   byte[] result = new byte[len];
	   char[] achar = hex.toCharArray();
	   for (int i = 0; i < len; i++) {
	    int pos = i * 2;
	    result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
	   }
	   return result;
	  }
	  
	  private static int toByte(char c) {
		    byte b = (byte) "0123456789ABCDEF".indexOf(c);
		    return b;
	  }
	  
	  /**
	   * 数组转换成十六进制字符串
	   * @param byte[]
	   * @return HexString
	   */
	  public static  String bytesToHexString(byte[] bArray) {
	   StringBuffer sb = new StringBuffer(bArray.length);
	   String sTemp;
	   for (int i = 0; i < bArray.length; i++) {
	    sTemp = Integer.toHexString(0xFF & bArray[i]);
	    if (sTemp.length() < 2)
	     sb.append(0);
	    sb.append(sTemp.toUpperCase());
	   }
	   return sb.toString();
	  }
	  /**
	   * 
	   * <B>方法名称：十进制转十六进制</B><BR>
	   * <B>概要说明：</B><BR>
	   * @param num
	   * @return
	   */
	 
	  public static String toHex(int num) {
		  String s=Integer.toHexString(num);
		  if(num<16) {
			  s="0"+s;
		  }
		   return s.toUpperCase();

       }
	  /**
	   * 
	   * <B>方法名称：16进制转换成2进制字符串</B><BR>
	   * <B>概要说明：</B><BR>
	   * @param num
	   * @return
	   */
	  public static String hexToBinary(String hex) {
		  int i = Integer.parseInt(hex, 16);
		  String str2 = Integer.toBinaryString(i);
		  if(str2.length()<8) {
			  int j=8-str2.length();
			  for(int k=0;k<j;k++) {
				  str2=0+str2; 
			  }
		  }
		  return str2;
       }
	  
	  public static String getTurnBinary(String hex) {
		  String s =hexToBinary(hex);
		  String turn="";
		  int longth=s.length();
		  for (int i = 0; i < s.length(); i++) {
			  turn+=s.substring(longth-1, longth);
			  longth--;
		  }
		  return turn;
	  }
	  
	  public static void main(String[] args) {
		System.out.println(getTurnBinary("9"));
	  }
	
}
