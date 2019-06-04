package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class Browse extends Model<Browse>{

    public static Browse dao = new Browse();

    public static final String browse_id="columnName=browse_id,remarks=浏览记录表ID,dataType=int,defaultValue=null";

    public static final String user_id="columnName=user_id,remarks=用户ID,dataType=int,defaultValue=null";

    public static final String browse_user_id="columnName=browse_user_id,remarks=被浏览用户ID,dataType=int,defaultValue=null";

    public static final String post_time="columnName=post_time,remarks=浏览时间,dataType=String,defaultValue=null";

    public static final String post_date="columnName=post_date,remarks=浏览日期：2015-09-01,dataType=String,defaultValue=null";

}
