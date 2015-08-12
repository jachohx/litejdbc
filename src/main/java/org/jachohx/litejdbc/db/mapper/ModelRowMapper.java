package org.jachohx.litejdbc.db.mapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.jachohx.litejdbc.Model;
import org.jachohx.litejdbc.db.ModelRegistry;
import org.jachohx.litejdbc.db.meta.ColumnMeta;
import org.jachohx.litejdbc.db.meta.ResultSetMeta;
import org.jachohx.litejdbc.db.meta.TableMeta;
import org.jachohx.litejdbc.db.utils.ColumnUtils;
import org.springframework.jdbc.core.RowMapper;

public class ModelRowMapper implements RowMapper<Model>{
	
	private ResultSetMeta[] metaDataInfos;
	
	Class<? extends Model> clazz;
	
	public ModelRowMapper() {
	}
	
	public ModelRowMapper(Class<? extends Model> clazz) {
		this.clazz = clazz;
	}
	
	private ResultSetMeta[] getResultSetMetas(ResultSet rs) throws SQLException{
		if (metaDataInfos == null) {
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			ResultSetMeta[] infos = new ResultSetMeta[columnCount];
	        for (int i = 1; i <= columnCount; i++) {
	        	infos[i - 1] = ColumnUtils.getResultSetMeta(metaData, i);
	        }
	        this.metaDataInfos = infos;
		}
		return metaDataInfos;
	}
	
	
	
	@Override
	public Model mapRow(ResultSet rs, int rowNum) throws SQLException {
		Model model = null;
		try {
			model = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if (clazz == null) 
			return null;
		Map<String, Object> row = new HashMap<String, Object>();
		TableMeta tableMeta = ModelRegistry.instance().init(clazz).getTableMeta(clazz);
		ResultSetMeta[] metaDataInfos = getResultSetMetas(rs);
        for (ResultSetMeta meta : metaDataInfos) {
        	String columnName = meta.getColumnName();
        	ColumnMeta columnMeta = tableMeta.getColumnMeta(columnName.toLowerCase());
        	int typeNum = 0;
        	Field field = null;
        	String paramName = null;
        	Object columnObject = ColumnUtils.getObject(field, typeNum, rs, columnName);
        	if (columnMeta == null || columnMeta.getField() == null) {
        		typeNum = meta.getType();
        		paramName = columnName;
        	} else  {
        		field = columnMeta.getField();
        		paramName = field.getName();
        		if (columnObject != null) {
	        		field.setAccessible(true);
	        		try {
	        			field.set(model, columnObject);
	        		} catch (IllegalArgumentException e) {
	        			e.printStackTrace();
	        		} catch (IllegalAccessException e) {
	        			e.printStackTrace();
	        		} finally {
	        			field.setAccessible(false);
	        		}
        		}
        	}
        	row.put(paramName, columnObject);
        }
        model.setValues(row);
		return model;
	}
}
