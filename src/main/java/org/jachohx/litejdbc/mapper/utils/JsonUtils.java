package org.jachohx.litejdbc.mapper.utils;

import java.lang.reflect.Field;

import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONObject;

public class JsonUtils {
	public static Object getFieldObject(Field field, JSONObject object, String key) {
		Object res = null;
		Class<?> fieldClazz = field.getType();
		if (classEquals(fieldClazz, String.class)) {
			res = object.getString(key);
		} else if (classEquals(fieldClazz, Integer.class) || classEquals(fieldClazz, int.class)) {
			res = getJsonInt(object, key, 0);
		} else if (classEquals(fieldClazz, Long.class) || classEquals(fieldClazz, long.class)) {
			res = getJsonLong(object, key, 0);
		} else if (classEquals(fieldClazz, Double.class) || classEquals(fieldClazz, double.class)) {
			res = getJsonDouble(object, key, 0);
		} 
		return res;
	}
	
	public static boolean classEquals(Class<?> clazz1, Class<?> clazz2) {
		return (clazz1.isAssignableFrom(clazz2) || clazz2.equals(clazz1));
	}
	
	public static int getJsonInt(JSONObject object, String key, int defaultValue) {
		Object value = object.get(key);
		if (value instanceof Number) {
			return ((Number) value).intValue();
		} else {
			return NumberUtils.toInt(String.valueOf(value), defaultValue);
		}
	}
	public static long getJsonLong(JSONObject object, String key, long defaultValue) {
		Object value = object.get(key);
		if (value instanceof Number) {
			return ((Number) value).longValue();
		} else {
			return NumberUtils.toLong(String.valueOf(value), defaultValue);
		}
	}
	public static double getJsonDouble(JSONObject object, String key, double defaultValue) {
		Object value = object.get(key);
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		} else {
			return NumberUtils.toDouble(String.valueOf(value), defaultValue);
		}
	}
}
