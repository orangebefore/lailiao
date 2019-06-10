package com.quark.model.extend;

import com.jfinal.plugin.activerecord.Model;

public class UserExpect extends Model<UserExpect>{
	
	public static UserExpect dao = new UserExpect();
	
    public static final String expect_id="columnName=expect_id,remarks=期望ID,dataType=int,defaultValue=null";

    public static final String expect_name="columnName=expect_name,remarks=期望名称,dataType=String,defaultValue=null";

    public static final String create_time="columnName=create_time,remarks=上传时间,dataType=String,defaultValue=null";

    public static final String expect_gender="columnName=expect_gender,remarks=0-女，1-男,2-通用,dataType=int,defaultValue=0";
}
