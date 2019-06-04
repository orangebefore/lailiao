package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class CellPhone extends Model<CellPhone>{

    public static CellPhone dao = new CellPhone();

    public static final String cell_phone_id="columnName=cell_phone_id,remarks=,dataType=int,defaultValue=null";

    public static final String user_id="columnName=user_id,remarks=,dataType=int,defaultValue=null";

    public static final String telephone="columnName=telephone,remarks=手机联系人的电话号码,dataType=String,defaultValue=null";

    public static final String post_time="columnName=post_time,remarks=,dataType=String,defaultValue=null";

}
