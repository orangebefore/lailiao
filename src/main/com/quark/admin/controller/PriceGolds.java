package com.quark.admin.controller;

import java.util.List;

import org.jsoup.helper.DataUtil;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.quark.interceptor.Login;
import com.quark.interceptor.Privilege;
import com.quark.model.extend.GoldPrice;
import com.quark.model.extend.Job;
import com.quark.model.extend.Price;
import com.quark.model.extend.Tag;
import com.quark.utils.DateUtils;

/**
 * 钻石价格列表
 * 
 * @author C罗
 *
 */
@Before(Login.class)
public class PriceGolds extends Controller {

	/**
	 * 分类列表
	 */
	public void list() {
		int currentPage = getParaToInt("pn", 1);
		String message = getPara("message",null);
		Page<GoldPrice> pricePage = null;
		String except_sql = "";
		except_sql = " from gold_price order by sort asc,post_time desc";
		setAttr("action", "list");
		pricePage = GoldPrice.dao.paginate(currentPage, PAGE_SIZE,
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
		render("/admin/PriceGoldList.html");
	}

	/**
	 * 增加
	 */
	public void add() {
		int gold_value = getParaToInt("gold_value",1);
		int sort = getParaToInt("sort",0);
		String gold_price = getPara("gold_price");
		String product_id = getPara("product_id");
		GoldPrice price2 = new GoldPrice();
		if (gold_price!=null) {
			gold_price = gold_price.trim();
		}
		List<GoldPrice> prices = GoldPrice.dao.find("select * from gold_price where gold_price='"+gold_price+"' and gold_value="+gold_value);
		if (prices.size()==0) {
			boolean save = price2.set(price2.gold_price, gold_price).set("sort", sort)
					.set(price2.gold_value, gold_value).set(price2.product_id, product_id)
					.set(price2.post_time, DateUtils.getCurrentDateTime())
					.save();
			if (save) {
				redirect("/admin/PriceGolds/list?message=1");//添加成功
			}
		}else {
			redirect("/admin/PriceGolds/list?message=2");//添加失败
		}
	}

	/**
	 * 删除
	 */
	public void delete() {
		int gold_price_id = getParaToInt("gold_price_id");
		GoldPrice price = GoldPrice.dao.findById(gold_price_id);
		boolean delete = price.delete();
		redirect("/admin/PriceGolds/list?message=6");
	}

	/**
	 * 更新
	 */
	public void addModify() {
		int gold_price_id = getParaToInt("gold_price_id");
		String price_input = getPara("gold_price");
		String product_id = getPara("product_id");
		int gold_value = getParaToInt("gold_value",1);
		int sort = getParaToInt("sort",0);
		if (price_input!=null) {
			price_input = price_input.trim();
		}
		List<GoldPrice> prices = GoldPrice.dao.find("select * from gold_price where gold_price='"+price_input+"' and gold_value="+gold_value+" and gold_price_id!="+gold_price_id);
		if (prices.size()==0) {
			GoldPrice price = GoldPrice.dao.findById(gold_price_id);
			boolean update = price.set(price.gold_price, price_input).set(price.gold_value, gold_value)
					.set(price.product_id, product_id)
					.set("sort", sort).set(price.post_time, DateUtils.getCurrentDateTime())
					.update();
			if (update) {
				redirect("/admin/PriceGolds/list?message=3");//更新成功
			}else {
				redirect("/admin/PriceGolds/list?message=4");//更新不成功
			}
		}else {
			redirect("/admin/PriceGolds/list?message=5");//已存在
		}
	}
}