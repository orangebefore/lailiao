/**
 * 
 */
package com.quark.tongji.bean;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.quark.app.bean.UserIncreaseHourCount;
import com.quark.model.extend.TongjiRegist;
import com.quark.model.extend.User;

/**
 * @author kingsley
 *
 *         每日新增会员统计
 */
public class DailyIncreaseUsers extends ChartTable {

	public DailyIncreaseUsers(String req) {
		super(req);
		String request_date = req;
		/**
		 * 表头
		 */
		TableHeader header = new TableHeader("时间", "合计(人)");
		this.getTh().add(header);
		/**
		 * 处理分类曲线
		 */
		List<TongjiRegist> regist = TongjiRegist.dao.find("select distinct(catalog) from tongji_regist");
		TableRow tr_total = new TableRow("总数", "-");
		this.getTr().add(tr_total);
		long total = 0;
		boolean set_header = false;
		for (TongjiRegist tongjiRegist : regist) {
			// 总数
			String catalog = tongjiRegist.getStr("catalog");
			TableRow row = new TableRow(catalog, "");
			long row_all = 0;
			this.getTr().add(row);
			// 计算曲线统计
			for (int i = 1; i < 25; i++) {
				String hour = "";
				if (i < 10) {
					hour = "0" + i;
				} else {
					hour = "" + i;
				}
				if (!set_header) {
					header.getTd().add(hour);
				}
				if(i==24){
					set_header = true;
				}
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
