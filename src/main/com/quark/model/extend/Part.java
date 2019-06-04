package com.quark.model.extend;

import com.jfinal.plugin.activerecord.Model;

public class Part extends Model<Part>{
	
	public static Part dao = new Part();
	
	public static final String part_id="columnName=part_id,remarks=满意集ID,dataType=int,defaultValue=null";

    public static final String part_name="columnName=part_name,remarks=满意部位名称,dataType=String,defaultValue=null";

    public static final String create_time="columnName=create_time,remarks=上传时间,dataType=String,defaultValue=null";

    public static final String sort="columnName=sort,remarks=排序：0-N，升序,dataType=int,defaultValue=0";

    public static final String type="columnName=type,remarks=0-女，1-男,dataType=int,defaultValue=0";
}
