package com.ConnectServer;

public interface ServerConnectCallBack {
	/** 服务器连接状态 */
	public void connect(boolean state);

	/** 返回结果 */
	public void ConnectResult(String result);
}
