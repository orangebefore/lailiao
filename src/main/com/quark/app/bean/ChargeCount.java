/**
 * 
 */
package com.quark.app.bean;

import java.math.BigDecimal;

/**
 * @author kingsley
 *
 */
public class ChargeCount {

	private String year;
	private BigDecimal babyCharge;
	//
	private BigDecimal daddyCharge;
	private BigDecimal allCharge;
	public String getYear() {
		return year;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	public BigDecimal getBabyCharge() {
		return babyCharge;
	}
	public void setBabyCharge(BigDecimal babyCharge) {
		this.babyCharge = babyCharge;
	}
	public BigDecimal getDaddyCharge() {
		return daddyCharge;
	}
	public void setDaddyCharge(BigDecimal daddyCharge) {
		this.daddyCharge = daddyCharge;
	}
	public BigDecimal getAllCharge() {
		return allCharge;
	}
	public void setAllCharge(BigDecimal allCharge) {
		this.allCharge = allCharge;
	}
	
}
