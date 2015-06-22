package com.ManageDevice;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.VO.DeviceInfo;

public class ChangeDeviceXMLInfo {

	private String DeviceInfoXMLPath = null;
	private String DeviceBlueMac = null;
	private DeviceInfo device = new DeviceInfo();

	public ChangeDeviceXMLInfo(DeviceInfo device) {
		// TODO Auto-generated constructor stub
		DeviceInfoXMLPath = "/sdcard/MyDeviceInfo.xml";
		this.device = device;
		DeviceBlueMac = device.getDeviceBlueMac();
	}

	public void ChangeData() {
		try {
			File DeviceFile = new File(DeviceInfoXMLPath);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(DeviceFile);
			// 合并相邻的 Text 节点并删除空的 Text 节点。
			document.normalize();
			Node root = document.getDocumentElement();
			if (root.hasChildNodes()) {
				NodeList ftpnodes = root.getChildNodes();
				for (int i = 0; i < ftpnodes.getLength(); i++) {

					NodeList ftplist = ftpnodes.item(i).getChildNodes();

					for (int k = 0; k < ftplist.getLength(); k++) {
						Node subnode = ftplist.item(k);

						if (subnode.getNodeType() == Node.ELEMENT_NODE
								&& subnode.getNodeName() == "DeviceBlueMac") {
							if (subnode.getFirstChild().getNodeValue()
									.equals(DeviceBlueMac)) {
								// 找到需要修改的ftplist
								for (int j = 0; j < ftplist.getLength(); j++) {
									Node modNode = ftplist.item(j);
									if (modNode.getNodeType() == Node.ELEMENT_NODE
											&& modNode.getNodeName() == "DeviceName") {
										modNode.getFirstChild().setNodeValue(
												device.getDeviceName());
									} else if (modNode.getNodeType() == Node.ELEMENT_NODE
											&& modNode.getNodeName() == "Cartype") {
										modNode.getFirstChild().setNodeValue(
												device.getCartype());
									} else if (modNode.getNodeType() == Node.ELEMENT_NODE
											&& modNode.getNodeName() == "CarNumber") {
										modNode.getFirstChild().setNodeValue(
												device.getCarNumber());
									}
								}
							}
						}
					}

				}
			}

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();

			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(DeviceInfoXMLPath));
			transformer.transform(source, result);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
