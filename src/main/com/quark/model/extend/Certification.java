package com.quark.model.extend;

import com.jfinal.plugin.activerecord.Model;

public class Certification extends Model<Certification>{
	
	public static Certification dao = new Certification();

    public static final String id="columnName=id,remarks=认证ID,dataType=int,defaultValue=null";

    public static final String drivers="columnName=drivers,remarks=驾驶证,dataType=String,defaultValue=null";

    public static final String id_card_up="columnName=id_card_up,remarks=身份证正,dataType=String,defaultValue=null";

    public static final String id_card_down="columnName=id_card_down,remarks=身份证反证正,dataType=String,defaultValue=null";

    public static final String video_url="columnName=video_url,remarks=视频,dataType=String,defaultValue=null";

    public static final String house_city="columnName=house_city,remarks=房产城市,dataType=String,defaultValue=null";

    public static final String house_url="columnName=house_url,remarks=房产证,dataType=String,defaultValue=null";

    public static final String edu_url="columnName=edu_url,remarks=学历证,dataType=String,defaultValue=null";

    public static final String is_heart="columnName=is_heart,remarks=个性签名,dataType=String,defaultValue=null";

    public static final String user_id="columnName=user_id,remarks=认证用户Id,dataType=int,defaultValue=null";

    public static final String id_card_status="columnName=id_card_status,remarks=身份证审核状态：0.待审核 1.审核已通过 2.审核未通过,dataType=int,defaultValue=null";

    public static final String video_status="columnName=video_status,remarks=视频审核状态：0.待审核 1.审核已通过 2.审核未通过,dataType=int,defaultValue=null";

    public static final String id_card_reason="columnName=id_card_reason,remarks=身份审核未通过理由,dataType=String,defaultValue=null";

    public static final String video_reason="columnName=video_reason,remarks=视频审核未通过理由,dataType=String,defaultValue=null";

    public static final String car_status="columnName=car_status,remarks=汽车认证状态:0待审核1审核通过2未通过审核,dataType=int,defaultValue=0";

}
