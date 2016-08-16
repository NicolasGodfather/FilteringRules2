package com.filtering;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.TreeMap;

/**
 * Realization Main
 * Use SAXParser cause app will use XML files more than 100MB
 *
 * @author Nicolas Asinovich.
 */
class Main {

    private FilteringHandler handler = new FilteringHandler();
    private static String filePathOut = "src/main/resources/output.xml";

    public static void main (String[] args) {
        new Main().parseXML("src/main/resources/input.xml");
    }

    /**
     * Read xml file
     * @param filePath
     */
    void parseXML (String filePath) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(filePath, handler);

            writeXML();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeXML () {
        try {
            OutputStream outputStream = new FileOutputStream(/*exists*/(filePathOut), true);
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(outputStream));
            TreeMap<String, Rule> data = handler.getDataRule();

            out.writeStartDocument("UTF-8", "1.0");
            out.writeCharacters("\n");
            // here line will be convert output-file
            out.writeDTD("<?xml-stylesheet type=\"text/xsl\" href=\"transform.xsl\"?>");
            out.writeCharacters("\n");

            out.writeStartElement("rules");
            out.writeCharacters("\n");

            for (String s : data.keySet()) {
                out.writeStartElement("rule");
                out.writeAttribute("name", s);
                out.writeAttribute("type", data.get(s).getRuleType().toString().toLowerCase());
                out.writeAttribute("weight", String.valueOf(data.get(s).getWeight()));
                out.writeEndElement();
                out.writeCharacters("\n");
            }
            out.writeEndElement();
            out.writeEndDocument();

            out.close();
            outputStream.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
