package org.jachohx.litejdbc;

import java.util.List;

public class Pager<T> {
	private long total;
	private List<T> pos;
	
	public Pager(long total, List<T> entitys){
		this.total = total;
		this.pos = entitys;
	}

	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<T> getPos() {
		return pos;
	}
	public void setPos(List<T> pos) {
		this.pos = pos;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("total: ").append(total).append("\r\n");
		sb.append("pos: ").append("\r\n");
		if (pos != null && pos.size() > 0) {
			for (T t : pos) 
				sb.append(t).append("\r\n");
		}
		return sb.toString();
	}
	
}
