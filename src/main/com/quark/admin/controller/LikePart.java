package com.quark.admin.controller;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.quark.interceptor.Login;
import com.quark.model.extend.Part;
import com.quark.model.extend.Tag;
import com.quark.utils.DateUtils;

@Before(Login.class)
public class LikePart extends Controller{
	
	/**
	 * 分类列表
	 */
	public void list() {
		int currentPage = getParaToInt("pn", 1);
		String message = getPara("message",null);
		Page<Part> partPage = null;
		String except_sql = "";
		except_sql = " from like_part order by type asc,create_time desc";
		setAttr("action", "list");
		partPage = Part.dao.paginate(currentPage, 1000,
				"select * ", except_sql);
		setAttr("list", partPage);
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
		render("/admin/PartList.html");
	}

	/**
	 * 增加
	 */
	public void add() {
		String part_name = getPara("part_name");
		int sort = getParaToInt("sort",0);
		int sex = getParaToInt("sex",0);//0-女性，1-男性
		Part part = new Part();
		if (part_name!=null) {
			part_name = part_name.trim();
		}
		List<Part> parts = Part.dao.find("select * from like_part where part_name='"+part_name+"'");
		if (parts.size()==0) {
			boolean save = part.set("part_name", part_name).set("sort", sort).set("type", sex)
					.set(part.create_time, DateUtils.getCurrentDateTime())
					.save();
			if (save) {
				redirect("/admin/LikePart/list?message=1");//添加成功
			}
		}else {
			redirect("/admin/LikePart/list?message=2");//添加失败
		}
	}

	/**
	 * 删除
	 */
	public void delete() {
		int part_id = getParaToInt("part_id");
		Part part = Part.dao.findById(part_id);
		boolean delete = part.delete();
		redirect("/admin/LikePart/list?message=6");
	}

	/**
	 * 更新
	 */
	public void addModify() {
		int part_id = getParaToInt("part_id");
		String part_name = getPara("part_name");
		int sort = getParaToInt("sort",0);
		if (part_name!=null) {
			part_name = part_name.trim();
		}
		List<Part> parts = Part.dao.find("select * from like_part where part_name='"+part_name+"' and part_id!="+part_id);
		if (parts.size()==0) {
			Part part = Part.dao.findById(part_id);
			boolean update = part.set(part.part_name, part_name)
			   .set("sort", sort).set(part.create_time, DateUtils.getCurrentDateTime())
			   .update();
			if (update) {
				redirect("/admin/LikePart/list?message=3");//更新成功
			}else {
				redirect("/admin/LikePart/list?message=4");//更新不成功
			}
		}else {
			redirect("/admin/LikePart/list?message=5");//已存在
		}
	}
}
