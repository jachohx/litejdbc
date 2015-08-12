package org.jachohx.litejdbc.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD, ElementType.METHOD})
public @interface Mapper {
	static String DEFAULT_VALUE = "";
	static boolean DEFAULT_DISPLAY = true;
	static boolean DISPLAY_VALUE = true;
	public String from() default DEFAULT_VALUE;
	public String to() default DEFAULT_VALUE;
	public boolean isDisplay() default DEFAULT_DISPLAY;
}