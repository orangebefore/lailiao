package com.quark.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.submail.utils.RequestEncoder;

/**
 * http://www.qybor.com/qyb/login/loginAction/toindex.qyb 巨辰科技
 * http://www.juchn.com/a/sdk.html
 * 
 * @author Administrator
 */

public class MessageUtils {

	/**
	 * 时间戳接口配置
	 */
	public static final String TIMESTAMP = "https://api.mysubmail.com/service/timestamp";
	/**
	 * 备用接口 http://api.mysubmail.com/service/timestamp
	 * https://api.submail.cn/service/timestamp
	 * http://api.submail.cn/service/timestamp
	 */

	public static final String TYPE_MD5 = "md5";
	public static final String TYPE_SHA1 = "sha1";
	/**
	 * API 请求接口配置
	 */
	private static final String URL = "https://api.mysubmail.com/message/send";

	/**
	 * 备用接口
	 * 
	 * @param args
	 *            http://api.mysubmail.com/message/send
	 *            https://api.submail.cn/message/send
	 *            http://api.submail.cn/message/send
	 */
	/**
	 * 获取时间戳
	 * 
	 * @return
	 */
	private static String getTimestamp() {
		CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
		HttpGet httpget = new HttpGet(TIMESTAMP);
		try {
			HttpResponse response = closeableHttpClient.execute(httpget);
			HttpEntity httpEntity = response.getEntity();
			String jsonStr = EntityUtils.toString(httpEntity, "UTF-8");
			if (jsonStr != null) {
				JSONObject json = JSONObject.fromObject(jsonStr);
				return json.getString("timestamp");
			}
			closeableHttpClient.close();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void sendNotice(String telephone) {
		HashMap<String, String> post_data = new HashMap<String, String>();
		post_data.put("appid", "13556");
		post_data.put("to", telephone);
		post_data.put("project", "msdlD");
		post_data.put("signature", "e7ddeb6816d8c797f4b23765953674a4");
		try {
			Document doc = Jsoup.connect("https://api.mysubmail.com/message/xsend.json").ignoreContentType(true)
					.data(post_data).post();
			System.out.println("sendNotice");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	}

	public static void sendVipNotice(String telephone) {
		HashMap<String, String> post_data = new HashMap<String, String>();
		post_data.put("appid", "13556");
		post_data.put("to", telephone);
		post_data.put("project", "WK6Mq3");
		post_data.put("signature", "e7ddeb6816d8c797f4b23765953674a4");
		try {
			Document doc = Jsoup.connect("https://api.mysubmail.com/message/xsend.json").ignoreContentType(true)
					.data(post_data).post();
			System.out.println("sendVipNotice");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * 发送验证码
	 */
	public static String sendCode(String telephone) {
		String ram = "";
		for (int i = 0; i < 6; i++) {
			int x = (int) (Math.random() * 10);
			ram = ram + x;
		}
		HashMap<String, String> post_data = new HashMap<String, String>();
		post_data.put("appid", "13556");
		post_data.put("to", telephone);
		post_data.put("project", "yXxYA4");
		post_data.put("vars", "{\"code\":\"" + ram + "\"}");
		post_data.put("signature", "e7ddeb6816d8c797f4b23765953674a4");
		try {
			Document doc = Jsoup.connect("https://api.mysubmail.com/message/xsend.json").ignoreContentType(true)
					.data(post_data).post();
			System.out.println("sendCode");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return ram;
	}

	/**
	 * 发送验证码
	 */
	public static String sendReportMessage(String telephone) {
		String ram = "";
		for (int i = 0; i < 6; i++) {
			int x = (int) (Math.random() * 10);
			ram = ram + x;
		}
		HashMap<String, String> post_data = new HashMap<String, String>();
		post_data.put("appid", "13556");
		post_data.put("to", telephone);
		post_data.put("project", "lGEAM");
		post_data.put("vars", "{\"code\":\"" + ram + "\"}");
		post_data.put("signature", "e7ddeb6816d8c797f4b23765953674a4");
		try {
			Document doc = Jsoup.connect("https://api.mysubmail.com/message/xsend.json").ignoreContentType(true)
					.data(post_data).post();
			System.out.println("sendCode");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return ram;
	}
	/**
	 * 发送验证码
	 */
	public static String sendReportedMessage(String telephone) {
		String ram = "";
		for (int i = 0; i < 6; i++) {
			int x = (int) (Math.random() * 10);
			ram = ram + x;
		}
		HashMap<String, String> post_data = new HashMap<String, String>();
		post_data.put("appid", "13556");
		post_data.put("to", telephone);
		post_data.put("project", "jFzfe4");
		post_data.put("vars", "{\"code\":\"" + ram + "\"}");
		post_data.put("signature", "e7ddeb6816d8c797f4b23765953674a4");
		try {
			Document doc = Jsoup.connect("https://api.mysubmail.com/message/xsend.json").ignoreContentType(true)
					.data(post_data).post();
			System.out.println("sendCode");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return ram;
	}

	public static void main(String[] args) {
		sendCode("18520844021");
	}
}
