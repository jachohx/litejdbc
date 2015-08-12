package org.jachohx.litejdbc;

import java.util.Date;

import org.jachohx.litejdbc.annotations.Column;
import org.jachohx.litejdbc.annotations.DbName;
import org.jachohx.litejdbc.annotations.IdGenerator;
import org.jachohx.litejdbc.annotations.Mapper;
import org.jachohx.litejdbc.annotations.ManageDate;
import org.jachohx.litejdbc.annotations.ManageMapper;
import org.jachohx.litejdbc.annotations.PrimaryKey;
import org.jachohx.litejdbc.annotations.Table;

@Table(value="user")
@IdGenerator(value="id")
@DbName(value="mysql")
@ManageDate(value="true")
@ManageMapper(value="true")
public class UserJson extends JsonModel{
	@PrimaryKey
	int id;
	String name;
	int age;
	@Column(column="birthday")
	@Mapper(from="birthday")
	Date birth;
	@Mapper(isDisplay=false)
	int updateAt;
	@Mapper(isDisplay=false)
	Date createAt;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	
}
