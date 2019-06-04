package com.quark.model.extend;

import com.jfinal.plugin.activerecord.Model;

public class CarClassify extends Model<CarClassify>{
	
	public static CarClassify dao = new CarClassify();

	 public static final String id="columnName=id,remarks=汽车类型id,dataType=int,defaultValue=null";
	 
	 public static final String categroy_id="columnName=categroy_id,remarks=分类id,dataType=int,defaultValue=null";
	 
	 public static final String car_name="columnName=car_name,remarks=汽车名称,dataType=String,defaultValue=null";

	 public static final String car_url="columnName=car_url,remarks=汽车图片,dataType=String,defaultValue=null";
	 
	 public static final String create_time="columnName=create_time,remarks=创建时间,dataType=String,defaultValue=null";

}
