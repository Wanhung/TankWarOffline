package com.tankwar;
import java.io.IOException;
import java.util.Properties;
/**
 * ������
 * @author WanHung
 *
 */

public class PropertyMgr {
	/*
	*����ÿ����һ�ξͼ���һ�Σ�Ч�ʽ��ͣ�
	*����static һ�μ��ص��ڴ�����Ч��
	*/
	static Properties pros=new Properties();
	static{
		try {
	//��ȡ�����ļ���������ݣ��漰����֪ʶ��
		pros.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config/tank.properties"));
	} catch (IOException e1) {
		e1.printStackTrace();
	}
	}
 public static String getProperty(String key){
		return pros.getProperty(key);
 }
}
