package com.ManageDevice;

import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.MainFunction.FunctionSelectActivity;
import com.VO.DeviceInfo;
import com.aa_mysafedrive.R;

public class DeviceAdapter extends BaseAdapter {

	private Context context;
	private List<DeviceInfo> device;
	private LayoutInflater myInflater;
	private String username;
	private String Bluemac;

	public DeviceAdapter(Context context, List<DeviceInfo> device,
			String username) {
		this.context = context;
		this.device = device;
		this.username = username;
		myInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return device.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return device.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo = device.get(position);
		Bluemac = deviceInfo.getDeviceBlueMac();

		// 获取当前连接的MAC地址
		GetTestDeviceInfoXML currentMAC = new GetTestDeviceInfoXML();
		DeviceInfo currentDevice = new DeviceInfo();
		currentDevice = currentMAC.getDeviceInfo();

		ViewHolder viewHolder = null;
		if (convertView == null) {
			if (currentDevice != null) {
				if (currentDevice.getDeviceBlueMac().equals(Bluemac)) {
					convertView = myInflater.inflate(
							R.layout.device_manage_connect_item, null);
				} else {
					convertView = myInflater.inflate(
							R.layout.device_manage_item, null);
				}
			} else {
				convertView = myInflater.inflate(R.layout.device_manage_item,
						null);
			}

			viewHolder = new ViewHolder();
			viewHolder.DeviceName_tv = (TextView) convertView
					.findViewById(R.id.device_name);
			viewHolder.DeviceManage_bt = (Button) convertView
					.findViewById(R.id.device_manage_bt);
			viewHolder.DeviceManage_bt.setTag(position);
			viewHolder.DeviceConnect_bt = (Button) convertView
					.findViewById(R.id.device_connect_bt);
			viewHolder.DeviceConnect_bt.setTag(position);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if ((deviceInfo.getDeviceName()).equals(" ")) {
			viewHolder.DeviceName_tv.setText(deviceInfo.getDeviceBlueMac());
		} else {
			viewHolder.DeviceName_tv.setText(deviceInfo.getDeviceName());
		}

		viewHolder.DeviceConnect_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int clickPosition = (Integer) v.getTag();
				Toast.makeText(context, "连接成功", Toast.LENGTH_SHORT).show();
				DeviceInfo clickDevice = device.get(clickPosition);
				CurrentDeviceTOXML currentDeviceTOXML = new CurrentDeviceTOXML(
						clickDevice);
				currentDeviceTOXML.ToXML();
				Intent intent = new Intent(context,
						FunctionSelectActivity.class);
				intent.putExtra("username", username);
				context.startActivity(intent);
			}
		});

		viewHolder.DeviceManage_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int clickPosition = (Integer) v.getTag();
				DeviceInfo clickDevice = device.get(clickPosition);
				Bluemac = clickDevice.getDeviceBlueMac();
				Intent intent = new Intent(context,
						ChangeDevicInfoActivity.class);
				intent.putExtra("BlueMac", Bluemac);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	static class ViewHolder {
		public TextView DeviceName_tv;
		public Button DeviceManage_bt;
		public Button DeviceConnect_bt;
	}

}
