package org.jachohx.litejdbc.db.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TableMeta implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String dbName = null;
	private String tableName = null;
	private String idGenerator = null;
	private boolean isManageDate = true;
	private List<String> primaryKeys = new ArrayList<String>();
	private ColumnMeta updateAtColumnMeta;
	private ColumnMeta createAtColumnMeta;
	private Map<String, ColumnMeta> columnMetas = new HashMap<String, ColumnMeta>();
	
	public TableMeta() {
	}
	public TableMeta(String dbName, String tableName, String idGenerator) {
		this.dbName = dbName;
		this.tableName = tableName;
		this.idGenerator = idGenerator;
	}
	
	
	public void addColumnMeta(String columnName, ColumnMeta columnMeta){
		columnMetas.put(columnName, columnMeta);
	}
	
	public void addColumnMetas(Map<String, ColumnMeta> columnMetas){
		this.columnMetas.putAll(columnMetas);
	}
	
	public ColumnMeta getColumnMeta(String columnName) {
		return columnMetas.get(columnName);
	}
	
	public Map<String, ColumnMeta> getColumnMetas(){
		return columnMetas;
	}
	
	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getIdGenerator() {
		return idGenerator;
	}
	public void setIdGenerator(String idGenerator) {
		this.idGenerator = idGenerator;
	}
	public boolean isIdGenerator(String key) {
		return idGenerator != null ? idGenerator.equals(key) : false;
	}
	public boolean isManageDate() {
		return isManageDate;
	}
	public void setManageDate(boolean isManageDate) {
		this.isManageDate = isManageDate;
	}
	public void addPrimaryKeys(Collection<String> primaryKeys) {
		this.primaryKeys.addAll(primaryKeys);
	}
	public List<String> getPrimaryKeys() {
		return primaryKeys;
	}
	public boolean isPrimaryKey(String key) {
		return primaryKeys.contains(key);
	}
	public ColumnMeta getUpdateAtColumnMeta() {
		return updateAtColumnMeta;
	}
	public void setUpdateAtColumnMeta(ColumnMeta updateAtColumnMeta) {
		this.updateAtColumnMeta = updateAtColumnMeta;
	}
	public ColumnMeta getCreateAtColumnMeta() {
		return createAtColumnMeta;
	}
	public void setCreateAtColumnMeta(ColumnMeta createAtColumnMeta) {
		this.createAtColumnMeta = createAtColumnMeta;
	}
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder("dbName: " + dbName).append("\r\n");
		res.append("tableName: " + tableName).append("\r\n");
		res.append("idGenerator: " + idGenerator).append("\r\n");
		res.append("isManageDate: " + isManageDate).append("\r\n");
		res.append("primaryKeys: ");
		for (int i = 0; i < primaryKeys.size(); i++) {
			String primaryKey = primaryKeys.get(i);
			if(i > 0)res.append(primaryKey).append(", ");
			res.append(primaryKey);
		}
		res.append("\r\n");
		res.append("columnMetas:").append("\r\n");
		for (Entry<String, ColumnMeta> entry : columnMetas.entrySet()) {
			res.append(entry).append("\r\n");
		}
		return res.toString();
	}
}
