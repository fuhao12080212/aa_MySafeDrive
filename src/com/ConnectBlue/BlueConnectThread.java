package com.ConnectBlue;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

public class BlueConnectThread extends Thread {
	private Handler handler;
	private final UUID uuid = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private BluetoothDevice device;
	private BluetoothSocket socket;
	private String mac;
	private static DataOutputStream outputStream;

	public BlueConnectThread(Handler handler, String mac) {
		try {
			// 判断是不是有效蓝牙地址，如果地址无效IllegalArgumentException异常
			device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mac);
			// 更改socket信道
			socket = device.createRfcommSocketToServiceRecord(uuid);
		} catch (IllegalArgumentException e) {
		} catch (IOException e) {
		}
		this.handler = handler;
	}

	@Override
	public void run() {
		if (socket != null) {
			try {
				socket.connect();
				Message message = handler.obtainMessage(
						BlueConnetHandler.CONNECT_SUCCEED, mac);
				message.sendToTarget();
				runReadDataProcess(socket.getInputStream());
				outputStream = new DataOutputStream(new BufferedOutputStream(
						socket.getOutputStream()));
			} catch (IOException e) {
				Message message = handler
						.obtainMessage(BlueConnetHandler.CONNECT_FAIL);
				message.sendToTarget();
			}
		} else {
			Message message = handler
					.obtainMessage(BlueConnetHandler.CONNECT_FAIL);
			message.sendToTarget();
		}
	}

	// 接收
	private void runReadDataProcess(final InputStream inputStream) {
		new Thread() {
			public void run() {
				byte[] buffer = new byte[1024];
				int bytes;
				while (true) {
					try {
						bytes = inputStream.read(buffer); // //DEBUG
						String readMessage = new String(buffer, 0, bytes);
						buffer = new byte[1024];
						Message message = handler.obtainMessage(
								BlueConnetHandler.GET_DATA, readMessage);
						message.sendToTarget();
					} catch (IOException e) {
						break;
					}
				}
			}
		}.start();
	}

	// 发送
	public static void sendDataProcess(String data) {
		if (outputStream != null) {
			try {
				byte[] b = new byte[1];
				b[0] = 0x0D;
				String strInput = data + new String(b) + "0x0A";
				outputStream.write(strInput.getBytes());
				outputStream.flush();
			} catch (Exception e) {
			}
		}
	}

	public void close() {
		try {
			if (socket != null) {
				socket.close();
				outputStream.close();
			}
		} catch (Exception e) {
		}
	}

	// 关闭连接
	public void stopBlue() {
		if (socket == null) {
			return;
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
