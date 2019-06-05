package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class InvitationPrice extends Model<InvitationPrice>{

    public static InvitationPrice dao = new InvitationPrice();

    public static final String ip_id="columnName=ip_id,remarks=邀约置顶价格,dataType=int,defaultValue=null";

    public static final String price="columnName=price,remarks=价格,dataType=String,defaultValue=null";

    public static final String product_id="columnName=product_id,remarks=苹果支付识别产品,dataType=String,defaultValue=";

    public static final String post_time="columnName=post_time,remarks=修改时间,dataType=String,defaultValue=null";

}
