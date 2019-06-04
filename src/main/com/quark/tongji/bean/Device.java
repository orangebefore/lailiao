/**
 * 
 */
package com.quark.tongji.bean;

import javax.servlet.http.HttpServletRequest;

import com.quark.app.bean.UserIncreaseHourCount;
import com.quark.model.extend.User;

/**
 * @author kingsley
 *
 * 设备分布统计
 */
public class Device extends ChartTable {

	public Device(String req) {
		super(req);
		String request_date = req;
		TableRow tr_total = new TableRow("设备","");
		long all = 0;
		
		for (int i = 1; i < 25; i++) {
			String hour = "";
			if (i < 10) {
				hour = "0" + i;
			} else {
				hour = "" + i;
			}
			header.getTd().add(hour);
			User baby_count_model = User.dao
					.findFirst("select count(user_id) as count from user where sex=0 and regist_hour=?", request_date
							+ " " + hour);
			User daddy_count_model = User.dao.findFirst(
					"select count(user_id) as count from user where sex=1  and regist_hour=?", request_date + " "
							+ hour);
			long baby_count = 0;
			long daddy_count = 0;
			if (baby_count_model != null) {
				baby_count = baby_count_model.getLong("count");
			}
			if (daddy_count_model != null) {
				daddy_count = daddy_count_model.getLong("count");
			}
			all_baby_count = all_baby_count + baby_count;
			all_dady_count = all_dady_count + daddy_count;
			all = all + baby_count + daddy_count;
			tr_total.getTd().add("" + (daddy_count+baby_count));
			tr_daddy.getTd().add("" + daddy_count);
			tr_baby.getTd().add("" + baby_count);
		}
		//统计总数
		tr_total.getTd().add("" + all);
		tr_daddy.getTd().add("" + all_dady_count);
		tr_baby.getTd().add("" + all_baby_count);
		this.getTh().add(header);
		this.getTr().add(tr_total);
		this.getTr().add(tr_daddy);
		this.getTr().add(tr_baby);
		this.setChart_title(request_date);
		this.setChart_sub_title("总数：" + all);
		this.setTitle(request_date + " 新增加用户统计 - 共："+all);
	}
}
