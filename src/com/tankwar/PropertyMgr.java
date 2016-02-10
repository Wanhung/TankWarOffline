package com.tankwar;
import java.io.IOException;
import java.util.Properties;
/**
 * 配置类
 * @author WanHung
 *
 */

public class PropertyMgr {
	/*
	*避免每调用一次就加载一次，效率降低！
	*利用static 一次加载到内存里，提高效率
	*/
	static Properties pros=new Properties();
	static{
		try {
	//获取配置文件里面的数据，涉及反射知识！
		pros.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config/tank.properties"));
	} catch (IOException e1) {
		e1.printStackTrace();
	}
	}
 public static String getProperty(String key){
		return pros.getProperty(key);
 }
}
