/**
 * 
 */
package com.quark.app.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
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
import com.quark.common.config;
import com.quark.interceptor.AppToken;
import com.quark.model.extend.BlackList;
import com.quark.model.extend.Browse;
import com.quark.model.extend.CellPhone;
import com.quark.model.extend.Charge;
import com.quark.model.extend.Collection;
import com.quark.model.extend.ReportBean;
import com.quark.model.extend.Search;
import com.quark.model.extend.Tokens;
import com.quark.model.extend.User;
import com.quark.model.extend.UserOld;
import com.quark.model.extend.UserTag;
import com.quark.utils.DateUtils;
import com.quark.utils.MD5Util;
import com.quark.utils.MessageUtils;

/**
 * @author C罗
 *
 */
@Before(Tx.class)
public class Setting extends Controller {

	@Author("cluo")
	@Rp("设置")
	@Explaination(info = "所有状态")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String_NotRequired, name = Tokens.token)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnDBParam(name = "UserBInfoResponse{SettingStatus:$}", column = User.telephone)
	@ReturnDBParam(name = "UserBInfoResponse{SettingStatus:$}", column = User.setting_message)
	@ReturnDBParam(name = "UserBInfoResponse{SettingStatus:$}", column = User.setting_focus)
	@ReturnDBParam(name = "UserBInfoResponse{SettingStatus:$}", column = User.setting_voice)
	@ReturnDBParam(name = "UserBInfoResponse{SettingStatus:$}", column = User.setting_shock)
	@ReturnDBParam(name = "UserBInfoResponse{SettingStatus:$}", column = User.setting_emotion)
	@ReturnDBParam(name = "UserBInfoResponse{SettingStatus:$}", column = User.setting_freedate)
	@ReturnDBParam(name = "UserBInfoResponse{SettingStatus:$}", column = User.setting_telecontact)
	@ReturnOutlet(name = "UserBInfoResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "UserBInfoResponse{status}", remarks = "1-操作成功", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "UserBInfoResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void settingStatus() {
		try {
			String token = getPara("token");
			String message = "";
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("code", 405);
				response2.put("message", "请重新登陆");
				setAttr("UserBInfoResponse", response2);
				renderMultiJson("UserBInfoResponse");
				return;
			}
			String user_id = AppToken.getUserId(token, this);
			ResponseValues response2 = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			User user = User.dao.findFirst(
					"select user_id,telephone,setting_message,setting_focus,setting_voice,setting_shock,setting_emotion,setting_freedate,setting_telecontact from user where user_id=?",
					user_id);
			if (user == null) {
				response2.put("status", 0);
				message = "服务器出错";
			} else {
				message = "";
				response2.put("status", 1);
				user.set(user.last_login_time, DateUtils.getCurrentDateTime()).update();
			}
			response2.put("SettingStatus", user);
			response2.put("message", message);
			response2.put("code", 200);
			setAttr("Response", response2);
			renderMultiJson("Response");
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
			AppData.analyze("Setting/settingStatus", "所有状态", this);
		}
	}

	@Author("cluo")
	@Rp("设置")
	@Explaination(info = "设置消息提醒")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "{1、2、3、4、5、6、7}", explain = "1-新消息提醒，2-关注提醒，3-提醒声音，4-震动，5-情感状态，6-加入无偿约会，7-屏蔽手机联系人", type = Type.Int, name = "setting_type")
	@URLParam(defaultValue = "{0、1}", explain = Value.Infer, type = Type.Int_NotRequired, name = User.setting_message)
	@URLParam(defaultValue = "{0、1}", explain = Value.Infer, type = Type.Int_NotRequired, name = User.setting_focus)
	@URLParam(defaultValue = "{0、1}", explain = Value.Infer, type = Type.Int_NotRequired, name = User.setting_voice)
	@URLParam(defaultValue = "{0、1}", explain = Value.Infer, type = Type.Int_NotRequired, name = User.setting_shock)
	@URLParam(defaultValue = "{0、1}", explain = Value.Infer, type = Type.Int_NotRequired, name = User.setting_emotion)
	@URLParam(defaultValue = "{0、1}", explain = Value.Infer, type = Type.Int_NotRequired, name = User.setting_freedate)
	@URLParam(defaultValue = "{0、1}", explain = Value.Infer, type = Type.Int_NotRequired, name = User.setting_telecontact)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "SettingTypeResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "SettingTypeResponse{status}", remarks = "1-操作成功,0-失败", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "SettingTypeResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void settingType() {
		try {
			String token = getPara("token");
			String message = "";
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("code", 405);
				response2.put("status", 1);
				response2.put("message", "请重新登陆");
				setAttr("SettingTypeResponse", response2);
				renderMultiJson("SettingTypeResponse");
				return;
			}
			String user_id = AppToken.getUserId(token, this);
			ResponseValues response2 = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			int setting_type = getParaToInt("setting_type");
			int setting_message = getParaToInt("setting_message");
			int setting_focus = getParaToInt("setting_focus");
			int setting_voice = getParaToInt("setting_voice");
			int setting_shock = getParaToInt("setting_shock");
			int setting_emotion = getParaToInt("setting_emotion");
			int setting_freedate = getParaToInt("setting_freedate");
			int setting_telecontact = getParaToInt("setting_telecontact");
			User user = User.dao.findById(user_id);
			boolean update = false;
			if (setting_type == 1) {
				if (setting_message == 0) {
					update = user.set(User.setting_message, 0).update();
				} else {
					update = user.set(User.setting_message, 1).update();
				}
			}
			if (setting_type == 2) {
				if (setting_focus == 0) {
					update = user.set(User.setting_focus, 0).update();
				} else {
					update = user.set(User.setting_focus, 1).update();
				}
			}
			if (setting_type == 3) {
				if (setting_voice == 0) {
					update = user.set(User.setting_voice, 0).update();
				} else {
					update = user.set(User.setting_voice, 1).update();
				}
			}
			if (setting_type == 4) {
				if (setting_shock == 0) {
					update = user.set(User.setting_shock, 0).update();
				} else {
					update = user.set(User.setting_shock, 1).update();
				}
			}
			if (setting_type == 5) {
				if (setting_emotion == 0) {
					update = user.set(User.setting_emotion, 0).update();
				} else {
					update = user.set(User.setting_emotion, 1).update();
				}
			}
			if (setting_type == 6) {
				if (setting_freedate == 0) {
					update = user.set(User.setting_freedate, 0).update();
				} else {
					update = user.set(User.setting_freedate, 1).update();
				}
			}
			if (setting_type == 7) {
				if (setting_telecontact == 0) {
					update = user.set(User.setting_telecontact, 0).update();
				} else {
					update = user.set(User.setting_telecontact, 1).update();
				}
			}
			if (update) {
				message = "设置成功";
				response2.put("status", 1);
			} else {
				message = "设置失败";
				response2.put("status", 0);
			}
			response2.put("message", message);
			response2.put("code", 200);
			setAttr("SettingTypeResponse", response2);
			renderMultiJson("SettingTypeResponse");
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("Setting/settingType", "设置", this);
		}
	}

	@Author("cluo")
	@Rp("屏蔽名单")
	@Explaination(info = "屏蔽名单列表")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "1", explain = Value.Infer, type = Type.String, name = "pn")
	@URLParam(defaultValue = "3", explain = Value.Infer, type = Type.String, name = "page_size")
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	// page property
	@ReturnOutlet(name = "BlackListResponse{BlackListResult:blacks:pageNumber}", remarks = "page number", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "BlackListResponse{BlackListResult:blacks:pageSize}", remarks = "result amount of this page", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "BlackListResponse{BlackListResult:blacks:totalPage}", remarks = "total page", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "BlackListResponse{BlackListResult:blacks:totalRow}", remarks = "total row", dataType = DataType.Int, defaultValue = "")
	//
	@ReturnDBParam(name = "BlackListResponse{BlackListResult:blacks:list[Black:$]}", column = BlackList.black_list_id)
	@ReturnDBParam(name = "BlackListResponse{BlackListResult:blacks:list[Black:$]}", column = BlackList.black_user_id)
	@ReturnDBParam(name = "BlackListResponse{BlackListResult:blacks:list[Black:$]}", column = User.image_01)
	@ReturnDBParam(name = "BlackListResponse{BlackListResult:blacks:list[Black:$]}", column = User.nickname)
	@ReturnDBParam(name = "BlackListResponse{BlackListResult:blacks:list[Black:$]}", column = User.sex)
	@ReturnOutlet(name = "BlackListResponse{BlackListResult:blacks:list[Black:age]}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnDBParam(name = "BlackListResponse{BlackListResult:blacks:list[Black:$]}", column = User.city)
	@ReturnOutlet(name = "BlackListResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "BlackListResponse{status}", remarks = "1-操作成功", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "BlackListResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void blackList() {
		try {
			int pn = getParaToInt("pn", 1);
			int page_size = getParaToInt("page_size", 10);
			String message = "";
			String token = getPara("token");
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("code", 405);
				response2.put("message", "请重新登陆");
				setAttr("BlackListResponse", response2);
				renderMultiJson("BlackListResponse");
				return;
			}
			String user_id = AppToken.getUserId(token, this);

			String select2 = "select black_list_id,black_user_id,post_time ";
			ResponseValues responseValues = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			// 地区
			String filter_sql = " type=2 and user_id=" + user_id;

			final Page<BlackList> blackPage = BlackList.dao.paginate(pn, page_size, select2,
					" from black_list where " + filter_sql + " order by post_time desc");
			List<BlackList> blackLists = blackPage.getList();
			for (BlackList blackList : blackLists) {
				String select = "select is_vip,image_01,nickname,sex,birthday,city ";
				int black_user_id = blackList.get(blackList.black_user_id);// 被屏蔽用户ID
				User user = User.dao.findFirst(select + " from user where user_id=" + black_user_id);
				int age_int = 0, sex;
				String image_01 = "", nickname = "", city = "";
				if (user != null) {
					// 出生年月日
					Date birthday = user.getDate("birthday");
					if (birthday != null) {
						String age_date = user.getDate("birthday").toString();
						if (!age_date.equals("") && age_date != null) {
							age_int = DateUtils.getCurrentAgeByBirthdate(age_date);
						}
					}
				}
				image_01 = user.getStr("image_01");
				nickname = user.getStr("nickname");
				sex = user.get("sex");
				city = user.getStr("city");

				blackList.put("image_01", image_01);
				blackList.put("nickname", nickname);
				blackList.put("sex", sex);
				blackList.put("is_vip", user.get("is_vip"));
				blackList.put("city", city);
				blackList.put("age", age_int + "岁");
			}
			responseValues.put("status", 1);
			responseValues.put("code", 200);
			responseValues.put("Result", new HashMap<String, Object>() {
				{
					put("blacks", blackPage);
				}
			});
			responseValues.put("message", "返回成功");
			setAttr("BlackListResponse", responseValues);
			renderMultiJson("BlackListResponse");

		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
			AppData.analyze("Setting/blackList", "屏蔽名单列表", this);
		}
	}

	@Author("cluo")
	@Rp("屏蔽名单")
	@Explaination(info = "删除屏蔽名单")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.Int, name = BlackList.black_list_id)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "DeleteBlackUserResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "DeleteBlackUserResponse{status}", remarks = "1-操作成功，0-失败", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "DeleteBlackUserResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void deleteBlackUser() {
		try {
			String token = getPara("token");
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("DeleteBlackUserResponse", response2);
				renderMultiJson("DeleteBlackUserResponse");
				return;
			}
			int black_list_id = getParaToInt("black_list_id");
			BlackList blackList = BlackList.dao.findById(black_list_id);
			boolean update = false;
			int status = 0;
			String message = "删除失败";
			if (blackList != null) {
				update = blackList.delete();
			}
			if (update) {
				message = "删除成功";
				status = 1;
			}
			response.put("message", message);
			response.put("status", status);
			response.put("code", 200);
			setAttr("DeleteBlackUserResponse", response);
			renderMultiJson("DeleteBlackUserResponse");
			AppLog.info("", getRequest());
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("Setting/deleteBlackUser", "删除", this);
		}
	}

	@Author("cluo")
	@Rp("消息详情")
	@Explaination(info = "举报,屏蔽")
	@UpdateLog(date = "2015-03-24 11:12", log = "初次添加")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.Int, name = BlackList.black_user_id)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.Int, name = BlackList.type)
	@URLParam(defaultValue = "色情#暴力", explain = Value.Infer, type = Type.String_NotRequired, name = ReportBean.report_bean)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "ReportResponse{black_list_id}", remarks = "ID", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "ReportResponse{status}", remarks = "1-成功", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "ReportResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "ReportResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void report() {
		try {
			String token = getPara("token");
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("ReportResponse", response2);
				renderMultiJson("ReportResponse");
				return;
			}
			int status = 0;
			String message = "操作失败";
			String user_id = AppToken.getUserId(token, this);
			int black_user_id = getParaToInt("black_user_id");
			int type = getParaToInt("type");
			BlackList suggest = new BlackList();
			if (type == 1) {
				String report_bean = getPara("report_bean", "");
				suggest.set(suggest.report_beans, report_bean);
			}
			boolean save = suggest.set(suggest.user_id, user_id).set(suggest.black_user_id, black_user_id)
					.set(suggest.type, type).set(suggest.post_time, DateUtils.getCurrentDateTime()).save();
			String report_telephone = User.dao.findById(user_id).getStr(User.telephone);
			String reported_telephone = User.dao.findById(black_user_id).getStr(User.telephone);
			MessageUtils.sendReportMessage(report_telephone);
			MessageUtils.sendReportedMessage(reported_telephone);

			int black_list_id = 0;
			if (save) {
				status = 1;
				if (type == 1) {
					message = "举报成功";
				} else {
					message = "屏蔽成功";
				}
				black_list_id = suggest.get(suggest.black_list_id);
			}
			
			response.put("black_list_id", black_list_id);
			response.put("status", status);
			response.put("message", message);
			response.put("code", 200);
			setAttr("ReportResponse", response);
			renderMultiJson("ReportResponse");
			AppLog.info(null, getRequest());
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("Messages/report", "举报,屏蔽", this);
		}
	}

	@Author("cluo")
	@Rp("喜欢名单")
	@Explaination(info = "喜欢列表")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "{1、2}", explain = "1-我喜欢的，2-喜欢我的", type = Type.String, name = "type")
	@URLParam(defaultValue = "1", explain = Value.Infer, type = Type.String, name = "pn")
	@URLParam(defaultValue = "3", explain = Value.Infer, type = Type.String, name = "page_size")
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	// page property
	@ReturnOutlet(name = "LikeListResponse{LikeListResult:likes:pageNumber}", remarks = "page number", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "LikeListResponse{LikeListResult:likes:pageSize}", remarks = "result amount of this page", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "LikeListResponse{LikeListResult:likes:totalPage}", remarks = "total page", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "LikeListResponse{LikeListResult:likes:totalRow}", remarks = "total row", dataType = DataType.Int, defaultValue = "")
	//
	@ReturnDBParam(name = "LikeListResponse{LikeListResult:likes:list[LikeUser:$]}", column = Collection.collection_id)
	@ReturnDBParam(name = "LikeListResponse{LikeListResult:likes:list[LikeUser:$]}", column = Collection.user_id)
	@ReturnDBParam(name = "LikeListResponse{LikeListResult:likes:list[LikeUser:$]}", column = Collection.collection_user_id)
	@ReturnDBParam(name = "LikeListResponse{LikeListResult:likes:list[LikeUser:$]}", column = User.image_01)
	@ReturnDBParam(name = "LikeListResponse{LikeListResult:likes:list[LikeUser:$]}", column = User.nickname)
	@ReturnDBParam(name = "LikeListResponse{LikeListResult:likes:list[LikeUser:$]}", column = User.sex)
	@ReturnOutlet(name = "LikeListResponse{LikeListResult:likes:list[LikeUser:age]}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnDBParam(name = "LikeListResponse{LikeListResult:likes:list[LikeUser:$]}", column = User.city)
	@ReturnOutlet(name = "LikeListResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "LikeListResponse{status}", remarks = "1-操作成功", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "LikeListResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void likeList() {
		try {
			int pn = getParaToInt("pn", 1);
			int page_size = getParaToInt("page_size", 10);
			String message = "";
			String token = getPara("token");
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this, Thread
						.currentThread().getStackTrace()[1].getMethodName());
				response2.put("code", 405);
				response2.put("message", "请重新登陆");
				setAttr("LikeListResponse", response2);
				renderMultiJson("LikeListResponse");
				return;
			}
			String user_id = AppToken.getUserId(token, this);
			int type = getParaToInt("type", 1);
			String select2 = "select collection_id,user_id,collection_user_id,post_time ";
			ResponseValues responseValues = new ResponseValues(this, Thread
					.currentThread().getStackTrace()[1].getMethodName());
			String filter_sql = "";
			if (type == 1) {
				// 我喜欢的
				filter_sql = " user_id=" + user_id;
			} else {
				// 喜欢我的
				filter_sql = " collection_user_id=" + user_id;
			}
			final Page<Collection> likePage = Collection.dao.paginate(pn,
					page_size, select2, " from collection where " + filter_sql
							+ " order by post_time desc");
			List<Collection> collections = likePage.getList();
			 for (Iterator iter = collections.iterator(); iter.hasNext();) {
				 Collection collection = (Collection) iter.next();
				String select = "select image_01,nickname,sex,birthday,city,height,hope,is_vip ";
				User user = null;
				if (type == 1) {
					// 我喜欢的
					int collection_user_id = collection.get(collection.collection_user_id);
					user = User.dao.findFirst(select+ " from user where user_id=" + collection_user_id);
				} else {
					// 喜欢我的
					int liked_user_id = collection.get(collection.user_id);
					user = User.dao.findFirst(select+ " from user where user_id=" + liked_user_id);
				}
				int age_int = 0, sex;
				String image_01 = "", nickname = "", city = "";
				if(user == null){
					iter.remove();
					continue;
				}
				if (user != null) {
					// 出生年月日
					Date birthday = user.getDate("birthday");
					if (birthday != null) {
						String age_date = user.getDate("birthday").toString();
						if (!age_date.equals("") && age_date != null) {
							age_int = DateUtils
									.getCurrentAgeByBirthdate(age_date);
						}
					}
				}
				image_01 = user.getStr("image_01");
				nickname = user.getStr("nickname");
				sex = user.get("sex");
				city = user.getStr("city");

				collection.put("image_01", image_01);
				collection.put("nickname", nickname);
				collection.put("sex", sex);
				collection.put("is_vip", user.get("is_vip"));
				collection.put("city", city);
				collection.put("age", age_int + "岁");
			}
			responseValues.put("status", 1);
			responseValues.put("code", 200);
			responseValues.put("Result", new HashMap<String, Object>() {
				{
					put("likes", likePage);
				}
			});
			responseValues.put("message", "返回成功");
			setAttr("LikeListResponse", responseValues);
			renderMultiJson("LikeListResponse");

		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
			AppData.analyze("Setting/likeList()", "喜欢列表", this);
		}
	}

	@Author("cluo")
	@Rp("喜欢名单")
	@Explaination(info = "删除名单")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.Int, name = Collection.collection_id)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "DeleteLikeUserResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "DeleteLikeUserResponse{status}", remarks = "1-操作成功，0-失败", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "DeleteLikeUserResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void deleteLikeUser() {
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
				setAttr("DeleteLikeUserResponse", response2);
				renderMultiJson("DeleteLikeUserResponse");
				return;
			}
			int collection_id = getParaToInt("collection_id");
			Collection collection = Collection.dao.findById(collection_id);
			boolean update = false;
			int status = 0;
			String message = "删除失败";
			if (collection != null) {
				update = collection.delete();
			}
			if (update) {
				message = "删除成功";
				status = 1;
			}
			response.put("message", message);
			response.put("status", status);
			response.put("code", 200);
			setAttr("DeleteLikeUserResponse", response);
			renderMultiJson("DeleteLikeUserResponse");
			AppLog.info("", getRequest());
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("Setting/deleteLikeUser", "删除", this);
		}
	}

	@Author("cluo")
	@Rp("注销账户")
	@Explaination(info = "注销账户")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.Int, name = User.password)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "CancelUserResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "CancelUserResponse{status}", remarks = "2-密码错误，1-操作成功，0-失败", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "CancelUserResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void cancelUser() {
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
				setAttr("CancelUserResponse", response2);
				renderMultiJson("CancelUserResponse");
				return;
			}
			String user_id = AppToken.getUserId(token, this);
			String password = getPara("password");
			User user = User.dao.findById(user_id);
			boolean update = false;
			int status = 0;
			String message = "注销账户失败";
			if (user != null) {
				String user_pwd = user.getStr("password");
				if (MD5Util.string2MD5(password).equals(user_pwd)) {

					UserOld userOld = new UserOld();
					String birthday = user.getDate("birthday").toString();
					String last_login_time2 = user.getTimestamp("last_login_time").toString();
					String regist_time = user.getTimestamp("regist_time").toString();
					String regist_date = user.getDate("regist_date").toString();

					Timestamp vip_from_datetime0 = user.getTimestamp("vip_from_datetime");
					String vip_from_datetime = "";
					if (vip_from_datetime0 != null) {
						vip_from_datetime = user.getTimestamp("vip_from_datetime").toString();
						userOld.set(userOld.vip_from_datetime, vip_from_datetime);
					}
					Timestamp vip_end_datetime0 = user.getTimestamp("vip_end_datetime");
					String vip_end_datetime = "";
					if (vip_end_datetime0 != null) {
						vip_end_datetime = user.getTimestamp("vip_end_datetime").toString();
						userOld.set(userOld.vip_end_datetime, vip_end_datetime);
					}
					boolean save_old = userOld.set(userOld.telephone, user.getStr(user.telephone))
							.set(userOld.password, user.getStr(user.password))
							.set(userOld.nickname, user.getStr(user.nickname)).set(userOld.birthday, birthday)
							.set(userOld.job, user.getStr(user.job)).set(userOld.height, user.getStr(user.height))
							.set(userOld.sex, user.get(user.sex)).set(userOld.taste, user.get(user.taste))
							.set(userOld.image_01, user.getStr(user.image_01))
							.set(userOld.image_02, user.getStr(user.image_02))
							.set(userOld.image_03, user.getStr(user.image_03))
							.set(userOld.image_04, user.getStr(user.image_04))
							.set(userOld.image_05, user.getStr(user.image_05))
							.set(userOld.image_06, user.getStr(user.image_06))
							.set(userOld.is_vip, user.get(user.is_vip)).set(userOld.heart, user.getStr(user.heart))
							.set(userOld.income, user.getStr(user.income))
							.set(userOld.province, user.getStr(user.province)).set(userOld.city, user.getStr(user.city))
							.set(userOld.edu, user.getStr(user.edu)).set(userOld.shape, user.getStr(user.shape))
							.set(userOld.marry, user.getStr(user.marry)).set(userOld.drink, user.getStr(user.drink))
							.set(userOld.last_login_time, last_login_time2).set(userOld.hot, user.get(user.hot))
							.set(userOld.sweet, user.get(user.sweet)).set(userOld.regist_time, regist_time)
							.set(userOld.regist_date, regist_date)
							.set(userOld.regist_hour, user.getStr(user.regist_hour))
							.set(userOld.status, user.get(user.status)).save();
					if (save_old) {
						update = user.delete();
						if (update) {
							// 黑名单
							List<BlackList> blackLists = BlackList.dao
									.find("select black_list_id from black_list where user_id=" + user_id);
							for (BlackList blackList : blackLists) {
								int black_list_id = blackList.get("black_list_id");
								blackList.deleteById(black_list_id);
							}
							// 浏览记录
							List<Browse> browses = Browse.dao.find("select browse_id from browse where user_id="
									+ user_id + " or browse_user_id=" + user_id);
							for (Browse browse : browses) {
								int browse_id = browse.get("browse_id");
								browse.deleteById(browse_id);
							}
							// 充值记录
							List<Charge> charges = Charge.dao
									.find("select charge_id from charge where user_id=" + user_id);
							for (Charge charge : charges) {
								int charge_id = charge.get("charge_id");
								charge.deleteById(charge_id);
							}
							// 喜欢记录
							List<Collection> collections = Collection.dao
									.find("select collection_id from collection where user_id=" + user_id
											+ " or collection_user_id=" + user_id);
							for (Collection collection : collections) {
								int collection_id = collection.get("collection_id");
								collection.deleteById(collection_id);
							}
							// 搜索记录
							List<Search> searchs = Search.dao
									.find("select search_id from search where user_id=" + user_id);
							for (Search search : searchs) {
								int search_id = search.get("search_id");
								search.deleteById(search_id);
							}
							// 标签
							List<UserTag> userTags = UserTag.dao
									.find("select user_tag_id from user_tag where user_id=" + user_id);
							for (UserTag userTag : userTags) {
								int user_tag_id = userTag.get("user_tag_id");
								userTag.deleteById(user_tag_id);
							}
						}
					}
				} else {
					message = "密码错误";
					status = 2;
				}
			}
			if (update) {
				message = "注销账户成功";
				status = 1;
			}
			response.put("message", message);
			response.put("status", status);
			response.put("code", 200);
			setAttr("CancelUserResponse", response);
			renderMultiJson("CancelUserResponse");
			AppLog.info("", getRequest());
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("Setting/cancelUser", "删除", this);
		}
	}

	@Author("cluo")
	@Rp("消息详情")
	@Explaination(info = "举报文字列表-不用提示")
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnDBParam(name = "ReportBeansResponse{ReportBeans:list[ReportBean:$]}", column = ReportBean.report_bean_id)
	@ReturnDBParam(name = "ReportBeansResponse{ReportBeans:list[ReportBean:$]}", column = ReportBean.report_bean)
	@ReturnOutlet(name = "ReportBeansResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "ReportBeansResponse{status}", remarks = "1-操作成功，0-失败", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "ReportBeansResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void reportBeans() {
		try {
			List<ReportBean> reportBeans = ReportBean.dao
					.find("select report_bean_id,report_bean from report_bean order by post_time desc");
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			response.put("ReportBeans", reportBeans);
			response.put("message", "成功");
			response.put("status", 1);
			response.put("code", 200);
			setAttr("ReportBeansResponse", response);
			renderMultiJson("ReportBeansResponse");
			AppLog.info("", getRequest());
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("Setting/reportBeans", "举报文字列表", this);
		}
	}

	@Author("cluo")
	@Rp("设置")
	@Explaination(info = "存储手机联系人【不需要提示】")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "{15889610183#15889610184}", explain = "手机联系人列表", type = Type.String, name = "cellphones")
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnDBParam(name = "CellPhoneResponse{ReportBeans:list[ReportBean:$]}", column = ReportBean.report_bean_id)
	@ReturnDBParam(name = "CellPhoneResponse{ReportBeans:list[ReportBean:$]}", column = ReportBean.report_bean)
	@ReturnOutlet(name = "CellPhoneResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "CellPhoneResponse{status}", remarks = "1-操作成功，0-失败", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "CellPhoneResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void cellPhone() {
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
				setAttr("CellPhoneResponse", response2);
				renderMultiJson("CellPhoneResponse");
				return;
			}
			String user_id = AppToken.getUserId(token, this);
			String cellphones = getPara("cellphones", "");
			if (!cellphones.equals("")) {
				String[] cellphoneList = cellphones.split("#");
				for (int i = 0; i < cellphoneList.length; i++) {
					String cellphone = cellphoneList[i];
					CellPhone cellPhone3 = CellPhone.dao.findFirst(
							"select user_id from cell_phone where telephone=? and user_id=?", cellphone, user_id);
					if (cellPhone3 == null) {
						CellPhone cellPhone2 = new CellPhone();
						cellPhone2.set(cellPhone2.user_id, user_id).set(cellPhone2.telephone, cellphone)
								.set(cellPhone2.post_time, DateUtils.getCurrentDateTime()).save();
					}
				}
			}
			response.put("message", "成功");
			response.put("status", 1);
			response.put("code", 200);
			setAttr("CellPhoneResponse", response);
			renderMultiJson("CellPhoneResponse");
			AppLog.info("", getRequest());
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("Setting/cellPhone", "存储手机联系人", this);
		}
	}

	@Author("cluo")
	@Rp("我")
	@Explaination(info = "自动更新")
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "AutoUpdateResponse{version}", remarks = "你懂的", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "AutoUpdateResponse{releaseNotes}", remarks = "你懂的", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "AutoUpdateResponse{trackViewUrl}", remarks = "你懂的", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "AutoUpdateResponse{status}", remarks = "1-有更新，0-无更新", dataType = DataType.Int, defaultValue = "")
	public void autoUpdate() {
		try {
			String version = "", releaseNotes = "", trackViewUrl = "";
			ResponseValues response2 = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			response2.put("status", 0);
			response2.put("version", "");
			response2.put("releaseNotes", releaseNotes);
			response2.put("trackViewUrl", trackViewUrl);
			setAttr("AutoUpdateResponse", response2);
			renderMultiJson("AutoUpdateResponse");
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
			AppData.analyze("Setting/autoUpdate", "自动更新", this);
		}
	}
}
