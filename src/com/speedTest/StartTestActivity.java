package com.speedTest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ManageDevice.GetTestDeviceInfoXML;
import com.VO.DeviceInfo;
import com.aa_mysafedrive.R;

public class StartTestActivity extends Activity {
	private TextView Device_mac_tv;
	private TextView Device_name_tv;
	private TextView Device_type_tv;
	private TextView Device_num_tv;
	private Button Cancel_bt;
	private Button StartTest_bt;

	private DeviceInfo deviceInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start_test);

		initData();
		initView();
	}

	private void initData() {
		// TODO Auto-generated method stub
		GetTestDeviceInfoXML deviceInfoXML = new GetTestDeviceInfoXML();
		deviceInfo = new DeviceInfo();
		deviceInfo = deviceInfoXML.getDeviceInfo();

	}

	private void initView() {
		// TODO Auto-generated method stub
		Device_mac_tv = (TextView) findViewById(R.id.before_device_mac_value);
		Device_name_tv = (TextView) findViewById(R.id.before_device_name_value);
		Device_type_tv = (TextView) findViewById(R.id.before_device_type_value);
		Device_num_tv = (TextView) findViewById(R.id.before_device_num_value);

		Cancel_bt = (Button) findViewById(R.id.before_test_cancel_button);
		StartTest_bt = (Button) findViewById(R.id.before_test_start_button);

		Device_mac_tv.setText(deviceInfo.getDeviceBlueMac());
		Device_name_tv.setText(deviceInfo.getDeviceName());
		Device_type_tv.setText(deviceInfo.getCartype());
		Device_num_tv.setText(deviceInfo.getCarNumber());

		Cancel_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StartTestActivity.this.finish();
			}
		});

		StartTest_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(StartTestActivity.this,
//						SpeedTestActivity.class);
//				intent.putExtra("Mac", deviceInfo.getDeviceBlueMac());
//				StartTestActivity.this.startActivity(intent);
			}
		});
	}
}
