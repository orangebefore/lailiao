package com.quark.admin.controller;

import java.util.List;

import org.jsoup.helper.DataUtil;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.quark.interceptor.Login;
import com.quark.interceptor.Privilege;
import com.quark.model.extend.Job;
import com.quark.model.extend.SloveLanguage;
import com.quark.model.extend.Sweet;
import com.quark.model.extend.Tag;
import com.quark.utils.DateUtils;

/**
 * 甜心语言示例
 * 
 * @author c罗
 *
 */
@Before(Login.class)
public class SloveLanguages extends Controller {

	/**
	 * 甜心语录列表
	 */
	public void list() {
		int currentPage = getParaToInt("pn", 1);
		String message = getPara("message",null);
		Page<SloveLanguage> slovePage = null;
		String except_sql = "";
		except_sql = " from slove_language order by type asc,post_time desc";
		setAttr("action", "list");
		slovePage = SloveLanguage.dao.paginate(currentPage, PAGE_SIZE,
				"select * ", except_sql);
		setAttr("list", slovePage);
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
		render("/admin/LoveLanguageList.html");
	}

	/**
	 * 增加
	 */
	public void add() {
		String slove_lang = getPara("slove_lang");
		int type = getParaToInt("type",1);
		SloveLanguage sLanguage = new SloveLanguage();
		boolean save = sLanguage.set(sLanguage.slove_lang, slove_lang)
				.set(sLanguage.type, type)
				.set(sLanguage.post_time, DateUtils.getCurrentDateTime())
				.save();
		if (save) {
			redirect("/admin/SloveLanguages/list?message=1");//添加成功
		}else {
			redirect("/admin/SloveLanguages/list?message=2");//添加失败
		}
	}

	/**
	 * 删除
	 */
	public void delete() {
		int slove_language_id = getParaToInt("slove_language_id");
		SloveLanguage  sLanguage= SloveLanguage.dao.findById(slove_language_id);
		boolean delete = sLanguage.delete();
		redirect("/admin/SloveLanguages/list?message=3");
	}

	/**
	 * 更新
	 */
	public void addModify() {
		int slove_language_id = getParaToInt("slove_language_id");
		String slove_lang = getPara("slove_lang");
		int type = getParaToInt("type",1);
		SloveLanguage  sLanguage= SloveLanguage.dao.findById(slove_language_id);
		boolean update = sLanguage.set(sLanguage.slove_lang, slove_lang)
				.set(sLanguage.type, type)
				.set(sLanguage.post_time, DateUtils.getCurrentDateTime())
				.update();
		if (update) {
			redirect("/admin/SloveLanguages/list?message=4");//更新成功
		}else {
			redirect("/admin/SloveLanguages/list?message=5");//更新不成功
		}
	}
}