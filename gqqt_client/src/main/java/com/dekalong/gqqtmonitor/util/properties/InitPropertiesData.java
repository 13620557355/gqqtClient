/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.util.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;  

import org.springframework.beans.BeansException;  
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;  
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.dekalong.gqqtmonitor.util.encrypt.SymmetricEncoder;   
  
public class InitPropertiesData extends PropertyPlaceholderConfigurer {
	private static Map<String, String> ctxPropertiesMap;
      protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)   
              throws BeansException { 
    		ctxPropertiesMap = new HashMap<String, String>();
            for (Object key : props.keySet()) {
                String keyStr = key.toString();
                String value = props.getProperty(keyStr);
                ctxPropertiesMap.put(keyStr.trim(), value.trim());
            }
            
            encryptSpringConfig(beanFactory, props);
      }  
      public static String getContextProperty(String name) {
          return ctxPropertiesMap.get(name);
      }
   
      /**
       * 
       * <B>方法名称：加密Spring配置文件信息</B><BR>
       * <B>概要说明：</B><BR>
       * @param beanFactory
       * @param props
       */
	    private void encryptSpringConfig(ConfigurableListableBeanFactory beanFactory,Properties props) {
	  	  
	          String password = props.getProperty("jdbc.password");   
	          String username = props.getProperty("jdbc.username");
	          String url= props.getProperty("jdbc.url");
	          String driver= props.getProperty("jdbc.driver");
	          try {  
	          if (password != null) {   
	              //解密jdbc.password属性值，并重新设置   
	               //SymmetricEncoder.AESDncode;aes加密算法
	               props.setProperty("jdbc.password", SymmetricEncoder.AESDncode(password));
	          }   
	          if (username != null) {   
	            props.setProperty("jdbc.username", SymmetricEncoder.AESDncode(username));
	          }
	          if (url!= null) {   
	              props.setProperty("jdbc.url", SymmetricEncoder.AESDncode(url));
	          }
	          if (driver!= null) {   
	              props.setProperty("jdbc.driver", SymmetricEncoder.AESDncode(driver)); 
	          }
	          super.processProperties(beanFactory, props);  
	          } catch (Exception e) {  
	                e.printStackTrace();  
	          }  
	    }
}