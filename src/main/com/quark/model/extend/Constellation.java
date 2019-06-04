package com.quark.model.extend;

import com.jfinal.plugin.activerecord.Model;

public class Constellation extends Model<Constellation>{
	
	public static Constellation dao = new Constellation();

    public static final String star_id="columnName=star_id,remarks=星座ID,dataType=int,defaultValue=null";

    public static final String star_name="columnName=star_name,remarks=星座名称,dataType=String,defaultValue=null";

    public static final String sort="columnName=sort,remarks=排序：0-N，升序,dataType=int,defaultValue=0";

    public static final String create_time="columnName=create_time,remarks=创建时间,dataType=String,defaultValue=null";
}
