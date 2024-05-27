package kalender.modell;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

public class KalenderserieTest {

   @Test
    public void gueltigerTerminPersonenkalender() throws Exception {
        Termin termin1 = new Termin("Vorlesung", LocalDateTime.parse("2024-05-20T10:00"),
                LocalDateTime.parse("2024-05-20T12:00"));
        PersonenKalender kalender = new PersonenKalender("MeinKalender", "Elli");
        kalender.addTermin(termin1);

        assertEquals(1, kalender.getTermine().size());
        Termin resultTermin = kalender.getTermine().get(0);
        assertEquals("Vorlesung", resultTermin.getName());
        assertEquals(LocalDateTime.parse("2024-05-20T10:00"), resultTermin.getStart());
        assertEquals(LocalDateTime.parse("2024-05-20T12:00"), resultTermin.getEnde());
    }

    @Test
    public void ungueltigerTermin() throws Exception {
        try {
            new Termin(null, LocalDateTime.parse("2024-05-20T10:00"),
                    LocalDateTime.parse("2024-05-20T12:00"));
            fail("Expected IllegalArgumentException to be thrown");
        }catch (IllegalArgumentException ex){
            assertEquals("Name, Start und Ende des Termins sind Pflichtfelder.", ex.getMessage());
        }

        try {
            new Termin("Vorlesung", LocalDateTime.parse("2024.05.20T10:00"), LocalDateTime.parse("2024-05-20T12:00"));
            fail("Expected DateTimeParseException to be thrown");
        } catch (DateTimeParseException ex) {
            //System.out.println("Das Datum muss das Format yyyy-MM-ddThh:mm haben.");
        }

        try {
            new Termin("Vorlesung", LocalDateTime.parse("2024-05-20T12:00"), LocalDateTime.parse("2024-05-20T10:00"));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Das Startdatum des Termins muss vor dem Enddatum liegen.", ex.getMessage());
        }

    }

    @Test
    public void gueltigeTerminserie() throws Exception {
        Terminserie serie = new Terminserie(3, "Meeting", LocalDateTime.parse("2024-05-20T08:00"),
                LocalDateTime.parse("2024-05-20T10:00"), 7);
        Kalender kalender = new PersonenKalender("MeinKalender", "Elli");
        kalender.addSerie(serie);

        assertEquals(1, kalender.getSerien().size());
        Terminserie resultTermin = kalender.getSerien().get(0);
        assertEquals("Meeting", resultTermin.getName());
        assertEquals(3, resultTermin.getAnzahl());
    }

    @Test
    public void ungueltigeTerminserie() throws Exception {
        try {
            new Terminserie(3, "Meeting", LocalDateTime.parse("2024.05.20T08:00"),
                    LocalDateTime.parse("2024-05-20T10:00"), 7);
            fail("Expected DateTimeParseException to be thrown.");
        }catch (DateTimeParseException ex){
            System.out.println("Das Datum muss das Format yyyy-MM-ddThh:mm haben.");
        }

        try {
            new Terminserie(0, "Meeting", LocalDateTime.parse("2024-05-20T08:00"),
                    LocalDateTime.parse("2024-05-20T10:00"), 7);
            fail("Expected IllegalArgumentException to be thrown.");
        }catch (IllegalArgumentException ex){
            assertEquals("Die Anzahl der Termine und die Intervallgroeße muessen größer als Null sein.", ex.getMessage());
        }

    }

    @Test
    public void PersonenkalenderLeererBesitzer(){
        try {
            new PersonenKalender("MeinKalender", null);
            fail("Expected IllegalArgumentException to be thrown.");
        }catch (IllegalArgumentException ex){
            assertEquals("Besitzer ist ein Pflichtfeld im Personenkalender.", ex.getMessage());
        }
    }

    @Test
    public void RaumkalenderWenigerEinPlatz(){
        try {
            new RaumKalender("MeinKalender", 0);
            fail("Expected IllegalArgumentException to be thrown.");
        }catch (IllegalArgumentException ex){
            assertEquals("Plaetze ist ein Pflichtfeld im Raumkalender.", ex.getMessage());
        }
    }

    @Test
    public void GruppenkalenderOhneMitglieder(){
        String[] mitglieder = {};
        try {
            new GruppenKalender("MeinKalender", mitglieder);
            fail("Expected IllegalArgumentException to be thrown.");
        }catch (IllegalArgumentException ex){
            assertEquals("Mitglieder ist ein Pflichtfeld im Gruppenkalender.", ex.getMessage());
        }
    }

    @Test
    public void gueltigerTerminRaumkalender() throws TerminException {
        RaumKalender raumKalender = new RaumKalender("MeinRaumkalender", 20);
        Termin termin = new Termin("Vorlesung", LocalDateTime.parse("2024-05-20T10:00"), LocalDateTime.parse("2024-05-20T12:00"));
        raumKalender.addTermin(termin);

        assertEquals(1, raumKalender.getTermine().size());
        Termin resultTermin = raumKalender.getTermine().get(0);
        assertEquals("Vorlesung", resultTermin.getName());
        assertEquals(LocalDateTime.parse("2024-05-20T10:00"), resultTermin.getStart());
        assertEquals(LocalDateTime.parse("2024-05-20T12:00"), resultTermin.getEnde());
    }

    @Test
    public void gueltigerTerminGruppenkalender() throws TerminException {
        String[] mitglieder = {"Elli", "Matti"};
        GruppenKalender gruppenKalender = new GruppenKalender("MeinGruppenkalender", mitglieder);
        Termin termin = new Termin("Vorlesung", LocalDateTime.parse("2024-05-20T10:00"), LocalDateTime.parse("2024-05-20T12:00"));
        gruppenKalender.addTermin(termin);

        assertEquals(1, gruppenKalender.getTermine().size());
        Termin resultTermin = gruppenKalender.getTermine().get(0);
        assertEquals("Vorlesung", resultTermin.getName());
        assertEquals(LocalDateTime.parse("2024-05-20T10:00"), resultTermin.getStart());
        assertEquals(LocalDateTime.parse("2024-05-20T12:00"), resultTermin.getEnde());
    }

    @Test
    public void UeberschneidungPersonenkalender() throws Exception {
        PersonenKalender personenKalender = new PersonenKalender("MeinKalender", "Elli");
        Termin termin1 = new Termin("Vorlesung", LocalDateTime.parse("2024-05-20T10:00"),
                LocalDateTime.parse("2024-05-20T12:00"));
        Termin termin2 = new Termin("Vorlesung", LocalDateTime.parse("2024-05-20T11:00"),
                LocalDateTime.parse("2024-05-20T13:00"));
        personenKalender.addTermin(termin1);
        try {
            personenKalender.addTermin(termin2);
        }catch(TerminUeberschneidungException ex){
            //assertEquals("", ex.getMessage());
        }
    }

    @Test
    public void UeberschneidungRaumkalender() throws TerminException {
        RaumKalender raumKalender = new RaumKalender("MeinKalender", 20);
        Termin termin1 = new Termin("Vorlesung", LocalDateTime.parse("2024-05-20T10:00"),
                LocalDateTime.parse("2024-05-20T12:00"));
        Termin termin2 = new Termin("Vorlesung", LocalDateTime.parse("2024-05-20T11:00"),
                LocalDateTime.parse("2024-05-20T13:00"));
        raumKalender.addTermin(termin1);
        try {
            raumKalender.addTermin(termin2);
            fail("Expected TerminException to be thrown");
        }catch(TerminUeberschneidungException ex) {
            //assertEquals(null, ex.getMessage());
        }
    }

    @Test
    public void UeberschneidungGruppenkalender() throws TerminException {
        String[] mitglieder = {"Elli", "Matti"};
        GruppenKalender gruppenKalender = new GruppenKalender("MeinGruppenkalender", mitglieder);

        Termin termin1 = new Termin("Vorlesung", LocalDateTime.parse("2024-05-20T08:00"),
                LocalDateTime.parse("2024-05-20T10:00"));
        Termin termin2 = new Termin("Vorlesung", LocalDateTime.parse("2024-05-20T09:00"),
                LocalDateTime.parse("2024-05-20T10:00"));
        new PersonenKalender("MeinKalender", "Elli");
        new PersonenKalender("DeinKalender", "Matti");

        gruppenKalender.addTermin(termin1);
        try {
            gruppenKalender.addTermin(termin2);
            fail("Expected TerminUeberschneidungException to be thrown");
        } catch (TerminUeberschneidungException ex) {
            // Exception expected
        }
    }

    @Test
    public void UeberschneidungGruppenkalenderPersonenkalender() throws TerminException {
        String[] mitglieder = {"Eva", "Martina"};
        GruppenKalender gruppenKalender = new GruppenKalender("GruppenKalender", mitglieder);

        Termin termin1 = new Termin("Vorlesung", LocalDateTime.parse("2024-05-20T08:00"),
                LocalDateTime.parse("2024-05-20T10:00"));
        Termin termin2 = new Termin("Vorlesung", LocalDateTime.parse("2024-05-20T10:00"),
                LocalDateTime.parse("2024-05-20T12:00"));
        Termin termin3 = new Termin("Vorlesung", LocalDateTime.parse("2024-05-20T08:00"),
                LocalDateTime.parse("2024-05-20T10:00"));

        PersonenKalender person1 = new PersonenKalender("MeinKalender", "Eva");
        PersonenKalender person2 = new PersonenKalender("DeinKalender", "Martina");
        person1.addTermin(termin1);
        person2.addTermin(termin2);

        try {
            gruppenKalender.addTermin(termin3);
            fail("Expected PersonNichtVerfuegbarException to be thrown.");
        }catch(PersonNichtVerfuegbarException ex){
            System.out.println("PersonNichtVerfuegbarException. Überschneidung mit Termin des Mitglieds " + ex.getPerson());
        }
    }

}