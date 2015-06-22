package com.familyControl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.util.Xml;

import com.CommonData.MySDcardPath;
import com.VO.FamilyInfo;

public class FamilyListXML extends Activity {

	private MySDcardPath SDpath = new MySDcardPath();
	private String PATH = null;

	public FamilyListXML() {
		PATH = SDpath.getFamliyListXMLPath();
	}

	public List<FamilyInfo> GetFamilyList() {
		List<FamilyInfo> famliylist = new ArrayList<FamilyInfo>();
		try {
			File file = new File(PATH);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(file);

			Element element = document.getDocumentElement();
			NodeList FamilyInfos = element.getElementsByTagName("FamilyInfo");
			for (int i = 0; i < FamilyInfos.getLength(); i++) {
				Element familyInfoElement = (Element) FamilyInfos.item(i);
				FamilyInfo famliyInfo = new FamilyInfo();

				NodeList childNodes = familyInfoElement.getChildNodes();
				for (int j = 0; j < childNodes.getLength(); j++) {
					if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
						if ("Name".equals(childNodes.item(j).getNodeName())) {
							famliyInfo.setName(childNodes.item(j)
									.getFirstChild().getNodeValue());
						}
						if ("Phone".equals(childNodes.item(j).getNodeName())) {
							famliyInfo.setPhone(childNodes.item(j)
									.getFirstChild().getNodeValue());
						}
					}
				}
				famliylist.add(famliyInfo);
			}
		} catch (Exception e) {
			System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaa");
		}
		return famliylist;
	}

	public void WriteToXML(List<FamilyInfo> familyList) {
		File file = new File(PATH);
		try {
			file.createNewFile();
		} catch (IOException e) {
			System.out.println("exception in createNewFile() method");
			e.printStackTrace();
		}

		FileOutputStream fileos = null;
		try {
			fileos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("can't create FileOutputStream");
		}

		XmlSerializer serializer = Xml.newSerializer();
		try {
			serializer.setOutput(fileos, "utf-8");
			serializer.startDocument(null, true);
			serializer.startTag(null, "FamilyInfos");
			for (int i = 0; i < familyList.size(); i++) {
				serializer.startTag(null, "FamilyInfo");

				serializer.startTag(null, "Name");
				serializer.text(familyList.get(i).getName());
				serializer.endTag(null, "Name");

				serializer.startTag(null, "Phone");
				serializer.text(familyList.get(i).getPhone());
				serializer.endTag(null, "Phone");

				serializer.endTag(null, "FamilyInfo");
			}

			serializer.endTag(null, "FamilyInfos");
			serializer.endDocument();
			serializer.flush();
			fileos.close();
			System.out.println("创建XML成功");
		} catch (Exception e) {
		}
	}

	// 删除好友
	public void DeleteFriend(String username) {
		List<FamilyInfo> familyList = new ArrayList<FamilyInfo>();
		familyList = GetFamilyList();
		for (int i = 0; i < familyList.size(); i++) {
			if (familyList.get(i).getName().equals(username)) {
				familyList.remove(i);
			}
		}
		WriteToXML(familyList);
	}

	public void AddFriend(FamilyInfo newinfo) {
		List<FamilyInfo> familyList = new ArrayList<FamilyInfo>();
		familyList = GetFamilyList();
		familyList.add(newinfo);
		WriteToXML(familyList);
	}
}
