package anzeige;
import kalender.modell.*;
import kalender.xmlKalenderLader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class KalenderFenster extends KalenderAnzeige {
    Frame fenster;
    Choice kalenderChoice;
    List terminListe;
    Label fehlermeldung;
    JFileChooser fileChooser, fileChooser1;

    /**
     * Konstruktor, der den Kalender für die Anzeige festlegt.
     */
    public KalenderFenster() {
        super(null);
        fenster = new Frame("KalenderApp");
        fenster.setSize(1000,700);
        fenster.setLayout(new GridLayout(6, 1));
        fenster.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fenster.dispose();
            }
        });

        Panel topPanel = new Panel(new GridLayout(4, 2, 0, 3));
        Panel topPanel1 = new Panel(new FlowLayout(FlowLayout.CENTER));
        Panel middlePanel = new Panel(new GridLayout(4, 2, 0, 3));
        Panel middlePanel1 = new Panel(new FlowLayout(FlowLayout.CENTER));
        Panel middlePanel2 = new Panel(new GridLayout(1, 2, 0, 3));
        Panel bottomPanel = new Panel(new FlowLayout(FlowLayout.CENTER));

        //Kalender anlegen
        TextField kalenderName = new TextField();
        TextField plaetze = new TextField();
        TextField besitzer = new TextField();
        TextField mitglieder = new TextField();
        topPanel.add(new Label("       Kalendername:"));
        topPanel.add(kalenderName);
        topPanel.add(new Label("       Plaetze (Raumkalender):"));
        topPanel.add(plaetze);
        topPanel.add(new Label("       Besitzer (Personenkalender):"));
        topPanel.add(besitzer);
        topPanel.add(new Label("       Mitglieder (Gruppenkalender):"));
        topPanel.add(mitglieder);

        //Separates Panel für die Buttons
        Button raumKalenderButton = new Button("       RaumKalender erstellen       ");
        Button personenKalenderButton = new Button("       PersonenKalender erstellen       ");
        Button gruppenKalenderButton = new Button("       GruppenKalender erstellen       ");
        topPanel1.add(new Label("                                                                                  "));
        topPanel1.add(new Label("                                                                                  "));
        topPanel1.add(new Label("                                                                                  "));
        topPanel1.add(new Label("                                               "));
        topPanel1.add(raumKalenderButton);
        topPanel1.add(personenKalenderButton);
        topPanel1.add(gruppenKalenderButton);


        //Kalenderauswahl und Anzeige
        kalenderChoice = new Choice();
        middlePanel.add(new Label("       Kalender auswählen:"));
        middlePanel.add(kalenderChoice);

        //Termin erstellen
        TextField terminName = new TextField();
        TextField terminStart = new TextField();
        TextField terminEnde = new TextField();
        middlePanel.add(new Label("       TerminName:"));
        middlePanel.add(terminName);
        middlePanel.add(new Label("       TerminStart (yyyy-MM-dd hh:mm):"));
        middlePanel.add(terminStart);
        middlePanel.add(new Label("       TerminEnde (yyyy-MM-dd hh:mm):"));
        middlePanel.add(terminEnde);

        middlePanel1.add(new Label("                                                                                  "));
        middlePanel1.add(new Label("                                                                                  "));
        middlePanel1.add(new Label("                                                                                  "));
        middlePanel1.add(new Label("                                                   "));
        Button terminErstellenButton = new Button("            Termin hinzufügen              ");
        Button fileWaehlenButton = new Button("                Kalender laden                       ");
        Button kalenderSpeichernButton = new Button("             Kalender speichern                ");
        middlePanel1.add(new Label(""));
        middlePanel1.add(new Label(""));
        middlePanel1.add(new Label(""));
        middlePanel1.add(terminErstellenButton);
        middlePanel1.add(fileWaehlenButton);
        middlePanel1.add(kalenderSpeichernButton);
        middlePanel1.add(new Label(""));
        middlePanel1.add(new Label(""));
        middlePanel1.add(new Label(""));

        //Terminliste
        terminListe = new List();
        middlePanel2.add(new Label("       Terminanzeige:"));
        middlePanel2.add(terminListe);

        //Fehlermeldung
        fehlermeldung = new Label();
        fehlermeldung.setForeground(Color.RED);
        bottomPanel.add(new Label("                                                                                  "));
        bottomPanel.add(new Label("                                                                                  "));
        bottomPanel.add(new Label("                                                                                  "));
        bottomPanel.add(new Label("                                                     "));
        bottomPanel.add(fehlermeldung);

        //Layout Panels hinzufügen
        fenster.add(topPanel);
        fenster.add(topPanel1);
        fenster.add(middlePanel);
        fenster.add(middlePanel1);
        fenster.add(middlePanel2);
        fenster.add(bottomPanel);

        kalenderChoice.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String kalenderItem = kalenderChoice.getSelectedItem();
                Kalender kalender = Kalenderserie.returnKalender(kalenderItem);
                aktualisiereTerminListe(kalender);
            }
        });

        raumKalenderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kalendername = kalenderName.getText();
                String kalenderplaetze = plaetze.getText();
                try {
                    int kalenderp = Integer.parseInt(kalenderplaetze);
                    if(!Kalenderserie.kalenderExistiert(kalendername)) {
                        RaumKalender raumkalender = new RaumKalender(kalendername, kalenderp);
                        Kalenderserie.addKalender(raumkalender);
                        kalenderName.setText("");
                        plaetze.setText("");
                        fehlermeldung.setText("");
                        kalenderChoice.add(kalendername);
                        aktualisiereTerminListe(raumkalender);
                    }else{
                        throw new IllegalArgumentException("Der Kalender ist schon vorhanden.");
                    }
                }catch (NumberFormatException ex) {
                    fehlermeldung.setText("Fehler beim Erstellen des Kalenders: Plätze muss eine gültige Zahl sein.");
                }catch (IllegalArgumentException ex) {
                    fehlermeldung.setText("Fehler beim Erstellen des Kalenders: " + ex.getMessage());
                }
            }
        });

        personenKalenderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kalendername = kalenderName.getText();
                String kalenderbesizer = besitzer.getText();
                try {
                    if(!Kalenderserie.kalenderExistiert(kalendername)) {
                        PersonenKalender personenKalender = new PersonenKalender(kalendername, kalenderbesizer);
                        Kalenderserie.addKalender(personenKalender);
                        kalenderName.setText("");
                        besitzer.setText("");
                        fehlermeldung.setText("");
                        kalenderChoice.add(kalendername);
                        aktualisiereTerminListe(personenKalender);
                    }else{
                        throw new IllegalArgumentException("Der Kalender ist schon vorhanden.");
                    }
                }catch(IllegalArgumentException exeption){
                    fehlermeldung.setText("Fehler beim Erstellen des Kalenders: " + exeption.getMessage());
                }
            }
        });

        gruppenKalenderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kalendername = kalenderName.getText();
                String[] kalendermitglieder = new String[]{mitglieder.getText()};
                try {
                    if(!Kalenderserie.kalenderExistiert(kalendername)) {
                        GruppenKalender gruppenKalender = new GruppenKalender(kalendername, kalendermitglieder);
                        Kalenderserie.addKalender(gruppenKalender);
                        kalenderName.setText("");
                        mitglieder.setText("");
                        fehlermeldung.setText("");
                        kalenderChoice.add(kalendername);
                        aktualisiereTerminListe(gruppenKalender);
                    }else{
                        throw new IllegalArgumentException("Der Kalender ist schon vorhanden.");
                    }
                }catch (Exception ex){
                    fehlermeldung.setText("Fehler beim Erstellen des Kalenders: " + ex.getMessage());
                }
            }
        });

        fileWaehlenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("./src/kalender")); //set directory to project folder

                int response = fileChooser.showOpenDialog(null);

                if(response == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    String path = file.getAbsolutePath();

                    try {
                        xmlKalenderLader lader = new xmlKalenderLader(path);
                        Kalender kalender = lader.loadCalendar();
                        Kalenderserie.addKalender(kalender);
                        kalenderName.setText("");
                        mitglieder.setText("");
                        fehlermeldung.setText("");
                        kalenderChoice.add(kalender.getName());
                        aktualisiereTerminListe(kalender);

                    } catch (PersonNichtVerfuegbarException ex){
                        fehlermeldung.setText("Person ist nicht verfügbar. " + ex.getMessage());
                    }
                    catch (TerminUeberschneidungException ex){
                        fehlermeldung.setText("Termin überschneidet sich. " + ex.getMessage());
                    }
                    catch (Exception ex) {
                        fehlermeldung.setText("Fehler: " + ex.getMessage());
                    }
                }
            }
        });

        kalenderSpeichernButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser1 = new JFileChooser();
                fileChooser1.setCurrentDirectory(new File("./src/kalender"));
                int response = fileChooser1.showSaveDialog(null);

                if(response == JFileChooser.APPROVE_OPTION){
                    File fileToSave = fileChooser1.getSelectedFile();
                    if (!fileToSave.getName().endsWith(".xml")) {
                        fileToSave = new File(fileToSave.getPath() + ".xml");
                    }

                    String kalenderName = kalenderChoice.getSelectedItem();
                    Kalender kalender = Kalenderserie.returnKalender(kalenderName);
                    if (kalender != null) {
                        try {
                            new xmlKalenderLader(kalender, fileToSave.getPath());
                        } catch (Exception ex) {
                            fehlermeldung.setText("Fehler beim Speichern des Kalenders: " + ex.getMessage());
                        }
                    }
                }
            }
        });

        terminErstellenButton.addActionListener(new ActionListener() {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            @Override
            public void actionPerformed(ActionEvent e) {
                String startDatum = terminStart.getText();
                String endDatum = terminEnde.getText();
                String terminNameText = terminName.getText();
                try {
                    LocalDateTime start = LocalDateTime.parse(startDatum, formatter);
                    LocalDateTime end = LocalDateTime.parse(endDatum, formatter);

                    Termin termin = new Termin(terminNameText, start, end);
                    String name = kalenderChoice.getSelectedItem();
                    Kalenderserie.returnKalender(name).addTermin(termin);
                    terminStart.setText("");
                    terminEnde.setText("");
                    terminName.setText("");
                    fehlermeldung.setText("");
                    aktualisiereTerminListe(Kalenderserie.returnKalender(name));
                }catch (DateTimeParseException ex){
                    fehlermeldung.setText("Fehler beim Erstellen des Termins: Start und Ende sind Pflichtfelder.");
                }catch(TerminUeberschneidungException ex) {
                    fehlermeldung.setText("Termin \"" + ex.getTermin().getName() + "\" ueberschneidet sich.");
                }catch(PersonNichtVerfuegbarException ex){
                    fehlermeldung.setText("Mitglied " + ex.getPerson() + " hat mit dem Termin \"" + ex.getTermin().getName() +
                            "\" ueberschneidende Termine.");
                }catch(Exception ex) {
                    fehlermeldung.setText("Fehler beim Erstellen des Termins: " + ex.getMessage());
                }
            }
        });
    }

    public void aktualisiereTerminListe(Kalender kalender) {
        terminListe.removeAll();
        if (kalender != null) {
            terminListe.add("Kalender:\t " + kalender.getName());
            if (kalender instanceof RaumKalender) {
                terminListe.add("Plaetze:\t " + ((RaumKalender) kalender).getPlaetze());
            } else if (kalender instanceof GruppenKalender) {
                terminListe.add("Mitglieder:\t " + String.join(", ", ((GruppenKalender) kalender).getMitglieder()));
            }

            terminListe.add("Termine:");
            for (Termin termin : kalender.getTermine()) {
                terminListe.add(termin.getInfo());
            }
        }
    }

    @Override
    public void ausgeben() {
        fenster.setVisible(true);
    }
}
