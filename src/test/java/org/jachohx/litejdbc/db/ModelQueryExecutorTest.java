package org.jachohx.litejdbc.db;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jachohx.litejdbc.User;
import org.jachohx.litejdbc.db.executor.DBExecutor;
import org.jachohx.litejdbc.db.executor.ModelQueryExecutor;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ModelQueryExecutorTest {
	FileSystemXmlApplicationContext context;
	private ModelQueryExecutor excutor;
	private DBExecutor db;

	@Before
	public void setUp() throws Exception {
		context = new FileSystemXmlApplicationContext("classpath:spring_server.xml");
		context.isRunning();
		excutor = new ModelQueryExecutor(User.class);
		db = new DBExecutor("mysql");
	}
	@Test
	public void count(){
		System.out.println("all:" + excutor.count(null));
		Map<String, String> opts = new HashMap<String, String>();
		opts.put(ModelManager.QUERY_PARAM, "id=2");
		System.out.println("id=2:" + excutor.count(opts));
	}
	@Test
	public void first(){
		System.out.println(excutor.first(null));
	}
	
	@Test
	public void all(){
		List<User> list = excutor.all(null);
		for(User user : list) {
			System.out.println(user);
		}
	}
	
	@Test
	public void insert() {
		String sql = "insert into user(name,age,birthday) values(?,?,?)";
		Object[] params = new Object[]{"jacho", 20, new Date(492901200000l)};
		long id = db.insert(sql, "id", params);
//		long id = db.insert(sql, null, params);
		System.out.println("id:" + id);
	}
	
	@Test
	public void exec() {
		String sql = "update user set age = 21 where id = 1";
		long res = db.exec(sql);
		System.out.println("res:" + res);
	}
}
