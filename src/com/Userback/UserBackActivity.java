package com.Userback;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.CommonData.MyURLPath;
import com.ConnectServer.ServerConnectCallBack;
import com.ConnectServer.ServerConnectHandler;
import com.aa_mysafedrive.R;

public class UserBackActivity extends Activity {

	private EditText userback_et;
	private Button userback_bt;

	private String username;
	private MyURLPath myURLPath;
	private String PATH;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_userback);

		Intent intent = getIntent();
		username = intent.getStringExtra("username");

		myURLPath = new MyURLPath();
		PATH = myURLPath.getUserBackPath();

		userback_et = (EditText) findViewById(R.id.userback_et);
		userback_bt = (Button) findViewById(R.id.userback_bt);

		userback_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String adviceText = userback_et.getText().toString();

				// TODO 获取系统时间
				Calendar calendar = Calendar.getInstance();
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH)+1;
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				StringBuffer buffer = new StringBuffer();
				buffer.append(year).append("-").append(month).append("-")
						.append(day).append("*").append(hour).append(":")
						.append(minute);
				String Time = null;
				Time = buffer.toString();
				if (adviceText != null) {
					ServerConnectHandler serverConnectHandler = new ServerConnectHandler(
							new ServerConnectCallBack() {

								@Override
								public void connect(boolean state) {

								}

								@Override
								public void ConnectResult(String result) {
									if (result.equals("1")) {
										userback_et.setText("");
										Toast.makeText(UserBackActivity.this,
												"感谢您的宝贵意见，我们会及时改正！",
												Toast.LENGTH_SHORT).show();
									} else {
										Toast.makeText(UserBackActivity.this,
												"提交失败，请检查网络连接状态！",
												Toast.LENGTH_SHORT).show();
									}
								}
							}, username, adviceText, Time, PATH);
					serverConnectHandler
							.sendEmptyMessage(ServerConnectHandler.UPLOAD_USERADVICE);
				} else {
					Toast.makeText(UserBackActivity.this, "还未输入信息",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
