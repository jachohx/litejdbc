package org.jachohx.litejdbc.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;

public class ConnectionsAccess {
    
    private static JdbcTemplate jdbcTemplate;
    
    public static JdbcTemplate getJdbcTemplate() {
    	return jdbcTemplate;
	}
    
	public static JdbcTemplate getJdbcTemplate(String dbName) {
		return jdbcTemplate;
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		ConnectionsAccess.jdbcTemplate = jdbcTemplate;
	}
	
	public static Connection getConnection(String dbName) throws SQLException {
		return ConnectionsAccess.jdbcTemplate.getDataSource().getConnection();
	}
}
