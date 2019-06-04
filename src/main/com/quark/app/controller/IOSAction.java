/**
 * 
 */
package com.quark.app.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.CacheName;
import com.jfinal.plugin.ehcache.EvictInterceptor;
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
import com.quark.buyVerify.ios.IOS_Verify;
import com.quark.common.AppData;
import com.quark.common.OrderUtils;
import com.quark.common.RongToken;
import com.quark.interceptor.AppToken;
import com.quark.model.extend.Browse;
import com.quark.model.extend.Charge;
import com.quark.model.extend.ChargeGold;
import com.quark.model.extend.Collection;
import com.quark.model.extend.LoveYu;
import com.quark.model.extend.Search;
import com.quark.model.extend.Sweet;
import com.quark.model.extend.Tokens;
import com.quark.model.extend.User;
import com.quark.model.extend.UserTag;
import com.quark.utils.DateUtils;

/**
 * @author cluo
 *
 */
public class IOSAction extends Controller  implements  Serializable{

	@Author("C罗")
	@Rp("支付")
	@Explaination(info = "苹果购买")
	@UpdateLog(date = "2015-10-28 11:12", log = "初次添加")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = "receipt")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = "product_id")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = "environment")
	@ReturnOutlet(name = "IOSVerifyResponse{verifyResult}", remarks = "verifyResult", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "IOSVerifyResponse{result}", remarks = "验证结果", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "IOSVerifyResponse{status}", remarks = "1-验证成功,2-账单有效，但己验证过,3-账单无效", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "IOSVerifyResponse{message}", remarks = "", dataType = DataType.Int, defaultValue = "")
	public void IOSVerify() throws Exception {
		try {
			//HttpServletRequest request = getRequest();
			//HttpServletResponse response= getResponse();
			//System.out.println(new 	Date().toLocaleString()+"  来自苹果端的验证...");
			//苹果客户端传上来的收据,是最原据的收据
			//String receipt=request.getParameter("receipt");
			
			String receipt = getPara("receipt");
			String product_id = getPara("product_id");
			String environment = getPara("environment");
			int status = 0;
			System.out.println(receipt);
			System.out.println(product_id);
			System.out.println(environment);
			//拿到收据的MD5
			//String md5_receipt=MD5.md5Digest(receipt);
			String md5_receipt=receipt;//;MD5.md5Digest(receipt);
			//默认是无效账单
			String result=md5_receipt;
			//查询数据库，看是否是己经验证过的账号
			boolean isExists=false;//DbServiceImpl_PNM.isExistsIOSReceipt(md5_receipt);
			String verifyResult=null;
			if(!isExists){
				String verifyUrl=IOS_Verify.getVerifyURL(environment);
				verifyResult=IOS_Verify.buyAppVerify(receipt, verifyUrl);
				//System.out.println(verifyResult);
				if(verifyResult==null){
					//苹果服务器没有返回验证结果
					result=md5_receipt;;//R.BuyState.STATE_D+"#"+md5_receipt;
				}else{
					//跟苹果验证有返回结果------------------
					JSONObject job = JSONObject.fromObject(verifyResult);
					String states=job.getString("status");
					if(states.equals("0"))//验证成功
					{
						String r_receipt=job.getString("receipt");
						JSONObject returnJson = JSONObject.fromObject(r_receipt);
						//产品ID
						product_id=returnJson.getString("product_id");
						//数量
						String quantity=returnJson.getString("quantity");
						//跟苹果的服务器验证成功
						/*result=R.BuyState.STATE_A+"#"+md5_receipt+"_"+product_id+"_"+quantity;
						//交易日期
						String purchase_date=returnJson.getString("purchase_date");
						//保存到数据库
						DbServiceImpl_PNM.saveIOSReceipt(md5_receipt, product_id, purchase_date, r_receipt);*/
						status = 1;
					}else{
						//账单无效
						result=md5_receipt;;//R.BuyState.STATE_C+"#"+md5_receipt;
						status = 3;
					}
					//跟苹果验证有返回结果------------------
				}
				//传上来的收据有购买信息==end=============
			}else{
				//账单有效，但己验证过
				//result=R.BuyState.STATE_B+"#"+md5_receipt;
				status = 2;
			}
			//返回结果
			System.out.println("验证结果     "+result);
			System.out.println();
			//response.getWriter().write(result);
			ResponseValues responseValues = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			
			responseValues.put("verifyResult", verifyResult);
			responseValues.put("result", result);
			responseValues.put("status", status);
			responseValues.put("message", 1);
			setAttr("IOSVerifyResponse", responseValues);
			renderMultiJson("IOSVerifyResponse");
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
			AppData.analyze("IOSAction/IOSVerify", "苹果购买", this);
		}
	}
	@Author("C罗")
	@Rp("支付")
	@Explaination(info = "苹果购买成功异步通知")
	@UpdateLog(date = "2015-10-28 11:12", log = "初次添加")
	@URLParam(defaultValue = "", explain = "用户类型：1-甜心大哥，0-甜心宝贝", type = Type.String, name = "sex")
	@URLParam(defaultValue = "", explain = "天数", type = Type.String, name = "days")
	@URLParam(defaultValue = "", explain = "用户ID", type = Type.String, name = "user_id")
	@URLParam(defaultValue = "", explain = "VIP价格", type = Type.String, name = "price")
	@ReturnOutlet(name = "iPhonePayAysnResponse{status}", remarks = "1-支付成功,0-失败", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "iPhonePayAysnResponse{message}", remarks = "", dataType = DataType.Int, defaultValue = "")
	public void iPhonePayAysn() throws Exception {
		try {
			int sex = getParaToInt("sex",11);
			int days = getParaToInt("days",0);
			int user_id = getParaToInt("user_id",0); 
			String price_str = getPara("price","00");
			
			ResponseValues response2 = new ResponseValues(this, Thread
					.currentThread().getStackTrace()[1].getMethodName());
			int status = 1;String message ="支付成功";
			if (sex==11||days==0||user_id==0||price_str.equals("00")) {
				status = 0;
				message ="支付失败";
			}else {
				BigDecimal money = new BigDecimal(price_str);
				Charge charge = new Charge();
				boolean save = charge.set(charge.user_id, user_id).set(charge.from_time, DateUtils.getCurrentDateTime())
					.set(charge.end_time, DateUtils.getAddDaysString2(days,DateUtils.getCurrentDateTime()))
					.set(charge.days, days)
					.set("is_pay", 0).set(charge.charge_time, DateUtils.getCurrentDateTime())
					.set(charge.money, money).set(charge.charge_month, DateUtils.getCurrentMonth())
					.set("type", sex).set("pay_type", 4).set(charge.charge_date, DateUtils.getCurrentDate())
					.set(charge.charge_hour, DateUtils.getCurrentDateHours())
					.save();
				if (save) {
					int out_trade_no = charge.get("charge_id");
					Charge log = Charge.dao.findById(out_trade_no);
					if (log!=null) {
						OrderUtils.Dall(log);
					}else {
						System.out.println("支付null");
					}
				}
			}
			response2.put("status", status);
			response2.put("message", message);
			response2.put("code", 200);
			setAttr("iPhonePayAysnResponse", response2);
			renderMultiJson("iPhonePayAysnResponse");
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
			AppData.analyze("IOSAction/iPhonePayAysn", "表", this);
		}
	}
	@Author("C罗")
	@Rp("支付")
	@Explaination(info = "苹果购买成功异步通知【钻石】")
	@UpdateLog(date = "2015-10-28 11:12", log = "初次添加")
	@URLParam(defaultValue = "", explain = "用户ID", type = Type.String, name = "user_id")
	@URLParam(defaultValue = "", explain = "钻石值", type = Type.String, name = "gold_value")
	@URLParam(defaultValue = "", explain = "钻石价格", type = Type.String, name = "gold_price")
	@ReturnOutlet(name = "iPhonePayGiftAysnResponse{status}", remarks = "1-支付成功,0-失败", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "iPhonePayGiftAysnResponse{message}", remarks = "", dataType = DataType.Int, defaultValue = "")
	public void iPhonePayGiftAysn() throws Exception {
		try {
			int gold_value = getParaToInt("gold_value",0);
			int user_id = getParaToInt("user_id",0); 
			String gold_price = getPara("gold_price","00");
			
			ResponseValues response2 = new ResponseValues(this, Thread
					.currentThread().getStackTrace()[1].getMethodName());
			int status = 1;String message ="支付成功";
			if (user_id==0||gold_price.equals("00")) {
				status = 0;
				message ="支付失败";
			}else {
				User user = User.dao.findById(user_id);
				int sex = user.get(user.sex);
				BigDecimal money = new BigDecimal(gold_price);
				ChargeGold charge = new ChargeGold();
				boolean save = charge.set(charge.user_id, user_id).set(charge.gold_value, gold_value)
					  .set(charge.is_pay, 0).set(charge.charge_time, DateUtils.getCurrentDateTime())
					  .set(charge.money, money).set(charge.charge_month, DateUtils.getCurrentMonth())
					  .set("type", sex).set("pay_type", 4).set(charge.charge_date, DateUtils.getCurrentDate())
					  .set(charge.charge_hour, DateUtils.getCurrentDateHours())
					  .save();
				if (save) {
					int out_trade_no = charge.get("charge_gold_id");
					ChargeGold log = ChargeGold.dao.findById(out_trade_no);
					if (log!=null) {
						OrderUtils.DallGold(log);
					}else {
						System.out.println("支付null");
					}
				}
			}
			response2.put("status", status);
			response2.put("message", message);
			response2.put("code", 200);
			setAttr("iPhonePayGiftAysnResponse", response2);
			renderMultiJson("iPhonePayGiftAysnResponse");
		} catch (Exception e) {
			e.printStackTrace();
			AppLog.error(e, getRequest());
		} finally {
			AppLog.info("", getRequest());
			AppData.analyze("IOSAction/iPhonePayGiftAysn", "苹果购买成功异步通知【钻石】", this);
		}
	}
}
