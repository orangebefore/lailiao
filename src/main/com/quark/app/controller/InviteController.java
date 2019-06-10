package com.quark.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jfinal.core.Controller;
import com.quark.api.auto.bean.ResponseValues;
import com.quark.app.logs.AppLog;
import com.quark.interceptor.AppToken;
import com.quark.model.extend.InviteCost;
import com.quark.model.extend.InviteTime;
import com.quark.model.extend.TravelDays;
import com.quark.model.extend.TravelMode;

public class InviteController extends Controller{

	
	public void list() {
		
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
