package com.quark.model.extend;

import com.jfinal.plugin.activerecord.Model;

public class Invite extends Model<Invite>{
	

	public static Invite dao = new Invite();

    public static final String invite_id="columnName=invite_id,remarks=邀约表ID,dataType=int,defaultValue=null";

    public static final String invite_type_id="columnName=invite_type_id,remarks=邀约类型id,dataType=int,defaultValue=null";

    public static final String invite_place="columnName=invite_place,remarks=邀约地点（自填）,dataType=String,defaultValue=null";

    public static final String invite_province="columnName=invite_province,remarks=省,dataType=String,defaultValue=null";
    
    public static final String invite_city="columnName=invite_city,remarks=市,dataType=String,defaultValue=null";

    public static final String invite_content="columnName=invite_content,remarks=邀约内容,dataType=String,defaultValue=null";
    
    public static final String time_id="columnName=time_id,remarks=邀约时间id,dataType=int,defaultValue=null";

    public static final String invite_sex="columnName=invite_sex,remarks=邀约性别 0-男 1-女 2-不限,dataType=int,defaultValue=null";
    
    public static final String cost_id="columnName=cost_id,remarks=邀约费用id,dataType=int,defaultValue=null";

    public static final String invite_receive="columnName=invite_receive,remarks=是否由我接送 0-是 1-不是,dataType=int,defaultValue=null";

    public static final String invite_explain="columnName=invite_explain,remarks=邀约说明,dataType=String,defaultValue=null";

    public static final String explain_url="columnName=explain_url,remarks=说明图片,dataType=String,defaultValue=null";

    public static final String travel_mode_id="columnName=travel_mode_id,remarks=出行方式id,dataType=int,defaultValue=null";

    public static final String travel_days_id="columnName=travel_days_id,remarks=旅行天数id,dataType=int,defaultValue=null";
    
    public static final String is_equal_place="columnName=is_equal_place,remarks=有相同目的地的异性是否通知我,dataType=int,defaultValue=null";

    public static final String is_carry_bestie="columnName=is_carry_bestie,remarks=是否可以携带闺蜜,dataType=int,defaultValue=null";

    public static final String user_id="columnName=user_id,remarks=用户id,dataType=int,defaultValue=null";
    
    public static final String is_top="columnName=is_top,remarks=是否置顶,dataType=int,defaultValue=null";
    
    public static final String top_date="columnName=top_date,remarks=置顶时间,dataType=String,defaultValue=null";



}
