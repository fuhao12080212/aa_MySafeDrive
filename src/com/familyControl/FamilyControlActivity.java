package com.familyControl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.CommonData.MyURLPath;
import com.ConnectServer.ServerConnectCallBack;
import com.ConnectServer.ServerConnectHandler;
import com.VO.DriveRecordInfo;
import com.VO.FamilyInfo;
import com.VO.FamilyRecord;
import com.aa_mysafedrive.R;

public class FamilyControlActivity extends Activity {

	private static String current_username = null;

	private ListView familyRecord_lw;
	private Button FamilyControl_bt;

	private FamilyListXML famliyListXMl;
	private List<FamilyInfo> FamilyList = new ArrayList<FamilyInfo>();
	private MyURLPath urlPath = new MyURLPath();
	private String PATH;

	private List<DriveRecordInfo> RecordList = new ArrayList<DriveRecordInfo>();
	private List<FamilyRecord> displayRecordList = new ArrayList<FamilyRecord>();

	private FamilyControlAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_family_control);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		FamilyControl_bt = (Button) findViewById(R.id.family_manage_button);
		familyRecord_lw = (ListView) findViewById(R.id.family_listview);
	}

	private void initData() {

		famliyListXMl = new FamilyListXML();
		FamilyList = famliyListXMl.GetFamilyList();

		PATH = urlPath.getDriveRecordURLPath();
		for (int i = 0; i < FamilyList.size(); i++) {
			current_username = FamilyList.get(i).getName();
			ServerConnectHandler handler = new ServerConnectHandler(
					new ServerConnectCallBack() {

						@Override
						public void connect(boolean state) {

						}

						@Override
						public void ConnectResult(String result) {
							if (result == null) {
								Toast.makeText(FamilyControlActivity.this,
										"请检查网络连接状态或还未添加好友", Toast.LENGTH_SHORT).show();
							} else {
								String username = StringToList(result);
								TodisplayList(username);
								RecordList = new ArrayList<DriveRecordInfo>();
								adapter = new FamilyControlAdapter(FamilyControlActivity.this,
										displayRecordList);
								familyRecord_lw.setAdapter(adapter);
							}
						}
					}, current_username, PATH);
			handler.sendEmptyMessage(ServerConnectHandler.GET_DRIVE_RECORD);
		}
	}

	private void initListener() {
		FamilyControl_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 弹出菜单
				Intent intent = new Intent(FamilyControlActivity.this,
						FamilyManageActivity.class);
				FamilyControlActivity.this.startActivity(intent);
			}
		});
	}

	public String StringToList(String result) {
		String myname[] = result.split("\\$");
		String username = myname[0];
		String myinfo[] = myname[1].split("&");
		for (int i = 0; i < myinfo.length; i++) {
			String mydetail[] = myinfo[i].split(",");
			DriveRecordInfo newInfo = new DriveRecordInfo();
			newInfo.setRecordId(Integer.valueOf(mydetail[0]));
			newInfo.setTime(mydetail[1]);
			newInfo.setDangerLongitude(mydetail[2]);
			newInfo.setDangerLatitude(mydetail[3]);
			newInfo.setDangerSpeed(mydetail[4]);
			newInfo.setRecordType(mydetail[5]);
			RecordList.add(newInfo);
		}
		return username;
	}

	public void TodisplayList(String username) {
		int current = 1;
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < RecordList.size(); i++) {
			if (RecordList.get(i).getRecordId() == current) {
				buffer.append(RecordList.get(i).getRecordId()).append(",")
						.append(RecordList.get(i).getDangerLongitude())
						.append(",")
						.append(RecordList.get(i).getDangerLatitude())
						.append(",").append(RecordList.get(i).getDangerSpeed())
						.append("&");
			} else {
				buffer.deleteCharAt(buffer.length() - 1);
				FamilyRecord RecordInfo = new FamilyRecord();
				RecordInfo.setId(current);
				RecordInfo.setUsername(username);
				RecordInfo.setTime(RecordList.get(i - 1).getTime());
				RecordInfo.setType(RecordList.get(i - 1).getRecordType());
				RecordInfo.setData(buffer.toString());
				//得到危险次数
				String RecordType = RecordList.get(i - 1).getRecordType();
				if (RecordType.equals("safe")) {
					RecordInfo.setDangerTime("0");
				} else {
					String Data = buffer.toString();
					String myinfo[] = Data.split("&");
					RecordInfo.setDangerTime(String.valueOf(myinfo.length));
				}
				
				buffer = new StringBuffer();
				displayRecordList.add(RecordInfo);
				current++;
				// 指针回移
				i--;
			}
		}
		buffer.deleteCharAt(buffer.length() - 1);

		FamilyRecord RecordInfo = new FamilyRecord();
		RecordInfo.setId(current);
		RecordInfo.setUsername(username);
		RecordInfo.setTime(RecordList.get(RecordList.size() - 1).getTime());
		RecordInfo.setType(RecordList.get(RecordList.size() - 1)
				.getRecordType());
		RecordInfo.setData(buffer.toString());
		
		//得到危险次数
		String Data = buffer.toString();
		String myinfo[] = Data.split("&");
		if (myinfo.length == 1) {
			RecordInfo.setDangerTime("0");
		} else {
			RecordInfo.setDangerTime(String.valueOf(myinfo.length+1));
		}

		//得到危险次数
		String RecordType = RecordList.get(RecordList.size() - 1).getRecordType();
		if (RecordType.equals("safe")) {
			RecordInfo.setDangerTime("0");
		} else {
			String Data1 = buffer.toString();
			String myinfo1[] = Data1.split("&");
			RecordInfo.setDangerTime(String.valueOf(myinfo1.length));
		}
		buffer = new StringBuffer();
		displayRecordList.add(RecordInfo);

	}
}
