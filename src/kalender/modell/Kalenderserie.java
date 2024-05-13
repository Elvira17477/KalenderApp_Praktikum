package kalender.modell;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Scanner;
import static java.time.LocalDateTime.parse;

/**
 * Diese Klasse repräsentiert eine Sammlung von Kalendern und bietet Methoden
 * zum Hinzufügen, Löschen, Umbenennen und Verwalten von Kalendern.
 */
public class Kalenderserie {
    private static Kalenderserie kalenderserie;
    static Kalender[] kalenderarray;

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
        kalenderarray = new Kalender[10];
    }

    /**
     * Gibt einen bestimmten Kalender zurück, der mit einem angegebenen Namen übereinstimmt.
     * @param kname Der Name des gesuchten Kalenders.
     * @return Der gefundene Kalender oder null, wenn kein Kalender mit diesem Namen gefunden wurde.
     */
    public static Kalender returnKalender(String kname) {
        for (int i = 0; i < Kalenderserie.kalenderarray.length; i++) {
            if (Objects.equals(Kalenderserie.kalenderarray[i].getName(), kname)){
                return Kalenderserie.kalenderarray[i];
            }
        }
        return null;
    }

    /**
     * Fügt einen neuen Kalender zur Kalenderserie hinzu.
     * @param kalender Der hinzuzufügende Kalender.
     */
    public static void addKalender(Kalender kalender){
        for(int i = 0; i < kalenderarray.length; i++) {
            if (kalenderarray[i] == null) {
                kalenderarray[i] = kalender;
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
        for (int i = 0; i < kalenderarray.length; i++) {
            if(Objects.equals(kalenderarray[i].getName(), kalenderLoeschen)){
                kalenderarray[i] = null;
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
        for (int i = 0; i < kalenderarray.length; i++) {
            if(kalenderarray[i] != null)
                System.out.println(kalenderarray[i].getName());
        }
    }

    /**
     * Fügt einen neuen Termin zu einem bestimmten Kalender in der Kalenderserie hinzu.
     * @param kalendername Der Name des Kalenders, zu dem der Termin hinzugefügt werden soll.
     */
    public static void terminHinzufuegen(String kalendername){
        Scanner scan = new Scanner(System.in);

        for (int i = 0; i < kalenderarray.length; i++) {
            if(Objects.equals(kalenderarray[i].getName(), kalendername)) {

                System.out.println("Bitte geben Sie den Terminnamen an: ");
                String termin = scan.nextLine();

                boolean startdatumValide = false;
                LocalDateTime parsedstart = null;
                while(!startdatumValide){
                    System.out.println("Bitte geben Sie das Startdatum an (\"YYYY-MM-DD HH:mm\"): ");
                    String startdatum = scan.nextLine();

                    if(startdatum.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")){
                        parsedstart = parse(startdatum.replace(" ", "T"));
                        startdatumValide = true;
                    }else{
                        System.out.println("Bitte geben Sie ein valides Startdatum der Form \"YYYY-MM-DD HH:mm\" an.");
                    }
                }

                boolean enddatumValide = false;
                LocalDateTime parsedend = null;
                while(!enddatumValide){
                    System.out.println("Bitte geben Sie das Enddatum an (\"YYYY-MM-DD HH:mm\"): ");
                    String enddatum = scan.nextLine();
                    if(enddatum.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")){
                        parsedend = parse(enddatum.replace(" ", "T"));
                        enddatumValide = true;
                    }else{
                        System.out.println("Bitte geben Sie ein valides Enddatum der Form \"YYYY-MM-DD HH:mm\" an.");
                    }
                }

                Termin neuerTermin = new Termin(termin, parsedstart, parsedend);
                kalenderarray[i].addTermin(neuerTermin);
                //System.out.println("Der kalender.modell.Termin " + termin + " wurde zum kalender.modell.Kalender '" + kalendername + "' hinzugefügt.");
                return;
            }
        }
    }

    /**
     * Fügt eine neue Terminserie zu einem bestimmten Kalender in der Kalenderserie hinzu.
     * @param nameserie Der Name des Kalenders, zu dem die Terminserie hinzugefügt werden soll.
     */
    public static void serieHinzufuegen(String nameserie){
        Scanner scan = new Scanner(System.in);

        for (int i = 0; i < kalenderarray.length; i++) {
            if(Objects.equals(kalenderarray[i].getName(), nameserie)){

                System.out.println("Bitte geben Sie Terminanzahl an: ");
                int anzahl = scan.nextInt();
                scan.nextLine();

                System.out.println("Bitte geben Sie den Terminnamen an: ");
                String termin = scan.nextLine();

                boolean startdatumValide = false;
                LocalDateTime parsedstart = null;
                while(!startdatumValide){
                    System.out.println("Bitte geben Sie das Startdatum an (\"YYYY-MM-DD HH:mm\"): ");
                    String startdatum = scan.nextLine();

                    if(startdatum.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")){
                        parsedstart = parse(startdatum.replace(" ", "T"));
                        startdatumValide = true;
                    }else{
                        System.out.println("Bitte geben Sie ein valides Startdatum der Form \"YYYY-MM-DD HH:mm\" an.");
                    }
                }
                boolean enddatumValide = false;
                LocalDateTime parsedend = null;
                while(!enddatumValide){
                    System.out.println("Bitte geben Sie das Enddatum an (\"YYYY-MM-DD HH:mm\"): ");
                    String enddatum = scan.nextLine();
                    if(enddatum.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")){
                        parsedend = parse(enddatum.replace(" ", "T"));
                        enddatumValide = true;
                    }else{
                        System.out.println("Bitte geben Sie ein valides Enddatum der Form \"YYYY-MM-DD HH:mm\" an.");
                    }
                }

                System.out.println("Bitte geben Sie das Intervall an: ");
                int intervall = scan.nextInt();

                Terminserie neueSerie = new Terminserie(anzahl, termin, parsedstart, parsedend, intervall);
                kalenderarray[i].addSerie(neueSerie);
                System.out.println("Die Terminserie " + termin + " wurde zum Kalender '" + nameserie + "' hinzugefügt.");
                return;
            }
        }
        System.out.println("Der Kalender " + nameserie + " existiert nicht.");
    }

    /**
     * Benennt einen Kalender in der Kalenderserie um.
     * @param name Der aktuelle Name des Kalenders.
     * @param neuername Der neue Name für den Kalender.
     */
    public static void kalenderUmbenennen(String name, String neuername){
        for (int i = 0; i < kalenderarray.length; i++) {
            if(Objects.equals(kalenderarray[i].getName(), name)){
                kalenderarray[i].setName(neuername);
                break;
            }
        }
    }

    /**
     * Überprüft, ob die Kalenderserie leer ist.
     * @return true, wenn die Kalenderserie leer ist, andernfalls false.
     */
    public static boolean isEmpty(){
        for (int i = 0; i < kalenderarray.length; i++) {
            if (kalenderarray[i] != null) {
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
        for (int i = 0; i < kalenderarray.length; i++) {
            if(kalenderarray[i] == null) {
                return false;
            }else if(Objects.equals(kalenderarray[i].getName(), name)){
                return true;
            }
        }
        return false;
    }
}