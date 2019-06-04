package com.quark.model.extend;

import com.jfinal.plugin.activerecord.Model;

public class LikeDate extends Model<LikeDate>{
	
	public static LikeDate dao = new LikeDate();

    public static final String date_id="columnName=date_id,remarks=约会方式ID,dataType=int,defaultValue=null";

    public static final String date_name="columnName=date_name,remarks=约会名称,dataType=String,defaultValue=null";

    public static final String sort="columnName=sort,remarks=排序：0-N，升序,dataType=int,defaultValue=0";

    public static final String create_time="columnName=create_time,remarks=创建时间,dataType=String,defaultValue=null";
  
}
