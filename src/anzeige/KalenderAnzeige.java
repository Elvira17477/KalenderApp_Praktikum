package anzeige;

import kalender.modell.Kalender;

/**
 * Abstrakte Klasse, die eine Schnittstelle für potentiell verschiedene Arten von Kalenderanzeigen definiert.
 * Jede Implementierung bietet eine Möglichkeit, den Kalender und seine Termine anzuzeigen.
 */
public abstract class KalenderAnzeige {

    /** Der Kalender, der von dieser Anzeige dargestellt wird. */
    private final Kalender kalender;

    /**
     * Konstruktor, der den Kalender für die Anzeige festlegt.
     * @param kalender Der Kalender, der angezeigt werden soll.
     */
    protected KalenderAnzeige(Kalender kalender) {
        this.kalender = kalender;
    }

    /**
     * Gibt den Kalender zurück, der von dieser Anzeige dargestellt wird.
     * @return Der dargestellte Kalender.
     */
    //Getter, kein Setter da Komposition
    public Kalender getKalender(){
        return kalender;
    }

    /**
     * Eine abstrakte Methode, die implementiert werden muss, um die Anzeige des Kalenders zu gewährleisten.
     */
    public abstract void ausgeben();
}
