package com.quark.model.extend;

import com.jfinal.plugin.activerecord.Model;

public class InviteTime extends Model<InviteTime>{
	
	public static InviteTime dao = new InviteTime();

    public static final String time_id="columnName=time_id,remarks=邀约时间ID,dataType=int,defaultValue=null";
    
    public static final String time_name="columnName=time_name,remarks=时间名称,dataType=String,defaultValue=null";

}
