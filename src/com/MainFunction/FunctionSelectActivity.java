package com.MainFunction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.CloudRecord.CloudRecordActivity;
import com.ConnectBlue.BlueDeviceControl;
import com.ManageDevice.DeviceActiviy;
import com.Userback.UserBackActivity;
import com.aa_mysafedrive.R;
import com.familyControl.FamilyControlActivity;
import com.speedTest.StartTestActivity;

public class FunctionSelectActivity extends Activity {

	private TextView username_tv;

	private Button openBlue_bt;
	private Button StartTest_bt;
	private Button DeviceManage_bt;
	private Button Cloud_bt;
	private Button familyControl_bt;
	private Button userBack_bt;

	private String username;
	// 蓝牙
	private BlueDeviceControl blueControl;

	// private
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_function_selection);

		Intent intent = getIntent();
		username = intent.getStringExtra("username");

		username_tv = (TextView) findViewById(R.id.main_name_tv);
		username_tv.setText(username);
		openBlue_bt = (Button) findViewById(R.id.main_blue_bt);
		StartTest_bt = (Button) findViewById(R.id.main_start_test_bt);
		DeviceManage_bt = (Button) findViewById(R.id.main_device_button);
		Cloud_bt = (Button) findViewById(R.id.main_cloud_button);
		familyControl_bt = (Button) findViewById(R.id.main_familycontrol_button);
		userBack_bt = (Button) findViewById(R.id.main_userback_button);

		blueControl = new BlueDeviceControl();
		if (blueControl.isOpen()) {
			openBlue_bt.setBackgroundResource(R.drawable.main_blueon);
		}

		// 开启蓝牙
		openBlue_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (blueControl.isOpen()) {
					blueControl.closeBlue();
					openBlue_bt.setBackgroundResource(R.drawable.main_blueoff);
				} else {
					blueControl.openBlue();
					openBlue_bt.setBackgroundResource(R.drawable.main_blueon);
				}

			}
		});

		// 设备管理
		DeviceManage_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FunctionSelectActivity.this,
						DeviceActiviy.class);
				intent.putExtra("username", username);
				FunctionSelectActivity.this.startActivity(intent);
			}
		});

		// TODO 云端记录
		Cloud_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FunctionSelectActivity.this,
						CloudRecordActivity.class);
				intent.putExtra("username", username);
				FunctionSelectActivity.this.startActivity(intent);
			}
		});

		// TODO 家人监控
		familyControl_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FunctionSelectActivity.this,
						FamilyControlActivity.class);
				intent.putExtra("username", username);
				FunctionSelectActivity.this.startActivity(intent);
			}
		});

		// TODO 开始测试
		StartTest_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FunctionSelectActivity.this,
						StartTestActivity.class);
				intent.putExtra("username", username);
				FunctionSelectActivity.this.startActivity(intent);
			}
		});

		// TODO 用户反馈
		userBack_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FunctionSelectActivity.this,
						UserBackActivity.class);
				intent.putExtra("username", username);
				FunctionSelectActivity.this.startActivity(intent);
			}
		});

	}
}
