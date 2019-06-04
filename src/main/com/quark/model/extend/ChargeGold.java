package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class ChargeGold extends Model<ChargeGold>{

    public static ChargeGold dao = new ChargeGold();

    public static final String charge_gold_id="columnName=charge_gold_id,remarks=充值记录ID,dataType=int,defaultValue=null";

    public static final String user_id="columnName=user_id,remarks=用户ID,dataType=int,defaultValue=null";

    public static final String gold_value="columnName=gold_value,remarks=钻石值,dataType=int,defaultValue=0";

    public static final String pay_id="columnName=pay_id,remarks=支付平台交易流水号,dataType=String,defaultValue=null";

    public static final String is_pay="columnName=is_pay,remarks=是否交易完成：0-未完成 ，1-已完成,dataType=int,defaultValue=0";

    public static final String charge_time="columnName=charge_time,remarks=充值日期,dataType=String,defaultValue=null";

    public static final String money="columnName=money,remarks=充值金额,dataType=String,defaultValue=0";

    public static final String charge_month="columnName=charge_month,remarks=充值月份：2016-01,dataType=String,defaultValue=";

    public static final String type="columnName=type,remarks=用户类型：1-甜心大哥，0-甜心宝贝,dataType=int,defaultValue=null";

    public static final String charge_date="columnName=charge_date,remarks=充值日期：206-09-01,dataType=String,defaultValue=null";

    public static final String charge_hour="columnName=charge_hour,remarks=充值小时：2016-01-09 05,dataType=String,defaultValue=";

    public static final String pay_type="columnName=pay_type,remarks=1-支付宝，2-微信，3-银联,dataType=int,defaultValue=null";

}
