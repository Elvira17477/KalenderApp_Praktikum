package kalender.modell;

public class RaumKalender extends Kalender {
    public int plaetze;

    /**
     * Konstruktor für die Erstellung eines Raumkalenders mit Namen und der Anzahl von Plätzen.
     * @param name Der Name des Raumkalenders.
     * @param plaetze Die Anzahl der Plätze im Raum.
     */
    public RaumKalender(String name, int plaetze) {
        super(name);
        this.plaetze = plaetze;
    }

    public int getPlaetze(){
        return plaetze;
    }

    /**
     * Überprüft, ob ein Termin zu diesem Raumkalender hinzugefügt werden kann.
     * @param termin Der Termin, der überprüft werden soll.
     * @return true, wenn der Termin hinzugefügt werden kann, ansonsten false.
     */
    @Override
    public boolean pruefeHinzufuegen(Termin termin) {
        if(!passtTerminInKalender(termin)){
            return false;
        }
        return true;
    }
}
