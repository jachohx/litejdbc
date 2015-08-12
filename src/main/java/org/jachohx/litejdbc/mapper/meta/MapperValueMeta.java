package org.jachohx.litejdbc.mapper.meta;

import java.io.Serializable;
import java.lang.reflect.Field;

public class MapperValueMeta implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
    private Field field;

    public MapperValueMeta(String name, Field field) {
        this.name = name;
        this.field = field;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Field getField() {
		return field;
	}

	public String toString() {
        return "[ name=" + name
                + ", field=" + (field != null ? "(" + field.getType() + ")" + field.getName() : null)
                + " ]";
    }
}
