package com.CommonData;

public class MyURLPath {

	// 用于获得行驶记录
	public final String DriveRecordURLPath = "http://192.168.191.1:8080/SafeDriveServer/servlet/DriveRecordServlet";
	// 用户获取危险信息
	public final String GetDangerURLPath = "http://192.168.191.1:8080/SafeDriveServer/servlet/GetDangerServlet";
	// 用于获取最大记录数
	public final String GetMaxRecordIdURLPath = "http://192.168.191.1:8080/SafeDriveServer/servlet/GetMaxRecordIdServlet";
	// 用于获取坐标集
	public final String GetPositionListURLPath = "http://192.168.191.1:8080/SafeDriveServer/servlet/GetPositionListServlet";
	// 用于上传行驶记录
	public final String UploadDriveInfoURLPath = "http://192.168.191.1:8080/SafeDriveServer/servlet/ReceiveDriveInfoServlet";
	// 用于上传坐标集
	public final String UploadPositionInfoURLPath = "http://192.168.191.1:8080/SafeDriveServer/servlet/ReceivePositionInfoServlet";
	// 用于用户管理
	public final String UserManageURLPath = "http://192.168.191.1:8080/SafeDriveServer/servlet/UserManageServlet";
	// 用于用户意见反馈
	public final String UserBackPath = "http://192.168.191.1:8080/SafeDriveServer/servlet/ReceiveAdviceServlet";

	public String getDriveRecordURLPath() {
		return DriveRecordURLPath;
	}

	public String getGetDangerURLPath() {
		return GetDangerURLPath;
	}

	public String getGetMaxRecordIdURLPath() {
		return GetMaxRecordIdURLPath;
	}

	public String getGetPositionListURLPath() {
		return GetPositionListURLPath;
	}

	public String getUploadDriveInfoURLPath() {
		return UploadDriveInfoURLPath;
	}

	public String getUploadPositionInfoURLPath() {
		return UploadPositionInfoURLPath;
	}

	public String getUserManageURLPath() {
		return UserManageURLPath;
	}

	public String getUserBackPath() {
		return UserBackPath;
	}

}
