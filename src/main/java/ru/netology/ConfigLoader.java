package ru.netology;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class ConfigLoader {
  private final File file;

  public boolean loadEnabled = false;
  public String loadFileName = "basket.json";
  public BasketType loadType = BasketType.JSON;

  public boolean saveEnabled = true;
  public String saveFileName = "basket.json";
  public BasketType saveType = BasketType.JSON;

  public boolean logEnabled = true;
  public String logFileName = "client.csv";

  public ConfigLoader(String fileName) {
    this.file = new File(fileName);
    loadConfig();
  }

  public void loadConfig() {
    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = builder.parse(file);
      Node root = doc.getDocumentElement();

      NodeList nodeList = root.getChildNodes();
      for(int i = 0; i < nodeList.getLength(); i++) {
        if(nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
        String nodeName = nodeList.item(i).getNodeName();
        switch (nodeName) {
          case "load" -> {
            NodeList nodeList1 = nodeList.item(i).getChildNodes();
            for (int x = 0; x < nodeList1.getLength(); x++) {
              if (nodeList1.item(x).getNodeType() != Node.ELEMENT_NODE) continue;
              String nodeName1 = nodeList1.item(x).getNodeName();
              String value = nodeList1.item(x).getChildNodes().item(0).getTextContent();
              switch (nodeName1) {
                case "enabled" -> loadEnabled = Boolean.parseBoolean(value);
                case "fileName" -> loadFileName = value;
                case "format" -> loadType = BasketType.getTypeFromString(value);
              }
            }
          }
          case "save" -> {
            NodeList nodeList1 = nodeList.item(i).getChildNodes();
            for (int x = 0; x < nodeList1.getLength(); x++) {
              if (nodeList1.item(x).getNodeType() != Node.ELEMENT_NODE) continue;
              String nodeName1 = nodeList1.item(x).getNodeName();
              String value = nodeList1.item(x).getChildNodes().item(0).getTextContent();
              switch (nodeName1) {
                case "enabled" -> saveEnabled = Boolean.parseBoolean(value);
                case "fileName" -> saveFileName = value;
                case "format" -> saveType = BasketType.getTypeFromString(value);
              }
            }
          }
          case "log" -> {
            NodeList nodeList1 = nodeList.item(i).getChildNodes();
            for (int x = 0; x < nodeList1.getLength(); x++) {
              if (nodeList1.item(x).getNodeType() != Node.ELEMENT_NODE) continue;
              String nodeName1 = nodeList1.item(x).getNodeName();
              String value = nodeList1.item(x).getChildNodes().item(0).getTextContent();
              switch (nodeName1) {
                case "enabled" -> logEnabled = Boolean.parseBoolean(value);
                case "fileName" -> logFileName = value;
              }
            }
          }
        }
      }
    } catch (ParserConfigurationException | IOException | SAXException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return "configObject{" +
      "loadEnabled='" + loadEnabled + '\'' +
      ", loadFileName='" + loadFileName + '\'' +
      ", loadType='" + loadType + '\'' +
      ", saveEnabled='" + saveEnabled + '\'' +
      ", saveFileName='" + saveFileName + '\'' +
      ", saveType='" + saveType + '\'' +
      ", logEnabled='" + logEnabled + '\'' +
      ", logFileName='" + logFileName + '\'' +
      '}';
  }
}
