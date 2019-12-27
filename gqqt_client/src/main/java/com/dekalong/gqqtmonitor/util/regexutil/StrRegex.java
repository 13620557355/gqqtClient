/**
 * 
 */
package com.dekalong.gqqtmonitor.util.regexutil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dekalong.gqqtmonitor.po.BaseModel;
import com.dekalong.gqqtmonitor.util.josnutil.JsonHelper;

/**
 * @author Long
 *用于String字符串的正则操作
 * 
 */
public class StrRegex {
	
/**
 *  传递一个String进来，提取其中的数字，返回一个List集合
 */
   public static List<Long> getDigit(String extract_num) {
	   List<Long> digitList = new ArrayList<Long>();
	    Pattern p = Pattern.compile("(\\d+)");
	    Matcher m = p.matcher(extract_num);
	    while (m.find()) {
	        String find = m.group(1).toString();
	        digitList.add(Long.valueOf(find));
	    }
	    return digitList;   
   }
   /**
    * 截取uuid,用于webSocket的session的key
    */
//   public static Integer stringIntercept(String parm) {
//	   if(parm==null) {return 0;}
//	   String s="0";
//			try {
//				int end=parm.indexOf("|");//提取|符号后面的参数
//				      s =parm.substring(end+1, parm.length());
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//		 return Integer.valueOf(s);
//   } 
   public static String getWebsocketParm(String parm,String queryParm) {
	   queryParm=queryParm.trim()+"=";
	   if(parm==null) {return "";}
	   String s="";
			try {
				int start=parm.indexOf(queryParm);//提取符号后面的参数
				if(start!=-1) {
					int end= parm.indexOf("|",start);
					 s =parm.substring(start, end);
					 int end2=s.indexOf("=");//提取|符号后面的参数
					 if(end2==-1||(end2+1)==s.length()) {
						 return "";
					 }
					 s =s.substring(end2+1, s.length());
					 return s;
				}
			} catch (Exception e) {
				return "";
			}
		 return "";
   } 
   public static int getSearcherParm(String parm,String queryParm) {
	   queryParm=queryParm+"=";
	   if(parm==null) {return 0;}
	   String s="";
			try {
				int start=parm.indexOf(queryParm);//提取符号后面的参数
				if(start!=-1) {
					int end= parm.indexOf("|",start);
					 s =parm.substring(start, end);
					 int end2=s.indexOf("=");//提取|符号后面的参数
					 if(end2==-1||(end2+1)==s.length()) {
						 return queryParm.equals("power=")?5:0;
					 }
					 s =s.substring(end2+1, s.length());
					 return Integer.valueOf(s);
				}
			} catch (Exception e) {
				return queryParm.equals("power=")?5:0;
			}
		 return queryParm.equals("power=")?5:0;
   } 
   @SuppressWarnings({ "rawtypes", "unchecked", "unused" }) 
   public static String listToJson(List<BaseModel> list) {
   		Map map=new HashMap();
       	int total=list.size();
       	map.put("total", total);
   		map.put("rows",list);
   		String json =JsonHelper.map2String(map);
       	return json;
   }
   @SuppressWarnings({ "rawtypes", "unchecked"}) 
   public static <T> String listToJsonAddExcessKey(Collection<T> collection ,String addKey,int total) {
   		Map map=new HashMap();
   		if(total<0) {
   			total=collection.size();
   		}
       	map.put("total", total);
   		map.put("rows",collection);
   		map.put(addKey, addKey);
   		String json =JsonHelper.map2String(map);
       	return json;
   }
   /**
    * 根据传递的值和位置取得继电器状态，10000 表示第0位继电器闭合，取余松开，01100表示第1，2位继电器闭合，其他松开
    */
   public static int getRelayPosition(String parm,int position) {
		return Integer.valueOf(parm.subSequence(position, position+1).toString());
   } 
   
   
   public static void main(String[] args) {
//	  String parm="pagehomeSearcher=安|page=|mm=2|";
////	  System.out.println(parm.length());
////	  System.out.println(parm.indexOf("mm=z"));
//	  System.out.println(getSearcherParm(parm, "pagehomeSearcher"));
//	  String s="";
//	  System.out.println(s.trim().equals(""));
//	   String string="systemName=广气气体智能监控系统|aniLogoID=gqqt|indexLogoPath=/images/gqqt/indexLogo.png|icoPath=/images/gqqt/gqqt.ico|";
//	   System.out.println(getWebsocketParm(string,"icoPath"));
//	   System.out.println(getRelayPosition("1000000000000000", 0));
//	   System.out.println(getRelayPosition("1000000000000000", 1));
   }
}
