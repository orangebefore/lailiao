package com.quark.admin.controller;

import java.util.List;

import org.jsoup.helper.DataUtil;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.quark.interceptor.Login;
import com.quark.interceptor.Privilege;
import com.quark.model.extend.Job;
import com.quark.utils.DateUtils;

/**
 * jobType管理——分类
 * 
 * @author lucheng
 *
 */
@Before(Login.class)
public class JobTypes extends Controller {

	/**
	 * 分类列表
	 */
	public void list() {
		int currentPage = getParaToInt("pn", 1);
		String message = getPara("message",null);
		Page<Job> catalog = null;
		String except_sql = "";
		except_sql = " from job order by type asc,post_time desc";
		setAttr("action", "list");
		catalog = Job.dao.paginate(currentPage, PAGE_SIZE,
				"select * ", except_sql);
		setAttr("list", catalog);
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
		render("/admin/JobsList.html");
	}

	/**
	 * 增加
	 */
	public void add() {
		String job_name = getPara("job_name");
		int sort = getParaToInt("sort",0);
		int type = getParaToInt("type",0);//职业范围：0-甜心宝贝，1-甜心大哥
		Job job = new Job();
		if (job_name!=null) {
			job_name = job_name.trim();
		}
		List<Job> Jobs = Job.dao.find("select * from job where job='"+job_name+"' and type="+type);
		if (Jobs.size()==0) {
			boolean save = job.set("job", job_name).set("type", type)
					.set("sort", sort).set(job.post_time, DateUtils.getCurrentDateTime())
					.save();
			if (save) {
				redirect("/admin/JobTypes/list?message=1");//添加成功
			}
		}else {
			redirect("/admin/JobTypes/list?message=2");//添加失败
		}
	}

	/**
	 * 删除
	 */
	public void delete() {
		int job_id = getParaToInt("job_id");
		Job job = Job.dao.findById(job_id);
		boolean delete = job.delete();
		redirect("/admin/JobTypes/list");
	}

	/**
	 * 更新
	 */
	public void addModify() {
		int job_id = getParaToInt("job_id");
		String job_name = getPara("job_name");
		int sort = getParaToInt("sort",0);
		if (job_name!=null) {
			job_name = job_name.trim();
		}
		List<Job> jobs = Job.dao.find("select * from job where job='"+job_name+"' and job_id!="+job_id);
		if (jobs.size()==0) {
			Job job = Job.dao.findById(job_id);
			boolean update = job.set(job.job, job_name)
			   .set("sort", sort).set(job.post_time, DateUtils.getCurrentDateTime())
			   .update();
			if (update) {
				redirect("/admin/JobTypes/list?message=3");//更新成功
			}else {
				redirect("/admin/JobTypes/list?message=4");//更新不成功
			}
		}else {
			redirect("/admin/JobTypes/list?message=5");//已存在
		}
	}
}