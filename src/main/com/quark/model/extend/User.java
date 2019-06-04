package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class User extends Model<User>{

    public static User dao = new User();

    public static final String user_id="columnName=user_id,remarks=用户ID,dataType=int,defaultValue=null";

    public static final String telephone="columnName=telephone,remarks=手机号码,dataType=String,defaultValue=";

    public static final String password="columnName=password,remarks=密码,dataType=String,defaultValue=";

    public static final String nickname="columnName=nickname,remarks=昵称,dataType=String,defaultValue=";

    public static final String birthday="columnName=birthday,remarks=生日日期,dataType=String,defaultValue=1980-01-01";

    public static final String job="columnName=job,remarks=职业,dataType=String,defaultValue=";

    public static final String height="columnName=height,remarks=身高,dataType=String,defaultValue=165CM";

    public static final String sex="columnName=sex,remarks=性别：0-女，1-男,dataType=int,defaultValue=1";

    public static final String taste="columnName=taste,remarks=对谁有兴趣：0-女性，1-男性,dataType=int,defaultValue=0";

    public static final String image_01="columnName=image_01,remarks=用户头像,dataType=String,defaultValue=";

    public static final String image_02="columnName=image_02,remarks=用户第二张图片,dataType=String,defaultValue=";

    public static final String image_03="columnName=image_03,remarks=用户第三张图片,dataType=String,defaultValue=";

    public static final String image_04="columnName=image_04,remarks=用户第四张图片,dataType=String,defaultValue=";

    public static final String image_05="columnName=image_05,remarks=用户第五张图片,dataType=String,defaultValue=";

    public static final String image_06="columnName=image_06,remarks=用户第六张图片,dataType=String,defaultValue=";

    public static final String is_set_heart="columnName=is_set_heart,remarks=头像设置：1-是，0-否,dataType=int,defaultValue=0";

    public static final String setting_message="columnName=setting_message,remarks=消息提醒：新消息提醒【0-关，1-开】,dataType=int,defaultValue=1";

    public static final String setting_focus="columnName=setting_focus,remarks=消息提醒：关注提醒【0-关，1-开】,dataType=int,defaultValue=1";

    public static final String setting_voice="columnName=setting_voice,remarks=消息提醒：提醒声音【0-关，1-开】,dataType=int,defaultValue=1";

    public static final String setting_shock="columnName=setting_shock,remarks=震动提醒：提醒震动【0-关，1-开】,dataType=int,defaultValue=1";

    public static final String setting_emotion="columnName=setting_emotion,remarks=情感状态：开启状态【0-关，1-开】,dataType=int,defaultValue=1";

    public static final String setting_freedate="columnName=setting_freedate,remarks=加入附近无偿约会【0-关，1-开】,dataType=int,defaultValue=1";

    public static final String setting_telecontact="columnName=setting_telecontact,remarks=提醒：屏蔽手机联系人【0-关-1开】,dataType=int,defaultValue=1";

    public static final String is_vip="columnName=is_vip,remarks=会员级别：0-普通会员，1-高级会员,dataType=int,defaultValue=0";

    public static final String vip_from_datetime="columnName=vip_from_datetime,remarks=会员开始日期,dataType=String,defaultValue=null";

    public static final String vip_end_datetime="columnName=vip_end_datetime,remarks=会员结束日期,dataType=String,defaultValue=null";

    public static final String user_gold_value="columnName=user_gold_value,remarks=用户钻石值,dataType=int,defaultValue=0";

    public static final String heart="columnName=heart,remarks=个性签名,dataType=String,defaultValue=null";

    public static final String income="columnName=income,remarks=年收入,dataType=String,defaultValue=";

    public static final String province="columnName=province,remarks=省,dataType=String,defaultValue=";

    public static final String city="columnName=city,remarks=市,dataType=String,defaultValue=";

    public static final String latitude="columnName=latitude,remarks=维度,dataType=String,defaultValue=";

    public static final String longitude="columnName=longitude,remarks=经度,dataType=String,defaultValue=";

    public static final String edu="columnName=edu,remarks=学历：中专、大专、大学、硕士、博士及以上,dataType=String,defaultValue=";

    public static final String shape="columnName=shape,remarks=体型,dataType=String,defaultValue=";

    public static final String marry="columnName=marry,remarks=婚姻状态：从未结婚、已婚、离异,dataType=String,defaultValue=";

    public static final String drink="columnName=drink,remarks=饮酒：从不、有时、经常,dataType=String,defaultValue=";

    public static final String smoke="columnName=smoke,remarks=吸烟：从不、有时、经常,dataType=String,defaultValue=";

    public static final String last_login_time="columnName=last_login_time,remarks=最近登陆时间,dataType=String,defaultValue=null";

    public static final String hot="columnName=hot,remarks=热度,每被浏览一次热度加1,dataType=int,defaultValue=0";

    public static final String sweet="columnName=sweet,remarks=建立一次聊天，甜蜜度加1,dataType=int,defaultValue=0";

    public static final String regist_time="columnName=regist_time,remarks=注册日间,dataType=String,defaultValue=null";

    public static final String regist_date="columnName=regist_date,remarks=注册日期,dataType=String,defaultValue=null";

    public static final String regist_hour="columnName=regist_hour,remarks=注册时间：2016-09-12 10,dataType=String,defaultValue=null";

    public static final String status="columnName=status,remarks=0-冻结，1-正常,dataType=int,defaultValue=1";

    public static final String black_status="columnName=black_status,remarks=系统加入黑名单：0-不，1-黑,dataType=int,defaultValue=0";

    public static final String hope="columnName=hope,remarks=幸福期望：轻奢，高奢，中等,dataType=String,defaultValue=中等";
    
    public static final String is_car="columnName=is_car,remarks=汽车认证,dataType=int,defaultValue=0";

    public static final String is_card="columnName=is_card,remarks=身份认证状态,dataType=String,defaultValue=no";

    public static final String is_video="columnName=is_video,remarks=视频认证状态,dataType=String,defaultValue=no";

    public static final String is_edu="columnName=is_edu,remarks=学历认证状态,dataType=String,defaultValue=no";
    
    public static final String is_house="columnName=is_house,remarks=房产认证状态,dataType=String,defaultValue=no";
    

		
	
}
