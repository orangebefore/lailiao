/**
 * 
 */
package com.quark.app.bean;

/**
 * 按日分析新增会员对比
 * @author kingsley
 *
 */
public class UserCount {

	private String date;
	private long sugarBabyCount;
	private long sugarDaddyCount;
	private long all;
	
	public long getAll() {
		return all;
	}
	public void setAll(long all) {
		this.all = all;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public long getSugarBabyCount() {
		return sugarBabyCount;
	}
	public void setSugarBabyCount(long sugarBabyCount) {
		this.sugarBabyCount = sugarBabyCount;
	}
	public long getSugarDaddyCount() {
		return sugarDaddyCount;
	}
	public void setSugarDaddyCount(long sugarDaddyCount) {
		this.sugarDaddyCount = sugarDaddyCount;
	}
}
