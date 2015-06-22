package com.ConnectBlue;

import android.os.Handler;
import android.os.Message;

public class BlueConnetHandler extends Handler {
	private BlueToothCallBack callBack;
	public static String mac = "00:00:00:00:00:00";// 默认
	private BlueConnectThread thread;

	public static final int CONNECT = 0x111111;
	public static final int GET_DATA = 0x111112;
	public static final int CONNECT_FAIL = 0x111114;
	public static final int CONNECT_SUCCEED = 0x111115;
	public static final int SEND = 0x111116;
	public static final int GET_DATA0 = 0x111117;
	public boolean isSend = true;
	public boolean isConnect = false;
	
	private String msgString = "";


	public BlueConnetHandler(BlueToothCallBack callBack) {
		this.callBack = callBack;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);

		switch (msg.what) {
		case CONNECT:
			thread = new BlueConnectThread(this, mac);
			thread.start();
			break;

		case CONNECT_FAIL:
			callBack.connect(false);
			isConnect = false;
			break;

		case CONNECT_SUCCEED:
			callBack.connect(true);
			isConnect = true;
			break;

		case GET_DATA: // 从蓝牙里收到数据
			msgString = msgString +(String) msg.obj;
			removeMessages(GET_DATA0);
			sendEmptyMessageDelayed(GET_DATA0, 200);
			break;
			
		case GET_DATA0: // 发送给ui
			callBack.getDate(msgString);
			msgString = "";
			sendEmptyMessageDelayed(SEND, 800);
			break;

		case SEND:
			BlueConnectThread.sendDataProcess("ATI");
			break;
		}
	}

	/** 发送 */
	public void sendMsg() {
		if (isConnect) {
			this.sendEmptyMessageDelayed(SEND, 900);
		}
	}

	// 关闭连接
	public void stopBlue() {
		if (isConnect) {
			thread.stopBlue();
		}
	}

	public void setIsSend(boolean paramBoolean) {
		this.isSend = paramBoolean;
		if (!isSend)
			return;
		if (isConnect) {
			this.sendEmptyMessageDelayed(SEND, 1000);
		}
	}
}
