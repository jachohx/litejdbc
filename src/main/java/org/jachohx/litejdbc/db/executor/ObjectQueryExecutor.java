package org.jachohx.litejdbc.db.executor;

import java.util.List;
import java.util.Map;

public class ObjectQueryExecutor{
	public List<Map<String, Object>> list(String sql, Object[] params) {
		return new DBExecutor().findListMap(sql, params);
	}
	
	public List<Map<String, Object>> list(String dbName, String sql, Object[] params) {
		return new DBExecutor(dbName).findListMap(sql, params);
	}
}
