package com.ManageDevice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.VO.DeviceInfo;
import com.aa_mysafedrive.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeDevicInfoActivity extends Activity {

	private Button cancel_bt;
	private Button save_bt;
	private EditText name_ed;
	private EditText cartype_ed;
	private EditText num_ed;
	private TextView BlueMac_tv;

	private OnClickListener listener;

	private String Bluemac;
	private DeviceInfo CurrentDeviceInfo;
	private DeviceInfo AfterChangedeviceInfo;
	private Intent intent;

	// 0--添加设备信息
	// 1--更改设备信息
	private int SaveState = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_change_device_info);

		intent = getIntent();
		Bluemac = intent.getStringExtra("BlueMac");
		System.out.println("BlueMac---->>>" + Bluemac);
		initDate();
		initView();
	}

	private void initDate() {

		CurrentDeviceInfo = new DeviceInfo();
		AfterChangedeviceInfo = new DeviceInfo();

		String XMLPath = "/sdcard/MyDeviceInfo.xml";
		File DeviceFile = new File(XMLPath);

		if (DeviceFile.exists()) {
			//如果存在，判断是否文件为空
			GetDeviceInfoFromXML getDeviceInfoFromXML = new GetDeviceInfoFromXML();
			List<DeviceInfo> list = new ArrayList<DeviceInfo>();
			list = getDeviceInfoFromXML.getDeviceList();
			if (list.size() == 0) {
				//文件如果为空，则当前信息设置为空
				CurrentDeviceInfo.setDeviceName("");
				CurrentDeviceInfo.setDeviceBlueMac(Bluemac);
				CurrentDeviceInfo.setCartype("");
				CurrentDeviceInfo.setCarNumber("");
				
			} else {
				//如果不为空，则判断是否有当前MAC地址的记录
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getDeviceBlueMac().equals(Bluemac)) {
						//如果有记录，则获取该信息，并且设置修改State为1
						CurrentDeviceInfo = list.get(i);
						SaveState = 1;
					} else {
						//如果无记录，则当前信息设置为空
						CurrentDeviceInfo.setDeviceName("");
						CurrentDeviceInfo.setDeviceBlueMac(Bluemac);
						CurrentDeviceInfo.setCartype("");
						CurrentDeviceInfo.setCarNumber("");
					}
				}
			}
		} else {
			//如果文件不存在，则第一次登陆，所有当前信息设置为空
			CurrentDeviceInfo.setDeviceName("");
			CurrentDeviceInfo.setDeviceBlueMac(Bluemac);
			CurrentDeviceInfo.setCartype("");
			CurrentDeviceInfo.setCarNumber("");
		}

	}

	private void initView() {
		cancel_bt = (Button) findViewById(R.id.change_info_cancel_bt);
		save_bt = (Button) findViewById(R.id.change_info_sava_bt);
		name_ed = (EditText) findViewById(R.id.change_device_info_name_ed);
		cartype_ed = (EditText) findViewById(R.id.change_device_info_cartype_ed);
		num_ed = (EditText) findViewById(R.id.change_device_info_num_ed);
		BlueMac_tv = (TextView) findViewById(R.id.change_device_info_mac);
		name_ed.setText(CurrentDeviceInfo.getDeviceName());
		cartype_ed.setText(CurrentDeviceInfo.getCartype());
		num_ed.setText(CurrentDeviceInfo.getCarNumber());
		BlueMac_tv.setText(Bluemac);
		listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.change_info_cancel_bt: // 关闭当前界面，返回上一个界面
					cancel();
					break;
				case R.id.change_info_sava_bt: // 保存数据
					save();
					break;
				}
			}
		};

		cancel_bt.setOnClickListener(listener);
		save_bt.setOnClickListener(listener);
	}

	// 取消当前界面，返回上一个界面
	private void cancel() {
		ChangeDevicInfoActivity.this.finish();
	}

	// 保存修改的数据
	private void save() {
		AfterChangedeviceInfo.setDeviceName(name_ed.getText().toString());
		AfterChangedeviceInfo.setDeviceBlueMac(Bluemac);
		AfterChangedeviceInfo.setCartype(cartype_ed.getText().toString());
		AfterChangedeviceInfo.setCarNumber(num_ed.getText().toString());
		
		if (SaveState == 0) {
			WriteDeviceInfoToXML infoToXML = new WriteDeviceInfoToXML(
					AfterChangedeviceInfo);
			infoToXML.ToXML();
		} else if(SaveState == 1){
			ChangeDeviceXMLInfo changeDeviceXMLInfo = new ChangeDeviceXMLInfo(
					AfterChangedeviceInfo);
			changeDeviceXMLInfo.ChangeData();
		}
		
		Toast.makeText(ChangeDevicInfoActivity.this, "修改成功", Toast.LENGTH_SHORT)
				.show();

		Intent intent = new Intent(ChangeDevicInfoActivity.this,
				DeviceActiviy.class);
		ChangeDevicInfoActivity.this.startActivity(intent);
	}
}
