package org.jachohx.litejdbc.db.utils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import org.jachohx.litejdbc.Model;
import org.jachohx.litejdbc.db.meta.ResultSetMeta;

public class ColumnUtils {
	public static final String COLUMN_UPDATEAT_KEY = "updateat";
	public static final String COLUMN_UPDATE_AT_KEY = "update_at";
	public static final String COLUMN_CREATEAT_KEY = "createat";
	public static final String COLUMN_CREATE_AT_KEY = "create_at";
	
	public static Object getObject(Field field, int type, ResultSet rs, String columnName) throws SQLException {
		Object res = null;
		if (field == null) {
			res = getResultSetObject(type, rs, columnName);
		} else {
			res = getFieldObject(field, rs, columnName);
		}
		return res;
	}
	
	public static <T extends Model> boolean isEmpty(Field field, T t) throws Exception {
		if (field == null)return false;
		field.setAccessible(true);
		Object object = field.get(t);
		field.setAccessible(false);
		if (object == null) return false;
		Class<?> fieldClazz = field.getType();
		if (classEquals(fieldClazz, Integer.class) || classEquals(fieldClazz, int.class)) {
			if (((Integer) object) == 0)return false;
		}
		return true;
	}
	
	private static Object getResultSetObject(int type, ResultSet rs, String columnName) throws SQLException {
		Object object = null;
		switch (type) {
			case Types.DATE : 
	        case Types.TIME:
	        case Types.TIMESTAMP:
	        	object = getColumnDate(rs, columnName);;
	        	break;
	        default:
	        	object =  rs.getObject(columnName);
	    }
		return object;
	}
	
	private static Object getFieldObject(Field field, ResultSet rs, String columnName) throws SQLException {
		Object res = null;
		Class<?> fieldClazz = field.getType();
		if (classEquals(fieldClazz, String.class)) {
			res = rs.getString(columnName);
		} else if (classEquals(fieldClazz, Integer.class) || classEquals(fieldClazz, int.class)) {
			res = rs.getInt(columnName);
		} else if (classEquals(fieldClazz, Long.class) || classEquals(fieldClazz, long.class)) {
			res = rs.getLong(columnName);
		} else if (classEquals(fieldClazz, Date.class)) {
			res = getColumnDate(rs, columnName);
		}
		return res;
	}
	
	
	public static boolean classEquals(Class<?> clazz1, Class<?> clazz2) {
		return (clazz1.isAssignableFrom(clazz2) || clazz2.equals(clazz1));
	}
	
	public static ResultSetMeta getResultSetMeta(ResultSetMetaData metaData, int index) throws SQLException {
		String lableName = metaData.getColumnLabel(index);
    	String columnName = metaData.getColumnName(index);
    	int type = metaData.getColumnType(index);
    	String typeName = metaData.getColumnTypeName(index);
    	return new ResultSetMeta(columnName, type, typeName, lableName);
	}
	
	private static Date getColumnDate(ResultSet rs, String columnName) throws SQLException {
		Timestamp time = rs.getTimestamp(columnName);
		if (time == null) return null;
		return new Date(time.getTime());
	}
	
	/**
	 * 是否是createAt字段
	 * @param columnName
	 * @return
	 */
	public static boolean isCreateDate(String columnName) {
		if (COLUMN_CREATEAT_KEY.equals(columnName.toLowerCase()) || COLUMN_CREATE_AT_KEY.equals(columnName.toLowerCase())){
			return true;
		}
		return false;
	}
	
	/**
	 * 是否是updateAt字段
	 * @param columnName
	 * @return
	 */
	public static boolean isUpdateDate(String columnName) {
		if (COLUMN_UPDATEAT_KEY.equals(columnName.toLowerCase()) || COLUMN_UPDATE_AT_KEY.equals(columnName.toLowerCase())){
			return true;
		}
		return false;
	}
	
}
