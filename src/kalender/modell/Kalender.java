package kalender.modell;

import anzeige.KalenderAnzeige;
import anzeige.TerminalAnzeige;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Abstrakte Klasse, die ein allgemeines Kalenderobjekt repräsentiert.
 * Enthält generalisierte Eigenschaften und Methoden, die von spezifischen
 * Kalenderimplementierungen verwendet werden können.
 */
public abstract class Kalender {
    private String name;
    private ArrayList<Termin> termine;
    private ArrayList<Terminserie> serien;
    private ArrayList<Termin> freieTermine;
    private KalenderAnzeige kalenderAnzeige; //= new TerminalAnzeige(this);

    /**
     * Konstruktor für die Initialisierung eines Kalenders mit einem Namen.
     * @param name Der Name des Kalenders.
     */
    public Kalender(String name) {
        this.name = name;
        termine = new ArrayList<>();
        serien = new ArrayList<>();
        freieTermine = new ArrayList<>();
        //this.kalenderAnzeige = kalenderAnzeige;
    }

    /**
     * Setzt die Kalenderanzeige.
     * @param kalenderAnzeige Die Kalenderanzeige.
     */
    public void setKalenderAnzeige(KalenderAnzeige kalenderAnzeige) {
        this.kalenderAnzeige = kalenderAnzeige;
    }

    /**
     * Gibt alle Termine dieses Kalenders zurück.
     * @return Ein Array aller Termine dieses Kalenders.
     */
    public ArrayList<Termin> getTermine() {
        return termine;
    }

    /**
     * Gibt alle Terminserien dieses Kalenders zurück.
     * @return Ein Array aller Terminserien dieses Kalenders.
     */
    public ArrayList<Terminserie> getSerien() {
        return serien;
    }

    /**
     * Abstrakte Methode zur Überprüfung, ob ein Termin zu diesem Kalender hinzugefügt werden kann.
     * Muss von Unterklassen implementiert werden.
     * @param termin Der zu prüfende Termin.
     * @return true, wenn der Termin hinzugefügt werden kann, ansonsten false.
     */
    protected abstract void pruefeHinzufuegen(Termin termin) throws TerminException;

    /**
     * Gibt den Namen dieses Kalenders zurück.
     * @return Der Name des Kalenders.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Legt den Namen dieses Kalenders fest.
     * @param name Der neue Name für diesen Kalender.
     */
    public void setName(String name) {
        if (!name.isEmpty()) {
            this.name = name;
        }
    }

    /**
     * Fügt einen neuen Termin zu diesem Kalender hinzu.
     * @param termin Der hinzuzufügende Termin.
     */
    public void addTermin(Termin termin) throws TerminException {
        pruefeHinzufuegen(termin);
        termine.add(termin);
        System.out.println("Der Termin wurde zum Kalender hinzugefügt.");
    }

    /**
     * Fügt eine neue Terminserie zu diesem Kalender hinzu.
     * @param serie Die hinzuzufügende Terminserie.
     */
    public void addSerie(Terminserie serie) {
        serien.add(serie);
        System.out.println("Die Terminserie wurde zum Kalender hinzugefügt.");
    }

    /**
     * Findet alle freien Termine in diesem Kalender für einen bestimmten Zeitraum.
     * @param kalender2 Der Kalender, mit dem die Terminüberschneidung überprüft wird.
     * @param startDatum Das Startdatum des Zeitraums.
     * @param endDatum Das Enddatum des Zeitraums.
     * @param ab Die Startzeit des Zeitraums.
     * @param bis Die Endzeit des Zeitraums.
     * @param dauer Die Dauer des Termins.
     * @param name Der Name für die zu erstellenden Termine.
     * @return Ein Array aller gefundenen freien Termine.
     */
    public ArrayList<Termin> freieTermineFinden(Kalender kalender2, java.time.LocalDate startDatum, java.time.LocalDate endDatum,
                                       java.time.LocalTime ab, java.time.LocalTime bis, int dauer, String name) {

        ArrayList<Termin> testTermine = new ArrayList<>();
        int zeitraum = startDatum.until(endDatum).getDays();
        long zeitraeumeProTag = Duration.between(ab, bis).toMinutes() / 30;

        for (int i = 0; i <= zeitraum; i++) {
            for (long j = 0; j <= zeitraeumeProTag; j++) {
                LocalTime startZeit = ab.plusMinutes(j * 30);
                LocalTime endZeit = startZeit.plusMinutes(dauer);
                LocalDateTime startDate = startDatum.plusDays(i).atTime(startZeit);
                LocalDateTime endDate = startDatum.plusDays(i).atTime(endZeit);

                if (endZeit.isBefore(bis)) {
                    Termin termin = new Termin(name, startDate, endDate);
                    testTermine.add(termin);
                }
            }
        }
        ArrayList<Termin> ergebnisliste = new ArrayList<>();

        for (Termin test : testTermine) {
            if (this.passtTerminInKalender(test)) {
                if (kalender2.passtTerminInKalender(test)) {
                    ergebnisliste.add(test);
                }
            }
        }
        return ergebnisliste;
    }

    /**
     * Überprüft, ob ein bestimmter Termin in diesen Kalender passt, ohne Überschneidungen zu verursachen.
     * @param test Der zu überprüfende Termin.
     * @return true wenn der Termin in den Kalender passt, ansonsten false.
     */
    public boolean passtTerminInKalender(Termin test) {
        for (Termin termin : termine) {
            if (!termin.TermineNichtUeberschneiden(test)) return false;
        }
        return true;
    }

//    public static void ausgeben(String name){
//
//        for (int k = 0; k < kalender.modell.Kalenderserie.kalenderarray.size(); k++) {
//            if(Kalenderserie.kalenderarray.get(k) != null && Kalenderserie.kalenderarray.get(k).getName().equals(name)){
//
//                System.out.println("\n" + name + " ");
//                System.out.println("Termine:");
//                for (int i = 0; i < termine.size(); i++) {
//                    System.out.println(termine.get(i).getInfo());
//                }
//
//                System.out.println("\nTerminserien:");
//                for (int i = 0; i < serien.size(); i++) {                     //array serien enthält Terminserien arrays
//                    //System.out.println("Serie: " + serien[i].getName());
//                    for (int j = 0; j < serien.get(i).getAnzahl(); j++) {         //anzahl der Terminserien im array durchlaufen
//                        System.out.println(serien.get(i).getTermin(j).getInfo()); //je serie jede Termininfo ausgeben
//                    }
//                }
//            }
//        }
//    }
}