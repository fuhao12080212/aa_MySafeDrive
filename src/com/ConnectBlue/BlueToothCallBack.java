package com.ConnectBlue;

/** 蓝牙回调接口 */
public interface BlueToothCallBack {

	/** 蓝牙连接状态 */
	public void connect(boolean state);

	/** 获取到id */
	public void getDate(String date);
}
