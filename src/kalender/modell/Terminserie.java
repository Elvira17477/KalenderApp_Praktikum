package kalender.modell;


import java.util.ArrayList;

public class Terminserie {
    private String name;
    private final ArrayList<Termin> termine;

    /**
     * Konstruktor zur Erstellung einer Terminserie mit der Anzahl von Terminen, Namen, Start- und Enddatum sowie dem Intervall.
     * @param anzahl Die Anzahl der Termine in der Serie.
     * @param name Der Name der Terminserie.
     * @param start Das Startdatum der Serie.
     * @param ende Das Enddatum der Serie.
     * @param intervall Das Intervall zwischen den Terminen in Tagen.
     */
    public Terminserie(int anzahl, String name, java.time.LocalDateTime start, java.time.LocalDateTime ende, int intervall){
        if(anzahl <= 0 || intervall <= 0){
            throw new IllegalArgumentException("Die Anzahl der Termine und die Intervallgroeße muessen größer als Null sein.");
        }
        if(start == null || ende == null){
            throw new IllegalArgumentException("Das Start- und Enddatum sind Pflichtfelder.");
        }
        if(!start.isBefore(ende)){
            throw new IllegalArgumentException("Das Startdatum der Terminserie muss vor dem Enddatum liegen.");
        }
        termine = new ArrayList<>(anzahl);

        this.name = name;

        for (int i = 0; i < anzahl; i++) {
            java.time.LocalDateTime neuerStart = start.plusDays((long) i * intervall); //termindaten in intervallabständen berechnen
            java.time.LocalDateTime neuesEnde = ende.plusDays((long) i * intervall);
            termine.add(new Termin(name, neuerStart, neuesEnde));              //anzahl terminobjekte erzeugen und initialisieren
        }
    }

    /**
     * Setzt den Namen der Terminserie und aktualisiert den Namen aller einzelnen Termine in der Serie.
     * @param name Der neue Name der Terminserie.
     */
    public void setName(String name){
        this.name = name;

        for (int i = 0; i < termine.size(); i++) {
            termine.get(i).setName(name);  //namen aller einzeltermine aktualisieren
        }
    }

    /**
     * Gibt den Termin an einem bestimmten Index in der Serie zurück.
     * @param index Der Index des gesuchten Termins.
     * @return Der Termin an dem angegebenen Index.
     */
    public Termin getTermin(int index){
        if(index >= 0 && index < termine.size()){
            return termine.get(index);
        }
        return null;
    }

    /**
     * Gibt die Anzahl der Termine in der Serie zurück.
     * @return Die Anzahl der Termine in der Serie.
     */
    public int getAnzahl(){
        return termine.size();
    }

    /**
     * Gibt den Namen der Terminserie zurück.
     * @return Der Name der Terminserie.
     */
    public String getName(){
        return name;
    }

}