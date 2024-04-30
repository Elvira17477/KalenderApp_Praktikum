import java.util.ArrayList;

public class Terminserie {
    private String name;
    private final Termin[] termine;

    public Terminserie(int anzahl, String name, java.time.LocalDateTime start, java.time.LocalDateTime ende, int intervall){
        termine = new Termin[anzahl];               //deklaration arrayinstanz für # termine

        for (int i = 0; i < anzahl; i++) {
            java.time.LocalDateTime neuerStart = start.plusDays((long) i * intervall); //termindaten in intervallabständen berechnen
            java.time.LocalDateTime neuesEnde = ende.plusDays((long) i * intervall);
            termine[i] = new Termin(name, neuerStart, neuesEnde);              //anzahl terminobjekte erzeugen und initialisieren
        }
    }

    public boolean serienNichtUeberschneiden(Terminserie serie) {
        if (serie == null) return true;

        return false;
    }

    public void setName(String name){
        this.name = name;

        for (int i = 0; i < termine.length; i++) {
            termine[i].setName(name);  //namen aller einzeltermine aktualisieren
        }
    }
    public Termin getTermin(int index){
        if(index >= 0 && index < termine.length){
            return termine[index];
        }
        return null;
    }
    public int getAnzahl(){
        return termine.length;
    }
    public String getName(){
        return name;
    }

}