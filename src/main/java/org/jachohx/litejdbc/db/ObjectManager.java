package org.jachohx.litejdbc.db;

import java.util.List;
import java.util.Map;

import org.jachohx.litejdbc.db.executor.ObjectQueryExecutor;

public class ObjectManager {
	public static List<Map<String, Object>> list(String sql, Object[] params) {
		return new ObjectQueryExecutor().list(sql, params);
	}
}
