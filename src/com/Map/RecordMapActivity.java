package com.Map;

import java.util.ArrayList;
import java.util.List;

import com.CommonData.MyURLPath;
import com.ConnectServer.ServerConnectCallBack;
import com.ConnectServer.ServerConnectHandler;
import com.VO.DriveRecordInfo;
import com.VO.Position;
import com.aa_mysafedrive.R;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract.Colors;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

public class RecordMapActivity extends ActionBarActivity {

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
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(16);
		mBaiduMap.animateMapStatus(mapStatusUpdate);

		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);

		Intent intent = getIntent();
		String username = intent.getStringExtra("username");
		String recordNumber = intent.getStringExtra("recordNumber");
		final String recordData = intent.getStringExtra("data");

		// TODO 将记录转换成DriveRecordInfo的List
		StringToRecordlist(recordData);

		MyURLPath urlPath = new MyURLPath();
		String PATH = urlPath.getGetPositionListURLPath();

		// 标点

		double addr, addr2;
		// 39.97147, 116.357916
		for (int temp = 0; temp < RecordList.size(); temp++) {
			addr2 = Double.valueOf(RecordList.get(temp).getDangerLongitude());
			addr = Double.valueOf(RecordList.get(temp).getDangerLatitude());
			Mark(addr, addr2);
		}

		ServerConnectHandler handler = new ServerConnectHandler(
				new ServerConnectCallBack() {

					@Override
					public void connect(boolean state) {
						// TODO Auto-generated method stub

					}

					@Override
					public void ConnectResult(String result) {
						// TODO result是获取的这个记录的经纬度集
						System.out.println("--Result--->>>" + result);
						// 将result转换成Position类的List
						StringToPositionlist(result);

						// TODO 画线
						PolyLine(PositionList);
						// 确定中心
						int temp;
						temp = PositionList.size() / 2;
						LatLng cenpt = new LatLng(Double.valueOf(PositionList
								.get(temp).getLongitude()),
								Double.valueOf(PositionList.get(temp)
										.getLatitude()));
						MapStatusUpdate mapStatusUpdate1 = MapStatusUpdateFactory.newLatLng(cenpt);
						mBaiduMap.setMapStatus(mapStatusUpdate1);

					}
				}, username, recordNumber, PATH);
		handler.sendEmptyMessage(ServerConnectHandler.GET_POSITION);
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

	public void PolyLine(List<Position> positionList2) {
		List<LatLng> pts = new ArrayList<LatLng>();
		LatLng pt, pt1;
		for (int temp = 0; temp < positionList2.size(); temp++) {
			double addr, addr1;
			addr = Double.valueOf(positionList2.get(temp).getLatitude());
			addr1 = Double.valueOf(positionList2.get(temp).getLongitude());
			pt = new LatLng(addr1, addr);
			// pt1 = new LatLng(39.88573400, 116.48300100);

			pts.add(pt);
			// pts.add(pt1);
		}

		OverlayOptions points = new PolylineOptions().points(pts)
				.color(Color.BLUE).width(10);
		mBaiduMap.addOverlay(points);
		PolylineOptions polylineOptions = new PolylineOptions();
		polylineOptions.points(pts);
		Overlay polyline = mBaiduMap.addOverlay(polylineOptions);
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

	private void StringToPositionlist(String data) {
		String myinfo[] = data.split("&");
		for (int i = 0; i < myinfo.length; i++) {
			String mydetail[] = myinfo[i].split(",");
			Position position = new Position();
			position.setLongitude(mydetail[0]);
			position.setLatitude(mydetail[1]);
			PositionList.add(position);
		}
	}
}
