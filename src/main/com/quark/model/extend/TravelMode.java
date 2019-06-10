package com.quark.model.extend;

import com.jfinal.plugin.activerecord.Model;

public class TravelMode extends Model<TravelMode>{
	
	public static TravelMode dao = new TravelMode();

    public static final String travel_mode_id="columnName=travel_mode_id,remarks=出行方式id,dataType=int,defaultValue=null";
    
    public static final String travel_mode_name="columnName=travel_mode_name,remarks=出行方式,dataType=String,defaultValue=null";

}
