package com.quark.admin.controller;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.quark.interceptor.Login;
import com.quark.model.extend.Price;
import com.quark.model.extend.SuperstarPrice;
import com.quark.utils.DateUtils;

/**
 * tagType管理——分类
 * 
 * @author C罗
 *
 */
@Before(Login.class)
public class SuperstarPrices extends Controller {

	/**
	 * 分类列表
	 */
	public void list() {
		int currentPage = getParaToInt("pn", 1);
		String message = getPara("message",null);
		Page<SuperstarPrice> superPricePage = null;
		String except_sql = "";
		except_sql = " from superstar_price order by price asc,post_time desc";
		setAttr("action", "list");
		superPricePage = SuperstarPrice.dao.paginate(currentPage, PAGE_SIZE,
				"select * ", except_sql);
		setAttr("list", superPricePage);
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
			if (message.equals("6")) {
				setAttr("ok", "删除成功");
			}
		}
		render("/admin/SuperstarPrice.html");
	}

	/**
	 * 增加
	 */
	public void add() {
		int hours = getParaToInt("hours",1);
		String price = getPara("price");
		String product_id = getPara("product_id");
		SuperstarPrice superstarPrice = new SuperstarPrice();
		if (price!=null) {
			price = price.trim();
		}
		List<SuperstarPrice> superstarPrices = SuperstarPrice.dao.find("select * from superstar_price where price='"+price+"' and hours="+hours);
		
		if (superstarPrices.size()==0) {
			boolean save = superstarPrice.set(SuperstarPrice.price, price)
					.set(superstarPrice.hours, hours).set(superstarPrice.product_id, product_id)
					.set(superstarPrice.post_time, DateUtils.getCurrentDateTime())
					.save();
			if (save) {
				redirect("/admin/SuperstarPrices/list?message=1");//添加成功
			}
		}else {
			redirect("/admin/SuperstarPrices/list?message=2");//添加失败
		}
	}

	/**
	 * 删除
	 */
	public void delete() {
		int sp_id = getParaToInt("sp_id");
		SuperstarPrice superstarPrice = SuperstarPrice.dao.findById(sp_id);
		boolean delete = superstarPrice.delete();
		redirect("/admin/SuperstarPrices/list?message=6");
	}

	/**
	 * 更新
	 */
	public void addModify() {
		int sp_id = getParaToInt("sp_id");
		String price_input = getPara("price");
		String product_id = getPara("product_id");
		int hours = getParaToInt("hours",1);
		if (price_input!=null) {
			price_input = price_input.trim();
		}
		List<SuperstarPrice> superstarPrices = SuperstarPrice.dao.find("select * from superstar_price where price='"+price_input+"' and hours="+hours+" and sp_id!="+sp_id);
		if (superstarPrices.size()==0) {
			SuperstarPrice superstarPrice = SuperstarPrice.dao.findById(sp_id);
			boolean update = superstarPrice.set(superstarPrice.price, price_input).set(superstarPrice.hours, hours)
					.set(superstarPrice.product_id, product_id)
					.set(superstarPrice.post_time, DateUtils.getCurrentDateTime())
					.update();
			if (update) {
				redirect("/admin/SuperstarPrices/list?message=3");//更新成功
			}else {
				redirect("/admin/SuperstarPrices/list?message=4");//更新不成功
			}
		}else {
			redirect("/admin/SuperstarPrices/list?message=5");//已存在
		}
	}
}