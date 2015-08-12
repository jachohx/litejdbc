package org.jachohx.litejdbc.db;

import java.util.List;
import java.util.Map;

import org.jachohx.litejdbc.Model;
import org.jachohx.litejdbc.Pager;
import org.jachohx.litejdbc.db.executor.ModelCreateExecutor;
import org.jachohx.litejdbc.db.executor.ModelDeleteExecutor;
import org.jachohx.litejdbc.db.executor.ModelQueryExecutor;
import org.jachohx.litejdbc.db.executor.ModelUpdateExecutor;
import org.jachohx.litejdbc.db.meta.ColumnMeta;
import org.jachohx.litejdbc.db.meta.ModelDBMeta;
import org.jachohx.litejdbc.db.meta.TableMeta;
import org.jachohx.litejdbc.db.utils.ColumnUtils;
import org.jachohx.litejdbc.exception.DBException;


public class ModelManager {
	
	public final static String QUERY_PARAM = ModelQueryExecutor.QUERY_PARAM;
    public final static String SELECT_PARAM = ModelQueryExecutor.SELECT_PARAM;
    public final static String ORDER_BY_PARAM = ModelQueryExecutor.ORDER_BY_PARAM;
    public final static String LIMIT_PARAM = ModelQueryExecutor.LIMIT_PARAM;
    public final static String LIMIT_START_PARAM = ModelQueryExecutor.LIMIT_START_PARAM;
    public final static String LIMIT_END_PARAM = ModelQueryExecutor.LIMIT_END_PARAM;

    public static TableMeta getTableMeta(Class<? extends Model> clazz) {
    	TableMeta tableMeta = ModelRegistry.instance().init(clazz).getTableMeta(clazz);
    	if (tableMeta == null) throw new DBException("class name [" + clazz.getName() + "] no tablemate");
    	return tableMeta;
    }
    
    public static String getTableName(Class<? extends Model> clazz) {
    	TableMeta tableMeta = getTableMeta(clazz);
    	return tableMeta.getTableName();
    }
    
    public static <T extends Model> long count(Class<? extends Model> clazz, Map<String, String> opts) throws Exception {
		return new ModelQueryExecutor(clazz).count(opts);
	}
	
	public static <T extends Model> T first(Class<? extends Model> clazz, Map<String, String> opts) throws Exception{
		return new ModelQueryExecutor(clazz).first(opts);
	}
	
	public static <T extends Model> List<T> all(Class<? extends Model> clazz, Map<String, String> opts) throws Exception {
		return new ModelQueryExecutor(clazz).all(opts);
	}
	
	public static <T extends Model> List<T> find(Class<? extends Model> clazz, Map<String, String> opts) throws Exception {
		return new ModelQueryExecutor(clazz).find(opts);
	}
	
	public static <T extends Model> Pager<T> pager(Class<? extends Model> clazz, Map<String, String> opts, int pageNo, int pageSize) throws Exception {
		long count = count(clazz, opts);
		if (count == 0)
			return new Pager<T>(count, null);
		int start = pageNo > 1 ? (pageNo-1) * pageSize : 0;
		int end = start + pageSize;
		opts.put(LIMIT_START_PARAM, String.valueOf(start));
		opts.put(LIMIT_END_PARAM, String.valueOf(end));
		List<T> list = find(clazz, opts);
		return new Pager<T>(count, list);
	}
	
	public static <T extends Model> long save(T t) throws Exception {
		Class<? extends Model> clazz = t.getClass();
		TableMeta tableMeta = getTableMeta(clazz);
		if (hasIdValue(t, tableMeta)) {
			return update(t);
		} else {
			return insert(t);
		}
	}
	
	public static <T extends Model> long update(T t) throws Exception {
		return new ModelUpdateExecutor().update(t);
	}
	
	public static <T extends Model> long update(Class<? extends Model> clazz, Map<String, Object> sets, Map<String, Object> wheres) throws Exception {
		ModelDBMeta meta = new ModelDBMeta(sets, wheres, null);
		return new ModelUpdateExecutor().update(clazz, meta);
	}
	
	public static <T extends Model> long insert(T t) throws Exception {
		return new ModelCreateExecutor().insert(t);
	}
	
	public static <T extends Model> long insertAndIgnore(T t) throws Exception {
		return new ModelCreateExecutor().insertAndIgnore(t);
	}
	
	public static <T extends Model> long delete(T t) throws Exception {
		return new ModelDeleteExecutor().delete(t);
	}
	
	private static <T extends Model> boolean hasIdValue(T t, TableMeta tableMeta) throws Exception {
		ColumnMeta idColumnMeta = tableMeta.getColumnMeta(tableMeta.getIdGenerator());
		if (idColumnMeta == null) return false;
		return ColumnUtils.isEmpty(idColumnMeta.getField(), t) ;
	}
	
}
