package com.quark.common;

import java.util.List;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.Const;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.Restful;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.bonecp.BoneCpPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.upload.UploadFile;
import com.jolbox.bonecp.BoneCP;
import com.quark.admin.controller.AdminUsers;
import com.quark.admin.controller.Files;
import com.quark.admin.controller.JavaFiles;
import com.quark.admin.controller.Login;
import com.quark.admin.controller.SwiftFiles;
import com.quark.admin.controller.Tongji;
import com.quark.admin.controller.api;
import com.quark.admin.controller.rp;
import com.quark.handler.H5Handler;
import com.quark.handler.RpHandler;
import com.quark.model.*;
import com.quark.model.extend.AdminUser;
import com.quark.model.extend.Applogs;
import com.quark.model.extend.Audit;
import com.quark.model.extend.BlackList;
import com.quark.model.extend.Browse;
import com.quark.model.extend.CarCategroy;
import com.quark.model.extend.CarClassify;
import com.quark.model.extend.CellPhone;
import com.quark.model.extend.Certification;
import com.quark.model.extend.Charge;
import com.quark.model.extend.ChargeGold;
import com.quark.model.extend.Collection;
import com.quark.model.extend.Constellation;
import com.quark.model.extend.Gift;
import com.quark.model.extend.GoldPrice;
import com.quark.model.extend.Interest;
import com.quark.model.extend.Job;
import com.quark.model.extend.LikeDate;
import com.quark.model.extend.LoveYu;
import com.quark.model.extend.MyGift;
import com.quark.model.extend.News;
import com.quark.model.extend.Notices;
import com.quark.model.extend.Part;
import com.quark.model.extend.Price;
import com.quark.model.extend.ReportBean;
import com.quark.model.extend.Search;
import com.quark.model.extend.SloveLanguage;
import com.quark.model.extend.Sweet;
import com.quark.model.extend.Tag;
import com.quark.model.extend.Tokens;
import com.quark.model.extend.TongjiCharge;
import com.quark.model.extend.TongjiRegist;
import com.quark.model.extend.User;
import com.quark.model.extend.UserIncome;
import com.quark.model.extend.UserOld;
import com.quark.model.extend.UserShape;
import com.quark.model.extend.UserTag;
import com.quark.model.extend.ZanNews;
import com.quark.utils.PackageUtils;

/**
 * API引导式配置
 */
public class config extends JFinalConfig {

	/**
	 * pay host
	 */
	public static final String server_web = "http://app.sugarbaby.online";
	//public static final String server_ip = "114.215.102.155";
	public static final String server_ip = "120.24.16.244";
	//public static final String pay_notify_host = "114.215.102.155:9001";
	public static final String pay_notify_host = "120.24.16.244";
	public static final String controllers = "com.quark.app.controller";

	//public static final String videos_path = "c:/videos/";
	public static final String videos_path = "C:/videos/";

	//public static final String images_path = "c:/images/";
	public static final String images_path = "C:/images/";
	public static final String ueditor_images_path = "C:/images/ueditor/";
	//public static final String ueditor_images_path = "c:/images/ueditor/";
	public static final String relative_path = "/upload/";
	public static final String save_path_root = PathKit.getWebRootPath() + relative_path;

	public static final String project = "";
	public static final int app_page_size = Controller.PAGE_SIZE;
	public static final String save_path = images_path;// PathKit.getWebRootPath()+ relative_path;
	/**
	 * 网易免费企业邮箱 pop：pop.ym.163.com smtp：smtp.ym.163.com
	 * 
	 * 网易收费企业邮箱 pop：pop.qiye.163.com smtp：smtp.qiye.163.com
	 */
	public static final String email_smtp = "smtp.ym.163.com";
	public static final String email_username = "no-reply@uelives.com";
	public static final String email_password = "youyi123";
	/**
	 * iPhone自动升级id
	 */
	public static final String itunes_apple_id = "1074111242";
	public static final String hunxin_admin_id = "1";
	/**
	 * 数据库用户名及密码
	 */
	public static String db_username = "";
	public static String db_password = "";
	public static final String backup = PathKit.getWebRootPath() + "/backup";
	public static C3p0Plugin boneCpPlugin;
	/**
	 * 配置常量
	 */
	public static boolean devMode = false;
	/**
	 * 配置常量
	 */
	/**
	 * 甜心传媒对应的user_id
	 * 线上服务对应的是97
	 */
	public static final String sweet_user_id = "97";
	

	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用getProperty(..)获取值
		loadPropertyFile("config.txt");
		me.setDevMode(getPropertyToBoolean("devMode", false));
		// 过滤以^拼接的请求
		me.setUrlParaSeparator("^");
		// 文件上传默认10M,此处设置为最大1000M
		me.setMaxPostSize(100 * Const.DEFAULT_MAX_POST_SIZE);
	}

	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		/**
		 * 初始化
		 */
		me.add("/",Init.class);
		me.add("/api", api.class);
		me.add("/rp", rp.class);
		me.add("/java", JavaFiles.class);
		me.add("/swift", SwiftFiles.class);
		// 统一图片路径
		me.add("/files", Files.class);
		/**
		 * app功能
		 */
		List<Class> app_clazz = PackageUtils.getClasses("com.quark.app.controller");
		for (Class class1 : app_clazz) {
			Class<Controller> controller = class1;
			if (!"".equals(class1.getSimpleName()))
				me.add("/app/" + class1.getSimpleName(), controller);
		}
		/**
		 * end
		 */
		/**
		 * web admin功能
		 */
		List<Class> admin_clazz = PackageUtils
				.getClasses("com.quark.admin.controller");
		for (Class class2 : admin_clazz) {
			Class<Controller> controller = class2;
			me.add("/admin/" + class2.getSimpleName(), controller);
		}
		/**
		 * end
		 */
		me.add("/Tongji", Tongji.class);
		/**
		 * end
		 */
	}

	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		// 配置BoneCp数据库连接池插件
		db_username = getProperty("user").trim();
		db_password = getProperty("password").trim();
		boneCpPlugin = new C3p0Plugin(getProperty("jdbcUrl"), db_username, db_password);
		me.add(boneCpPlugin);
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(boneCpPlugin);
		arp.setShowSql(false);
		me.add(arp);
		//配置Cache缓存
		//me.add(new EhCachePlugin());
		//me.add(new EhCachePlugin(save_path_root  + "ehcache.xml")); 
		/**
		 * tables
		 */
		arp.addMapping("admin_user", AdminUser.class);
		arp.addMapping("applogs","log_id", Applogs.class);
		arp.addMapping("black_list","black_list_id", BlackList.class);
		arp.addMapping("browse","browse_id", Browse.class);
		arp.addMapping("charge","charge_id", Charge.class);
		arp.addMapping("search","search_id", Search.class);
		arp.addMapping("sweet","sweet_id", Sweet.class);
		arp.addMapping("tag","tag_id", Tag.class);
		arp.addMapping("tokens","token_id", Tokens.class);
		arp.addMapping("user","user_id", User.class);
		arp.addMapping("user_income","user_income_id", UserIncome.class);
		arp.addMapping("user_shape","user_shape_id", UserShape.class);
		arp.addMapping("user_tag","user_tag_id", UserTag.class);
		arp.addMapping("job","job_id", Job.class);
		arp.addMapping("collection","collection_id", Collection.class);
		arp.addMapping("price","price_id", Price.class);
		arp.addMapping("user_old","user_old_id", UserOld.class);
		arp.addMapping("love_yu","love_yu_id", LoveYu.class);
		arp.addMapping("report_bean","report_bean_id", ReportBean.class);
		arp.addMapping("news","news_id", News.class);
		arp.addMapping("zan_news","zan_news_id", ZanNews.class);
		arp.addMapping("slove_language","slove_language_id", SloveLanguage.class);
		arp.addMapping("tongji", com.quark.model.extend.Tongji.class);
		arp.addMapping("tongji_regist",TongjiRegist.class);
		arp.addMapping("tongji_charge", TongjiCharge.class);
		//新增
		arp.addMapping("gift","gift_id", Gift.class);
		arp.addMapping("gold_price","gold_price_id", GoldPrice.class);
		arp.addMapping("my_gift","my_gift_id", MyGift.class);
		arp.addMapping("charge_gold","charge_gold_id", ChargeGold.class);
		arp.addMapping("cell_phone","cell_phone_id", CellPhone.class);
		arp.addMapping("notices", Notices.class);
		arp.addMapping("like_part","part_id", Part.class);
		arp.addMapping("interest", "interest_id",Interest.class);
		arp.addMapping("like_date", "date_id", LikeDate.class);
		arp.addMapping("constellation", "star_id", Constellation.class );
		arp.addMapping("car_categroy", CarCategroy.class);
		arp.addMapping("car_classify", CarClassify.class);
		arp.addMapping("certification", "id",Certification.class);
		arp.addMapping("certification", "id",Audit.class);

		/**
		 * view
		 */
	}

	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
	}

	/**
	 * 配置处理器 处理伪静态请求
	 */
	public void configHandler(Handlers me) {
		me.add(new RpHandler());
	}
}
