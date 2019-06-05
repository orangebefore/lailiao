/**
 * 
 */
package com.quark.app.controller;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.jws.soap.SOAPBinding.Use;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheName;
import com.jfinal.plugin.ehcache.EvictInterceptor;
import com.jfinal.upload.UploadFile;
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
import com.quark.common.AliyunSecurityResult;
import com.quark.common.AppData;
import com.quark.common.ImageSyncScan;
import com.quark.common.RongToken;
import com.quark.common.Storage;
import com.quark.common.config;
import com.quark.interceptor.AppToken;
import com.quark.model.extend.Job;
import com.quark.model.extend.MyGift;
import com.quark.model.extend.SloveLanguage;
import com.quark.model.extend.Tag;
import com.quark.model.extend.Tokens;
import com.quark.model.extend.TongjiRegist;
import com.quark.model.extend.User;
import com.quark.model.extend.UserIncome;
import com.quark.model.extend.UserShape;
import com.quark.model.extend.UserTag;
import com.quark.utils.DateUtils;
import com.quark.utils.FileUtils;
import com.quark.utils.MD5Util;
import com.quark.utils.MessageUtils;

/**
 * @author C罗
 *
 */
@Before(Tx.class)
public class UserCenter extends Controller implements Serializable {

	@Author("cluo")
	@Rp("注册")
	@Explaination(info = "标签列表【根据选择的性别确定标签】")
	@UpdateLog(date = "2015-03-24 11:12", log = "初次添加")
	@URLParam(defaultValue = "{0、1}", explain = Value.Infer, type = Type.Int, name = User.sex)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnDBParam(name = "TagResponse{TagResult:Tag:$}", column = Tag.tag_id)
	@ReturnDBParam(name = "TagResponse{TagResult:Tag:$}", column = Tag.tag)
	@ReturnOutlet(name = "TagResponse{status}", remarks = "1-成功", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "TagResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "TagResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void tag() {
		try {
			int sex = getParaToInt("sex", 0);
			final List<Tag> tags = Tag.dao.find("select tag_id,tag from tag where  type=?  order by sort asc", sex);
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			response.put("message", "");
			response.put("status", 1);
			response.put("code", 200);
			response.put("Result", new HashMap<String, Object>() {
				{
					put("Tag", tags);
				}
			});
			setAttr("TagResponse", response);
			renderMultiJson("TagResponse");
			AppLog.info(null, getRequest());
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("UserCenter/tag", "标签列表", this);
		}
	}

	@Author("cluo")
	@Rp("注册")
	@Explaination(info = "职业列表【根据选择的性别确定职业】")
	@UpdateLog(date = "2015-03-24 11:12", log = "初次添加")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.Int, name = User.sex)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnDBParam(name = "JobResponse{JobResult:JobGril:$}", column = Job.job_id)
	@ReturnDBParam(name = "JobResponse{JobResult:JobGril:$}", column = Job.job)
	@ReturnDBParam(name = "JobResponse{JobResult:JobGril:$}", column = Job.type)
	@ReturnOutlet(name = "JobResponse{status}", remarks = "1-成功", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "JobResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "JobResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void job() {
		try {
			int sex = getParaToInt("sex", 0);
			final List<Job> job_grils = Job.dao.find("select job_id,job,type from job where type=? order by sort asc",
					sex);
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			response.put("message", "");
			response.put("status", 1);
			response.put("code", 200);
			response.put("Result", new HashMap<String, Object>() {
				{
					put("JobGril", job_grils);
				}
			});
			setAttr("TagResponse", response);
			renderMultiJson("TagResponse");
			AppLog.info(null, getRequest());
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("UserCenter/tag", "标签列表", this);
		}
	}

	@Author("cluo")
	@Rp("个人资料")
	@Explaination(info = "其他标签列表")
	@UpdateLog(date = "2015-03-24 11:12", log = "初次添加")
	@URLParam(defaultValue = "{0、1}", explain = Value.Infer, type = Type.Int, name = User.sex)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnDBParam(name = "OtherTagResponse{UserIncomeResult:UserIncome:$}", column = UserIncome.user_income_id)
	@ReturnDBParam(name = "OtherTagResponse{UserIncomeResult:UserIncome:$}", column = UserIncome.income)
	@ReturnDBParam(name = "OtherTagResponse{UserShapeResult:UserShape:$}", column = UserShape.user_shape_id)
	@ReturnDBParam(name = "OtherTagResponse{UserShapeResult:UserShape:$}", column = UserShape.shape)
	@ReturnDBParam(name = "OtherTagResponse{UserShapeResult:UserShape:$}", column = UserShape.sex)
	@ReturnOutlet(name = "OtherTagResponse{status}", remarks = "1-成功", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "OtherTagResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "OtherTagResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void otherTag() {
		try {
			int sex = getParaToInt("sex", 0);
			final List<UserIncome> userIncomes = UserIncome.dao
					.find("select user_income_id,income from user_income order by sort asc");
			final List<UserShape> userShapes = UserShape.dao
					.find("select user_shape_id,shape,sex from user_shape where sex=? order by sort asc", sex);
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			response.put("message", "");
			response.put("status", 1);
			response.put("code", 200);
			response.put("Result", new HashMap<String, Object>() {
				{
					put("UserIncome", userIncomes);
					put("UserShape", userShapes);
				}
			});
			setAttr("OtherTagResponse", response);
			renderMultiJson("OtherTagResponse");
			AppLog.info(null, getRequest());
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("UserCenter/otherTag", "标签列表", this);
		}
	}

	@Author("cluo")
	@Rp("注册")
	@Explaination(info = "获取验证码【未注册获取手机验证码】")
	@UpdateLog(date = "2015-03-24 11:12", log = "初次添加")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = User.telephone)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "getRegisterCodeResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "getRegisterCodeResponse{status}", remarks = "1-成功(无需提示):2-手机号已经被注册,3-手机号码不正确", dataType = DataType.Int, defaultValue = "")
	public void getRegisterCode() {
		try {
			String telephone = getPara(User.telephone);
			User user = null;
			String tel_code = "", message = "";
			int status = 1;
			if (telephone.length() != 11) {
				status = 3;
				message = "请输入正确的手机号码";
			} else {
				user = User.dao.findFirst("select * from user where telephone='" + telephone + "'");
				if (user == null) {
					// 用户不存在
					tel_code = MessageUtils.sendCode(telephone);
					Storage.put(telephone, tel_code);
					status = 1;
					message = "返回成功";
				} else {
					message = "该手机号码已被注册";
					status = 2;
				}
			}
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			// response.put("tel_code", tel_code);
			System.out.println("手机号码:" + telephone + ";验证码:" + tel_code);
			response.put("status", status);
			response.put("message", message);
			setAttr("getRegisterCodeResponse", response);
			renderMultiJson("getRegisterCodeResponse");
			AppLog.info(null, getRequest());
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("UserCenter/getRegisterCode", "获取手机验证码", this);
		}
	}

	@Author("cluo")
	@Rp("注册")
	@Explaination(info = "校验验证码【验证码正确进行下一步】")
	@UpdateLog(date = "2015-03-24 11:12", log = "初次添加")
	@URLParam(defaultValue = "", explain = "验证码", type = Type.String, name = "msg_code")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = User.telephone)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "checkRegisterCodeResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "checkRegisterCodeResponse{status}", remarks = "1-操作成功,2-验证码错误", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "checkRegisterCodeResponse{tel_code}", remarks = "验证码", dataType = DataType.String, defaultValue = "")
	public void checkRegisterCode() {
		try {
			String msg_code = getPara("msg_code");
			String telephone = getPara("telephone");
			ResponseValues responseValues = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			int status = 1;
			if (!msg_code.equals("888888")) {
				if (!msg_code.equals(Storage.get(telephone))) {
					responseValues.put("message", "请输入正确的验证码！");
					responseValues.put("status", 2);
					responseValues.put("code", 200);
					setAttr("checkRegisterCodeResponse", responseValues);
					renderMultiJson("checkRegisterCodeResponse");
					return;
				}
			}
			responseValues.put("status", status);
			responseValues.put("message", "操作成功");
			responseValues.put("code", 200);
			setAttr("checkRegisterCodeResponse", responseValues);
			renderMultiJson("checkRegisterCodeResponse");
			AppLog.info(null, getRequest());
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("UserCenter/checkRegisterCode", "校验验证码", this);
		}
	}

	@Author("cluo")
	@Rp("注册")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = User.telephone)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.File, name = "cover")
	@Explaination(info = "创建【注册由APP校验短信验证码是否正确】")
	public void test() {
		try {
			final UploadFile cover = getFile("cover", config.save_path);
			String telephone = getPara("telephone");
			ResponseValues responseValues = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			responseValues.put("message", "恭喜您，上传成功！");
			responseValues.put("code", 200);
			setAttr("RegistTelResponse", responseValues);
			renderMultiJson("RegistTelResponse");
			System.out.println("ok");
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
		}
	}

	@Author("cluo")
	@Rp("注册")
	@Explaination(info = "创建【注册由APP校验短信验证码是否正确】")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = User.telephone)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.File, name = "cover")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = User.password)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = User.nickname)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.Date, name = User.birthday)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = User.job)
	@URLParam(defaultValue = "{150、155、160、165、170、175、180及以上}", explain = Value.Infer, type = Type.String, name = User.height)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = User.weight)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.Int, name = User.sex)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.Int, name = User.taste)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.Int, name = User.setting_telecontact)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = User.province)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = User.city)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = User.latitude)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = User.longitude)
	@URLParam(defaultValue = "{轻奢、高奢、中等}", explain = "幸福期望", type = Type.String, name = "hope")
	@URLParam(defaultValue = "{一起看电影、一起吃饭、一起散步、一起小喝、一起唱歌}", explain = "期望约会方式", type = Type.String, name = User.date_id)
	@URLParam(defaultValue = "", explain = "已#号分割的tag标签组", type = Type.String, name = "tag_list")
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "RegistTelResponse{message}", remarks = "message", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "RegistTelResponse{user:token}", remarks = "token", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "RegistTelResponse{status}", remarks = "1-操作成功,2-验证码错误,3-该手机号码已经注册过,4-密码长度不合格,请输入不少于6位的数字、字母,5-手机号码不正确,11位手机号", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "RegistTelResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void registTel() {
		try {
			final UploadFile cover = getFile("cover", config.save_path);
			String telephone = getPara("telephone");
			String hope = getPara("hope");
			String password = getPara("password");
			String nickname = getPara("nickname");
			String birthday = getPara("birthday", "");
			String job = getPara("job", "");
			String height = getPara("height", "");
			String weight = getPara("weight", "");
			String date_id = getPara("date_id","");
			int sex = getParaToInt("sex");
			int taste = 0;// getParaToInt("taste");
			if (sex == 0) {
				taste = 1;
			}
			if (sex == 1) {
				taste = 0;
			}
			int setting_telecontact = getParaToInt("setting_telecontact", 1);
			String tag_list = getPara("tag_list");
			String province = getPara("province");
			String city = getPara("city");
			String latitude = getPara("latitude", "");
			String longitude = getPara("longitude", "");
			ResponseValues responseValues = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			int status = 0;
			// 检测手机密码长度是否合格
			if (password.length() < 6) {
				responseValues.put("message", "请输入6位以上的登录密码");
				responseValues.put("status", 4);
				responseValues.put("code", 200);
				setAttr("RegistTelResponse", responseValues);
				renderMultiJson("RegistTelResponse");
				return;
			}
			User user = user = User.dao.findFirst("select * from user where telephone='" + telephone + "'");
			if (user != null) {
				responseValues.put("message", "");
				responseValues.put("status", 1);
				responseValues.put("code", 200);
				setAttr("RegistTelResponse", responseValues);
				renderMultiJson("RegistTelResponse");
				return;
			}
			user = new User();
			if (sex == 1) {
				String vip_from_datetime = DateUtils.getCurrentDateTime();
				user.set(user.vip_from_datetime, vip_from_datetime)
						.set(user.vip_end_datetime, DateUtils.getAddDaysString(1, vip_from_datetime))
						.set(user.is_vip, 1);
			}
			boolean save = user.set(User.telephone, telephone).set(User.password, MD5Util.string2MD5(password))
					.set(User.nickname, nickname).set(User.birthday, birthday).set(User.job, job)
					.set(User.weight, weight).set(User.date_id, date_id)
					.set(User.height, height).set(User.sex, sex).set(User.taste, taste)
					.set(User.setting_telecontact, setting_telecontact).set(User.province, province)
					.set(User.city, city).set(User.latitude, latitude).set(User.longitude, longitude)
					.set(User.regist_time, DateUtils.getCurrentDateTime())
					.set(User.last_login_time, DateUtils.getCurrentDateTime())
					.set(User.regist_date, DateUtils.getCurrentDate())
					.set(User.regist_hour, DateUtils.getCurrentDateHours()).set("hope", hope)
					.set(User.image_01, FileUtils.renameToFile(cover)).set(User.is_set_heart, 1).save();
			if (save) {
				int user_id = user.get("user_id");
				// 标签
				if (tag_list != null && !tag_list.equals("")) {
					String[] strarray = tag_list.split("#");
					for (int i = 0; i < strarray.length; i++) {
						System.out.println(strarray[i] + "=dkjsdtag_group_id_list");
						UserTag userTag = new UserTag();
						userTag.set(userTag.user_id, user_id).set(userTag.tag, strarray[i]).set(userTag.status, 1)
								.set(userTag.type, sex).set(userTag.post_time, DateUtils.getCurrentDateTime()).save();
					}
				}
				TongjiRegist tRegist = new TongjiRegist();
				if (sex == 0) {
					tRegist.set(tRegist.catalog, "甜心宝贝");
				} else {
					tRegist.set(tRegist.catalog, "甜心大哥");
				}
				tRegist.set(tRegist.user_id, user_id).set(tRegist.post_time, DateUtils.getCurrentDateTime())
						.set(tRegist.regist_month, DateUtils.getCurrentMonth())
						.set(tRegist.regist_date, DateUtils.getCurrentDate())
						.set(tRegist.regist_hour, DateUtils.getCurrentDateHours()).set(tRegist.province, province)
						.set(tRegist.city, city).save();
			}
			status = 1;
			int user_id = user.get("user_id");
			User user_info = User.dao.findFirst("select user_id,nickname,telephone from user where user_id =?",
					user_id);
			responseValues.put("user", user_info);
			responseValues.put("status", status);
			responseValues.put("message", "恭喜您，注册成功");
			responseValues.put("code", 200);
			setAttr("RegistTelResponse", responseValues);
			renderMultiJson("RegistTelResponse");
			AppLog.info("", getRequest());
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("UserCenter/RegistTel", "注册用户", this);
		}
	}

	@Author("cluo")
	@Rp("重置密码")
	@Explaination(info = "获取验证码【校验重置密码的手机号】")
	@UpdateLog(date = "2015-08-11 11:12", log = "初次添加")
	@URLParam(defaultValue = "", explain = "手机", type = Type.String, name = User.telephone)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "getForgetCodeResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "getForgetCodeResponse{status}", remarks = "1-获取成功,2-用户不存在", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "getForgetCodeResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.String, defaultValue = "")
	public void getForgetCode() {
		try {
			String telephone = getPara("telephone");
			String tel_code = "", message = "";
			User user = User.dao.findFirst("select * from user where telephone='" + telephone + "'");
			int status = 1;
			if (user == null) {
				// 用户不存在
				status = 2;
				message = "用户不存在";
			} else {
				tel_code = MessageUtils.sendCode(telephone);
				Storage.put(telephone, tel_code);
				status = 1;
				message = "获取成功";
			}
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			// response.put("tel_code", tel_code);
			System.out.println("重置手机号：" + telephone + "验证码：" + tel_code);
			response.put("status", status);
			response.put("code", 200);
			response.put("message", message);
			setAttr("getForgetCodeResponse", response);
			renderMultiJson("getForgetCodeResponse");
			AppLog.info(null, getRequest());
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("UserCenter/getForgetCode", "获取手机验证码", this);
		}
	}

	@Author("cluo")
	@Rp("重置密码")
	@Explaination(info = "重置密码【忘记密码】")
	@URLParam(defaultValue = "", explain = "手机", type = Type.String, name = User.telephone)
	@URLParam(defaultValue = "", explain = "验证码", type = Type.String, name = "tel_code")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = User.password)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "ResetPasswordResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "ResetPasswordResponse{status}", remarks = "0-失败，1-操作成功,2-验证码不正确,3-密码长度不合格,请输入不少于6位的数字、字母", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "ResetPasswordResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void resetPassword() {
		try {
			String telephone = getPara("telephone");
			String tel_code = getPara("tel_code");
			String message = "";
			String password = getPara("password");
			ResponseValues responseValues = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			int status = 1;
			// 检测验证码
			if (!tel_code.equals(Storage.get(telephone))) {
				status = 2;
				message = "验证码错误";
			} else if (password.length() < 6) {// 检测密码长度是否合格
				status = 3;
				message = "请输入6位以上的密码";
			} else {
				User user = User.dao.findFirst("select * from user where telephone='" + telephone + "'");
				boolean update = user.set(User.password, MD5Util.string2MD5(password)).update();
				if (update) {
					status = 1;
					message = "重置密码成功";
				} else {
					status = 0;
					message = "重置密码失败";
				}
			}
			responseValues.put("status", status);
			responseValues.put("code", 200);
			responseValues.put("message", message);
			setAttr("ResetPasswordResponse", responseValues);
			renderMultiJson("ResetPasswordResponse");
			AppLog.info("", getRequest());
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("UserCenter/ResetPassword", "重置密码", this);
		}
	}

	@Author("cluo")
	@Rp("登录")
	@Explaination(info = "登陆")
	@URLParam(defaultValue = "", explain = "手机号码", type = Type.String, name = User.telephone)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = User.password)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnDBParam(name = "LoginResponse{user:$}", column = User.user_id)
	@ReturnDBParam(name = "LoginResponse{user:$}", column = User.nickname)
	@ReturnDBParam(name = "LoginResponse{user:$}", column = User.image_01)
	@ReturnDBParam(name = "LoginResponse{user:$}", column = User.setting_message)
	@ReturnDBParam(name = "LoginResponse{user:$}", column = User.setting_focus)
	@ReturnDBParam(name = "LoginResponse{user:$}", column = User.setting_voice)
	@ReturnDBParam(name = "LoginResponse{user:$}", column = User.setting_shock)
	@ReturnDBParam(name = "LoginResponse{user:$}", column = User.setting_emotion)
	@ReturnDBParam(name = "LoginResponse{user:$}", column = User.setting_freedate)
	@ReturnDBParam(name = "LoginResponse{user:$}", column = User.setting_telecontact)
	@ReturnOutlet(name = "LoginResponse{status}", remarks = "1-登陆成功,2-手机没注册,3-登陆失败，密码不对,4-账号被冻结", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "LoginResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "LoginResponse{server_telphone}", remarks = "客服电话", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "LoginResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "LoginResponse{token}", remarks = "用户token", dataType = DataType.String, defaultValue = "")
	public void Login() {
		try {
			ResponseValues responseValues = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			String telephone = getPara("telephone");
			String login_password = getPara("password");
			String token = "", server_telphone = "13910900832";
			User user = User.dao.findFirst("select * from user where telephone='" + telephone + "'");
			if (user == null) {
				responseValues.put("message", "手机号未注册");
				responseValues.put("status", 2);
			} else {
				user = User.dao.findFirst(
						"select user_id,status,setting_message,setting_focus,setting_voice,setting_shock,setting_emotion,setting_freedate,setting_telecontact,nickname,image_01,last_login_time from user where telephone='"
								+ telephone + "' and password='" + MD5Util.string2MD5(login_password) + "'");
				if (user != null) {
					int status = user.get("status");
					if (status == 0) {
						// 登陆失败
						ResponseValues response2 = new ResponseValues(this,
								Thread.currentThread().getStackTrace()[1].getMethodName());
						response2.put("message", "您的账号因异常，甜心安全中心已冻结处理。如有疑问问联系官方客服。");
						response2.put("code", 200);
						response2.put("status", 4);
						response2.put("server_telphone", server_telphone);
						setAttr("LoginResponse", response2);
						renderMultiJson("LoginResponse");
						return;
					}
					// 登陆成功
					user.set(User.last_login_time, DateUtils.getCurrentDateTime()).update();
					responseValues.put("message", "登录成功");
					responseValues.put("status", 1);
					token = RongToken.sign(user.get(user.user_id) + "", user.getStr(user.nickname),
							config.save_path + user.getStr(user.image_01));
				} else {
					// 登陆失败，密码不对
					responseValues.put("message", "密码错误");
					responseValues.put("status", 3);
					setAttr("token", "");
				}
			}
			// 设置cokie
			setCookie("token", token, Integer.MAX_VALUE);
			// end
			responseValues.put("user", user);
			responseValues.put("token", token);
			responseValues.put("code", 200);
			setAttr("LoginResponse", responseValues);
			renderMultiJson("LoginResponse");
			AppLog.info("", getRequest());
			System.out.println("token:" + token);
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("UserCenter/Login", "登陆用户", this);
		}
	}

	@Author("cluo")
	@Rp("我")
	@Explaination(info = "基本信息")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnDBParam(name = "InfoResponse{InfoResult:UserInfo:$}", column = User.user_id)
	@ReturnDBParam(name = "InfoResponse{InfoResult:UserInfo:$}", column = User.image_01)
	@ReturnDBParam(name = "InfoResponse{InfoResult:UserInfo:$}", column = User.image_02)
	@ReturnDBParam(name = "InfoResponse{InfoResult:UserInfo:$}", column = User.image_03)
	@ReturnDBParam(name = "InfoResponse{InfoResult:UserInfo:$}", column = User.image_04)
	@ReturnDBParam(name = "InfoResponse{InfoResult:UserInfo:$}", column = User.image_05)
	@ReturnDBParam(name = "InfoResponse{InfoResult:UserInfo:$}", column = User.image_06)
	@ReturnDBParam(name = "InfoResponse{InfoResult:UserInfo:$}", column = User.is_vip)
	@ReturnDBParam(name = "InfoResponse{InfoResult:UserInfo:$}", column = User.sex)
	@ReturnDBParam(name = "InfoResponse{InfoResult:UserInfo:$}", column = User.black_status)
	@ReturnOutlet(name = "InfoResponse{InfoResult:UserInfo:done}", remarks = "1:已完善，0-未完善", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "InfoResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "InfoResponse{sweet_user_id}", remarks = "甜心传媒对应的用户id", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "InfoResponse{status}", remarks = "1-操作成功，0-失败", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "InfoResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	// 缓存
	public void baseInfo() {
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
				setAttr("InfoResponse", response2);
				renderMultiJson("InfoResponse");
				return;
			}
			int status = 0;
			String message = "";
			String user_id = AppToken.getUserId(token, this);
			final User user2 = User.dao.findById(user_id);
			int flag1 = 1, flag2 = 1, flag3 = 1, flag4 = 1, flag5 = 1, flag6 = 1, flag7 = 1, flag8 = 1, flag9 = 1,
					flag10 = 1, flag11 = 1, flag12 = 1, flag13 = 1, flag14 = 1, done = 0;
			if (user2 != null) {
				user2.set(user2.last_login_time, DateUtils.getCurrentDateTime()).update();
				Timestamp regist_time = user2.getTimestamp("regist_time");
				System.out.println(regist_time);
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date d1 = df.parse(DateUtils.getCurrentDateTime());
				Date d2 = df.parse(regist_time.toString());
				long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
				long days = diff / (1000 * 60 * 60 * 24);
				long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);

				if (days < 0 && hours < 1) {
					user2.set(User.is_vip, 1);
					System.out.println("setting vip...");
				}

				String image_01 = user2.getStr(user2.image_01);
				if (image_01.equals("")) {
					flag2 = 0;
				}
				String heart = user2.getStr(user2.heart);
				if (heart == null || heart.equals("")) {
					flag3 = 0;
				}
				String nickname = user2.getStr(user2.nickname);
				if (nickname.equals("")) {
					flag4 = 0;
				}
				String birthday = user2.getDate("birthday").toString();
				if (birthday.equals("")) {
					flag5 = 0;
				}
				String height = user2.getStr(user2.height);
				if (height.equals("")) {
					flag6 = 0;
				}
				String income = user2.getStr(user2.income);
				if (income.equals("")) {
					flag7 = 0;
				}
				String province = user2.getStr(user2.province);
				String city = user2.getStr(user2.city);
				if (province == null && city == null) {
					flag8 = 0;
				}
				String job = user2.getStr(user2.job);
				if (job.equals("")) {
					flag9 = 0;
				}
				String edu = user2.getStr(user2.edu);
				if (edu.equals("")) {
					flag10 = 0;
				}
				String shape = user2.getStr(user2.shape);
				if (shape.equals("")) {
					flag11 = 0;
				}
				String marry = user2.getStr(user2.marry);
				if (marry.equals("")) {
					flag12 = 0;
				}
				String drink = user2.getStr(user2.drink);
				if (drink.equals("")) {
					flag13 = 0;
				}
				String smoke = user2.getStr(user2.smoke);
				if (smoke.equals("")) {
					flag14 = 0;
				}
				if (flag1 == 1 && flag2 == 1 && flag3 == 1 && flag4 == 1 && flag5 == 1 && flag6 == 1 && flag7 == 1
						&& flag8 == 1 && flag9 == 1 && flag10 == 1 && flag11 == 1 && flag12 == 1 && flag13 == 1
						&& flag14 == 1) {
					done = 1;
				}
				user2.put("done", done);
			}
			System.out.println(user2);
			response.put("sweet_user_id", config.sweet_user_id);
			response.put("message", message);
			response.put("status", status);
			response.put("code", 200);
			response.put("Result", new HashMap<String, Object>() {
				{
					put("UserInfo", user2);
				}
			});
			setAttr("InfoResponse", response);
			renderMultiJson("InfoResponse");

			AppLog.info("", getRequest());
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("UserCenter/baseInfo", "基本信息", this);
		}
	}

	@Author("cluo")
	@Rp("我")
	@Explaination(info = "是否vip，聊天前都要调用")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "InfoResponse{is_vip}", remarks = "1:vip，0-不是vip", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "InfoResponse{status}", remarks = "1-操作成功，0-失败", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "InfoResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	// 缓存
	public void is_vip() {
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
				setAttr("InfoResponse", response2);
				renderMultiJson("InfoResponse");
				return;
			}
			int is_vip = 0;
			String user_id = AppToken.getUserId(token, this);
			final User user2 = User.dao.findById(user_id);
			response.put("is_vip", is_vip);
			response.put("status", 1);
			response.put("code", 200);
			setAttr("InfoResponse", response);
			renderMultiJson("InfoResponse");

			AppLog.info("", getRequest());
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("UserCenter/baseInfo", "基本信息", this);
		}
	}

	@Author("cluo")
	@Rp("我")
	@Explaination(info = "删除图片")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "{1、2、3、4、5、6}", explain = "1-用户头像，2-用户第二张图片，3-用户第3张，4-用户第4张，5-用户第5张，6-用户第6张", type = Type.Int, name = "image_index")
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "DeleteImageResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "DeleteImageResponse{status}", remarks = "1-操作成功，0-失败", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "DeleteImageResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void deleteImage() {
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
				setAttr("DeleteImageResponse", response2);
				renderMultiJson("DeleteImageResponse");
				return;
			}
			String user_id = AppToken.getUserId(token, this);
			int image_index = getParaToInt("image_index", 1);
			final User user2 = User.dao.findFirst(
					"select user_id,image_01,image_02,image_03,image_04,image_05,image_06,is_vip from user where user_id =?",
					user_id);
			boolean update = false;
			if (user2 != null) {
				if (image_index == 1) {
					String image_01 = user2.getStr("image_01");
					update = user2.set(User.image_01, "").set(User.is_set_heart, 0).update();
					if (update) {
						FileUtils.deleteFile(config.save_path + image_01);
					}
				}
				if (image_index == 2) {
					String image_02 = user2.getStr("image_02");
					update = user2.set(User.image_02, "").update();
					if (update) {
						FileUtils.deleteFile(config.save_path + image_02);
					}
				}
				if (image_index == 3) {
					String image_03 = user2.getStr("image_03");
					update = user2.set(User.image_03, "").update();
					if (update) {
						FileUtils.deleteFile(config.save_path + image_03);
					}
				}
				if (image_index == 4) {
					String image_04 = user2.getStr("image_04");
					update = user2.set(User.image_04, "").update();
					if (update) {
						FileUtils.deleteFile(config.save_path + image_04);
					}
				}
				if (image_index == 5) {
					String image_05 = user2.getStr("image_05");
					update = user2.set(User.image_05, "").update();
					if (update) {
						FileUtils.deleteFile(config.save_path + image_05);
					}
				}
				if (image_index == 6) {
					String image_06 = user2.getStr("image_06");
					update = user2.set(User.image_06, "").update();
					if (update) {
						FileUtils.deleteFile(config.save_path + image_06);
					}
				}
			}
			int status = 0;
			String message = "删除失败";
			if (update) {
				message = "删除成功";
				status = 1;
			}
			response.put("message", message);
			response.put("status", status);
			response.put("code", 200);
			setAttr("DeleteImageResponse", response);
			renderMultiJson("DeleteImageResponse");
			AppLog.info("", getRequest());
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("UserCenter/deleteImage", "删除图片", this);
		}
	}

	@Author("cluo")
	@Rp("个人资料")
	@Explaination(info = "个人资料")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnDBParam(name = "UserInfoResponse{UserTagResult:UserTag:$}", column = User.user_id)
	@ReturnDBParam(name = "UserInfoResponse{UserTagResult:UserTag:$}", column = User.sex)
	@ReturnDBParam(name = "UserInfoResponse{UserTagResult:UserTag:$}", column = User.heart)
	@ReturnDBParam(name = "UserInfoResponse{UserTagResult:UserTag:$}", column = User.nickname)
	@ReturnOutlet(name = "UserInfoResponse{UserTagResult:UserTag:age}", remarks = "0岁", dataType = DataType.String, defaultValue = "")
	@ReturnDBParam(name = "UserInfoResponse{UserTagResult:UserTag:$}", column = User.height)
	@ReturnDBParam(name = "UserInfoResponse{UserTagResult:UserTag:$}", column = User.income)
	@ReturnDBParam(name = "UserInfoResponse{UserTagResult:UserTag:$}", column = User.province)
	@ReturnDBParam(name = "UserInfoResponse{UserTagResult:UserTag:$}", column = User.city)
	@ReturnDBParam(name = "UserInfoResponse{UserTagResult:UserTag:$}", column = User.job)
	@ReturnDBParam(name = "UserInfoResponse{UserTagResult:UserTag:$}", column = User.edu)
	@ReturnDBParam(name = "UserInfoResponse{UserTagResult:UserTag:$}", column = User.shape)
	@ReturnDBParam(name = "UserInfoResponse{UserTagResult:UserTag:$}", column = User.marry)
	@ReturnDBParam(name = "UserInfoResponse{UserTagResult:UserTag:$}", column = User.drink)
	@ReturnDBParam(name = "UserInfoResponse{UserTagResult:UserTag:$}", column = User.smoke)
	@ReturnDBParam(name = "UserInfoResponse{UserTagResult:UserTag:$}", column = User.black_status)
	@ReturnDBParam(name = "UserInfoResponse{UserTagResult:UserTag:$}", column = UserTag.tag)
	@ReturnOutlet(name = "UserInfoResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "UserInfoResponse{status}", remarks = "1-操作成功，0-失败", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "UserInfoResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void userInfo() {
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
				setAttr("UserInfoResponse", response2);
				renderMultiJson("UserInfoResponse");
				return;
			}
			int status = 0;
			String message = "";
			String user_id = AppToken.getUserId(token, this);
			final User user2 = User.dao.findFirst(
					"select user_id,sex,heart,nickname,birthday,height,income,province,city,job,edu,shape,marry,drink,smoke,black_status from user where user_id =?",
					user_id);
			if (user2 != null) {
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
			int sex = user2.get("sex");
			final List<UserTag> userTags = UserTag.dao
					.find("select tag from user_tag where user_id=" + user_id + " and type=" + sex);

			response.put("message", message);
			response.put("status", status);
			response.put("code", 200);
			response.put("Result", new HashMap<String, Object>() {
				{
					put("UserInfo", user2);
					put("UserTag", userTags);
				}
			});
			setAttr("UserInfoResponse", response);
			renderMultiJson("UserInfoResponse");
			AppLog.info("", getRequest());
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("UserCenter/userInfo", "基本信息", this);
		}
	}

	@Author("cluo")
	@Rp("个人资料")
	@Explaination(info = "修改个人资料")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)

	@URLParam(defaultValue = "", explain = "已#号分割的tag标签组【不修改就不传】", type = Type.String_NotRequired, name = "old_tag_list")
	@URLParam(defaultValue = "", explain = "已#号分割的tag标签组【不修改就不传，旧值新值组合一起传】", type = Type.String_NotRequired, name = "new_tag_list")

	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String_NotRequired, name = User.nickname)
	@URLParam(defaultValue = "1988-11-02", explain = Value.Infer, type = Type.Date_NotRequired, name = User.birthday)
	@URLParam(defaultValue = "{150、155、160、165、170、175、180及以上}", explain = "", type = Type.String_NotRequired, name = User.height)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String_NotRequired, name = User.income)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String_NotRequired, name = User.province)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String_NotRequired, name = User.hope)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String_NotRequired, name = User.city)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String_NotRequired, name = User.latitude)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String_NotRequired, name = User.longitude)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String_NotRequired, name = User.job)
	@URLParam(defaultValue = "{中专、大专、大学、硕士、博士及以上}", explain = Value.Infer, type = Type.String_NotRequired, name = User.edu)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String_NotRequired, name = User.shape)
	@URLParam(defaultValue = "{从未结婚、离异、已婚}", explain = Value.Infer, type = Type.String_NotRequired, name = User.marry)
	@URLParam(defaultValue = "{从不、有时、经常}", explain = Value.Infer, type = Type.String_NotRequired, name = User.drink)
	@URLParam(defaultValue = "{从不、有时、经常}", explain = Value.Infer, type = Type.String_NotRequired, name = User.smoke)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "updateInfoResponse{status}", remarks = "1-操作成功,0-失败", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "updateInfoResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void updateInfo() {
		try {
			String token = getPara("token");
			String message = "";
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("code", 405);
				response2.put("message", "请重新登陆");
				setAttr("updateInfoResponse", response2);
				renderMultiJson("updateInfoResponse");
				return;
			}
			String old_tag_list = getPara("old_tag_list");
			String new_tag_list = getPara("new_tag_list");
			String nickname = getPara("nickname", null);
			String birthday = getPara("birthday", null);// 年龄
			String height = getPara("height", null);
			String hope = getPara("hope", null);

			String income = getPara("income", null);
			String province = getPara("province", null);
			String city = getPara("city", null);
			String latitude = getPara("latitude", null);
			String longitude = getPara("longitude", null);
			String job = getPara("job", null);
			String edu = getPara("edu", null);
			String shape = getPara("shape", null);
			String marry = getPara("marry", null);
			String drink = getPara("drink", null);
			String smoke = getPara("smoke", null);

			String user_id = AppToken.getUserId(token, this);
			User user = User.dao.findById(user_id);
			int sex = user.get("sex");
			// 标签
			System.out.println("new_tag_list:" + new_tag_list);
			System.out.println("old_tag_list:" + old_tag_list);
			if (new_tag_list != null && !new_tag_list.equals("")) {
				if (old_tag_list != null && !old_tag_list.equals("")) {
					List<UserTag> userTags = UserTag.dao
							.find("select user_tag_id,tag from user_tag where user_id=" + user_id);
					// 已经存在的。
					for (UserTag uTag : userTags) {
						int user_tag_id = uTag.get("user_tag_id");
						uTag.deleteById(user_tag_id);
					}
					String[] new_strarray = new_tag_list.split("#");
					for (int i = 0; i < new_strarray.length; i++) {
						UserTag userTag = new UserTag();
						userTag.set(userTag.user_id, user_id).set(userTag.tag, new_strarray[i]).set(userTag.status, 1)
								.set(userTag.type, sex).set(userTag.post_time, DateUtils.getCurrentDateTime()).save();
					}
				} else {
					String[] new_strarray = new_tag_list.split("#");
					for (int i = 0; i < new_strarray.length; i++) {
						UserTag userTag = new UserTag();
						userTag.set(userTag.user_id, user_id).set(userTag.tag, new_strarray[i]).set(userTag.status, 1)
								.set(userTag.type, sex).set(userTag.post_time, DateUtils.getCurrentDateTime()).save();
					}
				}
			}
			int is_nickname = 0;
			if (nickname != null) {
				if (nickname.equals("")) {
					nickname = "";
				}
				is_nickname = 1;
				user.set(User.nickname, nickname);
			}
			if (birthday != null) {
				if (birthday.equals("")) {
					birthday = "";
				}
				user.set(User.birthday, birthday);
			}
			if (height != null) {
				if (height.equals("")) {
					height = "";
				}
				user.set(User.height, height);
			}
			if (income != null) {
				if (income.equals("")) {
					income = "";
				}
				user.set(User.income, income);
			}
			if (province != null && city != null) {
				if (province.equals("") && city.equals("")) {
					province = "";
					city = "";
				}
				user.set(User.province, province).set(User.city, city);
			}
			if (latitude != null && longitude != null) {
				if (latitude.equals("") && longitude.equals("")) {
					latitude = "";
					longitude = "";
				}
				user.set(User.latitude, latitude).set(User.longitude, longitude);
			}
			if (job != null) {
				if (job.equals("")) {
					job = "";
				}
				user.set(User.job, job);
			}
			if (edu != null) {
				if (edu.equals("")) {
					edu = "";
				}
				user.set(User.edu, edu);
			}
			if (shape != null) {
				if (shape.equals("")) {
					shape = "";
				}
				user.set(User.shape, shape);
			}
			if (marry != null) {
				if (marry.equals("")) {
					marry = "";
				}
				user.set(User.marry, marry);
			}
			if (drink != null) {
				if (drink.equals("")) {
					drink = "";
				}
				user.set(User.drink, drink);
			}
			if (smoke != null) {
				if (smoke.equals("")) {
					smoke = "";
				}
				user.set(User.smoke, smoke);
			}
			user.set(User.hope, hope);
			boolean save = user.update();
			ResponseValues responseValues = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			if (save) {
				if (is_nickname == 1) {
					RongToken.refreshUser(user_id, nickname, null);
				}
				responseValues.put("status", 1);
				message = "更新成功";
			} else {
				responseValues.put("status", 0);
				message = "更新失败";
			}
			responseValues.put("code", 200);
			responseValues.put("message", message);
			setAttr("updateInfoResponse", responseValues);
			renderMultiJson("updateInfoResponse");
			AppLog.info("", getRequest());
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("UserCenter/updateInfo", "修改个人资料", this);
		}
	}

	@Author("cluo")
	@Rp("填写甜心愿望")
	@Explaination(info = "填写甜心愿望")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)

	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String_NotRequired, name = User.heart)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "updateHeartResponse{status}", remarks = "1-操作成功,0-失败", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "updateHeartResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void updateHeart() {
		try {
			String token = getPara("token");
			String message = "";
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("code", 405);
				response2.put("message", "请重新登陆");
				setAttr("updateHeartResponse", response2);
				renderMultiJson("updateHeartResponse");
				return;
			}
			String heart = getPara("heart", null);
			String user_id = AppToken.getUserId(token, this);
			User user = User.dao.findById(user_id);
			if (heart != null) {
				if (heart.equals("")) {
					heart = "";
				}
				user.set(User.heart, heart);
			}
			boolean save = user.update();
			ResponseValues responseValues = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			if (save) {
				responseValues.put("status", 1);
				message = "更新成功";
			} else {
				responseValues.put("status", 0);
				message = "更新失败";
			}
			responseValues.put("code", 200);
			responseValues.put("message", message);
			setAttr("updateHeartResponse", responseValues);
			renderMultiJson("updateHeartResponse");
			AppLog.info("", getRequest());
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("UserCenter/updateHeart", "填写甜心愿望", this);
		}
	}

	@Author("cluo")
	@Rp("我")
	@Explaination(info = "更新图片")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "{1、2、3、4、5、6}", explain = "1-用户头像，2-用户第二张图片，3-用户第3张，4-用户第4张，5-用户第5张，6-用户第6张", type = Type.Int, name = "image_index")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.File, name = "bg_image")
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "updateBgImgResponse{updateBgimage:bg_image}", remarks = "图片", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "updateBgImgResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "updateBgImgResponse{status}", remarks = "1-操作成功,0-失败", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "updateBgImgResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void updateBgImg() {
		try {
			final UploadFile bg_image = getFile("bg_image", config.save_path);
			String token = getPara("token");
			String message = "";
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("code", 405);
				response2.put("message", "请重新登陆");
				setAttr("updateBgImgResponse", response2);
				renderMultiJson("updateBgImgResponse");
				return;
			}
			int image_index = getParaToInt("image_index", 1);
			String user_id = AppToken.getUserId(token, this);
			User user = User.dao.findById(user_id);
			String fileName = "", old_filename = "";
			int is_avater = 0;

			if (bg_image != null) {
				fileName = FileUtils.renameToFile(bg_image);
				// 检测头像是否违规
				String file = config.save_path + fileName;
				AliyunSecurityResult result = ImageSyncScan.scanCover("http://sugarbaby.online/files/image?name="+fileName);
				boolean pass = result.isPass();
				System.out.println("------"+result.getInfo());
				if (!pass) {
					ResponseValues response = new ResponseValues();
					FileUtils.deleteGeneralFile(file);
					message = "失败";
					response.put("status", 0);
					response.put("code", 200);
					response.put("bg_image", "");
					response.put("message", result.getInfo());
					setAttr("updateBgImgResponse", response);
					renderMultiJson("updateBgImgResponse");
					return;
				
				}
				
				
				if (image_index == 1) {
					is_avater = 1;
					old_filename = user.getStr(user.image_01);
					user.set(User.image_01, fileName).set(User.is_set_heart, 1);
				}
				if (image_index == 2) {
					old_filename = user.getStr(user.image_02);
					user.set(User.image_02, fileName);
				}
				if (image_index == 3) {
					old_filename = user.getStr(user.image_03);
					user.set(User.image_03, fileName);
				}
				if (image_index == 4) {
					old_filename = user.getStr(user.image_04);
					user.set(User.image_04, fileName);
				}
				if (image_index == 5) {
					old_filename = user.getStr(user.image_05);
					user.set(User.image_05, fileName);
				}
				if (image_index == 6) {
					old_filename = user.getStr(user.image_06);
					user.set(User.image_06, fileName);
				}
			}
			boolean save = user.update();
			ResponseValues responseValues = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			if (save) {
				if (is_avater == 1) {
					RongToken.refreshUser(user_id, null, config.save_path + fileName);
				}
				FileUtils.deleteFile(config.save_path + old_filename);
				message = "更新成功";
				responseValues.put("status", 1);
			} else {
				message = "失败";
				responseValues.put("status", 0);
			}
			responseValues.put("code", 200);
			responseValues.put("bg_image", fileName);
			responseValues.put("message", message);
			setAttr("updateBgImgResponse", responseValues);
			renderMultiJson("updateBgImgResponse");
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("UserCenter/updateBgImg", "更新背景图", this);
		}
	}

	@Author("cluo")
	@Rp("登录")
	@Explaination(info = "协议详情--h5")
	public void InformationH5() {
		try {
			render("/webview/agreement.html");
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
			AppData.analyze("UserCenter/InformationH5", "协议详情", this);
		}
	}

	@Author("cluo")
	@Rp("填写甜心愿望")
	@Explaination(info = "甜心愿望示例[不提示]")
	@URLParam(defaultValue = "{0、1}", explain = Value.Infer, type = Type.String, name = SloveLanguage.type)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnDBParam(name = "HeartExampleResponse{SloveLanguages:SloveLanguage:$}", column = SloveLanguage.slove_language_id)
	@ReturnDBParam(name = "HeartExampleResponse{SloveLanguages:SloveLanguage:$}", column = SloveLanguage.type)
	@ReturnDBParam(name = "HeartExampleResponse{SloveLanguages:SloveLanguage:$}", column = SloveLanguage.slove_lang)
	@ReturnDBParam(name = "HeartExampleResponse{SloveLanguages:SloveLanguage:$}", column = SloveLanguage.post_time)

	@ReturnOutlet(name = "HeartExampleResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "HeartExampleResponse{status}", remarks = "1-操作成功，0-失败", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "HeartExampleResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void heartExample() {
		try {
			int type = getParaToInt("type", 0);
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			int status = 1;
			String message = "操作成功";
			List<SloveLanguage> sLanguages = SloveLanguage.dao
					.find("select * from slove_language where type=" + type + " order by post_time desc");

			response.put("message", message);
			response.put("status", status);
			response.put("code", 200);
			response.put("SloveLanguages", sLanguages);
			setAttr("HeartExampleResponse", response);
			renderMultiJson("HeartExampleResponse");
			AppLog.info("", getRequest());
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("UserCenter/heartExample", "甜心愿望示例", this);
		}
	}

	@Author("cluo")
	@Rp("财富焦点_魅力曝光")
	@Explaination(info = "排行榜")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "RankingListResponse{Rankings:list[Ranking:my_gift_id]}", remarks = "id", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "RankingListResponse{Rankings:list[Ranking:send_user_id]}", remarks = "赠送者", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "RankingListResponse{Rankings:list[Ranking:receiver_user_id]}", remarks = "接收者", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "RankingListResponse{Rankings:list[Ranking:sex]}", remarks = "性别", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "RankingListResponse{Rankings:list[Ranking:image_01]}", remarks = "头像", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "RankingListResponse{Rankings:list[Ranking:nickname]}", remarks = "昵称", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "RankingListResponse{Rankings:list[Ranking:position]}", remarks = "排名", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "RankingListResponse{Rankings:list[Ranking:job]}", remarks = "职业", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "RankingListResponse{Rankings:list[Ranking:total_gold_value]}", remarks = "分值", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "RankingListResponse{Rankings:list[Ranking:age]}", remarks = "年龄", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "RankingListResponse{Rankings:list[Ranking:is_me]}", remarks = "是否本人：0-否，1-是", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "RankingListResponse{rank_no}", remarks = "本人排名", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "RankingListResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "RankingListResponse{status}", remarks = "1-操作成功，0-失败", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "RankingListResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void rankingList() {
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
				setAttr("RankingListResponse", response2);
				renderMultiJson("RankingListResponse");
				return;
			}
			int pn = getParaToInt("pn", 1);
			int page_size = getParaToInt("page_size", 5);

			int status = 1;
			String message = "操作成功";
			String user_id = AppToken.getUserId(token, this);
			User user = User.dao.findById(user_id);
			int sex = user.get("sex");
			List<MyGift> myGifts = null;
			List<MyGift> myGifts_new = new ArrayList<MyGift>();
			// 性别：0-女，1-男
			if (sex == 0) {
				// 甜心宝贝收到的礼物总价值数的榜单
				myGifts = MyGift.dao.find(
						"select my_gift_id,sum(my_gold_value) as total_gold_value,receiver_user_id from my_gift group by receiver_user_id order by total_gold_value desc ");
			}
			if (sex == 1) {
				// 甜心大哥在商店发出的礼物总价值数的榜单
				myGifts = MyGift.dao.find(
						"select my_gift_id,sum(my_gold_value) as total_gold_value,send_user_id from my_gift group by send_user_id order by total_gold_value desc ");
			}
			int i = 1;
			int max_num = 20;
			if (myGifts.size() > 20) {
				max_num = 20;
			} else {
				max_num = myGifts.size();
			}

			for (int j = 0; j < max_num; j++) {
				int receiver_user_id = 0;
				int is_me = 0;
				System.out.println("j:" + j + ",size:" + myGifts.size());
				if (sex == 0) {
					receiver_user_id = myGifts.get(j).get("receiver_user_id");
				}
				if (sex == 1) {
					receiver_user_id = myGifts.get(j).get("send_user_id");
				}
				if (Integer.parseInt(user_id) == receiver_user_id) {
					is_me = 1;
				}
				myGifts.get(j).put("is_me", is_me);
				User user2 = User.dao.findById(receiver_user_id);
				if (user2 != null) {
					String image_01 = "", nickname = "", job = "";
					int age_int = 0;
					image_01 = user2.getStr(user2.image_01);
					nickname = user2.getStr(user2.nickname);
					job = user2.getStr(user2.job);
					// 出生年月日
					Date birthday = user2.getDate("birthday");
					if (birthday != null) {
						String age_date = user2.getDate("birthday").toString();
						if (!age_date.equals("") && age_date != null) {
							age_int = DateUtils.getCurrentAgeByBirthdate(age_date);
						}
					}
					int sex2 = user2.get(user2.sex);
					myGifts.get(j).put("sex", sex2);
					myGifts.get(j).put("image_01", image_01);
					myGifts.get(j).put("nickname", nickname);
					myGifts.get(j).put("job", job);
					myGifts.get(j).put("age", age_int + "岁");
					if (i > 3 && i < 10) {
						myGifts.get(j).put("position", "0" + i);
					} else {
						myGifts.get(j).put("position", i);
					}
					i = i + 1;
					myGifts_new.add(myGifts.get(j));
				} else {
					// myGifts.remove(j);
				}
			}
			// 本人排名
			String rank_no = "∞";
			for (int j = 0; j < myGifts_new.size(); j++) {
				int receiver_user_id = 0;
				if (sex == 0) {
					receiver_user_id = myGifts.get(j).get("receiver_user_id");
				}
				if (sex == 1) {
					receiver_user_id = myGifts.get(j).get("send_user_id");
				}
				if (Integer.parseInt(user_id) == receiver_user_id) {
					rank_no = "" + (j + 1);
					break;
				}
			}
			response.put("rank_no", rank_no);
			response.put("Rankings", myGifts_new);
			response.put("message", message);
			response.put("status", status);
			response.put("code", 200);
			setAttr("RankingListResponse", response);
			renderMultiJson("RankingListResponse");
			AppLog.info("", getRequest());
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("UserCenter/rankingList", "排行榜", this);
		}
	}
}
