package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class Gift extends Model<Gift>{

    public static Gift dao = new Gift();

    public static final String gift_id="columnName=gift_id,remarks=,dataType=int,defaultValue=null";

    public static final String gift_name="columnName=gift_name,remarks=礼物名称,dataType=String,defaultValue=";

    public static final String gift_cover="columnName=gift_cover,remarks=礼物封面,dataType=String,defaultValue=";

    public static final String gold_value="columnName=gold_value,remarks=金币值,dataType=int,defaultValue=0";

    public static final String sort="columnName=sort,remarks=排序，小到大,dataType=int,defaultValue=0";

    public static final String post_time="columnName=post_time,remarks=,dataType=String,defaultValue=null";

}
