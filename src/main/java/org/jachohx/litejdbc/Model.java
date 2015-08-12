package org.jachohx.litejdbc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jachohx.litejdbc.db.ModelManager;
import org.jachohx.litejdbc.db.meta.ColumnMeta;

@SuppressWarnings("unchecked")
public class Model {
	Map<String, Object> values = new HashMap<String, Object>();
	Map<String, String> opts = new HashMap<String, String>();
	Map<String, Object> sets = new LinkedHashMap<String, Object>();
	Map<String, Object> wheres = new LinkedHashMap<String, Object>();
	List<String> selects = new ArrayList<String>();
	
	public String getTableName() {
		return ModelManager.getTableName(getClass());
	}
	
	public long count() throws Exception {
		return ModelManager.count(getClass(), opts);
	}
	
	public  <T extends Model> T first() throws Exception{
		return (T) ModelManager.first(getClass(), opts);
	}
	
	public <T extends Model> List<T> all() throws Exception {
		return (List<T>) ModelManager.all(getClass(), opts);
	}
	
	public <T extends Model> Pager<T> pager(int pageNo, int pageSize) throws Exception {
		return (Pager<T>) ModelManager.pager(getClass(), opts, pageNo, pageSize);
	}
	
	public long save() throws Exception{
		return ModelManager.save(this);
	}
	
	public long insert() throws Exception{
		return ModelManager.insert(this);
	}
	/**
	 * 如果插入的数据已存在，则忽略
	 * @return
	 * @throws Exception
	 */
	public long insertAndIgnore() throws Exception{
		return ModelManager.insertAndIgnore(this);
	}
	
	public long update() throws Exception{
		return ModelManager.update(this);
	}
	
	public Model addUpdateObject(String key, Object value) {
		sets.put(key, value);
		return this;
	}
	
	public Model addUpdateCondition(String key, Object value) {
		wheres.put(key, value);
		return this;
	}
	
	public long doUpdate() throws Exception{
		return ModelManager.update(getClass(), sets, wheres);
	}

	public long delete() throws Exception{
		return ModelManager.delete(this);
	}
	
	public Model limit(int limit) {
		opts.put(ModelManager.LIMIT_PARAM, String.valueOf(limit));
		return this;
	}
	
	public Model setQuery(String query) {
		opts.put(ModelManager.QUERY_PARAM, query);
		return this;
	}
	
	public Model setOrderBy(String orderBy) {
		opts.put(ModelManager.ORDER_BY_PARAM, orderBy);
		return this;
	}
	
	public void setValues(Map<String, Object> values) {
		this.values = values;
	}
	
	public Object getValue(String column){
		return values.get(column);
	}

	public void clear() {
		values.clear();
		opts.clear();
		sets.clear();
		wheres.clear();
		selects.clear();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		sb.append("[ ");
		for (Entry<String, ColumnMeta> entry: ModelManager.getTableMeta(getClass()).getColumnMetas().entrySet()){
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
