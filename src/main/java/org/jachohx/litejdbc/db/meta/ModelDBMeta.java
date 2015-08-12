package org.jachohx.litejdbc.db.meta;

import java.util.Map;

public class ModelDBMeta {
	
	/**
	 * 除去primaryKeys、ids的其它参数
	 */
	private Map<String, Object> params;
	private Map<String, Object> primaryKeys;
	private Map<String, Object> ids;
	public ModelDBMeta(Map<String, Object> params, Map<String, Object> primaryKeys, Map<String, Object> ids){
		this.params = params;
		this.primaryKeys = primaryKeys;
		this.ids = ids;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	public Map<String, Object> getPrimaryKeys() {
		return primaryKeys;
	}
	public void setPrimaryKeys(Map<String, Object> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}
	public Map<String, Object> getIds() {
		return ids;
	}
	public void setIds(Map<String, Object> ids) {
		this.ids = ids;
	}
}
