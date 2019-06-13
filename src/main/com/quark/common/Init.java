package com.quark.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.print.Doc;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.xml.sax.InputSource;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheName;
import com.quark.api.auto.bean.ResponseValues;
import com.quark.model.extend.News;
import com.quark.model.extend.Notices;
import com.quark.model.extend.User;
import com.quark.model.extend.ZanNews;
import com.quark.utils.DateUtils;
import com.quark.utils.MessageUtils;
import com.tenpay.ResponseHandler;
import com.tenpay.util.ConstantUtil;
import com.tenpay.util.WXUtil;

public class Init extends Controller {

	private static boolean isStart = false;

	// 缓存
	// @Before(CacheInterceptor.class)
	// @CacheName("sweetinfo")
	public void index() throws IOException, InterruptedException {
		if (!isStart) {
			showTimer();
			showStarTimer();
			NoticeVipTimer();
			NoticeTimer();
			isStart = true;
			List<User> list = new ArrayList<User>();// User.dao.find("select * from user");
			int i = 1;

			for (User user : list) {
				String province = user.getStr(User.province);
				String city = user.getStr(User.city);
				if (city == null || "".equals(city)) {
					city = "北京市";
				}
				String url = "http://restapi.amap.com/v3/geocode/geo?address=" + URLEncoder.encode(city, "UTF-8")
						+ "&output=XML&key=e8c6003e91a18d2ebedf711796cd55c4";
				System.out.println(url);
				Connection connect = Jsoup.connect(url);
				Map<String, String> header = new HashMap<String, String>();
				header.put("User-Agent", "  Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0");
				header.put("Accept", "  text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				header.put("Accept-Language", "zh-cn,zh;q=0.5");
				header.put("Accept-Charset", "  GB2312,utf-8;q=0.7,*;q=0.7");
				header.put("Connection", "keep-alive");
				Connection data = connect.data(header);
				org.jsoup.nodes.Document document = connect.get();
				org.jsoup.nodes.Element ele = document.select("location").first();
				if (ele != null) {
					String location = ele.text();
					if (location != null) {
						user.set(User.longitude, location.split(",")[0]);
						user.set(User.latitude, location.split(",")[1]);
						user.update();
						System.err.println(
								user.get(User.user_id) + ":" + (i++) + " / " + list.size() + " update..." + location);
					}
				}
			}
		}

		render("/sweeth/html/index.html");
	}

	// 缓存
	// @Before(CacheInterceptor.class)
	// @CacheName("sweetinfo")
	public void about() {
		render("/sweeth/html/about.html");
	}

	// 缓存
	/// @Before(CacheInterceptor.class)
	// @CacheName("sweetinfo")
	public void culture() {
		render("/sweeth/html/culture.html");
	}

	public void privates() {
		render("/sweeth/html/private.html");
	}

	/**
	 * 获取android apk
	 */
	public void uelives() {
		String imgPath = getPara("name");
		String fullPath = config.save_path_root + imgPath;
		renderFile(new File(fullPath));
	}

	// 百度编辑器保存的图片路径
	public void getImg() {
		String imgPath = getPara("imgpath");
		String path = config.ueditor_images_path + imgPath;
		renderFile(new File(path));
	}

	/**
	 * 定时执行
	 */
	public static void showTimer() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					List<User> users = User.dao.find(
							"select user_id,is_vip from user where is_vip=1 and vip_end_datetime <?",
							DateUtils.getCurrentDateTime());
					for (User user : users) {
						user.set(user.is_vip, 0).update();
					}
					try {
						Thread.currentThread().sleep(1000*60*5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}
	/**
	 * 定时执行修改超级明星
	 */
	public static void showStarTimer() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					List<User> users = User.dao.find(
							"select user_id,is_star from user where is_star=1 and star_end_datetime <?",
							DateUtils.getCurrentDateTime());
					for (User user : users) {
						user.set(user.is_star, 0).update();
					}
					try {
						Thread.currentThread().sleep(1000*60*5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}

	/**
	 * 定时执行
	 */
	public static void NoticeTimer() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// 后台自动推送功能，一段时间没上（3天），则推送手机短信
				List<User> send = User.dao.find(
						"select telephone from user where black_status=0 and TIMESTAMPDIFF(Day,last_login_time,?)=3",
						DateUtils.getCurrentDateTime());
				for (User user : send) {
					String telephone = user.getStr(User.telephone);
					MessageUtils.sendNotice(telephone);
				}
			}
		};
		Timer timer = new Timer();
		long delay = 0;
		int period = 1000 * 60 * 60 * 24 * 3;
		timer.schedule(task, delay, period);
	}

	/**
	 * 定时执行
	 */
	public static void NoticeVipTimer() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// 后台自动推送功能，vip超过3天推送，则推送手机短信
				List<User> send = User.dao.find(
						"select user_id,telephone from user where black_status=0 and is_vip=1  and TIMESTAMPDIFF(Day,vip_end_datetime,?)=-3",
						DateUtils.getCurrentDateTime());
				for (User user : send) {
					int user_id = user.get(User.user_id);
					Notices notices = Notices.dao.findFirst("select * from notices where user_id=? and type=1",
							user_id);
					if (notices == null) {
						String telephone = user.getStr(User.telephone);
						MessageUtils.sendVipNotice(telephone);
						notices = new Notices();
						notices.set("user_id", user_id);
						notices.set("post_time", DateUtils.getCurrentDateTime());
						notices.set("type", 1);
						notices.save();
					}
				}
			}
		};
		Timer timer = new Timer();
		long delay = 0;
		int period = 1000 * 60 * 60 * 24 * 2;
		timer.schedule(task, delay, period);
	}

	public void addZan() {
		int is_zan = getParaToInt("is_zan");
		String news_id = getPara("news_id");
		String user_id = getPara("user_id");
		News news = News.dao.findById(news_id);
		int zan_num = news.get(news.zan_num);
		if (is_zan == 0) {
			ZanNews zNews = new ZanNews();
			boolean save = zNews.set(zNews.user_id, user_id).set(zNews.news_id, news_id)
					.set(zNews.post_time, DateUtils.getCurrentDateTime()).save();
			if (save) {
				news.set(news.zan_num, (zan_num + 1)).update();
			}
			setAttr("news_id", news_id);
			setAttr("user_id", user_id);
			setAttr("zan_num", (zan_num + 1));
			setAttr("is_zan", 1);
			setAttr("message", true);
			renderMultiJson("zan_num", "is_zan", "message", "news_id", "user_id");
		} else {
			ZanNews zanNews = ZanNews.dao
					.findFirst("select zan_news_id from zan_news where user_id=" + user_id + " and news_id=" + news_id);
			boolean delete = false;
			if (zanNews != null) {
				delete = zanNews.delete();
				setAttr("message", true);
			} else {
				setAttr("message", false);
			}
			if (delete) {
				news.set(news.zan_num, (zan_num - 1)).update();
			}
			setAttr("news_id", news_id);
			setAttr("user_id", user_id);
			setAttr("zan_num", (zan_num - 1));
			setAttr("is_zan", 0);
			renderMultiJson("zan_num", "is_zan", "message", "news_id", "user_id");
		}
	}
}
