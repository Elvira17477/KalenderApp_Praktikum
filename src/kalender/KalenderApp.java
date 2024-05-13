package kalender;

import anzeige.KalenderAnzeige;
import anzeige.TerminalAnzeige;
import kalender.modell.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Scanner;

/*Methodenpolymorphie bezieht sich auf die Fähigkeit, dass Objekte einer Unterklasse sich in verschiedenen
    Situationen unterschiedlich verhalten können, indem sie dieselbe Methode auf unterschiedliche Weise implementieren.
    Dieses Konzept ermöglicht es, den gleichen Methodennamen in verschiedenen Klassen zu verwenden,
    wobei jede Klasse ihre eigene spezifische Implementierung hat. Wird eine Methode aufgerufen, dann
    wird die Implementierung verwendet, die der spezifischen Klasse des Objekts entspricht, das die Methode aufruft.
    Sie ermöglicht auch die Implementierung von abstrakten Methoden in Oberklassen, die von den Unterklassen
    überschrieben werden müssen, um spezifische Funktionalitäten bereitzustellen.*/

/*In der Objektpolymorphie können Objekte einer Subklasse als Objekte ihrer Superklasse behandelt werden.
    Dies bedeutet, dass ein Objekt einer Subklasse einer Methode übergeben werden kann, die eine Superklasse
    als Parameter erwartet.*/



public class KalenderApp{

    /**
     * Diese Methode ermöglicht Benutzereingaben und steuert die Interaktion mit der Kalenderanwendung.
     * Die Eingaben des Benutzers werden ausgewertet und die ausgewählten Methodenaufrufe werden durchgeführt.
     */
        public static void Benutzereingabe(){
            Scanner scan = new Scanner(System.in);
            boolean nichtBeenden = true;

            while (nichtBeenden) {
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

                switch (input) {
                    case 1:
                        System.out.println("Bitte geben Sie eine Kalenderart (1-Raumkalender, 2-Personenkalender, 3-Gruppenkalender) an: ");
                        String kalenderart = scan.nextLine();

                        if (Objects.equals(kalenderart, "1")) {
                            System.out.println("Bitte geben Sie einen Namen an: ");
                            String rName = scan.nextLine();
                            System.out.println("Bitte geben Sie die Plaetze an: ");
                            int rPlaetze = scan.nextInt();

                            if (!Kalenderserie.kalenderExistiert(rName)) {
                                RaumKalender rkalender = new RaumKalender(rName, rPlaetze);
                                Kalenderserie.addKalender(rkalender);
                            } else {
                                System.out.println("Dieser Kalender existiert bereits.");
                            }
                        }else if (Objects.equals(kalenderart, "2")) {
                            System.out.println("Bitte geben Sie einen Namen an: ");
                            String pName = scan.nextLine();
                            System.out.println("Bitte geben Sie den Besitzer an: ");
                            String pBesitzer = scan.nextLine();

                            if (!Kalenderserie.kalenderExistiert(pName)) {
                                PersonenKalender pkalender = new PersonenKalender(pName, pBesitzer);
                                Kalenderserie.addKalender(pkalender);
                            } else {
                                System.out.println("Dieser Kalender existiert bereits.");
                            }
                        }else if (Objects.equals(kalenderart, "3")) {
                            System.out.println("Bitte geben Sie einen Namen an: ");
                            String gName = scan.nextLine();

                            System.out.println("Bitte geben Sie alle Mitglieder mit Namen an (getrennt durch Komma): ");
                            String mitgliederString = scan.nextLine();
                            String[] mitglieder = mitgliederString.split(",");

                            for (int i = 0; i < mitglieder.length; i++) {
                                mitglieder[i] = mitglieder[i].trim();
                            }

                            if (!Kalenderserie.kalenderExistiert(gName)) {
                                GruppenKalender gkalender = new GruppenKalender(gName, mitglieder);
                                Kalenderserie.addKalender(gkalender);
                            } else {
                                System.out.println("Dieser Kalender existiert bereits.");
                            }
                        }
                        break;
                    case 2:
                        System.out.println("Bitte geben Sie den zu löschenden Kalender an: ");
                        String kalenderLoeschen = scan.nextLine();
                        if (Kalenderserie.kalenderExistiert(kalenderLoeschen)) {
                            Kalenderserie.loescheKalender(kalenderLoeschen);
                        }
                        break;
                    case 3:
                        if (!Kalenderserie.isEmpty()) {
                            Kalenderserie.kalenderlisteAusgeben();
                        } else {
                            System.out.println("Es sind keine Kalender vorhanden.");
                        }
                        break;
                    case 4:
                        System.out.println("Bitte geben Sie den Kalender an, zu dem der Termin hinzugefügt werden soll: ");
                        String name = scan.nextLine();
                        if (Kalenderserie.kalenderExistiert(name)) {
                            Kalenderserie.terminHinzufuegen(name);
                        } else {
                            System.out.println("Der Kalender wurde nicht gefunden.");
                        }
                        break;
                    case 5:
                        System.out.println("Bitte geben Sie den Kalender an, zu dem die Terminserie hinzugefügt werden soll: ");
                        String nameserie = scan.nextLine();
                        if (Kalenderserie.kalenderExistiert(nameserie)) {
                            Kalenderserie.serieHinzufuegen(nameserie);
                        } else {
                            System.out.println("Der Kalender wurde nicht gefunden.");
                        }
                        break;
                    case 6:
                        System.out.println("Bitte geben Sie den Kalender an, dessen Termine ausgegeben werden sollen: ");
                        String kname = scan.nextLine();
                        if (Kalenderserie.kalenderExistiert(kname)) {
                            Kalender kalender = Kalenderserie.returnKalender(kname);
                            KalenderAnzeige anzeige = new TerminalAnzeige(kalender);
                            anzeige.ausgeben();
                        } else {
                            System.out.println("Der Kalender wurde nicht gefunden.");
                        }
                        break;
                    case 7:
                        System.out.println("Bitte geben Sie den Kalender an, der umbenannt werden soll: ");
                        String uname = scan.nextLine();
                        System.out.println("Bitte geben Sie den neuen Namen an: ");
                        String neuername = scan.nextLine();
                        if (Kalenderserie.kalenderExistiert(uname)) {
                            Kalenderserie.kalenderUmbenennen(uname, neuername);
                        } else {
                            System.out.println("Der Kalender wurde nicht gefunden.");
                        }
                        break;
                    case 8:
//                        Termin[] freieTermine = testeKalenderVergleich();
//                        System.out.println("Freie Termine:\n");
//
//                    for(Termin termin : freieTermine){
//                        System.out.println(termin.getInfo());
//                    }
                        break;
                    case 9:
                        System.out.println("KalenderApp wird geschlossen.");
                        nichtBeenden = false;
                        break;
                    default:
                        System.out.println("Bitte waehlen Sie eine gueltige Aktion.");
                }
            }
        }

    /**
     * Diese Methode erstellt eine Instanz der Kalenderserie, ruft die Benutzereingabe-Methode auf
     * und startet damit die Benutzerinteraktion.
     */
        public static void main(String[] args){
            Kalenderserie.getInstance();
            Benutzereingabe();

        }

//    public static kalender.modell.Termin[] testeKalenderVergleich(){
//        int terminDauer = 120;
//        String name = "Freier kalender.modell.Termin";
//
//        Kalender kalender1 = new Kalender("Vorlesungen");
//        Termin terminV1 = new kalender.modell.Termin("EIDOP", LocalDateTime.of(2024, 4, 25, 8, 0),
//                LocalDateTime.of(2024, 4, 25, 10, 0));
//        Termin terminV2 = new kalender.modell.Termin("Matlab", LocalDateTime.of(2024, 4, 25, 12, 0),
//                LocalDateTime.of(2024, 4, 25, 14, 0));
//
//        Termin terminV3 = new kalender.modell.Termin("SoftSkills", LocalDateTime.of(2024, 4, 26, 10, 0),
//                LocalDateTime.of(2024, 4, 26, 12, 0));
//
//        kalender1.addTermin(terminV1);
//        kalender1.addTermin(terminV2);
//        kalender1.addTermin(terminV3);
//
//        Kalender kalender2 = new Kalender("Freizeit");
//        Termin terminF0 = new Termin("Klettern", LocalDateTime.of(2024, 4, 25, 8, 0),
//                LocalDateTime.of(2024, 4, 25, 10, 0));
//        Termin terminF1 = new Termin("Lernen", LocalDateTime.of(2024, 4, 25, 12, 0),
//                LocalDateTime.of(2024, 4, 25, 14, 0));
//
//        Termin terminF2 = new Termin("Zahnarzt", LocalDateTime.of(2024, 4, 26, 10, 0),
//                LocalDateTime.of(2024, 4, 26, 12, 0));
//
//        kalender2.addTermin(terminF0);
//        kalender2.addTermin(terminF1);
//        kalender2.addTermin(terminF2);
//
//        Termin[] freieTermine = kalender1.freieTermineFinden(kalender2, LocalDate.of(2024, 4, 25),
//                LocalDate.of(2024, 4, 26), LocalTime.of(8, 0), LocalTime.of(16, 0),
//                terminDauer, name);
//
//        return freieTermine;
//    }
}