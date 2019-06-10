package com.quark.model.extend;

import com.jfinal.plugin.activerecord.Model;

public class InviteCost extends Model<InviteCost>{
	
	public static InviteCost dao = new InviteCost();

    public static final String cost_id="columnName=cost_id,remarks=费用表ID,dataType=int,defaultValue=null";
    
    public static final String cost_name="columnName=cost_name,remarks=承担费用方式,dataType=String,defaultValue=null";

}
