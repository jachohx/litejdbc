package org.jachohx.litejdbc.db.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jachohx.litejdbc.Model;
import org.jachohx.litejdbc.db.meta.ModelDBMeta;
import org.jachohx.litejdbc.db.meta.TableMeta;

public class ModelUpdateExecutor extends ModelChangeExecutor {

	public <T extends Model> long update(T t) throws Exception {
		return execute(t);
	}
	
	public <T extends Model> long update(Class<? extends Model> clazz, ModelDBMeta modelDBMeta) throws Exception {
		return execute(clazz, 0, modelDBMeta);
	}
	
	/**
	 * 得到update的SQL语句及参数
	 * @param tableMeta
	 * @return
	 */
	@Override
	protected SqlData toSqlData(TableMeta tableMeta, ModelDBMeta meta){
		Map<String, Object> sets = meta.getParams();
		Map<String, Object> wheres = meta.getPrimaryKeys();
		manageData(tableMeta, sets, 1, -1);
		List<String> setKeys = new ArrayList<String>(sets.size());
		List<String> whereKeys = new ArrayList<String>(wheres.size());
		List<Object> values = new ArrayList<Object>(sets.size() + wheres.size());
		for (Map.Entry<String, Object> entry: sets.entrySet()) {
			setKeys.add(entry.getKey() + " = ?");
			values.add(entry.getValue());
		}
		for (Map.Entry<String, Object> entry: wheres.entrySet()) {
			whereKeys.add(entry.getKey() + " = ?");
			values.add(entry.getValue());
		}
		String keyStr = StringUtils.join(setKeys, ", ");
		String wheresStr = StringUtils.join(whereKeys, " and ");
		String sql = "update " + tableMeta.getTableName() + " set " + keyStr + " where " + wheresStr;
		return new SqlData(sql, values);
	}

}
