/**
 * 
 */
package com.quark.app.controller;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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
import com.quark.model.extend.CellPhone;
import com.quark.model.extend.Collection;
import com.quark.model.extend.LoveYu;
import com.quark.model.extend.Search;
import com.quark.model.extend.Sweet;
import com.quark.model.extend.Tokens;
import com.quark.model.extend.User;
import com.quark.model.extend.UserTag;
import com.quark.utils.DateUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * @author cluo
 *
 */
public class Home extends Controller implements Serializable {

	@Author("cluo")
	@Rp("主页")
	@Explaination(info = "主页甜心宝贝列表")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@URLParam(defaultValue = "1", explain = Value.Infer, type = Type.String, name = "pn")
	@URLParam(defaultValue = "1-附近，2-线上，3-新人", explain = Value.Infer, type = Type.String, name = "type")
	@URLParam(defaultValue = "5", explain = Value.Infer, type = Type.String, name = "page_size")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = "latitude")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = "longitude")
	// 返回信息
	// page property
	@ReturnOutlet(name = "SweetsResponse{SweetsResult:sweets:pageNumber}", remarks = "page number", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "SweetsResponse{SweetsResult:sweets:pageSize}", remarks = "result amount of this page", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "SweetsResponse{SweetsResult:sweets:totalPage}", remarks = "total page", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "SweetsResponse{SweetsResult:sweets:totalRow}", remarks = "total row", dataType = DataType.Int, defaultValue = "")
	//
	@ReturnDBParam(name = "SweetsResponse{SweetsResult:sweets:list[sweet:$]}", column = User.user_id)
	@ReturnDBParam(name = "SweetsResponse{SweetsResult:sweets:list[sweet:$]}", column = User.telephone)
	@ReturnDBParam(name = "SweetsResponse{SweetsResult:sweets:list[sweet:$]}", column = User.image_01)
	@ReturnDBParam(name = "SweetsResponse{SweetsResult:sweets:list[sweet:$]}", column = User.nickname)
	@ReturnDBParam(name = "SweetsResponse{SweetsResult:sweets:list[sweet:$]}", column = User.heart)
	@ReturnDBParam(name = "SweetsResponse{SweetsResult:sweets:list[sweet:$]}", column = User.job)
	@ReturnOutlet(name = "SweetsResponse{SweetsResult:sweets:list[sweet:age]}", remarks = "0岁", dataType = DataType.String, defaultValue = "")
	@ReturnDBParam(name = "SweetsResponse{SweetsResult:sweets:list[sweet:$]}", column = User.height)
	@ReturnDBParam(name = "SweetsResponse{SweetsResult:sweets:list[sweet:$]}", column = User.city)
	@ReturnDBParam(name = "SweetsResponse{SweetsResult:sweets:list[sweet:$]}", column = User.is_vip)
	@ReturnDBParam(name = "SweetsResponse{SweetsResult:sweets:list[sweet:$]}", column = User.sex)
	@ReturnOutlet(name = "SweetsResponse{SweetsResult:sweets:list[sweet:loved_count]}", remarks = "被爱人数", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "SweetsResponse{SweetsResult:sweets:list[sweet:active_time]}", remarks = "活跃时间", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "SweetsResponse{status}", remarks = "1-操作成功", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "SweetsResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	// 缓存
	public void sweets() {
		try {
			int pn = getParaToInt("pn", 1);
			int page_size = getParaToInt("page_size", 5);
			String token = getPara("token");
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("SweetsResponse", response2);
				renderMultiJson("SweetsResponse");
				return;
			}	
			int status = 0;   // 0-冻结，1-正常
			String message = "";
			int setting_telecontact = 0;   	// 提醒：屏蔽手机联系人【0-关-1开】
			int taste = 0;// 0-美女
			String user_id = AppToken.getUserId(token, this);
			User user = User.dao.findById(user_id);
			String user_latitude = getPara("latitude", "30.344");
			String user_longitude = getPara("longitude", "120.00");
			if (user != null) {
				taste = user.get("taste");
				setting_telecontact = user.get(user.setting_telecontact);
				user.set(user.last_login_time, DateUtils.getCurrentDateTime()).update();
			}
			if(user_latitude != null && user_longitude!= null) {
				user.set(User.latitude, user_latitude).set(User.longitude, user_longitude).update();
			}
			String filter_sql = " setting_emotion=1 and status=1 and black_status=0 ";
			if (setting_telecontact == 10) {
				List<CellPhone> celPhones = CellPhone.dao
						.find("select telephone from cell_phone where user_id=" + user_id);
				String strC = "";
				for (int i = 0; i < celPhones.size(); i++) {
					String cellPhone = celPhones.get(i).getStr("telephone");
					User user2 = User.dao.findFirst(
							"select user_id from user where " + filter_sql + " and user_id!=? and telephone=?", user_id,
							cellPhone);
					if (user2 != null) {
						int user_id2 = user2.get("user_id");
						if (i == (celPhones.size() - 1)) {
							strC += String.valueOf(user_id2);
						} else {
							strC += String.valueOf(user_id2) + ",";
						}
					}
				}
				if (strC.length() > 0) {
					strC = strC.substring(0, strC.length() - 1);
				}
				if (celPhones.size() == 0 || strC.equals("")) {
					strC = "0";
				}
				filter_sql = filter_sql + " and user_id not in(" + strC + ") ";
			}
			 Page<User> userPage = null;
			int type = getParaToInt("type",1);
			if(type==1){
				userPage = User.dao.paginate(pn, page_size,
					"select regist_date,heart,user_id,telephone,sex,image_01,nickname,job,birthday,height,city,is_vip,last_login_time,round(6378.138*2*asin(sqrt(pow(sin( (latitude*pi()/180-"
								+ user_latitude + "*pi()/180)/2),2)+cos(latitude*pi()/180)*cos("
								+ user_latitude + "*pi()/180)* pow(sin( (longitude*pi()/180-" + user_longitude
								+ "*pi()/180)/2),2)))*1000) as distance",
					"from user where " + filter_sql
							+ " and sex=? and user_id!=? and is_set_heart=1 order by distance asc,last_login_time desc,hot desc",
					taste, user_id);
			}
			if(type==2){
				userPage = User.dao.paginate(pn, page_size,
					"select regist_date,heart,user_id,telephone,sex,image_01,nickname,job,birthday,height,city,is_vip,last_login_time,round(6378.138*2*asin(sqrt(pow(sin( (latitude*pi()/180-"
								+ user_latitude + "*pi()/180)/2),2)+cos(latitude*pi()/180)*cos("
								+ user_latitude + "*pi()/180)* pow(sin( (longitude*pi()/180-" + user_longitude
								+ "*pi()/180)/2),2)))*1000) as distance",
					"from user where " + filter_sql
							+ " and sex=? and user_id!=? and is_set_heart=1 and TIMESTAMPDIFF(MINUTE,last_login_time,'"+DateUtils.getCurrentDateTime()+"')<30 order by distance asc,last_login_time desc,hot desc",
					taste, user_id);
			}
			if(type==3){
				userPage = User.dao.paginate(pn, page_size,
					"select regist_date,heart,user_id,telephone,sex,image_01,nickname,job,birthday,height,city,is_vip,last_login_time,round(6378.138*2*asin(sqrt(pow(sin( (latitude*pi()/180-"
								+ user_latitude + "*pi()/180)/2),2)+cos(latitude*pi()/180)*cos("
								+ user_latitude + "*pi()/180)* pow(sin( (longitude*pi()/180-" + user_longitude
								+ "*pi()/180)/2),2)))*1000) as distance",
					"from user where " + filter_sql
							+ " and sex=? and user_id!=? and is_set_heart=1 and TIMESTAMPDIFF(DAY,regist_time,'"+DateUtils.getCurrentDateTime()+"')< 60 order by regist_time desc,distance asc,last_login_time desc,hot desc",
					taste, user_id);
			}
			List<User> userList = userPage.getList();
			for (User user2 : userList) {
				int user_id2 = user2.get("user_id");
				// 出生年月日
				Date birthday = user2.getDate("birthday");
				int age_int = 0;
				if (birthday != null) {
					String age_date = user2.getDate("birthday").toString();
					if (!age_date.equals("") && age_date != null) {
						age_int = DateUtils.getCurrentAgeByBirthdate(age_date);
					}
				}
				user2.put("age", age_int + "岁");
				List<Collection> collections = Collection.dao
						.find("select collection_id from collection where collection_user_id=" + user_id2);
				user2.put("loved_count", collections.size());
				// 什么时候登录
				String last_login_time = user2.getTimestamp("last_login_time").toString();
				
				String current_time = DateUtils.getCurrentDateTime();
				String active_time = DateUtils.getActiveTime(last_login_time, current_time);
				if(type==1){
					double distance = user2.getDouble("distance");
					double d = Math.ceil((distance/1000));
					user2.put("active_time",String.valueOf(d).replace(".0", "")+"公里");
					//user2.put("active_time", active_time);
					System.out.println(user2.get("distance"));

				}
				else if(type==3){
					String regist_date = user2.getDate("regist_date").toString();
					int days = DateUtils.daysBetween(regist_date, DateUtils.getCurrentDate());
					if(days < 1) {
						user2.put("active_time","今天");
					}else {
						user2.put("active_time", days+"天前");
					}
				}else{
					user2.put("active_time", active_time);
				}
				// is like
				Collection collection = Collection.dao.findFirst(
						"select collection_id from collection where user_id=? and collection_user_id=?", user_id,
						user_id2);
				if (collection == null) {
					user2.put("is_like", 0);
				} else {
					user2.put("is_like", 1);
				}

			}
			ResponseValues responseValues = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			responseValues.put("status", 1);
			responseValues.put("code", 200);
			HashMap<String, Object> map = new HashMap<String, Object>(); 
					map.put("sweets", userPage);
			responseValues.put("Result", map);
			responseValues.put("message", "返回成功");
			setAttr("SweetsResponse", responseValues);
			renderMultiJson("SweetsResponse");
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
			AppData.analyze("Home/sweets", "主页甜心宝贝列表", this);
		}
	}

	@Author("cluo")
	@Rp("搜索")
	@Explaination(info = "搜索列表,2.0版本")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "", explain = "距离，离我最近", type = Type.String, name = "distance")
	@URLParam(defaultValue = "", explain = "活跃，最近活跃", type = Type.String, name = "last_login_time")
	//@URLParam(defaultValue = "", explain = "距离，默认为不限", type = Type.String, name = "distance_to")
	@URLParam(defaultValue = "", explain = "性别，默认则不限", type = Type.String, name = "sex")
	@URLParam(defaultValue = "", explain = "城市，默认则不限", type = Type.String, name = "city")
	@URLParam(defaultValue = "", explain = "视频认证，默认则不限", type = Type.String, name = "is_video")
	@URLParam(defaultValue = "", explain = "从-体重，默认则不限", type = Type.String, name = "weight_from")
	@URLParam(defaultValue = "", explain = "从-体重，默认则不限", type = Type.String, name = "weight_from")
	@URLParam(defaultValue = "", explain = "从年龄，默认则不限", type = Type.String, name = "age_from")
	@URLParam(defaultValue = "", explain = "到年龄，默认则不限", type = Type.String, name = "age_to")
	@URLParam(defaultValue = "", explain = "从-身高，默认则不限", type = Type.String, name = "height_from")
	@URLParam(defaultValue = "", explain = "到-身高，默认则不限", type = Type.String, name = "height_to")
	//@URLParam(defaultValue = "", explain = "请求接口：http://sugarbaby.online/rp/index.html#p=个人资料", type = Type.String, name = "shape")
	@URLParam(defaultValue = "", explain = "学历，默认则不限", type = Type.String, name = "edu")
	@URLParam(defaultValue = "", explain = "体型，默认则不限", type = Type.String, name = "shape")
	@URLParam(defaultValue = "", explain = "满意部位，,默认则不限", type = Type.String, name = "part")
	//@URLParam(defaultValue = "{轻奢，高奢，中等}", explain = "幸福期望,默认则不限", type = Type.String, name = "hope")
	//@URLParam(defaultValue = "", explain = "月收入，跟个人中心设置一样,默认则不限", type = Type.String, name = "income")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String_NotRequired, name = User.latitude)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String_NotRequired, name = User.longitude)
	@URLParam(defaultValue = "1", explain = Value.Infer, type = Type.String, name = "pn")
	public void search() {
		int pn = getParaToInt("pn", 1);
		int page_size = getParaToInt("page_size", 10);
		String token = getPara("token");
		ResponseValues response = new ResponseValues(this, Thread.currentThread().getStackTrace()[1].getMethodName());
		if (!AppToken.check(token, this)) {
			// 登陆失败
			ResponseValues response2 = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			response2.put("message", "请重新登陆");
			response2.put("code", 405);
			setAttr("SearchSweetsResponse", response2);
			renderMultiJson("SearchSweetsResponse");
			return;
		}
		String city = getPara("city", "不限");
		String distance = getPara("distance", "不限");
		String is_video = getPara("is_video","不限");
		String weigth_from = getPara("weigth_from","不限");
		String weight_to = getPara("weight_to", "不限");
		String age_from = getPara("age_from", "不限");
		String age_to = getPara("age_to", "不限");
		String height_from = getPara("height_from", "不限");
		String height_to = getPara("height_to", "不限");
		String user_latitude = getPara("latitude", "30.344");
		String user_longitude = getPara("longitude", "120.00");
		String shape = getPara("shape", "不限");
		String part_id = getPara("part_id","不限");
		String sex = getPara("sex","不限");
		String edu = getPara("edu","不限");
		String part = getPara("part","不限");

		String filter_sql = " setting_emotion=1 and status=1 and black_status=0 ";
		
		/*int status = 0;
		String message = "";
		int setting_telecontact = 0;
		int taste = 0;// 0-甜心宝贝
		String user_id = AppToken.getUserId(token, this);
		User user = User.dao.findById(user_id);
		if (user != null) {
			int sex = user.get("sex");
			if(sex == 0){
				taste = 1;
			}else{
				taste = 0;
			}
			setting_telecontact = user.get(user.setting_telecontact);
		}*/
		filter_sql = filter_sql + " and sex=" + sex;
		if (!"不限".equals(city)) {
			filter_sql = filter_sql + " and city='" + city+"'";
		}
		//if (!"不限".equals(distance_from)) {
		//	filter_sql = filter_sql + " and distance >= " + distance_from;
		//}
		//if (!"不限".equals(distance_to)) {
		//	filter_sql = filter_sql + " and distance <= " + distance_from;
		//}
		if (!"不限".equals(is_video)) {
			filter_sql = filter_sql + " and is_video='" + is_video+"'";
		}
		if (!"不限".equals(weigth_from)) {
			filter_sql = filter_sql + " and weigth >= " + weigth_from;
		}
		if (!"不限".equals(weight_to)) {
			filter_sql = filter_sql + " and weigth <= " + weight_to;
		}
		if (!"不限".equals(height_from)) {
			filter_sql = filter_sql + " and height >= " + height_from;
		}
		if (!"不限".equals(height_to)) {
			filter_sql = filter_sql + " and height <= " + height_to;
		}
		if (!"不限".equals(age_from)) {
			filter_sql = filter_sql + " and TIMESTAMPDIFF(Year,birthday,'"+DateUtils.getCurrentDate()+"') >= " + age_from;
		}
		if (!"不限".equals(age_to)) {
			filter_sql = filter_sql + " and TIMESTAMPDIFF(Year,birthday,'"+DateUtils.getCurrentDate()+"') <= " + age_to;
		}
		if (!"不限".equals(edu)) {
			filter_sql = filter_sql + " and edu='" + edu+"'";
		}
		if (!"不限".equals(shape)) {
			filter_sql = filter_sql + " and shape='" + shape+"'";
		}
		System.out.println("filter_sqk:"+filter_sql);
		final Page<User> userPage = User.dao.paginate(pn, page_size,
				"select user_id,is_vip,telephone, image_01,nickname,sex,birthday,city,job,round(6378.138*2*asin(sqrt(pow(sin( (latitude*pi()/180-"
								+ user_latitude + "*pi()/180)/2),2)+cos(latitude*pi()/180)*cos("
								+ user_latitude + "*pi()/180)* pow(sin( (longitude*pi()/180-" + user_longitude
								+ "*pi()/180)/2),2)))*1000) as distance ",
				"from user where " + filter_sql + "  order by is_set_heart desc,hot desc,sweet desc,regist_time desc ");
		List<User> userList = userPage.getList();
		for (User user2 : userList) {
			int user_id2 = user2.get("user_id");
			// 出生年月日
			Date birthday = user2.getDate("birthday");
			int age_int = 0;
			if (birthday != null) {
				String age_date = user2.getDate("birthday").toString();
				if (!age_date.equals("") && age_date != null) {
					age_int = DateUtils.getCurrentAgeByBirthdate(age_date);
				}
			}
			user2.put("age", age_int + "岁");
		}

		ResponseValues responseValues = new ResponseValues(this,
				Thread.currentThread().getStackTrace()[1].getMethodName());
		responseValues.put("status", 1);
		responseValues.put("code", 200);
		responseValues.put("Result", new HashMap<String, Object>() {
			{
				put("searchSweets", userPage);
			}
		});
		responseValues.put("message", "返回成功");
		setAttr("SearchSweetsResponse", responseValues);
		renderMultiJson("SearchSweetsResponse");

	}

	@Author("cluo")
	@Rp("搜索")
	@Explaination(info = "搜索列表,1.0版本，废弃")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "{0、1}", explain = "1-在线，0-不在线", type = Type.Int, name = "is_online")
	@URLParam(defaultValue = "{0、1}", explain = Value.Infer, type = Type.Int, name = User.is_vip)
	@URLParam(defaultValue = "{0、1}", explain = Value.Infer, type = Type.Int, name = User.setting_freedate)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String_NotRequired, name = User.province)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String_NotRequired, name = User.city)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String_NotRequired, name = User.latitude)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String_NotRequired, name = User.longitude)

	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@URLParam(defaultValue = "1", explain = Value.Infer, type = Type.String, name = "pn")
	@URLParam(defaultValue = "5", explain = Value.Infer, type = Type.String, name = "page_size")
	// 返回信息
	// page property
	@ReturnOutlet(name = "SearchSweetsResponse{SearchSweetsResult:searchSweets:pageNumber}", remarks = "page number", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "SearchSweetsResponse{SearchSweetsResult:searchSweets:pageSize}", remarks = "result amount of this page", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "SearchSweetsResponse{SearchSweetsResult:searchSweets:totalPage}", remarks = "total page", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "SearchSweetsResponse{SearchSweetsResult:searchSweets:totalRow}", remarks = "total row", dataType = DataType.Int, defaultValue = "")
	//
	@ReturnDBParam(name = "SearchSweetsResponse{SearchSweetsResult:searchSweets:list[LikeUser:$]}", column = User.user_id)
	@ReturnDBParam(name = "SearchSweetsResponse{SearchSweetsResult:searchSweets:list[LikeUser:$]}", column = User.telephone)
	@ReturnDBParam(name = "SearchSweetsResponse{SearchSweetsResult:searchSweets:list[LikeUser:$]}", column = User.image_01)
	@ReturnDBParam(name = "SearchSweetsResponse{SearchSweetsResult:searchSweets:list[LikeUser:$]}", column = User.nickname)
	@ReturnDBParam(name = "SearchSweetsResponse{SearchSweetsResult:searchSweets:list[LikeUser:$]}", column = User.sex)
	@ReturnDBParam(name = "SearchSweetsResponse{SearchSweetsResult:searchSweets:list[LikeUser:$]}", column = User.job)
	@ReturnOutlet(name = "SearchSweetsResponse{SearchSweetsResult:searchSweets:list[LikeUser:age]}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnDBParam(name = "SearchSweetsResponse{SearchSweetsResult:searchSweets:list[LikeUser:$]}", column = User.city)

	@ReturnOutlet(name = "SearchSweetsResponse{SearchSweetsResult:searchSweets:list[searchSweet:active_time]}", remarks = "活跃时间", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "SearchSweetsResponse{status}", remarks = "1-操作成功", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "SearchSweetsResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void searchSweets() {
		try {
			int pn = getParaToInt("pn", 1);
			int page_size = getParaToInt("page_size", 5);
			String token = getPara("token");
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("SearchSweetsResponse", response2);
				renderMultiJson("SearchSweetsResponse");
				return;
			}
			String cellPhones = getPara("cellPhones", "");
			int is_online = getParaToInt("is_online", 1);
			int is_vip = getParaToInt("is_vip", 1);
			int setting_freedate = getParaToInt("setting_freedate", 1);
			String province = getPara("province", null);
			String city = getPara("city", null);
			String latitude = getPara("latitude", null);
			String longitude = getPara("longitude", null);

			String filter_sql = " setting_emotion=1 and status=1 and black_status=0 ";

			int status = 0;
			String message = "";
			int setting_telecontact = 0;
			int taste = 0;// 0-甜心宝贝
			String user_id = AppToken.getUserId(token, this);
			User user = User.dao.findById(user_id);
			if (user != null) {
				taste = user.get("taste");
				setting_telecontact = user.get(user.setting_telecontact);
			}
			filter_sql = filter_sql + " and sex=" + taste;
			// VIP
			if (is_vip == 1) {
				filter_sql = filter_sql + " and is_vip=" + is_vip;
				;
			}
			// 在线与否
			if (is_online == 1) {
				System.out.println("select user_id from user where " + filter_sql);
				List<User> users = User.dao.find("select user_id from user where " + filter_sql);
				String strC = "";
				for (int i = 0; i < users.size(); i++) {
					int user_id2 = users.get(i).get("user_id");
					String online_status = RongToken.checkOnline(String.valueOf(user_id2));
					if (online_status.equals("1")) {
						if (i == (users.size() - 1)) {
							strC += String.valueOf(user_id2);
						} else {
							strC += String.valueOf(user_id2) + ",";
						}
					}
				}
				if (users.size() == 0 || strC.equals("")) {
					strC = "0";
				} else {
					strC = strC.substring(0, strC.length() - 1);
				}
				filter_sql = filter_sql + " and user_id in(" + strC + ") ";
			}
			// 是否加入附近无偿约会，加入后出现在搜索中
			if (setting_freedate == 1) {
				// 10公里内
				if (latitude != null && longitude != null) {
					filter_sql = filter_sql + " and ( round(6378.138*2*asin(sqrt(pow(sin( (" + latitude
							+ "*pi()/180-latitude*pi()/180)/2),2)+cos(" + latitude
							+ "*pi()/180)*cos(latitude*pi()/180)* pow(sin( (" + longitude
							+ "*pi()/180-longitude*pi()/180)/2),2)))*1000) ) <10000 ";
				}
				filter_sql = filter_sql + " and setting_freedate=1 ";
			} else {
				// 地区
				if (province != null) {
					if (!province.equals("")) {
						filter_sql = filter_sql + " and province like'%" + province + "%'";
					}
				}
				if (city != null) {
					if (!city.equals("")) {
						filter_sql = filter_sql + " and city like'%" + city + "%'";
					}
				}
			}
			/**
			 * 屏蔽手机联系人
			 */
			if (setting_telecontact == 1) {
				List<CellPhone> celPhones = CellPhone.dao
						.find("select telephone from cell_phone where user_id=" + user_id);
				String strC = "";
				for (int i = 0; i < celPhones.size(); i++) {
					String cellPhone = celPhones.get(i).getStr("telephone");
					User user2 = User.dao.findFirst(
							"select user_id from user where " + filter_sql + " and user_id!=? and telephone=?", user_id,
							cellPhone);
					if (user2 != null) {
						int user_id2 = user2.get("user_id");
						if (i == (celPhones.size() - 1)) {
							strC += String.valueOf(user_id2);
						} else {
							strC += String.valueOf(user_id2) + ",";
						}
					}
				}
				if (strC.length() > 0) {
					strC = strC.substring(0, strC.length() - 1);
				}
				if (celPhones.size() == 0 || strC.equals("")) {
					strC = "0";
				}
				filter_sql = filter_sql + " and user_id not in(" + strC + ") ";
			}
			final Page<User> userPage = User.dao.paginate(pn, page_size,
					"select user_id,telephone, image_01,nickname,sex,birthday,city,job ", "from user where "
							+ filter_sql + "  order by is_set_heart desc,hot desc,sweet desc,regist_time desc ");
			List<User> userList = userPage.getList();
			for (User user2 : userList) {
				int user_id2 = user2.get("user_id");
				// 出生年月日
				Date birthday = user2.getDate("birthday");
				int age_int = 0;
				if (birthday != null) {
					String age_date = user2.getDate("birthday").toString();
					if (!age_date.equals("") && age_date != null) {
						age_int = DateUtils.getCurrentAgeByBirthdate(age_date);
					}
				}
				user2.put("age", age_int + "岁");
			}
			// 记录搜索
			Search search = new Search();
			search.set(search.user_id, user_id).set(search.isonline, is_online).set(search.is_vip, is_vip)
					.set(search.province, province).set(search.city, city)
					.set(search.post_time, DateUtils.getCurrentDateTime()).save();

			ResponseValues responseValues = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			responseValues.put("status", 1);
			responseValues.put("code", 200);
			responseValues.put("Result", new HashMap<String, Object>() {
				{
					put("searchSweets", userPage);
				}
			});
			responseValues.put("message", "返回成功");
			setAttr("SearchSweetsResponse", responseValues);
			renderMultiJson("SearchSweetsResponse");
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
			AppData.analyze("Home/searchSweets", "搜索列表", this);
		}
	}

	@Author("cluo")
	@Rp("详情")
	@Explaination(info = "甜心详情")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "{select user_id from user}", explain = Value.Infer, type = Type.String, name = User.user_id)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.user_id)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.image_01)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.image_02)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.image_03)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.image_04)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.image_05)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.image_06)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.is_vip)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.nickname)
	@ReturnOutlet(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:loved_count}", remarks = "0对他心动", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:age}", remarks = "0岁", dataType = DataType.String, defaultValue = "")
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.province)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.city)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.job)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.heart)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.height)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.income)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.edu)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.shape)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.marry)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.drink)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.smoke)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.regist_date)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.last_login_time)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = User.black_status)
	@ReturnDBParam(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:$}", column = Collection.collection_id)
	@ReturnOutlet(name = "SweetInfoResponse{SweetInfoResult:SweetInfo:is_like}", remarks = "0-还没喜欢，1-喜欢", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "SweetInfoResponse{login_user_vip}", remarks = "会员级别：0-普通会员，1-高级会员", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "SweetInfoResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "SweetInfoResponse{status}", remarks = "1-操作成功，0-失败", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "SweetInfoResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	// 缓存
	public void sweetInfo() {
		try {
			String token = getPara("token");
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("SweetInfoResponse", response2);
				renderMultiJson("SweetInfoResponse");
				return;
			}
			int status = 0;
			String message = "";
			String user_id = AppToken.getUserId(token, this);
			int sweet_user_id = getParaToInt("user_id");
			final User user2 = User.dao.findFirst(
					"select regist_date,last_login_time,user_id,image_01,image_02,image_03,image_04,image_05,image_06,is_vip,nickname,sex,birthday,city,job,heart,province,"
							+ " height,income,edu,shape,marry,drink,smoke,hot,black_status from user where user_id =?",
					sweet_user_id);
			int sex = 0;
			if (user2 != null) {
				sex = user2.get("sex");
				// 热度加1
				int hot = user2.get("hot");
				user2.set(user2.hot, hot + 1).update();
				List<Collection> collections = Collection.dao
						.find("select collection_id from collection where collection_user_id=" + sweet_user_id);
				user2.put("loved_count", collections.size());
				// 出生年月日
				Date birthday = user2.getDate("birthday");
				int age_int = 0;
				if (birthday != null) {
					String age_date = user2.getDate("birthday").toString();
					if (!age_date.equals("") && age_date != null) {
						age_int = DateUtils.getCurrentAgeByBirthdate(age_date);
					}
				}
				user2.put("age", age_int + "岁");
				Collection collection = Collection.dao.findFirst("select collection_id from collection where user_id="
						+ user_id + " and collection_user_id=" + sweet_user_id);
				int collection_id = 0;
				int is_like = 0;
				if (collection != null) {
					is_like = 1;
					collection_id = collection.get(collection.collection_id);
				}
				user2.put("collection_id", collection_id);
				user2.put("is_like", is_like);
			}
			final List<UserTag> userTags = UserTag.dao
					.find("select tag from user_tag where user_id=" + sweet_user_id + " and type=" + sex);
			// 记录浏览
			Browse browse = new Browse();
			browse.set(browse.user_id, user_id).set(browse.browse_user_id, sweet_user_id)
					.set(browse.post_time, DateUtils.getCurrentDateTime())
					.set(browse.post_date, DateUtils.getCurrentDate()).save();
			// 登陆者信息
			User login_user = User.dao.findById(user_id);
			int login_user_vip = login_user.get(login_user.is_vip);
			response.put("login_user_vip", login_user_vip);
			response.put("message", message);
			response.put("status", status);
			response.put("code", 200);
			response.put("Result", new HashMap<String, Object>() {
				{
					put("SweetInfo", user2);
					put("UserTag", userTags);
				}
			});
			setAttr("SweetInfoResponse", response);
			renderMultiJson("SweetInfoResponse");
			AppLog.info("", getRequest());
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("Home/sweetInfo", "甜心详情", this);
		}
	}

	@Author("cluo")
	@Rp("详情")
	@Explaination(info = "喜欢他")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = User.user_id)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "AddLove{collection_id}", remarks = "主键", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "AddLove{message}", remarks = "", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "AddLove{status}", remarks = "0-失败,1-操作成功", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "AddLove{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void addLove() {
		try {
			String token = getPara("token");
			String sweet_user_id = getPara("user_id");
			String message = "";
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("code", 405);
				response2.put("message", "请重新登陆");
				setAttr("AddLove", response2);
				renderMultiJson("AddLove");
				return;
			}
			String curr_user_id = AppToken.getUserId(token, this);
			ResponseValues response2 = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			if (sweet_user_id.equals(curr_user_id)) {
				response2.put("status", 0);
				response2.put("collection_id", "");
				message = "喜欢对象不能是自己";
			} else {
				List<Collection> cList = Collection.dao.find(
						"select collection_id from collection where user_id=? and collection_user_id=?", curr_user_id,
						sweet_user_id);
				if (cList.size() > 0) {
					response2.put("status", 0);
					User user = User.dao.findById(sweet_user_id);
					if (user != null) {
						int sex = user.get(user.sex);
						if (sex == 0) {
							message = "您已喜欢过她";
						} else {
							message = "您已喜欢过他";
						}
					} else {
						message = "您已喜欢过Ta";
					}

				} else {
					Collection collection = new Collection();
					boolean save = collection.set(collection.user_id, curr_user_id)
							.set(collection.collection_user_id, sweet_user_id)
							.set(collection.post_time, DateUtils.getCurrentDateTime()).save();
					if (save) {
						LoveYu loveYu = LoveYu.dao.findFirst("select love_yu from love_yu order by rand() limit 1");
						String yuyanString = "有人喜欢你，去看看吧";
						if (loveYu != null) {
							yuyanString = loveYu.getStr(loveYu.love_yu);
						}
						List<String> toIds = new ArrayList<String>();
						toIds.add(sweet_user_id);
						RongToken.publishMessage(curr_user_id, toIds, yuyanString);
						response2.put("status", 1);
						response2.put("collection_id", collection.get("collection_id"));
						message = "恭喜，收藏成功";
					} else {
						response2.put("status", 0);
						response2.put("collection_id", "");
						message = "抱歉，收藏失败";
					}
				}
			}
			response2.put("message", message);
			response2.put("code", 200);
			setAttr("AddLove", response2);
			renderMultiJson("AddLove");
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
			AppData.analyze("Home/addLove", "添加收藏", this);
		}
	}

	@Author("cluo")
	@Rp("详情")
	@Explaination(info = "取消喜欢")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Collection.collection_id)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "CancelLoveResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "CancelLoveResponse{status}", remarks = "1-操作成功,0-失败，2-不存在此人", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "CancelLoveResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	// 方法前加注解 methodname为方法名
	public void cancelLove() {
		try {
			String token = getPara("token");
			String collection_id = getPara("collection_id");
			String message = "";
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("code", 405);
				response2.put("message", "请重新登陆");
				setAttr("CancelLoveResponse", response2);
				renderMultiJson("CancelLoveResponse");
				return;
			}
			ResponseValues response2 = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			Collection collection = Collection.dao.findById(collection_id);
			if (collection == null) {
				message = "抱歉，不存在此人";
				response2.put("status", 2);
			} else {
				boolean delete = collection.delete();
				if (delete) {
					message = "恭喜，取消成功";
					response2.put("status", 1);
				} else {
					message = "抱歉，取消失败";
					response2.put("status", 0);
				}
			}
			response2.put("code", 200);
			response2.put("message", message);
			setAttr("CancelLoveResponse", response2);
			renderMultiJson("CancelLoveResponse");
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
			AppData.analyze("Home/cancelLove", "取消收藏", this);
		}
	}
}
