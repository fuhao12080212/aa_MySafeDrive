package com.Map;

import java.util.ArrayList;
import java.util.List;

import com.VO.DriveRecordInfo;
import com.VO.Position;
import com.aa_mysafedrive.R;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

public class ControlMapActivity extends ActionBarActivity {

	// 地图
	MapView mMapView;
	BaiduMap mBaiduMap;
	Marker mMarkerA;

	private List<DriveRecordInfo> RecordList = new ArrayList<DriveRecordInfo>();
	private List<Position> PositionList = new ArrayList<Position>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_record_map);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);

		Intent intent = getIntent();
		String username = intent.getStringExtra("username");
		String recordNumber = intent.getStringExtra("recordNumber");
		final String recordData = intent.getStringExtra("data");

		// TODO 将记录转换成DriveRecordInfo的List
		StringToRecordlist(recordData);

		// 标点

		double addr, addr2;
		// 39.97147, 116.357916
		for (int temp = 0; temp < RecordList.size(); temp++) {
			addr2 = Double.valueOf(RecordList.get(temp).getDangerLongitude());
			addr = Double.valueOf(RecordList.get(temp).getDangerLatitude());
			Mark(addr, addr2);
		}

		
	}

	public void Mark(double arg, double arg1) {
		LatLng point = new LatLng(arg, arg1);
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.mark1);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option1 = new MarkerOptions().position(point).icon(
				bitmap);
		// mBaiduMap.addOverlay(option1);
		mMarkerA = (Marker) (mBaiduMap.addOverlay(option1));
		// 在地图上添加Marker，并显示
		// 创建InfoWindow展示的view
	}

	private void StringToRecordlist(String data) {
		String myinfo[] = data.split("&");
		for (int i = 0; i < myinfo.length; i++) {
			String mydetail[] = myinfo[i].split(",");
			DriveRecordInfo recordInfo = new DriveRecordInfo();
			recordInfo.setRecordId(Integer.valueOf(mydetail[0]));
			recordInfo.setDangerLongitude(mydetail[1]);
			recordInfo.setDangerLatitude(mydetail[2]);
			recordInfo.setDangerSpeed(mydetail[3]);
			RecordList.add(recordInfo);
		}
	}
}
