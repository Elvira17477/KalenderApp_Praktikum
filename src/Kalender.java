import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Kalender{
    private String name;
    private Termin[] termine;
    private Terminserie[] serien;
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
    //kalender2: Kalender@1070
    public Termin[] freieTermineFinden(Kalender kalender2, java.time.LocalDate startDatum, java.time.LocalDate endDatum,
                                       java.time.LocalTime ab, java.time.LocalTime bis, int dauer, String name){

        ArrayList<Termin> freieTermine = new ArrayList<>();
        ArrayList<Termin> suchzeitK1 = new ArrayList<>();
        ArrayList<Termin> suchzeitK2 = new ArrayList<>();
        //hier wird kalender2 durchsucht!!!
        for (int i = 0; i < termine.length; i++) {
            LocalDate startDate = termine[i].getStartDate();
            LocalDate endDate = termine[i].getEndDate();
            LocalTime startTime = termine[i].getStartTime();
            LocalTime endTime = termine[i].getEndTime();

            if ((startDate.equals(startDatum) || startDate.isAfter(startDatum)) && //zwischen startDatum und endDatum
                    (endDate.equals(endDatum) || endDate.isBefore(endDatum)) &&

                    (startTime.equals(ab) || startTime.isAfter(ab)) &&             //zwischen ab und bis
                    (endTime.equals(bis) || endTime.isBefore(bis))) {
                suchzeitK1.add(termine[i]);
            }
        }
        //Kalender2: Kalender@1070
        for (int i = 0; i < kalender2.getTermine().length; i++) {
            LocalDate startDate2 = kalender2.getTermine()[i].getStartDate();
            LocalDate endDate2 = kalender2.getTermine()[i].getEndDate();
            LocalTime startTime2 = kalender2.getTermine()[i].getStartTime();
            LocalTime endTime2 = kalender2.getTermine()[i].getEndTime();

            if ((startDate2.equals(startDatum) || startDate2.isAfter(startDatum)) && //zwischen startDatum und endDatum
                    (endDate2.equals(endDatum) || endDate2.isBefore(endDatum)) &&
                    (startTime2.equals(ab) || startTime2.isAfter(ab)) &&             //zwischen ab und bis
                    (endTime2.equals(bis) || endTime2.isBefore(bis))) {
                suchzeitK2.add(kalender2.getTermine()[i]);
            }
        }
        for (int i = 0; i < suchzeitK1.size(); i++) {
            if (i < suchzeitK1.size() - 1) {
                LocalDate startDate = suchzeitK1.get(i).getStartDate();
                LocalDate endDate = suchzeitK1.get(suchzeitK1.size()-1).getStartDate();
                LocalTime nextStartTime = suchzeitK1.get(i + 1).getStartTime();
                LocalTime endTime = suchzeitK1.get(i).getEndTime();
                Duration duration = Duration.between(endTime, nextStartTime);

                if (duration.compareTo(Duration.ofMinutes(dauer)) == 0 &&             //zwischentermine frei?
                        (endTime.getMinute() == 0 || endTime.getMinute() == 30)) {    //terminstart min 00 oder 30

                    for (int j = 0; j < suchzeitK2.size(); j++) {
                        if (j < suchzeitK2.size() - 1) {
                            LocalTime nextStartTime2 = suchzeitK2.get(j+1).getStartTime();
                            LocalTime endTime2 = suchzeitK2.get(j).getEndTime();
                            Duration duration2 = Duration.between(endTime2, nextStartTime2);

                            if (duration2.compareTo(Duration.ofMinutes(dauer)) == 0 &&              //zwischentermine frei?
                                    (endTime2.getMinute() == 0 || endTime2.getMinute() == 30)) {    //terminstart min 00 oder 30

                                if ((endTime.equals(endTime2) && nextStartTime.equals(nextStartTime2))) {
                                    java.time.LocalDateTime start = endDate.atTime(endTime);
                                    java.time.LocalDateTime ende = startDate.atTime(nextStartTime);
                                    Termin freierTermin = new Termin(name, start, ende);
                                    freieTermine.add(freierTermin);
                                }
                            }
                        }
                    }
                }
            }
        }
        Termin[] freieTermineArray = new Termin[freieTermine.size()];
        freieTermine.toArray(freieTermineArray);
        return freieTermineArray;
    }


    private boolean terminExistent(Kalender kalender, int index) {
        if(termine[index] == null) return true;
        return false;
    }
//    public static void ausgeben(String name){
//
//        for (int k = 0; k < Kalenderserie.kalenderarray.length; k++) {
//            if(Kalenderserie.kalenderarray[k] != null && Objects.equals(Kalenderserie.kalenderarray[k].getName(), name)){
//
//                System.out.println("\n" + name + " ");
//                System.out.println("Termine:");
//                for (int i = 0; i < termine.length; i++) {
//                    System.out.println(termine[i].getInfo());
//                }
//
//                System.out.println("\nTerminserien:");
//                for (int i = 0; i < serien.length; i++) {                     //array serien enthält Terminserien arrays
//                    //System.out.println("Serie: " + serien[i].getName());
//                    for (int j = 0; j < serien[i].getAnzahl(); j++) {         //anzahl der Terminserien im array durchlaufen
//                        System.out.println(serien[i].getTermin(j).getInfo()); //je serie jede Termininfo ausgeben
//                    }
//                }
//            }
//        }
//    }
}