package com.quark.app.controller;

import java.io.Serializable;

import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;
import com.quark.api.auto.bean.ResponseValues;
import com.quark.app.logs.AppLog;
import com.quark.common.AppData;
import com.quark.common.config;
import com.quark.interceptor.AppToken;
import com.quark.model.extend.Audit;
import com.quark.model.extend.Certification;
import com.quark.model.extend.SuperstarPrice;
import com.quark.utils.DateUtils;
import com.quark.utils.FileUtils;

/**
 * 
 * @author TOM
 *
 */
public class ReviewController extends Controller implements Serializable{
	
		//汽车认证
		public void saveCar() {
			
		}
		
				
		//视频认证
		public void saveVideo() {
				try {
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
				boolean save = false;
				String message = "";
				UploadFile upload_cover = getFile("video_url", config.videos_path);
				String user_id = AppToken.getUserId(token, this);
				Certification certification = new Certification();
				certification = Certification.dao.findFirst("select * from certification where user_id = " + user_id);
				//判断用户是否有审核申请记录
				if(certification!=null) {
					save = certification.set(certification.video_url, FileUtils.renameToFile(upload_cover))
							.set(certification.video_status, 2)
							.set(certification.video_reason, "").update();
				} else {
					save = certification.set(Certification.user_id, user_id)
							.set(certification.video_url, FileUtils.renameToFile(upload_cover))
							.save();
				}
				ResponseValues responseValues = new ResponseValues(this, Thread.currentThread().getStackTrace()[1].getMethodName());
				//判断审核是否提交成功
				if(save) {
					responseValues.put("status", 1);
					message = "申请成功";
				} else {
					responseValues.put("status", 0);
					message = "申请失败";
				}
				responseValues.put("message", message);
				responseValues.put("code", 200);
				setAttr("ReviewResponse", responseValues);
				renderMultiJson("ReviewResponse");
			} catch (Exception e) {
				AppLog.error(e, getRequest());
			} finally {
				AppLog.info("", getRequest());
			}
		}
		
		
		//身份证认证
		public void saveIdCard() {
			try {
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
				boolean save = false;
				String message = "";
				UploadFile upload_cover = getFile("id_card_up", config.save_path);
				UploadFile upload_cover2 = getFile("id_card_down", config.save_path);
				String user_id = AppToken.getUserId(token, this);
				Certification certification = new Certification();
				certification = Certification.dao.findFirst("select * from certification where user_id = " + user_id);
				//判断用户是否有审核申请记录
				if(certification!=null) {
					save = certification.set(certification.id_card_up, FileUtils.renameToFile(upload_cover))
							.set(certification.id_card_down, FileUtils.renameToFile(upload_cover2))
							.set(certification.id_card_status, 2)
							.set(certification.id_card_reason, "").update();
				} else {
					save = certification.set(certification.id_card_up, FileUtils.renameToFile(upload_cover))
							.set(certification.id_card_down, FileUtils.renameToFile(upload_cover2)).save();
				}
				ResponseValues responseValues = new ResponseValues(this, Thread.currentThread().getStackTrace()[1].getMethodName());
				//判断审核是否提交成功
				if(save) {
					responseValues.put("status", 1);
					message = "申请成功";
				} else {
					responseValues.put("status", 0);
					message = "申请失败";
				}
				responseValues.put("message", message);
				responseValues.put("code", 200);
				setAttr("ReviewResponse", responseValues);
				renderMultiJson("ReviewResponse");
				AppLog.info("", getRequest());
			} catch (Exception e) {
				AppLog.error(e, getRequest());
			} finally {
				AppLog.info("", getRequest());
			}
		}
		
		//房产认证
		public void saveHouse() {
			String token;
			ResponseValues response2;
			boolean save = false;
			String message = "";
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
				//判断用户是否有审核申请记录
				UploadFile HouseImg_Url = getFile("house_url",config.images_path);
				String user_id = AppToken.getUserId(token, this);
				Audit Audit = new Audit();
				Audit.dao.find("select * from certification where user_id =" + user_id);
				if(Audit != null) {
					save  = Audit.dao.set("house_url", FileUtils.renameToFile(HouseImg_Url))
							.set("house_reason", "").set("house_status",2).update();
				}else {
					save  = Audit.dao.set("house_url", FileUtils.renameToFile(HouseImg_Url))
							.set("user_id",user_id).save();
				}
				ResponseValues responseValues = new ResponseValues(this, Thread.currentThread().getStackTrace()[1].getMethodName());
				if(save) {
					responseValues.put("status", 1);
					message = "申请成功";
				}else {
					responseValues.put("status", 0);
					message = "申请失败";
				}
				responseValues.put("message",message);
				responseValues.put("code", 200);
				setAttr("ReviewResponse", responseValues);
				renderMultiJson("ReviewResponse");
			}
			 catch (Exception e) {
				AppLog.error(e, getRequest());
			}finally{
				AppLog.info("", getRequest());
			}
		}
		
		//学历认证
		public void saveEdu() {
			String token;
			ResponseValues response2;
			boolean save = false;
			String message = "";
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
				UploadFile EduImg_Url = getFile("edu_url",config.images_path);
				String user_id = AppToken.getUserId(token, this);
				Audit Audit = new Audit();
				Audit.dao.find("select * from certification where user_id =" + user_id);
				if(Audit != null) {
					save  = Audit.dao.set("edu_url", FileUtils.renameToFile(EduImg_Url))
							.set("edu_reason", "").set("edu_status",2).update();
				}else {
					save  = Audit.dao.set("edu_url", FileUtils.renameToFile(EduImg_Url)).set("edu_reason", "")
							.set("user_id",user_id).set("edu_status",2).save();
				}
				ResponseValues responseValues = new ResponseValues(this, Thread.currentThread().getStackTrace()[1].getMethodName());
				if(save) {
					responseValues.put("status", 1);
					message = "申请成功";
				}else {
					responseValues.put("status", 0);
					message = "申请失败";
				}
				responseValues.put("message",message);
				responseValues.put("code", 200);
				setAttr("ReviewResponse", responseValues);
				renderMultiJson("ReviewResponse");
			}
			 catch (Exception e) {
				AppLog.error(e, getRequest());
			}finally{
				AppLog.info("", getRequest());
			}
		}
		
		//汽车认证
		public void saveHeart() {
			
		}		
		
		
		
}
