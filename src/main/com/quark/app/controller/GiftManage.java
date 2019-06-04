/**
 * 
 */
package com.quark.app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import com.quark.admin.controller.api;
import com.quark.api.annotation.Author;
import com.quark.api.annotation.DataType;
import com.quark.api.annotation.Explaination;
import com.quark.api.annotation.ReturnDBParam;
import com.quark.api.annotation.ReturnJson;
import com.quark.api.annotation.ReturnOutlet;
import com.quark.api.annotation.Rp;
import com.quark.api.annotation.Type;
import com.quark.api.annotation.URLParam;
import com.quark.api.annotation.UpdateLog;
import com.quark.api.annotation.Value;
import com.quark.api.auto.bean.ResponseValues;
import com.quark.app.logs.AppLog;
import com.quark.common.AppData;
import com.quark.interceptor.AppToken;
import com.quark.mail.SendMail;
import com.quark.model.extend.BlackList;
import com.quark.model.extend.Browse;
import com.quark.model.extend.Charge;
import com.quark.model.extend.Collection;
import com.quark.model.extend.Gift;
import com.quark.model.extend.MyGift;
import com.quark.model.extend.Search;
import com.quark.model.extend.Sweet;
import com.quark.model.extend.Tag;
import com.quark.model.extend.Tokens;
import com.quark.model.extend.User;
import com.quark.model.extend.UserTag;
import com.quark.utils.DateUtils;
import com.quark.utils.FileUtils;
import com.quark.utils.MD5Util;
import com.quark.utils.MessageUtils;
import com.quark.utils.RandomUtils;
import com.quark.utils.StringUtils;

/**
 * @author C罗
 *	礼物管理
 */
@Before(Tx.class)
public class GiftManage extends Controller {
	
	@Author("cluo")
	@Rp("礼物")
	@Explaination(info = "礼物列表【礼物和我的礼物钻石值一样。不统计收到的礼物的钻石。点十号的按钮都是充钱。】")
	@UpdateLog(date = "2015-03-24 11:12", log = "初次添加")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnDBParam(name = "GiftsResponse{GiftsResult:Gifts:list[Gift:$]}", column = Gift.gift_id)
	@ReturnDBParam(name = "GiftsResponse{GiftsResult:Gifts:list[Gift:$]}", column = Gift.gift_name)
	@ReturnDBParam(name = "GiftsResponse{GiftsResult:Gifts:list[Gift:$]}", column = Gift.gift_cover)
	@ReturnDBParam(name = "GiftsResponse{GiftsResult:Gifts:list[Gift:$]}", column = Gift.gold_value)
	@ReturnOutlet(name = "GiftsResponse{user_gold_value}", remarks = "用户钻石值", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "GiftsResponse{status}", remarks = "1-成功", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "GiftsResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "GiftsResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void gifts() {
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
			int status = 0;String message="";
			String user_id = AppToken.getUserId(token, this);
			User user = User.dao.findById(user_id);
			int user_gold_value = user.get(user.user_gold_value);
			
			final List<Gift> gifts = Gift.dao.find("select gift_id,gift_name,gift_cover,gold_value from gift order by sort asc,post_time desc");
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			response.put("user_gold_value", user_gold_value);
			response.put("message", "");
			response.put("status", 1);
			response.put("code", 200);
			response.put("Result", new HashMap<String, Object>() {
				{
					put("Gifts", gifts);
				}
			});
			setAttr("GiftsResponse", response);
			renderMultiJson("GiftsResponse");
			AppLog.info(null, getRequest());
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("GiftManage/gifts", "礼物列表", this);
		}
	}
	/**
	 * 礼物和我的礼物钻石值一样。不统计收到的礼物的钻石。
	 */
	@Author("cluo")
	@Rp("我的礼物")
	@Explaination(info = "我的礼物列表【不统计收到的礼物的钻石】")
	@UpdateLog(date = "2015-03-24 11:12", log = "初次添加")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnDBParam(name = "MyGiftsResponse{MyGiftsResult:MyGifts:list[MyGift:$]}", column = MyGift.my_gift_id)
	@ReturnDBParam(name = "MyGiftsResponse{MyGiftsResult:MyGifts:list[MyGift:$]}", column = MyGift.my_gift_name)
	@ReturnDBParam(name = "MyGiftsResponse{MyGiftsResult:MyGifts:list[MyGift:$]}", column = MyGift.my_gift_cover)
	@ReturnDBParam(name = "MyGiftsResponse{MyGiftsResult:MyGifts:list[MyGift:$]}", column = MyGift.my_gold_value)
	@ReturnDBParam(name = "MyGiftsResponse{MyGiftsResult:MyGifts:list[MyGift:$]}", column = MyGift.send_user_id)
	@ReturnDBParam(name = "MyGiftsResponse{MyGiftsResult:MyGifts:list[MyGift:$]}", column = MyGift.receiver_user_id)
	@ReturnDBParam(name = "MyGiftsResponse{MyGiftsResult:MyGifts:list[MyGift:$]}", column = MyGift.gift_count)
	@ReturnOutlet(name = "MyGiftsResponse{status}", remarks = "1-成功", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "MyGiftsResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "MyGiftsResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void myGifts() {
		try {
			String token = getPara("token");
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("MyGiftsResponse", response2);
				renderMultiJson("MyGiftsResponse");
				return;
			}
			int status = 0;String message="";
			String user_id = AppToken.getUserId(token, this);
			
			final List<MyGift> myGifts = MyGift.dao.find("select my_gift_id,gift_count,my_gift_name,my_gift_cover,my_gold_value,send_user_id,receiver_user_id from my_gift where status=1 and receiver_user_id="+user_id+" order by post_time desc");
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			response.put("status", 1);
			response.put("message", "操作成功");
			response.put("code", 200);
			response.put("Result", new HashMap<String, Object>() {
				{
					put("MyGifts", myGifts);
				}
			});
			setAttr("MyGiftsResponse", response);
			renderMultiJson("MyGiftsResponse");
			AppLog.info(null, getRequest());
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("GiftManage/myGifts", "我的礼物列表", this);
		}
	}
	@Author("cluo")
	@Rp("我的礼物")
	@Explaination(info = "礼物转赠【接收礼物者的钻石值不增加。】")
	@UpdateLog(date = "2015-03-24 11:12", log = "初次添加")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = MyGift.my_gift_id)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = MyGift.receiver_user_id)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "GiveMyGiftResponse{status}", remarks = "1-成功", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "GiveMyGiftResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "GiveMyGiftResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void giveMyGift() {
		try {
			int my_gift_id = getParaToInt("my_gift_id");
			int input_receiver_user_id = getParaToInt("receiver_user_id");
			String token = getPara("token");
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("GiveMyGiftResponse", response2);
				renderMultiJson("GiveMyGiftResponse");
				return;
			}
			int status = 0;String message="";
			String user_id = AppToken.getUserId(token, this);
			MyGift myGift = MyGift.dao.findById(my_gift_id);
			if (myGift!=null) {
				String my_gift_cover = myGift.getStr(myGift.my_gift_cover);
				String my_gift_name = myGift.getStr(myGift.my_gift_name);
				int my_gold_value = myGift.get(myGift.my_gold_value);
				int gift_id = myGift.get(myGift.gift_id);
				int gift_count = myGift.get(myGift.gift_count);
				boolean update = false;
				if (gift_count>1) {
					update = myGift.set(myGift.gift_count, gift_count-1).update();
				}
				if (gift_count==1) {
					update = myGift.set(myGift.status, 0).set(myGift.gift_count, 0).update();
				}
				if (update) {
					MyGift myGift3 = MyGift.dao.findFirst("select my_gift_id,gift_count from my_gift where status=1 and gift_id=? and receiver_user_id=? ",gift_id,input_receiver_user_id);
					if (myGift3!=null) {
						int gift_count2 = myGift3.get(myGift3.gift_count);
						myGift3.set(myGift3.gift_count, (gift_count2+1)).update();
					}else {
						MyGift myGift2 = new MyGift();
						boolean save = myGift2.set(myGift2.my_gift_cover, my_gift_cover)
								.set(myGift2.gift_id, gift_id)
								.set(myGift2.gift_count, 1)
								.set(myGift2.my_gift_name, my_gift_name).set(myGift2.my_gold_value, my_gold_value)
								.set(myGift2.send_user_id, user_id).set(myGift2.receiver_user_id, input_receiver_user_id)
								.set(myGift2.status, 1).set(myGift2.post_time, DateUtils.getCurrentDateTime())
								.save();
					}
				}
			}
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			response.put("message", "赠送成功");
			response.put("status", 1);
			response.put("code", 200);
			setAttr("GiveMyGiftResponse", response);
			renderMultiJson("GiveMyGiftResponse");
			AppLog.info(null, getRequest());
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("GiftManage/giveMyGift", "礼物转赠", this);
		}
	}
	@Author("cluo")
	@Rp("礼物")
	@Explaination(info = "礼物赠送【接收礼物者的钻石值不增加，赠送者钻石值减少。】")
	@UpdateLog(date = "2015-03-24 11:12", log = "初次添加")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Gift.gift_id)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = User.user_gold_value)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = MyGift.receiver_user_id)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "GiveGiftResponse{left_user_gold_value}", remarks = "用户剩余钻石值", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "GiveGiftResponse{status}", remarks = "1-成功，2-当前钻石余额不足", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "GiveGiftResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "GiveGiftResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void giveGift() {
		try {
			int gift_id = getParaToInt("gift_id");
			int user_gold_value = getParaToInt("user_gold_value");
			int input_receiver_user_id = getParaToInt("receiver_user_id");
			String token = getPara("token");
			if (!AppToken.check(token, this)) {
				// 登陆失败
				ResponseValues response2 = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response2.put("message", "请重新登陆");
				response2.put("code", 405);
				setAttr("GiveGiftResponse", response2);
				renderMultiJson("GiveGiftResponse");
				return;
			}
			int left_user_gold_value = user_gold_value;
			int status = 0;String message="赠送失败";
			String user_id = AppToken.getUserId(token, this);
			Gift gift = Gift.dao.findById(gift_id);
			int gold_value = gift.get(gift.gold_value);
			if (user_gold_value<gold_value) {
				//当前钻石余额不足
				status = 2;
				message="当前钻石余额不足";
			}else {
				boolean save = false;
				String gift_cover = gift.getStr(gift.gift_cover);
				String gift_name = gift.getStr(gift.gift_name);
				MyGift myGift = MyGift.dao.findFirst("select my_gift_id,gift_count from my_gift where status=1 and gift_id=? and receiver_user_id=? ",gift_id,input_receiver_user_id);
				if (myGift!=null) {
					int gift_count = myGift.get(myGift.gift_count);
					 save = myGift.set(myGift.gift_count, (gift_count+1)).update();
				}else {
					MyGift myGift2 = new MyGift();
					 save = myGift2.set(myGift2.my_gift_cover, gift_cover)
						  .set(myGift2.gift_id, gift_id)
						  .set(myGift2.gift_count, 1)
						  .set(myGift2.my_gift_name, gift_name).set(myGift2.my_gold_value, gold_value)
						  .set(myGift2.send_user_id, user_id).set(myGift2.receiver_user_id, input_receiver_user_id)
						  .set(myGift2.status, 1).set(myGift2.post_time, DateUtils.getCurrentDateTime())
						  .save();
				}
				if (save) {
					User user = User.dao.findById(user_id);
					int u_gold_value = user.get(user.user_gold_value);
					left_user_gold_value = u_gold_value - gold_value;
					user.set(user.user_gold_value, left_user_gold_value).update();
					status = 1;
					message="赠送成功";
				}
			}
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			response.put("left_user_gold_value", left_user_gold_value);
			response.put("message", message);
			response.put("status", status);
			response.put("code", 200);
			setAttr("GiveGiftResponse", response);
			renderMultiJson("GiveGiftResponse");
			AppLog.info(null, getRequest());
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("GiftManage/giveGift", "礼物赠送", this);
		}
	}
}
