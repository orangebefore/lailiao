/**
 * 
 */
package com.quark.tongji.bean;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.quark.app.bean.UserCount;
import com.quark.app.bean.UserIncreaseHourCount;
import com.quark.model.extend.TongjiRegist;
import com.quark.model.extend.User;
import com.quark.utils.DateUtils;

/**
 * @author kingsley
 *
 *         按日分析新增会员统计
 */
public class DaysIncreaseUsers extends ChartTable {

	public DaysIncreaseUsers(String from_date, String to_date) {
		super(from_date,to_date);
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
			List<String> dates = DateUtils.getDates(from_date, to_date);
			for (int i = 0; i < dates.size(); ++i) {
				if (!set_header) {
					header.getTd().add(dates.get(i));
				}
				if (i == dates.size() - 1) {
					set_header = true;
				}
				long count = 0;
				TongjiRegist regist_model = TongjiRegist.dao.findFirst(
						"select count(user_id) as count from tongji_regist where catalog=? and regist_date=?", catalog,
						dates.get(i));
				if (regist_model != null) {
					count = regist_model.getLong("count");
				}
				row_all = row_all + count;
				total = total + count;
				row.getTd().add("" + count);
				int size = tr_total.getTd().size();
				if (size <= i) {
					// tr_total为空时插入
					tr_total.getTd().add("" + count);
				} else {
					// tr_total已经有数据了
					String tr_total_count = tr_total.getTd().get(i);
					tr_total.getTd().set(i, ("" + (Long.valueOf(tr_total_count) + count)));
				}
			}
			// 统计总数
			row.setTail("" + row_all);
		}
		tr_total.setTail("" + total);
		this.setChart_title(from_date+"至"+to_date+" 对比数据");
		this.setChart_sub_title("总数：" + total);
		this.setTitle(" 新增加用户统计 - 共：" + total);
	}
}
