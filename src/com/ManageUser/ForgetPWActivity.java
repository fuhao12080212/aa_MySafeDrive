package com.ManageUser;

import com.CommonData.MyURLPath;
import com.ConnectServer.ServerConnectCallBack;
import com.ConnectServer.ServerConnectHandler;
import com.VO.User;
import com.aa_mysafedrive.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class ForgetPWActivity extends Activity {

	private Button Confirm_button;
	private EditText username_et;
	private EditText phone_et;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_forget);
		initView();
		Confirm_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.show();
				User user = new User();
				user.setUsername(username_et.getText().toString());
				user.setPhone(phone_et.getText().toString());
				// URL地址
				MyURLPath myURLPath = new MyURLPath();
				String Path = myURLPath.getUserManageURLPath();
				// Method方法
				String Method = "IsMatch";

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
								if (result.equals("match")) {
									Intent intent = new Intent(ForgetPWActivity.this, ResetActivity.class);
									intent.putExtra("username", username_et.getText().toString());
									ForgetPWActivity.this.startActivity(intent);
									Toast.makeText(ForgetPWActivity.this, "匹配成功", Toast.LENGTH_LONG).show();
								} else if (result.equals("not match")) {
									dialog.dismiss();
									Toast.makeText(ForgetPWActivity.this,
											"用户名与手机号码不匹配，请重新输入",
											Toast.LENGTH_LONG).show();
									username_et.setText("");
									phone_et.setText("");
								}
							}
						}, user, Method, Path);
				handler.sendEmptyMessage(ServerConnectHandler.USER_MANAGE);
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		username_et = (EditText) findViewById(R.id.forget_username_ed);
		phone_et = (EditText) findViewById(R.id.forget_phone_ed);
		Confirm_button = (Button) findViewById(R.id.forget_confirm_bt);

		dialog = new ProgressDialog(this);
		dialog.setMessage("正在验证");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}

}
