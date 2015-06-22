package com.ConnectServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.os.Message;

import com.VO.DriveRecordInfo;
import com.VO.Position;
import com.VO.User;

public class ServerConnectThread extends Thread {

	private Handler handler;
	/*
	 * 各个功能需要的参数 PATH------连接服务器 user&&Method------用户管理（登陆、注册、修改密码）
	 * username------获取最大记录值&&获取记录集 dangerInfo------上传危险信息
	 * positionList------上传经纬度集
	 */
	private String Method = null;// 需要进行的操作
	private String PATH = null;
	private User user = null;
	private String UserManageMethod = null;
	private String username = null;
	private DriveRecordInfo dangerInfo = null;
	private String MaxDriveRecord;
	private String number;
	private List<Position> positionList = new ArrayList<Position>();
	private String UserBackText;
	private String Time;
	public ServerConnectThread(String PATH) {

	}

	// TODO 构造函数 ----用于用户管理
	public ServerConnectThread(Handler handler, User user,
			String UserManageMethod, String PATH, String Method) {
		this.handler = handler;
		this.user = user;
		this.UserManageMethod = UserManageMethod;
		this.PATH = PATH;
		this.Method = Method;
	}

	// TODO 构造函数 ----用于获取最大记录值&&获取记录集
	public ServerConnectThread(Handler handler, String username, String PATH,
			String Method) {
		this.handler = handler;
		this.username = username;
		this.PATH = PATH;
		this.Method = Method;
	}

	// TODO 构造函数 ----用于获取位置坐标集
	public ServerConnectThread(Handler handler, String username, String number,
			String PATH, String Method) {
		this.handler = handler;
		this.username = username;
		this.number = number;
		this.PATH = PATH;
		this.Method = Method;
	}

	// TODO 构造函数 ---- 上传危险信息
	public ServerConnectThread(Handler handler, DriveRecordInfo dangerInfo,
			String PATH, String Method) {
		this.handler = handler;
		this.dangerInfo = dangerInfo;
		this.PATH = PATH;
		this.Method = Method;
	}

	// TODO 构造函数 ---- 上传经纬度集
	public ServerConnectThread(Handler handler, List<Position> positionList,
			String username, String PATH, String MaxDriveRecord, String Method) {
		this.handler = handler;
		this.positionList = positionList;
		this.PATH = PATH;
		this.Method = Method;
		this.MaxDriveRecord = MaxDriveRecord;
	}

	// TODO 构造函数 ---- 上传用户反馈
	public ServerConnectThread(String UserBackText, Handler handler,
			String username, String Time,String PATH, String Method) {
		this.UserBackText = UserBackText;
		this.handler = handler;
		this.username = username;
		this.Time = Time;
		this.PATH = PATH;
		this.Method = Method;
	}

	@Override
	public void run() {
		// TODO run
		String result = null;
		try {
			// 设置HTTP连接
			URL url = new URL(PATH);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setConnectTimeout(3000);
			if (Method.equals("USER_MANAGE")) {
				if (UserManageMethod.equals("login")) {
					result = login(url, urlConnection);
					Message message = handler.obtainMessage(
							ServerConnectHandler.RESULT_USER_MANAGE, result);
					message.sendToTarget();
				} else if (UserManageMethod.equals("register")) {
					result = register(url, urlConnection);
					Message message = handler.obtainMessage(
							ServerConnectHandler.RESULT_USER_MANAGE, result);
					message.sendToTarget();
				} else if (UserManageMethod.equals("IsMatch")) {
					result = IsMatch(url, urlConnection);
					Message message = handler.obtainMessage(
							ServerConnectHandler.RESULT_USER_MANAGE, result);
					message.sendToTarget();
				} else if (UserManageMethod.equals("changePW")) {
					result = changePW(url, urlConnection);
					Message message = handler.obtainMessage(
							ServerConnectHandler.RESULT_USER_MANAGE, result);
					message.sendToTarget();
				}
			} else if (Method.equals("GET_USER_MAX_RECORD")) {
				result = GetMaxRecordNum(url, urlConnection);
				Message message = handler
						.obtainMessage(
								ServerConnectHandler.RESULT_GET_USER_MAX_RECORD,
								result);
				message.sendToTarget();
			} else if (Method.equals("GET_DRIVE_RECORD")) {
				result = GetDriveRecord(url, urlConnection);
				Message message = handler.obtainMessage(
						ServerConnectHandler.RESULT_GET_DRIVE_RECORD, result);
				message.sendToTarget();
			} else if (Method.equals("GET_POSITION")) {
				result = GetPosition(url, urlConnection);
				Message message = handler.obtainMessage(
						ServerConnectHandler.RESULT_GET_POSITION, result);
				message.sendToTarget();
			} else if (Method.equals("UPLOAD_POSITION")) {
				result = UploadPosition(url, urlConnection);
				Message message = handler.obtainMessage(
						ServerConnectHandler.RESULT_UPLOAD_POSITION, result);
				message.sendToTarget();
			} else if (Method.equals("UPLOAD_DANGER")) {
				result = UploadDanger(url, urlConnection);
				Message message = handler.obtainMessage(
						ServerConnectHandler.RESULT_UPLOAD_DANGER, result);
				message.sendToTarget();
			} else if (Method.equals("UPLOAD_USERADVICE")) {
				result = UploadUserBack(url, urlConnection);
				Message message = handler.obtainMessage(
						ServerConnectHandler.RESULT_UPLOAD_USERADVICE, result);
				message.sendToTarget();
			}
		} catch (Exception e) {
			Message message = handler.obtainMessage(
					ServerConnectHandler.CONNECT_FAIL, result);
			message.sendToTarget();
		}
	}

	// TODO 方法 ---- 登陆操作
	private String login(URL url, HttpURLConnection urlConnection)
			throws UnsupportedEncodingException {
		String loginResult = "";
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo.put("Method", "login");
		userInfo.put("Username", user.getUsername());
		userInfo.put("Password", user.getPassword());
		StringBuffer buffer = new StringBuffer();
		if (userInfo != null && !userInfo.isEmpty()) {
			for (Map.Entry<String, String> entry : userInfo.entrySet()) {
				buffer.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue(), "utf-8"))
						.append("&");
			}
			buffer.deleteCharAt(buffer.length() - 1);
		}
		byte[] mydata = buffer.toString().getBytes();
		urlConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		urlConnection.setRequestProperty("Content-Length",
				String.valueOf(mydata.length));
		System.out.println("-->>" + buffer.toString());
		try {
			OutputStream outputStream = urlConnection.getOutputStream();
			outputStream.write(mydata, 0, mydata.length);
			outputStream.close();

			int responseCode = urlConnection.getResponseCode();

			if (responseCode == 200) {
				loginResult = changeInputStream(urlConnection.getInputStream(),
						"utf-8");
			}
		} catch (Exception e) {

		}
		return loginResult;
	}

	// TODO 方法 ---- 注册操作
	private String register(URL url, HttpURLConnection urlConnection)
			throws UnsupportedEncodingException {
		String registerResult = null;
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo.put("Method", "register");
		userInfo.put("Username", user.getUsername());
		userInfo.put("Password", user.getPassword());
		userInfo.put("Phone", user.getPhone());
		userInfo.put("Name", user.getName());
		StringBuffer buffer = new StringBuffer();
		if (userInfo != null && !userInfo.isEmpty()) {
			for (Map.Entry<String, String> entry : userInfo.entrySet()) {
				buffer.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue(), "utf-8"))
						.append("&");
			}
			buffer.deleteCharAt(buffer.length() - 1);
		}
		byte[] mydata = buffer.toString().getBytes();
		urlConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		urlConnection.setRequestProperty("Content-Length",
				String.valueOf(mydata.length));
		System.out.println("-->>" + buffer.toString());
		try {
			OutputStream outputStream = urlConnection.getOutputStream();
			outputStream.write(mydata, 0, mydata.length);
			outputStream.close();

			int responseCode = urlConnection.getResponseCode();

			if (responseCode == 200) {
				registerResult = changeInputStream(
						urlConnection.getInputStream(), "utf-8");
			}
		} catch (Exception e) {

		}
		return registerResult;
	}

	// TODO 方法 ---- 匹配操作
	private String IsMatch(URL url, HttpURLConnection urlConnection)
			throws UnsupportedEncodingException {
		String IsMatchResult = null;
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo.put("Method", "IsMatch");
		userInfo.put("Username", user.getUsername());
		userInfo.put("Phone", user.getPhone());
		StringBuffer buffer = new StringBuffer();
		if (userInfo != null && !userInfo.isEmpty()) {
			for (Map.Entry<String, String> entry : userInfo.entrySet()) {
				buffer.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue(), "utf-8"))
						.append("&");
			}
			buffer.deleteCharAt(buffer.length() - 1);
		}
		byte[] mydata = buffer.toString().getBytes();
		urlConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		urlConnection.setRequestProperty("Content-Length",
				String.valueOf(mydata.length));
		System.out.println("-->>" + buffer.toString());
		try {
			OutputStream outputStream = urlConnection.getOutputStream();
			outputStream.write(mydata, 0, mydata.length);
			outputStream.close();

			int responseCode = urlConnection.getResponseCode();

			if (responseCode == 200) {
				IsMatchResult = changeInputStream(
						urlConnection.getInputStream(), "utf-8");
			}
		} catch (Exception e) {

		}
		return IsMatchResult;
	}

	// TODO 方法 ---- 修改密码操作
	private String changePW(URL url, HttpURLConnection urlConnection)
			throws UnsupportedEncodingException {
		String changePWResult = null;
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo.put("Method", "changePW");
		userInfo.put("Username", user.getUsername());
		userInfo.put("Password", user.getPassword());
		StringBuffer buffer = new StringBuffer();
		if (userInfo != null && !userInfo.isEmpty()) {
			for (Map.Entry<String, String> entry : userInfo.entrySet()) {
				buffer.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue(), "utf-8"))
						.append("&");
			}
			buffer.deleteCharAt(buffer.length() - 1);
		}
		byte[] mydata = buffer.toString().getBytes();
		urlConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		urlConnection.setRequestProperty("Content-Length",
				String.valueOf(mydata.length));
		System.out.println("-->>" + buffer.toString());
		try {
			OutputStream outputStream = urlConnection.getOutputStream();
			outputStream.write(mydata, 0, mydata.length);
			outputStream.close();

			int responseCode = urlConnection.getResponseCode();

			if (responseCode == 200) {
				changePWResult = changeInputStream(
						urlConnection.getInputStream(), "utf-8");
			}
		} catch (Exception e) {

		}
		return changePWResult;
	}

	// TODO 方法 ---- 获取最大记录值
	private String GetMaxRecordNum(URL url, HttpURLConnection urlConnection)
			throws UnsupportedEncodingException {
		String MaxRecordNum = null;
		Map<String, String> Info = new HashMap<String, String>();
		Info.put("Username", username);
		StringBuffer buffer = new StringBuffer();
		if (Info != null && !Info.isEmpty()) {
			for (Map.Entry<String, String> entry : Info.entrySet()) {
				buffer.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue(), "utf-8"))
						.append("&");
			}
			buffer.deleteCharAt(buffer.length() - 1);
		}
		byte[] mydata = buffer.toString().getBytes();
		urlConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		urlConnection.setRequestProperty("Content-Length",
				String.valueOf(mydata.length));
		System.out.println("-->>" + buffer.toString());
		try {
			OutputStream outputStream = urlConnection.getOutputStream();
			outputStream.write(mydata, 0, mydata.length);
			outputStream.close();

			int responseCode = urlConnection.getResponseCode();

			if (responseCode == 200) {
				MaxRecordNum = changeInputStream(
						urlConnection.getInputStream(), "utf-8");
			}
		} catch (Exception e) {

		}
		return MaxRecordNum;
	}

	// TODO 方法 ---- 获取坐标集
	private String GetPosition(URL url, HttpURLConnection urlConnection)
			throws UnsupportedEncodingException {
		String Position = null;
		Map<String, String> Info = new HashMap<String, String>();
		Info.put("Username", username);
		Info.put("Number", number);
		StringBuffer buffer = new StringBuffer();
		if (Info != null && !Info.isEmpty()) {
			for (Map.Entry<String, String> entry : Info.entrySet()) {
				buffer.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue(), "utf-8"))
						.append("&");
			}
			buffer.deleteCharAt(buffer.length() - 1);
		}
		byte[] mydata = buffer.toString().getBytes();
		urlConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		urlConnection.setRequestProperty("Content-Length",
				String.valueOf(mydata.length));
		System.out.println("-->>" + buffer.toString());
		try {
			OutputStream outputStream = urlConnection.getOutputStream();
			outputStream.write(mydata, 0, mydata.length);
			outputStream.close();

			int responseCode = urlConnection.getResponseCode();

			if (responseCode == 200) {
				Position = changeInputStream(urlConnection.getInputStream(),
						"utf-8");
			}
		} catch (Exception e) {

		}
		return Position;
	}

	// TODO 方法 ---- 获取行驶记录
	private String GetDriveRecord(URL url, HttpURLConnection urlConnection)
			throws UnsupportedEncodingException {
		String DriveRecord = null;
		Map<String, String> Info = new HashMap<String, String>();
		Info.put("UserName", username);
		StringBuffer buffer = new StringBuffer();
		if (Info != null && !Info.isEmpty()) {
			for (Map.Entry<String, String> entry : Info.entrySet()) {
				buffer.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue(), "utf-8"))
						.append("&");
			}
			buffer.deleteCharAt(buffer.length() - 1);
		}
		byte[] mydata = buffer.toString().getBytes();
		urlConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		urlConnection.setRequestProperty("Content-Length",
				String.valueOf(mydata.length));
		System.out.println("-->>" + buffer.toString());
		try {
			OutputStream outputStream = urlConnection.getOutputStream();
			outputStream.write(mydata, 0, mydata.length);
			outputStream.close();

			int responseCode = urlConnection.getResponseCode();

			if (responseCode == 200) {
				DriveRecord = changeInputStream(urlConnection.getInputStream(),
						"utf-8");
			}
		} catch (Exception e) {

		}
		return DriveRecord;
	}

	// TODO 方法 ---- 上传位置集
	private String UploadPosition(URL url, HttpURLConnection urlConnection)
			throws UnsupportedEncodingException {
		String UploadPositionResult = null;
		String PositionString = null;
		PositionString = positionListToString();
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo.put("UserName", username);
		userInfo.put("MaxDriveRecord", MaxDriveRecord);
		userInfo.put("Position", PositionString);
		StringBuffer buffer = new StringBuffer();
		if (userInfo != null && !userInfo.isEmpty()) {
			for (Map.Entry<String, String> entry : userInfo.entrySet()) {
				buffer.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue(), "utf-8"))
						.append("&");
			}
			buffer.deleteCharAt(buffer.length() - 1);
		}
		byte[] mydata = buffer.toString().getBytes();
		urlConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		urlConnection.setRequestProperty("Content-Length",
				String.valueOf(mydata.length));
		System.out.println("-->>" + buffer.toString());
		try {
			OutputStream outputStream = urlConnection.getOutputStream();
			outputStream.write(mydata, 0, mydata.length);
			outputStream.close();

			int responseCode = urlConnection.getResponseCode();

			if (responseCode == 200) {
				UploadPositionResult = changeInputStream(
						urlConnection.getInputStream(), "utf-8");
			}
		} catch (Exception e) {

		}
		return UploadPositionResult;
	}

	// TODO 方法 ---- 上传危险信息
	private String UploadDanger(URL url, HttpURLConnection urlConnection)
			throws UnsupportedEncodingException {
		String UploadDangerResult = null;
		Map<String, String> dangerInfoMap = new HashMap<String, String>();
		dangerInfoMap.put("UserName", username);
		dangerInfoMap.put("RecordId", String.valueOf(dangerInfo.getRecordId()));
		dangerInfoMap.put("Time", dangerInfo.getTime());
		dangerInfoMap.put("DangerLongitude", dangerInfo.getDangerLongitude());
		dangerInfoMap.put("DangerLatitude", dangerInfo.getDangerLatitude());
		dangerInfoMap.put("DangerSpeed", dangerInfo.getDangerSpeed());
		dangerInfoMap.put("RecordType", dangerInfo.getRecordType());
		StringBuffer buffer = new StringBuffer();
		if (dangerInfoMap != null && !dangerInfoMap.isEmpty()) {
			for (Map.Entry<String, String> entry : dangerInfoMap.entrySet()) {
				buffer.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue(), "utf-8"))
						.append("&");
			}
			buffer.deleteCharAt(buffer.length() - 1);
		}
		byte[] mydata = buffer.toString().getBytes();
		urlConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		urlConnection.setRequestProperty("Content-Length",
				String.valueOf(mydata.length));
		System.out.println("-->>" + buffer.toString());
		try {
			OutputStream outputStream = urlConnection.getOutputStream();
			outputStream.write(mydata, 0, mydata.length);
			outputStream.close();

			int responseCode = urlConnection.getResponseCode();

			if (responseCode == 200) {
				UploadDangerResult = changeInputStream(
						urlConnection.getInputStream(), "utf-8");
			}
		} catch (Exception e) {

		}
		return UploadDangerResult;
	}

	// TODO 上传用户反馈
	private String UploadUserBack(URL url, HttpURLConnection urlConnection) throws UnsupportedEncodingException {
		String UploadAdviceResult = null;
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo.put("UserName", username);
		userInfo.put("Time", Time);
		userInfo.put("UserBackText", UserBackText);
		StringBuffer buffer = new StringBuffer();
		if (userInfo != null && !userInfo.isEmpty()) {
			for (Map.Entry<String, String> entry : userInfo.entrySet()) {
				buffer.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue(), "utf-8"))
						.append("&");
			}
			buffer.deleteCharAt(buffer.length() - 1);
		}
		byte[] mydata = buffer.toString().getBytes();
		urlConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		urlConnection.setRequestProperty("Content-Length",
				String.valueOf(mydata.length));
		System.out.println("-->>" + buffer.toString());
		try {
			OutputStream outputStream = urlConnection.getOutputStream();
			outputStream.write(mydata, 0, mydata.length);
			outputStream.close();

			int responseCode = urlConnection.getResponseCode();

			if (responseCode == 200) {
				UploadAdviceResult = changeInputStream(
						urlConnection.getInputStream(), "utf-8");
			}
		} catch (Exception e) {

		}
		return UploadAdviceResult;
	}

	// TODO inputStream -----》 String
	private String changeInputStream(InputStream inputStream, String encode) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		String result = "";
		if (inputStream != null) {
			try {
				while ((len = inputStream.read(data)) != -1) {
					outputStream.write(data, 0, len);
				}
				result = new String(outputStream.toByteArray(), encode);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public String positionListToString() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < positionList.size(); i++) {
			// buffer.append(mylist.get(i).toString()).append(",");
			buffer.append(positionList.get(i).getLongitude()).append(",")
					.append(positionList.get(i).getLatitude()).append("&");
		}
		buffer.deleteCharAt(buffer.length() - 1);
		System.out.println(buffer.toString());
		return buffer.toString();
	}
}
