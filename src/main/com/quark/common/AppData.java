/**
 * 
 */
package com.quark.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.jfinal.core.Controller;
import com.quark.interceptor.AppToken;

/**
 * @author kingsley
 *
 */
public class AppData {

	private static String accessToken = "d0942118ea1a946f1113a6f10eabf95f";

	public static void analyze(final String url,final String event,Controller controller) {
		/*try {
			String user_token = controller.getPara("token","");
			final String user_id = AppToken.getUserId(user_token, controller);
			final String phone = controller.getPara("phone","-");//手机品牌名称
			final String operation = controller.getPara("operation","-");//操作系统：Android、IOS
			final String version = controller.getPara("version","-");//系统版本号:V1.2。。。。
			final String resolution = controller.getPara("resolution","-");//分辨率
			final String request_latitude = controller.getPara("request_latitude","-");//经纬度
			final String request_longitude = controller.getPara("request_longitude","-");//经纬度
			final String language = controller.getPara("language","-");//系统语言
			Jsoup.connect("http://yx.kksdapp.com:8999/company/distributed/analyze").data(new HashMap<String, String>() {
				{
					put("token", accessToken);
					put("uid", user_id);
					put("phone", phone);
					put("operation", operation);
					put("version", version);
					put("resolution", resolution);
					put("request_url", url);
					put("request_event", event);
					put("request_latitude", request_latitude);
					put("request_longitude", request_longitude);
					put("language", language);
				}
			}).ignoreContentType(true).get();
		} catch (Exception e2) {
			e2.printStackTrace();
		}*/
	}
	public static void main(String[] args) throws Exception {
	
		Document doc = Jsoup.connect("http://yx.kksdapp.com:8999/company/distributed/analyze").data(new HashMap<String, String>() {
			{
				put("token", accessToken);
				put("uid", "");
				put("phone", "");
				put("operation", "");
				put("version", "");
				put("resolution", "");
				put("request_url", "");
				put("request_event", "");
				put("request_latitude", "");
				put("request_longitude", "");
				put("language", "");
			}
		}).ignoreContentType(true).get();	
		System.out.println(doc);
	}
	
	public static String message(String language_app,String key) throws UnsupportedEncodingException {
		Locale china = null;
		if(language_app.equals("CN")){
			
			china = new Locale("zh", "CN"); 	//指定一个地域对象，等同于 Locale.CHINA
			
		}else if(language_app.equals("CA")){
			
			china = new Locale("en", "US"); 
			
		}else if(language_app.equals("JP")){
			
			china = new Locale("jp", "JP");
			
		}else if(language_app.equals("KR")){
			
			china = new Locale("kr", "KR");
		}
		ResourceBundle chineseProperties = ResourceBundle.getBundle("message", china); 		//通过制定地域读取Properties文件
        String keyValue = new String(chineseProperties.getString(key).getBytes("ISO-8859-1"), "UTF-8");  
        System.out.println(keyValue+"============");
        System.out.println(chineseProperties.getString(key)); 	//取出文件中对应Key的Value
        keyValue = jsonTransferredMeaning(keyValue);
        return keyValue;
	}
	
	public static String jsonTransferredMeaning(String theString){
	   theString = theString.replace(">", "&gt;");  
       theString = theString.replace("<", "&lt;");  
       theString = theString.replace("\"", "&quot;");  
       theString = theString.replace("\'", "&#39;");  
       theString = theString.replace("\\", "\\\\");//对斜线的转义  
       theString = theString.replace("\n", "\\n");  
       theString = theString.replace("\r", "\\r");  
       theString = theString.replace(",", "，"); 
       return theString;
	}
	
	 /**
	  * 验证邮箱
	  * @param email
	  * @return
	  */
	 public static boolean checkEmail(String email){
	  boolean flag = false;
	  try{
	    String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	    Pattern regex = Pattern.compile(check);
	    Matcher matcher = regex.matcher(email);
	    flag = matcher.matches();
	   }catch(Exception e){
	    flag = false;
	   }
	  return flag;
	 }
}
