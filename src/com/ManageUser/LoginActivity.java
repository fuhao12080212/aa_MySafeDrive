package com.ManageUser;

import com.CommonData.MyURLPath;
import com.ConnectServer.ServerConnectCallBack;
import com.ConnectServer.ServerConnectHandler;
import com.MainFunction.FunctionSelectActivity;
import com.Notification.NotificationService;
import com.VO.User;
import com.aa_mysafedrive.R;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class LoginActivity extends ActionBarActivity {

	private Button login_button;
	private Button register_button;
	private Button forgetPW_button;
	private EditText username_et;
	private EditText password_et;
	private CheckBox rememberPW_cb;
	private CheckBox autoLogin_cb;
	
	private ProgressDialog dialog;
	private SharedPreferences sharedPreferences;

	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		sharedPreferences = this.getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);

		dialog = new ProgressDialog(this);
		dialog.setMessage("正在登陆....");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		login_button = (Button) findViewById(R.id.login_login_bt);
		register_button = (Button) findViewById(R.id.login_register_bt);
		forgetPW_button = (Button) findViewById(R.id.login_forget_bt);

		username_et = (EditText) findViewById(R.id.login_username_ed);
		password_et = (EditText) findViewById(R.id.login_password_ed);
		rememberPW_cb = (CheckBox) findViewById(R.id.login_remember_cb);
		autoLogin_cb = (CheckBox) findViewById(R.id.login_auto_cb);

		if (sharedPreferences.getBoolean("ISCHECK", false)) {
			rememberPW_cb.setChecked(true);
			username_et.setText(sharedPreferences.getString("USER_NAME", ""));
			password_et.setText(sharedPreferences.getString("PASSWORD", ""));

			if (sharedPreferences.getBoolean("AUTO_ISCHECK", false)) {
				autoLogin_cb.setChecked(true);
				Intent intent = new Intent(LoginActivity.this,
						FunctionSelectActivity.class);
				LoginActivity.this.startActivity(intent);
			}
		}

		login_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.show();
				if (rememberPW_cb.isChecked()) {
					Editor editor = sharedPreferences.edit();
					editor.putString("USER_NAME", username_et.getText()
							.toString());
					editor.putString("PASSWORD", password_et.getText()
							.toString());
					editor.commit();
				}

				// 用户信息
				User user = new User();
				user.setUsername(username_et.getText().toString());
				user.setPassword(password_et.getText().toString());
				// URL地址
				MyURLPath myURLPath = new MyURLPath();
				String Path = myURLPath.getUserManageURLPath();
				// Method方法
				String Method = "login";

				ServerConnectHandler handler = new ServerConnectHandler(
						new ServerConnectCallBack() {

							@Override
							public void connect(boolean state) {
								// TODO Auto-generated method stub

							}

							@Override
							public void ConnectResult(String result) {
								// TODO Auto-generated method stub
								System.out
										.println("My result --->>>>" + result);
								if (result.equals("pass")) {
									Toast.makeText(LoginActivity.this, "登陆成功",
											Toast.LENGTH_SHORT).show();
									Intent service = new Intent(LoginActivity.this, NotificationService.class);
									startService(service);
									Intent intent = new Intent(
											LoginActivity.this,
											FunctionSelectActivity.class);
									intent.putExtra("username", username_et.getText().toString());
									LoginActivity.this.startActivity(intent);
								} else if (result.equals("not pass")) {
									Toast.makeText(LoginActivity.this,
											"用户名或者密码不正确", Toast.LENGTH_SHORT)
											.show();
									username_et.setText("");
									password_et.setText("");
									dialog.dismiss();
								} else if (result.equals("not find")) {
									Toast.makeText(LoginActivity.this,
											"此用户还未注册", Toast.LENGTH_SHORT)
											.show();
									username_et.setText("");
									password_et.setText("");
									dialog.dismiss();
								} else if (result.equals("")) {
									Toast.makeText(LoginActivity.this,
											"请检查网络连接状况", Toast.LENGTH_SHORT)
											.show();
									username_et.setText("");
									password_et.setText("");
									dialog.dismiss();
								}
							}
						}, user, Method, Path);
				handler.sendEmptyMessage(ServerConnectHandler.USER_MANAGE);
			}
		});

		register_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				LoginActivity.this.startActivity(intent);
			}
		});

		forgetPW_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						ForgetPWActivity.class);
				LoginActivity.this.startActivity(intent);
			}
		});

		rememberPW_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (rememberPW_cb.isChecked()) {
					sharedPreferences.edit().putBoolean("ISCHECK", true)
							.commit();
				} else {
					sharedPreferences.edit().putBoolean("ISCHECK", false)
							.commit();
				}
			}
		});

		autoLogin_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (autoLogin_cb.isChecked()) {
					sharedPreferences.edit().putBoolean("AUTO_ISCHECK", true)
							.commit();
				} else {
					sharedPreferences.edit().putBoolean("AUTO_ISCHECK", false)
							.commit();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
