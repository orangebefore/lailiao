package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class TongjiCharge extends Model<TongjiCharge>{

    public static TongjiCharge dao = new TongjiCharge();

    public static final String id="columnName=id,remarks=用户注册统计表ID,dataType=int,defaultValue=null";

    public static final String user_id="columnName=user_id,remarks=用户唯一标识,dataType=String,defaultValue=null";

    public static final String catalog="columnName=catalog,remarks=收入分类标识,dataType=String,defaultValue=";

    public static final String post_time="columnName=post_time,remarks=注册时间,dataType=String,defaultValue=null";

    public static final String charge_month="columnName=charge_month,remarks=注册月份：2016-01,dataType=String,defaultValue=null";

    public static final String charge_date="columnName=charge_date,remarks=注册日期：2016-01-01,dataType=String,defaultValue=null";

    public static final String charge_hour="columnName=charge_hour,remarks=注册时间：2016-01-01 10,dataType=String,defaultValue=null";

    public static final String money="columnName=money,remarks=充值金额,dataType=String,defaultValue=null";

}
