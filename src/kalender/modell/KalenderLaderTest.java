package kalender.modell;

import kalender.xmlKalenderLader;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class KalenderLaderTest {

    private xmlKalenderLader lader;
    private String testFilePath = "./src/kalender/testKalender.xml";
    private String originalTestFilePath = "./src/kalender/originalTestKalender.xml";
    private String invalidFilePath = "./src/kalender/invalidKalender.xml";
    private Kalender testKalender;


    @BeforeEach
    public void setUp() throws TerminException {
        testKalender = new RaumKalender("TestRaumKalender", 10);
        testKalender.addTermin(new Termin("TestTermin", LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
    }


    @Test
    public void testValidateDocument() {
        assertDoesNotThrow(() -> {
            lader = new xmlKalenderLader(testFilePath);
            lader.validateDocument();
        });
    }

    @Test
    public void testCreateNewDocument() throws Exception {
        lader = new xmlKalenderLader(testKalender, testFilePath);
        Document doc = lader.createNewDocument(testKalender);
        assertNotNull(doc);
        assertEquals("Kalender", doc.getDocumentElement().getNodeName());
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
    public void testLoadCalendar() throws Exception {
        lader = new xmlKalenderLader(originalTestFilePath);
        Kalender loadedKalender = lader.loadCalendar();
        assertNotNull(loadedKalender);
        assertEquals("TestRaumKalender", loadedKalender.getName());
    }
}
