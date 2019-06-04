package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:00
*/
public class Audit extends Model<Audit>{

    public static Audit dao = new Audit();

    public static final String id="columnName=id,remarks=,dataType=bigint,defaultValue=null";

    public static final String drivers="columnName=drivers,remarks=,dataType=String,defaultValue=null";

    public static final String id_card_up="columnName=id_card_up,remarks=,dataType=String,defaultValue=null";

    public static final String id_card_down="columnName=id_card_down,remarks=,dataType=String,defaultValue=null";

    public static final String video_url="columnName=video_url,remarks=,dataType=String,defaultValue=null";
    
    public static final String house_city="columnName=house_city,remarks=,dataType=String,defaultValue=null";
    
    public static final String house_url="columnName=house_url,remarks=,dataType=String,defaultValue=null";
    
    public static final String schooling_url="columnName=schooling_url,remarks=,dataType=String,defaultValue=null";
    
    public static final String is_heart="columnName=is_heart,remarks=,dataType=String,defaultValue=null";
    
    public static final String user_id="columnName=user_id,remarks=,dataType=int,defaultValue=null";
    
    public static final String heart_status="columnName=heart_status,remarks=,dataType=tinyint,defaultValue=null";
    
    public static final String edu_status="columnName=edu_status,remarks=,dataType=tinyint,defaultValue=null";

    public static final String house_status="columnName=house_status,remarks=,dataType=tinyint,defaultValue=null";
    
    public static final String create_date="columnName=state,remarks=,dataType=datetime,defaultValue=null";

    public static final String heart_reason="columnName=heart_reason,remarks=,dataType=String,defaultValue=null";

    public static final String edu_reason="columnName=edu_reason,remarks=,dataType=String,defaultValue=null";
    
    public static final String house_reason="columnName=house_reason,remarks=,dataType=String,defaultValue=null";
    
    

}
