package com.howtodoinjava.demo.jdom2;

import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.DOMBuilder;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.StAXEventBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.xml.sax.SAXException;
 
@SuppressWarnings("unused")
public class ReadXMLDemo 
{   
    public static void main(String[] args) 
    {
        String xmlFile = "employees.xml";
        Document document = getSAXParsedDocument(xmlFile);
         
        /**Read Document Content*/
         
        Element rootNode = document.getRootElement();
        System.out.println("Root Element :: " + rootNode.getName());
         
        System.out.println("\n=================================\n");
         
        /**Read Employee Content*/
         
        rootNode.getChildren("employee").forEach( ReadXMLDemo::readEmployeeNode );
         
        System.out.println("\n=================================\n");
         
        readByXPath(document);
    }
     
    private static void readEmployeeNode(Element employeeNode) 
    {
        //Employee Id
        System.out.println("Id : " + employeeNode.getAttributeValue("id"));
         
        //First Name
        System.out.println("FirstName : " + employeeNode.getChildText("firstName"));
         
        //Last Name
        System.out.println("LastName : " + employeeNode.getChildText("lastName"));
         
        //Country
        System.out.println("country : " + employeeNode.getChild("country").getText());
         
        /**Read Department Content*/
        employeeNode.getChildren("department").forEach( ReadXMLDemo::readDepartmentNode );
    }
     
    private static void readDepartmentNode(Element deptNode) 
    {
        //Department Id
        System.out.println("Department Id : " + deptNode.getAttributeValue("id"));
         
        //Department Name
        System.out.println("Department Name : " + deptNode.getChildText("name"));
    }
     
    private static void readByXPath(Document document) 
    {
        //Read employee ids
        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression<Attribute> xPathA = xpfac.compile("//employees/employee/@id", Filters.attribute());
        for (Attribute att : xPathA.evaluate(document)) 
        {
            System.out.println("Employee Ids :: " + att.getValue());
        }
         
        XPathExpression<Element> xPathN = xpfac.compile("//employees/employee/firstName", Filters.element());
        for (Element element : xPathN.evaluate(document)) 
        {
            System.out.println("Employee First Name :: " + element.getValue());
        }
    }
     
    private static Document getSAXParsedDocument(final String fileName) 
    {
        SAXBuilder builder = new SAXBuilder(); 
        Document document = null;
        try
        {
            document = builder.build(fileName);
        } 
        catch (JDOMException | IOException e) 
        {
            e.printStackTrace();
        }
        return document;
    }
     
    private static Document getStAXParsedDocument(final String fileName) 
    {
         
        Document document = null;
        try
        {
            XMLInputFactory factory = XMLInputFactory.newFactory();
            XMLEventReader reader = factory.createXMLEventReader(new FileReader(fileName));
            StAXEventBuilder builder = new StAXEventBuilder(); 
            document = builder.build(reader);
        } 
        catch (JDOMException | IOException | XMLStreamException e) 
        {
            e.printStackTrace();
        }
        return document;
    }
     
    private static Document getDOMParsedDocument(final String fileName) 
    {
        Document document = null;
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //If want to make namespace aware.
            //factory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            org.w3c.dom.Document w3cDocument = documentBuilder.parse(fileName);
            document = new DOMBuilder().build(w3cDocument);
        } 
        catch (IOException | SAXException | ParserConfigurationException e) 
        {
            e.printStackTrace();
        }
        return document;
    }
     
    /*private static String readFileContent(String filePath) 
    {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) 
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }*/
}