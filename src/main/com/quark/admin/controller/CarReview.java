package com.quark.admin.controller;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jolbox.bonecp.UsernamePassword;
import com.quark.interceptor.Login;
import com.quark.model.extend.Certification;
import com.quark.model.extend.Constellation;
import com.quark.model.extend.LikeDate;
import com.quark.model.extend.User;
import com.quark.utils.DateUtils;

@Before(Login.class)
public class CarReview extends Controller{	
	
	
	public void list() {
		int currentPage = getParaToInt("pn", 1);
		String message = getPara("message",null);
		int car_status = getParaToInt("car_status", 2);
		Page<Certification> CePage = null;
		String except_sql = "";
		if( car_status == 3){
			except_sql = "FROM certification,user where 1=1 AND certification.user_id = user.user_id order by id asc";
		}else {
			except_sql = "FROM certification,user WHERE car_status="+car_status+" AND certification.user_id = user.user_id order by id asc";
		}
		setAttr("action", "list");
		CePage = Certification.dao.paginate(currentPage, 1000, "SELECT *", except_sql);
		setAttr("list", CePage);
		setAttr("pn", currentPage);
		setAttr("car_status", car_status);
		if (message!=null) {
			if (message.equals("0")) {
				setAttr("ok", "添加成功");
			}
			if (message.equals("1")) {
				setAttr("ok", "审核通过");
			}
			if (message.equals("2")) {
				setAttr("ok", "审核失败");
			}
		}
		render("/admin/CarReviewType.html");
	}

	/**
	 * 通过
	 */
	public void pass() {
		int user_id = getParaToInt("user_id");
		int id = getParaToInt("id");
		User.dao.findById(user_id).set(User.is_car,1).update();
		Certification.dao.findById(id).set(Certification.car_status,1).update();
		
		redirect("/admin/CarReview/list?message=1");
	}
	
	
	
	public void addModify() {
		int id = getParaToInt("id");
		String car_reason = getPara("car_reason");
		if (car_reason!=null) {
			car_reason = car_reason.trim();
		}
		Certification.dao.findById(id).set("car_reason", car_reason).set(Certification.car_status,0).update();
		
		redirect("/admin/CarReview/list");
	}

}
	


