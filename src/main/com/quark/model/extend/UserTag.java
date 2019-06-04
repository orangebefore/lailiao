package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class UserTag extends Model<UserTag>{

    public static UserTag dao = new UserTag();

    public static final String user_tag_id="columnName=user_tag_id,remarks=,dataType=int,defaultValue=null";

    public static final String user_id="columnName=user_id,remarks=,dataType=int,defaultValue=null";

    public static final String tag="columnName=tag,remarks=标签名称,dataType=String,defaultValue=null";

    public static final String status="columnName=status,remarks=标签状态：0-删除，1-显示,dataType=int,defaultValue=1";

    public static final String post_time="columnName=post_time,remarks=修改时间,dataType=String,defaultValue=null";

    public static final String type="columnName=type,remarks=标签属性：1-甜心大哥，0-甜心宝贝,dataType=int,defaultValue=null";

}
