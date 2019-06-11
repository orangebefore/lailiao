package com.quark.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jfinal.core.Controller;
import com.quark.api.auto.bean.ResponseValues;
import com.quark.app.logs.AppLog;
import com.quark.common.AppData;
import com.quark.interceptor.AppToken;
import com.quark.model.extend.Invite;
import com.quark.model.extend.InviteCost;
import com.quark.model.extend.InviteTime;
import com.quark.model.extend.TravelDays;
import com.quark.model.extend.TravelMode;
import com.quark.model.extend.User;

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
			String message="";
			String user_id = AppToken.getUserId(token, this);
			User user = User.dao.findById(user_id);
			String latitude =user.get("latitude");
			String longitude =user.get("longitude");
			final List<Invite> iList = Invite.dao.find("select invite_type_id,invite_content,cost_id,invite_explain,is_top,invite_place from invite order by is_top asc");
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			response.put("message", "");
			response.put("status", 1);
			response.put("code", 200);
			response.put("Result", new HashMap<String, Object>() {
				{
					put("iList", iList);
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
		
	}
	

	public void saveFood() {
		
	}
	

	public void saveSing() {
		
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
		
	}
	

	public void saveMotion() {
		
	}
	

	public void saveLiterature() {
		
	}
	
}
