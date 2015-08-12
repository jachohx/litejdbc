package org.jachohx.litejdbc.db.executor;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jachohx.litejdbc.Model;
import org.jachohx.litejdbc.db.meta.ModelDBMeta;
import org.jachohx.litejdbc.db.meta.TableMeta;

public class ModelCreateExecutor extends ModelChangeExecutor {

	public <T extends Model> long insert(T t) throws Exception {
		return execute(t);
	}
	
	public <T extends Model> long insertAndIgnore(T t) throws Exception {
		return execute(t, 1);
	}
	
	@Override
	protected void handleSqlData(SqlData sqlData, int status) {
		if (status == 1) {
			String sql = sqlData.getSql();
			sql = sql.replace("insert", "insert IGNORE");
			sqlData.setSql(sql);
		}
	}	

	@Override
	protected SqlData toSqlData(TableMeta tableMeta, ModelDBMeta meta) {
		Map<String, Object> params = meta.getParams();
		Map<String, Object> pks = meta.getPrimaryKeys();
		Map<String, Object> ids = meta.getIds();
		
		//增加pk参数，让pk放在最前边
		pks.putAll(params);
		params = pks;
		//去除idGen
		for (String id : ids.keySet())
			params.remove(id);
		
		List<String> keys = new ArrayList<String>(params.size());
		List<String> values = new ArrayList<String>(params.size());
		List<Object> objects = new ArrayList<Object>(params.size());
		
		for (Map.Entry<String, Object> entry: params.entrySet()) {
			keys.add(entry.getKey());
			values.add("?");
			objects.add(entry.getValue());
		}
		String keyStr = StringUtils.join(keys, ", ");
		String valueStr = StringUtils.join(values, ", ");
		String sql = "insert " + tableMeta.getTableName() + " ( " + keyStr + " ) values ( " + valueStr + " )";
		
		String idGenerator = tableMeta.getIdGenerator();
		SqlData sqlData = new SqlData(sql, objects);
		sqlData.setIdGenerator(idGenerator);
		return sqlData;
	}
	
	@Override
	protected long execute(String dbName, SqlData sqlData) {
		String sql = sqlData.getSql();
		String idGenerator = sqlData.getIdGenerator();
		Object[] params = sqlData.getParams().toArray();
		return (new DBExecutor(dbName).insert(sql, idGenerator, params));
	}

}
