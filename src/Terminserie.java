import java.util.ArrayList;

public class Terminserie {
    private String name;
    //private Termin[] termine;
    private ArrayList<Termin> termine;

    public void init(int anzahl, String name, java.time.LocalDateTime start, java.time.LocalDateTime ende, int intervall){
        termine = new ArrayList<>(anzahl);

        for (int i = 0; i < anzahl; i++) {
            java.time.LocalDateTime neuerStart = start.plusDays((long) i * intervall);
            java.time.LocalDateTime neuesEnde = ende.plusDays((long) i * intervall);
            Termin termin = new Termin();
            termin.init(name, neuerStart, neuesEnde);
            termine.add(termin);
        }
    }
    public void setName(String name){
        this.name = name;

        for (Termin termin : termine) {
            termin.setName(name);
        }
    }
    public Termin getTermin(int index){
        if(index >= 0 && index < termine.size()){
            return termine.get(index);
        }
        return null;
    }
    public int getAnzahl(){
        return termine.size();
    }
//    public void init(int anzahl, String name, java.time.LocalDateTime start, java.time.LocalDateTime ende, int intervall){
//        termine = new Termin[anzahl];               //deklaration arrayinstanz für # termine
//
//        for (int i = 0; i < anzahl; i++) {
//            java.time.LocalDateTime neuerStart = start.plusDays((long) i * intervall); //termindaten in intervallabständen berechnen
//            java.time.LocalDateTime neuesEnde = ende.plusDays((long) i * intervall);
//            termine[i] = new Termin();              //anzahl terminobjekte erzeugen und initialisieren
//            termine[i].init(name, neuerStart, neuesEnde);
//        }
//    }
//    public void setName(String name){
//        this.name = name;
//
//        for (int i = 0; i < termine.length; i++) {
//            termine[i].setName(name);  //namen aller einzeltermine aktualisieren
//        }
//    }
//    public Termin getTermin(int index){
//        if(index >= 0 && index < termine.length){
//            return termine[index];
//        }
//        return null;
//    }
//    public int getAnzahl(){
//        return termine.length;
//    }

    public String getName(){
        return name;
    }

}