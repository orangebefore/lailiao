/**
 * 
 */
package com.quark.app.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.aliyun.config.alipayConfig;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;
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
import com.quark.common.OrderUtils;
import com.quark.interceptor.AppToken;
import com.quark.model.extend.Charge;
import com.quark.model.extend.ChargeAudit;
import com.quark.model.extend.ChargeGold;
import com.quark.model.extend.Gift;
import com.quark.model.extend.GoldPrice;
import com.quark.model.extend.Notices;
import com.quark.model.extend.Price;
import com.quark.model.extend.Tag;
import com.quark.model.extend.Tokens;
import com.quark.model.extend.User;
import com.quark.utils.DateUtils;
import com.tenpay.ResponseHandler;
import com.tenpay.util.ConstantUtil;
import com.unionpay.acp.demo.BackRcvResponse;
import com.unionpay.acp.demo.UnionPayUtils;

/**
 * @author C罗
 *
 */
@Before(Tx.class)
public class Pays extends Controller {

	@Author("cluo")
	@Rp("升级会员")
	@Explaination(info = "价格列表")
	@UpdateLog(date = "2015-03-24 11:12", log = "初次添加")
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnDBParam(name = "PricesResponse{PriceResult:Price:$}", column = Price.price_id)
	@ReturnDBParam(name = "PricesResponse{PriceResult:Price:$}", column = Price.days)
	@ReturnDBParam(name = "PricesResponse{PriceResult:Price:$}", column = Price.price)
	@ReturnDBParam(name = "PricesResponse{PriceResult:Price:$}", column = Price.product_id)
	@ReturnDBParam(name = "PricesResponse{TagResult:Tag:$}", column = Tag.tag)
	@ReturnOutlet(name = "PricesResponse{status}", remarks = "1-成功", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "PricesResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "PricesResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void prices() {
		try {
			final List<Price> prices = Price.dao
					.find("select price_id,days,price,product_id from price order by sort asc");
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			response.put("message", "");
			response.put("status", 1);
			response.put("code", 200);
			response.put("Result", new HashMap<String, Object>() {
				{
					put("Price", prices);
				}
			});
			setAttr("PricesResponse", response);
			renderMultiJson("PricesResponse");
			AppLog.info(null, getRequest());
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("Pays/prices", "价格列表", this);
		}
	}

	private static AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
			alipayConfig.APP_ID, alipayConfig.APP_PRIVATE_KEY, "json", "UTF-8", alipayConfig.ALIPAY_PUBLIC_KEY, "RSA2");

	@Author("cluo")
	@Rp("支付")
	@Explaination(info = "购买VIP")
	@UpdateLog(date = "2015-03-24 11:12", log = "初次添加")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "", explain = "用户类型：1-甜心大哥，0-甜心宝贝", type = Type.String, name = "sex")
	@URLParam(defaultValue = "", explain = "天数", type = Type.String, name = "days")
	@URLParam(defaultValue = "", explain = "用户ID", type = Type.String, name = "user_id")
	@URLParam(defaultValue = "", explain = "VIP价格", type = Type.String, name = "price")
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")

	@ReturnOutlet(name = "PayVipResponse{charge_id}", remarks = "付款Id", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "PayVipResponse{pay_id}", remarks = "支付流水号", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "PayVipResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "PayVipResponse{status}", remarks = "", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "PayVipResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	@Before(Tx.class)
	public void payVip() throws Exception {
		int pay_type = 1;//getParaToInt("pay_type", 1);
		int sex = getParaToInt("sex", 11);
		int days = getParaToInt("days", 0);
		int user_id = getParaToInt("user_id", 0);
		String price_str = getPara("price", "00");

		String token = getPara("token", null);
		
		
		String message = "";
		User user = User.dao.findById(user_id);
		/**
		 * log
		 */
		AppLog.info("购买-生成购买订单", getRequest());
		/**
		 * end
		 */
		BigDecimal money = new BigDecimal(price_str);
		Charge charge = new Charge();
		boolean save = charge.set(charge.user_id, user_id).set(charge.from_time, DateUtils.getCurrentDateTime())
				.set(charge.end_time, DateUtils.getAddDaysString2(days, DateUtils.getCurrentDateTime()))
				.set(charge.days, days).set("is_pay", 0).set(charge.charge_time, DateUtils.getCurrentDateTime())
				.set(charge.money, money).set(charge.charge_month, DateUtils.getCurrentMonth()).set("type", sex)
				.set("pay_type", 4).set(charge.charge_date, DateUtils.getCurrentDate()).set(charge.buy_type, 1)
				.set(charge.charge_hour, DateUtils.getCurrentDateHours()).save();
		if (save) {
			int out_trade_no = charge.get("charge_id");
			if (pay_type == 1) {

				// 实例化客户端
				// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
				AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
				// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
				AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
				model.setBody("支付订单号：" + out_trade_no);
				model.setSubject("支付订单号：" + out_trade_no);
				model.setOutTradeNo("" + out_trade_no);
				model.setTimeoutExpress("30m");
				model.setTotalAmount(money.toString());
				model.setProductCode("QUICK_MSECURITY_PAY");
				request.setBizModel(model);
				request.setNotifyUrl(alipayConfig.notify_url);
				try {
					// 这里和普通的接口调用不同，使用的是sdkExecute
					AlipayTradeAppPayResponse alipayTradeAppPayResponse = alipayClient.sdkExecute(request);
					ResponseValues response = new ResponseValues(this,
							Thread.currentThread().getStackTrace()[1].getMethodName());
					response.put("ALIPAY_APP_SELLER", alipayConfig.APP_SELLER);
					response.put("ALIPAY_APP_ID", alipayConfig.APP_ID);
					response.put("ALIPAY_APP_PRIVATE_KEY", alipayConfig.APP_PRIVATE_KEY);
					response.put("ALIPAY_PUBLIC_KEY", alipayConfig.ALIPAY_PUBLIC_KEY);
					response.put("out_trade_no",out_trade_no);
					setAttr("PayResponse", response);
					renderMultiJson("PayResponse");
				} catch (AlipayApiException e) {
					e.printStackTrace();
				}

			}
		}

	}
	@Author("chen")
	@Rp("支付")
	@Explaination(info = "购买超级明星")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "", explain = "用户类型：1-甜心大哥，0-甜心宝贝", type = Type.String, name = "sex")
	@URLParam(defaultValue = "", explain = "天数", type = Type.String, name = "days")
	@URLParam(defaultValue = "", explain = "用户ID", type = Type.String, name = "user_id")
	@URLParam(defaultValue = "", explain = "超级明星价格", type = Type.String, name = "price")
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")

	@ReturnOutlet(name = "PayStarResponse{charge_id}", remarks = "付款Id", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "PayStarResponse{pay_id}", remarks = "支付流水号", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "PayVipStarResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "PayStarResponse{status}", remarks = "", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "PayStarResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	@Before(Tx.class)
	public void payStar() throws Exception {
		int pay_type = 1;//getParaToInt("pay_type", 1);
		int sex = getParaToInt("sex", 11);
		int days = getParaToInt("days", 0);
		int user_id = getParaToInt("user_id", 0);
		String price_str = getPara("price", "00");

		String token = getPara("token", null);
		
		
		String message = "";
		User user = User.dao.findById(user_id);
		/**
		 * log
		 */
		AppLog.info("购买-生成购买订单", getRequest());
		/**
		 * end
		 */
		BigDecimal money = new BigDecimal(price_str);
		Charge charge = new Charge();
		boolean save = charge.set(charge.user_id, user_id).set(charge.from_time, DateUtils.getCurrentDateTime())
				.set(charge.end_time, DateUtils.getAddDaysString2(days, DateUtils.getCurrentDateTime()))
				.set(charge.days, days).set("is_pay", 0).set(charge.charge_time, DateUtils.getCurrentDateTime())
				.set(charge.money, money).set(charge.charge_month, DateUtils.getCurrentMonth()).set("type", sex)
				.set("pay_type", 4).set(charge.charge_date, DateUtils.getCurrentDate()).set(charge.buy_type, 2)
				.set(charge.charge_hour, DateUtils.getCurrentDateHours()).save();
		if (save) {
			int out_trade_no = charge.get("charge_id");
			if (pay_type == 1) {

				// 实例化客户端
				// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
				AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
				// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
				AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
				model.setBody("支付订单号：" + out_trade_no);
				model.setSubject("支付订单号：" + out_trade_no);
				model.setOutTradeNo("" + out_trade_no);
				model.setTimeoutExpress("30m");
				model.setTotalAmount(money.toString());
				model.setProductCode("QUICK_MSECURITY_PAY");
				request.setBizModel(model);
				request.setNotifyUrl(alipayConfig.notify_url);
				try {
					// 这里和普通的接口调用不同，使用的是sdkExecute
					AlipayTradeAppPayResponse alipayTradeAppPayResponse = alipayClient.sdkExecute(request);
					ResponseValues response = new ResponseValues(this,
							Thread.currentThread().getStackTrace()[1].getMethodName());
					response.put("ALIPAY_APP_SELLER", alipayConfig.APP_SELLER);
					response.put("ALIPAY_APP_ID", alipayConfig.APP_ID);
					response.put("ALIPAY_APP_PRIVATE_KEY", alipayConfig.APP_PRIVATE_KEY);
					response.put("ALIPAY_PUBLIC_KEY", alipayConfig.ALIPAY_PUBLIC_KEY);
					response.put("out_trade_no",out_trade_no);
					setAttr("PayStarResponse", response);
					renderMultiJson("PayStarResponse");
				} catch (AlipayApiException e) {
					e.printStackTrace();
				}

			}
		}

	}

	@Author("C罗")
	@Rp("支付")
	@Explaination(info = "支付宝支付异步通知")
	@UpdateLog(date = "2015-10-28 11:12", log = "初次添加")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = "out_trade_no")
	@ReturnJson("{'CommitProjectResponse':{'status':1}}")
	public void aliPayAysn() throws Exception {
		try {
			// 获取支付宝POST过来反馈信息
			Map<String, String> params = new HashMap<String, String>();
			Map requestParams = getRequest().getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。
				// valueStr = new String(valueStr.getBytes("ISO-8859-1"),
				// "utf-8");
				params.put(name, valueStr);
			}
			// 切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
			// boolean AlipaySignature.rsaCheckV1(Map<String, String> params,
			// String publicKey, String charset, String sign_type)
			boolean flag = AlipaySignature.rsaCheckV1(params, alipayConfig.ALIPAY_PUBLIC_KEY, "utf-8", "RSA");
			if (true) {
				String trade_no = getPara("trade_no");
				String out_trade_no = getPara("out_trade_no");
				Charge log = Charge.dao.findFirst("SELECT * FROM charge WHERE charge_id = ?",out_trade_no);
				int buy_type = log.get("buy_type");
				if (log != null) {
					if (buy_type==1) {
						OrderUtils.Dall(log);
					}else{
						OrderUtils.DallStar(log);
					}
					int user_id = log.get(Charge.user_id);
					Notices notices = Notices.dao.findFirst("select * from notices where user_id=? and type=1",user_id);
					if(notices != null) {
						notices.delete();
					}
					renderText("success"); // 请不要修改或删除
				} else {
					renderText("fail"); // 请不要修改或删除 }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Author("cluo")
	@Rp("支付")
	@Explaination(info = "微信支付异步通知")
	@UpdateLog(date = "2015-03-24 11:12", log = "初次添加")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = "out_trade_no")
	@ReturnJson("")
	public void WeixinAsynNotify() throws Exception {
		ResponseHandler responseHandler = new ResponseHandler(getRequest(), getResponse());
		responseHandler.setKey(ConstantUtil.APP_KEY);
		if (responseHandler.isTenpaySign()) {
			String out_trade_no = responseHandler.getParameter("out_trade_no").substring(10);
			/**
			 * 异步检验支付成功
			 */
			Charge log = Charge.dao.findById(out_trade_no);
			if (log != null) {
				int is_pay = log.get("is_pay");
				if (is_pay == 0) {
					if (!OrderUtils.Dall(log)) {
						renderText("fail"); // 请不要修改或删除
						AppLog.info("微信支付异步通知,账号锁定,无法支付.", getRequest());
						return;
					}
				}
				AppLog.info("微信支付异步通知,充值成功.", getRequest());
				renderText("success"); // 请不要修改或删除
				return;
			} else {
				System.out.println("冲值记录不存在");
			}
			/**
			 * end
			 */
			renderText("success");
		} else {
			AppLog.info("微信支付异步通知,验证失败.", getRequest());
			renderText("fail");
		}
	}

	@Author("C罗")
	@Rp("支付")
	@Explaination(info = "银联支付异步通知")
	@UpdateLog(date = "2019-06-10 10:12", log = "初次添加")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = "out_trade_no")
	@ReturnJson("{'CommitProjectResponse':{'status':1}}")
	public void unionPayAysn() throws Exception {
	}
	
	@Author("chen")
	@Rp("支付")
	@Explaination(info = "购买认证")
	@UpdateLog(date = "2019-06-10 10:12", log = "初次添加")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "", explain = "{1-支付宝、2-微信、3-银联}", type = Type.Int, name = "pay_type")
	@URLParam(defaultValue = "", explain = "用户ID", type = Type.String, name = "user_id")
	@URLParam(defaultValue = "", explain = "认证价格", type = Type.String, name = "price")
	@URLParam(defaultValue = "", explain = "{1-汽车、2-房子}", type = Type.Int, name = "aduit_type")
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "payAuditResponse{charge_id}", remarks = "付款Id", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "payAuditResponse{pay_id}", remarks = "支付流水号", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "payAuditResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "payAuditResponse{status}", remarks = "", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "payAuditResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void payAudit() {
		int pay_type = getParaToInt("pay_type");
		int user_id = getParaToInt("user_id", 0);
		String price_str = getPara("price", "00");
		int aduit_type = getParaToInt("aduit_type");

		String token = getPara("token", null);
		if (!AppToken.check(token, this)) {
			// 登陆失败
			ResponseValues response2 = new ResponseValues(this, Thread
					.currentThread().getStackTrace()[1].getMethodName());
			response2.put("code", 405);
			response2.put("message", "请重新登陆");
			setAttr("payAuditResponse", response2);
			renderMultiJson("payAuditResponse");
			return;
		}
		
		String message = "";
		User user = User.dao.findById(user_id);
		/**
		 * log
		 */
		AppLog.info("购买-生成购买审核订单", getRequest());
		/**
		 * end
		 */
		BigDecimal money = new BigDecimal(price_str);
		ChargeAudit cAudit = new ChargeAudit();
		boolean save = cAudit.set(cAudit.user_id, user_id).set(cAudit.charge_month, DateUtils.getCurrentMonth())
				.set(cAudit.charge_time, DateUtils.getCurrentDateTime()).set(cAudit.aduit_type, aduit_type)
				.set("is_pay", 0).set(cAudit.charge_date, DateUtils.getCurrentDate())
				.set(cAudit.money, money).set(cAudit.charge_hour, DateUtils.getCurrentDateHours())
				.set("pay_type", 4).save();
		int charge_audit_id = cAudit.get("charge_audit_id");
		cAudit = ChargeAudit.dao.findById(charge_audit_id);
		ResponseValues response2 = new ResponseValues(this, Thread
				.currentThread().getStackTrace()[1].getMethodName());
		// 充值方式pay_type：1-支付宝、2-微信、3-银联
		if (pay_type==1) {

			// 实例化客户端
			// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
			AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
			// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
			AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
			model.setBody("支付订单号：" + charge_audit_id);
			model.setSubject("支付订单号：" + charge_audit_id);
			model.setOutTradeNo("" + charge_audit_id);
			model.setTimeoutExpress("30m");
			model.setTotalAmount(money.toString());
			model.setProductCode("QUICK_MSECURITY_PAY");
			request.setBizModel(model);
			request.setNotifyUrl(alipayConfig.gold_notify_url);
			try {
				// 这里和普通的接口调用不同，使用的是sdkExecute
				AlipayTradeAppPayResponse alipayTradeAppPayResponse = alipayClient.sdkExecute(request);
				ResponseValues response = new ResponseValues(this,
						Thread.currentThread().getStackTrace()[1].getMethodName());
				response.put("ALIPAY_APP_SELLER", alipayConfig.APP_SELLER);
				response.put("ALIPAY_APP_ID", alipayConfig.APP_ID);
				response.put("ALIPAY_APP_PRIVATE_KEY", alipayConfig.APP_PRIVATE_KEY);
				response.put("ALIPAY_PUBLIC_KEY", alipayConfig.ALIPAY_PUBLIC_KEY);
				response.put("out_trade_no",charge_audit_id);
				setAttr("payAuditResponse", response);
				renderMultiJson("payAuditResponse");
			} catch (AlipayApiException e) {
				e.printStackTrace();
			}

		} else if (pay_type==2) {
			Double d_money = new Double(money.intValue() * 100);
			response2 = com.tenpay.util.WeixinPayGoldUtils.createOrder("购买认证:" + money, "购买认证", "" + charge_audit_id, ""
					+ d_money.intValue());
			String prepayid = response2.get("prepayid").toString();
			if (prepayid.equals("")) {
				response2.put("status", 0);
			}else{
				cAudit.set("pay_id", response2.get("prepayid")).update();
				response2.put("status", 1);
			}
			response2.put("pay_money", money);
			response2.put("code", 200);
			setAttr("payAuditResponse", response2);
			renderMultiJson("payAuditResponse");
		}else if (pay_type==3) {
			// 银联支付
			String sn = UnionPayUtils.createOrder(charge_audit_id, money.doubleValue());
			cAudit.set("pay_id", sn).update();
			
			response2.put("code", 200);
			response2.put("charge_id", charge_audit_id);
			response2.put("pay_id", sn);
			response2.put("pay_money", money);
			setAttr("payAuditResponse", response2);
			renderMultiJson("payAuditResponse");
		}

	}
}
