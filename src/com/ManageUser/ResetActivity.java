package com.ManageUser;

import com.CommonData.MyURLPath;
import com.ConnectServer.ServerConnectCallBack;
import com.ConnectServer.ServerConnectHandler;
import com.MainFunction.FunctionSelectActivity;
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

public class ResetActivity extends Activity {

	private Button confirm_button;
	private EditText password_ed;
	private EditText repassword_ed;

	private ProgressDialog dialog;
	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_password_reset);

		Intent intent = getIntent();
		username = intent.getStringExtra("username");
		System.out.println("-username---->>>" + username);

		initView();

		confirm_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.show();
				if (password_ed.getText().toString()
						.equals(repassword_ed.getText().toString())) {
					User user = new User();
					user.setUsername(username);
					user.setPassword(password_ed.getText().toString());
					// URL地址
					MyURLPath myURLPath = new MyURLPath();
					String Path = myURLPath.getUserManageURLPath();
					// Method方法
					String Method = "changePW";

					ServerConnectHandler handler = new ServerConnectHandler(
							new ServerConnectCallBack() {

								@Override
								public void connect(boolean state) {
									// TODO Auto-generated method stub

								}

								@Override
								public void ConnectResult(String result) {
									// TODO Auto-generated method stub
									if (result.equals("ok")) {
										Intent intent = new Intent(
												ResetActivity.this,
												FunctionSelectActivity.class);
										intent.putExtra("username", username);
										ResetActivity.this
												.startActivity(intent);
										Toast.makeText(ResetActivity.this,
												"修改成功", Toast.LENGTH_LONG)
												.show();
									} else {
										dialog.dismiss();
										Toast.makeText(ResetActivity.this,
												"修改成功", Toast.LENGTH_LONG)
												.show();
									}
								}
							}, user, Method, Path);
					handler.sendEmptyMessage(ServerConnectHandler.USER_MANAGE);
				} else {
					dialog.dismiss();
					Toast.makeText(ResetActivity.this, "密码不匹配，请重新输入",
							Toast.LENGTH_LONG).show();
					password_ed.setText("");
					repassword_ed.setText("");
				}

			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		password_ed = (EditText) findViewById(R.id.pwreset_password_ed);
		repassword_ed = (EditText) findViewById(R.id.pwreset_repassword_ed);
		confirm_button = (Button) findViewById(R.id.pwreset_bt);

		dialog = new ProgressDialog(this);
		dialog.setMessage("修改中");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}

}
