package com.CloudRecord;

import java.util.ArrayList;
import java.util.List;

import com.CommonData.MyURLPath;
import com.ConnectServer.ServerConnectCallBack;
import com.ConnectServer.ServerConnectHandler;
import com.VO.DriveRecordInfo;
import com.VO.RecordInfo;
import com.aa_mysafedrive.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

public class CloudRecordActivity extends Activity {
	private ListView RecordListView;
	private RecordAdapter adapter;

	private List<DriveRecordInfo> RecordList = new ArrayList<DriveRecordInfo>();
	private List<RecordInfo> displayRecordList = new ArrayList<RecordInfo>();
	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cloud_record);
		RecordListView = (ListView) findViewById(R.id.cloud_listview);
		Intent intent = getIntent();
		username = intent.getStringExtra("username");

		// URL地址
		MyURLPath myURLPath = new MyURLPath();
		String Path = myURLPath.getDriveRecordURLPath();
		ServerConnectHandler handler = new ServerConnectHandler(
				new ServerConnectCallBack() {

					@Override
					public void connect(boolean state) {
						// TODO Auto-generated method stub

					}

					@Override
					public void ConnectResult(String result) {
						// TODO Auto-generated method stub
						if (result.equals("")) {
							Toast.makeText(CloudRecordActivity.this,
									"检查网络连接状态，或者没有记录", Toast.LENGTH_SHORT)
									.show();
						} else {
							StringToList(result);
							TodisplayList();
							adapter = new RecordAdapter(
									CloudRecordActivity.this,
									displayRecordList, username);
							RecordListView.setAdapter(adapter);
						}
					}
				}, username, Path);
		handler.sendEmptyMessage(ServerConnectHandler.GET_DRIVE_RECORD);
	}

	public void StringToList(String result) {
		String myname[] = result.split("\\$");
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
	}

	public void TodisplayList() {
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
				RecordInfo info = new RecordInfo();
				info.setId(current);
				info.setData(buffer.toString());
				info.setTime(RecordList.get(i - 1).getTime());
				info.setType(RecordList.get(i - 1).getRecordType());
				buffer = new StringBuffer();
				displayRecordList.add(info);
				current++;
				// 指针回移
				i--;
			}
		}
		buffer.deleteCharAt(buffer.length() - 1);
		RecordInfo info = new RecordInfo();
		info.setId(current);
		info.setData(buffer.toString());
		info.setTime(RecordList.get(RecordList.size() - 1).getTime());
		info.setType(RecordList.get(RecordList.size() - 1).getRecordType());
		displayRecordList.add(info);
	}
}
