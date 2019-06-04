package com.quark.admin.controller;

import java.util.List;

import org.jsoup.helper.DataUtil;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.quark.interceptor.Login;
import com.quark.interceptor.Privilege;
import com.quark.model.extend.Job;
import com.quark.model.extend.LoveYu;
import com.quark.model.extend.Sweet;
import com.quark.model.extend.Tag;
import com.quark.utils.DateUtils;

/**
 * tagType管理——分类
 * 
 * @author c罗
 *
 */
@Before(Login.class)
public class Loves extends Controller {

	/**
	 * 喜欢语录列表
	 */
	public void list() {
		int currentPage = getParaToInt("pn", 1);
		String message = getPara("message",null);
		Page<LoveYu> sweetPage = null;
		String except_sql = "";
		except_sql = " from love_yu where status=1 order by post_time desc";
		setAttr("action", "list");
		sweetPage = LoveYu.dao.paginate(currentPage, PAGE_SIZE,
				"select * ", except_sql);
		setAttr("list", sweetPage);
		setAttr("pn", currentPage);
		if (message!=null) {
			if (message.equals("1")) {
				setAttr("ok", "添加成功");
			}
			if (message.equals("2")) {
				setAttr("ok", "添加失败");
			}
			if (message.equals("3")) {
				setAttr("ok", "删除成功");
			}
			if (message.equals("4")) {
				setAttr("ok", "修改成功");
			}
			if (message.equals("5")) {
				setAttr("ok", "修改失败");
			}
		}
		render("/admin/LovesList.html");
	}

	/**
	 * 增加
	 */
	public void add() {
		String sweet_content = getPara("sweet_content");
		LoveYu sweet = new LoveYu();
		if (sweet_content!=null) {
			sweet_content = sweet_content.trim();
		}
		boolean save = sweet.set(sweet.love_yu, sweet_content).set(sweet.status, 1)
				.set(sweet.post_time, DateUtils.getCurrentDateTime())
				.save();
		if (save) {
			redirect("/admin/Loves/list?message=1");//添加成功
		}else {
			redirect("/admin/Loves/list?message=2");//添加失败
		}
	}

	/**
	 * 删除
	 */
	public void delete() {
		int sweet_id = getParaToInt("love_yu_id");
		LoveYu sweet = LoveYu.dao.findById(sweet_id);
		boolean delete = sweet.delete();
		redirect("/admin/Loves/list?message=3");
	}

	/**
	 * 更新
	 */
	public void addModify() {
		int sweet_id = getParaToInt("love_yu_id");
		String sweet_content = getPara("sweet_content");
		int time = getParaToInt("time",1);
		if (sweet_content!=null) {
			sweet_content = sweet_content.trim();
		}
		LoveYu sweet = LoveYu.dao.findById(sweet_id);
		boolean update = sweet.set(sweet.love_yu, sweet_content)
		   .set(sweet.post_time, DateUtils.getCurrentDateTime())
		   .update();
		if (update) {
			redirect("/admin/Loves/list?message=4");//更新成功
		}else {
			redirect("/admin/Loves/list?message=5");//更新不成功
		}
	}
}