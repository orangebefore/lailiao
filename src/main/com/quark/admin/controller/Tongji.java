/**
 * 
 */
package com.quark.admin.controller;

import java.math.BigDecimal;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jfinal.core.Controller;
import com.quark.app.bean.AreaCount;
import com.quark.app.bean.ChargeCount;
import com.quark.app.bean.ChargeHourChart;
import com.quark.app.bean.ChargeHourCount;
import com.quark.app.bean.ChargeMonthChart;
import com.quark.app.bean.ChargeMonthCount;
import com.quark.app.bean.DailyUserChart;
import com.quark.app.bean.UserCount;
import com.quark.model.extend.Charge;
import com.quark.model.extend.User;
import com.quark.model.extend.UserTag;
import com.quark.tongji.bean.DailyIncreaseUsers;
import com.quark.tongji.bean.DailyVipIncome;
import com.quark.tongji.bean.DaysIncreaseUsers;
import com.quark.tongji.bean.MonthVipIncome;
import com.quark.tongji.bean.YearVipIncome;
import com.quark.utils.DateUtils;
import com.quark.utils.QDateUtils;

/**
 * @author kingsley
 *
 */
public class Tongji extends Controller {

	public void index() {
		render("/admin/index.html");
	}

	/**
	 * 标签云统计
	 */
	public void tag() {
		List<UserTag> userTags = UserTag.dao
				.find("select type,tag,COUNT(tag) as count from user_tag where `status` =1 GROUP BY tag");
		setAttr("tags", userTags);
		String xAxis = "";
		String yAxis = "";
		for (UserTag userTag : userTags) {
			String tag = userTag.getStr("tag");
			long count = userTag.getLong("count");
			xAxis += ",'" + tag + "'";
			yAxis += ",'" + count + "'";
		}
		setAttr("xAxis", xAxis.replaceFirst(",", ""));
		setAttr("yAxis", yAxis.replaceFirst(",", ""));
		render("/admin/yun_tag.html");
	}

	/**
	 * 按日分析新增会员
	 */
	public void dailyAddUser() {
		String from_date = getPara("from_date", DateUtils.getCurrentDate());
		String to_date = getPara("to_date", DateUtils.getCurrentDate());
		setAttr("from_date", from_date);
		setAttr("to_date", to_date);
		DaysIncreaseUsers table = new DaysIncreaseUsers(from_date, to_date);
		setAttr("table", table);
		render("/admin/anri_fenxi_new_user.html");
	}

	/**
	 * 地域分布
	 */
	public void area() {
		List<User> sugarDaddy = User.dao
				.find("select distinct(province) as province,COUNT(user_id) as count from user where sex = 1 group by province");
		List<User> sugarBaby = User.dao
				.find("select distinct(province) as province,COUNT(user_id) as count from user where sex = 0 group by province");

		// 按市区统计
		List<User> sugarDaddy_city = User.dao.find("select distinct(city) as city,province from user group by city");
		List<AreaCount> areas = new ArrayList<AreaCount>();
		for (User user : sugarDaddy_city) {
			String province = user.getStr("province");
			String city = user.getStr("city");
			User sugarDaddyByArea = User.dao
					.findFirst("select COUNT(user_id) as count from user where sex = 1 and province='" + province
							+ "' and city='" + city + "'");
			User sugarBabyByAreaa = User.dao
					.findFirst("select COUNT(user_id) as count from user where sex = 0 and province='" + province
							+ "' and city='" + city + "'");
			long sugarDaddyByAreaCount = (sugarBabyByAreaa == null ? 0 : sugarDaddyByArea.getLong("count"));
			long sugarBabyByAreaCount = (sugarBabyByAreaa == null ? 0 : sugarBabyByAreaa.getLong("count"));

			AreaCount area = new AreaCount();
			area.setArea(province + "-" + city);
			area.setSugarBabyCount(sugarBabyByAreaCount);
			area.setSugarDaddyCount(sugarDaddyByAreaCount);
			area.setAll(sugarDaddyByAreaCount + sugarBabyByAreaCount);
			areas.add(area);
		}

		setAttr("sugarDaddy", sugarDaddy);
		setAttr("sugarBaby", sugarBaby);
		setAttr("areas", areas);
		render("/admin/diyufenbutongji.html");
	}

	/**
	 * 设备分布
	 */
	public void device() {
		render("/admin/shebeifenbutongji.html");
	}

	/**
	 * 每月vip营收
	 */
	public void monthIncome() {
		String month = getPara("month", DateUtils.getCurrentMonth());
		setAttr("request_month", month);

		MonthVipIncome table = new MonthVipIncome(month);
		setAttr("table", table);
		MonthVipIncome table1 = new MonthVipIncome(QDateUtils.getMonthBeforeFromCurrentMonth(month, -1));
		setAttr("table1", table1);
		MonthVipIncome table2 = new MonthVipIncome(QDateUtils.getMonthBeforeFromCurrentMonth(month, -2));
		setAttr("table2", table2);
		MonthVipIncome table3 = new MonthVipIncome(QDateUtils.getMonthBeforeFromCurrentMonth(month, -3));
		setAttr("table3", table3);
		MonthVipIncome table4 = new MonthVipIncome(QDateUtils.getMonthBeforeFromCurrentMonth(month, -4));
		setAttr("table4", table4);
		render("/admin/monthVipyingshoutongji.html");
	}

	/**
	 * 每日 vip 营收统计
	 */
	public void dayIncome() {
		String currentdate = getPara("date", DateUtils.getCurrentDate());
		setAttr("request_date", currentdate);

		DailyVipIncome table = new DailyVipIncome(currentdate);
		setAttr("table", table);
		DailyVipIncome table1 = new DailyVipIncome(QDateUtils.getDateBeforeFromDate(currentdate, -1));
		setAttr("table1", table1);
		DailyVipIncome table2 = new DailyVipIncome(QDateUtils.getDateBeforeFromDate(currentdate, -2));
		setAttr("table2", table2);
		DailyVipIncome table3 = new DailyVipIncome(QDateUtils.getDateBeforeFromDate(currentdate, -3));
		setAttr("table3", table3);
		DailyVipIncome table4 = new DailyVipIncome(QDateUtils.getDateBeforeFromDate(currentdate, -4));
		setAttr("table4", table4);

		render("/admin/dayVipyingshoutongji.html");
	}

	/**
	 * 每日 新增加 会员统计
	 */
	public void dayAddUser() {

		String currentdate = getPara("date", DateUtils.getCurrentDate());
		setAttr("request_date", currentdate);

		DailyIncreaseUsers table = new DailyIncreaseUsers(currentdate);
		setAttr("table", table);
		DailyIncreaseUsers table1 = new DailyIncreaseUsers(QDateUtils.getDateBeforeFromDate(currentdate, -1));
		setAttr("table1", table1);
		DailyIncreaseUsers table2 = new DailyIncreaseUsers(QDateUtils.getDateBeforeFromDate(currentdate, -2));
		setAttr("table2", table2);
		DailyIncreaseUsers table3 = new DailyIncreaseUsers(QDateUtils.getDateBeforeFromDate(currentdate, -3));
		setAttr("table3", table3);
		DailyIncreaseUsers table4 = new DailyIncreaseUsers(QDateUtils.getDateBeforeFromDate(currentdate, -4));
		setAttr("table4", table4);
		render("/admin/dayNewUsertongji.html");
	}

	/**
	 * 每年VIP营收统计
	 */
	public void yearIncome() {
		String year = getPara("year", DateUtils.getCurrentYear());
		setAttr("year", year);
		YearVipIncome table = new YearVipIncome(year);
		setAttr("table", table);
		List<MonthVipIncome> months = new ArrayList<MonthVipIncome>();
		for (int i = 1; i < 13; ++i) {
			String month = "";
			if (i < 10) {
				month = "0" + i;
			}else{
				month = ""+i;
			}
			MonthVipIncome table1 = new MonthVipIncome(year + "-" + month);
			months.add(table1);
		}
		setAttr("months", months);
		render("/admin/yearVipTongji.html");
	}
}
