package com.quark.admin.controller;

import java.text.ParseException;
import java.util.List;

import com.alipay.api.domain.Car;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.quark.common.config;
import com.quark.interceptor.Login;
import com.quark.model.extend.CarCategroy;
import com.quark.model.extend.CarClassify;
import com.quark.model.extend.Gift;
import com.quark.utils.FileUtils;
import com.quarkso.utils.DateUitls;

@Before(Login.class)
public class CarCategroyType extends Controller{
	
	
	public void list(){
		int currentPage = getParaToInt("pn", 1);
		Page<CarCategroy> cPage = null;
		cPage = CarCategroy.dao.paginate(currentPage, PAGE_SIZE,
				"select * ",
				" from car_categroy order by type asc,create_time desc");
		setAttr("list", cPage);
		setAttr("pn", currentPage);
		render("/admin/CarCategroyList.html");
	}

	public void add() {
		render("/admin/CarCategroyAdd.html");
	}
	public void addCommit(){
		UploadFile upload_cover = getFile("car_url", config.images_path);
		String type = getPara("type");
		String type_name = getPara("type_name");
		CarCategroy carCategroy = new CarCategroy();
		if (upload_cover != null) {
			carCategroy.set("car_url", FileUtils.renameToFile(upload_cover));
		}
		carCategroy.set(CarCategroy.type, type)
				.set(CarCategroy.type_name, type_name)
				.set(CarCategroy.create_time, DateUitls.getCurrentDateTime())
				.save();
		redirect("/admin/CarCategroyType/list");
	}
	/**
	 * 修改
	 */
	public void modify() {
		int currentPage = getParaToInt("pn", 1);
		int id = getParaToInt("id");
		CarCategroy carCategroy = CarCategroy.dao.findById(id);
		setAttr("r", carCategroy);
		setAttr("pn", currentPage);
		render("/admin/CarCategroyModify.html");
	}
	public void addModify() {
		UploadFile upload_cover = getFile("car_url", config.images_path);
		int currentPage = getParaToInt("pn", 1);
		String type = getPara("type");
		String type_name = getPara("type_name");
		int id = getParaToInt("id");
		CarCategroy carCategroy = CarCategroy.dao.findById(id);
		if (upload_cover != null) {
			carCategroy.set("car_url", FileUtils.renameToFile(upload_cover));
		}
		carCategroy.set(CarCategroy.type, type)
		.set("type_name", type_name)
			.set(CarCategroy.create_time, DateUitls.getCurrentDateTime())
			.update();
		redirect("/admin/CarCategroyType/list?pn="+currentPage);
	}
	
	public void findId() {
		List<CarCategroy> cList = CarCategroy.dao.find("select * from car_categroy");
		setAttr("list", cList);
		render("/admin/CarClassifyAdd.html");
	}
	
	
}
