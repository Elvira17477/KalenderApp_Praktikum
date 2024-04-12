public class Terminserie {
    private String name;
    private Termin[] termine;

    public void init(int anzahl, String name, java.time.LocalDateTime start, java.time.LocalDateTime ende, int intervall){
        termine = new Termin[anzahl];

        for (int i = 0; i < anzahl; i++) {
            java.time.LocalDateTime neuerStart = start.plusDays((long) i * intervall);
            java.time.LocalDateTime neuesEnde = ende.plusDays((long) i * intervall);
            termine[i] = new Termin();
            termine[i].init(name, neuerStart, neuesEnde);
        }
    }
    public void setName(String name){
        this.name = name;
        for (int i = 0; i < termine.length; i++) {
            termine[i].setName(name);
        }
    }
    public String getName(){
        return name;
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

}