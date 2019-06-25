package com.qihsoft.webdev.utils.tool;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.utils.convert.S;
import com.qihsoft.webdev.utils.convert.V;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class HttpTookit {
	/**
	 * 向指定URL发送GET方法的请求
	 *
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection)realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
			// 建立实际的连接
			connection.connect();

			if(connection.getResponseCode()==200) {

				// 获取所有响应头字段
				Map<String, List<String>> map = connection.getHeaderFields();

				// 遍历所有的响应头字段
				for (String key : map.keySet()) {

				}
				// 定义 BufferedReader输入流来读取URL的响应
				in = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
				return result;
			}else{
				return  null;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	public static byte[] sendGetToByte(String url, String param) {
		InputStream is = null;
		byte[] buffer = new byte[1024];

		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			HttpURLConnection connection= (HttpURLConnection)realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
			// 建立实际的连接
			connection.connect();
			if(connection.getResponseCode()==200) {

				is = connection.getInputStream();
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();

				int len = 0;
				//使用一个输入流从buffer里把数据读取出来
				while ((len = is.read(buffer)) != -1) {
					//用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
					outStream.write(buffer, 0, len);
				}
				//关闭输入流
				is.close();
				//把outStream里的数据写入内存
				return outStream.toByteArray();
			}else{
				return  null;
			}

		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn= (HttpURLConnection)realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			if(conn.getResponseCode()==200) {

				// 定义BufferedReader输入流来读取URL的响应
				in = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
			}else{
				return null;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！"+e);
			e.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static String sendPostJson(String url, String param) {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection )realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
			conn.setRequestProperty("Content-Type", "application/json");

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn
					.getOutputStream(), "UTF-8");
			// 发送请求参数
			out.write(param);
			// flush输出流的缓冲
			out.flush();
			if(conn.getResponseCode()==200) {

				// 定义BufferedReader输入流来读取URL的响应
				in = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
			}else{
				return null;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！"+e);
			e.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static byte[] sendPostJsonToByte(String url, String param) {
		PrintWriter out = null;
		InputStream is = null;

		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
			conn.setRequestProperty("Content-Type", "text/json");

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			 is =conn.getInputStream();


			byte[] buffer = new byte[1024];

			ByteArrayOutputStream outStream = new ByteArrayOutputStream();

			int len = 0;
			//使用一个输入流从buffer里把数据读取出来
			while( (len=is.read(buffer)) != -1 ){
				//用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
				outStream.write(buffer, 0, len);
			}
			//关闭输入流
			is.close();
			//把outStream里的数据写入内存
			return outStream.toByteArray();

		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！"+e);
			e.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(is!=null){
					is.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return  null;
	}



	public static void main(String[] args) {
		String apiKey="Rh1weU6CnSOPjPtjG5G3dtmT";
		String secretKey="ZiWsHYyTPgk2NN3zcoc0OKh4IPGhyMtt";

		String result = HttpTookit.sendPost("https://aip.baidubce.com/oauth/2.0/token","grant_type=client_credentials&client_id="+apiKey+"&client_secret="+secretKey);
		System.out.println(result);
		JSONObject object = JSON.parseObject(result);
		if(V.isEmpty(object.getString("error"))){
			String access_token =object.getString("access_token");
			Integer expires_in =object.getInteger("expires_in");
			JSONObject postObject = new JSONObject();
			postObject.put("version","2.0");
			postObject.put("bot_id","57481");
			postObject.put("log_id", S.getToken());
			JSONObject request = new JSONObject();
			request.put("user_id",S.getToken());
			request.put("query","喵喵喵");
			JSONObject query_info = new JSONObject();
			query_info.put("type","TEXT");
			query_info.put("source","KEYBOARD");
			request.put("query_info",query_info);
			request.put("bernard_level",1);
			request.put("bot_session","");
			postObject.put("request", request);
			String chatResult=HttpTookit.sendPostJson("https://aip.baidubce.com/rpc/2.0/unit/bot/chat?access_token="+access_token,postObject.toJSONString());
			System.out.println(chatResult);
			JSONObject chatJSON= JSON.parseObject(chatResult);
			Integer error_code = chatJSON.getInteger("error_code");
			if(error_code==0){
				JSONObject chatRes =chatJSON.getJSONObject("result");
				JSONObject response =chatRes.getJSONObject("response");
				JSONObject bot_session =chatRes.getJSONObject("bot_session");
				System.out.println(bot_session);
				Integer status =response.getInteger("status");
				if(status==0) {
					JSONArray action_list = response.getJSONArray("action_list");
					int random =(int)(Math.random()*(action_list.size()));
					String say = action_list.getJSONObject(random).getString("say");
					System.out.println(say);
//					for (int i = 0; i < action_list.size(); i++) {
//						JSONObject action = action_list.getJSONObject(i);
//						String say = action.getString("say");
//						System.out.println(say);
//					}
				}
			}

		}
		String[] res = {"123","456","789","222"};
		System.out.println(res.length+":"+res[0]);

    }

	}
