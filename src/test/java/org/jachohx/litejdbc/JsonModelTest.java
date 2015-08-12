package org.jachohx.litejdbc;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class JsonModelTest {
	FileSystemXmlApplicationContext context;

	@Before
	public void setUp() throws Exception {
		context = new FileSystemXmlApplicationContext("classpath:spring_server.xml");
		context.isRunning();
	}
	
	@Test
	public void fromJson () throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", "1");
		map.put("name", "jacho");
		map.put("age", "21");
		JSONObject object = new JSONObject(map);
		UserJson user = new UserJson().fromJson(object);
		System.out.println(user);
	}
}
