package kalender;

import kalender.modell.*;
import org.w3c.dom.*;

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

/**
 * Diese Klasse behandelt das Laden und Speichern von Kalendern zu und von XML-Dateien.
 */
public class xmlKalenderLader {
    private String path;     //pfad der xml-Datei
    private Document doc;
    private Element rootElement;

    /**
     * Konstruktor: Erstellt einen xmlKalenderLader mit dem angegebenen Pfad und parst das XML-Dokument.
     * @param path der Pfad zur XML-Datei.
     * @throws Exception wenn beim Parsen oder Validieren der XML-Datei ein Fehler auftritt.
     */
    public xmlKalenderLader(String path) throws Exception {
        this.path = path;
        try {
            doc = parseDocument();
            validateDocument();
            rootElement = doc.getDocumentElement();  //wurzelelement des docs speichern

        }catch(Exception ex){
            throw new Exception("Fehler beim Validieren und Parsen der XML File " + ex.getMessage());
        }
    }

    /**
     * Konstruktor: Erstellt einen xmlKalenderLader mit dem angegebenen Kalender und Pfad und erstellt ein neues XML-Dokument.
     * @param kalender der zu speichernde Kalender.
     * @param path der Pfad, wo die XML-Datei gespeichert wird.
     * @throws Exception wenn beim Erstellen oder Speichern der XML-Datei ein Fehler auftritt.
     */
    public xmlKalenderLader(Kalender kalender, String path) throws Exception {
        this.path = path;
        try{
            doc = createNewDocument(kalender);
            writeDocumentToFile(doc, path);
            rootElement = doc.getDocumentElement();

        }catch (Exception ex){
            throw new Exception("Fehler beim Erstellen der XML File " + ex.getMessage());
        }
    }

    /**
     * Erstellt ein neues XML-Dokument basierend auf dem gegebenen Kalender.
     * @param kalender der Kalender, der in ein XML-Dokument umgewandelt wird.
     * @return das erstellte XML-Dokument.
     */
    public Document createNewDocument(Kalender kalender) throws ParserConfigurationException {
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

    /**
     * Hängt die Termine an das gegebene XML-Kalenderelement an.
     * @param kalender der Kalender, dessen Termine angehängt werden.
     * @param xKalenderElement das XML-Element, an das die Termine angehängt werden.
     */
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

    /**
     * Erstellt ein XML-Element mit dem angegebenen Tag und Textinhalt.
     * @param doc das XML-Dokument.
     * @param tag der Tag-Name für das Element.
     * @param text der Textinhalt für das Element.
     * @return das erstellte XML-Element.
     */
    private Element createElementWithText(Document doc, String tag, String text) {
        Element element = doc.createElement(tag);
        element.appendChild(doc.createTextNode(text));
        return element;
    }

    /**
     * Validiert das XML-Dokument gegen das Schema.
     * @throws Exception wenn während der Validierung ein Fehler auftritt.
     */
    public void validateDocument() throws Exception {
        //schema und xml files mit inputstream öffnen, inputstream wird automatisch am ende des blocks geschlossen
        try(InputStream schemaFileStream = new FileInputStream("./src/kalender/kalender.xsd")){
            InputStream xmlFileStream = new FileInputStream(path);

            //zugehörige Source-Objekte je Stream
            Source schemaFileSource = new StreamSource(schemaFileStream);
            Source xmlFileSource = new StreamSource(xmlFileStream);

            //erzeugung der Schema Factory unter verwendeung der W3C_XML_SCHEMA language
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            //schema aus der xsd file erzeugen
            Schema schema = factory.newSchema(schemaFileSource);

            //validator für das Schema erzeugen & xml validieren
            Validator validator = schema.newValidator();
            validator.validate(xmlFileSource);

        } catch (Exception e) {
            throw new Exception("Fehler bei der Validierung des XML-Dokuments: " + e.getMessage());
        }
    }

    /**
     * Parst das XML-Dokument aus der Datei.
     * @return das geparste XML-Dokument.
     * @throws Exception wenn beim Parsen des XML-Dokuments ein Fehler auftritt.
     */
    public Document parseDocument() throws Exception {
        //neues FileInputStream objekt erstellen, das die file öffnet und wieder schließt
        try(InputStream xmlFileStreamForParsing = new FileInputStream(path)) {

            //DocumentBuilder objekt erstellen um die datei parsen zu können
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            //xml mit dem erstellten DocumentBuilder in ein Document parsen
            doc = dBuilder.parse(xmlFileStreamForParsing);
            return doc;

        }catch (Exception e){
            throw new Exception("Fehler bei Validierung und Parsen der XML-Datei: " + e.getMessage());
        }
    }

    /**
     * Schreibt das XML-Dokument in die angegebene Datei.
     * @param document das zu schreibende XML-Dokument.
     * @param path der Pfad zur Datei, in die das Dokument geschrieben wird.
     * @throws TransformerException wenn während der Transformation ein Fehler auftritt.
     */
    public void writeDocumentToFile(Document document, String path) throws TransformerException {
        //transformer konvertiert DOM doc zu xml file
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        //output xml soll indentiert werden um lesbarkeit zu verbessern
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        //DOMSource objekt aus dokument erzeugen
        DOMSource domSource = new DOMSource(document);
        //StreamResult objekt spezifiziert die destination für die neue file
        StreamResult streamResult = new StreamResult(new File(path));

        //nimmt XML content vom Document und schreibt ihn in die file in path
        transformer.transform(domSource, streamResult);
    }

    /**
     * Lädt einen Kalender aus der XML-Datei.
     * @return der geladene Kalender.
     * @throws Exception wenn beim Laden des Kalenders ein Fehler auftritt.
     */
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
        //kalenderart herausfinden
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

                String[] mitglieder = new String[mitgliederListe.getLength()];
                for (int i = 0; i < mitgliederListe.getLength(); i++) {
                    mitglieder[i] = mitgliederListe.item(i).getTextContent();
                }
                kalender = new GruppenKalender(name, mitglieder);
            }
        }
        //termine in NodeList speichern
        NodeList terminListe = rootElement.getElementsByTagName("Termin");

        for (int i = 0; i < terminListe.getLength(); i++) {
            Element terminElement = (Element) terminListe.item(i);
            Termin termin = parseTermin(terminElement);
            if(kalender != null) kalender.addTermin(termin);
        }
        return kalender;
    }

    /**
     * Parsed ein Termin-Element aus der XML-Datei.
     * @param terminElement das XML-Element, das den Termin darstellt.
     * @return ein neues Termin-Objekt, das die Informationen aus dem XML-Element enthält.
     */
    public Termin parseTermin(Element terminElement) {
        String name = terminElement.getElementsByTagName("Name").item(0).getTextContent();
        String start = terminElement.getElementsByTagName("Start").item(0).getTextContent();
        String ende = terminElement.getElementsByTagName("Ende").item(0).getTextContent();

        return new Termin(name, LocalDateTime.parse(start), LocalDateTime.parse(ende));
    }
}
