package kalender.modell;

import java.util.Objects;

public class PersonenKalender extends Kalender{

    /** Ein Array, das alle vorhandenen Personenkalender enthält. */
    public static PersonenKalender[] alle = new PersonenKalender[100];

    /** Der Besitzer des Personenkalenders. */
    public String besitzer;

    /**
     * Konstruktor für die Erstellung eines Personenkalenders mit Namen und dem Besitzer.
     * @param name Der Name des Personenkalenders.
     * @param besitzer Der Besitzer des Personenkalenders.
     */
    public PersonenKalender(String name, String besitzer) {
        super(name);
        this.besitzer = besitzer;
        addPersonenKalender(this);
    }

    /**
     * Fügt einen Personenkalender zum Array der vorhandenen Personenkalender hinzu.
     * @param pkalender Der Personenkalender, der hinzugefügt werden soll.
     */
    //static: gehört zur Klasse selbst, nicht zu einer Instanz der Klasse
    //ohne static müsste die Methode von einer Instanz der Klasse kalender.modell.PersonenKalender aufgerufen werden
    private static void addPersonenKalender(PersonenKalender pkalender){
        for (int i = 0; i < alle.length; i++) {
            if(alle[i] == null){
                alle[i] = pkalender;
                break;
            }
        }
    }

    /**
     * Überprüft, ob ein Termin im Kalender eines bestimmten Besitzers vorhanden ist.
     * @param besitzer Der Besitzer des Kalenders, der überprüft werden soll.
     * @param termin Der Termin, der überprüft werden soll.
     * @return true, wenn der Termin im Kalender vorhanden ist, ansonsten false.
     */
    public static boolean terminVorhanden(String besitzer, Termin termin){
        for (int i = 0; i < alle.length; i++) {
            if(alle[i] != null && Objects.equals(alle[i].besitzer, besitzer)){
                if (alle[i].passtTerminInKalender(termin)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gibt den ersten Personenkalender eines bestimmten Besitzers zurück. Wenn kein Kalender für den Besitzer vorhanden ist, wird ein neuer erstellt.
     * @param besitzer Der Besitzer des Kalenders, der zurückgegeben werden soll.
     * @return Der erste Personenkalender des Besitzers.
     */
    public static PersonenKalender getKalender(String besitzer){
        for(PersonenKalender pkalender : alle){
            if(pkalender != null && Objects.equals(pkalender.besitzer, besitzer)){
                return pkalender;
            }
        }
        PersonenKalender pkalender = new PersonenKalender(besitzer, besitzer);
        addPersonenKalender(pkalender);
        return pkalender;
    }

    /**
     * Überprüft, ob ein Termin zu diesem Personenkalender hinzugefügt werden kann.
     * @param termin Der Termin, der überprüft werden soll.
     * @return true, wenn der Termin hinzugefügt werden kann, ansonsten false.
     */
    @Override
    public boolean pruefeHinzufuegen(Termin termin) {
        return true;
    }
}
