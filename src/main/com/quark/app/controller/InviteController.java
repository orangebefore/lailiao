package com.quark.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jfinal.upload.UploadFile;
import com.jfinal.core.Controller;
import com.quark.api.auto.bean.ResponseValues;
import com.quark.app.logs.AppLog;
import com.quark.common.AppData;
import com.quark.common.config;
import com.quark.interceptor.AppToken;
import com.quark.model.extend.Invite;
import com.quark.model.extend.InviteCost;
import com.quark.model.extend.InviteTime;
import com.quark.model.extend.TravelDays;
import com.quark.model.extend.TravelMode;
import com.quark.model.extend.User;
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
			final List<Invite> iList = Invite.dao.find("SELECT u.nickname,u.city,u.image_01,u.job,"
					+ "u.birthday,u.height,i.invite_content,i.cost_id,i.invite_explain,i.is_top,i.invite_type_id,"
					+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN(("+latitude+"*PI()/180-u.latitude*PI()/180)/2),2)+COS("+latitude+"*PI()/180)*COS(u.latitude*PI()/180)*POW"
					+ "(SIN(("+longitude+"*PI()/180-u.longitude*PI()/180)/2),2)))*1000) AS distance"
					+ " FROM `user` AS u INNER JOIN invite AS i ON u.`user_id` = i.`user_id` "+filter_sql+" ORDER BY i.`is_top` DESC");
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			response.put("message", "");
			response.put("status", 1);
			response.put("code", 200);
			response.put("Result", new HashMap<String, Object>() {
				{
					put("list", iList);
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
			save = Invite.dao.set(Invite.explain_url, FileUtils.renameToFile(explain_url))
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
			save = Invite.dao.set(Invite.explain_url, FileUtils.renameToFile(explain_url))
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
			save = Invite.dao.set(Invite.explain_url, FileUtils.renameToFile(explain_url))
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
			save = Invite.dao.set(Invite.explain_url, FileUtils.renameToFile(explain_url))
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
			save = Invite.dao.set(Invite.explain_url, FileUtils.renameToFile(explain_url))
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
			Invite invite =  new Invite();
			save = invite.set(Invite.explain_url, FileUtils.renameToFile(explain_url))
					.set(invite.invite_content, invite_content)
					.set(invite.invite_province, invite_province)
					.set(invite.invite_city, invite_city)
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
	//筛选
	public void search(){
		
	}
	//邀约详情
	public void details() {
		
	}
	
}
