package com.quark.model.extend;

import com.jfinal.plugin.activerecord.Model;

public class TravelDays extends Model<TravelDays>{
	
	public static TravelDays dao = new TravelDays();

    public static final String travel_days_id="columnName=travel_days_id,remarks=出行天数id,dataType=int,defaultValue=null";
    
    public static final String travel_days_name="columnName=travel_days_name,remarks=出行时长,dataType=String,defaultValue=null";

}
