package com.quark.model.extend;

import com.jfinal.plugin.activerecord.Model;

public class Certification extends Model<Certification>{
	
	public static Certification dao = new Certification();

    public static final String id="columnName=id,remarks=认证ID,dataType=int,defaultValue=null";

    public static final String drivers="columnName=drivers,remarks=驾驶证,dataType=String,defaultValue=null";

    public static final String car_status="columnName=car_status,remarks=汽车认证状态:0待审核1审核通过2未通过审核,dataType=int,defaultValue=0";

    public static final String user_id="columnName=user_id,remarks=用户id,dataType=int,defaultValue=null";

}
