package com.quark.admin.controller;

import java.util.List;

import org.jsoup.helper.DataUtil;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.quark.interceptor.Login;
import com.quark.interceptor.Privilege;
import com.quark.model.extend.Job;
import com.quark.model.extend.UserShape;
import com.quark.utils.DateUtils;

/**
 * jobType管理——分类
 * 
 * @author C罗
 *
 */
@Before(Login.class)
public class UserShapeTypes extends Controller {

	/**
	 * 分类列表
	 */
	public void list() {
		int currentPage = getParaToInt("pn", 1);
		String message = getPara("message",null);
		Page<UserShape> userShapePage = null;
		String except_sql = "";
		except_sql = " from user_shape order by sex asc,post_time desc";
		setAttr("action", "list");
		userShapePage = UserShape.dao.paginate(currentPage, PAGE_SIZE,
				"select * ", except_sql);
		setAttr("list", userShapePage);
		setAttr("pn", currentPage);
		if (message!=null) {
			if (message.equals("1")) {
				setAttr("ok", "添加成功");
			}
			if (message.equals("2")) {
				setAttr("ok", "添加失败，已经有同类型");
			}
			if (message.equals("3")) {
				setAttr("ok", "修改成功");
			}
			if (message.equals("4")) {
				setAttr("ok", "修改失败");
			}
			if (message.equals("5")) {
				setAttr("ok", "修改失败，已经有同类型");
			}
		}
		render("/admin/UserShapeList.html");
	}

	/**
	 * 增加
	 */
	public void add() {
		String shape_name = getPara("shape_name");
		int sort = getParaToInt("sort",0);
		int sex = getParaToInt("sex",0);//0-女性，1-男性
		UserShape userShape = new UserShape();
		if (shape_name!=null) {
			shape_name = shape_name.trim();
		}
		List<UserShape> userShapes = UserShape.dao.find("select * from user_shape where shape='"+shape_name+"' and sex="+sex);
		if (userShapes.size()==0) {
			boolean save = userShape.set(userShape.shape, shape_name).set(userShape.sex, sex)
					.set(userShape.sort, sort).set(userShape.post_time, DateUtils.getCurrentDateTime())
					.save();
			if (save) {
				redirect("/admin/UserShapeTypes/list?message=1");//添加成功
			}
		}else {
			redirect("/admin/UserShapeTypes/list?message=2");//添加失败
		}
	}

	/**
	 * 删除
	 */
	public void delete() {
		int user_shape_id = getParaToInt("user_shape_id");
		UserShape userShape = UserShape.dao.findById(user_shape_id);
		boolean delete = userShape.delete();
		redirect("/admin/UserShapeTypes/list");
	}

	/**
	 * 更新
	 */
	public void addModify() {
		int user_shape_id = getParaToInt("user_shape_id");
		String shape_name = getPara("shape_name");
		int sort = getParaToInt("sort",0);
		if (shape_name!=null) {
			shape_name = shape_name.trim();
		}
		List<UserShape> userShapes = UserShape.dao.find("select * from user_shape where shape='"+shape_name+"' and user_shape_id!="+user_shape_id);
		if (userShapes.size()==0) {
			UserShape userShape = UserShape.dao.findById(user_shape_id);
			boolean update = userShape.set(userShape.shape, shape_name)
			   .set("sort", sort).set(userShape.post_time, DateUtils.getCurrentDateTime())
			   .update();
			if (update) {
				redirect("/admin/UserShapeTypes/list?message=3");//更新成功
			}else {
				redirect("/admin/UserShapeTypes/list?message=4");//更新不成功
			}
		}else {
			redirect("/admin/UserShapeTypes/list?message=5");//已存在
		}
	}
}