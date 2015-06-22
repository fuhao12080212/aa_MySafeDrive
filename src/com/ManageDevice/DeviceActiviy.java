package com.ManageDevice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.ConnectBlue.BlueDeviceControl;
import com.VO.DeviceInfo;
import com.aa_mysafedrive.R;

public class DeviceActiviy extends Activity {

	private ListView deviceListView;

	private BlueDeviceControl blueControl;
	private List<DeviceInfo> device = new ArrayList<DeviceInfo>();
	private String blueMac[];// 蓝牙地址
	private List<String> existMac = new ArrayList<String>();
	private DeviceAdapter adapter;

	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_device_manage);

		Intent intent = getIntent();
		username = intent.getStringExtra("username");

		blueControl = new BlueDeviceControl();
		if (!blueControl.isOpen()) {
			Toast.makeText(DeviceActiviy.this, "请先开启蓝牙", Toast.LENGTH_SHORT)
					.show();
		}
		initView();
		initData();
	}

	private void initView() {
		deviceListView = (ListView) findViewById(R.id.device_listview);
	}

	private void initData() {
		List<DeviceInfo> XMLInfoList = new ArrayList<DeviceInfo>();

		blueMac = blueControl.getPartners();// 刷新
		// 第一次登陆没有XML文件
		String XMLPath = "/sdcard/MyDeviceInfo.xml";
		File DeviceFile = new File(XMLPath);
		if (DeviceFile.exists()) {

			GetDeviceInfoFromXML getDeviceInfoFromXML = new GetDeviceInfoFromXML();
			XMLInfoList = getDeviceInfoFromXML.getDeviceList();
			if (XMLInfoList.size() == 0) {
				for (int i = 0; i < blueMac.length; i++) {
					DeviceInfo deviceInfo = new DeviceInfo();
					deviceInfo.setDeviceName(" ");
					deviceInfo.setDeviceBlueMac(blueMac[i]);
					deviceInfo.setCartype(" ");
					deviceInfo.setCarNumber(" ");

					device.add(deviceInfo);
				}
			} else {
				// 如果XML中有记录，则显示在前面
				for (int i = 0; i < blueMac.length; i++) {
					for (int j = 0; j < XMLInfoList.size(); j++) {
						if (blueMac[i].equals(XMLInfoList.get(j)
								.getDeviceBlueMac())) {
							device.add(XMLInfoList.get(j));
						}
					}
					existMac.add(blueMac[i]);
				}
				// 如果XML中没有记录，则显示在最后，而且device中只有一个
				for (int i = 0; i < blueMac.length; i++) {
					for (int j = 0; j < XMLInfoList.size(); j++) {
						if (blueMac[i].equals(XMLInfoList.get(j)
								.getDeviceBlueMac())) {
							existMac.remove(blueMac[i]);
						}
					}
				}
				for (int i = 0; i < existMac.size(); i++) {
					DeviceInfo deviceInfo = new DeviceInfo();
					deviceInfo.setDeviceName(" ");
					deviceInfo.setDeviceBlueMac(existMac.get(i));
					deviceInfo.setCartype(" ");
					deviceInfo.setCarNumber(" ");
					device.add(deviceInfo);
				}
			}

		} else {
			for (int i = 0; i < blueMac.length; i++) {
				DeviceInfo deviceInfo = new DeviceInfo();
				deviceInfo.setDeviceName(" ");
				deviceInfo.setDeviceBlueMac(blueMac[i]);
				deviceInfo.setCartype(" ");
				deviceInfo.setCarNumber(" ");

				device.add(deviceInfo);
			}
		}
		adapter = new DeviceAdapter(this, device, username);
		deviceListView.setAdapter(adapter);
	}
}
