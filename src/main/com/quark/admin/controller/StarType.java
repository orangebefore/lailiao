package com.quark.admin.controller;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.quark.interceptor.Login;
import com.quark.model.extend.Constellation;
import com.quark.model.extend.Interest;
import com.quark.utils.DateUtils;

@Before(Login.class)
public class StarType extends Controller{
	
	
	/**
	 * 兴趣列表
	 */
	public void list() {
		int currentPage = getParaToInt("pn", 1);
		String message = getPara("message",null);
		Page<Constellation> CnPage = null;
		String except_sql = "";
		except_sql = " from constellation order by create_time asc";
		setAttr("action", "list");
		CnPage = Constellation.dao.paginate(currentPage, 1000, "select * ", except_sql);
		setAttr("list", CnPage);
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
		render("/admin/StarType.html");
	}

	/**
	 * 增加
	 */
	public void add() {
		String star_name = getPara("star_name");
		int sort = getParaToInt("sort",0);
		Constellation constellation = new Constellation();
		if (star_name!=null) {
			star_name = star_name.trim();
		}
		List<Constellation> cList = Constellation.dao.find("select * from constellation where star_name='"+star_name+"'");
		if (cList.size()==0) {
			boolean save = constellation.set("star_name", star_name).set("sort", sort).set(Constellation.create_time, DateUtils.getCurrentDateTime())
					.save();
			if (save) {
				redirect("/admin/StarType/list?message=1");//添加成功
			}
		}else {
			redirect("/admin/StarType/list?message=2");//添加失败
		}
	}

	/**
	 * 删除
	 */
	public void delete() {
		int star_id = getParaToInt("star_id");
		Constellation constellation = Constellation.dao.findById(star_id);
		boolean delete = constellation.delete();
		redirect("/admin/StarType/list?message=6");
	}

	/**
	 * 更新
	 */
	public void addModify() {
		int star_id = getParaToInt("star_id");
		String star_name = getPara("star_name");
		int sort = getParaToInt("sort",0);
		if (star_name!=null) {
			star_name = star_name.trim();
		}
		List<Constellation> cList = Constellation.dao.find("select * from constellation where star_name='"+star_name+"' and star_id!="+star_id);
		if (cList.size()==0) {
			Constellation constellation = Constellation.dao.findById(star_id);
			boolean update = constellation.set(Constellation.star_name, star_name)
			   .set("sort", sort).set(Constellation.create_time, DateUtils.getCurrentDateTime())
			   .update();
			if (update) {
				redirect("/admin/StarType/list?message=3");//更新成功
			}else {
				redirect("/admin/StarType/list?message=4");//更新不成功
			}
		}else {
			redirect("/admin/StarType/list?message=5");//已存在
		}
	}
	
}
