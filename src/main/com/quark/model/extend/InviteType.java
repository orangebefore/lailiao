package com.quark.model.extend;

import com.jfinal.plugin.activerecord.Model;

public class InviteType extends Model<InviteType>{
	
	public static InviteType dao = new InviteType();

    public static final String type_id="columnName=type_id,remarks=邀约类型id,dataType=int,defaultValue=null";
    
    public static final String type_name="columnName=type_name,remarks=类型名称,dataType=String,defaultValue=null";

}
