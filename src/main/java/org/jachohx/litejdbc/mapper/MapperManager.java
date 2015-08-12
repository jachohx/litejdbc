package org.jachohx.litejdbc.mapper;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

import org.jachohx.litejdbc.LogFilter;
import org.jachohx.litejdbc.Model;
import org.jachohx.litejdbc.exception.DBException;
import org.jachohx.litejdbc.exception.JsonException;
import org.jachohx.litejdbc.mapper.meta.MapperMeta;
import org.jachohx.litejdbc.mapper.meta.MapperValueMeta;
import org.jachohx.litejdbc.mapper.utils.JsonUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MapperManager {
	private static final Logger log = LoggerFactory.getLogger(MapperManager.class);
	public static MapperMeta getMapperMeta(Class<? extends Model> clazz) {
		MapperMeta mapperMeta = MapperRegistry.instance().init(clazz).getMapperMeta(clazz);
    	if (mapperMeta == null) throw new DBException("class name [" + clazz.getName() + "] no tablemate");
    	return mapperMeta;
    }
	
	public static <T extends Model> T fromJson(T t, JSONObject object) {
		MapperMeta jsonMeta = getMapperMeta(t.getClass());
		if (!jsonMeta.isManageMapper()) throw new JsonException("No mapper manage");
		Map<String, MapperValueMeta> jsonObjects = jsonMeta.getFromMapperMetas();
		return setModelJson(t, object, jsonObjects);
	}
	
	@SuppressWarnings("rawtypes")
	private static <T extends Model> T setModelJson(T t, JSONObject object, Map<String, MapperValueMeta> jsonObjects) {
		Iterator it = object.keys();
		while (it.hasNext()) {
			String key = (String)it.next();
			MapperValueMeta objectMeta = jsonObjects.get(key);
			if (objectMeta == null) continue;
			Field field = objectMeta.getField();
			field.setAccessible(true);
			try {
				Object value = JsonUtils.getFieldObject(field, object, key);
				if (value == null) continue;
				field.set(t, value);
			} catch (IllegalArgumentException e) {
				LogFilter.error(log, "IllegalArgumentException JSONObject [" + object + " ], field:" + field.getName() + ", key:" + key, e.getCause());
			} catch (JSONException e) {
				LogFilter.error(log, "JSONException JSONObject [" + object + " ], field:" + field.getName() + ", key:" + key + ", error " + e.getMessage(), e.getCause());
			} catch (IllegalAccessException e) {
				LogFilter.error(log, "IllegalAccessException JSONObject [" + object + " ], field:" + field.getName() + ", key:" + key, e.getCause());
			} finally {
				field.setAccessible(false);
			}
		}
		return t;
	}
}
