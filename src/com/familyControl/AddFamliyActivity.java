package com.familyControl;

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
import com.VO.FamilyInfo;
import com.VO.User;
import com.aa_mysafedrive.R;

public class AddFamliyActivity extends Activity {

	private EditText username_ed;
	private EditText phone_ed;
	private Button confirm_bt;
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_addfamily);
		
		username_ed = (EditText) findViewById(R.id.addfamily_username_ed);
		phone_ed = (EditText) findViewById(R.id.addfamily_phone_ed);
		confirm_bt = (Button) findViewById(R.id.addfamily_confirm_bt);
		
		dialog = new ProgressDialog(this);
		dialog.setMessage("正在查找");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		
		confirm_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				User user = new User();
				user.setUsername(username_ed.getText().toString());
				user.setPhone(phone_ed.getText().toString());
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
									dialog.dismiss();
									FamilyListXML familyListXML = new FamilyListXML();
									FamilyInfo familyInfo = new FamilyInfo();
									familyInfo.setName(username_ed.getText().toString());
									familyInfo.setPhone(phone_ed.getText().toString());
									familyListXML.AddFriend(familyInfo);
									Intent intent = new Intent(AddFamliyActivity.this, FamilyManageActivity.class);
									AddFamliyActivity.this.startActivity(intent);
									Toast.makeText(AddFamliyActivity.this, "添加成功", Toast.LENGTH_LONG).show();
								} else if (result.equals("not match")) {
									dialog.dismiss();
									Toast.makeText(AddFamliyActivity.this,
											"用户名与手机号码不匹配，请重新输入",
											Toast.LENGTH_LONG).show();
									username_ed.setText("");
									phone_ed.setText("");
								}
							}
						}, user, Method, Path);
				handler.sendEmptyMessage(ServerConnectHandler.USER_MANAGE);
			}
		});
	}
}
