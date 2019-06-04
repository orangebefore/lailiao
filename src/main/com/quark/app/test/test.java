package com.quark.app.test;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.lowagie.text.List;
import com.quark.mail.SendMail;
import com.quark.utils.MD5Util;

public class test {

	public static void just(){
		try {
			String name = Thread.currentThread().getStackTrace()[1].getMethodName();
			System.out.println(name);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			System.out.println("final");
		}
	}
	
	  /** 
     * 将emoji表情替换成* 
     *  
     * @param source 
     * @return 过滤后的字符串 
     */  
    public static String filterEmoji(String source) {  
        if(StringUtils.isNotBlank(source)){  
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");  
        }else{  
            return source;  
        }  
    }  
    
	public static void main(String[] args) throws IOException, ParseException {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		System.out.println(sdf.format(dt)); ;
		
		  System.out.println(getAddDaysString(5,"2016-01-20 09:41:38"));

	}
	public static String getAddDaysString(int days,String validatetime){
		try
		 {
		  Calendar fromCal=Calendar.getInstance();
		  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		  Date date=dateFormat.parse(validatetime);
		  fromCal.setTime(date);
		  fromCal.add(Calendar.DATE, days);
		 
		  System.out.println(dateFormat.format(fromCal.getTime()));
		  return dateFormat.format(fromCal.getTime());
		 }
		 catch(Exception e)
		 {
		  e.printStackTrace();
		  return "";
		 }
		
	}
	/**

	 * 获取微信授权的code

	 * @author sun

	 *

	 */


	    public static String  GetCodeRequest = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

	   //获取授权请求

	    public static String getCodeRequest(){

	        String result = null;

	        GetCodeRequest  = GetCodeRequest.replace("APPID", urlEnodeUTF8(""));

	        GetCodeRequest  = GetCodeRequest.replace("REDIRECT_URI",urlEnodeUTF8(""));

	        GetCodeRequest = GetCodeRequest.replace("SCOPE", "");

	        result = GetCodeRequest;

	        return result;

	    }

	    //进行转码

	    public static String urlEnodeUTF8(String str){

	        String result = str;

	        try {

	            result = URLEncoder.encode(str,"UTF-8");

	        } catch (Exception e) {

	            e.printStackTrace();

	        }

	        return result;

	    }
}
