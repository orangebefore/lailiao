package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class TongjiRegist extends Model<TongjiRegist>{

    public static TongjiRegist dao = new TongjiRegist();

    public static final String id="columnName=id,remarks=用户注册统计表ID,dataType=int,defaultValue=null";

    public static final String user_id="columnName=user_id,remarks=用户唯一标识,dataType=String,defaultValue=null";

    public static final String catalog="columnName=catalog,remarks=用户分类标识,dataType=String,defaultValue=";

    public static final String post_time="columnName=post_time,remarks=注册时间,dataType=String,defaultValue=null";

    public static final String regist_month="columnName=regist_month,remarks=注册月份：2016-01,dataType=String,defaultValue=null";

    public static final String regist_date="columnName=regist_date,remarks=注册日期：2016-01-01,dataType=String,defaultValue=null";

    public static final String regist_hour="columnName=regist_hour,remarks=注册时间：2016-01-01 10,dataType=String,defaultValue=null";

    public static final String province="columnName=province,remarks=省,dataType=String,defaultValue=";

    public static final String city="columnName=city,remarks=城市,dataType=String,defaultValue=";

}
