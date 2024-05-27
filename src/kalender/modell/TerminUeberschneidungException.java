package kalender.modell;

public class TerminUeberschneidungException extends TerminException{
    public TerminUeberschneidungException(Termin termin) {
        super(termin);
    }
}
