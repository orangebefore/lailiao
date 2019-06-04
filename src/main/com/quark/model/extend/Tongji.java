package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class Tongji extends Model<Tongji>{

    public static Tongji dao = new Tongji();

    public static final String id="columnName=id,remarks=,dataType=int,defaultValue=null";

    public static final String token="columnName=token,remarks=商户访问token,dataType=String,defaultValue=null";

    public static final String user_id="columnName=user_id,remarks=app 用户id,dataType=String,defaultValue=null";

    public static final String access_time="columnName=access_time,remarks=访问时间,dataType=String,defaultValue=null";

    public static final String device="columnName=device,remarks=设备:安卓、苹果,dataType=String,defaultValue=null";

    public static final String device_version="columnName=device_version,remarks=设备版本号,dataType=String,defaultValue=null";

    public static final String city="columnName=city,remarks=访问区域：深圳、上海,dataType=String,defaultValue=null";

    public static final String rp="columnName=rp,remarks=对应的rp页面,dataType=String,defaultValue=null";

    public static final String api="columnName=api,remarks=请求接口,dataType=String,defaultValue=null";

    public static final String ip="columnName=ip,remarks=访问ip,dataType=String,defaultValue=null";

    public static final String resolution="columnName=resolution,remarks=设备分辨率,dataType=String,defaultValue=null";

    public static final String language="columnName=language,remarks=设备语言,dataType=String,defaultValue=null";

}
