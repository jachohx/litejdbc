package org.jachohx.litejdbc.db;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jachohx.litejdbc.db.meta.ColumnMeta;
import org.jachohx.litejdbc.annotations.Column;
import org.jachohx.litejdbc.annotations.DbName;
import org.jachohx.litejdbc.annotations.IdGenerator;
import org.jachohx.litejdbc.annotations.ManageDate;
import org.jachohx.litejdbc.annotations.PrimaryKey;
import org.jachohx.litejdbc.annotations.Table;
import org.jachohx.litejdbc.LogFilter;
import org.jachohx.litejdbc.Model;
import org.jachohx.litejdbc.db.meta.TableMeta;
import org.jachohx.litejdbc.db.utils.ColumnUtils;
import org.jachohx.litejdbc.exception.DBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelRegistry {
	private static ModelRegistry INSTANCE = new ModelRegistry();
	private final static String TABLE_DATABASE_NAME = "default"; 
	private final static String TABLE_NAME = "";
	private final static String FIELD_MAP_COLUMN_KEY = "column";
	private final static String FIELD_MAP_PRIMARY_KEY = "primarykey";
	
	private ModelRegistry() {
	}
	
	private final static Logger logger = LoggerFactory.getLogger(ModelRegistry.class);
	Map<Class<? extends Model>, TableMeta> tables = new HashMap<Class<? extends Model>, TableMeta>();
	public static ModelRegistry instance() {
        return INSTANCE;
    }
	
	public synchronized ModelRegistry init(Class<? extends Model> clazz) {
		if (tables.containsKey(clazz)) 
			return this;
		try {
			TableMeta meta = initTableMeta(clazz);
			String dbName = meta.getDbName();
			String tableName = meta.getTableName();
			Connection c = ConnectionsAccess.getConnection(dbName);
			if(c == null){
				throw new DBException("Failed to retrieve metadata from DB, connection: '" + dbName + "' is not available");
			}
			DatabaseMetaData databaseMetaData = c.getMetaData();
			String databaseProductName = c.getMetaData().getDatabaseProductName();
			Map<String, Map<String, Field>> paramMap = getFields(clazz);
			Map<String, Field> fieldMap = paramMap.get(FIELD_MAP_COLUMN_KEY);
			Map<String, ColumnMeta> metaParams = fetchMetaParams(databaseMetaData, databaseProductName, tableName, fieldMap);
			meta.addColumnMetas(metaParams);
			
			//primarykey，如果没有加PrimaryKey，则默认使用idGenerator
			List<String> primaryKeys = new ArrayList<String>(paramMap.get(FIELD_MAP_PRIMARY_KEY).keySet());
			if (primaryKeys == null || primaryKeys.size() == 0)primaryKeys.add(meta.getIdGenerator());
			meta.addPrimaryKeys(primaryKeys);
			
			ColumnMeta columnMeta = null;
			//设置createat信息
			if ((columnMeta = metaParams.get(ColumnUtils.COLUMN_CREATE_AT_KEY)) != null || 
					(columnMeta = metaParams.get(ColumnUtils.COLUMN_CREATEAT_KEY)) != null) 
				meta.setCreateAtColumnMeta(columnMeta);
			//设置updateat信息
			if ((columnMeta = metaParams.get(ColumnUtils.COLUMN_UPDATE_AT_KEY)) != null || 
					(columnMeta = metaParams.get(ColumnUtils.COLUMN_UPDATEAT_KEY)) != null) 
				meta.setUpdateAtColumnMeta(columnMeta);
			tables.put(clazz, meta);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public TableMeta getTableMeta(Class<? extends Model> clazz) {
		return tables.get(clazz);
	}
	
	private TableMeta initTableMeta(Class<? extends Model> clazz) {
		String dbName = findDbName(clazz);
		String idGenerator = findIdGenerator(clazz);
		String tableName = findTableName(clazz);
		boolean isManageDate = findManagerDate(clazz);
		TableMeta tableMeta = new TableMeta(dbName, tableName, idGenerator);
		tableMeta.setManageDate(isManageDate);
		return tableMeta;
    }
	
	private String findIdGenerator(Class<? extends Model> clazz) {
        IdGenerator idGeneratorAnnotation = clazz.getAnnotation(IdGenerator.class);
        return idGeneratorAnnotation == null ? null : idGeneratorAnnotation.value();
    }
	private String findTableName(Class<? extends Model> clazz) {
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        return tableAnnotation == null ? TABLE_NAME : tableAnnotation.value();
    }
	private boolean findManagerDate(Class<? extends Model> clazz) {
		ManageDate manageDateAnnotation = clazz.getAnnotation(ManageDate.class);
		return manageDateAnnotation != null && "false".equals(manageDateAnnotation.value()) ? false : true;
	}
	protected String findDbName(Class<? extends Model> clazz) {
        DbName dbNameAnnotation = clazz.getAnnotation(DbName.class);
        return dbNameAnnotation == null ? TABLE_DATABASE_NAME : dbNameAnnotation.value();
    }
	
	/**
	 * 分析表的结构，与类的字段做匹配
	 * @param databaseMetaData
	 * @param databaseProductName
	 * @param table
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	private Map<String, ColumnMeta> fetchMetaParams(DatabaseMetaData databaseMetaData, String databaseProductName, 
			String table, Map<String, Field> fields) throws SQLException {
        String[] vals = table.split("\\.");
        String schema = null;
        String tableName;

        if(vals.length == 1) {
            tableName = vals[0];
        } else if (vals.length == 2) {
            schema = vals[0];
            tableName = vals[1];
            if (schema.length() == 0 || tableName.length() == 0) {
                throw new DBException("invalid table name : " + table);
            }
        } else {
            throw new DBException("invalid table name: " + table);
        }

        ResultSet rs = databaseMetaData.getColumns(null, schema, tableName, null);
        String dbProduct = databaseMetaData.getDatabaseProductName().toLowerCase();
        Map<String, ColumnMeta> columns = getColumns(rs, dbProduct, fields);
        rs.close();

        //try upper case table name - Oracle uses upper case
        if (columns.size() == 0) {
            rs = databaseMetaData.getColumns(null, schema, tableName.toUpperCase(), null);
            dbProduct = databaseProductName.toLowerCase();
            columns = getColumns(rs, dbProduct, fields);
            rs.close();
        }

        //if upper case not found, try lower case.
        if(columns.size() == 0){
            rs = databaseMetaData.getColumns(null, schema, tableName.toLowerCase(), null);
            columns = getColumns(rs, dbProduct, fields);
            rs.close();
        }

        if(columns.size() > 0){
            LogFilter.log(logger, "Fetched metadata for table: {}", table);
        }
        else{
            logger.warn("Failed to retrieve metadata for table: '{}'."
                    + " Are you sure this table exists? For some databases table names are case sensitive.",
                    table);
        }
        return columns;
    }
	
	/**
	 * 得到每个column对应的field
	 * @param rs
	 * @param dbProduct
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	private Map<String, ColumnMeta> getColumns(ResultSet rs, String dbProduct, Map<String, Field> fields) throws SQLException {
        Map<String, ColumnMeta> columns = new HashMap<String, ColumnMeta>();
        while (rs.next()) {
        	if (dbProduct.equals("h2") && "INFORMATION_SCHEMA".equals(rs.getString("TABLE_SCHEMA"))) continue;
        	String columnName = rs.getString("COLUMN_NAME").toLowerCase();
        	Field field = fields.get(columnName.toLowerCase());
        	ColumnMeta cm = new ColumnMeta(columnName, rs.getString("TYPE_NAME").toLowerCase(), rs.getInt("COLUMN_SIZE"), rs.getInt("DATA_TYPE"), field);
        	columns.put(cm.getColumnName(), cm);
        }
       return columns;
	}
	
	/**
	 * 得到类的field信息
	 * @param clazz
	 * @return
	 */
	private Map<String, Map<String, Field>> getFields(Class<? extends Model> clazz) {
		Map<String, Field> fieldMap = new HashMap<String, Field>();
		Map<String, Field> primaryKeyMap = new HashMap<String, Field>();
		Map<String, Field> JsonKeyMap = new HashMap<String, Field>();
		Map<String, Map<String, Field>> result = new HashMap<String, Map<String,Field>>();
		result.put(FIELD_MAP_COLUMN_KEY, fieldMap);
		result.put(FIELD_MAP_PRIMARY_KEY, primaryKeyMap);
		Field[] fields = clazz.getDeclaredFields();
		if (fields.length == 0) return result;
		for (Field field : fields) {
			String columnName = field.getName();
			String jsonKey = field.getName();
			if (field.isAnnotationPresent(Column.class)) {
				columnName = field.getAnnotation(Column.class).column();
			}
			if (field.isAnnotationPresent(PrimaryKey.class)) {
				primaryKeyMap.put(columnName.toLowerCase(), field);
			}
			fieldMap.put(columnName.toLowerCase(), field);
			JsonKeyMap.put(jsonKey, field);
		}
		return result;
	}
	
}