import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

public class Kalender{
    private String name;
    private static Termin[] termine;
    private static Terminserie[] serien;
    private Termin[] freieTermine;

    public Kalender(String name){
        this.name = name;
        termine = new Termin[0];     //leeres terminarray
        serien = new Terminserie[0]; //leeres terminserienarray
        freieTermine = new Termin[0];
    }
    public String getName(){
        return this.name;
    }

    public Termin[] getTermine(){
        return termine;
    }

    //Kalendername
    public void setName(String name){
        if(!name.isEmpty()){
            this.name = name;
        }
    }

    public void addTermin(Termin termin){
        Termin[] neueTermine = new Termin[termine.length + 1]; //neues array um eine posiion erweitern
        for (int i = 0; i < termine.length; i++) {             //alle bisherigen Termine kopieren
            neueTermine[i] = termine[i];
        }
        neueTermine[termine.length] = termin;                  //übergebenen Termin einfügen
        termine = neueTermine;                                 //attribut termine auf neues array setzen
    }
    public void addSerie(Terminserie serie){
        Terminserie[] neueTerminserien = new Terminserie[serien.length + 1];
        for(int i = 0; i < serien.length; i++) {
            neueTerminserien[i] = serien[i];
        }
        neueTerminserien[serien.length] = serie;
        serien = neueTerminserien;
    }

    public Termin[] freieTermineFinden(Kalender kalender2, java.time.LocalDate startDatum, java.time.LocalDate endDatum,
                                              java.time.LocalTime ab, java.time.LocalTime bis, int dauer, String name) {

        ArrayList<Termin> testTermine = new ArrayList<>();
        int zeitraum = startDatum.until(endDatum).getDays();
        long zeitraeumeProTag = Duration.between(ab, bis).toMinutes() / 30;

        for (int i = 0; i <= zeitraum; i++) {
            for(long j = 0; j <= zeitraeumeProTag; j++){
                LocalTime startZeit = ab.plusMinutes(j * 30);
                LocalTime endZeit = startZeit.plusMinutes(dauer);
                LocalDateTime startDate = startDatum.plusDays(i).atTime(startZeit);
                LocalDateTime endDate = startDatum.plusDays(i).atTime(endZeit);

                if(endZeit.isBefore(bis)) {
                    Termin termin = new Termin(name, startDate, endDate);
                    testTermine.add(termin);
                }
            }
        }
        ArrayList<Termin> ergebnisliste = new ArrayList<>();

        for(Termin test: testTermine){
            if(this.passtTerminInKalender(test)){
                if(kalender2.passtTerminInKalender(test)) {
                    ergebnisliste.add(test);
                }
            }
        }
        Termin[] freieTermine = new Termin[ergebnisliste.size()];
        freieTermine = ergebnisliste.toArray(freieTermine);
        return freieTermine;
    }

    public boolean passtTerminInKalender(Termin test){
        for(Termin termin : termine){
            if(!termin.TermineNichtUeberschneiden(test)) return false;
        }
        return true;
    }

    public static void ausgeben(String name){

        for (int k = 0; k < Kalenderserie.kalenderarray.length; k++) {
            if(Kalenderserie.kalenderarray[k] != null && Objects.equals(Kalenderserie.kalenderarray[k].getName(), name)){

                System.out.println("\n" + name + " ");
                System.out.println("Termine:");
                for (int i = 0; i < termine.length; i++) {
                    System.out.println(termine[i].getInfo());
                }

                System.out.println("\nTerminserien:");
                for (int i = 0; i < serien.length; i++) {                     //array serien enthält Terminserien arrays
                    //System.out.println("Serie: " + serien[i].getName());
                    for (int j = 0; j < serien[i].getAnzahl(); j++) {         //anzahl der Terminserien im array durchlaufen
                        System.out.println(serien[i].getTermin(j).getInfo()); //je serie jede Termininfo ausgeben
                    }
                }
            }
        }
    }
}