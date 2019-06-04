/**
 * 
 */
package com.quark.tongji.bean;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.quark.app.bean.ChargeHourCount;
import com.quark.app.bean.UserIncreaseHourCount;
import com.quark.model.extend.Charge;
import com.quark.model.extend.TongjiCharge;
import com.quark.model.extend.User;
import com.quark.utils.DateUtils;

/**
 * @author kingsley
 * 
 * 每日vip营收统计
 *
 */
public class YearVipIncome extends ChartTable {

	public YearVipIncome(String req) {
		super(req);
		String year = req;
		/**
		 * 表头
		 */
		TableHeader header = new TableHeader("时间", "合计(人)");
		this.getTh().add(header);
		/**
		 * 处理分类曲线
		 */
		List<TongjiCharge> regist = TongjiCharge.dao.find("select distinct(catalog) from tongji_charge");
		TableRow tr_total = new TableRow("总数", "-");
		this.getTr().add(tr_total);
		BigDecimal total = new BigDecimal(0);
		boolean set_header = false;
		for (TongjiCharge tongjiRegist : regist) {
			// 总数
			String catalog = tongjiRegist.getStr("catalog");
			TableRow row = new TableRow(catalog, "");
			BigDecimal row_all = new BigDecimal(0);
			this.getTr().add(row);
			// 计算曲线统计
			for (int i = 1; i < 13; i++) {
				String month = "";
				if (i < 10) {
					month = "0" + i;
				} else {
					month = "" + i;
				}
				if (!set_header) {
					header.getTd().add(month);
				}
				if(i==12){
					set_header = true;
				}
				BigDecimal count = new BigDecimal(0);
				TongjiCharge regist_model = TongjiCharge.dao.findFirst(
						"select sum(money) as money from tongji_charge where catalog=? and charge_month=?", catalog,
						year + "-" + month);
				if (regist_model != null) {
					count = regist_model.getBigDecimal("money");
					if(count == null)
						count = new BigDecimal(0);
				}
				row_all = row_all.add(count);
				total = total.add(count);
				row.getTd().add(count.toString());
				int size = tr_total.getTd().size();
				if(size < i){
					//tr_total为空时插入
					tr_total.getTd().add(count.toString());
				}else{
					//tr_total已经有数据了
					String tr_total_count = tr_total.getTd().get(i-1);
					tr_total.getTd().set(i-1,(""+(BigDecimal.valueOf(Long.valueOf(tr_total_count)).add(count))));
				}
			}
			//统计总数
			row.setTail(row_all.toString());
		}
		tr_total.setTail(total.toString());
		this.setChart_title(year);
		this.setChart_sub_title("总数：" + total.toString());
		this.setTitle(year + " 收入统计 - 共：" + total.toString());
	}
}
