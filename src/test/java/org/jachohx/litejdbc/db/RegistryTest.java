package org.jachohx.litejdbc.db;

import org.jachohx.litejdbc.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class RegistryTest {
	FileSystemXmlApplicationContext context;

	@Before
	public void setUp() throws Exception {FileSystemXmlApplicationContext
		context = new FileSystemXmlApplicationContext("classpath:spring_server.xml");
		context.isRunning();
	}
	
	@Test
	public void init(){
		ModelRegistry.instance().init(User.class);
		System.out.println(ModelRegistry.instance().getTableMeta(User.class));
	}
}
