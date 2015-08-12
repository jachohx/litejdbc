package org.jachohx.litejdbc;

import java.sql.SQLException;

import org.jachohx.litejdbc.db.ConnectionsAccess;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class ConnectionsAccessTest {
	FileSystemXmlApplicationContext context;
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setUp() throws Exception {FileSystemXmlApplicationContext
		context = new FileSystemXmlApplicationContext("src/test/resources/spring_server.xml");
		context.isRunning();
		jdbcTemplate = ConnectionsAccess.getJdbcTemplate(null);
	}
	@Test
	public void count() throws SQLException{
		System.out.println(jdbcTemplate);
		System.out.println(jdbcTemplate.getDataSource());
		System.out.println(jdbcTemplate.getDataSource().getLoginTimeout());
		System.out.println(jdbcTemplate.getDataSource().getConnection());
		System.out.println(jdbcTemplate.queryForInt("select count(*) from user"));
	}
}
