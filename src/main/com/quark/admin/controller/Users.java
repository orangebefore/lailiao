package com.quark.admin.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.eclipse.jetty.util.UrlEncoded;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.upload.UploadFile;
import com.quark.common.RongToken;
import com.quark.common.config;
import com.quark.interceptor.Login;
import com.quark.interceptor.Privilege;
import com.quark.model.extend.BlackList;
import com.quark.model.extend.Job;
import com.quark.model.extend.Price;
import com.quark.model.extend.Tag;
import com.quark.model.extend.User;
import com.quark.model.extend.UserIncome;
import com.quark.model.extend.UserOld;
import com.quark.model.extend.UserShape;
import com.quark.model.extend.UserTag;
import com.quark.utils.DateUtils;
import com.quark.utils.FileUtils;
import com.quark.utils.MD5Util;
import com.quarkso.utils.DateUitls;

import io.rong.models.FormatType;

/**
 * 用戶列表
 * 
 * @author C罗 用户状态：0-封号，1-正常
 */
@Before(Login.class)
public class Users extends Controller {

	public void list() throws ParseException {
		int currentPage = getParaToInt("pn", 1);
		int is_vip = getParaToInt("is_vip", 2);
		int sex = getParaToInt("sex", 2);

		String sheng = getPara("province", "全部"); // 省
		String shi = getPara("city", "全部"); // 市

		String kw = getPara("kw", "");
		String message = "list";
		Page<User> users = null;
		String filter_sql = " status=1 and sex=0 ";
		if (is_vip != 2) {
			filter_sql = filter_sql + " and is_vip=" + is_vip;
			message = "search";
		}
		/*
		 * if (sex!=2) { filter_sql=filter_sql+" and sex="+sex;
		 * message="search"; }
		 */
		if (!kw.equals("")) {
			kw = kw.trim();
			filter_sql = filter_sql + "  and (telephone like '%" + kw + "%' or nickname like '%" + kw + "%') ";
			message = "search";
		}
		// 地区
		if (!sheng.equals("全部")) {
			if (!sheng.equals("请选择") && !sheng.equals("")) {
				filter_sql = filter_sql + " and province like'%" + sheng + "%'";
				message = "search";
			}
		}
		if (!shi.equals("全部")) {
			if (!shi.equals("请选择") && !shi.equals("")) {
				filter_sql = filter_sql + " and city like'%" + shi + "%'";
				message = "search";
			}
		}
		setAttr("province", sheng);
		setAttr("city", shi);
		setAttr("kw", kw);
		setAttr("action", message);

		users = User.dao.paginate(currentPage, PAGE_SIZE, "select * ",
				"from user where " + filter_sql + " order by sweet desc,hot desc,is_set_heart desc ");
		for (User user : users.getList()) {
			// 什么时候登录
			String last_login_time = user.getTimestamp("last_login_time").toString();
			String current_time = DateUtils.getCurrentDateTime();
			String active_time = DateUtils.getActiveTime(last_login_time, current_time);
			user.put("active_time", active_time);
		}
		setAttr("is_vip", is_vip);
		setAttr("sex", sex);
		setAttr("list", users);
		setAttr("pn", currentPage);
		render("/admin/UserList.html");
	}

	public void userInfo() {
		int currentPage = getParaToInt("pn", 1);
		int is_vip = getParaToInt("is_vip", 2);
		int sex = getParaToInt("sex", 2);
		String province = getPara("province", "全部"); // 省
		String city = getPara("city", "全部"); // 市
		String kw = getPara("kw", "");

		int user_id = getParaToInt("user_id");
		User user = User.dao.findById(user_id);
		setAttr("r", user);
		setAttr("pn", currentPage);
		setAttr("is_vip", is_vip);
		setAttr("sex", sex);
		setAttr("kw", kw);
		setAttr("province", province);
		setAttr("city", city);
		
		
		
		render("/admin/UserInfo.html");
	}

	public void delete() {
		int user_id = getParaToInt("user_id");
		User.dao.deleteById(user_id);
		redirect("/admin/Users/list");
	}

	/**
	 * 1:甜蜜度,2:热度
	 */
	public void updateData() {
		int user_id = getParaToInt("user_id");
		int num = getParaToInt("num");
		int type = getParaToInt("type");
		User user = User.dao.findById(user_id);
		if (type == 1) {
			user.set(user.sweet, num);
		}
		if (type == 2) {
			user.set(user.hot, num);
		}
		boolean save = user.update();
		renderJson("message", save);
	}

	public void report() {
		int currentPage = getParaToInt("pn", 1);
		Page<BlackList> blPage = null;

		blPage = BlackList.dao.paginate(currentPage, PAGE_SIZE, "select * ",
				"from black_list where type=1 order by post_time desc");
		for(int i=0;i<blPage.getList().size();i++){
		//for (BlackList user : blPage.getList()) {
			int user_id = blPage.getList().get(i).get("user_id");
			User user2 = User.dao.findById(user_id);
			int black_user_id = blPage.getList().get(i).get("black_user_id");
			User user3 = User.dao.findById(black_user_id);
			if (user2 == null || user3==null){
				blPage.getList().remove(i);
				continue;
			}
			String nickname = user2.getStr("nickname");
			if (nickname.equals("")) {
				nickname = user2.getStr("telephone");
			}
			blPage.getList().get(i).put("nickname", nickname);
			blPage.getList().get(i).put("sex", user2.get(User.sex));

			
			int status = 0;
			String nickname2 = "";
			if (user3 != null) {
				status = user3.get("status");
				nickname2 = user3.getStr("nickname");
				if (nickname2.equals("")) {
					nickname2 = user3.getStr("telephone");
				}
				blPage.getList().get(i).put("black_sex", user3.get(User.sex));
			}
			blPage.getList().get(i).put("status", status);
			blPage.getList().get(i).put("black_nickname", nickname2);

			List<BlackList> blackLists = BlackList.dao
					.find("select count(black_user_id) from black_list where black_user_id=" + black_user_id);
			blPage.getList().get(i).put("report_size", blackLists.size());
		}
		setAttr("list", blPage);
		setAttr("pn", currentPage);
		render("/admin/ReportList.html");
	}

	public void unfreeze() throws Exception {
		int currentPage = getParaToInt("pn", 1);
		int is_vip = getParaToInt("is_vip", 2);
		int sex = getParaToInt("sex", 2);
		String province = getPara("province", "全部"); // 省
		String city = getPara("city", "全部"); // 市
		String note = getPara("user_note", ""); // 市

		String kw = getPara("kw", "");

		int black_user_id = getParaToInt("black_user_id");
		int status = getParaToInt("user_status");
		int black_status = getParaToInt("black_status");
		User user = User.dao.findById(black_user_id);
		user.set(user.heart,note).set(user.status, status).set(user.black_status, black_status).update();
		if(black_status==1) {
			RongToken.blockUser(""+black_user_id, FormatType.json);

		}
		if(black_status==0) {
			RongToken.unBlockUser(""+black_user_id, FormatType.json);
		}
		redirect("/admin/Users/list?pn=" + currentPage + "&is_vip=" + is_vip + "&sex=" + sex + "&province="
				+ URLEncoder.encode(province, "UTF-8") + "&city=" + URLEncoder.encode(city, "UTF-8") + "&kw="
				+ URLEncoder.encode(kw, "UTF-8"));
	}

	public void free() {
		int currentPage = getParaToInt("pn", 1);
		int black_user_id = getParaToInt("black_user_id");
		int type = getParaToInt("type");
		int status = 0;
		if (type == 0) {
			status = 1;
		} else {
			status = 0;
		}
		User user = User.dao.findById(black_user_id);
		user.set(user.status, status).update();
		redirect("/admin/Users/report?pn=" + currentPage);
	}

	public void add() {
		List<UserIncome> user_incomes = UserIncome.dao.find("select * from user_income order by sort asc");
		setAttr("user_incomes", user_incomes);
		render("/admin/UserAdd.html");
	}

	public void getJobs() {
		int sex = getParaToInt("sex");
		List<Job> jobs = Job.dao.find("select * from job where type=? order by sort asc", sex);
		renderJson(jobs);
	}

	public void getUserShape() {
		int sex = getParaToInt("sex");
		List<UserShape> jobs = UserShape.dao.find("select * from user_shape where sex=? order by sort asc", sex);
		renderJson(jobs);
	}

	public void getUserTag() {
		int sex = getParaToInt("sex");
		List<Tag> jobs = Tag.dao.find("select * from tag where type=? order by sort asc", sex);
		renderJson(jobs);
	}

	public void addCommit() {
		UploadFile image_01 = getFile("image_01", config.images_path);
		UploadFile image_02 = getFile("image_02", config.images_path);
		UploadFile image_03 = getFile("image_03", config.images_path);
		UploadFile image_04 = getFile("image_04", config.images_path);
		UploadFile image_05 = getFile("image_05", config.images_path);
		UploadFile image_06 = getFile("image_06", config.images_path);
		User user = new User();
		if (image_01 != null) {
			user.set("image_01", FileUtils.renameToFile(image_01));
		}
		if (image_02 != null) {
			user.set("image_02", FileUtils.renameToFile(image_02));
		}
		if (image_03 != null) {
			user.set("image_03", FileUtils.renameToFile(image_03));
		}
		if (image_04 != null) {
			user.set("image_04", FileUtils.renameToFile(image_04));
		}
		if (image_05 != null) {
			user.set("image_05", FileUtils.renameToFile(image_05));
		}
		if (image_06 != null) {
			user.set("image_06", FileUtils.renameToFile(image_06));
		}
		String telephone = getPara("telephone");
		String password = getPara("password");
		String nickname = getPara("nickname");
		int sex = getParaToInt("sex", 0);
		int taste = getParaToInt("taste", 1);
		String birthday = getPara("birthday");

		String job = getPara("job");
		String height = getPara("height");
		String income = getPara("income");
		String edu = getPara("edu");
		String user_shape = getPara("user_shape");
		String SelProvince = getPara("SelProvince");
		String SelCity = getPara("SelCity");
		String marry = getPara("marry");
		String drink = getPara("drink");
		String smoke = getPara("smoke");
		String heart = getPara("heart");
		if (sex == 1) {
			String vip_from_datetime = DateUtils.getCurrentDateTime();
			user.set(user.vip_from_datetime, vip_from_datetime)
					.set(user.vip_end_datetime, DateUtils.getAddDaysString(3, vip_from_datetime)).set(user.is_vip, 1);
		}
		boolean save = user.set(User.telephone, telephone).set(User.password, MD5Util.string2MD5(password))
				.set(User.nickname, nickname).set(User.birthday, birthday).set(User.job, job).set(User.height, height)
				.set(User.sex, sex).set(User.taste, taste).set(User.province, SelProvince).set(User.city, SelCity)
				.set(User.heart, heart).set(User.income, income).set(User.edu, edu).set(User.shape, user_shape)
				.set(User.marry, marry).set(User.drink, drink).set(User.smoke, smoke)
				.set(User.regist_time, DateUtils.getCurrentDateTime())
				.set(User.last_login_time, DateUtils.getCurrentDateTime())
				.set(User.regist_date, DateUtils.getCurrentDate())
				.set(User.regist_hour, DateUtils.getCurrentDateHours()).set(User.hot, 10).set(User.sweet, 10).save();
		if (save) {
			int user_id = user.get("user_id");
			String[] user_tags = getParaValues("user_tag");
			if (user_tags != null) {
				for (int i = 0; i < user_tags.length; i++) {
					UserTag userTag = new UserTag();
					userTag.set(userTag.user_id, user_id).set(userTag.tag, user_tags[i]).set(userTag.status, 1)
							.set(userTag.post_time, DateUtils.getCurrentDateTime()).set(userTag.type, sex).save();
				}
			}
		}
		redirect("/admin/Users/list");
	}
}
