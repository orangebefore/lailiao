package com.quark.tongji.bean;

import java.util.ArrayList;
import java.util.List;

public class TableHeader {

	
	private List<String> td = new ArrayList<String>();

	public TableHeader(String hear,String tail){
		this.setHead(hear);
		this.setTail(tail);
	}
	public List<String> getTd() {
		return td;
	}

	public void setTd(List<String> td) {
		this.td = td;
	}

	private String head = "";
	private String tail = "";

	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getTail() {
		return tail;
	}

	public void setTail(String tail) {
		this.tail = tail;
	}
	
}