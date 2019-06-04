package com.quark.admin.controller;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.quark.interceptor.Login;
import com.quark.model.extend.Constellation;
import com.quark.model.extend.Interest;
import com.quark.model.extend.UserExpect;
import com.quark.utils.DateUtils;

@Before(Login.class)
public class UserExpectType extends Controller{
	
	
	
	public void list() {
		int currentPage = getParaToInt("pn", 1);
		String message = getPara("message",null);
		Page<UserExpect> UPage = null;
		String except_sql = "";
		except_sql = " from user_expect order by expect_gender asc";
		setAttr("action", "list");
		UPage = UserExpect.dao.paginate(currentPage, 1000, "select * ", except_sql);
		setAttr("list", UPage);
		setAttr("pn", currentPage);
		if (message!=null) {
			if (message.equals("1")) {
				setAttr("ok", "添加成功");
			}
			if (message.equals("2")) {
				setAttr("ok", "添加失败，已经有同类型");
			}
			if (message.equals("3")) {
				setAttr("ok", "修改成功");
			}
			if (message.equals("4")) {
				setAttr("ok", "修改失败");
			}
			if (message.equals("5")) {
				setAttr("ok", "修改失败，已经有同类型");
			}
			if (message.equals("6")) {
				setAttr("ok", "删除成功");
			}
		}
		render("/admin/ExpectList.html");
	}

	/**
	 * 增加
	 */
	public void add() {
		String expect_name = getPara("expect_name");
		int expect_gender = getParaToInt("expect_gender",0);
		UserExpect uExpect = new UserExpect();
		if (expect_name!=null) {
			expect_name = expect_name.trim();
		}
		List<UserExpect> uList = uExpect.dao.find("select * from user_expect where expect_name='"+expect_name+"'");
		if (uList.size()==0) {
			boolean save = UserExpect.dao.set("expect_name", expect_name).set("expect_gender", expect_gender).set(Constellation.create_time, DateUtils.getCurrentDateTime())
					.save();
			if (save) {
				redirect("/admin/UserExpectType/list?message=1");//添加成功
			}
		}else {
			redirect("/admin/UserExpectType/list?message=2");//添加失败
		}
	}

	/**
	 * 删除
	 */
	public void delete() {
		int expect_id = getParaToInt("expect_id");
		UserExpect uExpect = UserExpect.dao.findById(expect_id);
		boolean delete = uExpect.delete();
		redirect("/admin/UserExpectType/list?message=6");
	}

	/**
	 * 更新
	 */
	public void addModify() {
		int expect_id = getParaToInt("expect_id");
		String expect_name = getPara("expect_name");
		int expect_gender = getParaToInt("expect_gender",0);
		if (expect_name!=null) {
			expect_name = expect_name.trim();
		}
		List<UserExpect> uList = UserExpect.dao.find("select * from constellation where expect_name='"+expect_name+"' and expect_id!="+expect_id);
		if (uList.size()==0) {
			UserExpect userExpect = UserExpect.dao.findById(expect_id);
			boolean update = userExpect.set(UserExpect.expect_name, expect_name)
			   .set(UserExpect.expect_gender, expect_gender).set(Constellation.create_time, DateUtils.getCurrentDateTime())
			   .update();
			if (update) {
				redirect("/admin/UserExpectType/list?message=3");//更新成功
			}else {
				redirect("/admin/UserExpectType/list?message=4");//更新不成功
			}
		}else {
			redirect("/admin/UserExpectType/list?message=5");//已存在
		}
	}
	
}
