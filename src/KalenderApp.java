import java.time.LocalDateTime;

public class KalenderApp{
    public static void main(String[] args){
        Kalender kalender1 = new Kalender();
        kalender1.init();
        Kalender kalender2 = new Kalender();
        kalender2.init();

        Termin termin1 = new Termin();
        termin1.init("Zahnarzt", LocalDateTime.of(2024, 03, 30, 17, 30),
                LocalDateTime.of(2024, 03, 30, 18, 00));
        Termin termin2 = new Termin();
        termin2.init("Klettern", LocalDateTime.of(2024, 04, 01, 18, 00),
                LocalDateTime.of(2024, 04, 01, 20, 00));
        Termin termin3 = new Termin();
        termin3.init("Geburtstag", LocalDateTime.of(2024, 03, 25, 10, 00),
                LocalDateTime.of(2024, 03, 25, 12, 00));


        Terminserie terminserie1 = new Terminserie();
        terminserie1.init(7, "Vorlesung", LocalDateTime.of(2024, 03, 28, 10, 30),
                LocalDateTime.of(2024, 03, 28, 12, 30), 7);

        Terminserie terminserie2 = new Terminserie();
        terminserie2.init(7, "Praktikum", LocalDateTime.of(2024, 03, 29, 14, 15),
                LocalDateTime.of(2024, 03, 29, 15, 45), 7);

        kalender1.addTermin(termin1);
        kalender1.addTermin(termin2);
        kalender1.addSerie(terminserie1);

        System.out.println("\nKalender1: ");
        kalender1.ausgeben();

        kalender2.addTermin(termin3);
        kalender2.addSerie(terminserie2);

        System.out.println("\n\nKalender2: ");
        kalender2.ausgeben();
    }
}