/**
 * 
 */
package com.quark.app.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.CacheName;
import com.jfinal.plugin.ehcache.EvictInterceptor;
import com.quark.api.annotation.Author;
import com.quark.api.annotation.DataType;
import com.quark.api.annotation.Explaination;
import com.quark.api.annotation.ReturnDBParam;
import com.quark.api.annotation.ReturnOutlet;
import com.quark.api.annotation.Rp;
import com.quark.api.annotation.Type;
import com.quark.api.annotation.URLParam;
import com.quark.api.annotation.UpdateLog;
import com.quark.api.annotation.Value;
import com.quark.api.auto.bean.ResponseValues;
import com.quark.app.logs.AppLog;
import com.quark.common.AppData;
import com.quark.common.RongToken;
import com.quark.interceptor.AppToken;
import com.quark.model.extend.Browse;
import com.quark.model.extend.Collection;
import com.quark.model.extend.LoveYu;
import com.quark.model.extend.News;
import com.quark.model.extend.Search;
import com.quark.model.extend.Sweet;
import com.quark.model.extend.Tokens;
import com.quark.model.extend.User;
import com.quark.model.extend.UserTag;
import com.quark.model.extend.ZanNews;
import com.quark.utils.DateUtils;

/**
 * @author cluo
 * 甜心传媒
 */
public class NewsManage extends Controller implements  Serializable{

	@Author("cluo")
	@Rp("推送消息列表")
	@Explaination(info = "消息列表")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@URLParam(defaultValue = "1", explain = Value.Infer, type = Type.String, name = "pn")
	@URLParam(defaultValue = "5", explain = Value.Infer, type = Type.String, name = "page_size")
	// 返回信息
	// page property
	@ReturnOutlet(name = "NewsListResponse{NewsResult:news:pageNumber}", remarks = "page number", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "NewsListResponse{NewsResult:news:pageSize}", remarks = "result amount of this page", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "NewsListResponse{NewsResult:news:totalPage}", remarks = "total page", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "NewsListResponse{NewsResult:news:totalRow}", remarks = "total row", dataType = DataType.Int, defaultValue = "")
	//
	@ReturnDBParam(name = "NewsListResponse{NewsResult:news:list[news:$]}", column = News.news_id)
	@ReturnDBParam(name = "NewsListResponse{NewsResult:news:list[news:$]}", column = News.title)
	@ReturnDBParam(name = "NewsListResponse{NewsResult:news:list[news:$]}", column = News.new_abstract)
	@ReturnDBParam(name = "NewsListResponse{NewsResult:news:list[news:$]}", column = News.cover)
	@ReturnDBParam(name = "NewsListResponse{NewsResult:news:list[news:$]}", column = News.read_num)
	@ReturnDBParam(name = "NewsListResponse{NewsResult:news:list[news:$]}", column = News.zan_num)
	@ReturnDBParam(name = "NewsListResponse{NewsResult:news:list[news:$]}", column = News.writer)
	@ReturnDBParam(name = "NewsListResponse{NewsResult:news:list[news:$]}", column = News.send_type)
	@ReturnDBParam(name = "NewsListResponse{NewsResult:news:list[news:$]}", column = News.post_time)
	@ReturnOutlet(name = "NewsListResponse{status}", remarks = "1-操作成功", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "NewsListResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	//缓存
	public void newsList() {
		try {
			int pn = getParaToInt("pn", 1);
			int page_size = getParaToInt("page_size", 5);
			String token = getPara("token");
			System.out.println("newsList_token="+token);
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("NewsListResponse", response2);
				renderMultiJson("NewsListResponse");
				return;
			}
			int status = 0;String message="";
			String user_id = AppToken.getUserId(token, this);
			User user = User.dao.findById(user_id);
			int sex = user.get(user.sex);
			final Page<News> newsPage = News.dao
					.paginate(
							pn,
							page_size,
							"select news_id,title,new_abstract,cover,read_num,zan_num,writer,send_type,post_time "," from news where send_type=2 or send_type=? order by post_time desc",sex);
			ResponseValues responseValues = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			responseValues.put("status", 1);
			responseValues.put("code", 200);
			responseValues.put("Result", new HashMap<String, Object>() {
				{
					put("news", newsPage);
				}
			});
			responseValues.put("message", "返回成功");
			setAttr("NewsListResponse", responseValues);
			renderMultiJson("NewsListResponse");
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
			AppData.analyze("NewsManage/newsList", "推送消息列表", this);
		}
	}
	
	@Author("cluo")
	@Rp("消息")
	@Explaination(info = "顶部最新消息")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnDBParam(name = "NewsOneResponse{news:list[news:$]}", column = News.cover)
	@ReturnDBParam(name = "NewsOneResponse{news:list[news:$]}", column = News.new_abstract)
	@ReturnDBParam(name = "NewsOneResponse{news:list[news:$]}", column = News.title)
	@ReturnDBParam(name = "NewsOneResponse{news:list[news:$]}", column = News.news_id)
	@ReturnDBParam(name = "NewsOneResponse{news:list[news:$]}", column = News.post_time)
	@ReturnOutlet(name = "NewsOneResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "NewsOneResponse{status}", remarks = "1-操作成功,0-失败，2-不存在此人", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "NewsOneResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void newsOne() {
		try {
			String token = getPara("token");
			String message = "";
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this, Thread
						.currentThread().getStackTrace()[1].getMethodName());
				response2.put("code", 405);
				response2.put("message", "请重新登陆");
				setAttr("NewsOneResponse", response2);
				renderMultiJson("NewsOneResponse");
				return;
			}
			ResponseValues response = new ResponseValues(this, Thread
					.currentThread().getStackTrace()[1].getMethodName());
			String user_id = AppToken.getUserId(token, this);
			User user = User.dao.findById(user_id);
			if (user!=null) {
				user.set(user.last_login_time, DateUtils.getCurrentDateTime()).update();
			}
			int sex = user.get(user.sex);
			News news = News.dao.findFirst("select news_id,cover,title,new_abstract,post_time from news where send_type=2 or send_type="+sex+" order by post_time desc");
			response.put("news", news);
			response.put("status", 1);
			response.put("code", 200);
			response.put("message", message);
			setAttr("NewsOneResponse", response);
			renderMultiJson("NewsOneResponse");
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
			AppData.analyze("NewsManage/newsOne", "顶部最新消息", this);
		}
	}
	@Author("cluo")
	@Rp("推送消息详情")
	@Explaination(info = "消息详情")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.Int, name = News.news_id)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "NewsDetailResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "NewsDetailResponse{status}", remarks = "1-操作成功,0-失败，2-不存在此人", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "NewsDetailResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void newsDetail() {
		try {
			String token = getPara("token");
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("NewsDetailResponse", response2);
				renderMultiJson("NewsDetailResponse");
				return;
			}
			String user_id = AppToken.getUserId(token, this);
			int news_id = getParaToInt("news_id");
			News news = News.dao.findById(news_id);
			int read_num = news.get(news.read_num);
			int zan_num = news.get(news.zan_num);
			news.set(news.read_num, (read_num+1)).update();
			String content = "",title="",post_date="",writer="";
			if (news!=null) {
				title  = news.getStr("title");
				content  = news.getStr("content");
				post_date  = news.getStr("publish_date");
				writer  = news.getStr("writer");
			}
			ZanNews zanNews = ZanNews.dao.findFirst("select zan_news_id from zan_news where user_id="+user_id+" and news_id="+news_id);
			if (zanNews!=null) {
				setAttr("is_zan", 1);
			}else {
				setAttr("is_zan", 0);
			}
			setAttr("news_id", news_id);
			setAttr("user_id", user_id);
			setAttr("read_num", (read_num+1));
			setAttr("zan_num", zan_num);
			setAttr("title", title);
			setAttr("post_date", post_date);
			setAttr("content", content);
			setAttr("writer", writer);
			render("/webview/detail.html");
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
			AppData.analyze("NewsManage/newsOne", "顶部最新消息", this);
		}
	}
	@Author("cluo")
	@Rp("商务合作")
	@Explaination(info = "商务合作--h5")
	public void hotlineH5() {
		try {
			render("/webview/businesshotline.html");
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
			AppData.analyze("NewsManage/hotlineH5", "商务合作", this);
		}
	}
}
