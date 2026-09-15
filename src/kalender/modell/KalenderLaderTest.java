package kalender.modell;

import kalender.xmlKalenderLader;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class KalenderLaderTest {

    private xmlKalenderLader lader;
    private String testFilePath = "./src/kalender/testKalender.xml";
    private String originalTestFilePath = "./src/kalender/originalTestKalender.xml";
    private String invalidFilePath = "./src/kalender/invalidKalender.xml";
    private Kalender testKalender, originalTestKalender;


    @BeforeEach
    public void setUp() throws TerminException, IOException {
        testKalender = new RaumKalender("TestRaumKalender", 10);
        testKalender.addTermin(new Termin("TestTermin", LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
    }


//    @Test
//    public void testLoadCalendar() throws Exception {
//        lader = new xmlKalenderLader(originalTestFilePath);
//        Kalender loadedKalender = lader.loadCalendar();
//        assertNotNull(loadedKalender);
//        assertEquals("TestRaumKalender", loadedKalender.getName());
//    }

    @Test
    public void testValidateDocument() {
        assertDoesNotThrow(() -> {
            lader = new xmlKalenderLader(testFilePath);
            lader.validateDocument();
        });
    }

    @Test
    public void testParseTermin() throws Exception {
        lader = new xmlKalenderLader(testFilePath);
        Document doc = lader.parseDocument();
        Element terminElement = (Element) doc.getElementsByTagName("Termin").item(0);
        Termin termin = lader.parseTermin(terminElement);
        assertNotNull(termin);
        assertEquals("TestTermin", termin.getName());
    }

    @Test
    public void testLoadInvalidCalendar() {
        Exception exception = assertThrows(Exception.class, () -> {
            lader = new xmlKalenderLader(invalidFilePath);
            lader.loadCalendar();
        });
        assertTrue(exception.getMessage().contains("Fehler"));
    }

    @Test
    public void testParseDocumentWithInvalidPath() {
        Exception exception = assertThrows(Exception.class, () -> {
            lader = new xmlKalenderLader("invalidPath.xml");
            lader.parseDocument();
        });
        assertTrue(exception.getMessage().contains("Fehler bei Validierung und Parsen der XML-Datei"));
    }

    @Test
    public void testWriteDocumentToFile() throws Exception {
        lader = new xmlKalenderLader(originalTestKalender, originalTestFilePath);
        Document doc = lader.createNewDocument(originalTestKalender);
        lader.writeDocumentToFile(doc, originalTestFilePath);
        assertTrue(Files.exists(Paths.get(originalTestFilePath)));
    }

    @Test
    public void testCreateNewDocument() throws Exception {
        lader = new xmlKalenderLader(testKalender, testFilePath);
        Document doc = lader.createNewDocument(testKalender);
        assertNotNull(doc);
        assertEquals("Kalender", doc.getDocumentElement().getNodeName());
    }
}
