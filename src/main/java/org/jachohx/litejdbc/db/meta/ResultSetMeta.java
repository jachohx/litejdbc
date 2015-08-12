package org.jachohx.litejdbc.db.meta;

public class ResultSetMeta {
	private String columnName;
	private int type;
	private String typeName;
	private String lableName;
	public ResultSetMeta() {
	}
	public ResultSetMeta(String columnName, int type, String typeName, String lableName) {
		this.columnName = columnName;
		this.type = type;
		this.typeName = typeName;
		this.lableName = lableName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getLableName() {
		return lableName;
	}
	public void setLableName(String lableName) {
		this.lableName = lableName;
	}
}
