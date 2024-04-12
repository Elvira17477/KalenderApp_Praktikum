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
        long duration = Duration.between(start, ende).toMinutes();
        int dauer = Long.valueOf(duration).intValue();
        return dauer;
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
        DateTimeFormatter startFormatter = DateTimeFormatter.ofPattern("yyyy-mm-dd  hh:mm");
        String formattedStart = start.format(startFormatter);
        DateTimeFormatter endFormatter = DateTimeFormatter.ofPattern("yyyy-mm-dd  hh:mm");
        String formattedEnd = ende.format(endFormatter);

        return (name + "\tStart: " + formattedStart + "\t\tEnde: " + formattedEnd);
    }
}
