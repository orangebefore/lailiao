package com.quark.tongji.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TableRow {

	
	private List<String> td = new ArrayList<String>();

	public List<String> getTd() {
		return td;
	}

	public TableRow(String head,String tail){
		this.setHead(head);
		this.setTail(tail);
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