package com.quark.admin.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.upload.UploadFile;
import com.quark.common.config;
import com.quark.interceptor.AdminLogin;
import com.quark.interceptor.Login;
import com.quark.interceptor.Privilege;
import com.quark.model.extend.Charge;
import com.quark.model.extend.ChargeGold;
import com.quark.model.extend.User;
import com.quarkso.utils.Base64Util;
import com.quarkso.utils.DateUitls;

/**
 * 购买礼物记录
 * 
 * @author C罗
 *
 */
@Before(Login.class)
public class ChargesGold extends Controller {

	public void list() {
		int currentPage = getParaToInt("pn", 1);
		int is_pay = getParaToInt("is_pay", 1);
		String pay_type = getPara("pay_type","");
		String kw = getPara("kw","");
		String start_time = getPara("start_time", "");
		String end_time = getPara("end_time", "");

		String message="list";
		String filter_sql = " is_pay ="+is_pay;
		
		Page<ChargeGold> chargePage = null;
		
		if (!start_time.equals("")&&!end_time.equals("")) {
			filter_sql = filter_sql + " and ( charge_time between '" + start_time+ "' and '" + end_time + "') ";
			message="search";
		}
		if (!pay_type.equals("全部")&&!pay_type.equals("")) {
			filter_sql = filter_sql + " and pay_type=" + pay_type;
			message="search";
		}
		if (!kw.equals("")) {
			kw = kw.trim();
			filter_sql = filter_sql +"  and pay_id like '%"+kw+"%' or user_id in(select user_id from user where nickname like '%"+kw+"%' or telephone like '%"+kw+"%') ";
			message="search";
		}
		chargePage = ChargeGold.dao.paginate(
				currentPage, 
				PAGE_SIZE, 
				"select * ", 
				"from charge_gold where "+ filter_sql + " order by charge_time desc");
		
		List<ChargeGold> list = chargePage.getList();
		int total_money = 0;
		for (ChargeGold charge : list) {
			int user_id = charge.get("user_id");
			User user = User.dao.findById(user_id);
			String telephone = "",nickname="";
			int sex = 0;
			if (user!=null) {
				sex = user.get("sex");
				telephone = user.getStr("telephone");
				nickname = user.getStr("nickname");
			}
			charge.put("sex", sex);
			charge.put("telephone", telephone);
			charge.put("nickname", nickname);
			//总金额
			BigDecimal money = charge.getBigDecimal("money");
			total_money =total_money+money.intValue();
		}
		setAttr("total_money", total_money);
		
		ChargeGold normol_count = ChargeGold.dao.findFirst("select count(*) as normol_count from charge_gold where is_pay=1  ");
		ChargeGold nodo_count = ChargeGold.dao.findFirst("select count(*) as nodo_count from charge_gold where is_pay=0  ");
		setAttr("normol_count", normol_count.get("normol_count"));
		setAttr("nodo_count", nodo_count.get("nodo_count"));
		
		setAttr("is_pay", is_pay);
		setAttr("start_time", start_time);
		setAttr("end_time", end_time);
		
		setAttr("pay_type", pay_type);
		setAttr("kw", kw);
		setAttr("action", message);
		setAttr("list", chargePage);
		
		setAttr("pn", currentPage);
		render("/admin/ChargesGoldList.html");
	}
}
