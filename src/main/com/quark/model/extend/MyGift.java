package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class MyGift extends Model<MyGift>{

    public static MyGift dao = new MyGift();

    public static final String my_gift_id="columnName=my_gift_id,remarks=,dataType=int,defaultValue=null";

    public static final String gift_id="columnName=gift_id,remarks=礼物表ID,dataType=int,defaultValue=0";

    public static final String my_gift_cover="columnName=my_gift_cover,remarks=钻石封面,dataType=String,defaultValue=";

    public static final String my_gift_name="columnName=my_gift_name,remarks=礼物名称,dataType=String,defaultValue=";

    public static final String my_gold_value="columnName=my_gold_value,remarks=金币值,dataType=int,defaultValue=0";

    public static final String send_user_id="columnName=send_user_id,remarks=赠送者,dataType=int,defaultValue=0";

    public static final String receiver_user_id="columnName=receiver_user_id,remarks=接收者,dataType=int,defaultValue=0";

    public static final String status="columnName=status,remarks=是否是赠送历史：1：现，0：是,dataType=int,defaultValue=1";

    public static final String post_time="columnName=post_time,remarks=,dataType=String,defaultValue=null";

    public static final String gift_count="columnName=gift_count,remarks=礼物数,dataType=int,defaultValue=1";

}
