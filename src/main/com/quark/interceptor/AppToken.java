package com.quark.interceptor;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import com.jfinal.aop.Interceptor;
import com.jfinal.config.Interceptors;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.quark.model.extend.Tokens;
import com.quark.utils.Base64Util;
import com.quark.utils.MD5Util;
import com.quarkso.utils.DateUitls;

/**
 * 正确登陆后的app使用返回的token请求连接。
 * 
 * @author kingsley
 * 
 *         2014年6月17日
 */
public class AppToken implements Interceptor {

	// 访问token
	public static HashMap<Object, String> token = new HashMap<Object, String>();
	// 验证码token,用于修改密码等操作
	public static HashMap<String, String> code_token = new HashMap<String, String>();

	@Override
	public void intercept(ActionInvocation ai) {
		String token = ai.getController().getPara("token", "");
		int user_id = ai.getController().getParaToInt("user_id");
		if (AppToken.token.get(user_id).equals(token)) {
			ai.invoke();
		} else {
			ai.getController().renderJson("{code:-1}");
		}
		ai.invoke();
	}

	/**
	 * 检验是否合法用户登陆
	 * 
	 * @param user_id
	 * @param token
	 * @return
	 */
	public static boolean check(String token,Controller controller) {
		if(token == null || "".equals(token)){
			token = controller.getCookie("token");
		}
		Tokens tokenModel = Tokens.dao.findFirst("select token_id from tokens where token='"+token+"'");
		if (tokenModel != null) {
			return true;
		}
		return false;
	}

	/**
	 * 登陆认证
	 */
	public static String sign(String user_id) {
		Tokens tokenModel = Tokens.dao.findFirst("select token_id from tokens where user_id=?", user_id);
		if (tokenModel == null) {
			// 没有登陆
			tokenModel = new Tokens();
			String token_str = MD5Util.string2MD5("" + System.currentTimeMillis());
			tokenModel.set("user_id", user_id).set("token", token_str).set("post_time", DateUitls.getCurrentDateTime())
					.save();
			return token_str;
		} else {      
			// 登陆成功记录，则更新记录
			String token_str = MD5Util.string2MD5("" + System.currentTimeMillis());
			tokenModel.set("token", token_str).set("post_time", DateUitls.getCurrentDateTime()).update();
			return token_str;
		}
	}

	/**
	 * 获取用户id
	 * @param token
	 * @return
	 */
	public static String getUserId(String token,Controller controller) {
		if(token == null || "".equals(token)){
			token = controller.getCookie("token");
		}
		Tokens tokenModel = Tokens.dao.findFirst("select * from tokens where token='"+token+"'");
		if(tokenModel == null)
			return "";
		return ""+tokenModel.get("user_id");
	}
}
