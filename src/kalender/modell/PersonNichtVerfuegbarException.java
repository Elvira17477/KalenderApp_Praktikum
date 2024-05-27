package kalender.modell;

public class PersonNichtVerfuegbarException extends TerminException{
    String person;
    public PersonNichtVerfuegbarException(String person, Termin termin) {
        super(termin);
        this.person = person;
    }
    public String getPerson() {
        return person;
    }
}
