import java.util.ArrayList;

public class Kalender{
    private String name;
//    private Termin[] termine;
//    private Terminserie[] serien;
    private ArrayList<Termin> termine;
    private ArrayList<Terminserie> serien;

    public void init(){
        termine = new ArrayList<>();     //leeres terminarray
        serien = new ArrayList<>();      //leeres terminserienarray
    }

    //Kalendername
    public void setName(String name){
        if(!name.isEmpty()){
            this.name = name;
        }
    }
    public void addTermin(Termin termin){
        termine.add(termin);
    }
    public void addSerie(Terminserie serie){
        serien.add(serie);
    }
    public void ausgeben(){
        System.out.println("\n" + name + " ");

        System.out.println("Termine:");
        for(Termin termin : termine) {
            System.out.println(termin.getInfo());
        }

        System.out.println("\nTerminserien:");
        for(Terminserie serie : serien) {
            for(int i = 0; i < serie.getAnzahl(); i++){
                System.out.println(serie.getTermin(i).getInfo());
            }
        }
    }
    //arrays statisch, bei Erweiterung / Reduktion der arraygröße muss vorhandenes array in das neue, angepasste array kopiert werden
    //lösung: dynamische ArrayLists (können aber keine primitiven Datentypen speichern e.g. int, etc) aber Objekte

//    public void init(){
//        termine = new Termin[0];     //leeres terminarray
//        serien = new Terminserie[0]; //leeres terminserienarray
//    }

//    public void addTermin(Termin termin){
//        Termin[] neueTermine = new Termin[termine.length + 1]; //neues array um eine posiion erweitern
//        for (int i = 0; i < termine.length; i++) {             //alle bisherigen Termine kopieren
//            neueTermine[i] = termine[i];
//        }
//        neueTermine[termine.length] = termin;                  //übergebenen Termin einfügen
//        termine = neueTermine;                                 //attribut termine auf neues array setzen
//    }
//    public void addSerie(Terminserie serie){
//        Terminserie[] neueTerminserien = new Terminserie[serien.length + 1];
//        for(int i = 0; i < serien.length; i++) {
//            neueTerminserien[i] = serien[i];
//        }
//        neueTerminserien[serien.length] = serie;
//        serien = neueTerminserien;
//    }
//    public void ausgeben(){
//        System.out.println("\n" + name + " ");
//        System.out.println("Termine:");
//        for (int i = 0; i < termine.length; i++) {
//            System.out.println(termine[i].getInfo());
//        }
//
//        System.out.println("\nTerminserien:");
//        for (int i = 0; i < serien.length; i++) {                     //array serien enthält Terminserien arrays
//            //System.out.println("Serie: " + serien[i].getName());
//            for (int j = 0; j < serien[i].getAnzahl(); j++) {         //anzahl der Terminserien im array durchlaufen
//                System.out.println(serien[i].getTermin(j).getInfo()); //je serie jede Termininfo ausgeben
//            }
//        }
//    }
}