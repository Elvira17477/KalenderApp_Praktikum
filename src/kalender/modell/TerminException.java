package kalender.modell;

public class TerminException extends Exception {
    Termin termin;
    public TerminException(Termin termin){
        this.termin = termin;
    }
    public Termin getTermin(){
        return termin;
    }

}
