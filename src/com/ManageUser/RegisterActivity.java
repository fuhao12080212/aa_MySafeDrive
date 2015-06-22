package com.ManageUser;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.MainFunction.FunctionSelectActivity;
import com.VO.User;
import com.aa_mysafedrive.R;

public class RegisterActivity extends Activity {

	private EditText username_et;
	private EditText phone_et;
	private EditText name_et;
	private EditText password_et;
	private EditText repassword_et;
	private Button register_bt;

	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		initView();
		register_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.show();

				if (password_et.getText().toString()
						.equals(repassword_et.getText().toString())) {

					if (phone_et.getText().toString().length() == 11) {
						// 用户信息
						User user = new User();
						user.setUsername(username_et.getText().toString());
						user.setPhone(phone_et.getText().toString());
						user.setName(name_et.getText().toString());
						user.setPassword(password_et.getText().toString());
						// URL地址
						MyURLPath myURLPath = new MyURLPath();
						String Path = myURLPath.getUserManageURLPath();
						// Method方法
						String Method = "register";
						
						ServerConnectHandler handler = new ServerConnectHandler(new ServerConnectCallBack() {
							
							@Override
							public void connect(boolean state) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void ConnectResult(String result) {
								// TODO Auto-generated method stub
								System.out.println("My result --->>>>" + result);
								if (result.equals("exist")) {
									dialog.dismiss();
									Toast.makeText(RegisterActivity.this, "此用户名已经被注册", Toast.LENGTH_LONG).show();
									username_et.setText("");
								}else if (result.equals("ok")) {
									Intent intent = new Intent(RegisterActivity.this, FunctionSelectActivity.class);
									intent.putExtra("username", username_et.getText().toString());
									Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
									RegisterActivity.this.startActivity(intent);
								}else if (result.equals("")) {
									dialog.dismiss();
									Toast.makeText(RegisterActivity.this, "请检查网络连接", Toast.LENGTH_LONG).show();
								}
							}
						}, user, Method, Path);
						handler.sendEmptyMessage(ServerConnectHandler.USER_MANAGE);
					} else {
						Toast.makeText(RegisterActivity.this, "手机号码格式错误，请重新输入",
								Toast.LENGTH_SHORT).show();
						phone_et.setText("");
						dialog.dismiss();
					}

				} else {
					Toast.makeText(RegisterActivity.this, "密码不匹配，请重新设置",
							Toast.LENGTH_SHORT).show();
					password_et.setText("");
					repassword_et.setText("");
					dialog.dismiss();
				}
			}
		});
	}

	public void initView() {
		username_et = (EditText) findViewById(R.id.register_username_ed);
		phone_et = (EditText) findViewById(R.id.register_phone_ed);
		name_et = (EditText) findViewById(R.id.register_name_ed);
		password_et = (EditText) findViewById(R.id.register_password_ed);
		repassword_et = (EditText) findViewById(R.id.register_repassword_ed);
		register_bt = (Button) findViewById(R.id.register_bt);

		dialog = new ProgressDialog(this);
		dialog.setMessage("请稍后....");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

	}

}
