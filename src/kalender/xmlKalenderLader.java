package kalender;

import kalender.modell.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class xmlKalenderLader {
    private final String path;     //pfad der xml-Datei
    private Document doc;
    private Element rootElement;

    public xmlKalenderLader(String path) throws Exception {
        this.path = path;
        try {
            doc = parseDocument();
            validateDocument();
            rootElement = doc.getDocumentElement();

        }catch(Exception ex){
            throw new Exception("Fehler beim Validieren und Parsen der XML File " + ex.getMessage());
        }
    }

    public xmlKalenderLader(Kalender kalender, String path) throws Exception {
        this.path = path;
        try{
            doc = createNewDocument(kalender);
            validateDocument();
            writeDocumentToFile(doc, path);
            rootElement = doc.getDocumentElement();

        }catch (Exception ex){
            throw new Exception("Fehler beim Validieren und Parsen der XML File " + ex.getMessage());
        }
    }

    private Document createNewDocument(Kalender kalender) throws ParserConfigurationException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = documentFactory.newDocumentBuilder();
        doc = builder.newDocument();

        //rootNode festlegen
        Element root = doc.createElement("Kalender");
        doc.appendChild(root);

        if(kalender instanceof RaumKalender){
            Element raumKalenderElement = doc.createElement("RaumKalender");
            root.appendChild(raumKalenderElement);

            Element kalenderNameElement = doc.createElement("Name");
            kalenderNameElement.appendChild(doc.createTextNode(kalender.getName()));
            raumKalenderElement.appendChild(kalenderNameElement);

            appendTermineElement(kalender, raumKalenderElement);

            Element plaetzeElement = doc.createElement("Plaetze");
            int plaetze = ((RaumKalender) kalender).getPlaetze();
            plaetzeElement.appendChild(doc.createTextNode(String.valueOf(plaetze)));
            raumKalenderElement.appendChild(plaetzeElement);
        }
        else if(kalender instanceof PersonenKalender) {
            Element pKalenderElement = doc.createElement("PersonenKalender");
            root.appendChild(pKalenderElement);

            Element kalenderNameElement = doc.createElement("Name");
            kalenderNameElement.appendChild(doc.createTextNode(kalender.getName()));
            pKalenderElement.appendChild(kalenderNameElement);

            appendTermineElement(kalender, pKalenderElement); //erst termine anhängen, dann besitzer

            Element besitzerElement = doc.createElement("Besitzer");
            besitzerElement.appendChild(doc.createTextNode(((PersonenKalender) kalender).getBesitzer()));
            pKalenderElement.appendChild(besitzerElement);
        }
        else if(kalender instanceof GruppenKalender) {
            Element gKalenderElement = doc.createElement("GruppenKalender");
            root.appendChild(gKalenderElement);

            Element kalenderNameElement = doc.createElement("Name");
            kalenderNameElement.appendChild(doc.createTextNode(kalender.getName()));
            gKalenderElement.appendChild(kalenderNameElement);

            appendTermineElement(kalender, gKalenderElement);

            Element mitgliederElement = doc.createElement("Mitglieder");
            for(String mitglied : ((GruppenKalender) kalender).getMitglieder()){
                Element mitgliedElement = doc.createElement("Mitglied");
                mitgliedElement.appendChild(doc.createTextNode(mitglied));
                mitgliederElement.appendChild(mitgliedElement);
            }
            gKalenderElement.appendChild(mitgliederElement);
        }
        return doc;
    }

    private void appendTermineElement(Kalender kalender, Element xKalenderElement) {
        Element termineElement = doc.createElement("Termine");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        for (Termin termin : kalender.getTermine()) {
            Element terminElement = doc.createElement("Termin");
            terminElement.appendChild(createElementWithText(doc, "Name", termin.getName()));
            terminElement.appendChild(createElementWithText(doc, "Start", termin.getStart().format(formatter)));
            terminElement.appendChild(createElementWithText(doc, "Ende", termin.getEnde().format(formatter)));
            termineElement.appendChild(terminElement);
        }
        xKalenderElement.appendChild(termineElement);
    }

    private Element createElementWithText(Document doc, String tag, String text) {
        Element element = doc.createElement(tag);
        element.appendChild(doc.createTextNode(text));
        return element;
    }

//    private void validateDocument(Document document) throws Exception {
//        // Implementiere die XML-Validierung
//        try {
//            InputStream schemaFileStream = new FileInputStream("./src/kalender/kalender.xsd");
//            Source schemaFileSource = new StreamSource(schemaFileStream);
//
//            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//            Schema schema = factory.newSchema(schemaFileSource);
//
//            Validator validator = schema.newValidator();
//            DOMSource domSource = new DOMSource(document);
//            validator.validate(domSource);
//        } catch (Exception e) {
//            throw new Exception("Fehler bei der Validierung des XML-Dokuments: " + e.getMessage());
//        }
//    }
    private void validateDocument() throws Exception {
        try{
            InputStream schemaFileStream = new FileInputStream("./src/kalender/kalender.xsd");
            InputStream xmlFileStream = new FileInputStream(path);

            // Zugehörige Source-Objekte je Stream
            Source schemaFileSource = new StreamSource(schemaFileStream);
            Source xmlFileSource = new StreamSource(xmlFileStream);

            // Erzeugung des Schema Objects mit einer Factory
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(schemaFileSource);

            // Validator für das Schema erzeugen & xml validieren
            Validator validator = schema.newValidator();
            validator.validate(xmlFileSource);

        } catch (Exception e) {
            throw new Exception("Fehler bei der Validierung des XML-Dokuments: " + e.getMessage());
        }
    }

    public Document parseDocument() throws IOException, ParserConfigurationException, SAXException {
        InputStream xmlFileStreamForParsing = new FileInputStream(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        doc = dBuilder.parse(xmlFileStreamForParsing);
        return doc;
    }

    private void writeDocumentToFile(Document document, String path) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(path));

        transformer.transform(domSource, streamResult);
    }

    public void importDataIntoCalendar(Kalender existenterKalender) throws Exception {
        NodeList termineListe = rootElement.getElementsByTagName("Termin");

        for (int i = 0; i < termineListe.getLength(); i++) {
            Element terminElement = (Element) termineListe.item(i);
            Termin termin = parseTermin(terminElement);
            existenterKalender.addTermin(termin);
        }
    }

    public Kalender loadCalendar() throws Exception {
        Kalender kalender = null;
        Node firstElementNode = null;

        //Iteriere über childnodes des rootelements, um den ersten Elementknoten zu finden
        NodeList childNodes = rootElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                firstElementNode = node;
                break;
            }
        }
        if (firstElementNode != null) {
            String kalenderTyp = firstElementNode.getNodeName();

            if (kalenderTyp.equals("RaumKalender")) {
                String name = rootElement.getElementsByTagName("Name").item(0).getTextContent();
                int plaetze = Integer.parseInt(rootElement.getElementsByTagName("Plaetze").item(0).getTextContent());
                kalender = new RaumKalender(name, plaetze);
            }
            else if (kalenderTyp.equals("PersonenKalender")) {
                String name = rootElement.getElementsByTagName("Name").item(0).getTextContent();
                String besitzer = rootElement.getElementsByTagName("Besitzer").item(0).getTextContent();
                kalender = new PersonenKalender(name, besitzer);
            }
            else if (kalenderTyp.equals("GruppenKalender")) {
                String name = rootElement.getElementsByTagName("Name").item(0).getTextContent();
                NodeList mitgliederListe = rootElement.getElementsByTagName("Mitglied");

                String[] mitglieder = new String[10];
                for (int i = 0; i < mitgliederListe.getLength(); i++) {
                    mitglieder[i] = mitgliederListe.item(i).getTextContent();
                }
                kalender = new GruppenKalender(name, mitglieder);
            }
        }
        NodeList terminListe = rootElement.getElementsByTagName("Termin");

        for (int i = 0; i < terminListe.getLength(); i++) {
            Element terminElement = (Element) terminListe.item(i);
            Termin termin = parseTermin(terminElement);
            if(kalender != null) kalender.addTermin(termin);
        }
        return kalender;
    }

    private Termin parseTermin(Element terminElement) {
        String name = terminElement.getElementsByTagName("Name").item(0).getTextContent();
        String start = terminElement.getElementsByTagName("Start").item(0).getTextContent();
        String ende = terminElement.getElementsByTagName("Ende").item(0).getTextContent();

        return new Termin(name, LocalDateTime.parse(start), LocalDateTime.parse(ende));
    }
}
