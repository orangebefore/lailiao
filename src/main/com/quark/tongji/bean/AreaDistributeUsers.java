/**
 * 
 */
package com.quark.tongji.bean;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.quark.app.bean.AreaCount;
import com.quark.app.bean.UserIncreaseHourCount;
import com.quark.model.extend.TongjiRegist;
import com.quark.model.extend.User;

/**
 * @author kingsley
 *
 *         每日新增会员统计
 */
public class AreaDistributeUsers extends ChartTable {

	{
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
		render("/admin/地域分布统计.html");
	}
	public AreaDistributeUsers() {
		/**
		 * 表头
		 */
		TableHeader header = new TableHeader("地域", "合计(人)");
		this.getTh().add(header);
		/**
		 * 处理分类曲线
		 */
		List<TongjiRegist> regist = TongjiRegist.dao.find("select distinct(catalog) from tongji_regist");
		long total = 0;
		boolean set_header = false;
		for (TongjiRegist tongjiRegist : regist) {
			// 总数
			String catalog = tongjiRegist.getStr("catalog");
			header.getTd().add(catalog);
			TableRow row = new TableRow(catalog, "");
			long row_all = 0;
			this.getTr().add(row);
			// 计算曲线统计
				long count = 0;
				TongjiRegist regist_model = TongjiRegist.dao.findFirst(
						"select count(user_id) as count from tongji_regist where catalog=? and regist_hour=?", catalog,
						request_date + " " + hour);
				if (regist_model != null) {
					count = regist_model.getLong("count");
				}
				row_all = row_all + count;
				total = total + count;
				row.getTd().add("" + count);
				int size = tr_total.getTd().size();
				if(size < i){
					//tr_total为空时插入
					tr_total.getTd().add("" + count);
				}else{
					//tr_total已经有数据了
					String tr_total_count = tr_total.getTd().get(i-1);
					tr_total.getTd().set(i-1,(""+(Long.valueOf(tr_total_count)+count)));
				}
			//统计总数
			row.setTail(""+row_all);
		}
		tr_total.setTail(""+total);
		this.setChart_title(request_date);
		this.setChart_sub_title("总数：" + total);
		this.setTitle(request_date + " 新增加用户统计 - 共：" + total);
	}
}
