package com.quark.admin.controller;


import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
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
import com.quark.common.AutoClose;
import com.quark.common.RongToken;
import com.quark.common.config;
import com.quark.interceptor.Login;
import com.quark.interceptor.Privilege;
import com.quark.model.extend.BlackList;
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
 * 新闻列表
 * @author C罗
 * 
 */
@Before(Login.class)
public class NewsManages extends Controller {
	
	public void list() throws ParseException{
		int currentPage = getParaToInt("pn", 1);
		String kw = getPara("kw","");
		int send_type = getParaToInt("send_type", 2);
		String message="list";
		Page<News> newsPage = null;
		String filter_sql=" 1=1 ";
		if (send_type!=2) {
			filter_sql=filter_sql+" and send_type="+send_type;
			message="search";
		}
		if (!kw.equals("")) {
			kw = kw.trim();
			filter_sql = filter_sql +"  and (title like '%" + kw + "%') ";
			message="search";
		}
		setAttr("send_type", send_type);
		setAttr("kw", kw);
		setAttr("action", message);
		newsPage = News.dao.paginate(currentPage, PAGE_SIZE,
				"select * ",
				"from news where "+filter_sql+" order by post_time desc");
		setAttr("list", newsPage);
		setAttr("pn", currentPage);
		render("/admin/NewsList.html");
	}
	
	public void newsInfo(){
		int currentPage = getParaToInt("pn", 1);
		int news_id = getParaToInt("news_id");
		News news = News.dao.findById(news_id);
		setAttr("r", news);
		setAttr("pn", currentPage);
		render("/admin/NewsInfo.html");
	}
	public void add() {
		render("/admin/NewsAdd.html");
	}
	public void addCommit(){
		UploadFile upload_cover = getFile("cover", config.images_path);
		int send_type = getParaToInt("send_type");
		String title = getPara("title");
		String content = getPara("content");
		final String new_abstract = getPara("new_abstract");
		String writer = getPara("writer");
		
		News news = new News();
		if (upload_cover != null) {
			news.set("cover", FileUtils.renameToFile(upload_cover));
		}
		boolean save = news.set(news.title, title).set(news.new_abstract, new_abstract)
				.set(news.content, content).set(news.writer, writer).set(news.send_type, send_type)
				.set(news.post_time, DateUitls.getCurrentDateTime())
				.set(news.publish_date, DateUitls.getCurrentDate())
				.set(news.publish_YM, DateUitls.getCurrentDate())
				.save();
		if (save) {
			List<User>users = null;
			if (send_type==0) {//女
				users = User.dao.find("select user_id from user where status=1 and sex="+send_type);
			}
			if (send_type==1) {//男
				users = User.dao.find("select user_id from user where status=1 and sex="+send_type);
			}
			if (send_type==2) {//全部
				users = User.dao.find("select user_id from user where status=1 ");
			}
			final List<String> toIds = new ArrayList<String>();
			for (User user:users) {
				int user_id2 = user.get(user.user_id);
				toIds.add(String.valueOf(user_id2));
			}
			Thread th = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					RongToken.publishMessage(config.sweet_user_id, toIds, new_abstract);
				}
			});
			th.start();
		}
		redirect("/admin/NewsManages/list");
	}
	public void delete(){
		int currentPage = getParaToInt("pn", 1);
		int news_id = getParaToInt("news_id");
		News news = News.dao.findById(news_id);
		if (news!=null) {
			news.delete();
		}
		redirect("/admin/NewsManages/list?pn="+currentPage);
	}
	public void newsEdit(){
		int currentPage = getParaToInt("pn", 1);
		int news_id = getParaToInt("news_id");
		News news = News.dao.findById(news_id);
		setAttr("r", news);
		setAttr("pn", currentPage);
		render("/admin/NewsModify.html");
	}
	public void addModify(){
		UploadFile upload_cover = getFile("cover", config.images_path);
		int currentPage = getParaToInt("pn", 1);
		int news_id = getParaToInt("news_id");
		News news = News.dao.findById(news_id);
		//int send_type = getParaToInt("send_type");
		String title = getPara("title");
		String content = getPara("content");
		final String new_abstract = getPara("new_abstract");
		String writer = getPara("writer");
		if (upload_cover != null) {
			news.set("cover", FileUtils.renameToFile(upload_cover));
		}
		boolean save = news.set(news.title, title).set(news.new_abstract, new_abstract)
				.set(news.content, content).set(news.writer, writer)//.set(news.send_type, send_type)
				.set(news.post_time, DateUitls.getCurrentDateTime())
				.set(news.publish_date, DateUitls.getCurrentDate())
				.set(news.publish_YM, DateUitls.getCurrentDate())
				.update();
		redirect("/admin/NewsManages/list?pn="+currentPage);
	}
}
