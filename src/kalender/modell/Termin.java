package kalender.modell;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Termin {
    private String name;
    private java.time.LocalDateTime start;
    private java.time.LocalDateTime ende;

    /**
     * Konstruktor für die Erstellung eines Termins mit einem bestimmten Namen, Start- und Enddatum.
     * @param name Der Name des Termins.
     * @param start Das Startdatum des Termins.
     * @param ende Das Enddatum des Termins.
     */
    public Termin(String name, java.time.LocalDateTime start, java.time.LocalDateTime ende){
        if(name == null || start == null || ende == null){
            throw new IllegalArgumentException("Name, Start und Ende des Termins sind Pflichtfelder.");
        }

        if(!start.isBefore(ende)){
            throw new IllegalArgumentException("Das Startdatum des Termins muss vor dem Enddatum liegen.");
        }
        this.name = name;
        this.start = start;
        this.ende = ende;
    }

    /**
     * Berechnet die Dauer des Termins in Minuten.
     * @return Die Dauer des Termins in Minuten.
     */
    public int getDauer(){
        long duration = Duration.between(start, ende).toMinutes();  //Abstand start - ende in minuten
        return Long.valueOf(duration).intValue();
    }

    /**
     * Gibt den Namen des Termins zurück.
     * @return Der Name des Termins.
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt das Startdatum des Termins zurück.
     * @return Das Startdatum des Termins.
     */
    public java.time.LocalDateTime getStart(){
        return start;
    }

    /**
     * Gibt das Enddatum des Termins zurück.
     * @return Das Enddatum des Termins.
     */
    public java.time.LocalDateTime getEnde(){
        return ende;
    }

    /**
     * Überprüft, ob sich dieser Termin nicht mit einem anderen überschneidet.
     * @param termin Der zu überprüfende Termin.
     * @return true, wenn sich die Termine nicht überschneiden, ansonsten false.
     */
    public boolean TermineNichtUeberschneiden(Termin termin){
        if(termin == null) return true;
        if(!termin.getStart().isAfter(this.getStart())){
            if(!termin.getEnde().isAfter(this.getStart())) return true;
            else return false;
        }
        else {
            if(!this.getEnde().isAfter(termin.getStart())) return true;
            else return false;
        }
    }

    /**
     * Setzt den Namen des Termins.
     * @param name Der neue Name des Termins.
     */
    public void setName(String name){
        if(!name.isEmpty()){
            this.name = name;
        }
    }

    /**
     * Gibt Informationen über den Termin in einem formatierten String zurück.
     * @return Informationen über den Termin.
     */
    public String getInfo(){
        DateTimeFormatter startFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  hh:mm"); //Startdatum formatieren
        String formattedStart = start.format(startFormatter);                                //Startdatum in gewünschtes Format konvertieren
        DateTimeFormatter endFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  hh:mm");
        String formattedEnd = ende.format(endFormatter);
        int dauer = getDauer();

        return (name + "\t\t  Start: " + formattedStart + "\t\t  Ende: " + formattedEnd + "\t\t  Dauer (Min.): " + dauer);
    }

}
