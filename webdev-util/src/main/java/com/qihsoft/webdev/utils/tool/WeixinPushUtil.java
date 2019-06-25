package com.qihsoft.webdev.utils.tool;


import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.utils.convert.V;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeixinPushUtil {


	/**
	 * 向微信平台发送推送信息
	 *
	 * @param openid
	 *            接收人id
	 * @param url
	 *           跳转链接
	 * @param obj
	 *            推送数据包：格式 {"name":"李文","result":"您已成功注册智慧党建服务平台"}
	 * @return boolean 所代表远程资源的响应结果
	 */

	//微信推送 传入对象   url推送地址 type推送类别 obj推送数据
	//推送类别 pushType： login 登录 bind 绑定  bookreturn图书催还
	// obj 推送数据:
	// login 登录 需要字段 wechatName 微信名 number 学号
	// bind 绑定 需要字段 wechatName 微信名 number 学号 myclass班级  name姓名
	// teabind 教师绑定 需要字段 wechatName 微信名 number 工号 name姓名
	// bookreturn图书催还 需要字段 returnDate归还时间  bookName图书名称
	// bookreturnout图书逾期提醒 需要字段 returnDate归还时间  bookName图书名称
	// courseTips上课提醒 需要字段 course课程 time时间  place地点  tipsType:今日/明日
    // cardTips一卡通余额提醒 需要字段 balance 余额 number 学工号 name姓名

	public static boolean sendPush(String openid,String url,JSONObject obj,String type,String  access_token) {
		if(V.isEmpty(openid)||V.isEmpty(url)||V.isEmpty(obj)||V.isEmpty(type)||V.isEmpty(access_token)){
			return false;
		}


		JSONObject data =new JSONObject();
		//微信推送流程 https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277
		PropertiesUtil db= new PropertiesUtil("conf.properties");
		String weixin_template_id =  db.readProperty("weixin.template."+type);


		//我要获取当前的日期
		Date date = new Date();
		//设置要获取到什么样的时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//获取String类型的时间
		String dateStr = sdf.format(date);


		JSONObject firstObj = new JSONObject();
		JSONObject keyword1Obj = new JSONObject();
		JSONObject keyword2Obj = new JSONObject();
		JSONObject keyword3Obj = new JSONObject();
		JSONObject keyword4Obj = new JSONObject();
		JSONObject remarkObj = new JSONObject();


		String wechatNameStr="";

		switch (type){
			case "login"://需要字段 wechatName 微信名 number 学号
				if(obj.getString("number")==null){
					return  false;
				}
				if(!V.isEmpty(obj.getString("wechatName"))){
					wechatNameStr="“"+obj.getString("wechatName")+"”";
				}
				firstObj.put("value", "您已成功使用微信账号"+wechatNameStr+"登录智慧校园！");
				data.put("first", firstObj);
				keyword1Obj.put("value",obj.getString("number"));
				data.put("keyword1", keyword1Obj);
				keyword2Obj.put("value",dateStr );
				data.put("keyword2", keyword2Obj);
				remarkObj.put("value","如非本人登陆，请联系网站管理员！");
				data.put("remark", remarkObj);
				break;
			case "bind"://需要字段 wechatName 微信名 number 学号 myclass班级  name姓名
				if(obj.getString("myclass")==null||obj.getString("name")==null||obj.getString("number")==null){
					return  false;
				}
				if(!V.isEmpty(obj.getString("wechatName"))){
					wechatNameStr="“"+obj.getString("wechatName")+"”";
				}
				firstObj.put("value", "您已成功使用微信账号"+wechatNameStr+"绑定身份信息：");
				data.put("first", firstObj);
				keyword1Obj.put("value","广西民族大学");
				data.put("keyword1", keyword1Obj);
				keyword2Obj.put("value",obj.getString("myclass"));
				data.put("keyword2", keyword2Obj);
				keyword3Obj.put("value",obj.getString("number"));
				data.put("keyword3", keyword3Obj);
				keyword4Obj.put("value",obj.getString("name"));
				data.put("keyword4", keyword4Obj);
				remarkObj.put("value","您可使用微信账号登陆智慧校园。");
				data.put("remark", remarkObj);
				break;
			case "teabind"://需要字段 wechatName 微信名 number 工号 name姓名
				if(obj.getString("name")==null||obj.getString("number")==null){
					return  false;
				}
				if(!V.isEmpty(obj.getString("wechatName"))){
					wechatNameStr="“"+obj.getString("wechatName")+"”";
				}
				firstObj.put("value", "您已成功使用微信账号"+wechatNameStr+"绑定身份信息：");
				keyword1Obj.put("value",obj.getString("name"));
				data.put("keyword1", keyword1Obj);
				keyword2Obj.put("value",obj.getString("number"));
				data.put("keyword2", keyword2Obj);
				keyword3Obj.put("value",dateStr );
				data.put("keyword3", keyword3Obj);
				remarkObj.put("value","您可使用微信账号登陆智慧校园！");
				data.put("remark", remarkObj);
				break;
			case "bookreturn"://需要字段 returnDate归还时间  bookName图书名称
				if(obj.getString("returnDate")==null||obj.getString("bookName")==null){
					return  false;
				}
				firstObj.put("value", "下列图书即将到期：");
				data.put("first", firstObj);
				keyword1Obj.put("value",obj.getString("returnDate"));
				data.put("keyword1", keyword1Obj);
				keyword2Obj.put("value",obj.getString("bookName"));
				data.put("keyword2", keyword2Obj);
				remarkObj.put("value","请在应还日期前归还图书或者办理续借手续。");
				data.put("remark", remarkObj);
				break;
			case "bookreturnout"://需要字段 returnDate归还时间  bookName图书名称
				if(obj.getString("returnDate")==null||obj.getString("bookName")==null){
					return  false;
				}
				firstObj.put("value", "下列图书已经超期：");
				data.put("first", firstObj);
				keyword1Obj.put("value",obj.getString("bookName"));
				data.put("keyword1", keyword1Obj);
				keyword2Obj.put("value",obj.getString("returnDate"));
				data.put("keyword2", keyword2Obj);
				remarkObj.put("value","请尽快归还，超期后将逐日产生罚款。");
				data.put("remark", remarkObj);
				break;
			case "courseTips":// 需要字段 course课程 time时间  place地点  tipsType:今日/明日
				if(obj.getString("course")==null||obj.getString("time")==null||obj.getString("place")==null||obj.getString("tipsType")==null){
					return  false;
				}
				firstObj.put("value",obj.getString("tipsType")+ "课程：");
				data.put("first", firstObj);
				keyword1Obj.put("value",obj.getString("course"));
				data.put("keyword1", keyword1Obj);
				keyword2Obj.put("value",obj.getString("time"));
				data.put("keyword2", keyword2Obj);
				keyword3Obj.put("value",obj.getString("place"));
				data.put("keyword3", keyword3Obj);
				remarkObj.put("value","请按时上课，如需请假请提前联系老师。");
				data.put("remark", remarkObj);
				break;
			case "cardTips"://需要字段 balance 余额 number 学工号 name姓名
				if(obj.getString("balance")==null||obj.getString("name")==null||obj.getString("number")==null){
					return  false;
				}
				firstObj.put("value", "您的一卡通余额已不足：");
				keyword1Obj.put("value",obj.getString("number"));
				data.put("keyword1", keyword1Obj);
				keyword2Obj.put("value",obj.getString("name"));
				data.put("keyword2", keyword2Obj);
				keyword3Obj.put("value",obj.getString("balance"));
				data.put("keyword3", keyword3Obj);
				remarkObj.put("value","您的一卡通余额已不足,请尽快充值！");
				data.put("remark", remarkObj);
				break;
				default:
					return  false;

		}


		if(data==null){
			return  false;
		}



			//2、推送模板消息
			//后台模板内容 尊敬的{{name.DATA}}： {{result.DATA}}
			JSONObject postData = new JSONObject();
			postData.put("touser", openid);//接收人识别码（每个微信号对不同公众号有不同识别码）
			postData.put("template_id", weixin_template_id );//模板ID（公众平台后台获取）
			postData.put("url", url);//通知链接地址
			postData.put("data", data);//推送数据

			//System.out.println(postData.toString());

			String sendStr = HttpTookit.sendPost("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" +access_token, postData.toString());

			//System.out.println(sendStr);


			JSONObject sendData = JSONObject.parseObject(sendStr .replace("\\", ""));
		//	System.out.println(sendStr );
			int code = sendData.getInteger("errcode");
			if (code == 0) {
				return  true;
			}else{
				return  false;
			}

	}




	public static void main(String[] args) {
		JSONObject data = new JSONObject();
		data.put("wechatName", "蚂蚁");
		data.put("number", "118583010101");
		data.put("myclass", "18软件");
		data.put("name", "张三");

		//	wechatName 微信名 number 学号 myClass班级  name姓名
		PropertiesUtil db = new PropertiesUtil("conf.properties");
		String weixin_appId = db.readProperty("weixin.appId");
		String weixin_appsecret = db.readProperty("weixin.appsecret");

		//1、获取系统快速开发平台应用授权密钥
		String tokenStr = HttpTookit.sendPost("https://api.weixin.qq.com/cgi-bin/token", "grant_type=client_credential&appid=" + weixin_appId + "&secret=" + weixin_appsecret);

		//appid和secret从微信后台获取
		if (tokenStr.indexOf("access_token") != -1) {
			JSONObject tokenData = JSONObject.parseObject(tokenStr.replace("\\", ""));
			String token = tokenData.getString("access_token");

			sendPush("oiUBo6LeTtmrgSdqvv6zo24BwIb4", "https://wechat.gxun.club/", data, "bind", token);
			//owVgRxIk3yTZBmWo7e9aqcw-VZbw


		}
	}


}
