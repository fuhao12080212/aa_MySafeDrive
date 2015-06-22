package com.VO;

public class DeviceInfo {

	// 设备名称
	private String DeviceName;
	// 设备MAC地址
	private String DeviceBlueMac;
	// 设备车型
	private String Cartype;
	// 设备车牌号
	private String CarNumber;

	public DeviceInfo() {
		// TODO Auto-generated constructor stub
	}

	public String getDeviceName() {
		return DeviceName;
	}

	public void setDeviceName(String deviceName) {
		DeviceName = deviceName;
	}

	public String getDeviceBlueMac() {
		return DeviceBlueMac;
	}

	public void setDeviceBlueMac(String deviceBlueMac) {
		DeviceBlueMac = deviceBlueMac;
	}

	public String getCartype() {
		return Cartype;
	}

	public void setCartype(String cartype) {
		Cartype = cartype;
	}

	public String getCarNumber() {
		return CarNumber;
	}

	public void setCarNumber(String carNumber) {
		CarNumber = carNumber;
	}

}
