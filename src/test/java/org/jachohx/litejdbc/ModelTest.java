package org.jachohx.litejdbc;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ModelTest {
	FileSystemXmlApplicationContext context;

	@Before
	public void setUp() throws Exception {
		context = new FileSystemXmlApplicationContext("classpath:spring_server.xml");
		context.isRunning();
	}
	
	@Test
	public void first() throws Exception{
		System.out.println("-------------first------------");
//		System.out.println(new User().first());
		System.out.println(new RarbgEntity().setOrderBy("id desc").first());
	}
	
	@Test
	public void all() throws Exception{
		System.out.println("-------------all------------");
		User user = new User();
		List<User> users = user.all();
		for(User u : users) {
			System.out.println(u);
		}
	}
	@Test
	public void pager() throws Exception{
		System.out.println("-------------pager------------");
		User user = new User();
		Pager<User> pager = user.setQuery("id>1").pager(1, 2);
		System.out.println("total: " + pager.getTotal());
		for(User u : pager.getPos()) {
			System.out.println(u);
		}
	}
	
	@Test
	public void save() throws Exception {
		System.out.println("-------------save------------");
		User user = new User();
		user.setAge(20);
		user.setBirth(new Date(492901200000l));
		user.setName("jacho huang");
		System.out.println(user.save());
	}
	
	@Test
	public void insert() throws Exception {
		System.out.println("-------------insert------------");
		User user = new User();
		user.setAge(20);
		user.setBirth(new Date(492901200000l));
		user.setName("jacho huang");
		System.out.println(user.insert());
	}
	
	@Test
	public void update() throws Exception {
		System.out.println("-------------update------------");
		User user = new User();
		user.setId(3);
		user.setAge(41);
		user.setBirth(new Date(492901200000l));
		user.setName("jacho huang");
		System.out.println(user.update());
	}
	
	@Test
	public void doUpdate() throws Exception {
		System.out.println("-------------update------------");
		User user = new User();
		user.addUpdateObject("age", 22);
		user.addUpdateObject("birthday", new Date(492901200000l));
		user.addUpdateObject("name", "jacho huang");
		user.addUpdateCondition("id", 3);
		System.out.println(user.doUpdate());
	}
	
}
