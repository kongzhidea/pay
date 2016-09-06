package com.kk.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlUtil {
    private static final Log logger = LogFactory.getLog(XmlUtil.class);

    public static Map<String, String> parseXml(InputStream inputStream) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            Element root = document.getRootElement();
            List<Element> elements = root.elements();

            for (Element e : elements) {
                map.put(e.getName(), e.getText());
            }

            return map;
        } catch (Exception e) {
            logger.error(e.getCause());
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error(e.getCause());
                return null;
            }
        }
    }

    public static JSONObject parseXmlJson(String xml) {
        try {
            SAXReader builder = new SAXReader();
            Document document = builder.read(new StringReader(xml));
            Element root = document.getRootElement();
            List<Element> elements = root.elements();
            JSONObject json = new JSONObject();

            for (Element e : elements) {
                if (e.isTextOnly()) {
                    json.put(e.getName(), e.getText());
                } else {
                    json.put(e.getName(), parseXmlJson(e));
                }
            }
            return json;
        } catch (Exception e) {
            logger.error(e.getCause());
            return null;
        }
    }

    public static JSONObject parseXmlJson(Element root) {
        try {
            List<Element> elements = root.elements();
            JSONObject json = new JSONObject();
            for (Element e : elements) {
                if (e.isTextOnly()) {
                    json.put(e.getName(), e.getText());
                } else {
                    json.put(e.getName(), parseXmlJson(e));
                }
            }
            return json;
        } catch (Exception e) {
            logger.error(e.getCause());
            return null;
        }
    }

    public static Map<String, String> parseXml(String xml) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            SAXReader builder = new SAXReader();
            Document document = builder.read(new StringReader(xml));
            Element root = document.getRootElement();
            List<Element> elements = root.elements();

            for (Element e : elements) {
                map.put(e.getName(), e.getText());
            }

            return map;
        } catch (Exception e) {
            logger.error(e.getCause());
            return null;
        }
    }

    public static String toXml(Map<String, String> data) {
        Document document = DocumentHelper.createDocument();
        Element nodeElement = document.addElement("xml");
        for (String key : data.keySet()) {
            Element keyElement = nodeElement.addElement(key);
            keyElement.setText(data.get(key));
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputFormat format = new OutputFormat("   ", true, "UTF-8");
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(document);
            return out.toString("UTF-8");
        } catch (Exception ex) {
            logger.error(ex.getCause());
        }
        return null;
    }


    public static String toXml(Map<String, String> headData, Map<String, String> bodyData) {
        Document document = DocumentHelper.createDocument();
        Element message = document.addElement("message");
        Element head = message.addElement("head");
        for (String key : headData.keySet()) {
            Element keyElement = head.addElement(key);
            keyElement.setText(headData.get(key));
        }
        Element body = message.addElement("body");
        for (String key : bodyData.keySet()) {
            Element keyElement = body.addElement(key);
            keyElement.setText(bodyData.get(key));
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputFormat format = new OutputFormat("", true, "UTF-8");
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(document);
            return out.toString("UTF-8");
        } catch (Exception ex) {
            logger.error(ex.getCause());
        }
        return null;
    }


}
