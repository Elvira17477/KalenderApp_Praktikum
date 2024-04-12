public class Kalender{
    private String name;
    private Termin[] termine;
    private Terminserie[] serien;

    public void init(){
        termine = new Termin[0];
        serien = new Terminserie[0];
    }
    //Kalendername
    public void setName(String name){
        if(!name.isEmpty()){
            this.name = name;
        }
    }
    public void addTermin(Termin termin){
        Termin[] neueTermine = new Termin[termine.length + 1]; //neues array um eine posiion erweitern
        for (int i = 0; i < termine.length; i++) {             //alle bisherigen Termine kopieren
            neueTermine[i] = termine[i];
        }
        neueTermine[termine.length] = termin;                  //übergebenen Termin einfügen
        termine = neueTermine;                                 //attribut termine auf neues array setzen
    }
    public void addSerie(Terminserie serie){
        Terminserie[] neueTerminserien = new Terminserie[serien.length + 1];
        for(int i = 0; i < serien.length; i++) {
            neueTerminserien[i] = serien[i];
        }
        neueTerminserien[serien.length] = serie;
        serien = neueTerminserien;
    }
    public void ausgeben(){
        //System.out.println(name + " ");
        System.out.println("Termine:");
        for (int i = 0; i < termine.length; i++) {
            System.out.println(termine[i].getInfo());
        }

        System.out.println("\nTerminserien:");
        for (int i = 0; i < serien.length; i++) {                     //array serien enthält Terminserien arrays
            //System.out.println("Serie: " + serien[i].getName());
            for (int j = 0; j < serien[i].getAnzahl(); j++) {         //anzahl der Terminserien im array durchlaufen
                System.out.println(serien[i].getTermin(j).getInfo()); //je serie jede Termininfo ausgeben
            }
        }
    }
}