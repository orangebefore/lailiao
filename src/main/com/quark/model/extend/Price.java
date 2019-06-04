package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class Price extends Model<Price>{

    public static Price dao = new Price();

    public static final String price_id="columnName=price_id,remarks=会员价格ID,dataType=int,defaultValue=null";

    public static final String days="columnName=days,remarks=使用天数,dataType=int,defaultValue=null";

    public static final String price="columnName=price,remarks=价格,dataType=String,defaultValue=null";

    public static final String sort="columnName=sort,remarks=排序：0-N，升序,dataType=int,defaultValue=null";

    public static final String product_id="columnName=product_id,remarks=苹果支付识别产品,dataType=String,defaultValue=";

    public static final String post_time="columnName=post_time,remarks=修改时间,dataType=String,defaultValue=null";

}
