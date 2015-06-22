package com.ManageDevice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import com.VO.DeviceInfo;

import android.util.Xml;

public class WriteDeviceInfoToXML {
	private String DeviceInfoXMLPath = null;
	private DeviceInfo device = new DeviceInfo();
	private List<DeviceInfo> list = new ArrayList<DeviceInfo>();

	public WriteDeviceInfoToXML(DeviceInfo device) {
		// TODO Auto-generated constructor stub
		DeviceInfoXMLPath = "/sdcard/MyDeviceInfo.xml";
		this.device = device;
	}

	public void ToXML() {
		File file = new File(DeviceInfoXMLPath);
		if (file.exists()) {
			GetDeviceInfoFromXML xml = new GetDeviceInfoFromXML();
			list = xml.getDeviceList();
		}
		list.add(device);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("exception in createNewFile() method");
		}
		FileOutputStream fileos = null;
		try {
			fileos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("can't create FileOutputStream");
		}

		try {
			XmlSerializer serializer = Xml.newSerializer();
			serializer.setOutput(fileos, "utf-8");
			serializer.startDocument(null, true);
			serializer.startTag(null, "DeviceInfos");
			for (int i = 0; i < list.size(); i++) {
				serializer.startTag(null, "DeviceInfo");

				serializer.startTag(null, "DeviceName");
				serializer.text(list.get(i).getDeviceName());
				serializer.endTag(null, "DeviceName");

				serializer.startTag(null, "DeviceBlueMac");
				serializer.text(list.get(i).getDeviceBlueMac());
				serializer.endTag(null, "DeviceBlueMac");

				serializer.startTag(null, "Cartype");
				serializer.text(list.get(i).getCartype());
				serializer.endTag(null, "Cartype");

				serializer.startTag(null, "CarNumber");
				serializer.text(list.get(i).getCarNumber());
				serializer.endTag(null, "CarNumber");

				serializer.endTag(null, "DeviceInfo");
			}
			serializer.endTag(null, "DeviceInfos");
			serializer.endDocument();
			serializer.flush();
			fileos.close();
			System.out.println("创建XML成功");
		} catch (Exception e) {
		}

	}
}
