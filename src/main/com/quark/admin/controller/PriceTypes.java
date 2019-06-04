package com.quark.admin.controller;

import java.util.List;

import org.jsoup.helper.DataUtil;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.quark.interceptor.Login;
import com.quark.interceptor.Privilege;
import com.quark.model.extend.Job;
import com.quark.model.extend.Price;
import com.quark.model.extend.Tag;
import com.quark.utils.DateUtils;

/**
 * tagType管理——分类
 * 
 * @author C罗
 *
 */
@Before(Login.class)
public class PriceTypes extends Controller {

	/**
	 * 分类列表
	 */
	public void list() {
		int currentPage = getParaToInt("pn", 1);
		String message = getPara("message",null);
		Page<Price> pricePage = null;
		String except_sql = "";
		except_sql = " from price order by price asc,post_time desc";
		setAttr("action", "list");
		pricePage = Price.dao.paginate(currentPage, PAGE_SIZE,
				"select * ", except_sql);
		setAttr("list", pricePage);
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
		render("/admin/PriceList.html");
	}

	/**
	 * 增加
	 */
	public void add() {
		int days = getParaToInt("days",1);
		int sort = getParaToInt("sort",0);
		String price = getPara("price");
		String product_id = getPara("product_id");
		Price price2 = new Price();
		if (price!=null) {
			price = price.trim();
		}
		List<Price> prices = Price.dao.find("select * from price where price='"+price+"' and days="+days);
		
		if (prices.size()==0) {
			boolean save = price2.set(price2.price, price).set("sort", sort)
					.set(price2.days, days).set(price2.product_id, product_id)
					.set(price2.post_time, DateUtils.getCurrentDateTime())
					.save();
			if (save) {
				redirect("/admin/PriceTypes/list?message=1");//添加成功
			}
		}else {
			redirect("/admin/PriceTypes/list?message=2");//添加失败
		}
	}

	/**
	 * 删除
	 */
	public void delete() {
		int price_id = getParaToInt("price_id");
		Price price = Price.dao.findById(price_id);
		boolean delete = price.delete();
		redirect("/admin/PriceTypes/list?message=6");
	}

	/**
	 * 更新
	 */
	public void addModify() {
		int price_id = getParaToInt("price_id");
		String price_input = getPara("price");
		String product_id = getPara("product_id");
		int days = getParaToInt("days",1);
		int sort = getParaToInt("sort",0);
		if (price_input!=null) {
			price_input = price_input.trim();
		}
		List<Price> prices = Price.dao.find("select * from price where price='"+price_input+"' and days="+days+" and price_id!="+price_id);
		if (prices.size()==0) {
			Price price = Price.dao.findById(price_id);
			boolean update = price.set(price.price, price_input).set(price.days, days)
					.set(price.product_id, product_id)
					.set("sort", sort).set(price.post_time, DateUtils.getCurrentDateTime())
					.update();
			if (update) {
				redirect("/admin/PriceTypes/list?message=3");//更新成功
			}else {
				redirect("/admin/PriceTypes/list?message=4");//更新不成功
			}
		}else {
			redirect("/admin/PriceTypes/list?message=5");//已存在
		}
	}
}