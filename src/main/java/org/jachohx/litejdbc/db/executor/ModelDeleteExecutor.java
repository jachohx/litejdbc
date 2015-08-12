package org.jachohx.litejdbc.db.executor;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jachohx.litejdbc.Model;
import org.jachohx.litejdbc.db.meta.ModelDBMeta;
import org.jachohx.litejdbc.db.meta.TableMeta;

public class ModelDeleteExecutor extends ModelChangeExecutor {

	public <T extends Model> long delete(T t) throws Exception {
		return execute(t);
	}
	
	@Override
	protected SqlData toSqlData(TableMeta tableMeta, ModelDBMeta meta) {
		Map<String, Object> wheres = meta.getPrimaryKeys();
		List<String> whereKeys = new ArrayList<String>(wheres.size());
		List<Object> values = new ArrayList<Object>(wheres.size());
		for (Map.Entry<String, Object> entry: wheres.entrySet()) {
			whereKeys.add(entry.getKey() + " = ?");
			values.add(entry.getValue());
		}
		String wheresStr = StringUtils.join(whereKeys, " and ");
		String sql = "delete from " + tableMeta.getTableName() + " where " + wheresStr;
		return new SqlData(sql, values);
	}

}
