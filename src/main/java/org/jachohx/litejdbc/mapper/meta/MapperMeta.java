package org.jachohx.litejdbc.mapper.meta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapperMeta implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean isManageMapper = false;
	private Map<String, MapperValueMeta> fromMapperMetas = new HashMap<String, MapperValueMeta>();
	private Map<String, MapperValueMeta> toMapperMetas = new HashMap<String, MapperValueMeta>();
	private Set<String> displayMetas = new HashSet<String>();
	
	public void addFromMapperMeta(String key, MapperValueMeta mapperValueMeta) {
		this.fromMapperMetas.put(key, mapperValueMeta);
	}
	public void addFromMapperMetas(Map<String, MapperValueMeta> mapperMetas) {
		this.fromMapperMetas.putAll(mapperMetas);
	}
	public Map<String, MapperValueMeta> getFromMapperMetas() {
		return fromMapperMetas;
	}
	public void setFromMapperMetas(Map<String, MapperValueMeta> mapperMetas) {
		this.fromMapperMetas = mapperMetas;
	}
	
	public void addToMapperMeta(String key, MapperValueMeta mapperValueMeta) {
		this.toMapperMetas.put(key, mapperValueMeta);
	}
	public void addToMapperMetas(Map<String, MapperValueMeta> mapperMetas) {
		this.toMapperMetas.putAll(mapperMetas);
	}
	public Map<String, MapperValueMeta> getToMapperMetas() {
		return toMapperMetas;
	}
	public void setToMapperMetas(Map<String, MapperValueMeta> mapperMetas) {
		this.toMapperMetas = mapperMetas;
	}
	
	public void addDisplayMeta(String key) {
		displayMetas.add(key);
	}
	public boolean isDisplayMeta(String key) {
		return displayMetas.contains(key);
	}

	public boolean isManageMapper() {
		return isManageMapper;
	}
	public void setManageMapper(boolean isManageMapper) {
		this.isManageMapper = isManageMapper;
	}

}
