package com.quark.app.controller;

import java.io.Serializable;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;
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
import com.quark.model.extend.Audit;
import com.quark.model.extend.CarCategroy;
import com.quark.model.extend.CarClassify;
import com.quark.model.extend.Certification;
import com.quark.model.extend.SuperstarPrice;
import com.quark.model.extend.Tokens;
import com.quark.model.extend.User;
import com.quark.utils.DateUtils;
import com.quark.utils.FileUtils;

/**
 * 
 * @author TOM
 *
 */
public class ReviewController extends Controller implements Serializable{
	
	
		public void findCategroy(){
			try {
				String token = getPara("token");
				if (!AppToken.check(token, this)) {
					// 登陆失败
					ResponseValues response2 = new ResponseValues(this,
							Thread.currentThread().getStackTrace()[1].getMethodName());
					response2.put("message", "请重新登陆");
					response2.put("code", 405);
					setAttr("GiftsResponse", response2);
					renderMultiJson("GiftsResponse");
					return;
				}
				CarCategroy carCategroy = new CarCategroy();
				List<CarCategroy> categroys = carCategroy.find("select id,car_url,type,type_name from car_categroy");
				ResponseValues responseValues = new ResponseValues(this, Thread.currentThread().getStackTrace()[1].getMethodName());
				responseValues.put("code", 200);
				responseValues.put("list", categroys);
				setAttr("ReviewResponse", responseValues);
				renderMultiJson("ReviewResponse");
			} catch (Exception e) {
				AppLog.error(e, getRequest());
			} finally {
				AppData.analyze("ReviewController/findCategroy", "汽车品牌列表", this);
			}
		}
		

		public void findCarClassify(){
			try {
				String token = getPara("token");
				if (!AppToken.check(token, this)) {
					// 登陆失败
					ResponseValues response2 = new ResponseValues(this,
							Thread.currentThread().getStackTrace()[1].getMethodName());
					response2.put("message", "请重新登陆");
					response2.put("code", 405);
					setAttr("GiftsResponse", response2);
					renderMultiJson("GiftsResponse");
					return;
				}
			int categroy_id = getParaToInt("categroy_id");
			System.out.println(categroy_id);
			CarClassify classify = new CarClassify();
			List<CarClassify> carClassifies = classify.find("select categroy_id,car_name,car_url from car_classify where categroy_id="+categroy_id);
			ResponseValues responseValues = new ResponseValues(this, Thread.currentThread().getStackTrace()[1].getMethodName());
			responseValues.put("code", 200);
			responseValues.put("list", carClassifies);
			setAttr("ReviewResponse", responseValues);
			renderMultiJson("ReviewResponse");
			} catch (Exception e) {
				AppLog.error(e, getRequest());
			} finally {
				AppData.analyze("ReviewController/findCarClassify", "汽车详情列表", this);
			}
		}
	
		//汽车认证
		public void saveCar() {
			try {
				String token = getPara("token");
				if (!AppToken.check(token, this)) {
					// 登陆失败
					ResponseValues response2 = new ResponseValues(this,
							Thread.currentThread().getStackTrace()[1].getMethodName());
					response2.put("message", "请重新登陆");
					response2.put("code", 405);
					setAttr("GiftsResponse", response2);
					renderMultiJson("GiftsResponse");
					return;
				}
			String car_name = getPara("car_name");
			boolean save = false;
			String message = "";
			UploadFile upload_cover = getFile("drivers", config.images_path);
			String user_id = AppToken.getUserId(token, this);
			Certification certification = new Certification();
			certification = Certification.dao.findFirst("select * from certification where user_id = " + user_id);
			//判断用户是否有审核申请记录
			if(certification!=null) {
				save = certification.set(certification.drivers, FileUtils.renameToFile(upload_cover))
						.set(certification.car_status, 2)
						.set(certification.car_reason, "").update();
			} else {
				save = certification.set(certification.user_id, user_id)
						.set(certification.drivers, FileUtils.renameToFile(upload_cover))
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
				AppData.analyze("ReviewController/saveCar", "汽车审核", this);
			}
			
			
		}
		
				
		@Author("chen")
		@Rp("认证中心")
		@Explaination(info = "视频认证")
		@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
		@URLParam(defaultValue = "", explain = Value.Infer, type = Type.File, name = Certification.video_url)
		@ReturnOutlet(name = "ReviewResponse{message}", remarks = "message", dataType = DataType.String, defaultValue = "")
		@ReturnOutlet(name = "ReviewResponse{user:token}", remarks = "token", dataType = DataType.String, defaultValue = "")
		@ReturnOutlet(name = "ReviewResponse{status}", remarks = "1-申请成功，2-申请失败", dataType = DataType.Int, defaultValue = "")
		@ReturnOutlet(name = "ReviewResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
		public void saveVideo() {
				try {
				String token = getPara("token");
				if (!AppToken.check(token, this)) {
					// 登陆失败
					ResponseValues response2 = new ResponseValues(this,
							Thread.currentThread().getStackTrace()[1].getMethodName());
					response2.put("message", "请重新登陆");
					response2.put("code", 405);
					setAttr("ReviewResponse", response2);
					renderMultiJson("ReviewResponse");
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
		
		@Author("chen")
		@Rp("认证中心")
		@Explaination(info = "身份认证")
		@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
		@URLParam(defaultValue = "", explain = Value.Infer, type = Type.File, name = Certification.id_card_up)
		@URLParam(defaultValue = "", explain = Value.Infer, type = Type.File, name = Certification.id_card_down)
		@ReturnOutlet(name = "ReviewResponse{message}", remarks = "message", dataType = DataType.String, defaultValue = "")
		@ReturnOutlet(name = "ReviewResponse{user:token}", remarks = "token", dataType = DataType.String, defaultValue = "")
		@ReturnOutlet(name = "ReviewResponse{status}", remarks = "1-申请成功，2-申请失败", dataType = DataType.Int, defaultValue = "")
		@ReturnOutlet(name = "ReviewResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
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
			} catch (Exception e) {
				AppLog.error(e, getRequest());
			} finally {
				AppLog.info("", getRequest());
			}
		}
		
		@Author("chen")
		@Rp("认证中心")
		@Explaination(info = "房产认证")
		@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
		@URLParam(defaultValue = "", explain = Value.Infer, type = Type.File, name = Certification.house_url)
		@ReturnOutlet(name = "ReviewResponse{message}", remarks = "message", dataType = DataType.String, defaultValue = "")
		@ReturnOutlet(name = "ReviewResponse{user:token}", remarks = "token", dataType = DataType.String, defaultValue = "")
		@ReturnOutlet(name = "ReviewResponse{status}", remarks = "1-申请成功，2-申请失败", dataType = DataType.Int, defaultValue = "")
		@ReturnOutlet(name = "ReviewResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
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
		
		@Author("chen")
		@Rp("认证中心")
		@Explaination(info = "学历认证")
		@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
		@URLParam(defaultValue = "", explain = Value.Infer, type = Type.File, name = Certification.edu_url)
		@ReturnOutlet(name = "ReviewResponse{message}", remarks = "message", dataType = DataType.String, defaultValue = "")
		@ReturnOutlet(name = "ReviewResponse{user:token}", remarks = "token", dataType = DataType.String, defaultValue = "")
		@ReturnOutlet(name = "ReviewResponse{status}", remarks = "1-申请成功，2-申请失败", dataType = DataType.Int, defaultValue = "")
		@ReturnOutlet(name = "ReviewResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
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
		
		//个性签名认证
		public void saveHeart() {
			
		}		
		
		
		
}
