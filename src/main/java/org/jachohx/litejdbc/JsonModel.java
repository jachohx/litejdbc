package org.jachohx.litejdbc;

import java.lang.reflect.Field;
import java.util.Map.Entry;

import org.jachohx.litejdbc.mapper.MapperManager;
import org.jachohx.litejdbc.mapper.meta.MapperMeta;
import org.jachohx.litejdbc.mapper.meta.MapperValueMeta;
import org.json.JSONObject;

@SuppressWarnings("unchecked")
public class JsonModel extends Model{
	
	public <T extends Model> T fromJson(JSONObject object) throws Exception {
		return (T) MapperManager.fromJson(this, object);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		MapperMeta meta = MapperManager.getMapperMeta(getClass());
		int count = 0;
		sb.append("[ ");
		for (Entry<String, MapperValueMeta> entry: meta.getToMapperMetas().entrySet()){
			if (!meta.isDisplayMeta(entry.getKey())) continue;
			if (count != 0) sb.append(", ");
			Field field = entry.getValue().getField();
			field.setAccessible(true);
			try {
				sb.append(field.getName() + " = " + field.get(this));
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} finally {
				field.setAccessible(true);
			}
			count ++;
		}
		sb.append(" ]");
		return sb.toString();
	}
}
