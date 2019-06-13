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
import com.quark.model.extend.ChargeGold;
import com.quark.model.extend.Gift;
import com.quark.model.extend.GoldPrice;
import com.quark.model.extend.InvitationPriceEntity;
import com.quark.model.extend.Invite;
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
public class PaysGold extends Controller {
	
	@Author("cluo")
	@Rp("我的钱包（钻石）")
	@Explaination(info = "钻石价格列表")
	@UpdateLog(date = "2015-03-24 11:12", log = "初次添加")
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnDBParam(name = "GoldPricesResponse{GoldPriceResult:GoldPrices:list[GoldPrice:$]}", column = GoldPrice.gold_price_id)
	@ReturnDBParam(name = "GoldPricesResponse{GoldPriceResult:GoldPrices:list[GoldPrice:$]}", column = GoldPrice.gold_value)
	@ReturnDBParam(name = "GoldPricesResponse{GoldPriceResult:GoldPrices:list[GoldPrice:$]}", column = GoldPrice.gold_price)
	@ReturnDBParam(name = "GoldPricesResponse{GoldPriceResult:GoldPrices:list[GoldPrice:$]}", column = GoldPrice.product_id)
	@ReturnOutlet(name = "GoldPricesResponse{status}", remarks = "1-成功", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "GoldPricesResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "GoldPricesResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void goldPrices() {
		try {
			final List<GoldPrice> goldPrices = GoldPrice.dao.find("select gold_price_id,gold_value,gold_price,product_id from gold_price order by sort asc,post_time desc");
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			response.put("message", "");
			response.put("status", 1);
			response.put("code", 200);
			response.put("Result", new HashMap<String, Object>() {
				{
					put("GoldPrices", goldPrices);
				}
			});
			setAttr("GoldPricesResponse", response);
			renderMultiJson("GoldPricesResponse");
			AppLog.info(null, getRequest());
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("Pays/goldPrices", "钻石价格列表", this);
		}
	}

	private static AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
			alipayConfig.APP_ID, alipayConfig.APP_PRIVATE_KEY, "json", "UTF-8", alipayConfig.ALIPAY_PUBLIC_KEY, "RSA2");

	@Author("cluo")
	@Rp("支付")
	@Explaination(info = "购买钻石【钻石】")
	@UpdateLog(date = "2015-03-24 11:12", log = "初次添加")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = Tokens.token)
	@URLParam(defaultValue = "", explain = "{1-支付宝、2-微信、3-银联}", type = Type.Int, name = "pay_type")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = GoldPrice.gold_value)
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = GoldPrice.gold_price)
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnOutlet(name = "PayGoldResponse{charge_id}", remarks = "付款Id", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "PayGoldResponse{pay_id}", remarks = "支付流水号", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "PayGoldResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "PayGoldResponse{status}", remarks = "", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "PayGoldResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	@Before(Tx.class)
	public void payGold() throws Exception {
		int pay_type = getParaToInt("pay_type");
		int gold_value = getParaToInt("gold_value");
		BigDecimal money = new BigDecimal(getPara("gold_price"));
		
		String token = getPara("token", null);
		if (!AppToken.check(token, this)) {
			// 登陆失败
			ResponseValues response2 = new ResponseValues(this, Thread
					.currentThread().getStackTrace()[1].getMethodName());
			response2.put("code", 405);
			response2.put("message", "请重新登陆");
			setAttr("PayGoldResponse", response2);
			renderMultiJson("PayGoldResponse");
			return;
		}
		String message = "";
		String user_id = AppToken.getUserId(token, this);
		User user = User.dao.findById(user_id);
		int sex = user.get("sex");
		/**
		 * log
		 */
		AppLog.info("购买-生成购买钻石订单", getRequest());
		/**
		 * end
		 */
		int charge_gold_id = 0;
		ChargeGold chargeLog = ChargeGold.dao.findFirst("select charge_gold_id from charge_gold where user_id="+user_id+" and money="+money+" and is_pay=0");
		if (chargeLog!=null) {
			charge_gold_id = chargeLog.get("charge_gold_id");
			chargeLog.set(chargeLog.user_id, user_id).set(chargeLog.gold_value, gold_value)
			  .set("is_pay", 0).set(chargeLog.charge_time, DateUtils.getCurrentDateTime())
			  .set(chargeLog.money, money).set(chargeLog.charge_month, DateUtils.getCurrentMonth())
			  .set("type", sex).set("pay_type", pay_type).set(chargeLog.charge_date, DateUtils.getCurrentDate())
			  .set(chargeLog.charge_hour, DateUtils.getCurrentDateHours())
			  .update();
		}else {
			ChargeGold charge = new ChargeGold();
			charge.set(charge.user_id, user_id).set(charge.gold_value, gold_value)
				  .set("is_pay", 0).set(charge.charge_time, DateUtils.getCurrentDateTime())
				  .set(charge.money, money).set(charge.charge_month, DateUtils.getCurrentMonth())
				  .set("type", sex).set("pay_type", pay_type).set(charge.charge_date, DateUtils.getCurrentDate())
				  .set(charge.charge_hour, DateUtils.getCurrentDateHours())
				  .save();
			charge_gold_id = charge.get("charge_gold_id");
			chargeLog = ChargeGold.dao.findById(charge_gold_id);
		}
		ResponseValues response2 = new ResponseValues(this, Thread
				.currentThread().getStackTrace()[1].getMethodName());
		// 充值方式pay_type：1-支付宝、2-微信、3-银联
		if (pay_type==1) {

			// 实例化客户端
			// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
			AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
			// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
			AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
			model.setBody("支付订单号：" + charge_gold_id);
			model.setSubject("支付订单号：" + charge_gold_id);
			model.setOutTradeNo("" + charge_gold_id);
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
				response.put("out_trade_no",charge_gold_id);
				setAttr("PayResponse", response);
				renderMultiJson("PayResponse");
			} catch (AlipayApiException e) {
				e.printStackTrace();
			}

		} else if (pay_type==2) {
			Double d_money = new Double(money.intValue() * 100);
			response2 = com.tenpay.util.WeixinPayGoldUtils.createOrder("购买钻石:" + money, "购买钻石", "" + charge_gold_id, ""
					+ d_money.intValue());
			String prepayid = response2.get("prepayid").toString();
			if (prepayid.equals("")) {
				response2.put("status", 0);
			}else{
				chargeLog.set("pay_id", response2.get("prepayid")).update();
				response2.put("status", 1);
			}
			response2.put("pay_money", money);
			response2.put("code", 200);
			setAttr("ComfiyPayResponse", response2);
			renderMultiJson("ComfiyPayResponse");
		}else if (pay_type==3) {
			// 银联支付
			String sn = UnionPayUtils.createOrder(charge_gold_id, money.doubleValue());
			chargeLog.set("pay_id", sn).update();
			
			response2.put("code", 200);
			response2.put("charge_id", charge_gold_id);
			response2.put("pay_id", sn);
			response2.put("pay_money", money);
			setAttr("ComfiyPayResponse", response2);
			renderMultiJson("ComfiyPayResponse");
		}
	}
	@Author("C罗")
	@Rp("支付")
	@Explaination(info = "支付宝支付异步通知【钻石】")
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
			String seller_email = getPara("seller_email");
			if (alipayConfig.APP_SELLER.equals(seller_email)) {
				String trade_no = getPara("trade_no");
				String out_trade_no = getPara("out_trade_no");
				ChargeGold log = ChargeGold.dao.findFirst("select * from charge_gold where charge_gold_id=?",out_trade_no);
				System.out.println(log);
				if (log != null) {
					OrderUtils.DallGold(log);
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
	@Explaination(info = "微信支付异步通知【钻石】")
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
			ChargeGold log = ChargeGold.dao.findById(out_trade_no);
			if (log != null) {
				int is_pay = log.get("is_pay");
				if (is_pay == 0) {
					if (!OrderUtils.DallGold( log)) {
						renderText("fail"); // 请不要修改或删除
						AppLog.info("微信支付异步通知,账号锁定,无法支付.", getRequest());
						return;
					}
				}
				AppLog.info("微信支付异步通知,充值成功.", getRequest());
				renderText("success"); // 请不要修改或删除
				return;
			}else {
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
	@Explaination(info = "银联支付异步通知【钻石】")
	@UpdateLog(date = "2015-11-05 11:12", log = "初次添加")
	@URLParam(defaultValue = "", explain = Value.Infer, type = Type.String, name = "out_trade_no")
	@ReturnJson("{'CommitProjectResponse':{'status':1}}")
	public void unionPayAysn() throws Exception {}
	
	
}
