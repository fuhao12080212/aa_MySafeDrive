package com.ManageDevice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.VO.DeviceInfo;

import android.util.Xml;

public class CurrentDeviceTOXML {

	private String CurrentDeviceInfoXMLPath = null;
	private DeviceInfo device = new DeviceInfo();

	
	public CurrentDeviceTOXML(DeviceInfo device) {
		// TODO Auto-generated constructor stub
		CurrentDeviceInfoXMLPath = "/sdcard/CurrentDeviceInfo.xml";
		this.device = device;
	}

	public void ToXML() {
		File file = new File(CurrentDeviceInfoXMLPath);
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

			serializer.startTag(null, "DeviceInfo");
			
			serializer.startTag(null, "DeviceName");
			serializer.text(device.getDeviceName());
			serializer.endTag(null, "DeviceName");

			serializer.startTag(null, "DeviceBlueMac");
			serializer.text(device.getDeviceBlueMac());
			serializer.endTag(null, "DeviceBlueMac");

			serializer.startTag(null, "Cartype");
			serializer.text(device.getCartype());
			serializer.endTag(null, "Cartype");

			serializer.startTag(null, "CarNumber");
			serializer.text(device.getCarNumber());
			serializer.endTag(null, "CarNumber");

			serializer.endTag(null, "DeviceInfo");

			serializer.endTag(null, "DeviceInfos");
			serializer.endDocument();
			serializer.flush();
			fileos.close();
			System.out.println("创建XML成功");
		} catch (Exception e) {
		}

	}
}
