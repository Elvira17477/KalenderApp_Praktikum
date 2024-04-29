import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;


public class KalenderApp{
    public static void Benutzereingabe(){
        Scanner scan = new Scanner(System.in);
        boolean nichtBeenden = true;

        while(nichtBeenden){
            System.out.println("\nWas möchten Sie tun?" +
                    "\nNeuen Kalender anlegen:\t\t 1\n" +
                    "Kalender löschen:\t\t\t 2\n" +
                    "Kalenderliste ausgeben:\t\t 3\n" +
                    "Termin hinzufügen:\t\t\t 4\n" +
                    "Terminserie hinzufügen:\t\t 5\n" +
                    "Alle Termine ausgeben:\t\t 6\n" +
                    "Kalender umbenennen:\t\t 7\n" +
                    "Kalender vergleichen:\t\t 8\n" +
                    "KalenderApp verlassen:\t\t 9\n");

            int input = scan.nextInt();
            scan.nextLine(); //konsumiert Zeilenendezeichen nach Eingabe des integers

            switch(input) {
                case 1:
                    System.out.println("Bitte geben Sie einen Kalendernamen an: ");
                    String kalendername = scan.nextLine();

                    if(!Kalenderserie.kalenderExistiert(kalendername)){
                        Kalender neuerkalender = new Kalender(kalendername);
                        Kalenderserie.addKalender(neuerkalender);
                    }else{
                        System.out.println("Dieser Kalender existiert bereits.");
                    }
                    break;
                case 2:
                    System.out.println("Bitte geben Sie den zu löschenden Kalender an: ");
                    String kalenderLoeschen = scan.nextLine();
                    if(Kalenderserie.kalenderExistiert(kalenderLoeschen)) {
                        Kalenderserie.loescheKalender(kalenderLoeschen);
                    }
                    break;
                case 3:
                    if(!Kalenderserie.isEmpty()) {
                        Kalenderserie.kalenderlisteAusgeben();
                    }else{
                        System.out.println("Es sind keine Kalender vorhanden.");
                    }
                    break;
                case 4:
                    System.out.println("Bitte geben Sie den Kalender an, zu dem der Termin hinzugefügt werden soll: ");
                    String name = scan.nextLine();
                    if(Kalenderserie.kalenderExistiert(name)) {
                        Kalenderserie.terminHinzufuegen(name);
                    }else{
                        System.out.println("Der Kalender wurde nicht gefunden.");
                    }
                    break;
                case 5:
                    String nameserie = scan.nextLine();
                    System.out.println("Bitte geben Sie den Kalender an, zu dem die Terminserie hinzugefügt werden soll: ");
                    if(Kalenderserie.kalenderExistiert(nameserie)) {
                        Kalenderserie.serieHinzufuegen(nameserie);
                    }else{
                        System.out.println("Der Kalender wurde nicht gefunden.");
                    }
                    break;
                case 6:
                    System.out.println("Bitte geben Sie den Kalender an, dessen Termine ausgegeben werden sollen: ");
                    String kname = scan.nextLine();
                    if(Kalenderserie.kalenderExistiert(kname)) {
                        //Kalender.ausgeben(kname);
                    }else{
                        System.out.println("Der Kalender wurde nicht gefunden.");
                    }
                    break;
                case 7:
                    System.out.println("Bitte geben Sie den Kalender an, der umbenannt werden soll: ");
                    String uname = scan.nextLine();
                    System.out.println("Bitte geben Sie den neuen Namen an: ");
                    String neuername = scan.nextLine();
                    if(Kalenderserie.kalenderExistiert(uname)){
                        Kalenderserie.kalenderUmbenennen(uname, neuername);
                    }else{
                        System.out.println("Der Kalender wurde nicht gefunden.");
                    }
                    break;
                case 8:
                    Kalender kalender1 = new Kalender("Vorlesungen");
                    Termin terminV1 = new Termin("EIDOP", LocalDateTime.of(2024, 4, 25, 8, 0),
                            LocalDateTime.of(2024, 4, 25, 10, 0));
                    Termin terminV2 = new Termin("Mathe", LocalDateTime.of(2024, 4, 25, 12, 0),
                            LocalDateTime.of(2024, 4, 25, 14, 0));

                    Termin terminV3 = new Termin("SoftSkills", LocalDateTime.of(2024, 4, 26, 10, 0),
                            LocalDateTime.of(2024, 4, 26, 12, 0));
//                    Termin terminV4 = new Termin("SoftSkill", LocalDateTime.of(2024, 4, 26, 14, 0),
//                            LocalDateTime.of(2024, 4, 26, 16, 0));
                    kalender1.addTermin(terminV1);
                    kalender1.addTermin(terminV2);
                    kalender1.addTermin(terminV3);
                    //kalender1.addTermin(terminV4);

                    Kalender kalender2 = new Kalender("Freizeit");
//                    Termin terminF0 = new Termin("Klettern", LocalDateTime.of(2024, 04, 25, 8, 00),
//                            LocalDateTime.of(2024, 04, 25, 10, 00));
                    Termin terminF00 = new Termin("Kochen", LocalDateTime.of(2024, 4, 25, 12, 0),
                            LocalDateTime.of(2024, 4, 25, 14, 0));
//                    Termin terminF1 = new Termin("Klettern", LocalDateTime.of(2024, 4, 25, 14, 0),
//                            LocalDateTime.of(2024, 4, 25, 16, 0));

                    Termin terminF2 = new Termin("Zahnarzt", LocalDateTime.of(2024, 4, 26, 10, 0),
                            LocalDateTime.of(2024, 04, 26, 12, 0));
//                    Termin terminF3 = new Termin("Kochen", LocalDateTime.of(2024, 4, 26, 14, 0),
//                            LocalDateTime.of(2024, 4, 26, 16, 0));
                    //kalender2.addTermin(terminF0);
                    kalender2.addTermin(terminF00);
                    //kalender2.addTermin(terminF1);
                    kalender2.addTermin(terminF2);
                    //kalender2.addTermin(terminF3);

                    Termin[] freieTermine = kalender1.freieTermineFinden(kalender2, LocalDate.of(2024, 4, 25),
                            LocalDate.of(2024, 4, 26), LocalTime.of(8, 0), LocalTime.of(16, 0),
                            120, "Freier Termin");

                    for(Termin termin : freieTermine){
                        System.out.println(termin.getInfo());
                    }
                    break;
                case 9:
                    System.out.println("KalenderApp wird geschlossen.");
                    nichtBeenden = false;
                    break;
            }
        }
    }

//    public static void testFreieTermineFinden() {
//        // Erstellen von Kalendern und Terminen
//        Kalender kalender1 = new Kalender("Freizeit");
//        Kalender kalender2 = new Kalender("Vorlesungen");
//
//        // Hinzufügen von Terminen zu den Kalendern (Annahme: die Methode zum Hinzufügen von Terminen existiert)
//        LocalDateTime startDatum = LocalDateTime.of(2024, 4, 29, 9, 0); // Startdatum und -zeit
//        LocalDateTime endDatum = LocalDateTime.of(2024, 4, 29, 18, 0);   // Enddatum und -zeit
//
//        Termin termin1 = new Termin("Meeting", startDatum, startDatum.plusHours(1)); // Termin im ersten Kalender
//        kalender1.addTermin(termin1);
//
//        Termin termin2 = new Termin("Appointment", startDatum.plusHours(2), startDatum.plusHours(3)); // Termin im zweiten Kalender
//        kalender2.addTermin(termin2);
//
//        // Durchführung der Methode freieTermineFinden
//        Termin[] freieTermine = freieTermineFinden(kalender2, LocalDate.of(2024, 4, 29), LocalDate.of(2024, 4, 30),
//                LocalTime.of(8, 0), LocalTime.of(20, 0), 60, "Free Slot");
//
//        // Überprüfung des Ergebnisses
//        Assert.assertEquals(1, freieTermine.length); // Es sollte einen freien Termin geben
//
//        // Überprüfen, ob der freie Termin den erwarteten Eigenschaften entspricht
//        Assert.assertEquals("Free Slot", freieTermine[0].getName());
//        Assert.assertEquals(startDatum.plusHours(1), freieTermine[0].getStart());
//        Assert.assertEquals(startDatum.plusHours(2), freieTermine[0].getEnde());
//    }

    public static void main(String[] args){

        Kalenderserie.getInstance();
        Benutzereingabe();
        //testFreieTermineFinden();
    }
}