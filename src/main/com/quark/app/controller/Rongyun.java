
package com.quark.app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.ehcache.config.Searchable;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.quark.api.annotation.Author;
import com.quark.api.annotation.DataType;
import com.quark.api.annotation.Explaination;
import com.quark.api.annotation.ReturnOutlet;
import com.quark.api.annotation.Rp;
import com.quark.api.annotation.Type;
import com.quark.api.annotation.URLParam;
import com.quark.api.annotation.Value;
import com.quark.api.auto.bean.ResponseValues;
import com.quark.app.logs.AppLog;
import com.quark.common.AppData;
import com.quark.common.config;
import com.quark.interceptor.AppToken;
import com.quark.model.extend.Tokens;
import com.quark.model.extend.User;
import com.quark.utils.DateUtils;

public class Rongyun extends Controller {

	@Author("cluo")
	@Rp("主页")
	@Explaination(info = "融云 ： 获取联系人头像及昵称")
	@URLParam(name = "user_id", defaultValue = "{select user_id from user}", explain = "用户ID", type = Type.String)
	@ReturnOutlet(name = "AvatarsResponse{user:uid}", remarks = "用户ID", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "AvatarsResponse{user:nickname}", remarks = "用户昵称", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "AvatarsResponse{user:avatar}", remarks = "用户头像", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "AvatarsResponse{user:age}", remarks = "0岁", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "AvatarsResponse{user:sex}", remarks = "", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "AvatarsResponse{user:city}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "AvatarsResponse{user:height}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "AvatarsResponse{user:heart}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "AvatarsResponse{user:black_status}", remarks = "", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "AvatarsResponse{status}", remarks = "1-操作成功", dataType = DataType.Int, defaultValue = "")
	public void avatars() {
		try {
			int user_id = getParaToInt("user_id");
			User user = User.dao.findFirst("select is_vip,user_id,heart,hope,height,nickname,telephone,image_01,birthday,sex,city,black_status from user where user_id="+user_id);
			ResponseValues response2 = new ResponseValues(this, Thread
					.currentThread().getStackTrace()[1].getMethodName());
			if (user == null) {
				response2.put("status", 0);
			} else {
				//user.set(user.last_login_time, DateUtils.getCurrentDateTime()).update();
				//出生年月日
				Date birthday = user.getDate("birthday");
				int age_int = 0;
		    	if (birthday!=null) {
		    		String age_date = user.getDate("birthday").toString();
		    		if (!age_date.equals("")&&age_date!=null) {
		    			age_int = DateUtils.getCurrentAgeByBirthdate(age_date);
		    		}
		    	}
		    	user.put("age", age_int+"岁");
		    	String image_01 = user.getStr("image_01");
		    	int sex = user.get(user.sex);
		    	if (image_01.equals("")) {
					if (sex==0) {
						image_01 = "avatar_small_female.png";
					}else {
						image_01 = "avatar_small_male.png";
					}
				}
		    	user.put("image_01", image_01);
				response2.put("status", 1);
			}
			response2.put("user", user);
			setAttr("AvatarsResponse", response2);
			renderMultiJson("AvatarsResponse");
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
			AppData.analyze("Rongyun/avatars", "", this);
		}
	
	}
}
