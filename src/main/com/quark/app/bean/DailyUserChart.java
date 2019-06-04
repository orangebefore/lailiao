/**
 * 
 */
package com.quark.app.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.quark.model.extend.Charge;
import com.quark.model.extend.User;
import com.quark.utils.DateUtils;

/**
 * @author kingsley
 *
 */
public class DailyUserChart {

	private String date;
	private String babyyAxis = "";
	private String daddyyAxis = "";
	private String allyAxis = "";
	private List<UserIncreaseHourCount> userIncreaseHourCounts;
	private long all_baby_user;
	private long all_daddy_user;
	private long all_user;



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
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



	public List<UserIncreaseHourCount> getUserIncreaseHourCounts() {
		return userIncreaseHourCounts;
	}



	public void setUserIncreaseHourCounts(List<UserIncreaseHourCount> userIncreaseHourCounts) {
		this.userIncreaseHourCounts = userIncreaseHourCounts;
	}



	public long getAll_baby_user() {
		return all_baby_user;
	}



	public void setAll_baby_user(long all_baby_user) {
		this.all_baby_user = all_baby_user;
	}



	public long getAll_daddy_user() {
		return all_daddy_user;
	}



	public void setAll_daddy_user(long all_daddy_user) {
		this.all_daddy_user = all_daddy_user;
	}



	public long getAll_user() {
		return all_user;
	}



	public void setAll_user(long all_user) {
		this.all_user = all_user;
	}



	@Override
	public String toString() {
		return "DailyUserChart [date=" + date + ", babyyAxis=" + babyyAxis + ", daddyyAxis=" + daddyyAxis
				+ ", allyAxis=" + allyAxis + ", userIncreaseHourCounts=" + userIncreaseHourCounts + ", all_baby_user="
				+ all_baby_user + ", all_daddy_user=" + all_daddy_user + ", all_user=" + all_user + "]";
	}



	public DailyUserChart(String date) {
		userIncreaseHourCounts = new ArrayList<UserIncreaseHourCount>();
		all_baby_user = 0;
		all_daddy_user = 0;
		this.date = date;
		for (int i = 1; i < 25; i++) {
			String hour = "";
			if (i < 10) {
				hour = "0" + i;
			} else {
				hour = "" + i;
			}
			User baby_count_model = User.dao.findFirst(
					"select count(user_id) as count from user where sex=0 and regist_hour=?", date + " "
							+ hour);
			User daddy_count_model = User.dao.findFirst(
					"select count(user_id) as count from user where sex=1  and regist_hour=?", date + " "
							+ hour);
			UserIncreaseHourCount userIncreaseHourCount = new UserIncreaseHourCount();
			long baby_count = 0;
			long daddy_count = 0;
			if (baby_count_model != null) {
				baby_count = baby_count_model.getLong("count");
			}
			if (daddy_count_model != null) {
				daddy_count = daddy_count_model.getLong("count");
			}
			// 曲线
			babyyAxis = babyyAxis + ",'" + baby_count + "'";
			daddyyAxis = daddyyAxis + ",'" + daddy_count + "'";
			allyAxis = allyAxis + ",'" + (baby_count+daddy_count) + "'";
			userIncreaseHourCount.setAllCount(baby_count+daddy_count);
			userIncreaseHourCount.setBabyCount(baby_count);
			userIncreaseHourCount.setDaddyCount(daddy_count);
			userIncreaseHourCount.setHour(hour);
			userIncreaseHourCounts.add(userIncreaseHourCount);
			all_baby_user = all_baby_user + baby_count;
			all_daddy_user = all_daddy_user + daddy_count;
			all_user = all_user + all_baby_user + all_daddy_user;
		}
		babyyAxis = babyyAxis.replaceFirst(",", "");
		daddyyAxis = daddyyAxis.replaceFirst(",", "");
		allyAxis = allyAxis.replaceFirst(",", "");
	}
}
