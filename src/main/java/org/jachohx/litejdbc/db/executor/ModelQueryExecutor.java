package org.jachohx.litejdbc.db.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jachohx.litejdbc.Model;
import org.jachohx.litejdbc.db.ModelRegistry;
import org.jachohx.litejdbc.db.meta.TableMeta;

public class ModelQueryExecutor{
	
	public final static String QUERY_PARAM = "query";
    public final static String SELECT_PARAM = "select";
    public final static String ORDER_BY_PARAM = "orderBy";
    public final static String LIMIT_PARAM = "limit";
    public final static String LIMIT_START_PARAM = "limitStart";
    public final static String LIMIT_END_PARAM = "limitEnd";
    
    TableMeta tableMeta = null;
    Class<? extends Model> clazz = null;
    DBExecutor dbExecutor = null;
    String tableName = null;
    
    public <T extends Model> ModelQueryExecutor(Class<? extends Model> clazz) {
    	this.clazz = clazz;
		tableMeta = ModelRegistry.instance().init(clazz).getTableMeta(clazz);
		tableName = tableMeta.getTableName();
		dbExecutor = new DBExecutor(tableMeta.getDbName());
	}

	public long count(Map<String, String> opts, Object... params) {
    	String query = getMapValue(opts, QUERY_PARAM);
        if(params != null && params.length != 0 && query.trim().equals("*") && params.length != 0){
            throw new IllegalArgumentException("cannot use '*' and parameters");
        }
        if (opts == null) opts = new HashMap<String, String>();
        String _selectValue = opts.get(SELECT_PARAM);
        
        opts.put(SELECT_PARAM, "count(*)");
        String sql = getSql(opts);
        
        long count = dbExecutor.findLong(sql, params);
        opts.put(SELECT_PARAM, _selectValue);
        return count;
    }
    
	public <T extends Model> T first(Map<String, String> opts, Object ... params) {
		if (opts == null) opts = new HashMap<String, String>();
		String _LimitValue = opts.get(LIMIT_PARAM);

		opts.put(LIMIT_PARAM, "1");
		String sql = getSql(opts);
		
		T t = dbExecutor.findObject(clazz, sql, params);
		opts.put(LIMIT_PARAM, _LimitValue);
        return t;
    }
	
	public <T extends Model> List<T> all(Map<String, String> opts, Object... params) {
		String sql = getSql(opts);
		return dbExecutor.findList(clazz, sql, params);
	}
    
	public <T extends Model> List<T> find(Map<String, String> opts, Object ... params) {
		String sql = getSql(opts);
        return dbExecutor.findList(clazz, sql, params);
    }

	
	private String getSql(Map<String, String> opts) {
    	String select = getMapValue(opts, SELECT_PARAM, "*");
    	String query = getMapValue(opts, QUERY_PARAM);
		String orderBy = getMapValue(opts, ORDER_BY_PARAM);
		String limit = getMapValue(opts, LIMIT_PARAM);
		if (StringUtils.isBlank(limit)) {
			long limitStart = NumberUtils.toLong(getMapValue(opts, LIMIT_START_PARAM), -1l);
			if (limitStart != -1l) {
				limit = String.valueOf(limitStart);
				long limitEND = NumberUtils.toLong(getMapValue(opts, LIMIT_END_PARAM), -1l);
				if (limitEND != -1l) limit += "," + limitEND;
			}
		}
		String sql = "SELECT " + select + " FROM " + tableName + 
				(StringUtils.isNotBlank(query) ? " WHERE " + query : "") + 
				(StringUtils.isNotBlank(orderBy) ? " ORDER BY " + orderBy : "") + 
				(StringUtils.isNotBlank(limit) ? " limit " + limit : "");
       return sql;
    }
    
    private String getMapValue(Map<String, String> values, String key) {
    	return getMapValue(values, key, null);
    }
    
    private String getMapValue(Map<String, String> values, String key, String defaultValue) {
    	if (values == null) return defaultValue;
    	String value = values.get(key);
    	if (value == null) value = defaultValue;
    	return value;
    }
    
}
