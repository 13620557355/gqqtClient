package com.dekalong.gqqtmonitor.util.josnutil;
import java.util.Map;

/**
 * json工具类
 */
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@SuppressWarnings({"rawtypes","unchecked"})
public class JsonHelper {
	
	private JsonHelper(){}
	
	public static String object2str(Object obj){
		String retStr = "";
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			retStr = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return retStr;
	}
	
	public static Object str2Object(String str,Class cls){
		Object retObj = null;
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			 retObj = mapper.readValue(str, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retObj;
	}
	public static String map2String(Map map) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json=mapper.writeValueAsString(map);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Object mapToObject(Map map) {
		return str2Object(map2String(map),Object.class);
	}
	public static void main(String[] args) {
		Object object=str2Object("\"s3\":110,\"s4\":42,\"s5\":0,\"s1\":7,\"s2\":8", Object.class);
		System.out.println(object);
//		List<DataStatistics> list=new ArrayList<DataStatistics>();
//		DataStatistics ds=new DataStatistics();
//		ds.setUuid(10020);
//		ds.setGasMedium("氮气");
//		ds.setReplaceDirection("left");
//		list.add(ds);
//		System.out.println(object2str(list));
	}
	
}
