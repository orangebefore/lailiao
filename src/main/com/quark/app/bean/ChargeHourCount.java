/**
 * 
 */
package com.quark.app.bean;

import java.math.BigDecimal;

/**
 * @author kingsley
 *
 */
public class ChargeHourCount {

	private String hour;
	private BigDecimal babyCharge;
	private BigDecimal daddyCharge;
	private BigDecimal allCharge;
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
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
