package com.quark.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20170112.ImageSyncScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.util.*;

/**
 * Created by liuhai.lh on 2017/2/17. 图片同步检测接口
 * 
 * @author liuhai.lh
 * @date 2017/02/17
 */
public class ImageSyncScan extends AliyunSecurityBase {

	public static AliyunSecurityResult scanCover(String url) throws Exception {
		// 请替换成你自己的accessKeyId、accessKeySecret
		IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint(getEndPointName(), regionId, "Green", getDomain());
		IAcsClient client = new DefaultAcsClient(profile);

		ImageSyncScanRequest imageSyncScanRequest = new ImageSyncScanRequest();
		imageSyncScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
		imageSyncScanRequest.setContentType(FormatType.JSON);
		imageSyncScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
		imageSyncScanRequest.setEncoding("utf-8");
		imageSyncScanRequest.setRegionId(regionId);

		List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
		Map<String, Object> task = new LinkedHashMap<String, Object>();
		task.put("dataId", UUID.randomUUID().toString());
		task.put("url", url);
		task.put("time", new Date());

		tasks.add(task);
		JSONObject data = new JSONObject();
		/**
		 * porn: 色情 terrorism: 暴恐 qrcode: 二维码 ad: 图片广告 ocr: 文字识别 sface:铭感人脸识别
		 */
		data.put("scenes", Arrays.asList("sface", "porn", "terrorism", "qrcode", "ad"));
		data.put("tasks", tasks);

		imageSyncScanRequest.setContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);

		/**
		 * 请务必设置超时时间
		 */
		imageSyncScanRequest.setConnectTimeout(3000);
		imageSyncScanRequest.setReadTimeout(6000);
		boolean pass = true;
		String info = new String();
		try {
			HttpResponse httpResponse = client.doAction(imageSyncScanRequest);
			if (httpResponse.isSuccess()) {
				JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getContent(), "UTF-8"));
				// System.out.println(JSON.toJSONString(scrResponse, true));
				if (200 == scrResponse.getInteger("code")) {
					JSONArray taskResults = scrResponse.getJSONArray("data");
					for (Object taskResult : taskResults) {
						if (200 == ((JSONObject) taskResult).getInteger("code")) {
							JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
							// porn: 色情、terrorism: 暴恐、qrcode: 二维码、ad: 图片广告、ocr: 文字识别、sface:铭感人脸识别
							for (Object sceneResult : sceneResults) {
								String scene = ((JSONObject) sceneResult).getString("scene");
								String suggestion = ((JSONObject) sceneResult).getString("suggestion");
								// 根据scene和suggetion做相关的处理
								// do something
								System.out.println("args = [" + scene + "]");
								System.out.println("args = [" + suggestion + "]");
								if ("sface".equals(scene)) {
									if (!"review".equals(suggestion)) {
										pass = false;
										info = info + ("无法明显识别人脸、");
									}
								}
								if ("porn".equals(scene)) {
									if (!"pass".equals(suggestion)) {
										pass = false;
										info = info + ("涉及色情、");
									}
								}
								if ("terrorism".equals(scene)) {
									if (!"pass".equals(suggestion)) {
										pass = false;
										info = info + ("涉及暴恐、");
									}
								}
								if ("qrcode".equals(scene)) {
									if (!"pass".equals(suggestion)) {
										pass = false;
										info = info + ("包含二维码、");
									}
								}
								if ("ad".equals(scene)) {
									if (!"pass".equals(suggestion)) {
										pass = false;
										info = info + ("包含广告信息、");
									}
								}

							}

						} else {
							// System.out.println("task process fail:" +
							// ((JSONObject)taskResult).getInteger("code"));
						}
					}
				} else {
					// System.out.println("detect not success. code:" +
					// scrResponse.getInteger("code"));
				}
			} else {
				// System.out.println("response not success. status:" +
				// httpResponse.getStatus());
			}
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		AliyunSecurityResult ret = new AliyunSecurityResult();
		ret.setPass(pass);
		ret.setInfo(info);
		return ret;
	}
	public static AliyunSecurityResult scanPublish(String url) throws Exception {
		// 请替换成你自己的accessKeyId、accessKeySecret
		IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint(getEndPointName(), regionId, "Green", getDomain());
		IAcsClient client = new DefaultAcsClient(profile);

		ImageSyncScanRequest imageSyncScanRequest = new ImageSyncScanRequest();
		imageSyncScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
		imageSyncScanRequest.setContentType(FormatType.JSON);
		imageSyncScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
		imageSyncScanRequest.setEncoding("utf-8");
		imageSyncScanRequest.setRegionId(regionId);

		List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
		Map<String, Object> task = new LinkedHashMap<String, Object>();
		task.put("dataId", UUID.randomUUID().toString());
		task.put("url", url);
		task.put("time", new Date());

		tasks.add(task);
		JSONObject data = new JSONObject();
		/**
		 * porn: 色情 terrorism: 暴恐 qrcode: 二维码 ad: 图片广告 ocr: 文字识别 sface:铭感人脸识别
		 */
		data.put("scenes", Arrays.asList("porn", "terrorism", "qrcode", "ad"));
		data.put("tasks", tasks);

		imageSyncScanRequest.setContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);

		/**
		 * 请务必设置超时时间
		 */
		imageSyncScanRequest.setConnectTimeout(3000);
		imageSyncScanRequest.setReadTimeout(6000);
		boolean pass = true;
		String info = new String();
		try {
			HttpResponse httpResponse = client.doAction(imageSyncScanRequest);
			if (httpResponse.isSuccess()) {
				JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getContent(), "UTF-8"));
				// System.out.println(JSON.toJSONString(scrResponse, true));
				if (200 == scrResponse.getInteger("code")) {
					JSONArray taskResults = scrResponse.getJSONArray("data");
					for (Object taskResult : taskResults) {
						if (200 == ((JSONObject) taskResult).getInteger("code")) {
							JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
							// porn: 色情、terrorism: 暴恐、qrcode: 二维码、ad: 图片广告、ocr: 文字识别、sface:铭感人脸识别
							for (Object sceneResult : sceneResults) {
								String scene = ((JSONObject) sceneResult).getString("scene");
								String suggestion = ((JSONObject) sceneResult).getString("suggestion");
								// 根据scene和suggetion做相关的处理
								// do something
								System.out.println("args = [" + scene + "]");
								System.out.println("args = [" + suggestion + "]");
								if ("sface".equals(scene)) {
									if (!"review".equals(suggestion)) {
										pass = false;
										info = info + ("无法明显识别人脸、");
									}
								}
								if ("porn".equals(scene)) {
									if (!"pass".equals(suggestion)) {
										pass = false;
										info = info + ("涉及色情、");
									}
								}
								if ("terrorism".equals(scene)) {
									if (!"pass".equals(suggestion)) {
										pass = false;
										info = info + ("涉及暴恐、");
									}
								}
								if ("qrcode".equals(scene)) {
									if (!"pass".equals(suggestion)) {
										pass = false;
										info = info + ("包含二维码、");
									}
								}
								if ("ad".equals(scene)) {
									if (!"pass".equals(suggestion)) {
										pass = false;
										info = info + ("包含广告信息、");
									}
								}

							}

						} else {
							// System.out.println("task process fail:" +
							// ((JSONObject)taskResult).getInteger("code"));
						}
					}
				} else {
					// System.out.println("detect not success. code:" +
					// scrResponse.getInteger("code"));
				}
			} else {
				// System.out.println("response not success. status:" +
				// httpResponse.getStatus());
			}
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		AliyunSecurityResult ret = new AliyunSecurityResult();
		ret.setPass(pass);
		ret.setInfo(info);
		return ret;
	}
}
