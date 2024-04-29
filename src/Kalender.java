import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Kalender{
    private String name;
    private static Termin[] termine;
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

    public static Termin[] freieTermineFinden(Kalender kalender2, java.time.LocalDate startDatum, java.time.LocalDate endDatum,
                                              java.time.LocalTime ab, java.time.LocalTime bis, int dauer, String name) {

        ArrayList<Termin> freieTermine = new ArrayList<>();
        ArrayList<Termin> suchzeitK1;
        ArrayList<Termin> suchzeitK2;
        ArrayList<Termin> freieTermineK1;
        ArrayList<Termin> freieTermineK2;

        //extrahiere Vergleichszeitraum aus beiden Kalendern
        suchzeitK1 = extrahiereZeitraum(termine, startDatum, endDatum, ab, bis);
        suchzeitK2 = extrahiereZeitraum(kalender2.getTermine(), startDatum, endDatum, ab, bis);

        //suche und erstelle freie Termine am Anfang, Ende und zwischen den Terminen im relevanten Zeitraum
        freieTermineK1 = sucheFreieTermine(suchzeitK1, startDatum, endDatum, ab, bis, dauer, name);
        freieTermineK2 = sucheFreieTermine(suchzeitK2, startDatum, endDatum, ab, bis, dauer, name);

        //finde überschneidende freie Termine
        for (int i = 0; i < freieTermineK1.size(); i++) {
            if(freieTermineK1.get(i).getDauer() >= dauer &&                   //termin[i] dauert mindestens "dauer" minuten
                    (freieTermineK1.get(i).getStartTime().getMinute() == 0 || freieTermineK1.get(i).getStartTime().getMinute() == 30)){  //termin[i] startet entweder um 00 oder 30 nach

                for(int j = 0; j < freieTermineK2.size(); j++){
                    LocalDateTime start1 = freieTermineK1.get(i).getStart();
                    LocalDateTime end1 = freieTermineK1.get(i).getEnde();
                    LocalDateTime start2 = freieTermineK2.get(j).getStart();
                    LocalDateTime end2 = freieTermineK2.get(j).getEnde();

                    LocalDateTime overlapStart = start1.isAfter(start2) ? start1 : start2;
                    LocalDateTime overlapEnd = end1.isBefore(end2) ? end1 : end2;
                    Duration overlapDuration = Duration.between(overlapStart, overlapEnd);

                    if(Objects.equals(freieTermineK1.get(i).getName(), name) &&
                            freieTermineK2.get(j).getDauer() >= dauer &&
                            (freieTermineK2.get(j).getStartTime().getMinute() == 0 || freieTermineK2.get(j).getStartTime().getMinute() == 30)){

                        if(start1.isEqual(start2) && end1.isEqual(end2)){      //beide Termine genau überlappend
                            LocalDateTime start = freieTermineK2.get(j).getStartDate().atTime(freieTermineK2.get(j).getStartTime());
                            LocalDateTime ende = freieTermineK2.get(j).getEndDate().atTime(freieTermineK2.get(j).getEndTime());
                            Termin freierTermin = new Termin(name, start, ende);
                            freieTermine.add(freierTermin);
                        }
                        if(overlapDuration.toMinutes() >= dauer) {             //Termine asymmetrisch überlappend
                            Termin freierTermin = new Termin(name, overlapStart, overlapStart.plusMinutes(dauer));
                            freieTermine.add(freierTermin);
                        }
                    }
                }
            }
        }
        Termin[] freieTermineArray = new Termin[freieTermine.size()];
        freieTermine.toArray(freieTermineArray);
        return freieTermineArray;
    }

    private static ArrayList<Termin> sucheFreieTermine(ArrayList<Termin> suchzeitK, LocalDate startDatum,
                                                       LocalDate endDatum, LocalTime ab, LocalTime bis, int dauer, String name) {

        ArrayList<Termin> freieTermine = new ArrayList<>();

        //freie Termine zwischen vorhandenen Terminen einfügen
        for (int i = 0; i < suchzeitK.size()-1; i++) {

            //Ende des aktuellen Termins
            LocalDateTime start = suchzeitK.get(i).getEndDate().atTime(suchzeitK.get(i).getEndTime());
            //Anfang des nächsten Termins
            LocalDateTime ende = suchzeitK.get(i + 1).getStartDate().atTime(suchzeitK.get(i + 1).getStartTime());
            //Dauer des freien Termins
            Duration duration = Duration.between(start, ende);

            //wenn mindestdauer erreicht wird
            if (duration.toMinutes() >= dauer) {
                //aufeinenderfolgende Termine liegen am selben Datum
                if (suchzeitK.get(i + 1).getStartDate().isEqual(suchzeitK.get(i).getEndDate())) {
                    Termin zwischenTermin = new Termin(name, start, ende); //ende des freien Termins am Start des nächsten Termins
                    freieTermine.add(zwischenTermin);
                } else {
                    //aufeinanderfolgende Termine liegen an verschiedenen Daten
                    LocalDateTime end = suchzeitK.get(i).getEndDate().atTime(bis); //ende des freien Termins am Zeitpunkt bis
                    Termin zwischenTermin = new Termin(name, start, end);
                    Duration dur = Duration.between(start, end);
                    if (dur.toMinutes() >= dauer) {
                        freieTermine.add(zwischenTermin);
                    }
                }
                //vorheriger Termin liegt am vorigen Tag && Termin liegt nach ab
                if (i != 0 && suchzeitK.get(i).getStartDate().isAfter(suchzeitK.get(i - 1).getEndDate()) &&
                        suchzeitK.get(i).getStartTime().isAfter(ab)) {

                    LocalDateTime startOfDay = suchzeitK.get(i).getStartDate().atTime(ab);
                    LocalDateTime end = suchzeitK.get(i).getEndDate().atTime(suchzeitK.get(i).getStartTime());
                    Duration dur = Duration.between(startOfDay, end);
                    if (dur.toMinutes() >= dauer) {
                        Termin anfangsTermin = new Termin(name, startOfDay, end);
                        freieTermine.add(anfangsTermin);
                    }
                }
            }
            // Füge freie Termine am Anfang und Ende des Zeitraums ein, wenn nötig
            if (suchzeitK.get(0).getStartTime().isAfter(ab)) {
                LocalDateTime start1 = startDatum.atTime(ab);
                LocalDateTime ende1 = suchzeitK.get(0).getStartDate().atTime(suchzeitK.get(0).getStartTime());
                Duration dur = Duration.between(start1, ende);
                if(dur.toMinutes() >= dauer) {
                    Termin ersterTermin = new Termin(name, start1, ende1);
                    freieTermine.add(ersterTermin);
                }
            }
            if (suchzeitK.get(suchzeitK.size() - 1).getEndTime().isBefore(bis)) {
                LocalDateTime start2 = suchzeitK.get(suchzeitK.size() - 1).getEndDate().atTime(suchzeitK.get(suchzeitK.size() - 1).getEndTime());
                LocalDateTime ende2 = endDatum.atTime(bis);
                Duration dur = Duration.between(start2, ende2);
                if(dur.toMinutes() >= dauer) {
                    Termin letzterTermin = new Termin(name, start2, ende2);
                    freieTermine.add(letzterTermin);
                }
            }
        }
        return freieTermine;
    }

//    private ArrayList<Termin> sucheFreieTermine(ArrayList<Termin> suchzeitK, LocalDate startDatum,
//                                                LocalDate endDatum, LocalTime ab, LocalTime bis, int dauer, String name) {
//
//        ArrayList<Termin> freieTermine = new ArrayList<>();
//
//        if (suchzeitK.isEmpty()) {
//            LocalDateTime start = startDatum.atTime(ab);
//            LocalDateTime ende = endDatum.atTime(bis);
//            Termin ersterTermin = new Termin(name, start, ende);
//            freieTermine.add(ersterTermin);
//        } else {
//            // Freie Termine zwischen vorhandenen Terminen einfügen
//            for (int i = 0; i <= suchzeitK.size(); i++) {
//                LocalDateTime start, ende;
//
//                if (i == 0) {
//                    // Erster Termin
//                    start = startDatum.atTime(ab);
//                } else if (i == suchzeitK.size()) {
//                    // Letzter Termin
//                    start = suchzeitK.get(i - 1).getEndDate().atTime(suchzeitK.get(i - 1).getEndTime());
//                    ende = endDatum.atTime(bis);
//                    Duration duration = Duration.between(start, ende);
//                    if (duration.toMinutes() >= dauer) {
//                        freieTermine.add(new Termin(name, start, ende));
//                    }
//                    break;
//                } else {
//                    // Zwischenliegende Termine
//                    start = suchzeitK.get(i - 1).getEndDate().atTime(suchzeitK.get(i - 1).getEndTime());
//                }
//
//                if (i < suchzeitK.size()) {
//                    ende = suchzeitK.get(i).getStartDate().atTime(suchzeitK.get(i).getStartTime());
//                    Duration duration = Duration.between(start, ende);
//
//                    if (duration.toMinutes() >= dauer) {
//                        // Freier Termin zwischen zwei Terminen am selben Tag
//                        if (suchzeitK.get(i).getStartDate().isEqual(suchzeitK.get(i - 1).getEndDate())) {
//                            freieTermine.add(new Termin(name, start, ende));
//                        } else {
//                            // Freier Termin über mehrere Tage
//                            LocalDateTime endOfDay = suchzeitK.get(i - 1).getEndDate().atTime(bis);
//                            Duration dur = Duration.between(start, endOfDay);
//                            if (dur.toMinutes() >= dauer) {
//                                freieTermine.add(new Termin(name, start, endOfDay));
//                            }
//                        }
//                    }
//                }
//
//                // Überprüfen auf vorherige Termine
//                if (i > 0 && suchzeitK.get(i - 1).getEndDate().isBefore(suchzeitK.get(i).getStartDate())
//                        && suchzeitK.get(i).getStartTime().isAfter(ab)) {
//                    LocalDateTime startOfDay = suchzeitK.get(i).getStartDate().atTime(ab);
//                    LocalDateTime end = suchzeitK.get(i).getEndDate().atTime(suchzeitK.get(i).getStartTime());
//                    Duration dur = Duration.between(startOfDay, end);
//                    if (dur.toMinutes() >= dauer) {
//                        freieTermine.add(new Termin(name, startOfDay, end));
//                    }
//                }
//            }
//        }
//
//        return freieTermine;
//    }

    private static ArrayList<Termin> extrahiereZeitraum(Termin[] terminArray, java.time.LocalDate startDatum, java.time.LocalDate endDatum,
                                                        java.time.LocalTime ab, java.time.LocalTime bis) {
        ArrayList<Termin> suchzeitK1 = new ArrayList<>();

        for (int i = 0; i < terminArray.length; i++) {
            LocalDate startDate = terminArray[i].getStartDate();
            LocalDate endDate = terminArray[i].getEndDate();
            LocalTime startTime = terminArray[i].getStartTime();
            LocalTime endTime = terminArray[i].getEndTime();

            if ((startDate.equals(startDatum) || startDate.isAfter(startDatum)) && //zwischen startDatum und endDatum
                    (endDate.equals(endDatum) || endDate.isBefore(endDatum)) &&

                    (startTime.equals(ab) || startTime.isAfter(ab)) &&             //zwischen ab und bis
                    (endTime.equals(bis) || endTime.isBefore(bis))) {
                suchzeitK1.add(terminArray[i]);
            }
        }
        return suchzeitK1;
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