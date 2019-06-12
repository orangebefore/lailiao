package com.quark.model.extend;

import com.jfinal.plugin.activerecord.Model;

public class ChargeAudit extends Model<ChargeAudit>{
	
	public static ChargeAudit dao = new ChargeAudit();

    public static final String charge_audit_id="columnName=charge_audit_id,remarks=审核充值记录ID,dataType=int,defaultValue=null";

    public static final String user_id="columnName=user_id,remarks=用户ID,dataType=int,defaultValue=null";

    public static final String pay_id="columnName=pay_id,remarks=支付平台交易流水号,dataType=String,defaultValue=null";

    public static final String is_pay="columnName=is_pay,remarks=是否交易完成：0-未完成 ，1-已完成,dataType=int,defaultValue=0";

    public static final String charge_time="columnName=charge_time,remarks=充值日期,dataType=String,defaultValue=null";

    public static final String money="columnName=money,remarks=充值金额,dataType=String,defaultValue=0";

    public static final String charge_month="columnName=charge_month,remarks=充值月份：2016-01,dataType=String,defaultValue=";

    public static final String charge_date="columnName=charge_date,remarks=充值日期：206-09-01,dataType=String,defaultValue=null";

    public static final String charge_hour="columnName=charge_hour,remarks=充值小时：2016-01-09 05,dataType=String,defaultValue=";

    public static final String pay_type="columnName=pay_type,remarks=1-支付宝，2-微信，3-银联,dataType=int,defaultValue=null";
    
    public static final String aduit_type="columnName=aduit_type,remarks=0-汽车审核，1-房子审核,dataType=int,defaultValue=null";

}
