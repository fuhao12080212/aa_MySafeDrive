package com.ManageDevice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.VO.DeviceInfo;

public class GetDeviceInfoFromXML {
	private String XMLPath = null;
	private List<DeviceInfo> list = new ArrayList<DeviceInfo>();

	public GetDeviceInfoFromXML() {
		// TODO Auto-generated constructor stub
		XMLPath = "/sdcard/MyDeviceInfo.xml";
	}

	public List<DeviceInfo> getDeviceList() {
		System.out.println("->>>>>>>test");
		try {
			File DeviceFile = new File(XMLPath);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(DeviceFile);
			Element element = document.getDocumentElement();
			NodeList DeviceInfos = element.getElementsByTagName("DeviceInfo");
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
						if ("Cartype".equals(childNodes.item(j).getNodeName())) {
							device.setCartype(childNodes.item(j)
									.getFirstChild().getNodeValue());
						}
						if ("CarNumber"
								.equals(childNodes.item(j).getNodeName())) {
							device.setCarNumber(childNodes.item(j)
									.getFirstChild().getNodeValue());
						}
					}
				}
				list.add(device);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaa");
		}
		return list;
	}

}
