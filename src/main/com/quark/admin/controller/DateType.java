package com.quark.admin.controller;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.quark.interceptor.Login;
import com.quark.model.extend.Interest;
import com.quark.model.extend.LikeDate;
import com.quark.utils.DateUtils;

@Before(Login.class)
public class DateType extends Controller{
	
	/**
	 * 兴趣列表
	 */
	public void list() {
		int currentPage = getParaToInt("pn", 1);
		String message = getPara("message",null);
		Page<LikeDate> LiPage = null;
		String except_sql = "";
		except_sql = " from like_date order by create_time asc";
		setAttr("action", "list");
		LiPage = LikeDate.dao.paginate(currentPage, 1000, "select * ", except_sql);
		setAttr("list", LiPage);
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
		render("/admin/DateType.html");
	}

	/**
	 * 增加
	 */
	public void add() {
		String date_name = getPara("date_name");
		int sort = getParaToInt("sort",0);
		LikeDate likeDate = new LikeDate();
		if (date_name!=null) {
			date_name = date_name.trim();
		}
		List<LikeDate> likeDates = LikeDate.dao.find("select * from like_date where date_name='"+date_name+"'");
		if (likeDates.size()==0) {
			boolean save = likeDate.set("date_name", date_name).set("sort", sort).set(LikeDate.create_time, DateUtils.getCurrentDateTime())
					.save();
			if (save) {
				redirect("/admin/DateType/list?message=1");//添加成功
			}
		}else {
			redirect("/admin/DateType/list?message=2");//添加失败
		}
	}

	/**
	 * 删除
	 */
	public void delete() {
		int date_id = getParaToInt("date_id");
		LikeDate likeDate = LikeDate.dao.findById(date_id);
		boolean delete = likeDate.delete();
		redirect("/admin/DateType/list?message=6");
	}

	/**
	 * 更新
	 */
	public void addModify() {
		int date_id = getParaToInt("date_id");
		String date_name = getPara("date_name");
		int sort = getParaToInt("sort",0);
		if (date_name!=null) {
			date_name = date_name.trim();
		}
		List<LikeDate> likeDates = LikeDate.dao.find("select * from like_date where date_name='"+date_name+"' and date_id!="+date_id);
		if (likeDates.size()==0) {
			LikeDate likeDate = LikeDate.dao.findById(date_id);
			boolean update = likeDate.set(LikeDate.date_name, date_name)
			   .set("sort", sort).set(LikeDate.create_time, DateUtils.getCurrentDateTime())
			   .update();
			if (update) {
				redirect("/admin/DateType/list?message=3");//更新成功
			}else {
				redirect("/admin/DateType/list?message=4");//更新不成功
			}
		}else {
			redirect("/admin/DateType/list?message=5");//已存在
		}
	}

}
