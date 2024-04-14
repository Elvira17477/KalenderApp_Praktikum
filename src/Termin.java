import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Termin {
    private String name;
    private java.time.LocalDateTime start;
    private java.time.LocalDateTime ende;

    public void init(String name, java.time.LocalDateTime start, java.time.LocalDateTime ende){
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

        return ("Name: " + name + "\tStart: " + formattedStart + "\t\tEnde: " + formattedEnd);
    }
}
