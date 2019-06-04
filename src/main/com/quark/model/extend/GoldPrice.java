package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class GoldPrice extends Model<GoldPrice>{

    public static GoldPrice dao = new GoldPrice();

    public static final String gold_price_id="columnName=gold_price_id,remarks=,dataType=int,defaultValue=null";

    public static final String gold_value="columnName=gold_value,remarks=钻石值,dataType=int,defaultValue=0";

    public static final String gold_price="columnName=gold_price,remarks=钻石价格,dataType=String,defaultValue=null";

    public static final String sort="columnName=sort,remarks=排序：0-N，升序,dataType=int,defaultValue=null";

    public static final String product_id="columnName=product_id,remarks=苹果支付识别产品,dataType=String,defaultValue=";

    public static final String post_time="columnName=post_time,remarks=修改时间,dataType=String,defaultValue=null";

}
