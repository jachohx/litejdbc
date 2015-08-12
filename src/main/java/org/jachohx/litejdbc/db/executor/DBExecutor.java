package org.jachohx.litejdbc.db.executor;

import org.apache.commons.lang.StringUtils;
import org.jachohx.litejdbc.LogFilter;
import org.jachohx.litejdbc.Model;
import org.jachohx.litejdbc.db.ConnectionsAccess;
import org.jachohx.litejdbc.db.mapper.ModelRowMapper;
import org.jachohx.litejdbc.exception.DBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;


import java.sql.*;
import java.util.*;
@SuppressWarnings("unchecked")
public class DBExecutor {

    private final static Logger logger = LoggerFactory.getLogger(DBExecutor.class);
    
    JdbcTemplate jdbcTemplate = null;

    public DBExecutor(){
        this.jdbcTemplate = ConnectionsAccess.getJdbcTemplate();
    }
    
    public DBExecutor(String dbName){
        this.jdbcTemplate = ConnectionsAccess.getJdbcTemplate(dbName);
    }
    
    public long findLong(String sql, Object... params) {
    	long start = System.currentTimeMillis();
    	long result = 0;
    	if (params == null || params.length == 0) {
    		try {
    			result = jdbcTemplate.queryForLong(sql);
    		} catch (EmptyResultDataAccessException e) {
			}
    	}
    	else {
    		try {
    			result = jdbcTemplate.queryForLong(sql, params);
    		} catch (EmptyResultDataAccessException e) {
			}
    	}
    	LogFilter.logQuery(logger, sql, null, start);
    	return result;
    }
    
    public <T extends Model> T findObject(Class<? extends Model> clazz, String sql, Object... params) {
    	long start = System.currentTimeMillis();
    	T result = null;
    	if (params == null || params.length == 0) {
    		try {
    			result = (T)jdbcTemplate.queryForObject(sql, new ModelRowMapper(clazz));
    		} catch (EmptyResultDataAccessException e) {
			}
    	}
    	else {
    		try {
    			result = (T)jdbcTemplate.queryForObject(sql, params, new ModelRowMapper(clazz));
    		} catch (EmptyResultDataAccessException e) {
			}
    	}
    	LogFilter.logQuery(logger, sql, params, start);
    	return result;
    }
    
    public <T extends Model> List<T> findList(Class<? extends Model> clazz, String sql, Object... params) {
    	long start = System.currentTimeMillis();
    	List<T> results = null;
    	if (params == null || params.length == 0) {
    		try {
    			results = (List<T>)jdbcTemplate.query(sql, new ModelRowMapper(clazz));
    		} catch (EmptyResultDataAccessException e) {
			}
    	}
    	else {
    		try {
    			results = (List<T>)jdbcTemplate.query(sql, params, new ModelRowMapper(clazz));
    		} catch (EmptyResultDataAccessException e) {
			}
    	}
    	LogFilter.logQuery(logger, sql, params, start);
    	return results;
    }
    
    public List<Map<String, Object>> findListMap(String sql, Object... params) {
    	long start = System.currentTimeMillis();
    	List<Map<String, Object>> res = null;
		try {
			if (params != null && params.length > 0 )
				res = jdbcTemplate.queryForList(sql, params);
			else res = jdbcTemplate.queryForList(sql);
		} catch (EmptyResultDataAccessException e) {
		}
    	LogFilter.logQuery(logger, sql, params, start);
    	return res;
    }
    
    public long exec(String sql, Object ... params){
    	long start = System.currentTimeMillis();

        if(sql.trim().toLowerCase().startsWith("select")) 
        	throw new IllegalArgumentException("expected DML, but got select...");
        if(params != null && params.length != 0 && sql.indexOf('?') == -1) 
        	throw new IllegalArgumentException("query must be parametrized");

        int count = jdbcTemplate.update(sql, params);
        LogFilter.logQuery(logger, sql, params, start);
        return count;

    }

    public long insert(String sql, String autoIncrementColumnName, Object... params) {
        if (!sql.toLowerCase().contains("insert"))
            throw new IllegalArgumentException("this method is only for inserts");

        long start = System.currentTimeMillis();
        PreparedStatement ps;
        boolean autoInc = autoIncrementColumnName != null && StringUtils.isNotBlank(autoIncrementColumnName);
        try {
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            if (autoInc)
            	ps = connection.prepareStatement(sql, new String[]{autoIncrementColumnName});
            else {
            	ps = connection.prepareStatement(sql);
            }
            for (int index = 0; index < params.length; index++) {
                Object param = params[index];
                    ps.setObject(index + 1, param);
            }
            int status = ps.executeUpdate();

            ResultSet rs = null;
            try{
                if (autoInc) {
                	rs = ps.getGeneratedKeys();
                	if (rs.next()) {
                		long id = rs.getLong(1);
                		return id;
                	} else {
                		return -1;
                	}
                } else {
                	return status;
                }
            } catch (Exception e) {
                logger.error("Failed to find out the auto-incremented value, returning -1, query: {}", sql, e);
                return -1;
            } finally {
            	LogFilter.logQuery(logger, sql, params, start);
                try { if (rs != null) rs.close(); } catch (Exception e) {/*ignore*/}
                try { if (ps != null) ps.close(); } catch (Exception e) {/*ignore*/}
                try { if (connection != null) connection.close(); } catch (Exception e) {/*ignore*/}
            }
        } catch (Exception e) {
            throw new DBException(sql, params, e);
        }
    }
    
}
