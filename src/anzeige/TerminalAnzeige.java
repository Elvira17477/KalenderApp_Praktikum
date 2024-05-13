package anzeige;
import kalender.modell.*;

/**
 * Eine Implementierung von KalenderAnzeige, die die Anzeige des Kalenders auf der Konsole durchführt.
 */
public class TerminalAnzeige extends KalenderAnzeige {

    /**
     * Konstruktor, der den Kalender für die Anzeige auf der Konsole festlegt.
     * @param kalender Der Kalender, der auf der Konsole angezeigt werden soll.
     */
    public TerminalAnzeige(Kalender kalender) {
        super(kalender);
    }

    /**
     * Führt die Anzeige der Termine des Kalenders auf der Konsole durch.
     */
    @Override
    public void ausgeben() {
        System.out.println("Kalender: " + getKalender().getName());
        if(getKalender() instanceof RaumKalender){
            System.out.println("Plaetze: " + ((RaumKalender) getKalender()).getPlaetze());
        }
        if(getKalender() instanceof GruppenKalender){
            System.out.print("Mitglieder: ");
            for (String s : (((GruppenKalender) getKalender()).getMitglieder())) {
                System.out.print(s + " ");
            }
        }
        System.out.println("\nTermine:");
        for (Termin termin : getKalender().getTermine()) {
            System.out.println(termin.getInfo());
        }
        System.out.println("Terminserien:");
        for (Terminserie serie : getKalender().getSerien()) {
            for (int i = 0; i < serie.getAnzahl(); i++) {
                System.out.println(serie.getTermin(i).getInfo());
            }
        }
    }
}
