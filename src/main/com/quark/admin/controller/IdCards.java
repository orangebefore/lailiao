package com.quark.admin.controller;

import java.math.BigDecimal;
import java.util.List;

import org.jsoup.helper.DataUtil;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.quark.interceptor.Login;
import com.quark.interceptor.Privilege;
import com.quark.model.extend.Certification;
import com.quark.model.extend.Charge;
import com.quark.model.extend.Job;
import com.quark.model.extend.Tag;
import com.quark.model.extend.User;
import com.quark.utils.DateUtils;

/**
 * 身份证认证审核
 * 
 */
@Before(Login.class)
public class IdCards extends Controller {

	/**
	 * 身份证认证列表
	 */
	public void list() {
		int currentPage = getParaToInt("pn", 1);
		int id_card_status = getParaToInt("id_card_status", 2);
		String message = getPara("message",null);
		Page<Certification> idCardPage = null;
		String except_sql = "";
		except_sql = " from certification where id_card_up is not null and id_card_down is not null and id_card_status = " + id_card_status + " order by id";
		setAttr("action", "list");
		idCardPage = Certification.dao.paginate(currentPage, 3,
				"select * ", except_sql);
		//查询用户昵称
		List<Certification> list = idCardPage.getList();
		int total_money = 0;
		for (Certification certification : list) {
			int user_id = certification.get("user_id");
			User user = User.dao.findById(user_id);
			String nickname="";
			if (user!=null) {
				nickname = user.getStr("nickname");
			}
			certification.put("nickname", nickname);
		}

		setAttr("list", idCardPage);
		setAttr("pn", currentPage);
		setAttr("id_card_status", id_card_status);
		if (message!=null) {
			if (message.equals("1")) {
				setAttr("ok", "审核已通过");
			}
			if (message.equals("2")) {
				setAttr("ok", "审核失败");
			}
			if (message.equals("3")) {
				setAttr("ok", "审核未通过");
			}
			if (message.equals("4")) {
				setAttr("ok", "审核异常");
			}
			
		}
		render("/admin/IdCardList.html");
	}
	
	/**
	 * 判断身份证认证是否通过
	 */
	public void modifyIsCard() {
		int user_id = getParaToInt("user_id");
		int id = getParaToInt("id");
		String id_card_reason = getPara("id_card_reason");
		String message = getPara("message",null);
		Boolean flag = getParaToBoolean("flag");
		Certification certification = Certification.dao.findById(id);
		if(flag) {
			User user = User.dao.findById(user_id);
			boolean update = user.set(user.is_card, "1").update();
			if (update) {
				boolean status = certification.set(Certification.id_card_status, "1").set(Certification.id_card_reason, "").update();
				if (status) {
					redirect("/admin/IdCards/list?message=1");//修改成功
				} else {
					redirect("/admin/IdCards/list?message=4");//修改失败
				}
			} else {
				redirect("/admin/IdCards/list?message=4");//修改失败
			}
		} else {
			boolean status = certification.set(Certification.id_card_status, "0").set(Certification.id_card_reason, id_card_reason).update();
			if (status) {
				redirect("/admin/IdCards/list?message=3");//修改成功
			} else {
				redirect("/admin/IdCards/list?message=4");//修改失败
			}
		}
		
			
	}
	
	
	
}