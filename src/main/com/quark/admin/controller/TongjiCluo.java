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
import com.quark.interceptor.CompanyLogin;
import com.quark.model.extend.Browse;
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
 * @author cluo
 *
 */
public class TongjiCluo extends Controller {

	public void dayForBrowse() {
		String user_id = getPara("user_id","全部");
		String start_day = getPara("start_day", DateUtils.getLastWeek());
		String end_day = getPara("end_day", DateUtils.getCurrentDate());
		String days = "";
		String data = "";
		String sql="";
		if(!user_id.equals("全部")){
			sql="  and user_id="+user_id;
		}
		List<Browse> browses = Browse.dao.find("select distinct (post_date) from browse where (post_date between '"
						+ start_day
						+ "' and '"
						+ end_day
						+ "')"+sql);
		for (int i = 0; i < browses.size(); ++i) {
			String post_time = browses.get(i).getStr("post_date");
			days += "'" + post_time + "'";
			Browse count_orders = Browse.dao
					.findFirst("select count(post_date) as total from browse where post_date like '"+ post_time+"'"+sql);
			if (count_orders != null) {
				data = data + count_orders.getLong("total");
			} else {
				data = data;
			}
			if (i < browses.size()) {
				days = days + ",";
				data = data + ",";
			}
		}
		// 用户
		List<User> users = User.dao .find("select * from user");
		for(User user:users){
			String nickname = user.getStr(user.nickname);
			if (nickname.equals("")) {
				 nickname = user.getStr(user.telephone);
			}
			user.put("nickname", nickname);
		}
		setAttr("user_id", user_id);
		setAttr("users", users);
		setAttr("start_day", start_day);
		setAttr("end_day", end_day);
		setAttr("days", days);
		setAttr("data", data);
		render("/admin/dayForBrowse.html");
	}
	
}
