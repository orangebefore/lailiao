package com.quark.admin.controller;

import java.util.List;

import org.jsoup.helper.DataUtil;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.quark.interceptor.Login;
import com.quark.interceptor.Privilege;
import com.quark.model.extend.Job;
import com.quark.model.extend.Tag;
import com.quark.utils.DateUtils;

/**
 * tagType管理——分类
 * 
 * @author lucheng
 *
 */
@Before(Login.class)
public class TagTypes extends Controller {

	/**
	 * 分类列表
	 */
	public void list() {
		int currentPage = getParaToInt("pn", 1);
		String message = getPara("message",null);
		Page<Tag> tagPage = null;
		String except_sql = "";
		except_sql = " from tag order by type asc,post_time desc";
		setAttr("action", "list");
		tagPage = Tag.dao.paginate(currentPage, 1000,
				"select * ", except_sql);
		setAttr("list", tagPage);
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
		render("/admin/TagsList.html");
	}

	/**
	 * 增加
	 */
	public void add() {
		String tag_name = getPara("tag_name");
		int sort = getParaToInt("sort",0);
		int sex = getParaToInt("sex",0);//0-女性，1-男性
		Tag tag = new Tag();
		if (tag_name!=null) {
			tag_name = tag_name.trim();
		}
		List<Tag> tags = Tag.dao.find("select * from tag where tag='"+tag_name+"'");
		if (tags.size()==0) {
			boolean save = tag.set("tag", tag_name).set("sort", sort).set(tag.type, sex)
					.set(tag.post_time, DateUtils.getCurrentDateTime())
					.save();
			if (save) {
				redirect("/admin/TagTypes/list?message=1");//添加成功
			}
		}else {
			redirect("/admin/TagTypes/list?message=2");//添加失败
		}
	}

	/**
	 * 删除
	 */
	public void delete() {
		int tag_id = getParaToInt("tag_id");
		Tag tag = Tag.dao.findById(tag_id);
		boolean delete = tag.delete();
		redirect("/admin/TagTypes/list?message=6");
	}

	/**
	 * 更新
	 */
	public void addModify() {
		int tag_id = getParaToInt("tag_id");
		String tag_name = getPara("tag_name");
		int sort = getParaToInt("sort",0);
		if (tag_name!=null) {
			tag_name = tag_name.trim();
		}
		List<Tag> tags = Tag.dao.find("select * from tag where tag='"+tag_name+"' and tag_id!="+tag_id);
		if (tags.size()==0) {
			Tag tag = Tag.dao.findById(tag_id);
			boolean update = tag.set(tag.tag, tag_name)
			   .set("sort", sort).set(tag.post_time, DateUtils.getCurrentDateTime())
			   .update();
			if (update) {
				redirect("/admin/TagTypes/list?message=3");//更新成功
			}else {
				redirect("/admin/TagTypes/list?message=4");//更新不成功
			}
		}else {
			redirect("/admin/TagTypes/list?message=5");//已存在
		}
	}
}