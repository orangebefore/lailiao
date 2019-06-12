package com.quark.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jfinal.upload.UploadFile;
import com.jfinal.core.Controller;
import com.quark.admin.controller.InvitationPrice;
import com.quark.api.auto.bean.ResponseValues;
import com.quark.app.logs.AppLog;
import com.quark.common.AppData;
import com.quark.common.config;
import com.quark.interceptor.AppToken;
import com.quark.model.extend.Gift;
import com.quark.model.extend.InvitationPriceEntity;
import com.quark.model.extend.Invite;
import com.quark.model.extend.InviteCost;
import com.quark.model.extend.InviteTime;
import com.quark.model.extend.MyGift;
import com.quark.model.extend.TravelDays;
import com.quark.model.extend.TravelMode;
import com.quark.model.extend.User;
import com.quark.utils.DateUtils;
import com.quark.utils.FileUtils;

public class InviteController extends Controller{

	
	public void list() {
		try {
			String token = getPara("token");
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("InviteResponse", response2);
				renderMultiJson("InviteResponse");
				return;
			}
			int status = 0;
			String filter_sql = " where ";
			String latitude = getPara("latitude", "30.344");
			String longitude = getPara("longitude", "120.00");
			String type = getPara("invite_type_id");
			if (type == "" || type == null) {
				filter_sql = filter_sql+" 1=1 ";
			}else {
				filter_sql = filter_sql+"invite_type_id ="+type;
			}
			String message="";
			String user_id = AppToken.getUserId(token, this);
			User user = User.dao.findById(user_id);
			final List<Invite> iList = Invite.dao.find("SELECT u.nickname,u.city,u.image_01,u.job,u.sex,"
					+ "u.height,i.invite_content,i.cost_id,i.invite_explain,i.is_top,i.invite_type_id,"
					+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN(("+latitude+"*PI()/180-u.latitude*PI()/180)/2),2)+COS("+latitude+"*PI()/180)*COS(u.latitude*PI()/180)*POW"
					+ "(SIN(("+longitude+"*PI()/180-u.longitude*PI()/180)/2),2)))*1000) AS distance ,DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(u.birthday)), '%Y')+0 AS age"
					+ " FROM `user` AS u INNER JOIN invite AS i ON u.`user_id` = i.`user_id` "+filter_sql+" and i.is_top = 0");
			final List<Invite> topList = Invite.dao.find("SELECT u.nickname,u.city,u.image_01,u.job,u.sex,"
					+ "u.height,i.invite_content,i.cost_id,i.invite_explain,i.is_top,i.invite_type_id,"
					+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN(("+latitude+"*PI()/180-u.latitude*PI()/180)/2),2)+COS("+latitude+"*PI()/180)*COS(u.latitude*PI()/180)*POW"
					+ "(SIN(("+longitude+"*PI()/180-u.longitude*PI()/180)/2),2)))*1000) AS distance ,DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(u.birthday)), '%Y')+0 AS age"
					+ " FROM `user` AS u INNER JOIN invite AS i ON u.`user_id` = i.`user_id` "+filter_sql+" and i.is_top = 1 ORDER BY i.`top_date` DESC LIMIT 0,3");
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			response.put("message", "");
			response.put("status", 1);
			response.put("code", 200);
			response.put("Result", new HashMap<String, Object>() {
				{
					put("list", iList);
					put("topList", topList);
				}
			});
			setAttr("InviteResponse", response);
			renderMultiJson("InviteResponse");
			AppLog.info(null, getRequest());
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("InviteController/list", "邀约列表", this);
		}
	}
	
	public void screenList() {
		try {
			boolean save = false;
			String message = "";
			String sex = getPara("sex");
			String sort = getPara("sort");
			int status = 0;
			String token = getPara("token");
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("InviteResponse", response2);
				renderMultiJson("InviteResponse");
				return;
			}
			String user_id = AppToken.getUserId(token, this);
			User user = User.dao.findById(user_id);
			String latitude =user.get("latitude");
			String longitude =user.get("longitude");
			String province = getPara("province");
			int is_video = getParaToInt("is_video");
			List<Invite> iList = new ArrayList<Invite>();
			if(sort.equals("new")) {
				iList = Invite.dao.find("SELECT u.nickname,u.city,u.image_01,u.job,"
						+ "u.birthday,u.height,i.invite_content,i.cost_id,i.invite_explain,u.sex,i.is_top,i.invite_type_id,"
						+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN(("+latitude+"*PI()/180-u.latitude*PI()/180)/2),2)+COS("+latitude+"*PI()/180)*COS(u.latitude*PI()/180)*POW"
						+ "(SIN(("+longitude+"*PI()/180-u.longitude*PI()/180)/2),2)))*1000) AS distance"
						+ " FROM `user` AS u INNER JOIN invite AS i ON u.`user_id` = i.`user_id` WHERE u.sex = "+sex+" AND i.invite_province = '"+province+"' AND u.is_video = "+is_video+" ORDER BY i.invite_id DESC");
			} else {
				iList = Invite.dao.find("SELECT u.nickname,u.city,u.image_01,u.job,"
						+ "u.birthday,u.height,i.invite_content,i.cost_id,i.invite_explain,u.sex,i.is_top,i.invite_type_id,"
						+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN(("+latitude+"*PI()/180-u.latitude*PI()/180)/2),2)+COS("+latitude+"*PI()/180)*COS(u.latitude*PI()/180)*POW"
						+ "(SIN(("+longitude+"*PI()/180-u.longitude*PI()/180)/2),2)))*1000) AS distance"
						+ " FROM `user` AS u INNER JOIN invite AS i ON u.`user_id` = i.`user_id` WHERE u.sex = "+sex+" AND i.invite_province = '"+province+"' AND u.is_video = "+is_video+" ORDER BY distance ASC");
			}
			final List<Invite> iList2 = iList;
			final List<Invite> topList = Invite.dao.find("SELECT u.nickname,u.city,u.image_01,u.job,u.sex,"
					+ "u.height,i.invite_content,i.cost_id,i.invite_explain,i.is_top,i.invite_type_id,"
					+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN(("+latitude+"*PI()/180-u.latitude*PI()/180)/2),2)+COS("+latitude+"*PI()/180)*COS(u.latitude*PI()/180)*POW"
					+ "(SIN(("+longitude+"*PI()/180-u.longitude*PI()/180)/2),2)))*1000) AS distance ,DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(u.birthday)), '%Y')+0 AS age"
					+ " FROM `user` AS u INNER JOIN invite AS i ON u.`user_id` = i.`user_id` and i.is_top = 1 ORDER BY i.`top_date` DESC LIMIT 0,3");
			
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			response.put("message", "");
			response.put("status", 1);
			response.put("code", 200);
			response.put("Result", new HashMap<String, Object>() {
				{
					put("list", iList2);
					put("topList", topList);
				}
			});
			setAttr("InviteResponse", response);
			renderMultiJson("InviteResponse");
			AppLog.info(null, getRequest());
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("InviteController/list", "筛选邀约列表", this);
		}
	}
	
	public void saveTravel() {
		try {
			boolean save = false;
			String message = "";
			UploadFile explain_url = getFile("explain_url",config.images_path);
			int cost_id = getParaToInt("cost_id");
			String token = getPara("token");
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("InviteResponse", response2);
				renderMultiJson("InviteResponse");
				return;
			}
			String user_id = AppToken.getUserId(token, this);
			int invite_sex = getParaToInt("invite_sex");
			String travel_days_id = getPara("travel_days_id");
			String invite_content = getPara("invite_content");
			String travel_mode_id = getPara("travel_mode_id");
			String is_equal_place = getPara("is_equal_place");
			String invite_explain = getPara("invite_explain");
			int time_id = getParaToInt("time_id");
			save = new Invite().set(Invite.explain_url, FileUtils.renameToFile(explain_url))
					.set(Invite.travel_days_id, travel_days_id)
					.set(Invite.travel_mode_id, travel_mode_id)
					.set(Invite.invite_content, invite_content)
					.set(Invite.invite_sex, invite_sex)
					.set(Invite.invite_explain, invite_explain)
					.set(Invite.is_equal_place, is_equal_place)
					.set(Invite.cost_id, cost_id)
					.set(Invite.time_id, time_id)
					.set(Invite.invite_type_id, 1)
					.set(Invite.user_id, user_id).save();
			ResponseValues responseValues = new ResponseValues(this, Thread.currentThread().getStackTrace()[1].getMethodName());
			if(save) {
				responseValues.put("status", 1);
				message = "发布成功";
			}else {
				responseValues.put("status", 0);
				message = "发布失败";
			}
			responseValues.put("message",message);
			responseValues.put("code", 200);
			setAttr("InviteResponse", responseValues);
			renderMultiJson("InviteResponse");
			render("/app/InviteController/list");
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
		}
	}
	

	public void saveFood() {
		try {
			boolean save = false;
			String message = "";
			UploadFile explain_url = getFile("explain_url",config.images_path);
			int cost_id = getParaToInt("cost_id");
			String token = getPara("token");
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("InviteResponse", response2);
				renderMultiJson("InviteResponse");
				return;
			}
			String user_id = AppToken.getUserId(token, this);
			String invite_province = getPara("invite_province");
			String invite_content = getPara("invite_content");
			String invite_city = getPara("invite_city");
			String invite_place = getPara("invite_place");
			int invite_sex = getParaToInt("invite_sex");
			String invite_explain = getPara("invite_explain");
			int invite_receive = getParaToInt("invite_receive");
			int time_id = getParaToInt("time_id");
			save = new Invite().set(Invite.explain_url, FileUtils.renameToFile(explain_url))
					.set(Invite.invite_province, invite_province)
					.set(Invite.invite_content, invite_content)
					.set(Invite.invite_city, invite_city)
					.set(Invite.invite_place, invite_place)
					.set(Invite.invite_sex, invite_sex)
					.set(Invite.invite_explain, invite_explain)
					.set(Invite.cost_id, cost_id)
					.set(Invite.invite_receive, invite_receive)
					.set(Invite.time_id, time_id)
					.set(Invite.invite_type_id, 2)
					.set(Invite.user_id, user_id).save();
			ResponseValues responseValues = new ResponseValues(this, Thread.currentThread().getStackTrace()[1].getMethodName());
			if(save) {
				responseValues.put("status", 1);
				message = "发布成功";
			}else {
				responseValues.put("status", 0);
				message = "发布失败";
			}
			responseValues.put("message",message);
			responseValues.put("code", 200);
			setAttr("InviteResponse", responseValues);
			renderMultiJson("InviteResponse");
			render("/app/InviteController/list");
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
		}
	}
	

	public void saveSing() {
		try {
			boolean save = false;
			String message = "";
			UploadFile explain_url = getFile("explain_url",config.images_path);
			int cost_id = getParaToInt("cost_id");
			String token = getPara("token");
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("InviteResponse", response2);
				renderMultiJson("InviteResponse");
				return;
			}
			String user_id = AppToken.getUserId(token, this);
			String invite_province = getPara("invite_province");
			String invite_city = getPara("invite_city");
			String invite_place = getPara("invite_place");
			int invite_sex = getParaToInt("invite_sex");
			String invite_explain = getPara("invite_explain");
			int invite_receive = getParaToInt("invite_receive");
			int time_id = getParaToInt("time_id");
			save = new Invite().set(Invite.explain_url, FileUtils.renameToFile(explain_url))
					.set(Invite.invite_province, invite_province)
					.set(Invite.invite_city, invite_city)
					.set(Invite.invite_place, invite_place)
					.set(Invite.invite_sex, invite_sex)
					.set(Invite.invite_explain, invite_explain)
					.set(Invite.cost_id, cost_id)
					.set(Invite.invite_receive, invite_receive)
					.set(Invite.time_id, time_id)
					.set(Invite.invite_type_id, 3)
					.set(Invite.user_id, user_id).save();
			ResponseValues responseValues = new ResponseValues(this, Thread.currentThread().getStackTrace()[1].getMethodName());
			if(save) {
				responseValues.put("status", 1);
				message = "发布成功";
			}else {
				responseValues.put("status", 0);
				message = "发布失败";
			}
			responseValues.put("message",message);
			responseValues.put("code", 200);
			setAttr("InviteResponse", responseValues);
			renderMultiJson("InviteResponse");
			render("/app/InviteController/list");
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
		}
	}
	
	
	public void findTime() {
		try {
			int type = getParaToInt("invite_type_id");
			String token = getPara("token");
			InviteTime inviteTime = new InviteTime();
			HashMap<String , List> map = new HashMap<String,List>();
			List<InviteTime>inviteTimeList = new ArrayList<InviteTime>();
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("InviteResponse", response2);
				renderMultiJson("InviteResponse");
				return;
			}
			if(type!=1) {
				inviteTimeList = inviteTime.dao.find("select time_name from invite_time limit 0,4");
				map.put("timeList", inviteTimeList);
			} else {
				List<InviteCost> costList = InviteCost.dao.find("select cost_name from invite_cost"); 
				List<TravelDays> daysList = TravelDays.dao.find("select travel_days_name from travel_days"); 
				List<TravelMode> modeList = TravelMode.dao.find("select travel_mode_name from travel_mode"); 
				inviteTimeList = inviteTime.find("select time_name from invite_time limit 3,4");
				map.put("costList", costList);
				map.put("daysList", daysList);
				map.put("timeList", inviteTimeList);
				map.put("modeList", modeList);
			}
			ResponseValues responseValues = new ResponseValues(this, Thread.currentThread().getStackTrace()[1].getMethodName());
			responseValues.put("code", 200);
			responseValues.put("List", map);
			setAttr("InviteResponse", responseValues);
			renderMultiJson("InviteResponse");
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
		}
	}
	
	
	public void saveMovie() {
		try {
			boolean save = false;
			String message = "";
			UploadFile explain_url = getFile("explain_url",config.images_path);
			int cost_id = getParaToInt("cost_id");
			String token = getPara("token");
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("InviteResponse", response2);
				renderMultiJson("InviteResponse");
				return;
			}
			String user_id = AppToken.getUserId(token, this);
			String invite_content = getPara("invite_content");
			String invite_province = getPara("invite_province");
			String invite_city = getPara("invite_city");
			String invite_place = getPara("invite_place");
			int invite_sex = getParaToInt("invite_sex");
			String invite_explain = getPara("invite_explain");
			int invite_receive = getParaToInt("invite_receive");
			int time_id = getParaToInt("time_id");
			save = new Invite().set(Invite.explain_url, FileUtils.renameToFile(explain_url))
					.set(Invite.invite_content, invite_content)
					.set(Invite.invite_province, invite_province)
					.set(Invite.invite_city, invite_city)
					.set(Invite.invite_place, invite_place)
					.set(Invite.invite_sex, invite_sex)
					.set(Invite.invite_explain, invite_explain)
					.set(Invite.cost_id, cost_id)
					.set(Invite.invite_receive, invite_receive)
					.set(Invite.time_id, time_id)
					.set(Invite.invite_type_id, 4)
					.set(Invite.user_id, user_id).save();
			ResponseValues responseValues = new ResponseValues(this, Thread.currentThread().getStackTrace()[1].getMethodName());
			if(save) {
				responseValues.put("status", 1);
				message = "发布成功";
			}else {
				responseValues.put("status", 0);
				message = "发布失败";
			}
			responseValues.put("message",message);
			responseValues.put("code", 200);
			setAttr("InviteResponse", responseValues);
			renderMultiJson("InviteResponse");
			render("/app/InviteController/list");
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
		}
	}
	

	public void saveMotion() {
		try {
			boolean save = false;
			String message = "";
			String token = getPara("token");
			UploadFile explain_url = getFile("explain_url",config.images_path);
			int cost_id = getParaToInt("cost_id");
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("InviteResponse", response2);
				renderMultiJson("InviteResponse");
				return;
			}	
			
			String user_id = AppToken.getUserId(token, this);
			String invite_content = getPara("invite_content");
			String invite_province = getPara("invite_province");
			String invite_city = getPara("invite_city");
			String invite_place = getPara("invite_place");
			int invite_sex = getParaToInt("invite_sex");
			String invite_explain = getPara("invite_explain");
			int is_carry_bestie = getParaToInt("is_carry_bestie");
			int invite_receive = getParaToInt("invite_receive");
			int time_id = getParaToInt("time_id");
			save = new Invite().set(Invite.explain_url, FileUtils.renameToFile(explain_url))
					.set(Invite.invite_content, invite_content)
					.set(Invite.invite_province, invite_province)
					.set(Invite.invite_city, invite_city)
					.set(Invite.invite_place, invite_place)
					.set(Invite.invite_sex, invite_sex)
					.set(Invite.invite_explain, invite_explain)
					.set(Invite.cost_id, cost_id)
					.set(Invite.invite_receive, invite_receive)
					.set(Invite.is_carry_bestie, is_carry_bestie)
					.set(Invite.time_id, time_id)
					.set(Invite.invite_type_id, 5)
					.set(Invite.user_id, user_id).save();
			ResponseValues responseValues = new ResponseValues(this, Thread.currentThread().getStackTrace()[1].getMethodName());
			if(save) {
				responseValues.put("status", 1);
				message = "发布成功";
			}else {
				responseValues.put("status", 0);
				message = "发布失败";
			}
			responseValues.put("message",message);
			responseValues.put("code", 200);
			setAttr("InviteResponse", responseValues);
			renderMultiJson("InviteResponse");
			render("/app/InviteController/list");
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
		}
	}
	

	public void saveLiterature() {
		try {
			boolean save = false;
			String message = "";
			UploadFile explain_url = getFile("explain_url",config.images_path);
			int cost_id = getParaToInt("cost_id");
			String token = getPara("token");
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("InviteResponse", response2);
				renderMultiJson("InviteResponse");
				return;
			}
			String user_id = AppToken.getUserId(token, this);
			String invite_content = getPara("invite_content");
			int invite_sex = getParaToInt("invite_sex");
			int invite_receive = getParaToInt("invite_receive");
			String invite_explain = getPara("invite_explain");
			String invite_place = getPara("invite_place");
			String invite_province = getPara("invite_province");
			String invite_city = getPara("invite_city");			
			int time_id = getParaToInt("time_id");
			save = new Invite().set(Invite.explain_url, FileUtils.renameToFile(explain_url))
					.set(Invite.invite_content, invite_content)
					.set(Invite.invite_province, invite_province)
					.set(Invite.invite_city, invite_city)
					.set(Invite.invite_place, invite_place)
					.set(Invite.invite_sex, invite_sex)
					.set(Invite.invite_explain, invite_explain)
					.set(Invite.cost_id, cost_id)
					.set(Invite.invite_receive, invite_receive)
					.set(Invite.time_id, time_id)
					.set(Invite.invite_type_id, 6)
					.set(Invite.user_id, user_id).save();
			ResponseValues responseValues = new ResponseValues(this, Thread.currentThread().getStackTrace()[1].getMethodName());
			if(save) {
				responseValues.put("status", 1);
				message = "发布成功";
			}else {
				responseValues.put("status", 0);
				message = "发布失败";
			}
			responseValues.put("message",message);
			responseValues.put("code", 200);
			setAttr("InviteResponse", responseValues);
			renderMultiJson("InviteResponse");
			render("/app/InviteController/list");
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
		}
	}
	//邀约详情
	public void details() {
		String token;
		ResponseValues response2;
		boolean save = false;
		try {
			token = getPara("token");
			if (!AppToken.check(token, this)) {
				response2 = new ResponseValues(this, Thread.currentThread().getStackTrace()[1].getMethodName());
				// 登陆失败
				response2.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("ReviewResponse", response2);
				renderMultiJson("ReviewResponse");
				return;
			}
			String filter_sql = " where ";
			String latitude = getPara("latitude", "30.344");
			String longitude = getPara("longitude", "120.00");
			String invite_id = getPara("invite_id");
			String user_id = AppToken.getUserId(token, this);
//			final User user = User.dao.findFirst("select nickname,image_01,DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(birthday)), '%Y')+0 AS age,"
//					+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN(("+latitude+"*PI()/180-latitude*PI()/180)/2),2)+COS("+latitude+"*PI()/180)*COS(latitude*PI()/180)*POW"
//					+ "(SIN(("+longitude+"*PI()/180-longitude*PI()/180)/2),2)))*1000) AS distance"
//					+ " from user where user_id = " + user_id);
			final List<Invite> iList = Invite.dao.find("SELECT u.nickname,u.image_01,u.hot,"
					+ "i.*,"
					+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN(("+latitude+"*PI()/180-u.latitude*PI()/180)/2),2)+COS("+latitude+"*PI()/180)*COS(u.latitude*PI()/180)*POW"
					+ "(SIN(("+longitude+"*PI()/180-u.longitude*PI()/180)/2),2)))*1000) AS distance,DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(birthday)), '%Y')+0 AS age"
					+ " FROM `user` AS u INNER JOIN invite AS i ON u.`user_id` = i.`user_id` "+filter_sql+"i.invite_id="+invite_id+" ORDER BY i.`is_top` DESC");
			System.out.println(user_id.toString());
			ResponseValues responseValues = new ResponseValues(this, Thread.currentThread().getStackTrace()[1].getMethodName());
			responseValues.put("code", 200);
			responseValues.put("Result", new HashMap<String, Object>() {
				{
					put("detailsList", iList);
				}
			});
			setAttr("InviteResponse", responseValues);
			renderMultiJson("InviteResponse");
		}
		 catch (Exception e) {
			AppLog.error(e, getRequest());
		}finally{
			AppLog.info("", getRequest());
		}
	}
	
	//购买置顶
	public void payTop(){
		try {
			int invite_id = getParaToInt("invite_id");
			String token = getPara("token");
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("InviteResponse", response2);
				renderMultiJson("InviteResponse");
				return;
			}
			int status = 0;
			boolean save = false;
			String message="置顶失败";
			String user_id = AppToken.getUserId(token, this);
			User user = User.dao.findFirst("select * from user where user_id = "+user_id);
			int user_gold_value = user.get("user_gold_value");
			InvitationPriceEntity invitationPriceEntity = InvitationPriceEntity.dao.findFirst("select * from invitation_price");
			Integer price = invitationPriceEntity.get("price");
			int left_user_gold_value = user_gold_value;
			if (user_gold_value<price) {
				status = 2;
				message="当前钻石余额不足";
			}else {
				Invite invite = Invite.dao.findById(invite_id);
				Integer is_top = invite.get("is_top");
				System.out.println(is_top);
				if (is_top == 1) {
					message="已经置顶";
				}else {
					save = invite.set(Invite.is_top, 1).set("top_date", DateUtils.getCurrentDateTime()).update();
				}
				if (save) {
					left_user_gold_value = user_gold_value - price;
					user.set(user.user_gold_value, left_user_gold_value)
					.update();
					status = 1;
					message="置顶成功";
				}
			}
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			response.put("message", message);
			response.put("status", status);
			response.put("code", 200);
			setAttr("InviteResponse", response);
			renderMultiJson("InviteResponse");
			AppLog.info(null, getRequest());
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
		}
	}	
	
	
	
}
