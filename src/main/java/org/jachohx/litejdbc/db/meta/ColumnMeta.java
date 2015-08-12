package org.jachohx.litejdbc.db.meta;

import java.io.Serializable;
import java.lang.reflect.Field;

public class ColumnMeta implements Serializable {

	private static final long serialVersionUID = 1L;
	private String columnName;
	private String typeName;
    private int columnSize;
    private int javaType;
    private Field field;

    public ColumnMeta(String columnName, String  typeName, int columnSize, int javaType, Field field) {
        this.columnName = columnName;
        this.typeName = typeName;
        this.columnSize = columnSize;
        this.javaType = javaType;
        this.field = field;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getJavaType() {
		return javaType;
	}

	public Field getField() {
		return field;
	}

	public String toString() {
        return "[ columnName=" + columnName
                + ", typeName=" + typeName
                + ", columnSize=" + columnSize
                + ", javaType=" + javaType
                + ", field=" + (field != null ? "(" + field.getType() + ")" + field.getName() : null)
                + " ]";
    }
}
