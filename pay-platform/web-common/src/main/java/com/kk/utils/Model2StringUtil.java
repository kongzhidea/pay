package com.kk.utils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * 打印model对象的各个字段信息使用的工具类，功能类似于JSONSerializer.toJSON(model)
 * 
 * 
 */

public class Model2StringUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static String transModel2String(Object o) {
		if (o == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(o.getClass().getName() + "[");
		Field[] farr = o.getClass().getDeclaredFields();
		for (int i = 0; i < farr.length; i++) {
			Field field = farr[i];
			try {
				if (i > 0) {
					sb.append(",");
				}
				field.setAccessible(true);
				sb.append(field.getName());
				sb.append("=");
				if (field.get(o) != null && field.get(o) instanceof Date) {
					// 日期的处理
					sb.append(sdf.format(field.get(o)));
				} else {
					sb.append(field.get(o));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sb.append("]");
		return sb.toString();
	}
}