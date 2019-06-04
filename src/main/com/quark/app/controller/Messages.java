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
 *
 */
@Before(Tx.class)
public class Messages extends Controller {
	
	@Author("cluo")
	@Rp("消息详情")
	@Explaination(info = "甜语")
	@UpdateLog(date = "2015-03-24 11:12", log = "初次添加")
	@URLParam(defaultValue = "{app、h5}", explain = Value.Infer, type = Type.String, name = "invoke")
	@ReturnDBParam(name = "SweetResponse{SweetResult:Sweet:$}", column = Sweet.sweet_id)
	@ReturnDBParam(name = "SweetResponse{SweetResult:Sweet:$}", column = Sweet.sweet)
	@ReturnDBParam(name = "SweetResponse{SweetResult:Sweet:$}", column = Sweet.time)
	@ReturnOutlet(name = "SweetResponse{status}", remarks = "1-成功", dataType = DataType.Int, defaultValue = "")
	@ReturnOutlet(name = "SweetResponse{message}", remarks = "", dataType = DataType.String, defaultValue = "")
	@ReturnOutlet(name = "SweetResponse{code}", remarks = "200-正常返回，405-重新登陆", dataType = DataType.Int, defaultValue = "")
	public void sweet() {
		try {
			//随机显示2条
			final List<Sweet> sweets = Sweet.dao.find("select sweet_id,sweet,time from sweet order by rand() limit 1");
			ResponseValues response = new ResponseValues(this,
					Thread.currentThread().getStackTrace()[1].getMethodName());
			response.put("message", "");
			response.put("status", 1);
			response.put("code", 200);
			response.put("Result", new HashMap<String, Object>() {
				{
					put("Sweet", sweets);
				}
			});
			setAttr("SweetResponse", response);
			renderMultiJson("SweetResponse");
			AppLog.info(null, getRequest());
		} catch (Exception e) {
			AppLog.error(e, getRequest());
		} finally {
			AppData.analyze("Messages/sweet", "甜语", this);
		}
	}
}
