package com.ManageDevice;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.VO.DeviceInfo;

public class GetTestDeviceInfoXML {

	private String TestDeviceInfoXMLPath = null;
	private DeviceInfo deviceInfo = new DeviceInfo();

	public GetTestDeviceInfoXML() {
		// TODO Auto-generated constructor stub
		TestDeviceInfoXMLPath = "/sdcard/CurrentDeviceInfo.xml";
	}

	public DeviceInfo getDeviceInfo() {
		File DeviceFile = new File(TestDeviceInfoXMLPath);
		if (DeviceFile.exists()) {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(DeviceFile);
				Element element = document.getDocumentElement();

				NodeList DeviceInfos = element
						.getElementsByTagName("DeviceInfo");
				for (int i = 0; i < DeviceInfos.getLength(); i++) {
					Element deviceDataElement = (Element) DeviceInfos.item(i);
					DeviceInfo device = new DeviceInfo();

					NodeList childNodes = deviceDataElement.getChildNodes();

					for (int j = 0; j < childNodes.getLength(); j++) {
						if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
							if ("DeviceName".equals(childNodes.item(j)
									.getNodeName())) {
								device.setDeviceName(childNodes.item(j)
										.getFirstChild().getNodeValue());
							}
							if ("DeviceBlueMac".equals(childNodes.item(j)
									.getNodeName())) {
								device.setDeviceBlueMac(childNodes.item(j)
										.getFirstChild().getNodeValue());
							}
							if ("Cartype".equals(childNodes.item(j)
									.getNodeName())) {
								device.setCartype(childNodes.item(j)
										.getFirstChild().getNodeValue());
							}
							if ("CarNumber".equals(childNodes.item(j)
									.getNodeName())) {
								device.setCarNumber(childNodes.item(j)
										.getFirstChild().getNodeValue());
							}
						}
					}
					deviceInfo = device;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			System.out.println("---》》》First Login   设备信息不存在");
			return null;
		}
		return deviceInfo;
	}
}
