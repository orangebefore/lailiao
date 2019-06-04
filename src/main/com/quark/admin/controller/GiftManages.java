package com.quark.admin.controller;


import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.eclipse.jetty.util.UrlEncoded;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.upload.UploadFile;
import com.quark.common.config;
import com.quark.interceptor.Login;
import com.quark.interceptor.Privilege;
import com.quark.model.extend.BlackList;
import com.quark.model.extend.Gift;
import com.quark.model.extend.GoldPrice;
import com.quark.model.extend.Job;
import com.quark.model.extend.News;
import com.quark.model.extend.Tag;
import com.quark.model.extend.User;
import com.quark.model.extend.UserIncome;
import com.quark.model.extend.UserOld;
import com.quark.model.extend.UserShape;
import com.quark.model.extend.UserTag;
import com.quark.utils.DateUtils;
import com.quark.utils.FileUtils;
import com.quark.utils.MD5Util;
import com.quarkso.utils.DateUitls;


/**
 * 礼物列表
 * @author C罗
 * 
 */
@Before(Login.class)
public class GiftManages extends Controller {
	
	public void list() throws ParseException{
		int currentPage = getParaToInt("pn", 1);
		String kw = getPara("kw","");
		String message="list";
		Page<Gift> giftPage = null;
		String filter_sql=" 1=1 ";
		if (!kw.equals("")) {
			kw = kw.trim();
			filter_sql = filter_sql +"  and (gift_name like '%" + kw + "%') ";
			message="search";
		}
		setAttr("kw", kw);
		setAttr("action", message);
		giftPage = Gift.dao.paginate(currentPage, PAGE_SIZE,
				"select * ",
				"from gift where "+filter_sql+" order by sort asc,post_time desc");
		setAttr("list", giftPage);
		setAttr("pn", currentPage);
		render("/admin/GiftList.html");
	}

	public void add() {
		render("/admin/GiftAdd.html");
	}
	public void addCommit(){
		UploadFile upload_cover = getFile("gift_cover", config.images_path);
		String gift_name = getPara("gift_name");
		int gold_value = getParaToInt("gold_value");
		int sort = getParaToInt("sort");
		Gift gift = new Gift();
		if (upload_cover != null) {
			gift.set("gift_cover", FileUtils.renameToFile(upload_cover));
		}
		gift.set(gift.gift_name, gift_name)
				.set(gift.sort, sort).set(gift.gold_value, gold_value)
				.set(gift.post_time, DateUitls.getCurrentDateTime())
				.save();
		redirect("/admin/GiftManages/list");
	}
	/**
	 * 修改
	 */
	public void modify() {
		int currentPage = getParaToInt("pn", 1);
		int gift_id = getParaToInt("gift_id");
		Gift gift = Gift.dao.findById(gift_id);
		setAttr("r", gift);
		setAttr("pn", currentPage);
		render("/admin/GiftModify.html");
	}
	public void addModify() {
		UploadFile upload_cover = getFile("gift_cover", config.images_path);
		int currentPage = getParaToInt("pn", 1);
		String gift_name = getPara("gift_name");
		int gold_value = getParaToInt("gold_value");
		int sort = getParaToInt("sort");
		
		int gift_id = getParaToInt("gift_id");
		Gift gift = Gift.dao.findById(gift_id);
		if (upload_cover != null) {
			gift.set("gift_cover", FileUtils.renameToFile(upload_cover));
		}
		gift.set(gift.gift_name, gift_name)
			.set(gift.sort, sort).set(gift.gold_value, gold_value)
			.set(gift.post_time, DateUitls.getCurrentDateTime())
			.update();
		redirect("/admin/GiftManages/list?pn="+currentPage);
	}
}
