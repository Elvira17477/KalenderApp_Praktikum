import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Termin {
    private String name;
    private java.time.LocalDateTime start;
    private java.time.LocalDateTime ende;

    public Termin(String name, java.time.LocalDateTime start, java.time.LocalDateTime ende){
        this.name = name;
        this.start = start;
        this.ende = ende;
    }
    public int getDauer(){
        long duration = Duration.between(start, ende).toMinutes();  //Abstand start - ende in minuten
        return Long.valueOf(duration).intValue();
    }
    public String getName() {
        return name;
    }

    public java.time.LocalDateTime getStart(){
        return start;
    }
    public java.time.LocalDateTime getEnde(){
        return ende;
    }

    //prüfen, ob sich die Termine überschneiden
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
    public void setName(String name){
        if(!name.isEmpty()){
            this.name = name;
        }
    }
    public String getInfo(){
        DateTimeFormatter startFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  hh:mm"); //Startdatum formatieren
        String formattedStart = start.format(startFormatter);                                //Startdatum in gewünschtes Format konvertieren
        DateTimeFormatter endFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  hh:mm");
        String formattedEnd = ende.format(endFormatter);
        int dauer = getDauer();

        return (name + "\tStart: " + formattedStart + "\t\tEnde: " + formattedEnd + "\t\tDauer: " + dauer);
    }

    public LocalDate getStartDate() {
        return start.toLocalDate();
    }
    public LocalDate getEndDate() {
        return ende.toLocalDate();
    }
    public LocalTime getStartTime() {
        return start.toLocalTime();
    }
    public LocalTime getEndTime() {
        return ende.toLocalTime();
    }
}
