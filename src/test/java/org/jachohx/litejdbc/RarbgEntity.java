package org.jachohx.litejdbc;

import org.jachohx.litejdbc.annotations.DbName;
import org.jachohx.litejdbc.annotations.IdGenerator;
import org.jachohx.litejdbc.annotations.Table;

@Table(value="movie_rarbg")
@IdGenerator(value="id")
@DbName(value="mysql")
public class RarbgEntity extends Model {
	private int id;
	private String name;
	private String imdb;
	private String url;
	private String img;
	private String title;
	private String size;
	private String magnet;
	private int createAt;
	private int updateAt;
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
	public String getImdb() {
		return imdb;
	}
	public void setImdb(String imdb) {
		this.imdb = imdb;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getMagnet() {
		return magnet;
	}
	public void setMagnet(String magnet) {
		this.magnet = magnet;
	}
	public int getCreateAt() {
		return createAt;
	}
	public void setCreateAt(int createAt) {
		this.createAt = createAt;
	}
	public int getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(int updateAt) {
		this.updateAt = updateAt;
	}
	@Override
	public String toString() {
		return "[" +
				"  id: " + id + 
				"  name: " + name + 
				", imbd: " + imdb +
				", size: " + size + 
				", url: " + url +
				", img: " + img +
				", title: " + title + 
				", magnet: " + magnet + 
				", createAt: " + createAt + 
				", updateAt: " + updateAt + 
				" ]";
	}
}
