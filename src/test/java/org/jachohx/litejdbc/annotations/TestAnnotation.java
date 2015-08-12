package org.jachohx.litejdbc.annotations;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class TestAnnotation {
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class<TestColumn> clazz = TestColumn.class;
		System.out.println(clazz.getAnnotation(Table.class));
		Method[] methods = clazz.getMethods();
		TestColumn t1 = new TestColumn();
		int idx = 1;
		for (Method method : methods) {
			if (method.isAnnotationPresent(Column.class)) {
				System.out.println(method.getAnnotation(Column.class));
				Class<?>[] types = method.getParameterTypes();
				for (Class<?> type: types){
					System.out.println(type);
				}
				method.getName();
				method.invoke(t1, idx++);
			}
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class)) {
				field.setAccessible(true);
				System.out.println(field.getAnnotation(Column.class).column());
				field.set(t1, idx++);
			}
			System.out.println("value:" + field.get(t1));
			System.out.println("type:" + field.getType());
			System.out.println("name:" + field.getName());
		}
		System.out.println(t1);
	}
}


@Table(value="1")
class TestColumn{
	@Column(column="1")
	private Integer t1;
	@Column(column="1")
	private Integer t2;
	@Column(column="1")
	private Integer t3;
	public void setMethod1( int i){
		t1 = i;
	}
	@Column(column="2")
	public void setMethod2( int i){
		t2 = i;
	}
	public String toString() {
		return "t1:" + t1 + ", t2:" + t2 + ", t3:" + t3;
	}
}