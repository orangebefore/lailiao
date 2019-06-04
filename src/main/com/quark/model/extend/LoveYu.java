package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class LoveYu extends Model<LoveYu>{

    public static LoveYu dao = new LoveYu();

    public static final String love_yu_id="columnName=love_yu_id,remarks=,dataType=int,defaultValue=null";

    public static final String love_yu="columnName=love_yu,remarks=,dataType=String,defaultValue=null";

    public static final String post_time="columnName=post_time,remarks=,dataType=String,defaultValue=null";

    public static final String status="columnName=status,remarks=,dataType=int,defaultValue=1";

}
