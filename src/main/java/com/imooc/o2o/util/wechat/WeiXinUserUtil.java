package com.imooc.o2o.util.wechat;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.util.DESUtil;
import com.imooc.o2o.util.wechat.message.pojo.UserAccessToken;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeiXinUserUtil {

	private static Logger log = LoggerFactory.getLogger(MenuManager.class);

	public static void getCode() throws UnsupportedEncodingException {
		// String codeUrl =
		// "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf0e81c3bee622d60&redirect_uri="
		// + URLEncoder.encode("www.cityrun.com", "utf-8")
		// +
		// "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
	}

	public static UserAccessToken getUserAccessToken(String code)
			throws IOException {
//		Properties pro = new Properties();
//		pro.load(WeiXinUserUtil.class.getClassLoader().getResourceAsStream(
//				"weixin.properties"));
//		String appId = DESUtil
//				.getDecryptString(pro.getProperty("weixinappid"));
		//测试信号里的appId
		String appId = "wxd7f6c5b8899fba83";
		log.debug("appId:" + appId);
//		String appsecret = DESUtil.getDecryptString(pro
//				.getProperty("weixinappsecret"));
		//测试信号里的appsecret
		String appsecret = "665ae80dba31fc91ab6191e7da4d676d";
		log.debug("secret:" + appsecret);
		//根据传入的code,拼接出访问微信定义好的接口的URL
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ appId + "&secret=" + appsecret + "&code=" + code
				+ "&grant_type=authorization_code";
//		JSONObject jsonObject = WeixinUtil.httpsRequest(url, "GET", null);
		// 向相应URL发送请求获取token\ josn字符串
		String tokenStr = httpsRequest(url, "GET", null);
		log.debug("userAccessToken:" + tokenStr);
		UserAccessToken token = new UserAccessToken();
		ObjectMapper mapper = new ObjectMapper();
		try {
			//将字符串转换成相应对象
			token = mapper.readValue(tokenStr, UserAccessToken.class);
		} catch (JsonParseException e) {
			log.error("获取用户accessToken失败:" + e.getMessage());
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.error("获取用户accessToken失败:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error("获取用户accessToken失败:" + e.getMessage());
			e.printStackTrace();
		}
		if (token==null) {
			log.error("获取用户accessToken失败。");
			return null;
		}

		return token;
	}

	public static WeiXinUser getUserInfo(String accessToken, String openId) {
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
				+ accessToken + "&openid=" + openId + "&lang=zh_CN";
		JSONObject jsonObject = WeixinUtil.httpsRequest(url, "GET", null);
		WeiXinUser user = new WeiXinUser();
		String openid = jsonObject.getString("openid");
		if (openid == null) {
			log.debug("获取用户信息失败。");
			return null;
		}
		user.setOpenId(openid);
		user.setNickName(jsonObject.getString("nickname"));
		user.setSex(jsonObject.getInt("sex"));
		user.setProvince(jsonObject.getString("province"));
		user.setCity(jsonObject.getString("city"));
		user.setCountry(jsonObject.getString("country"));
		user.setHeadimgurl(jsonObject.getString("headimgurl"));
		user.setPrivilege(null);
		// user.setUnionid(jsonObject.getString("unionid"));
		return user;
	}

	public static boolean validAccessToken(String accessToken, String openId) {
		String url = "https://api.weixin.qq.com/sns/auth?access_token="
				+ accessToken + "&openid=" + openId;
		JSONObject jsonObject = WeixinUtil.httpsRequest(url, "GET", null);
		int errcode = jsonObject.getInt("errcode");
		if (errcode == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static PersonInfo getPersonInfoFromRequest(WeiXinUser user) {
		PersonInfo personInfo = new PersonInfo();
		personInfo.setName(user.getNickName());
		personInfo.setGender(user.getSex() + "");
		personInfo.setProfileImg(user.getHeadimgurl());
		personInfo.setEnableStatus(1);
		return personInfo;
	}

	/**
	 * 发起https请求并获取结果
	 */
	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		StringBuffer buffer = new StringBuffer();
		try {
			//创建SSLContext对象,并使用我们指定的信任管理器初始化
			TrustManager[] tm = {new MyX509TrustManager()};
			SSLContext sSlContext = SSLContext.getInstance("SSL", "SunJSSE");
			SSLSocketFactory ssf = sSlContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpURLConn = (HttpsURLConnection) url.openConnection();
			httpURLConn.setSSLSocketFactory(ssf);

			httpURLConn.setDoOutput(true);
			httpURLConn.setDoInput(true);
			//设置请求方式(/GET/POS)
			httpURLConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod))
				httpURLConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpURLConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 将返回的输入流转换成字符串
			InputStream inputStream = httpURLConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpURLConn.disconnect();
			log.debug("https buffer:"+buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return buffer.toString();
		}
	}

