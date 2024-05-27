package kalender.modell;

public class GruppenKalender extends Kalender{

    /** Die Mitglieder der Gruppe, die Zugriff auf diesen Kalender haben. */
    public String[] mitglieder;

    /**
     * Konstruktor für die Erstellung eines Gruppenkalenders mit Namen und einer Liste von Mitgliedern.
     * @param name Der Name des Gruppenkalenders.
     * @param mitglieder Die Liste der Mitglieder der Gruppe.
     */
    public GruppenKalender(String name, String[] mitglieder){
        super(name);
        if(mitglieder.length == 0 || name == null || name.isEmpty()){
            throw new IllegalArgumentException("Mitglieder und Name sind Pflichtfelder im Gruppenkalender.");
        }
        if (mitglieder.length > 10) {
            throw new IllegalArgumentException("Die maximale Anzahl von 10 Mitgliedern wurde überschritten.");
        }
        this.mitglieder = mitglieder;
    }

    public String[] getMitglieder() {
        return mitglieder;
    }

    /**
     * Überprüft, ob ein Termin zu diesem Gruppenkalender hinzugefügt werden kann.
     * @param termin Der Termin, der überprüft werden soll.
     * @return {@code true}, wenn der Termin hinzugefügt werden kann, ansonsten {@code false}.
     */
    @Override
    public void pruefeHinzufuegen(Termin termin) throws TerminException {

        if (!passtTerminInKalender(termin)) {
            throw new TerminUeberschneidungException(termin);
        }

        for (String mitglied : mitglieder) {
            if(mitglied != null) {
                String[] einzelneMitglieder = mitglied.split(",");
                for(String einzelMitglied : einzelneMitglieder){
                    einzelMitglied = einzelMitglied.trim();
                    PersonenKalender pkalender = PersonenKalender.getKalender(einzelMitglied);
                    if (pkalender.terminVorhanden(einzelMitglied, termin)) {
                        throw new PersonNichtVerfuegbarException(einzelMitglied, termin);
                }
                }
            }
        }

        //termin zu allen Personenkalendern hinzufügen
        for (String mitglied : mitglieder) {
            String[] einzelneMitglieder = mitglied.split(",");
            for(String einzelMitglied : einzelneMitglieder){
                einzelMitglied = einzelMitglied.trim();
                if(!einzelMitglied.isEmpty()) {
                    PersonenKalender.getKalender(einzelMitglied).addTermin(termin);
                }
            }
        }
    }
}
