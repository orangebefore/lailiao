package com.quark.common;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.druid.support.logging.Log;
import com.quark.app.logs.AppLog;
import com.quark.model.extend.Applogs;
import com.quark.model.extend.Charge;
import com.quark.model.extend.ChargeGold;
import com.quark.model.extend.TongjiCharge;
import com.quark.model.extend.TongjiRegist;
import com.quark.model.extend.User;
import com.quark.utils.DateUtils;
import com.quark.utils.MessageUtils;

/**
 * 账号安全支付值，只支付单线程操作
 * 
 * @author cluo
 *
 */
public class OrderUtils {

	public static boolean Dall(Charge log) throws Exception {
		// 处理请求
		boolean update = log.set("is_pay", 1).update();
		if (update) {
			int user_id = log.get("user_id");
			int days = log.get(log.days);
			String vip_from_datetime = log.getTimestamp("from_time").toString();
			String vip_end_datetime = log.getTimestamp("end_time").toString();
			BigDecimal charge_money = log.getBigDecimal("money");
			User user = User.dao.findById(user_id);
			TongjiCharge tRegist = new TongjiCharge();
			if (user != null) {
				int sex = user.get(user.sex);
				if (sex == 0) {
					tRegist.set(tRegist.catalog, "女");
				} else {
					tRegist.set(tRegist.catalog, "男");
				}
				// 设置会员记录
				int is_vip = user.get(user.is_vip);
				if (is_vip == 0) {
					user.set(user.vip_from_datetime, vip_from_datetime).set(user.vip_end_datetime, vip_end_datetime);
				} else {
					String user_vip_end_datetime = user.getTimestamp("vip_end_datetime").toString();
					user.set(user.vip_end_datetime, DateUtils.getAddDaysString2(days, user_vip_end_datetime));
				}
				user.set(user.is_vip, 1).update();
			}
			tRegist.set(tRegist.user_id, user_id).set(tRegist.post_time, DateUtils.getCurrentDateTime())
					.set(tRegist.charge_month, DateUtils.getCurrentMonth())
					.set(tRegist.charge_date, DateUtils.getCurrentDate())
					.set(tRegist.charge_hour, DateUtils.getCurrentDateHours()).set(tRegist.money, charge_money).save();
			if (charge_money != null){}
				//MessageUtils.sendCode2("13910900832", "【甜心在线】用户ID：" + user_id + ",成功充值：" + charge_money + "元。");
		}
		return update;
	}

	public static boolean DallGold(ChargeGold log) throws Exception {
		// 处理请求
		boolean update = log.set("is_pay", 1).update();
		if (update) {
			int user_id = log.get("user_id");
			int gold_value = log.get(log.gold_value);
			BigDecimal charge_money = log.getBigDecimal(log.money);
			User user = User.dao.findById(user_id);
			if (user != null) {
				int user_gold_value = user.get(user.user_gold_value);
				user.set(user.user_gold_value, (user_gold_value + gold_value)).update();
			}
			//MessageUtils.sendCode2("13910900832", "【甜心在线】用户ID：" + user_id + ",成功购买：" + charge_money + "元。");
		}
		return update;
	}
}
