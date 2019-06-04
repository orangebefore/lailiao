package com.quark.admin.controller;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.quark.common.config;
import com.quark.interceptor.Login;
import com.quark.model.extend.CarCategroy;
import com.quark.model.extend.CarClassify;
import com.quark.utils.FileUtils;
import com.quarkso.utils.DateUitls;import com.sun.java_cup.internal.runtime.virtual_parse_stack;

@Before(Login.class)
public class CarClassifyType extends Controller{
	
	public void list(){
		int currentPage = getParaToInt("pn", 1);
		Page<CarClassify> cPage = null;
		cPage = CarClassify.dao.paginate(currentPage, PAGE_SIZE,
				"select * ",
				" from car_classify order by categroy_id asc,create_time desc");
		setAttr("list", cPage);
		setAttr("pn", currentPage);
		render("/admin/CarClassifyList.html");
	}

	public void add() {
		render("/admin/CarClassifyAdd.html");
	}
	public void addCommit(){
		UploadFile upload_cover = getFile("car_url", config.images_path);
		String car_name = getPara("car_name");
		CarClassify carClassify = new CarClassify();
		if (upload_cover != null) {
			carClassify.set("car_url", FileUtils.renameToFile(upload_cover));
		}
		carClassify.set(CarClassify.car_name, car_name)
				.set(CarClassify.create_time, DateUitls.getCurrentDateTime())
				.save();
		redirect("/admin/CarClassifyType/list");
	}
	/**
	 * 修改
	 */
	public void modify() {
		int currentPage = getParaToInt("pn", 1);
		int id = getParaToInt("id");
		CarClassify carClassify = CarClassify.dao.findById(id);
		setAttr("r", carClassify);
		setAttr("pn", currentPage);
		render("/admin/CarClassifyModify.html");
	}
	public void addModify() {
		UploadFile upload_cover = getFile("car_url", config.images_path);
		int currentPage = getParaToInt("pn", 1);
		String car_name = getPara("car_name");
		
		int id = getParaToInt("id");
		CarClassify carClassify = CarClassify.dao.findById(id);
		if (upload_cover != null) {
			carClassify.set("car_url", FileUtils.renameToFile(upload_cover));
		}
		carClassify.set(CarClassify.car_name, car_name)
			.set(CarClassify.create_time, DateUitls.getCurrentDateTime())
			.update();
		redirect("/admin/CarClassifyType/list?pn="+currentPage);
	}
	
}
