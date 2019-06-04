package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class UserShape extends Model<UserShape>{

    public static UserShape dao = new UserShape();

    public static final String user_shape_id="columnName=user_shape_id,remarks=用户体型集合ID,dataType=int,defaultValue=null";

    public static final String shape="columnName=shape,remarks=体型,dataType=String,defaultValue=null";

    public static final String sex="columnName=sex,remarks=0-女性，1-男性,dataType=int,defaultValue=null";

    public static final String sort="columnName=sort,remarks=排序，0-N，升序,dataType=int,defaultValue=0";

    public static final String post_time="columnName=post_time,remarks=日期,dataType=String,defaultValue=null";

}
