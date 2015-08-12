package org.jachohx.litejdbc.mapper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jachohx.litejdbc.annotations.Mapper;
import org.jachohx.litejdbc.annotations.ManageMapper;
import org.jachohx.litejdbc.LogFilter;
import org.jachohx.litejdbc.Model;
import org.jachohx.litejdbc.mapper.meta.MapperMeta;
import org.jachohx.litejdbc.mapper.meta.MapperValueMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapperRegistry {
	private static MapperRegistry INSTANCE = new MapperRegistry();
	private final static String FIELD_MAPPER_FROM_KEY = "mapper_from";
	private final static String FIELD_MAPPER_TO_KEY = "mapper_to";
	private final static String FIELD_MAPPER_IS_DISPLAY_KEY = "mapper_is_display";
	
	private MapperRegistry() {
	}
	
	private final static Logger logger = LoggerFactory.getLogger(MapperRegistry.class);
	Map<Class<? extends Model>, MapperMeta> mappers = new HashMap<Class<? extends Model>, MapperMeta>();
	public static MapperRegistry instance() {
        return INSTANCE;
    }
	
	public synchronized MapperRegistry init(Class<? extends Model> clazz) {
		if (mappers.containsKey(clazz)) 
			return this;
		MapperMeta meta = initMapperMeta(clazz);
		Map<String, Map<String, Field>> paramMap = getFields(clazz);
		//mapper
		if (meta.isManageMapper()) {
			Map<String, Field> fromMap = paramMap.get(FIELD_MAPPER_FROM_KEY);
			for (Entry<String, Field> entry : fromMap.entrySet()) {
				meta.addFromMapperMeta(entry.getKey(), new MapperValueMeta(entry.getKey(), entry.getValue()));
			}
			Map<String, Field> toMap = paramMap.get(FIELD_MAPPER_TO_KEY);
			for (Entry<String, Field> entry : toMap.entrySet()) {
				meta.addToMapperMeta(entry.getKey(), new MapperValueMeta(entry.getKey(), entry.getValue()));
			}
			Map<String, Field> displayMap = paramMap.get(FIELD_MAPPER_IS_DISPLAY_KEY);
			for (Entry<String, Field> entry : displayMap.entrySet()) {
				meta.addDisplayMeta(entry.getKey());
			}
		}
		mappers.put(clazz, meta);
		LogFilter.log(logger, "Fetched metadata for mapper: {}", clazz.getName());
		return this;
	}
	
	public MapperMeta getMapperMeta(Class<? extends Model> clazz) {
		return mappers.get(clazz);
	}
	
	private MapperMeta initMapperMeta(Class<? extends Model> clazz) {
		boolean isManageMapper = findManageMapper(clazz);
		MapperMeta mapperMeta = new MapperMeta();
		mapperMeta.setManageMapper(isManageMapper);
		return mapperMeta;
    }
	
	private boolean findManageMapper(Class<? extends Model> clazz) {
		ManageMapper manageMapperAnnotation = clazz.getAnnotation(ManageMapper.class);
		return manageMapperAnnotation != null && "true".equals(manageMapperAnnotation.value()) ? true : false;
	}
	
	/**
	 * 得到类的field信息
	 * @param clazz
	 * @return
	 */
	private Map<String, Map<String, Field>> getFields(Class<? extends Model> clazz) {
		Map<String, Field> fromMap = new HashMap<String, Field>();
		Map<String, Field> toMap = new HashMap<String, Field>();
		Map<String, Field> displayMap = new HashMap<String, Field>();
		Map<String, Map<String, Field>> result = new HashMap<String, Map<String,Field>>();
		result.put(FIELD_MAPPER_FROM_KEY, fromMap);
		result.put(FIELD_MAPPER_TO_KEY, toMap);
		result.put(FIELD_MAPPER_IS_DISPLAY_KEY, displayMap);
		Field[] fields = clazz.getDeclaredFields();
		if (fields.length == 0) return result;
		for (Field field : fields) {
			String from = field.getName();
			String to = field.getName();
			boolean isDisplay = Mapper.DEFAULT_DISPLAY;
			if (field.isAnnotationPresent(Mapper.class)) {
				//from值，如果from为默认值，则使用字段名
				from = field.getAnnotation(Mapper.class).from();
				if (Mapper.DEFAULT_VALUE.equals(from)) from = field.getName();
				//to值，如果to为默认值，则使用字段名
				to = field.getAnnotation(Mapper.class).to();
				if (Mapper.DEFAULT_VALUE.equals(to)) to = field.getName();
				
				isDisplay = field.getAnnotation(Mapper.class).isDisplay();
			}
			fromMap.put(from, field);
			toMap.put(to, field);
			//display值，如果display为默认值，则显示，否则不显示
			if (Mapper.DISPLAY_VALUE == isDisplay) displayMap.put(to, field);;
		}
		return result;
	}
	
}