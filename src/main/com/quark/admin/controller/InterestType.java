package com.quark.admin.controller;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.quark.interceptor.Login;
import com.quark.model.extend.Interest;
import com.quark.model.extend.Tag;
import com.quark.utils.DateUtils;

@Before(Login.class)
public class InterestType extends Controller{
	
	/**
	 * 兴趣列表
	 */
	public void list() {
		int currentPage = getParaToInt("pn", 1);
		String message = getPara("message",null);
		Page<Interest> InPage = null;
		String except_sql = "";
		except_sql = " from interest order by create_time asc";
		setAttr("action", "list");
		InPage = Interest.dao.paginate(currentPage, 1000, "select * ", except_sql);
		setAttr("list", InPage);
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
		render("/admin/InterestType.html");
	}

	/**
	 * 增加
	 */
	public void add() {
		String name = getPara("name");
		int sort = getParaToInt("sort",0);
		Interest interest = new Interest();
		if (name!=null) {
			name = name.trim();
		}
		List<Interest> interests = Interest.dao.find("select * from interest where name='"+name+"'");
		if (interests.size()==0) {
			boolean save = interest.set("name", name).set("sort", sort).set(Interest.create_time, DateUtils.getCurrentDateTime())
					.save();
			if (save) {
				redirect("/admin/InterestType/list?message=1");//添加成功
			}
		}else {
			redirect("/admin/InterestType/list?message=2");//添加失败
		}
	}

	/**
	 * 删除
	 */
	public void delete() {
		int interest_id = getParaToInt("interest_id");
		Interest interest = Interest.dao.findById(interest_id);
		boolean delete = interest.delete();
		redirect("/admin/InterestType/list?message=6");
	}

	/**
	 * 更新
	 */
	public void addModify() {
		int interest_id = getParaToInt("interest_id");
		String name = getPara("name");
		int sort = getParaToInt("sort",0);
		if (name!=null) {
			name = name.trim();
		}
		List<Interest> interests = Interest.dao.find("select * from interest where name='"+name+"' and interest_id!="+interest_id);
		if (interests.size()==0) {
			Interest interest = Interest.dao.findById(interest_id);
			boolean update = interest.set(Interest.name, name)
			   .set("sort", sort).set(Interest.create_time, DateUtils.getCurrentDateTime())
			   .update();
			if (update) {
				redirect("/admin/InterestType/list?message=3");//更新成功
			}else {
				redirect("/admin/InterestType/list?message=4");//更新不成功
			}
		}else {
			redirect("/admin/InterestType/list?message=5");//已存在
		}
	}

}
