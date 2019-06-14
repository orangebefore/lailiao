define({ "api": [
  {
    "type": "get",
    "url": "/app/Home/search",
    "title": "用户筛选",
    "description": "<p>筛选后普通用户和超级明星的展示</p>",
    "group": "Home",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "distance",
            "description": "<p>按距离，默认则不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "last_login_time",
            "description": "<p>按活跃时间，默认则不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "sex",
            "description": "<p>性别，默认则不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "city",
            "description": "<p>城市，默认则不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "is_video",
            "description": "<p>视频认证，默认则不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "weight_from",
            "description": "<p>从-体重，默认则不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "weight_to",
            "description": "<p>到-体重，默认则不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "age_from",
            "description": "<p>从年龄，默认则不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "age_to",
            "description": "<p>到年龄，默认则不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "height_from",
            "description": "<p>从-身高，默认则不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "height_to",
            "description": "<p>到-身高，默认则不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "edu",
            "description": "<p>学历，默认则不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "shape",
            "description": "<p>体型，默认则不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "part",
            "description": "<p>满意部位，,默认则不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "latitude",
            "description": "<p>用户经度</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "longitude",
            "description": "<p>用户纬度</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n  \"SearchSweetsResponse\": {\n        \"searchResult\": {\n            \"topSweets\": {\t//超级明星列表\n                \"totalRow\": 2,\n                \"pageNumber\": 1,\n                \"totalPage\": 1,\n                \"pageSize\": 10,\n                \"list\": [\n                    {\n                        \"birthday\": \"1989-01-01\",\t//生日\n                        \"last_login_time\": \"2019-06-14 09:49:16\",\t//最后登陆时间\n                        \"distance\": 0,\t//距离米\n                        \"city\": null,\t//城市\n                        \"image_01\": \"1455965571482.jpg\",\t//头像\n                        \"is_vip\": 0,\t//是否vip\n                        \"sex\": 0,\t//性别\n                        \"regist_date\": \"2016-02-20\",\t//注册时间\n                        \"telephone\": \"18503058915\",\t\t//手机号\n                        \"heart\": null,\t//个性签名\n                        \"user_id\": 34,\t//用户id\n                        \"nickname\": \"xiaofang\",\t//昵称\n                        \"endTime\": \"2019-06-16\",\t//超级明星到期时间\n                        \"is_star\": 1,\t//是否超级明星\n                        \"job\": \"高管\",\t\t//职业\n                        \"age\": \"30岁\",\t//年龄\n                        \"height\": \"180CM以上\"\t\t//身高\n                    }\n                ]\n            },\n            \"searchSweets\": {\t//搜索普通用户id\n                \"totalRow\": 963,\n                \"pageNumber\": 1,\n                \"totalPage\": 97,\n                \"pageSize\": 10,\n                \"list\": [\n                    {\n                        \"birthday\": \"1995-07-16\",\t//生日\n                        \"distance\": 12864421,\t//距离米\n                        \"user_id\": 256,\t//用户id\n                        \"city\": null,\t//城市\n                        \"is_vip\": 0,\t//是否vip\n                        \"image_01\": \"\",\t//头像\n                        \"sex\": 1,\t//性别\n                        \"nickname\": \"喧哗华丽落幕\",\t//昵称\n                        \"telephone\": \"13978123514\",\t\t//手机号码\n                        \"job\": \"职场白领\",\t//职业\n                        \"age\": \"24岁\"\t//年龄\n                    }\n                ]\n            }\n        },\n        \"code\": 200,\n        \"message\": \"返回成功\",\n        \"status\": 1\n    }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/Home.java",
    "groupTitle": "Home",
    "name": "GetAppHomeSearch"
  },
  {
    "type": "get",
    "url": "/app/Home/sweets",
    "title": "首页显示",
    "description": "<p>首页普通用户和超级明星的展示</p>",
    "group": "Home",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "type",
            "description": "<p>1-附近，2-线上，3-新人</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n \"SweetsResponse\": {\n        \"code\": 200,\n        \"message\": \"返回成功\",\n        \"sweetsResult\": {\n            \"topSweets\": [     //超级明星列表\n                {\n                    \"birthday\": \"1989-01-01\",\t\t//生日\n                    \"last_login_time\": \"2019-06-14 09:49:16\",\t//最后登录时间\n                    \"distance\": 0,\t//距离\n                    \"city\": null,\t//城市\n                    \"image_01\": \"1455965571482.jpg\",\t//头像1\n                    \"is_vip\": 0,\t//是否VIP 0-不是，1-是\n                    \"sex\": 0,\t//性别：0-女，1-男\n                    \"regist_date\": \"2016-02-20\",\t//注册日期\n                    \"telephone\": \"18503058915\",\t//手机号码\n                    \"loved_count\": 2,\t//被爱人数\n                    \"heart\": null,\t\t//个性签名\n                    \"active_time\": \"0公里\",\t//距离公里\n                    \"user_id\": 34,\t//用户id\n                    \"is_like\": 0,\t//是否喜欢\n                    \"nickname\": \"xiaofang\",\t//昵称\n                    \"endTime\": \"2019-06-16\",\t//超级明星结束时间\n                    \"is_star\": 1,\t//是否超级明星0-不是,1-是\n                    \"job\": \"高管\",\t\t//职业\n                    \"age\": \"30岁\",\t//年龄\n                    \"height\": \"180CM以上\"\t//身高\n                }\n            ],\n            \"sweets\": {\n                \"totalRow\": 186,\n                \"pageNumber\": 1,\n                \"totalPage\": 38,\n                \"pageSize\": 5,\n                \"list\": [\n                    {\n                        \"birthday\": \"1983-01-01\", //生日\n                        \"last_login_time\": \"2019-06-06 17:25:38\", //最后登录时间\n                        \"distance\": 2315181, //距离\n                        \"city\": \"广州市\"，                //城市\n                        \"image_01\": \"1456109546949.jpg\",\t//头像1\n                        \"is_vip\": 0,\t//是否VIP 0-不是，1-是\n                        \"sex\": 1,\t\t//性别：0-女，1-男\n                        \"regist_date\": \"2016-02-20\", //注册日期\n                        \"telephone\": \"18503058914\",\t//手机号码\n                        \"loved_count\": 0,\t//被爱人数\n                        \"heart\": \"摩羯里\",\t//个性签名\n                        \"active_time\": \"2316公里\",  //距离公里\n                        \"user_id\": 33,\t\t//用户id\n                        \"is_like\": 0,\t//是否喜欢\n                        \"nickname\": \"xiaoguan\",\t//昵称\n                        \"job\": \"IT\",\t//职业\n                        \"age\": \"36岁\",\t//年龄\n                        \"height\": \"180CM以上\"\t\t//身高\n                    }\n                ]\n            }\n        },\n        \"status\": 1\n    }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/Home.java",
    "groupTitle": "Home",
    "name": "GetAppHomeSweets"
  },
  {
    "type": "get",
    "url": "/app/InviteController/details",
    "title": "邀约详情",
    "group": "InviteController",
    "description": "<p>邀约详情接口</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "invite_id",
            "description": "<p>邀约id.</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n    \"InviteResponse\": {\n        \"code\": 200,\t//除了200均为错误，405需要重新登录\n        \"detailsResult\": {\n            \"detailsList\": [\n                {\n                    \"invite_content\": \"唱跳rap篮球\",\t//邀约内容\n                    \"type_name\": \"电影\",\t//类型名称\n                    \"invite_explain\": \"i want to play basketball\",\t//邀约说明\n                    \"distance\": 2315181,\t//距离\n                    \"travel_mode_name\": \"自驾\",\t//出行方式\n                    \"hot\": 26,\t//热度\n                    \"travel_mode_id\": 3,\t//出行方式id\n                    \"travel_days_id\": 4,\t//类型id\n                    \"time_name\": \"任何休息日\",\t//邀约时间\n                    \"invite_province\": \"云南\",\t//邀约省份\n                    \"travel_days_name\": \"预计3~5天\",\t//出行时间\n                    \"nickname\": \"xiaoguan\",\t//用户昵称\n                    \"cost_name\": \"我买单\",\t//费用类型\n                    \"explain_url\": \"1560222965884.jpg\",\t//说明图片\n                    \"invite_sex\": 0,\t//邀约性别 0-男 1-女 2-不限\n                    \"invite_receive\": 1,\t//是否由我接送 0-是 1-不是\n                    \"invite_id\": 1,\t\n                    \"time_id\": 2,\t//邀约时间id\n                    \"image_01\": \"1456109546949.jpg\",\t//用户头像\n                    \"is_top\": 1,\t//是否置顶\n                    \"cost_id\": 1,\t//邀约费用id\n                    \"invite_place\": \"滇池路\",\t//邀约地点（自填）\n                    \"user_id\": 33,\t//用户id\n                    \"invite_city\": \"昆明\",\t//邀约城市\n                    \"invite_type_id\": 4,\t//邀约类型id\n                    \"top_date\": \"2019-06-14 11:49:13\",\t//置顶时间\n                    \"age\": 36,\t//用户年龄\n                    \"is_equal_place\": 0,\t//有相同目的地的异性是否通知我 0-是 1-否\n                    \"is_carry_bestie\": 0\t//是否可以携带闺蜜 0-是 1-否\n                }\n            ]\n        }\n    }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/InviteController.java",
    "groupTitle": "InviteController",
    "name": "GetAppInvitecontrollerDetails"
  },
  {
    "type": "get",
    "url": "/app/InviteController/list",
    "title": "邀约显示",
    "description": "<p>邀约首页的显示和邀约分类显示</p>",
    "group": "InviteController",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "latitude",
            "defaultValue": "30.344",
            "description": "<p>用户的纬度</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "longitude",
            "defaultValue": "120.00",
            "description": "<p>用户的经度</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": true,
            "field": "invite_type_id",
            "description": "<p>邀约的类型id：1-旅游，2-美食，3-唱歌，4-电影，5-运动，6-文艺，不填查询全部</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n \"InviteResponse\": {\n     \"listResult\": {\n         \"topList\": [\n         {\n             \"invite_content\": \"唱跳rap篮球\", \t//邀约内容\n             \"invite_explain\": \"i want to play basketball\",\t//邀约说明\n             \"distance\": 12188067,\t//邀约发起人与用户的距离（米）\n             \"city\": null,\t//邀约城市\n             \"image_01\": \"\",\t//邀约图片\n             \"sex\": 0,\t//邀约对象性别：0-男，1-女，2-不限\n             \"is_top\": 1,\t//邀约是否置顶：1-是，0-否\n             \"cost_id\": 1,\t//邀约费用id：1-我买单，2-AA制，3-来回机票我承担，4-期望对方承担\n             \"nickname\": \"兰兰\",\t//邀约发起人昵称\n             \"invite_type_id\": 5,\t//邀约类型id：1-旅游，2-美食，3-唱歌，4-电影，5-运动，6-文艺\n             \"job\": \"助理\",\t//邀约发起人工作\n             \"age\": 28,\t//邀约发起人年龄\n             \"height\": \"180CM及以上\"\t//邀约发起人身高\n         }\n         ],\n         \"list\": [\n             {\n                 \"invite_content\": \"唱跳rap篮球\",\t//邀约内容\n                 \"invite_explain\": \"i want to play basketball\",\t//邀约说明\n                 \"distance\": 8911148,\t//邀约发起人与用户的距离（米）\n                 \"city\": \"广州市\",\t//邀约城市\n                 \"image_01\": \"1456109546949.jpg\",\t//邀约图片\n                 \"sex\": 1,\t//邀约对象性别：0-男，1-女，2-不限\n                 \"is_top\": 0,\t//邀约是否置顶：1-是，0-否\n                 \"cost_id\": 1,\t//邀约费用id：1-我买单，2-AA制，3-来回机票我承担，4-期望对方承担\n                 \"nickname\": \"xiaoguan\",\t//邀约发起人昵称\n                 \"invite_type_id\": 4,\t//邀约类型id：1-旅游，2-美食，3-唱歌，4-电影，5-运动，6-文艺\n                 \"job\": \"IT\",\t//邀约发起人工作\n                 \"age\": 36,\t//邀约发起人年龄\n                 \"height\": \"180CM以上\"\t//邀约发起人身高\n             }\n         ]\n     },\n     \"code\": 200,\t//除了200均为错误，405需要重新登录\n     \"message\": \"\",\t//返回的信息\n     \"status\": 1\t//状态为1为成功，0为失败\n }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/InviteController.java",
    "groupTitle": "InviteController",
    "name": "GetAppInvitecontrollerList"
  },
  {
    "type": "get",
    "url": "/app/InviteController/screenList",
    "title": "邀约筛选",
    "group": "InviteController",
    "description": "<p>邀约筛选查询</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "sex",
            "description": "<p>筛选邀约发起人的性别：0-女，1-男，2-全部.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "sort",
            "description": "<p>筛选方式：new-最新发布，place-离我最近</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "latitude",
            "defaultValue": "30.344",
            "description": "<p>用户的纬度</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "longitude",
            "defaultValue": "120.00",
            "description": "<p>用户的经度</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "province",
            "description": "<p>筛选省</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "is_video",
            "description": "<p>是否筛选视频认证：0-关闭视频认证筛选，1-开启视频认证筛选</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n \"InviteResponse\": {\n     \"listResult\": {\n         \"topList\": [\n         {\n             \"invite_content\": \"唱跳rap篮球\", \t//邀约内容\n             \"invite_explain\": \"i want to play basketball\",\t//邀约说明\n             \"distance\": 12188067,\t//邀约发起人与用户的距离\n             \"city\": null,\t//邀约城市\n             \"image_01\": \"\",\t//邀约图片\n             \"sex\": 0,\t//邀约对象性别，0-男：1-女，2-不限\n             \"is_top\": 1,\t//邀约是否置顶：1-是，0-否\n             \"cost_id\": 1,\t//邀约费用id：1-我买单，2-AA制，3-来回机票我承担，4-期望对方承担\n             \"nickname\": \"兰兰\",\t//邀约发起人昵称\n             \"invite_type_id\": 5,\t//邀约类型id：1-旅游，2-美食，3-唱歌，4-电影，5-运动，6-文艺\n             \"job\": \"助理\",\t//邀约发起人工作\n             \"age\": 28,\t//邀约发起人年龄\n             \"height\": \"180CM及以上\"\t//邀约发起人身高\n         }\n         ],\n         \"list\": [\n             {\n                 \"invite_content\": \"唱跳rap篮球\",\t//邀约内容\n                 \"invite_explain\": \"i want to play basketball\",\t//邀约说明\n                 \"distance\": 8911148,\t//邀约发起人与用户的距离\n                 \"city\": \"广州市\",\t//邀约城市\n                 \"image_01\": \"1456109546949.jpg\",\t//邀约图片\n                 \"sex\": 1,\t//邀约对象性别：0-男，1-女，2-不限\n                 \"is_top\": 0,\t//邀约是否置顶：1-是，0-否\n                 \"cost_id\": 1,\t//邀约费用id：1-我买单，2-AA制，3-来回机票我承担，4-期望对方承担\n                 \"nickname\": \"xiaoguan\",\t//邀约发起人昵称\n                 \"invite_type_id\": 4,\t//邀约类型id：1-旅游，2-美食，3-唱歌，4-电影，5-运动，6-文艺\n                 \"job\": \"IT\",\t//邀约发起人工作\n                 \"age\": 36,\t//邀约发起人年龄\n                 \"height\": \"180CM以上\"\t//邀约发起人身高\n             }\n         ]\n     },\n     \"code\": 200,\t//除了200均为错误，405需要重新登录\n     \"message\": \"\",\t//返回的信息\n     \"status\": 1\t//状态为1为成功，0为失败\n }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/InviteController.java",
    "groupTitle": "InviteController",
    "name": "GetAppInvitecontrollerScreenlist"
  },
  {
    "type": "post",
    "url": "/app/InviteController/findData",
    "title": "加载选项数据",
    "group": "InviteController",
    "description": "<p>添加邀约时需要的选项数据</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_type_id",
            "description": "<p>邀约的类型id：1-旅游，2-美食，3-唱歌，4-电影，5-运动，6-文艺</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n    \"InviteResponse\": {\n        \"code\": 200,\n        \"List\": {\n            \"daysList\": [\t//旅行天数\n                {\n                    \"travel_days_id\": 1,\n                    \"travel_days_name\": \"当天往返\"\n                },...\n            ],\n            \"timeList\": [\t//邀约时间\n                {\n                    \"time_name\": \"双方商议具体时间\"\n                },...\n            ],\n            \"costList\": [\t//付款方式\n                {\n                    \"cost_id\": 1,\n                    \"cost_name\": \"我买单\"\n                },...\n            ],\n            \"modeList\": [\t//旅游出行方式\n                {\n                    \"travel_mode_name\": \"往返坐飞机\",\n                    \"travel_mode_id\": 1\n                },...\n            ]\n        }\n    }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/InviteController.java",
    "groupTitle": "InviteController",
    "name": "PostAppInvitecontrollerFinddata"
  },
  {
    "type": "post",
    "url": "/app/InviteController/payTop",
    "title": "购买邀约置顶",
    "group": "InviteController",
    "description": "<p>购买邀约置顶接口</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n\t\"InviteResponse\": {\n\t\t\"code\": 400,\t\t//除了200均为错误，405需要重新登录\n\t\t\"message\": \"置顶成功\",\t//返回的信息\n\t\t\"status\": 1\t\t//状态为1为成功，0为失败\n\t}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/InviteController.java",
    "groupTitle": "InviteController",
    "name": "PostAppInvitecontrollerPaytop"
  },
  {
    "type": "post",
    "url": "/app/InviteController/saveFood",
    "title": "添加美食邀约",
    "group": "InviteController",
    "description": "<p>用户添加美食邀约</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "explain_url",
            "description": "<p>邀约说明图片</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "cost_id",
            "description": "<p>邀约费用id：1-我买单，2-AA制</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "invite_sex",
            "description": "<p>邀约对象性别：0-男，1-女，2-不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_province",
            "description": "<p>邀约省</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_city",
            "description": "<p>邀约市</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_place",
            "description": "<p>邀约地点</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_content",
            "description": "<p>邀约食物</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_explain",
            "description": "<p>邀约说明</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "invite_receive",
            "description": "<p>是否由我接送 0-是 1-不是</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "time_id",
            "description": "<p>邀约时间：1-不限时间，2-任何休息日，3-不常周末，4-双方商议具体时间</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n    \"InviteResponse\": {\n        \"code\": 200,\t//除了200均为错误，405需要重新登录\n        \"message\": \"发布成功\",\t///返回的信息\n        \"status\": 1\t//状态为1为成功，0为失败\n    }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/InviteController.java",
    "groupTitle": "InviteController",
    "name": "PostAppInvitecontrollerSavefood"
  },
  {
    "type": "post",
    "url": "/app/InviteController/saveLiterature",
    "title": "添加文艺邀约",
    "group": "InviteController",
    "description": "<p>用户添加文艺邀约</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "explain_url",
            "description": "<p>邀约说明图片</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "cost_id",
            "description": "<p>邀约费用id：1-我买单，2-AA制</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "invite_sex",
            "description": "<p>邀约对象性别：0-男，1-女，2-不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_province",
            "description": "<p>邀约省</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_city",
            "description": "<p>邀约市</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_place",
            "description": "<p>邀约地点</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_content",
            "description": "<p>邀约文艺</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_explain",
            "description": "<p>邀约说明</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "invite_receive",
            "description": "<p>是否由我接送 0-是 1-不是</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "is_carry_bestie",
            "description": "<p>是否可以携带闺蜜 0-是 1-否</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "time_id",
            "description": "<p>邀约时间：1-不限时间，2-任何休息日，3-不常周末，4-双方商议具体时间</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n    \"InviteResponse\": {\n        \"code\": 200,\t//除了200均为错误，405需要重新登录\n        \"message\": \"发布成功\",\t///返回的信息\n        \"status\": 1\t//状态为1为成功，0为失败\n    }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/InviteController.java",
    "groupTitle": "InviteController",
    "name": "PostAppInvitecontrollerSaveliterature"
  },
  {
    "type": "post",
    "url": "/app/InviteController/saveMotion",
    "title": "添加运动邀约",
    "group": "InviteController",
    "description": "<p>用户添加运动邀约</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "explain_url",
            "description": "<p>邀约说明图片</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "cost_id",
            "description": "<p>邀约费用id：1-我买单，2-AA制</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "invite_sex",
            "description": "<p>邀约对象性别：0-男，1-女，2-不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_province",
            "description": "<p>邀约省</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_city",
            "description": "<p>邀约市</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_place",
            "description": "<p>邀约地点</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_content",
            "description": "<p>邀约运动</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_explain",
            "description": "<p>邀约说明</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "invite_receive",
            "description": "<p>是否由我接送 0-是 1-不是</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "is_carry_bestie",
            "description": "<p>是否可以携带闺蜜 0-是 1-否</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "time_id",
            "description": "<p>邀约时间：1-不限时间，2-任何休息日，3-不常周末，4-双方商议具体时间</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n    \"InviteResponse\": {\n        \"code\": 200,\t//除了200均为错误，405需要重新登录\n        \"message\": \"发布成功\",\t///返回的信息\n        \"status\": 1\t//状态为1为成功，0为失败\n    }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/InviteController.java",
    "groupTitle": "InviteController",
    "name": "PostAppInvitecontrollerSavemotion"
  },
  {
    "type": "post",
    "url": "/app/InviteController/saveMovie",
    "title": "添加电影邀约",
    "group": "InviteController",
    "description": "<p>用户添加电影邀约</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "explain_url",
            "description": "<p>邀约说明图片</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "cost_id",
            "description": "<p>邀约费用id：1-我买单，2-AA制</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "invite_sex",
            "description": "<p>邀约对象性别：0-男，1-女，2-不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_province",
            "description": "<p>邀约省</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_city",
            "description": "<p>邀约市</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_place",
            "description": "<p>邀约地点</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_content",
            "description": "<p>邀约电影</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_explain",
            "description": "<p>邀约说明</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "invite_receive",
            "description": "<p>是否由我接送 0-是 1-不是</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "time_id",
            "description": "<p>邀约时间：1-不限时间，2-任何休息日，3-不常周末，4-双方商议具体时间</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n    \"InviteResponse\": {\n        \"code\": 200,\t//除了200均为错误，405需要重新登录\n        \"message\": \"发布成功\",\t///返回的信息\n        \"status\": 1\t//状态为1为成功，0为失败\n    }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/InviteController.java",
    "groupTitle": "InviteController",
    "name": "PostAppInvitecontrollerSavemovie"
  },
  {
    "type": "post",
    "url": "/app/InviteController/saveSing",
    "title": "添加唱歌邀约",
    "group": "InviteController",
    "description": "<p>用户添加唱歌邀约</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "explain_url",
            "description": "<p>邀约说明图片</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "cost_id",
            "description": "<p>邀约费用id：1-我买单，2-AA制</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "invite_sex",
            "description": "<p>邀约对象性别：0-男，1-女，2-不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_province",
            "description": "<p>邀约省</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_city",
            "description": "<p>邀约市</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_place",
            "description": "<p>邀约地点</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_explain",
            "description": "<p>邀约说明</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "invite_receive",
            "description": "<p>是否由我接送 0-是 1-不是</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "time_id",
            "description": "<p>邀约时间：1-不限时间，2-任何休息日，3-不常周末，4-双方商议具体时间</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n    \"InviteResponse\": {\n        \"code\": 200,\t//除了200均为错误，405需要重新登录\n        \"message\": \"发布成功\",\t///返回的信息\n        \"status\": 1\t//状态为1为成功，0为失败\n    }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/InviteController.java",
    "groupTitle": "InviteController",
    "name": "PostAppInvitecontrollerSavesing"
  },
  {
    "type": "post",
    "url": "/app/InviteController/saveTravel",
    "title": "添加旅行邀约",
    "group": "InviteController",
    "description": "<p>用户添加旅行邀约</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "explain_url",
            "description": "<p>邀约说明图片</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "cost_id",
            "description": "<p>邀约费用id：1-我买单，2-AA制，3-来回机票我承担，4-期望对方承担</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "invite_sex",
            "description": "<p>邀约对象性别：0-男，1-女，2-不限</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "travel_days_id",
            "description": "<p>出行天数：1-当天往返，2-预计1~2天，3-预计2~3天，4-预计3~5天，5-预计10天半个月，6-还准备回来吗？</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_content",
            "description": "<p>旅行目的地</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "travel_mode_id",
            "description": "<p>出行方式：1-往返坐飞机，2-动车高铁，3-自驾，4-骑行</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "is_equal_place",
            "description": "<p>有相同目的地的异性是否通知我 0-是 1-否</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "invite_explain",
            "description": "<p>邀约说明</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "time_id",
            "description": "<p>邀约时间：4-双方商议具体时间，5-最近出发，6-某个周末，7-说走就走</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n    \"InviteResponse\": {\n        \"code\": 200,\t//除了200均为错误，405需要重新登录\n        \"message\": \"发布成功\",\t///返回的信息\n        \"status\": 1\t//状态为1为成功，0为失败\n    }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/InviteController.java",
    "groupTitle": "InviteController",
    "name": "PostAppInvitecontrollerSavetravel"
  },
  {
    "type": "get",
    "url": "/app/Pays/aduiPrices",
    "title": "邀约置顶价格",
    "description": "<p>显示邀约价格</p>",
    "group": "Pays",
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n  \"aduiPricesResponse\": {\n        \"code\": 200,\n        \"aduiPricesResult\": {\n            \"Price\": [\n                {\n                    \"price\": 500\t//置顶价格\n                }\n            ]\n        },\n        \"message\": \"\",\n        \"status\": 1\n    }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/Pays.java",
    "groupTitle": "Pays",
    "name": "GetAppPaysAduiprices"
  },
  {
    "type": "get",
    "url": "/app/Pays/payAudit",
    "title": "购买认证",
    "description": "<p>支付认证</p>",
    "group": "Pays",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "pay_type",
            "description": "<p>{1-支付宝、2-微信、3-银联}&quot;</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "user_id",
            "description": "<p>用户ID&quot;</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "aduit_type",
            "description": "<p>认证类型：1-汽车、2-房子</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "price",
            "description": "<p>认证价格</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "InviteResponse\": {\n\t\tPayStarResponse{charge_id}  付款Id\n\t\tPayStarResponse{pay_id}\t\t支付流水号\n\t\t\"code\": 400,\t\t200-正常返回，405-重新登陆\n\t\t\"message\": \"\",\t//返回的信息\n\t\t\"status\": 1\t\t//状态为1为成功，0为失败\n\t}\n}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/Pays.java",
    "groupTitle": "Pays",
    "name": "GetAppPaysPayaudit"
  },
  {
    "type": "get",
    "url": "/app/Pays/payStar",
    "title": "购买超级明星",
    "description": "<p>支付超级明星</p>",
    "group": "Pays",
    "version": "1.0.0",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "sex",
            "description": "<p>用户类型：1-甜心大哥，0-甜心宝贝</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "days",
            "description": "<p>天数</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "user_id",
            "description": "<p>用户ID</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "price",
            "description": "<p>超级明星价格</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "InviteResponse\": {\n\t\tPayStarResponse{charge_id}  付款Id\n\t\tPayStarResponse{pay_id}\t\t支付流水号\n\t\t\"code\": 400,\t\t200-正常返回，405-重新登陆\n\t\t\"message\": \"\",\t//返回的信息\n\t\t\"status\": 1\t\t//状态为1为成功，0为失败\n\t}\n}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/Pays.java",
    "groupTitle": "Pays",
    "name": "GetAppPaysPaystar"
  },
  {
    "type": "get",
    "url": "/app/Pays/superStarPrices",
    "title": "超级明星价格",
    "description": "<p>显示超级明星价格</p>",
    "group": "Pays",
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n  {\n    \"superStarPricesResponse\": {\n        \"code\": 200,\n        \"superStarPricesResult\": {\n            \"Price\": [\n                {\n                    \"hours\": 12,\t//超级明星使用时间\t\n                    \"price\": 12\t\t//超级明星价格\n                }\n            ]\n        },\n        \"message\": \"\",\n        \"status\": 1\n    }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/Pays.java",
    "groupTitle": "Pays",
    "name": "GetAppPaysSuperstarprices"
  },
  {
    "type": "get",
    "url": "/app/ReviewController/saveHeart",
    "title": "提交个性签名审核申请",
    "description": "<p>个性签名审核申请</p>",
    "group": "ReviewController",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "content",
            "description": "<p>用户更改的个性签名.</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n\t\"ReviewResponse\": {\n\t\t\"code\": 400,\t\t\t//除了200均为错误，405需要重新登录\n\t\t\"message\": \"未知错误\",\t\t//返回的信息\n\t\t\"status\": 0\t\t\t//状态为1为成功，0为失败\n\t}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/ReviewController.java",
    "groupTitle": "ReviewController",
    "name": "GetAppReviewcontrollerSaveheart"
  },
  {
    "type": "post",
    "url": "/app/ReviewController/findCarClassify",
    "title": "返回汽车详情列表",
    "group": "ReviewController",
    "description": "<p>返回汽车详情列表接口</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "int",
            "optional": false,
            "field": "categroy_id",
            "description": "<p>汽车分类id</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n\t\"ReviewResponse\": {\n\t\t\"code\": 400,\t\t\t//除了200均为错误，405需要重新登录\n\t\t\"list\": [\n\t\t\t{\n\t\t\t\t\"car_name\": \"奔驰A级\",\t//汽车名称\n\t\t\t\t\"car_url\": \"1559290520110.jpg\",\t//汽车图片\n\t\t\t\t\"categroy_id\": 1 \t//分类id\n\t\t\t},\n\t\t\t{\n\t\t\t\t\"car_name\": \"奔驰G级AMG\",\t//汽车名称\n\t\t\t\t\"car_url\": \"1559290551038.jpg\",\t//汽车图片\n\t\t\t\t\"categroy_id\": 1\t//分类id\n\t\t\t}\n\t\t]\n\t}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/ReviewController.java",
    "groupTitle": "ReviewController",
    "name": "PostAppReviewcontrollerFindcarclassify"
  },
  {
    "type": "post",
    "url": "/app/ReviewController/findCategroy",
    "title": "返回汽车品牌列表",
    "group": "ReviewController",
    "description": "<p>返回汽车品牌列表接口</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n\t\"ReviewResponse\": {\n\t\t\"code\": 400,\t\t\t//除了200均为错误，405需要重新登录\n\t\t\"list\": [\n\t\t\t{\n\t\t\t\t\"type_name\": \"奔驰\",\t//品牌名称\n\t\t\t\t\"car_url\": \"1559275278062.jpg\",\t//分类图片\n\t\t\t\t\"id\": 1,\n\t\t\t\t\"type\": \"high\"\t//品牌类型\n\t\t\t}\n\t\t]\n\t}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/ReviewController.java",
    "groupTitle": "ReviewController",
    "name": "PostAppReviewcontrollerFindcategroy"
  },
  {
    "type": "post",
    "url": "/app/ReviewController/saveEdu",
    "title": "提交学历认证审核申请",
    "description": "<p>学历审核申请接口</p>",
    "group": "ReviewController",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "File",
            "optional": false,
            "field": "edu_url",
            "description": "<p>用户上传的学历照片</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n\t\"ReviewResponse\": {\n\t\t\"code\": 400,\t\t\t//除了200均为错误，405需要重新登录\n\t\t\"message\": \"未知错误\",\t\t//返回的信息\n\t\t\"status\": 0\t\t\t//状态为1为成功，0为失败\n\t}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/ReviewController.java",
    "groupTitle": "ReviewController",
    "name": "PostAppReviewcontrollerSaveedu"
  },
  {
    "type": "post",
    "url": "/app/ReviewController/saveHouse",
    "title": "提交房产申请",
    "description": "<p>房产审核申请接口</p>",
    "group": "ReviewController",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "File",
            "optional": false,
            "field": "house_url",
            "description": "<p>用户上传的房产证照片</p>"
          },
          {
            "group": "Parameter",
            "type": "File",
            "optional": false,
            "field": "house_city",
            "description": "<p>用户的城市</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n\t\"ReviewResponse\": {\n\t\t\"code\": 400,\t\t\t//除了200均为错误，405需要重新登录\n\t\t\"message\": \"未知错误\",\t\t//返回的信息\n\t\t\"status\": 0\t\t\t//状态为1为成功，0为失败\n\t}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/ReviewController.java",
    "groupTitle": "ReviewController",
    "name": "PostAppReviewcontrollerSavehouse"
  },
  {
    "type": "post",
    "url": "/app/ReviewController/saveIdCard",
    "title": "提交身份证审核申请",
    "group": "ReviewController",
    "description": "<p>身份证审核申请接口</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "File",
            "optional": false,
            "field": "id_card_up",
            "description": "<p>身份证正面</p>"
          },
          {
            "group": "Parameter",
            "type": "File",
            "optional": false,
            "field": "id_card_down",
            "description": "<p>身份证反面</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n\t\"ReviewResponse\": {\n\t\t\"code\": 400,\t\t\t//除了200均为错误，405需要重新登录\n\t\t\"message\": \"未知错误\",\t\t//返回的信息\n\t\t\"status\": 0\t\t\t//状态为1为成功，0为失败\n\t}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/ReviewController.java",
    "groupTitle": "ReviewController",
    "name": "PostAppReviewcontrollerSaveidcard"
  },
  {
    "type": "post",
    "url": "/app/ReviewController/saveVideo",
    "title": "提交汽车认证审核申请",
    "group": "ReviewController",
    "description": "<p>汽车认证审核申请接口</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "File",
            "optional": false,
            "field": "drivers",
            "description": "<p>视频文件</p>"
          },
          {
            "group": "Parameter",
            "type": "Sting",
            "optional": false,
            "field": "car_name",
            "description": "<p>汽车名称</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n\t\"ReviewResponse\": {\n\t\t\"code\": 400,\t\t\t//除了200均为错误，405需要重新登录\n\t\t\"message\": \"未知错误\",\t\t//返回的信息\n\t\t\"status\": 0\t\t\t//状态为1为成功，0为失败\n\t}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/ReviewController.java",
    "groupTitle": "ReviewController",
    "name": "PostAppReviewcontrollerSavevideo"
  },
  {
    "type": "post",
    "url": "/app/ReviewController/saveVideo",
    "title": "提交视频认证审核申请",
    "group": "ReviewController",
    "description": "<p>视频认证审核申请接口</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "token",
            "description": "<p>用户的Token.</p>"
          },
          {
            "group": "Parameter",
            "type": "File",
            "optional": false,
            "field": "video_url",
            "description": "<p>视频文件</p>"
          }
        ]
      }
    },
    "version": "1.0.0",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n\t\"ReviewResponse\": {\n\t\t\"code\": 400,\t\t\t//除了200均为错误，405需要重新登录\n\t\t\"message\": \"未知错误\",\t\t//返回的信息\n\t\t\"status\": 0\t\t\t//状态为1为成功，0为失败\n\t}\n}",
          "type": "json"
        }
      ]
    },
    "filename": "src/main/com/quark/app/controller/ReviewController.java",
    "groupTitle": "ReviewController",
    "name": "PostAppReviewcontrollerSavevideo"
  }
] });
