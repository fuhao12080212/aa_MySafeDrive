package com.ConnectBlue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;

public class BlueDeviceControl {
	private BluetoothAdapter adapter;

	/** 构造方法 */
	public BlueDeviceControl() {
		adapter = BluetoothAdapter.getDefaultAdapter();
	}

	/** 设备是否支持蓝牙 */
	public boolean isBlueToothSupported() {
		return adapter != null;
	}
	
	/** 设备是否已经打开蓝牙 */
	public boolean isOpen() {
		return adapter.isEnabled();
	}

	/** 打开蓝牙 */
	public void openBlue() {
		if (!adapter.isEnabled()) {
			adapter.enable();
		}
	}

	/** 关闭蓝牙 */
	public void closeBlue() {
		if (adapter.isEnabled()) {
			adapter.disable();
		}
	}

	/** 返回已配对的蓝牙设备 */
	public String[] getPartners() {
		Set<BluetoothDevice> set = adapter.getBondedDevices();
		String list[] = new String[set.size()];
		int i = 0;
		for (BluetoothDevice device : set) {
			list[i] = device.getAddress();
			i++;
		}
		return list;

	}

	/** 返回已配对的蓝牙设备 */
	public ArrayList<Map<String, String>> allPartners() {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Set<BluetoothDevice> set = adapter.getBondedDevices();
		for (BluetoothDevice device : set) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", device.getName());
			map.put("address", device.getAddress());
			list.add(map);
		}
		return list;
	}

	/** 自动连接上次连接过的蓝牙地址 */
	public String getAdress(Context context) {
		SharedPreferences prference;
		String BLUE_PREF_NAME = "BLUE_PREF_NAME";
		String ADDRESS = "address";
		prference = context.getSharedPreferences(BLUE_PREF_NAME, 0);
		String tmp = prference.getString(ADDRESS, "00:0E:EA:CE:C9:64");
		return tmp;
	}

	/** 保存蓝牙地址，SharedPreferences保存 */
	public void saveAdress(Context context, String mac) {
		if (context != null) {
			SharedPreferences prference;
			String BLUE_PREF_NAME = "BLUE_PREF_NAME";
			String ADDRESS = "address";
			prference = context.getSharedPreferences(BLUE_PREF_NAME, 0);
			prference.edit().putString(ADDRESS, mac).commit();
		}
	}
}
