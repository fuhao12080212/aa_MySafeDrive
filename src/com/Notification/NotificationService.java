package com.Notification;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.CommonData.MyURLPath;
import com.MainFunction.FunctionSelectActivity;
import com.VO.DangerInfo;
import com.VO.FamilyInfo;
import com.aa_mysafedrive.R;
import com.familyControl.FamilyListXML;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class NotificationService extends Service {

	private final IBinder mBinder = new MyBinder();
	private NotificationManager manager;
	private Notification.Builder builder;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);// 创建一个通知的管理类
		builder = new Notification.Builder(this);
		new DangerMessageThread().start();
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	private class MyBinder extends Binder {
		public NotificationService getService() {
			return NotificationService.this;
		}
	}

	private class DangerMessageThread extends Thread {
		private List<FamilyInfo> familyList = new ArrayList<FamilyInfo>();
		private FamilyListXML familyListXML = new FamilyListXML();
		private MyURLPath myURLPath = new MyURLPath();
		private String PATH;
		private final Boolean flag = true;

		public void run() {
			// TODO Auto-generated method stub

			while (flag) {
				PATH = myURLPath.getGetDangerURLPath();
				familyList = familyListXML.GetFamilyList();
				String SumNumber = String.valueOf(familyList.size());
				String Result = null;
				try {
					// 设置HTTP连接
					URL url = new URL(PATH);
					HttpURLConnection urlConnection = (HttpURLConnection) url
							.openConnection();
					urlConnection.setDoInput(true);
					urlConnection.setDoOutput(true);
					urlConnection.setRequestMethod("POST");
					urlConnection.setConnectTimeout(3000);

					Map<String, String> userInfo = new HashMap<String, String>();
					userInfo.put("SumNumber", SumNumber);
					for (int i = 0; i < familyList.size(); i++) {
						userInfo.put("Person" + i, familyList.get(i).getName());
					}
					StringBuffer buffer = new StringBuffer();
					if (userInfo != null && !userInfo.isEmpty()) {
						for (Map.Entry<String, String> entry : userInfo
								.entrySet()) {
							buffer.append(entry.getKey())
									.append("=")
									.append(URLEncoder.encode(entry.getValue(),
											"utf-8")).append("&");
						}
						buffer.deleteCharAt(buffer.length() - 1);
					}
					byte[] mydata = buffer.toString().getBytes();
					urlConnection.setRequestProperty("Content-Type",
							"application/x-www-form-urlencoded");
					urlConnection.setRequestProperty("Content-Length",
							String.valueOf(mydata.length));
					System.out.println("-->>" + buffer.toString());
					OutputStream outputStream = urlConnection.getOutputStream();
					outputStream.write(mydata, 0, mydata.length);
					outputStream.close();

					int responseCode = urlConnection.getResponseCode();

					if (responseCode == 200) {
						Result = changeInputStream(
								urlConnection.getInputStream(), "utf-8");
					}

					if (!Result.equals("")) {
						Context context = getApplicationContext();
						Intent intent = new Intent(context,
								FunctionSelectActivity.class);
						intent.putExtra("dangerInfo", Result);
						PendingIntent contentIntent = PendingIntent
								.getActivity(context, 0, intent, 0);
						builder.setContentIntent(contentIntent);
						builder.setContentTitle("危险行驶记录");
						builder.setContentText("您的家人正处于危险之中");
						builder.setTicker("家人危险驾驶");// 第一次出现在状态栏的内容
						builder.setSmallIcon(R.drawable.notification);
						builder.setDefaults(Notification.DEFAULT_ALL);
						builder.setAutoCancel(true);
						Notification notification = builder.build();// 仅仅限于在高版本4.1中使用
						manager.notify(1000, notification);
					}
					sleep(10000);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}
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

	public List<DangerInfo> ResultToList(String result) {
		List<DangerInfo> dangerList = new ArrayList<DangerInfo>();
		String myinfo[] = result.split("&");
		for (int i = 0; i < myinfo.length; i++) {
			String mydetail[] = myinfo[i].split(",");
			DangerInfo newInfo = new DangerInfo();
			newInfo.setName(mydetail[0]);
			newInfo.setPhone(mydetail[1]);
			newInfo.setDangerLongitude(mydetail[2]);
			newInfo.setDangerLatitude(mydetail[3]);
			newInfo.setDangerSpeed(mydetail[4]);
			dangerList.add(newInfo);
		}
		return dangerList;
	}
}
