package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class Tokens extends Model<Tokens>{

    public static Tokens dao = new Tokens();

    public static final String token_id="columnName=token_id,remarks=,dataType=int,defaultValue=null";

    public static final String user_id="columnName=user_id,remarks=用户ID,dataType=int,defaultValue=null";

    public static final String token="columnName=token,remarks=token,dataType=String,defaultValue=";

    public static final String post_time="columnName=post_time,remarks=登陆日期,dataType=String,defaultValue=null";

    public static final String post_date="columnName=post_date,remarks=请求日期：2016-01-06,dataType=String,defaultValue=null";

    public static final String post_hour="columnName=post_hour,remarks=请求时间：2016-01-06 12,dataType=String,defaultValue=null";

}
