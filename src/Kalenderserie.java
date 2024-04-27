import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Scanner;

public class Kalenderserie {
    private static Kalenderserie kalenderserie;
    static Kalender[] kalenderarray;

    public static Kalenderserie getInstance(){
        if(kalenderserie == null){
            kalenderserie = new Kalenderserie();
        }
        return kalenderserie;
    }
    private Kalenderserie(){
        kalenderarray = new Kalender[10];
    }
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
    public static void kalenderlisteAusgeben(){
        System.out.println("Kalenderliste:\n");
        for (int i = 0; i < kalenderarray.length; i++) {
            if(kalenderarray[i] != null)
                System.out.println(kalenderarray[i].getName());
        }
    }
    //Kommunikation in die main()
    public static void terminHinzufuegen(String kalendername){
        Scanner scan = new Scanner(System.in);

        for (int i = 0; i < kalenderarray.length; i++) {
            if(Objects.equals(kalenderarray[i].getName(), kalendername)){

                System.out.println("Bitte geben Sie den Terminnamen an: ");
                String termin = scan.nextLine();

                System.out.println("Bitte geben Sie das Startdatum an (\"YYYY-MM-DD HH:MM\"): ");
                String startdatum = scan.nextLine();
                java.time.LocalDateTime parsedstart = LocalDateTime.parse(startdatum.replace(" ", "T"));

                System.out.println("Bitte geben Sie das Enddatum an (\"YYYY-MM-DD HH:MM\"): ");
                String enddatum = scan.nextLine();
                java.time.LocalDateTime parsedend = LocalDateTime.parse(enddatum.replace(" ", "T"));

                Termin neuerTermin = new Termin(termin, parsedstart, parsedend);
                kalenderarray[i].addTermin(neuerTermin);
                System.out.println("Der Termin " + termin + " wurde zum Kalender '" + kalendername + "' hinzugefügt.");
                return;
            }
        }
    }
    public static void serieHinzufuegen(String nameserie){
        Scanner scan = new Scanner(System.in);

        for (int i = 0; i < kalenderarray.length; i++) {
            if(Objects.equals(kalenderarray[i].getName(), nameserie)){

                System.out.println("Bitte geben Sie Terminanzahl an: ");
                int anzahl = scan.nextInt();

                System.out.println("Bitte geben Sie den Terminnamen an: ");
                String termin = scan.nextLine();

                System.out.println("Bitte geben Sie das Startdatum an: ");
                String startdatum = scan.nextLine();
                java.time.LocalDateTime parsedstart = LocalDateTime.parse(startdatum.replace("T", " "));

                System.out.println("Bitte geben Sie das Enddatum an: ");
                String enddatum = scan.nextLine();
                java.time.LocalDateTime parsedend = LocalDateTime.parse(enddatum.replace("T", " "));

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
    public static void kalenderUmbenennen(String name, String neuername){
        for (int i = 0; i < kalenderarray.length; i++) {
            if(Objects.equals(kalenderarray[i].getName(), name)){
                kalenderarray[i].setName(neuername);
            }
        }
    }
    public static boolean isEmpty(){
        for (int i = 0; i < kalenderarray.length; i++) {
            if (kalenderarray[i] != null) {
                return false;
            }
        }
        return true;
    }
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