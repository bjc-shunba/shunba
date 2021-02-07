package com.baidu.shunba.common.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlUtils {
	private static final Log logger = LogFactory.getLog(XmlUtils.class);
	
	public static String toUtf8Xml(Map<String, String> paramsMap) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml encoding=\"UTF-8\">");
		for (String pa : paramsMap.keySet()) {
			if (pa.equals("body")) {
				sb.append("<" + pa + "><![CDATA[");
				sb.append(paramsMap.get(pa));
				sb.append("]]></" + pa + ">");
			} else {
				sb.append("<" + pa + ">");
				sb.append(paramsMap.get(pa));
				sb.append("</" + pa + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}
	
	public static org.dom4j.Document parseText(String srcTxt) throws Exception {
		org.dom4j.Document result = null;

		String text = srcTxt.replaceAll("\\<xsd:", "<").replaceAll("</xsd:", "</").replaceAll("xmlns:xsd=\"\"", "");

		SAXReader saxReader = new SAXReader();
		saxReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		saxReader.setFeature("http://xml.org/sax/features/external-general-entities", false);
		saxReader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

		String encoding = getEncoding(text);

		InputSource source = new InputSource(new StringReader(text));
		source.setEncoding(encoding);

		result = saxReader.read(source);
		if (result.getXMLEncoding() == null) {
			result.setXMLEncoding(encoding);
		}
		return result;
	}

	private static String getEncoding(String text) {
		String result = null;

		String xml = text.trim();
		if (xml.startsWith("<?xml")) {
			int end = xml.indexOf("?>");
			String sub = xml.substring(0, end);
			StringTokenizer tokens = new StringTokenizer(sub, " =\"'");
			while (tokens.hasMoreTokens()) {
				String token = tokens.nextToken();
				if ("encoding".equals(token)) {
					if (!tokens.hasMoreTokens()) {
						break;
					}
					result = tokens.nextToken();
					break;
				}
			}
		}
		return result;
	}

	public static Map<String, Object> simpleXmlToMap(String xmlString) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			String FEATURE = null;
			// This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
			// Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
			FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
			dbf.setFeature(FEATURE, true);
			
			// If you can't completely disable DTDs, then at least do the following:
			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
			// JDK7+ - http://xml.org/sax/features/external-general-entities 
			FEATURE = "http://xml.org/sax/features/external-general-entities";
			dbf.setFeature(FEATURE, false);
			
			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
			// JDK7+ - http://xml.org/sax/features/external-parameter-entities 
			FEATURE = "http://xml.org/sax/features/external-parameter-entities";
			dbf.setFeature(FEATURE, false);
			
			// Disable external DTDs as well
			FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
			dbf.setFeature(FEATURE, false);
			
			// and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
			dbf.setXIncludeAware(false);
			dbf.setExpandEntityReferences(false);
			
			// And, per Timothy Morgan: "If for some reason support for inline DOCTYPEs are a requirement, then 
			// ensure the entity settings are disabled (as shown above) and beware that SSRF attacks
			// (http://cwe.mitre.org/data/definitions/918.html) and denial 
			// of service attacks (such as billion laughs or decompression bombs via "jar:") are a risk."
			
			// remaining parser logic
			DocumentBuilder safebuilder = dbf.newDocumentBuilder();
			
			Document doc = safebuilder.parse(new ByteArrayInputStream(xmlString.getBytes()));
            
			Element root = doc.getDocumentElement();
			root.normalize();
			
            NodeList nodes = root.getChildNodes();
		
            for (int i = 0; i < nodes.getLength(); i++) {
            		Node node = nodes.item(i);
            		if (node.getNodeType() != 1) {
            			continue;
            		}
            		map.put(node.getNodeName(), node.getTextContent());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("XmlUtils::(" + xmlString + ")", e);
		}
		return map;
	}

	
	public static Map<String, Object> simpleXmlToMap(InputStream inputStream) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			String FEATURE = null;
			// This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
			// Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
			FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
			dbf.setFeature(FEATURE, true);
			
			// If you can't completely disable DTDs, then at least do the following:
			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
			// JDK7+ - http://xml.org/sax/features/external-general-entities 
			FEATURE = "http://xml.org/sax/features/external-general-entities";
			dbf.setFeature(FEATURE, false);
			
			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
			// JDK7+ - http://xml.org/sax/features/external-parameter-entities 
			FEATURE = "http://xml.org/sax/features/external-parameter-entities";
			dbf.setFeature(FEATURE, false);
			
			// Disable external DTDs as well
			FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
			dbf.setFeature(FEATURE, false);
			
			// and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
			dbf.setXIncludeAware(false);
			dbf.setExpandEntityReferences(false);
			
			// And, per Timothy Morgan: "If for some reason support for inline DOCTYPEs are a requirement, then 
			// ensure the entity settings are disabled (as shown above) and beware that SSRF attacks
			// (http://cwe.mitre.org/data/definitions/918.html) and denial 
			// of service attacks (such as billion laughs or decompression bombs via "jar:") are a risk."
			
			// remaining parser logic
			DocumentBuilder safebuilder = dbf.newDocumentBuilder();
			
			Document doc = safebuilder.parse(inputStream);
            
			Element root = doc.getDocumentElement();
			root.normalize();
			
            NodeList nodes = root.getChildNodes();
		
            for (int i = 0; i < nodes.getLength(); i++) {
            		Node node = nodes.item(i);
            		if (node.getNodeType() != 1) {
            			continue;
            		}
            		map.put(node.getNodeName(), node.getTextContent());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("XmlUtils::" + e);
		}
		return map;
	}
	

	public static void mainTest(String[] args) throws Exception {
		String xml = "<xml><appid><![CDATA[wx2421b1c4370ec43b]]></appid><attach><![CDATA[支付测试]]></attach><bank_type><![CDATA[CFT]]></bank_type><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[10000100]]></mch_id><nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str><openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>\r<out_trade_no><![CDATA[1409811653]]></out_trade_no>\r<result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign><sub_mch_id><![CDATA[10000100]]></sub_mch_id><time_end><![CDATA[20140903131540]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id></xml>";
		//xml = "<root>" + "  <reportChart>" + "    <ReportChartid>1111111111</ReportChartid>" + "    <DataType>Chart</DataType>" + "    <ChartInQdex> </ChartInQdex>" + "    <ChartType>Column2D.swf</ChartType>" + "    <ReportCaption></ReportCaption>" + "    <IsMultiSeries></IsMultiSeries>    <ReportUnits></ReportUnits>" + "    <FromDB>false</FromDB>" + "    <OrcSQL>select * from aaa</OrcSQL>" + "  </reportChart>" + "  <reportChart>" + "    <ReportChartid>222222222</ReportChartid>" + "    <DataType>Chart</DataType>" + "    <ChartInQdex> </ChartInQdex>    <ChartType>Column2D.swf</ChartType>" + "    <ReportCaption></ReportCaption>" + "    <IsMultiSeries></IsMultiSeries>" + "    <ReportUnits></ReportUnits>" + "    <FromDB>false</FromDB>" + "    <OrcSQL>select</OrcSQL>" + "  </reportChart>" + "</root>";

		String text = xml.replaceAll("\\<xsd:", "<").replaceAll("</xsd:", "</").replaceAll("xmlns:xsd=\"\"", "");
		System.err.println(text);

		// System.err.println(xml2map(xml));
		System.out.println(simpleXmlToMap(xml).size() + "::" + simpleXmlToMap(xml));
		System.out.println(simpleXmlToMap(xml).get("out_trade_no"));
		// xml = "<?xml version=\"1.0\"
		// encoding=\"UTF-8\"?><root><classs><id>001</id><name>st001</name><student><id>0001</id><name><first>xue</first><last>bo</last></name></student></classs><classs><id>001</id><name>st001</name><student><id>0001</id><name><first>xue</first><last>bo</last></name></student></classs></root>";
		// System.err.println(xml2map(xml));
		// System.out.println(xmlToMap(xml));
	}
}
