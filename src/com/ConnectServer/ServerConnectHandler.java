package com.ConnectServer;

import java.util.ArrayList;
import java.util.List;
import com.VO.DriveRecordInfo;
import com.VO.Position;
import com.VO.User;
import android.os.Handler;
import android.os.Message;

public class ServerConnectHandler extends Handler {

	private ServerConnectCallBack callBack;
	private ServerConnectThread thread;
	/*
	 * 各个功能需要的参数 PATH------连接服务器 user&&Method------用户管理（登陆、注册、修改密码）
	 * username------获取最大记录值&&获取记录集 dangerInfo------上传危险信息
	 * positionList------上传经纬度集 UserBackText------用户反馈的意见
	 */
	private String PATH;
	private User user;
	private String UserManageMethod;
	private String username;
	private DriveRecordInfo dangerInfo;
	private String MaxDriveRecord;
	private String Number;
	private List<Position> positionList = new ArrayList<Position>();
	private String UserBackText;
	private String Time;
	// 功能参数
	public static final int CONNECT = 0x11110;
	public static final int CONNECT_FAIL = 0x11111;
	public static final int CONNECT_SUCCEED = 0x11112;

	public static final int USER_MANAGE = 0x11114;
	public static final int RESULT_USER_MANAGE = 0x11115;

	public static final int GET_USER_MAX_RECORD = 0x11116;
	public static final int RESULT_GET_USER_MAX_RECORD = 0x11117;

	public static final int GET_DRIVE_RECORD = 0x11118;
	public static final int RESULT_GET_DRIVE_RECORD = 0x11119;

	public static final int GET_POSITION = 0x1111a;
	public static final int RESULT_GET_POSITION = 0x11117b;

	public static final int UPLOAD_POSITION = 0x1111c;
	public static final int RESULT_UPLOAD_POSITION = 0x1111d;

	public static final int UPLOAD_DANGER = 0x11120;
	public static final int RESULT_UPLOAD_DANGER = 0x11121;

	public static final int UPLOAD_USERADVICE = 0x11122;
	public static final int RESULT_UPLOAD_USERADVICE = 0x11123;
	// 获得的返回数据
	String Result = "";

	public ServerConnectHandler(String PATH) {
		this.PATH = PATH;
	}

	// TODO 构造函数----》》 用于用户管理
	public ServerConnectHandler(ServerConnectCallBack callBack, User user,
			String Method, String PATH) {
		this.callBack = callBack;
		this.user = user;
		this.UserManageMethod = Method;
		this.PATH = PATH;
	}

	// TODO 构造函数----》》 用于获取最大记录值&&获取记录集
	public ServerConnectHandler(ServerConnectCallBack callBack,
			String username, String PATH) {
		this.callBack = callBack;
		this.username = username;
		this.PATH = PATH;
	}

	// TODO 构造函数----》》 用于获取位置坐标集
	public ServerConnectHandler(ServerConnectCallBack callBack,
			String username, String Number, String PATH) {
		this.callBack = callBack;
		this.username = username;
		this.Number = Number;
		this.PATH = PATH;
	}

	// TODO 构造函数----》》 上传危险信息
	public ServerConnectHandler(ServerConnectCallBack callBack,
			DriveRecordInfo dangerInfo, String PATH) {
		this.callBack = callBack;
		this.dangerInfo = dangerInfo;
		this.PATH = PATH;
	}

	// TODO 构造函数----》》 上传经纬度集
	public ServerConnectHandler(ServerConnectCallBack callBack,
			List<Position> positionList, String PATH, String MaxDriveRecord) {
		this.callBack = callBack;
		this.positionList = positionList;
		this.PATH = PATH;
		this.MaxDriveRecord = MaxDriveRecord;
	}

	// TODO 构造函数----》》上传用户反馈
	public ServerConnectHandler(ServerConnectCallBack callBack,String username, String UserBackText,
			String Time, String PATH) {
		this.callBack = callBack;
		this.username = username;
		this.UserBackText = UserBackText;
		this.Time = Time;
		this.PATH = PATH;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);

		switch (msg.what) {
		case CONNECT:
			thread = new ServerConnectThread(PATH);
			thread.start();
			break;
		case CONNECT_FAIL:
			callBack.connect(false);
			break;
		case CONNECT_SUCCEED:
			callBack.connect(true);
			break;
		case USER_MANAGE:
			thread = new ServerConnectThread(this, user, UserManageMethod,
					PATH, "USER_MANAGE");
			thread.start();
			break;
		case RESULT_USER_MANAGE:
			Result = String.valueOf(msg.obj);
			callBack.ConnectResult(Result);
			Result = "";
			break;
		case GET_USER_MAX_RECORD:
			thread = new ServerConnectThread(this, username, PATH,
					"GET_USER_MAX_RECORD");
			thread.start();
			break;
		case RESULT_GET_USER_MAX_RECORD:
			Result = String.valueOf(msg.obj);
			callBack.ConnectResult(Result);
			Result = "";
			break;
		case GET_DRIVE_RECORD:
			thread = new ServerConnectThread(this, username, PATH,
					"GET_DRIVE_RECORD");
			thread.start();
			break;
		case RESULT_GET_DRIVE_RECORD:
			Result = String.valueOf(msg.obj);
			callBack.ConnectResult(Result);
			Result = "";
			break;
		case GET_POSITION:
			thread = new ServerConnectThread(this, username, Number, PATH,
					"GET_POSITION");
			thread.start();
			break;
		case RESULT_GET_POSITION:
			Result = String.valueOf(msg.obj);
			callBack.ConnectResult(Result);
			break;
		case UPLOAD_POSITION:
			thread = new ServerConnectThread(this, positionList, username,
					PATH, MaxDriveRecord, "UPLOAD_POSITION");
			thread.start();
			break;
		case RESULT_UPLOAD_POSITION:
			Result = String.valueOf(msg.obj);
			callBack.ConnectResult(Result);
			break;
		case UPLOAD_DANGER:
			thread = new ServerConnectThread(this, dangerInfo, PATH,
					"UPLOAD_DANGER");
			thread.start();
			break;
		case RESULT_UPLOAD_DANGER:
			Result = String.valueOf(msg.obj);
			callBack.ConnectResult(Result);
			break;
		case UPLOAD_USERADVICE:
			thread = new ServerConnectThread(UserBackText, this, username,Time,
					PATH, "UPLOAD_USERADVICE");
			thread.start();
			break;
		case RESULT_UPLOAD_USERADVICE:
			Result = String.valueOf(msg.obj);
			callBack.ConnectResult(Result);
			break;
		default:
			break;
		}
	}
}
