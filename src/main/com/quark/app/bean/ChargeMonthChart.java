/**
 * 
 */
package com.quark.app.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.quark.model.extend.Charge;
import com.quark.utils.DateUtils;

/**
 * @author kingsley
 *
 */
public class ChargeMonthChart {

	private String month;
	private String babyyAxis = "";
	private String daddyyAxis = "";
	private String allyAxis = "";
	private String daysxAxis = "";
	private List<ChargeMonthCount> chargeMonthCounts;
	private BigDecimal all_baby_charge_money;
	private BigDecimal all_daddy_charge_money;
	private BigDecimal all_charge_money;


	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDaysxAxis() {
		return daysxAxis;
	}

	public void setDaysxAxis(String daysxAxis) {
		this.daysxAxis = daysxAxis;
	}

	public BigDecimal getAll_charge_money() {
		return all_charge_money;
	}

	public void setAll_charge_money(BigDecimal all_charge_money) {
		this.all_charge_money = all_charge_money;
	}

	public BigDecimal getAll_baby_charge_money() {
		return all_baby_charge_money;
	}

	public void setAll_baby_charge_money(BigDecimal all_baby_charge_money) {
		this.all_baby_charge_money = all_baby_charge_money;
	}

	public BigDecimal getAll_daddy_charge_money() {
		return all_daddy_charge_money;
	}

	public void setAll_daddy_charge_money(BigDecimal all_daddy_charge_money) {
		this.all_daddy_charge_money = all_daddy_charge_money;
	}


	public List<ChargeMonthCount> getChargeMonthCounts() {
		return chargeMonthCounts;
	}

	public void setChargeMonthCounts(List<ChargeMonthCount> chargeMonthCounts) {
		this.chargeMonthCounts = chargeMonthCounts;
	}


	public String getBabyyAxis() {
		return babyyAxis;
	}

	public void setBabyyAxis(String babyyAxis) {
		this.babyyAxis = babyyAxis;
	}

	public String getDaddyyAxis() {
		return daddyyAxis;
	}

	public void setDaddyyAxis(String daddyyAxis) {
		this.daddyyAxis = daddyyAxis;
	}

	public String getAllyAxis() {
		return allyAxis;
	}

	public void setAllyAxis(String allyAxis) {
		this.allyAxis = allyAxis;
	}

	public ChargeMonthChart(String month) {
		chargeMonthCounts = new ArrayList<ChargeMonthCount>();
		all_baby_charge_money = new BigDecimal(0);
		all_daddy_charge_money = new BigDecimal(0);
		int days = DateUtils.getDaysOfMonth(month);
		this.month = month;
		for (int i = 1; i < days+1; i++) {
			String day = "";
			if (i < 10) {
				day = "0" + i;
			} else {
				day = "" + i;
			}
			daysxAxis = daysxAxis + ",'"+day+"'";
			Charge baby_charge = Charge.dao.findFirst(
					"select sum(money) as money from charge where is_pay=1 and type=0 and charge_date=?", month + "-"
							+ day);
			Charge daddy_charge = Charge.dao.findFirst(
					"select sum(money) as money from charge where is_pay=1 and type=1 and charge_date=?", month + "-"
							+ day);
			ChargeMonthCount chargeMonthCount = new ChargeMonthCount();
			BigDecimal baby_charge_money = new BigDecimal(0);
			BigDecimal daddy_charge_money = new BigDecimal(0);
			if (baby_charge != null) {
				baby_charge_money = baby_charge.getBigDecimal("money");
				if (baby_charge_money == null) {
					baby_charge_money = new BigDecimal(0);
				}
			}
			if (daddy_charge != null) {
				daddy_charge_money = daddy_charge.getBigDecimal("money");
				if (daddy_charge_money == null) {
					daddy_charge_money = new BigDecimal(0);
				}
			}
			// 曲线
			babyyAxis = babyyAxis + ",'" + baby_charge_money.toString() + "'";
			daddyyAxis = daddyyAxis + ",'" + daddy_charge_money.toString() + "'";
			allyAxis = allyAxis + ",'" + baby_charge_money.add(daddy_charge_money).toString() + "'";
			chargeMonthCount.setAllCharge(baby_charge_money.add(daddy_charge_money).toString());
			chargeMonthCount.setBabyCharge(baby_charge_money.toString());
			chargeMonthCount.setDaddyCharge(daddy_charge_money.toString());
			chargeMonthCount.setDay(day);
			chargeMonthCounts.add(chargeMonthCount);
			all_baby_charge_money = all_baby_charge_money.add(baby_charge_money);
			all_daddy_charge_money = all_daddy_charge_money.add(daddy_charge_money);
			all_charge_money = all_baby_charge_money.add(all_daddy_charge_money);
		}
		babyyAxis = babyyAxis.replaceFirst(",", "");
		daddyyAxis = daddyyAxis.replaceFirst(",", "");
		allyAxis = allyAxis.replaceFirst(",", "");
		daysxAxis = daysxAxis.replaceFirst(",", "");
	}
}
