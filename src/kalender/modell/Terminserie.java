package kalender.modell;


public class Terminserie {
    private String name;
    private final Termin[] termine;

    /**
     * Konstruktor zur Erstellung einer Terminserie mit der Anzahl von Terminen, Namen, Start- und Enddatum sowie dem Intervall.
     * @param anzahl Die Anzahl der Termine in der Serie.
     * @param name Der Name der Terminserie.
     * @param start Das Startdatum der Serie.
     * @param ende Das Enddatum der Serie.
     * @param intervall Das Intervall zwischen den Terminen in Tagen.
     */
    public Terminserie(int anzahl, String name, java.time.LocalDateTime start, java.time.LocalDateTime ende, int intervall){
        termine = new Termin[anzahl];

        for (int i = 0; i < anzahl; i++) {
            java.time.LocalDateTime neuerStart = start.plusDays((long) i * intervall); //termindaten in intervallabständen berechnen
            java.time.LocalDateTime neuesEnde = ende.plusDays((long) i * intervall);
            termine[i] = new Termin(name, neuerStart, neuesEnde);              //anzahl terminobjekte erzeugen und initialisieren
        }
    }

    /**
     * Setzt den Namen der Terminserie und aktualisiert den Namen aller einzelnen Termine in der Serie.
     * @param name Der neue Name der Terminserie.
     */
    public void setName(String name){
        this.name = name;

        for (int i = 0; i < termine.length; i++) {
            termine[i].setName(name);  //namen aller einzeltermine aktualisieren
        }
    }

    /**
     * Gibt den Termin an einem bestimmten Index in der Serie zurück.
     * @param index Der Index des gesuchten Termins.
     * @return Der Termin an dem angegebenen Index.
     */
    public Termin getTermin(int index){
        if(index >= 0 && index < termine.length){
            return termine[index];
        }
        return null;
    }

    /**
     * Gibt die Anzahl der Termine in der Serie zurück.
     * @return Die Anzahl der Termine in der Serie.
     */
    public int getAnzahl(){
        return termine.length;
    }

    /**
     * Gibt den Namen der Terminserie zurück.
     * @return Der Name der Terminserie.
     */
    public String getName(){
        return name;
    }

}