package kalender.modell;

public class GruppenKalender extends Kalender{

    /** Die Mitglieder der Gruppe, die Zugriff auf diesen Kalender haben. */
    public String[] mitglieder;

    /**
     * Konstruktor für die Erstellung eines Gruppenkalenders mit Namen und einer Liste von Mitgliedern.
     * @param name Der Name des Gruppenkalenders.
     * @param mitglieder Die Liste der Mitglieder der Gruppe.
     */
    public GruppenKalender(String name, String[] mitglieder) throws Exception {
        super(name);
        this.mitglieder = new String[10];
    }

    public void addMitglied(String name){

    }

    /**
     * Überprüft, ob ein Termin zu diesem Gruppenkalender hinzugefügt werden kann.
     * @param termin Der Termin, der überprüft werden soll.
     * @return {@code true}, wenn der Termin hinzugefügt werden kann, ansonsten {@code false}.
     */
    @Override
    public boolean pruefeHinzufuegen(Termin termin) {

        if (!passtTerminInKalender(termin)) {
            return false;
        }

        for (String mitglied : mitglieder) {
            if(mitglied != null) {
                PersonenKalender pkalender = PersonenKalender.getKalender(mitglied);
                if (pkalender.terminVorhanden(mitglied, termin)) {
                    return false;
                }
            }
        }

        //termin zu allen Personenkalendern hinzufügen
        for (int i = 0; i < mitglieder.length; i++) {
            if(mitglieder[i] != null) {
                PersonenKalender.getKalender(mitglieder[i]).addTermin(termin);
            }
        }
        return true;
    }
}
