/**
 * 
 */
package com.quark.tongji.bean;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kingsley
 *
 */
public abstract class ChartTable {

	// 表格标题
	private String title="";
	private List<TableHeader> th = new ArrayList<TableHeader>();
	private List<TableRow> tr = new ArrayList<TableRow>();
	
	private String chart_title ="";
	private String chart_sub_title ="";
	
	public ChartTable(){}
	/**
	 * make some data...
	 * @param req
	 */
    public ChartTable(String req) {
	}
    /**
     * make some data...
     * @param req
     */
    public ChartTable(String req,String req2) {
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public List<TableHeader> getTh() {
		return th;
	}

	public void setTh(List<TableHeader> th) {
		this.th = th;
	}

	public List<TableRow> getTr() {
		return tr;
	}

	public void setTr(List<TableRow> tr) {
		this.tr = tr;
	}

	public String getChart_title() {
		return chart_title;
	}

	public void setChart_title(String chart_title) {
		this.chart_title = chart_title;
	}

	public String getChart_sub_title() {
		return chart_sub_title;
	}

	public void setChart_sub_title(String chart_sub_title) {
		this.chart_sub_title = chart_sub_title;
	}

	@Override
	public String toString() {
		return "ChartTable [title=" + title + ", th=" + th + ", tr=" + tr + ", chart_title=" + chart_title
				+ ", chart_sub_title=" + chart_sub_title + "]";
	}

}






