package kalender.modell;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;
import static java.time.LocalDateTime.parse;

/**
 * Diese Klasse repräsentiert eine Sammlung von Kalendern und bietet Methoden
 * zum Hinzufügen, Löschen, Umbenennen und Verwalten von Kalendern.
 */
public class Kalenderserie {
    static Kalenderserie kalenderserie;
    static ArrayList<Kalender> kalenderarray;

    /**
     * Gibt die einzige Instanz der Kalenderserie zurück, um sicherzustellen,
     * dass nur eine Instanz existiert.
     * @return Die Instanz der Kalenderserie.
     */
    public static Kalenderserie getInstance(){
        if(kalenderserie == null){
            kalenderserie = new Kalenderserie();
        }
        return kalenderserie;
    }

    /**
     * Privater Konstruktor, der ein neues Kalenderarray mit einer bestimmten Größe initialisiert.
     */
    private Kalenderserie(){
        kalenderarray = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            kalenderarray.add(null);
        }
    }

    /**
     * Gibt einen bestimmten Kalender zurück, der mit einem angegebenen Namen übereinstimmt.
     * @param kname Der Name des gesuchten Kalenders.
     * @return Der gefundene Kalender oder null, wenn kein Kalender mit diesem Namen gefunden wurde.
     */
    public static Kalender returnKalender(String kname) {
        for (int i = 0; i < Kalenderserie.kalenderarray.size(); i++) {
            if (Objects.equals(Kalenderserie.kalenderarray.get(i).getName(), kname)){
                return Kalenderserie.kalenderarray.get(i);
            }
        }
        return null;
    }

    /**
     * Fügt einen neuen Kalender zur Kalenderserie hinzu.
     * @param kalender Der hinzuzufügende Kalender.
     */
    public static void addKalender(Kalender kalender){
        for(int i = 0; i < kalenderarray.size(); i++) {
            if (kalenderarray.get(i) == null) {
                kalenderarray.set(i, kalender);
                System.out.println("Der Kalender wurde erfolgreich hinzugefügt.");
                return;
            }
        }
        System.out.println("Die maximale Anzahl von 10 Kalendern wurde erreicht.");
    }

    /**
     * Löscht einen Kalender aus der Kalenderserie.
     * @param kalenderLoeschen Der Name des zu löschenden Kalenders.
     */
    public static void loescheKalender(String kalenderLoeschen){
        for (int i = 0; i < kalenderarray.size(); i++) {
            if(kalenderarray.get(i).getName().equals(kalenderLoeschen)){
                kalenderarray.set(i, null);
                System.out.println("Kalender wurde gelöscht.");
                return;
            }
        }
        System.out.println("Der Kalender " + kalenderLoeschen + " existiert nicht.");
    }

    /**
     * Gibt die Liste aller Kalender in dieser Kalenderserie aus.
     */
    public static void kalenderlisteAusgeben(){
        System.out.println("Kalenderliste:\n");
        for (int i = 0; i < kalenderarray.size(); i++) {
            if(kalenderarray.get(i) != null)
                System.out.println(kalenderarray.get(i).getName());
        }
    }

    /**
     * Fügt einen neuen Termin zu einem bestimmten Kalender in der Kalenderserie hinzu.
     * @param kalendername Der Name des Kalenders, zu dem der Termin hinzugefügt werden soll.
     */
    public static void terminHinzufuegen(String kalendername) throws TerminException, InvalidDateFormatException {
        Scanner scan = new Scanner(System.in);

        for (int i = 0; i < kalenderarray.size(); i++) {
            if (Objects.equals(kalenderarray.get(i).getName(), kalendername)) {

                System.out.println("Bitte geben Sie den Terminnamen an: ");
                String termin = scan.nextLine();

                LocalDateTime parsedstart = null;
                LocalDateTime parsedend = null;

                while (parsedstart == null) {
                    System.out.println("Bitte geben Sie das Startdatum an (\"YYYY-MM-DD HH:mm\"): ");
                    String startdatum = scan.nextLine();
                    try {
                        parsedstart = LocalDateTime.parse(startdatum.replace(" ", "T"));
                    } catch (DateTimeParseException e) {
                        throw new InvalidDateFormatException("Ungültiges Startdatum: " + startdatum);
                    }
                }
                while (parsedend == null) {
                    System.out.println("Bitte geben Sie das Enddatum an (\"YYYY-MM-DD HH:mm\"): ");
                    String enddatum = scan.nextLine();
                    try {
                        parsedend = LocalDateTime.parse(enddatum.replace(" ", "T"));
                    } catch (DateTimeParseException e) {
                        throw new InvalidDateFormatException("Ungültiges Enddatum: " + enddatum);
                    }
                }
                Termin neuerTermin = new Termin(termin, parsedstart, parsedend);
                kalenderarray.get(i).addTermin(neuerTermin);
                return;
            }
        }
        System.out.println("Der Kalender '" + kalendername + "' existiert nicht.");
    }

    /**
     * Fügt eine neue Terminserie zu einem bestimmten Kalender in der Kalenderserie hinzu.
     * @param nameserie Der Name des Kalenders, zu dem die Terminserie hinzugefügt werden soll.
     */
    public static void serieHinzufuegen(String nameserie) throws InvalidDateFormatException {
        Scanner scan = new Scanner(System.in);

        for (int i = 0; i < kalenderarray.size(); i++) {
            if (Objects.equals(kalenderarray.get(i).getName(), nameserie)) {

                System.out.println("Bitte geben Sie Terminanzahl an: ");
                int anzahl = 0;
                while (true) {
                    try {
                        anzahl = Integer.parseInt(scan.nextLine());
                        if (anzahl <= 0) {
                            System.out.println("Die Anzahl der Termine muss eine positive Zahl sein.");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException ex) {
                        System.out.println("Bitte geben Sie eine gültige Anzahl der Termine an.");
                    }
                }

                System.out.println("Bitte geben Sie den Terminnamen an: ");
                String termin = scan.nextLine();

                LocalDateTime parsedstart = null;
                LocalDateTime parsedend = null;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                while (parsedstart == null) {
                    System.out.println("Bitte geben Sie das Startdatum an (\"YYYY-MM-DD HH:mm\"): ");
                    String startdatum = scan.nextLine();
                    try {
                        parsedstart = LocalDateTime.parse(startdatum, formatter);
                    } catch (DateTimeParseException ex) {
                        throw new InvalidDateFormatException("Ungültiges Startdatum: " + startdatum);
                    }
                }

                while (parsedend == null) {
                    System.out.println("Bitte geben Sie das Enddatum an (\"YYYY-MM-DD HH:mm\"): ");
                    String enddatum = scan.nextLine();
                    try {
                        parsedend = LocalDateTime.parse(enddatum, formatter);
                    } catch (DateTimeParseException ex) {
                        throw new InvalidDateFormatException("Ungültiges Enddatum: " + enddatum);
                    }
                }

                System.out.println("Bitte geben Sie das Intervall in Tagen an: ");
                int intervall = 0;
                while (true) {
                    try {
                        intervall = Integer.parseInt(scan.nextLine());
                        if (intervall <= 0) {
                            System.out.println("Das Intervall muss eine positive Zahl sein.");
                            continue;
                        }
                        break;
                    } catch (NumberFormatException ex) {
                        System.out.println("Bitte geben Sie ein gültiges Intervall (eine positive Ganzzahl) an.");
                    }
                }

                Terminserie neueSerie = new Terminserie(anzahl, termin, parsedstart, parsedend, intervall);
                kalenderarray.get(i).addSerie(neueSerie);
                System.out.println("Die Terminserie " + termin + " wurde zum Kalender '" + nameserie + "' hinzugefügt.");
                return;
            }
        }
        System.out.println("Der Kalender " + nameserie + " existiert nicht.");
    }

//    public static void serieHinzufuegen(String nameserie) throws InvalidDateFormatException{
//        Scanner scan = new Scanner(System.in);
//
//        for (int i = 0; i < kalenderarray.size(); i++) {
//            if(Objects.equals(kalenderarray.get(i).getName(), nameserie)){
//
//                System.out.println("Bitte geben Sie Terminanzahl an: ");
//                int anzahl = 0;
//                while(true) {
//                    try {
//                        anzahl = scan.nextInt();
//                        scan.nextLine();
//                        if (anzahl <= 0) {
//                            System.out.println("Die Anzahl der Termine muss eine positive Zahl sein.");
//                            continue;
//                        }
//                        break;
//                    } catch (InputMismatchException ex) {
//                        System.out.println("Bitte geben Sie die Anzahl der Termine an.");
//                    }
//                }
//
//                System.out.println("Bitte geben Sie den Terminnamen an: ");
//                String termin = scan.nextLine();
//
//                LocalDateTime parsedstart = null;
//                LocalDateTime parsedend = null;
//
//                while(parsedstart == null){
//                    System.out.println("Bitte geben Sie das Startdatum an (\"YYYY-MM-DD HH:mm\"): ");
//                    String startdatum = scan.nextLine();
//
//                    try{
//                        parsedstart = parse(startdatum.replace(" ", "T"));
//                    }catch(DateTimeParseException ex){
//                        System.out.println("Bitte geben Sie ein valides Startdatum der Form \"YYYY-MM-DD HH:mm\" an.");
//                    }
//                }
//                while(parsedend == null){
//                    System.out.println("Bitte geben Sie das Enddatum an (\"YYYY-MM-DD HH:mm\"): ");
//                    String enddatum = scan.nextLine();
//
//                    try{
//                        parsedend = parse(enddatum.replace(" ", "T"));
//                    }catch(DateTimeParseException ex){
//                        System.out.println("Bitte geben Sie ein valides Enddatum der Form \"YYYY-MM-DD HH:mm\" an.");
//                    }
//                }
//
//                System.out.println("Bitte geben Sie das Intervall in Tagen an: ");
//                int intervall;
//                while(true) {
//                    try {
//                        String input = scan.next();
//                        intervall = Integer.parseInt(input);
//                        if(intervall <= 0){
//                            continue;
//                        }
//                        break;
//                    } catch (InputMismatchException ex) {
//                        System.out.println("Bitte geben Sie ein gueltiges Intervall (eine positive Ganzzahl) an.");
//                    }
//                }
//
//                Terminserie neueSerie = new Terminserie(anzahl, termin, parsedstart, parsedend, intervall);
//                kalenderarray.get(i).addSerie(neueSerie);
//                System.out.println("Die Terminserie " + termin + " wurde zum Kalender '" + nameserie + "' hinzugefügt.");
//                return;
//            }
//        }
//        System.out.println("Der Kalender " + nameserie + " existiert nicht.");
//    }

    /**
     * Benennt einen Kalender in der Kalenderserie um.
     * @param name Der aktuelle Name des Kalenders.
     * @param neuername Der neue Name für den Kalender.
     */
    public static void kalenderUmbenennen(String name, String neuername){
        for (int i = 0; i < kalenderarray.size(); i++) {
            if(Objects.equals(kalenderarray.get(i).getName(), name)){
                kalenderarray.get(i).setName(neuername);
                break;
            }
        }
    }

    /**
     * Überprüft, ob die Kalenderserie leer ist.
     * @return true, wenn die Kalenderserie leer ist, andernfalls false.
     */
    public static boolean isEmpty(){
        for (int i = 0; i < kalenderarray.size(); i++) {
            if (kalenderarray.get(i) != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Überprüft, ob ein Kalender mit einem bestimmten Namen in der Kalenderserie existiert.
     * @param name Der Name des zu überprüfenden Kalenders.
     * @return true, wenn ein Kalender mit diesem Namen existiert, andernfalls false.
     */
    public static boolean kalenderExistiert(String name){
        for (int i = 0; i < kalenderarray.size(); i++) {
            if(kalenderarray.get(i) == null) {
                return false;
            }else if(kalenderarray.get(i).getName().equals(name)){
                return true;
            }
        }
        return false;
    }
}