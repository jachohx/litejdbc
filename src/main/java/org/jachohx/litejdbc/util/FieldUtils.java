package org.jachohx.litejdbc.util;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.Date;

import org.jachohx.litejdbc.db.meta.ColumnMeta;
import org.jachohx.litejdbc.db.utils.ColumnUtils;

public class FieldUtils {
	public static Object getDate(Field field) {
		Date now = new Date();
		if (ColumnUtils.classEquals(field.getType(), Long.class) || ColumnUtils.classEquals(field.getType(), long.class) ||
				ColumnUtils.classEquals(field.getType(), Integer.class) || ColumnUtils.classEquals(field.getType(), int.class)) {
			return now.getTime() / 1000;
		} else {
			return now;
		}
	}
	
	public static Object getDate(ColumnMeta columnMeta) {
		Date now = new Date();
		if (columnMeta.getJavaType()==Types.INTEGER || columnMeta.getJavaType()==Types.BIGINT) {
			return now.getTime() / 1000;
		} else {
			return now;
		}
	}
}
