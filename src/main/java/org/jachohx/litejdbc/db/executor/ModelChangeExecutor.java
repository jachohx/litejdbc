package org.jachohx.litejdbc.db.executor;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jachohx.litejdbc.Model;
import org.jachohx.litejdbc.db.ModelRegistry;
import org.jachohx.litejdbc.db.meta.ColumnMeta;
import org.jachohx.litejdbc.db.meta.ModelDBMeta;
import org.jachohx.litejdbc.db.meta.TableMeta;
import org.jachohx.litejdbc.db.utils.ColumnUtils;
import org.jachohx.litejdbc.util.FieldUtils;

public abstract class ModelChangeExecutor{
	
	class SqlData{
		private String sql;
		private List<Object> params;
		private String idGenerator;
		public SqlData(String sql, List<Object> params){
			this.sql = sql;
			this.params = params;
		}
		public String getSql() {
			return sql;
		}
		public void setSql(String sql) {
			this.sql = sql;
		}
		public List<Object> getParams() {
			return params;
		}
		public void setParams(List<Object> params) {
			this.params = params;
		}
		public String getIdGenerator() {
			return idGenerator;
		}
		public void setIdGenerator(String idGenerator) {
			this.idGenerator = idGenerator;
		}
	}
	
	
	/**
	 * 得到update的SQL语句，如果表的createAt受管理，则忽略createAt
	 * @param tableMeta
	 * @return
	 */
	protected <T extends Model> ModelDBMeta getModelDBMeta(T t, TableMeta tableMeta) throws Exception{
		Map<String, ColumnMeta> columnMetas= tableMeta.getColumnMetas();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		Map<String, Object> pks = new LinkedHashMap<String, Object>();
		Map<String, Object> ids = new LinkedHashMap<String, Object>();
		for (Entry<String, ColumnMeta> entry : columnMetas.entrySet()) {
			String columnName = entry.getKey();
			ColumnMeta columnMeta = entry.getValue();
			
			Object value = getColumnMetaObject(t, tableMeta, columnName, columnMeta);
			if (value == null) continue;
			boolean isPrimarykey = tableMeta.isPrimaryKey(columnName);
			boolean isIdGenerator = tableMeta.isIdGenerator(columnName);
			if (isPrimarykey || isIdGenerator) {
				if (isPrimarykey) {
					pks.put(columnName, value);
				}
				if (isIdGenerator) {
					ids.put(columnName, value);
				}
			} else {
				params.put(columnName, value);
			}
		}
		return new ModelDBMeta(params, pks, ids);
	}
	
	/**
	 * 得到columnName对应T的值
	 * @param <T>
	 * @param t
	 * @param tableMeta
	 * @param columnName
	 * @param columnMeta
	 * @return
	 * @throws Exception
	 */
	protected <T extends Model> Object getColumnMetaObject(T t, TableMeta tableMeta, String columnName, ColumnMeta columnMeta) throws Exception {
		Field field = columnMeta.getField();
		//没有field的时候
		if (field == null) {
			if (ColumnUtils.isCreateDate(columnName) || ColumnUtils.isUpdateDate(columnName))
				return FieldUtils.getDate(columnMeta);
			return null;
		}
		Object res = null;
		field.setAccessible(true);
		//如果是createAt或updateAt的时候
		if (tableMeta.isManageDate() && (ColumnUtils.isCreateDate(columnName) || ColumnUtils.isUpdateDate(columnName))) {
			res = FieldUtils.getDate(field);
		} else {
			res = field.get(t);
		}
		field.setAccessible(false);
		return res;
	}
	
	protected abstract SqlData toSqlData(TableMeta tableMeta, ModelDBMeta meta);
	
	/**
	 * @param tableMeta
	 * @param params
	 * @param manages	参数1为是否管理update_at，参数2为是否管理create_at（值：1增加，0不处理，-1删除）
	 */
	protected void manageData(TableMeta tableMeta, Map<String, Object> params, int... manages) {
		if (!tableMeta.isManageDate()) return;
		int index = 0;
		
		//update_at
		if (manages == null || manages.length <= index)return;
		manageData(params, tableMeta.getUpdateAtColumnMeta(), manages[index++]);
		//create_at
		if (manages == null || manages.length <= index)return;
		manageData(params, tableMeta.getCreateAtColumnMeta(), manages[index++]);
	}
	
	protected void manageData(Map<String, Object> params, ColumnMeta columnMeta, int status ) {
		if (columnMeta == null || status == 0) return;
		String columnName = columnMeta.getColumnName();
		switch (status) {
			case -1:
				params.remove(columnName);
				break;
			case 1:
				if (params.get(columnName) == null) {
					params.put(columnName, FieldUtils.getDate(columnMeta));
				}
				break;
			default:
				break;
		}
	}
	
	protected <T extends Model> long execute(T t) throws Exception {
		return execute(t, 0);
	}
	
	protected <T extends Model> long execute(T t, int handelStatus) throws Exception {
		Class<? extends Model> clazz = t.getClass();
		TableMeta tableMeta = ModelRegistry.instance().init(clazz).getTableMeta(clazz);
		ModelDBMeta modelDBMeta = getModelDBMeta(t, tableMeta);
		return execute(clazz, handelStatus, modelDBMeta);
	}
	
	protected <T extends Model> long execute(Class<? extends Model> clazz, int handelStatus, ModelDBMeta modelDBMeta) throws Exception {
		TableMeta tableMeta = ModelRegistry.instance().init(clazz).getTableMeta(clazz);
		SqlData sqlData = toSqlData(tableMeta, modelDBMeta);
		handleSqlData(sqlData, handelStatus);
		return execute(tableMeta.getDbName(), sqlData);
	}
	
	protected long execute(String dbName, SqlData sqlData) {
		String sql = sqlData.getSql();
		Object[] params = sqlData.getParams().toArray();
		return (new DBExecutor(dbName).exec(sql, params));
	}
	
	protected void handleSqlData(SqlData sqlData, int status) {
	}
	
}
