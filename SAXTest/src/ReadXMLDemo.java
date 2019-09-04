import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

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
        String xmlFile = "_JLO0967_11_2019082113170254.xml";
        Document document = getSAXParsedDocument(xmlFile);
         
        /**Read Document Content*/
         
        Element rootNode = document.getRootElement();
       
        System.out.println("Root Element:\t\t\t" + rootNode.getName());        
        System.out.println("DB-Bereich:\t\t\t" + rootNode.getChildText("DB-Bereich"));
        System.out.println("Datei-Erstellungszeitpunkt:\t" + rootNode.getChildTextTrim("Datei-Erstellungszeitpunkt"));       
        rootNode.getChildren("UEVT_Daten").forEach(ReadXMLDemo::readUEVTNode); 
        
    }
     
    private static void readUEVTNode(Element UEVT_DatenNode)
    {
        System.out.println("\nUEVT_Daten:");
        System.out.println("id:\t\t\t\t" + UEVT_DatenNode.getAttributeValue("id")); 
        System.out.println("Zeitpunkt_UEVT-Aenderung:\t" + UEVT_DatenNode.getChildText("Zeitpunkt_UEVT-Aenderung"));       
        
        //Abstieg in die Stiftinfo
        UEVT_DatenNode.getChildren("Stift-Info").forEach( ReadXMLDemo::readStiftInfoNode );
    }
     
    private static void readStiftInfoNode(Element uevtNode)
    {         
        System.out.println("\n-->StiftInfo:");
        System.out.println("\nUeVtEvsStift:\t" + uevtNode.getChildText("UeVtEvsStift"));
        System.out.println("id:\t\t" + uevtNode.getAttributeValue("id"));
        System.out.println("Status:\t\t" + uevtNode.getChildText("Status"));
        
        //Abstieg in die GGS
        uevtNode.getChildren("GGS").forEach(ReadXMLDemo::readGGSNode);
        
        //Abstieg in die Leitung_UEVT
        uevtNode.getChildren("Leitung_UEVT").forEach(ReadXMLDemo::readLeitung_UEVTNode);


    }
     
    private static void readGGSNode(Element GGSNode){
    	System.out.println("\n-->GGS: ");

        
        //Abstieg in die HVTV_GGS
        GGSNode.getChildren("HVTV-GGS").forEach(ReadXMLDemo::readHVTVGGSNode);

    }
    
    private static void readHVTVGGSNode(Element HVTVGGSNode){
    	System.out.println("\n-->HVTV-GGS: ");
    	System.out.println("GGS-Bucht:\t" + HVTVGGSNode.getChildText("GGS-Bucht"));
        System.out.println("GGS-Zeile:\t" + HVTVGGSNode.getChildText("GGS-Zeile"));
    	System.out.println("GGS-FE:\t\t" + HVTVGGSNode.getChildText("GGS-FE"));
        System.out.println("GGS-Stift:\t" + HVTVGGSNode.getChildText("GGS-Stift"));
    	System.out.println("Kennbuchstabe:\t" + HVTVGGSNode.getChildText("Kennbuchstabe"));
        System.out.println("VBK-Art:\t" + HVTVGGSNode.getChildText("VBK-Art"));

    }
    
    private static void readLeitung_UEVTNode(Element LeitungUEVTNode){
    	System.out.println("\n-->Leitung_UEVT: ");
        System.out.println("LtgKey:\t\t" + LeitungUEVTNode.getChildText("LtgKey"));
        System.out.println("RangKennz:\t" + LeitungUEVTNode.getChildText("RangierungsKennz"));
        
        //Abstieg in die LTGBZ
        LeitungUEVTNode.getChildren("LTGBZ").forEach(ReadXMLDemo::readLTGBZNode);
    }
    
    private static void readLTGBZNode(Element LTGBZNode){
    	System.out.println("\n-->LTGBZ: ");
        System.out.println("LSZ:\t\t" + LTGBZNode.getChildText("LSZ"));
    	System.out.println("ONKzA:\t\t" + LTGBZNode.getChildText("ONKzA"));
        System.out.println("RufNr:\t\t" + LTGBZNode.getChildText("RufNr"));
    	System.out.println("LSZZ:\t\t" + LTGBZNode.getChildText("LSZZ"));
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
     
    private static String readFileContent(String filePath)
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
    }     
}