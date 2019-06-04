package com.quark.model.extend;

import com.jfinal.plugin.activerecord.Model;

public class CarCategroy extends Model<CarCategroy>{
	
	 public static CarCategroy dao = new CarCategroy();

	 public static final String id="columnName=id,remarks=汽车类型id,dataType=int,defaultValue=null";

	 public static final String car_url="columnName=car_url,remarks=分类图片,dataType=String,defaultValue=null";

	 public static final String type="columnName=type,remarks=品牌类型,dataType=String,defaultValue=null";
	 
	 public static final String create_time="columnName=create_time,remarks=创建时间,dataType=String,defaultValue=null";

	 public static final String type_name="columnName=type_name,remarks=品牌名称,dataType=String,defaultValue=null";

}
