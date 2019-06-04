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
import com.quark.model.extend.User;
import com.quarkso.utils.Base64Util;
import com.quarkso.utils.DateUitls;

/**
 * 充值记录
 * 
 * @author C罗
 *
 */
@Before(Login.class)
public class Charges extends Controller {

	public void list() {
		int currentPage = getParaToInt("pn", 1);
		int is_pay = getParaToInt("is_pay", 1);
		String pay_type = getPara("pay_type","");
		String kw = getPara("kw","");
		String start_time = getPara("start_time", "");
		String end_time = getPara("end_time", "");

		String message="list";
		String filter_sql = " is_pay ="+is_pay;
		
		Page<Charge> chargePage = null;
		
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
		chargePage = Charge.dao.paginate(
				currentPage, 
				PAGE_SIZE, 
				"select * ", 
				"from charge where "+ filter_sql + " order by charge_time desc");
		
		List<Charge> list = chargePage.getList();
		int total_money = 0;
		for (Charge charge : list) {
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
		
		Charge normol_count = Charge.dao.findFirst("select count(*) as normol_count from charge where is_pay=1  ");
		Charge nodo_count = Charge.dao.findFirst("select count(*) as nodo_count from charge where is_pay=0  ");
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
		render("/admin/ChargesList.html");
	}
	
	/**
	 *  充值列表导出Excel
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	public void chargeExcel() throws IOException, RowsExceededException, WriteException {
		
		int is_pay = getParaToInt("is_pay", 1);
		String start_time = getPara("start_time", "");
		String end_time = getPara("end_time", "");
		String type = getPara("type","");
		String kw = getPara("kw","");

		String message="list";
		String filter_sql = " is_pay ="+is_pay;
		
		if (!start_time.equals("")&&!end_time.equals("")) {
			filter_sql = filter_sql + " and ( charge_time between '" + start_time+ "' and '" + end_time + "') ";
			message="search";
		}
		if (!type.equals("全部")&&!type.equals("")) {
			filter_sql = filter_sql + " and pay_type like'%" + type + "%'";
			message="search";
		}
		if (!kw.equals("")) {
			kw = kw.trim();
			filter_sql = filter_sql +"  and pay_id like '%"+kw+"%' or user_id in(select user_id from user where nickname like '%"+kw+"%' or telephone like '%"+kw+"%') ";
			message="search";
		}
		
		List<Charge> chargeLogs = Charge.dao.find("select * from charge where "+ filter_sql + " order by charge_time desc");

		for (Charge charge : chargeLogs) {
			int user_id = charge.get("user_id");
			User user = User.dao.findById(user_id);
			String telephone = "",nickname="";
			if (user!=null) {
				telephone = user.getStr("telephone");
				nickname = user.getStr("nickname");
			}
			charge.put("telephone", telephone);
			charge.put("nickname", nickname);
		}
		OutputStream os = getResponse().getOutputStream();// 取得输出流
		getResponse().reset();// 清空输出流
		// 下面是对中文文件名的处理
		getResponse().setCharacterEncoding("UTF-8");// 设置相应内容的编码格式
		String fname = System.currentTimeMillis() + "";
		fname = java.net.URLEncoder.encode(fname, "UTF-8");
		getResponse().setHeader("Content-Disposition",
				"attachment;filename=" + new String(fname.getBytes("UTF-8"), "GBK") + ".xls");
		getResponse().setContentType("application/msexcel");// 定义输出类型
		// 创建工作薄
		WritableWorkbook workbook = Workbook.createWorkbook(os);
		// 创建新的一页
		WritableSheet sheet = workbook.createSheet("First Sheet", 0);
		// 创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
		Label id = new Label(0, 0, "ID");
		sheet.addCell(id);
		Label nickname = new Label(1, 0, "昵称");
		sheet.addCell(nickname);
		Label telephone = new Label(2, 0, "电话");
		sheet.addCell(telephone);
		Label email = new Label(3, 0, "支付方式");
		sheet.addCell(email);
		Label username = new Label(4, 0, "金额");
		sheet.addCell(username);
		Label pay_id = new Label(5, 0, "对账交易号");
		sheet.addCell(pay_id);
		Label hangye = new Label(6, 0, "交易日期");
		sheet.addCell(hangye);
		for (int i = 0; i < chargeLogs.size(); ++i) {
			int j = 0;
			int db_pay_type = chargeLogs.get(i).get("pay_type");
			String pay_type_str = "";
			if (db_pay_type==0) {
				pay_type_str="支付宝";
			}else if (db_pay_type==1) {
				pay_type_str="微信";
			}else {
				pay_type_str="银联";
			}
			int charge_id = chargeLogs.get(i).get(Charge.charge_id);
			String db_nickname = chargeLogs.get(i).getStr("nickname");
			String db_telephone = chargeLogs.get(i).getStr("telephone");
			BigDecimal db_money = chargeLogs.get(i).getBigDecimal("money");
			String db_pay_id = chargeLogs.get(i).getStr("pay_id");
			String db_post_time = chargeLogs.get(i).getTimestamp("charge_time").toString();
		
			Label id_db = new Label(j++,i+1, ""+charge_id);
			sheet.addCell(id_db);
			Label nickname_db = new Label(j++,i+1, db_nickname);
			sheet.addCell(nickname_db);
			Label telephone_db = new Label(j++,i+1, db_telephone);
			sheet.addCell(telephone_db);
			Label pay_type_str_db = new Label(j++,i+1, pay_type_str+"");
			sheet.addCell(pay_type_str_db);
			Label db_money_db = new Label(j++,i+1, db_money+"");
			sheet.addCell(db_money_db);
			Label db_pay_id_db = new Label(j++,i+1, db_pay_id);
			sheet.addCell(db_pay_id_db);
			Label post_time_db = new Label(j++,i+1, db_post_time);
			sheet.addCell(post_time_db);
		}

		// 把创建的内容写入到输出流中，并关闭输出流
		workbook.write();
		workbook.close();
		os.close();
		renderNull();
	}
}
