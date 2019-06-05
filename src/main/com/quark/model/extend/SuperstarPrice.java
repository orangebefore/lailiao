package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class SuperstarPrice extends Model<SuperstarPrice>{

    public static SuperstarPrice dao = new SuperstarPrice();

    public static final String sp_id="columnName=sp_id,remarks=超级明星价格ID,dataType=int,defaultValue=null";

    public static final String hours="columnName=hours,remarks=使用时间(小时),dataType=int,defaultValue=null";

    public static final String price="columnName=price,remarks=价格,dataType=String,defaultValue=null";

    public static final String product_id="columnName=product_id,remarks=苹果支付识别产品,dataType=String,defaultValue=";

    public static final String post_time="columnName=post_time,remarks=修改时间,dataType=String,defaultValue=null";

}
