package com.quark.model.extend;

import com.jfinal.plugin.activerecord.Model;

public class Interest extends Model<Interest>{
	
	public static Interest dao = new Interest();

    public static final String interest_id="columnName=interest_id,remarks=兴趣ID,dataType=int,defaultValue=null";

    public static final String name="columnName=name,remarks=兴趣名称,dataType=String,defaultValue=null";

    public static final String sort="columnName=sort,remarks=排序：0-N，升序,dataType=int,defaultValue=0";

    public static final String create_time="columnName=create_time,remarks=创建时间,dataType=String,defaultValue=null";
  
}
