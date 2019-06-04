package com.quark.admin.controller;


import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.eclipse.jetty.util.UrlEncoded;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.upload.UploadFile;
import com.quark.common.config;
import com.quark.interceptor.Login;
import com.quark.interceptor.Privilege;
import com.quark.model.extend.Audit;
import com.quark.model.extend.BlackList;
import com.quark.model.extend.Gift;
import com.quark.model.extend.GoldPrice;
import com.quark.model.extend.Job;
import com.quark.model.extend.News;
import com.quark.model.extend.Tag;
import com.quark.model.extend.User;
import com.quark.model.extend.UserIncome;
import com.quark.model.extend.UserOld;
import com.quark.model.extend.UserShape;
import com.quark.model.extend.UserTag;
import com.quark.utils.DateUtils;
import com.quark.utils.FileUtils;
import com.quark.utils.MD5Util;
import com.quarkso.utils.DateUitls;


/**
 * 礼物列表
 * @author C罗
 * 
 */
@Before(Login.class)
public class EduAudit extends Controller {
	
	public void list() throws ParseException{
		int currentPage = getParaToInt("pn", 1);
		String kw = getPara("kw","");
		String message="list";
		int edu_status = getParaToInt("edu_status", 2);
		Page<Audit> giftPage = null;
		String filter_sql=" 1=1 ";
		if (!kw.equals("")) {
			kw = kw.trim();
			filter_sql = filter_sql +" and (state like '%" + kw + "%')";
			message="search";
		}
		setAttr("kw", kw);
		setAttr("action", message);
		if( edu_status == 3){
			giftPage = Audit.dao.paginate(currentPage, PAGE_SIZE,"select * ","FROM certification,user where 1=1 AND certification.user_id = user.user_id order by id asc");
		}else if(edu_status == 2) {
			giftPage = Audit.dao.paginate(currentPage, PAGE_SIZE,"select * ","FROM certification,user WHERE edu_status=2 AND certification.user_id = user.user_id order by id asc");
		}else {
			giftPage = Audit.dao.paginate(currentPage, PAGE_SIZE,"select * ","FROM certification,user WHERE edu_status="+edu_status+" AND certification.user_id = user.user_id order by id asc");
		}
		setAttr("list", giftPage);
		setAttr("edu_status", edu_status);
		setAttr("pn", currentPage);
		render("/admin/EduAuditList.html");
	}
	/**
	 * 审核
	 */
	public void pass() {
		int currentPage = getParaToInt("pn", 1);
		String state = getPara("state");
		int isAudit = getParaToInt("isAudit");
		int id = getParaToInt("id");
		int user_id = getParaToInt("user_id");
		Audit audit = Audit.dao.findById(id);
		User user = User.dao.findById(user_id);
		audit.set(audit.edu_status, state).set(audit.edu_reason, "").update();
		user.set(user.is_edu, isAudit).update();
		redirect("/admin/EduAudit/list?pn="+currentPage);
	}
	
	public void refush() {
		int currentPage = getParaToInt("pn", 1);
		String edu_reason = getPara("edu_reason");
		int id = getParaToInt("id");
		if (edu_reason != null) {
			edu_reason = edu_reason.trim();
			Audit.dao.findById(id).set("edu_reason", edu_reason).set(Audit.edu_status,0).update();
			int user_id = getParaToInt("user_id");
			User.dao.findById(user_id).set(User.is_edu, 0).update();
		}
		redirect("/admin/EduAudit/list?pn="+currentPage);
	}
	
}
