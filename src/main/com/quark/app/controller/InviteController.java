package com.quark.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jfinal.upload.UploadFile;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.quark.admin.controller.InvitationPrice;
import com.quark.api.auto.bean.ResponseValues;
import com.quark.app.logs.AppLog;
import com.quark.common.AppData;
import com.quark.common.RongToken;
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
	
	/**
	 * @api {get} /app/InviteController/list 邀约显示
	 * @apiDescription 邀约首页的显示和邀约分类显示
	 * @apiGroup InviteController
	 * @apiParam {String} token  用户的Token.
	 * @apiParam {String} latitude =30.344  用户的纬度
	 * @apiParam {String} longitude =120.00 用户的经度
	 * @apiParam {String} [invite_type_id]  邀约的类型id：1-旅游，2-美食，3-唱歌，4-电影，5-运动，6-文艺，不填查询全部
	 * @apiVersion 1.0.0
	 * @apiSuccessExample {json} Success-Response:
	 * {
	 *  "InviteResponse": {
	 *      "listResult": {
	 *          "topList": [
	 *          {
     *              "invite_content": "唱跳rap篮球", 	//邀约内容
     *              "invite_explain": "i want to play basketball",	//邀约说明
     *              "distance": 12188067,	//邀约发起人与用户的距离（米）
     *              "city": null,	//邀约城市
     *              "image_01": "",	//邀约图片
     *              "sex": 0,	//邀约对象性别：0-男，1-女，2-不限
     *              "is_top": 1,	//邀约是否置顶：1-是，0-否
     *              "cost_id": 1,	//邀约费用id：1-我买单，2-AA制，3-来回机票我承担，4-期望对方承担
     *              "nickname": "兰兰",	//邀约发起人昵称
     *              "invite_type_id": 5,	//邀约类型id：1-旅游，2-美食，3-唱歌，4-电影，5-运动，6-文艺
     *              "job": "助理",	//邀约发起人工作
     *              "age": 28,	//邀约发起人年龄
     *              "height": "180CM及以上"	//邀约发起人身高
     *          }
     *          ],
	 *          "list": [
	 *              {
	 *                  "invite_content": "唱跳rap篮球",	//邀约内容
	 *                  "invite_explain": "i want to play basketball",	//邀约说明
	 *                  "distance": 8911148,	//邀约发起人与用户的距离（米）
	 *                  "city": "广州市",	//邀约城市
	 *                  "image_01": "1456109546949.jpg",	//邀约图片
	 *                  "sex": 1,	//邀约对象性别：0-男，1-女，2-不限
	 *                  "is_top": 0,	//邀约是否置顶：1-是，0-否
	 *                  "cost_id": 1,	//邀约费用id：1-我买单，2-AA制，3-来回机票我承担，4-期望对方承担
	 *                  "nickname": "xiaoguan",	//邀约发起人昵称
	 *                  "invite_type_id": 4,	//邀约类型id：1-旅游，2-美食，3-唱歌，4-电影，5-运动，6-文艺
	 *                  "job": "IT",	//邀约发起人工作
	 *                  "age": 36,	//邀约发起人年龄
	 *                  "height": "180CM以上"	//邀约发起人身高
	 *              }
	 *          ]
	 *      },
	 *      "code": 200,	//除了200均为错误，405需要重新登录
	 *      "message": "",	//返回的信息
	 *      "status": 1	//状态为1为成功，0为失败
	 *  }
	 * }
	 * */
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
					+ " FROM `user` AS u INNER JOIN invite AS i ON u.`user_id` = i.`user_id` where i.is_top = 1 ORDER BY i.`top_date` DESC LIMIT 0,3");
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

	/**
	 * @api {get} /app/InviteController/screenList 邀约筛选	 
	 * @apiGroup InviteController
	 * @apiDescription 邀约筛选查询
	 * @apiParam {String} token  用户的Token.
	 * @apiParam {String} sex  筛选邀约发起人的性别：0-女，1-男，2-全部.
	 * @apiParam {String} sort  筛选方式：new-最新发布，place-离我最近
	 * @apiParam {String} latitude =30.344  用户的纬度
	 * @apiParam {String} longitude =120.00 用户的经度
	 * @apiParam {String} province  筛选省
	 * @apiParam {int} is_video  是否筛选视频认证：0-关闭视频认证筛选，1-开启视频认证筛选
	 * @apiVersion 1.0.0
	 * @apiSuccessExample {json} Success-Response:
	 * {
	 *  "InviteResponse": {
	 *      "listResult": {
	 *          "topList": [
	 *          {
     *              "invite_content": "唱跳rap篮球", 	//邀约内容
     *              "invite_explain": "i want to play basketball",	//邀约说明
     *              "distance": 12188067,	//邀约发起人与用户的距离
     *              "city": null,	//邀约城市
     *              "image_01": "",	//邀约图片
     *              "sex": 0,	//邀约对象性别，0-男：1-女，2-不限
     *              "is_top": 1,	//邀约是否置顶：1-是，0-否
     *              "cost_id": 1,	//邀约费用id：1-我买单，2-AA制，3-来回机票我承担，4-期望对方承担
     *              "nickname": "兰兰",	//邀约发起人昵称
     *              "invite_type_id": 5,	//邀约类型id：1-旅游，2-美食，3-唱歌，4-电影，5-运动，6-文艺
     *              "job": "助理",	//邀约发起人工作
     *              "age": 28,	//邀约发起人年龄
     *              "height": "180CM及以上"	//邀约发起人身高
     *          }
     *          ],
	 *          "list": [
	 *              {
	 *                  "invite_content": "唱跳rap篮球",	//邀约内容
	 *                  "invite_explain": "i want to play basketball",	//邀约说明
	 *                  "distance": 8911148,	//邀约发起人与用户的距离
	 *                  "city": "广州市",	//邀约城市
	 *                  "image_01": "1456109546949.jpg",	//邀约图片
	 *                  "sex": 1,	//邀约对象性别：0-男，1-女，2-不限
	 *                  "is_top": 0,	//邀约是否置顶：1-是，0-否
	 *                  "cost_id": 1,	//邀约费用id：1-我买单，2-AA制，3-来回机票我承担，4-期望对方承担
	 *                  "nickname": "xiaoguan",	//邀约发起人昵称
	 *                  "invite_type_id": 4,	//邀约类型id：1-旅游，2-美食，3-唱歌，4-电影，5-运动，6-文艺
	 *                  "job": "IT",	//邀约发起人工作
	 *                  "age": 36,	//邀约发起人年龄
	 *                  "height": "180CM以上"	//邀约发起人身高
	 *              }
	 *          ]
	 *      },
	 *      "code": 200,	//除了200均为错误，405需要重新登录
	 *      "message": "",	//返回的信息
	 *      "status": 1	//状态为1为成功，0为失败
	 *  }
	 * }
	 * */
	public void screenList() {
		try {
			boolean save = false;
			String message = "";
			String sex = getPara("sex");
			String sort = getPara("sort");
			String filterSql = "WHERE ";
			if(sex.equals("2")) {
				filterSql = filterSql + "1=1";
			} else {
				filterSql = filterSql + "u.sex = "+sex;
			}
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
			String latitude = getPara("latitude", "30.344");
			String longitude = getPara("longitude", "120.00");
			String province = getPara("province");
			int is_video = getParaToInt("is_video");
			List<Invite> iList = new ArrayList<Invite>();
			if(sort.equals("new")) {
				iList = Invite.dao.find("SELECT u.nickname,u.city,u.image_01,u.job,"
						+ "u.birthday,u.height,i.invite_content,i.cost_id,i.invite_explain,u.sex,i.is_top,i.invite_type_id,"
						+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN(("+latitude+"*PI()/180-u.latitude*PI()/180)/2),2)+COS("+latitude+"*PI()/180)*COS(u.latitude*PI()/180)*POW"
						+ "(SIN(("+longitude+"*PI()/180-u.longitude*PI()/180)/2),2)))*1000) AS distance"
						+ " FROM `user` AS u INNER JOIN invite AS i ON u.`user_id` = i.`user_id` "+filterSql+" AND i.invite_province = '"+province+"' AND u.is_video = "+is_video+" ORDER BY i.invite_id DESC");
			} else {
				iList = Invite.dao.find("SELECT u.nickname,u.city,u.image_01,u.job,"
						+ "u.birthday,u.height,i.invite_content,i.cost_id,i.invite_explain,u.sex,i.is_top,i.invite_type_id,"
						+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN(("+latitude+"*PI()/180-u.latitude*PI()/180)/2),2)+COS("+latitude+"*PI()/180)*COS(u.latitude*PI()/180)*POW"
						+ "(SIN(("+longitude+"*PI()/180-u.longitude*PI()/180)/2),2)))*1000) AS distance"
						+ " FROM `user` AS u INNER JOIN invite AS i ON u.`user_id` = i.`user_id` "+filterSql+" AND i.invite_province = '"+province+"' AND u.is_video = "+is_video+" ORDER BY distance ASC");
			}
			final List<Invite> iList2 = iList;
			final List<Invite> topList = Invite.dao.find("SELECT u.nickname,u.city,u.image_01,u.job,u.sex,"
					+ "u.height,i.invite_content,i.cost_id,i.invite_explain,i.is_top,i.invite_type_id,"
					+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN(("+latitude+"*PI()/180-u.latitude*PI()/180)/2),2)+COS("+latitude+"*PI()/180)*COS(u.latitude*PI()/180)*POW"
					+ "(SIN(("+longitude+"*PI()/180-u.longitude*PI()/180)/2),2)))*1000) AS distance ,DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(u.birthday)), '%Y')+0 AS age"
					+ " FROM `user` AS u INNER JOIN invite AS i ON u.`user_id` = i.`user_id` WHERE i.is_top = 1 ORDER BY i.`top_date` DESC LIMIT 0,3");
			
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
	
	/**
	 * @api {post} /app/InviteController/saveTravel 添加旅行邀约
	 * @apiGroup InviteController
	 * @apiDescription 用户添加旅行邀约
	 * @apiParam {String} token  用户的Token.
	 * @apiParam {String} explain_url  邀约说明图片
	 * @apiParam {int} cost_id  邀约费用id：1-我买单，2-AA制，3-来回机票我承担，4-期望对方承担
	 * @apiParam {int} invite_sex 邀约对象性别：0-男，1-女，2-不限
	 * @apiParam {String} travel_days_id 出行天数：1-当天往返，2-预计1~2天，3-预计2~3天，4-预计3~5天，5-预计10天半个月，6-还准备回来吗？
	 * @apiParam {String} invite_content  旅行目的地
	 * @apiParam {String} travel_mode_id  出行方式：1-往返坐飞机，2-动车高铁，3-自驾，4-骑行
	 * @apiParam {String} is_equal_place  有相同目的地的异性是否通知我 0-是 1-否
	 * @apiParam {String} invite_explain  邀约说明
	 * @apiParam {int} time_id  邀约时间：4-双方商议具体时间，5-最近出发，6-某个周末，7-说走就走
	 * @apiVersion 1.0.0
	 * @apiSuccessExample {json} Success-Response:
	 * {
	 *     "InviteResponse": {
	 *         "code": 200,	//除了200均为错误，405需要重新登录
	 *         "message": "发布成功",	///返回的信息
	 *         "status": 1	//状态为1为成功，0为失败
	 *     }
	 * }
	 * */
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
			//render("/app/InviteController/list");
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
		}
	}
	
	/**
	 * @api {post} /app/InviteController/saveFood 添加美食邀约
	 * @apiGroup InviteController
	 * @apiDescription 用户添加美食邀约
	 * @apiParam {String} token  用户的Token.
	 * @apiParam {String} explain_url  邀约说明图片
	 * @apiParam {int} cost_id  邀约费用id：1-我买单，2-AA制
	 * @apiParam {int} invite_sex 邀约对象性别：0-男，1-女，2-不限
	 * @apiParam {String} invite_province 邀约省
	 * @apiParam {String} invite_city 邀约市
	 * @apiParam {String} invite_place 邀约地点
	 * @apiParam {String} invite_content  邀约食物
	 * @apiParam {String} invite_explain  邀约说明
	 * @apiParam {int} invite_receive  是否由我接送 0-是 1-不是
	 * @apiParam {int} time_id  邀约时间：1-不限时间，2-任何休息日，3-不常周末，4-双方商议具体时间
	 * @apiVersion 1.0.0
	 * @apiSuccessExample {json} Success-Response:
	 * {
	 *     "InviteResponse": {
	 *         "code": 200,	//除了200均为错误，405需要重新登录
	 *         "message": "发布成功",	///返回的信息
	 *         "status": 1	//状态为1为成功，0为失败
	 *     }
	 * }
	 * */
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
			//render("/app/InviteController/list");
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
		}
	}
	
	/**
	 * @api {post} /app/InviteController/saveSing 添加唱歌邀约
	 * @apiGroup InviteController
	 * @apiDescription 用户添加唱歌邀约
	 * @apiParam {String} token  用户的Token.
	 * @apiParam {String} explain_url  邀约说明图片
	 * @apiParam {int} cost_id  邀约费用id：1-我买单，2-AA制
	 * @apiParam {int} invite_sex 邀约对象性别：0-男，1-女，2-不限
	 * @apiParam {String} invite_province 邀约省
	 * @apiParam {String} invite_city 邀约市
	 * @apiParam {String} invite_place 邀约地点
	 * @apiParam {String} invite_explain  邀约说明
	 * @apiParam {int} invite_receive  是否由我接送 0-是 1-不是
	 * @apiParam {int} time_id  邀约时间：1-不限时间，2-任何休息日，3-不常周末，4-双方商议具体时间
	 * @apiVersion 1.0.0
	 * @apiSuccessExample {json} Success-Response:
	 * {
	 *     "InviteResponse": {
	 *         "code": 200,	//除了200均为错误，405需要重新登录
	 *         "message": "发布成功",	///返回的信息
	 *         "status": 1	//状态为1为成功，0为失败
	 *     }
	 * }
	 * */
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
			//render("/app/InviteController/list");
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
		}
	}
	
	/**
	 * @api {post} /app/InviteController/findData 加载选项数据
	 * @apiGroup InviteController
	 * @apiDescription 添加邀约时需要的选项数据
	 * @apiParam {String} token  用户的Token.
	 * @apiParam {String} invite_type_id  邀约的类型id：1-旅游，2-美食，3-唱歌，4-电影，5-运动，6-文艺
	 * @apiVersion 1.0.0
	 * @apiSuccessExample {json} Success-Response:
	 * {
    "InviteResponse": {
        "code": 200,
        "List": {
            "daysList": [	//旅行天数
                {
                    "travel_days_id": 1,
                    "travel_days_name": "当天往返"
                },...
            ],
            "timeList": [	//邀约时间
                {
                    "time_name": "双方商议具体时间"
                },...
            ],
            "costList": [	//付款方式
                {
                    "cost_id": 1,
                    "cost_name": "我买单"
                },...
            ],
            "modeList": [	//旅游出行方式
                {
                    "travel_mode_name": "往返坐飞机",
                    "travel_mode_id": 1
                },...
            ]
        }
    }
}
	 * */
	public void findData() {
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
				inviteTimeList = inviteTime.dao.find("select * from invite_time limit 0,4");
				map.put("timeList", inviteTimeList);
			} else {
				List<InviteCost> costList = InviteCost.dao.find("select * from invite_cost"); 
				List<TravelDays> daysList = TravelDays.dao.find("select * from travel_days"); 
				List<TravelMode> modeList = TravelMode.dao.find("select * from travel_mode"); 
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
	
	/**
	 * @api {post} /app/InviteController/saveMovie 添加电影邀约
	 * @apiGroup InviteController
	 * @apiDescription 用户添加电影邀约
	 * @apiParam {String} token  用户的Token.
	 * @apiParam {String} explain_url  邀约说明图片
	 * @apiParam {int} cost_id  邀约费用id：1-我买单，2-AA制
	 * @apiParam {int} invite_sex 邀约对象性别：0-男，1-女，2-不限
	 * @apiParam {String} invite_province 邀约省
	 * @apiParam {String} invite_city 邀约市
	 * @apiParam {String} invite_place 邀约地点
	 * @apiParam {String} invite_content  邀约电影
	 * @apiParam {String} invite_explain  邀约说明
	 * @apiParam {int} invite_receive  是否由我接送 0-是 1-不是
	 * @apiParam {int} time_id  邀约时间：1-不限时间，2-任何休息日，3-不常周末，4-双方商议具体时间
	 * @apiVersion 1.0.0
	 * @apiSuccessExample {json} Success-Response:
	 * {
	 *     "InviteResponse": {
	 *         "code": 200,	//除了200均为错误，405需要重新登录
	 *         "message": "发布成功",	///返回的信息
	 *         "status": 1	//状态为1为成功，0为失败
	 *     }
	 * }
	 * */
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
			//render("/app/InviteController/list");
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
		}
	}
	
	/**
	 * @api {post} /app/InviteController/saveMotion 添加运动邀约
	 * @apiGroup InviteController
	 * @apiDescription 用户添加运动邀约
	 * @apiParam {String} token  用户的Token.
	 * @apiParam {String} explain_url  邀约说明图片
	 * @apiParam {int} cost_id  邀约费用id：1-我买单，2-AA制
	 * @apiParam {int} invite_sex 邀约对象性别：0-男，1-女，2-不限
	 * @apiParam {String} invite_province 邀约省
	 * @apiParam {String} invite_city 邀约市
	 * @apiParam {String} invite_place 邀约地点
	 * @apiParam {String} invite_content  邀约运动
	 * @apiParam {String} invite_explain  邀约说明
	 * @apiParam {int} invite_receive  是否由我接送 0-是 1-不是
	 * @apiParam {int} is_carry_bestie  是否可以携带闺蜜 0-是 1-否
	 * @apiParam {int} time_id  邀约时间：1-不限时间，2-任何休息日，3-不常周末，4-双方商议具体时间
	 * @apiVersion 1.0.0
	 * @apiSuccessExample {json} Success-Response:
	 * {
	 *     "InviteResponse": {
	 *         "code": 200,	//除了200均为错误，405需要重新登录
	 *         "message": "发布成功",	///返回的信息
	 *         "status": 1	//状态为1为成功，0为失败
	 *     }
	 * }
	 * */
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
			//render("/app/InviteController/list");
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
		}
	}
	
	/**
	 * @api {post} /app/InviteController/saveLiterature 添加文艺邀约
	 * @apiGroup InviteController
	 * @apiDescription 用户添加文艺邀约
	 * @apiParam {String} token  用户的Token.
	 * @apiParam {String} explain_url  邀约说明图片
	 * @apiParam {int} cost_id  邀约费用id：1-我买单，2-AA制
	 * @apiParam {int} invite_sex 邀约对象性别：0-男，1-女，2-不限
	 * @apiParam {String} invite_province 邀约省
	 * @apiParam {String} invite_city 邀约市
	 * @apiParam {String} invite_place 邀约地点
	 * @apiParam {String} invite_content  邀约文艺
	 * @apiParam {String} invite_explain  邀约说明
	 * @apiParam {int} invite_receive  是否由我接送 0-是 1-不是
	 * @apiParam {int} is_carry_bestie  是否可以携带闺蜜 0-是 1-否
	 * @apiParam {int} time_id  邀约时间：1-不限时间，2-任何休息日，3-不常周末，4-双方商议具体时间
	 * @apiVersion 1.0.0
	 * @apiSuccessExample {json} Success-Response:
	 * {
	 *     "InviteResponse": {
	 *         "code": 200,	//除了200均为错误，405需要重新登录
	 *         "message": "发布成功",	///返回的信息
	 *         "status": 1	//状态为1为成功，0为失败
	 *     }
	 * }
	 * */
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
			//render("/app/InviteController/list");
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
		}
	}
	
	
	//邀约详情
	/**
	 * @api {get} /app/InviteController/details 邀约详情
	 * @apiGroup InviteController
	 * @apiDescription 邀约详情接口
	 * @apiParam {String} token  用户的Token.
	 * @apiParam {int} invite_id  邀约id.
	 * @apiVersion 1.0.0
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * {
    "InviteResponse": {
        "code": 200,	//除了200均为错误，405需要重新登录
        "detailsResult": {
            "detailsList": [
                {
                    "invite_content": "唱跳rap篮球",	//邀约内容
                    "type_name": "电影",	//类型名称
                    "invite_explain": "i want to play basketball",	//邀约说明
                    "distance": 2315181,	//距离
                    "travel_mode_name": "自驾",	//出行方式
                    "hot": 26,	//热度
                    "travel_mode_id": 3,	//出行方式id
                    "travel_days_id": 4,	//类型id
                    "time_name": "任何休息日",	//邀约时间
                    "invite_province": "云南",	//邀约省份
                    "travel_days_name": "预计3~5天",	//出行时间
                    "nickname": "xiaoguan",	//用户昵称
                    "cost_name": "我买单",	//费用类型
                    "explain_url": "1560222965884.jpg",	//说明图片
                    "invite_sex": 0,	//邀约性别 0-男 1-女 2-不限
                    "invite_receive": 1,	//是否由我接送 0-是 1-不是
                    "invite_id": 1,	
                    "time_id": 2,	//邀约时间id
                    "image_01": "1456109546949.jpg",	//用户头像
                    "is_top": 1,	//是否置顶
                    "cost_id": 1,	//邀约费用id
                    "invite_place": "滇池路",	//邀约地点（自填）
                    "user_id": 33,	//用户id
                    "invite_city": "昆明",	//邀约城市
                    "invite_type_id": 4,	//邀约类型id
                    "top_date": "2019-06-14 11:49:13",	//置顶时间
                    "age": 36,	//用户年龄
                    "is_equal_place": 0,	//有相同目的地的异性是否通知我 0-是 1-否
                    "is_carry_bestie": 0	//是否可以携带闺蜜 0-是 1-否
                }
            ]
        }
    }
}
	 * */
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
					+ "i.*,ic.cost_name,it.time_name,tm.travel_mode_name,itpe.type_name,td.travel_days_name,"
					+ "ROUND(6378.138*2*ASIN(SQRT(POW(SIN(("+latitude+"*PI()/180-u.latitude*PI()/180)/2),2)+COS("+latitude+"*PI()/180)*COS(u.latitude*PI()/180)*POW"
					+ "(SIN(("+longitude+"*PI()/180-u.longitude*PI()/180)/2),2)))*1000) AS distance,DATE_FORMAT(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(birthday)), '%Y')+0 AS age"
					+ " FROM `user` AS u INNER JOIN invite AS i ON u.`user_id` = i.`user_id` "
					+ "LEFT JOIN invite_cost AS ic ON i.cost_id = ic.cost_id LEFT JOIN invite_type AS itpe ON i.invite_type_id = itpe.type_id "
					+ "LEFT JOIN invite_time AS it ON i.time_id = it.time_id LEFT JOIN travel_mode AS tm ON i.travel_mode_id = tm.travel_mode_id "
					+ "LEFT JOIN travel_days AS td ON i.travel_days_id = td.travel_days_id"
					+filter_sql+"i.invite_id="+invite_id+" ORDER BY i.`is_top` DESC");
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
	/**
	 * @api {post} /app/InviteController/payTop 购买邀约置顶
	 * @apiGroup InviteController
	 * @apiDescription 购买邀约置顶接口
	 * @apiParam {String} token  用户的Token.
	 * @apiVersion 1.0.0
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 * {
	 *	"InviteResponse": {
	 *		"code": 400,		//除了200均为错误，405需要重新登录
	 *		"message": "置顶成功",	//返回的信息
	 *		"status": 1		//状态为1为成功，0为失败
	 *	}
	 *}
	 * */
	@Before(Tx.class)
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
