import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class WorldSaverUI {
    private JFrame frame;
    private JPanel leftPanel, rightPanel, bottomPanel;
    private JProgressBar progressBar;
    private JPanel boxPanel1, boxPanel2, boxPanel3, boxPanel4;
    private JButton weiterButton;

    private JLabel[] checkMarks;
    private boolean[] levelDone;
    private int antworten = 0;
    
    private List<Frage> fragenList = new ArrayList<>();
    // Timer für die Fortschrittsanzeige
    Timer timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int currentValue = progressBar.getValue();
            if (currentValue < 100) {
                progressBar.setValue(currentValue + 1); // Erhöhe den Fortschritt um 1%
            }
        }
    });

    public WorldSaverUI() {
    	timer.start(); // Starte den Timer
        frame = new JFrame("WorldSaver");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Vollbildmodus
        frame.setLayout(new BorderLayout());

        // Erzeugung des linken Panels mit 7 Quadraten und Haken
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(7, 1));

        checkMarks = new JLabel[7]; // Array für die Haken-Labels
        levelDone = new boolean[7]; // Array zum Speichern des Bestehensstatus der Level

        for (int i = 0; i < 7; i++) {
            JPanel squarePanel = createSquarePanel("Kippelement " + (i + 1));
            checkMarks[i] = new JLabel("\u2713"); // Haken-Symbol (U+2713)
            checkMarks[i].setForeground(Color.GREEN);
            checkMarks[i].setVisible(false); // Standardmäßig unsichtbar
            squarePanel.add(checkMarks[i]);
            leftPanel.add(squarePanel);
        }

        // Erzeugung des rechten Panels mit Boxen und Radiobuttons
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(4, 1));

        boxPanel1 = createBoxPanel("Dies ist eine Frage 1");
        boxPanel2 = createBoxPanel("Dies ist eine Frage 2");
        boxPanel3 = createBoxPanel("Dies ist eine Frage 3");
        boxPanel4 = createBoxPanel("Dies ist eine Frage 4");

        rightPanel.add(boxPanel1);
        rightPanel.add(boxPanel2);
        rightPanel.add(boxPanel3);
        rightPanel.add(boxPanel4);

        // Erzeugung der unteren Leiste mit Fortschrittsanzeige
        bottomPanel = new JPanel();
        progressBar = new JProgressBar(0, 100); // Mindestwert 0 und Maximalwert 100
        progressBar.setStringPainted(false); // Textanzeige entfernen
        progressBar.setPreferredSize(new Dimension(800, 20)); // Größe ändern
        bottomPanel.add(progressBar);
        
        //WeiterButton hinzugefügt
        weiterButton = new JButton("Weiter");
        weiterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	int level=1;
            	if(isLevelCompleted())
            		distributeFragenToBoxen();
            		level++;
                //goToNextQuestion();
            		timer.restart();
            		progressBar.setValue(0);
            }
        });
        bottomPanel.add(weiterButton);

        // Füge die Panels zum Hauptframe hinzu
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.EAST);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
        
        readFragenFromFile();
        distributeFragenToBoxen();
    }

    private JPanel createSquarePanel(String label) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setPreferredSize(new Dimension(100, 100)); // Größe des Quadrats
        JLabel squareLabel = new JLabel(label);
        squareLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(squareLabel);
        return panel;
    }

    private JPanel createBoxPanel(String boxText) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(boxText));

        JRadioButton radioButtonA = new JRadioButton("Antwort A");
        JRadioButton radioButtonB = new JRadioButton("Antwort B");
        JRadioButton radioButtonC = new JRadioButton("Antwort C");

        ButtonGroup group = new ButtonGroup();
        group.add(radioButtonA);
        group.add(radioButtonB);
        group.add(radioButtonC);

        panel.add(radioButtonA);
        panel.add(radioButtonB);
        panel.add(radioButtonC);

        // Füge einen ActionListener zu den Radiobuttons hinzu, um das Level zu überprüfen
        ActionListener radioListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int level = 0; // Hier das Level setzen, das zu diesem Panel gehört (1-4)
                JRadioButton selectedButton = (JRadioButton) e.getSource();

                if (selectedButton.getText().equals("Antwort A") ||
                    selectedButton.getText().equals("Antwort B") ||
                    selectedButton.getText().equals("Antwort C")) {
                    antworten = antworten + 1;
                    if (antworten == 4) {
                    	levelDone[level] = isLevelCompleted(); // Überprüfe, ob das Level bestanden wurde
                        updateStatusLabel(level); // Aktualisiere den Status-Label
                    }
                }
            }
        };
        radioButtonA.addActionListener(radioListener);
        radioButtonB.addActionListener(radioListener);
        radioButtonC.addActionListener(radioListener);
        return panel;
    }

    private boolean isLevelCompleted() {
    	//if() {	
    	//}
        // Hier die Logik zur Überprüfung, ob das Level bestanden wurde, einfügen
        // Zum Beispiel, wenn alle Radiobuttons ausgewählt wurden, return true;
        // Andernfalls, return false;
        return true; // Vorübergehend true für Testzwecke
    }
    
    private void updateStatusLabel(int level) {
    	if (levelDone[level]) {
            JOptionPane.showMessageDialog(frame, "Level " + (level + 1) + " bestanden!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Level " + (level + 1) + " nicht bestanden.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    	restartWorldSaverUI();
    }
    
    private void readFragenFromFile() {
        try {
            // Öffne die Textdatei als InputStream
            InputStream inputStream = ClassLoader.getSystemResourceAsStream("fragen.txt");

            if (inputStream != null) {
                // Verwende einen BufferedReader für effizientes Lesen
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                // Lese Zeilen aus der Datei, bis das Ende erreicht ist (null wird zurückgegeben)
                while ((line = bufferedReader.readLine()) != null) {
                		String line1 = splitLine(line)[0];
                		String line2 = splitLine(line)[1];
                		String line3 = splitLine(line)[2];
                		String line4 = splitLine(line)[3];
                		Frage frage = new Frage(line1, line2, line3, line4);
                        fragenList.add(frage); // Füge die Zeile zur Liste hinzu, wenn das erste Zeichen keine Ziffer ist
                }

                // Schließe den BufferedReader und den InputStream
                bufferedReader.close();
                inputStream.close();
            } else {
                System.err.println("Die Datei wurde nicht gefunden: " + "fragen.txt");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String[] splitLine(String line) {
    	String[] lines = new String[4];
    	lines = line.split("\\?");
     	return lines;
    }
 
    private void distributeFragenToBoxen() {
        // Mischen Sie die Fragen in zufälliger Reihenfolge
        Collections.shuffle(fragenList);

        // Verteilen Sie die Fragen auf die Boxen
        int i = 0;
        for (Frage frage : fragenList) {
            if (i == 0) {
                boxPanel1.setBorder(BorderFactory.createTitledBorder(frage.frage));
                setRadioButtonTexts(boxPanel1, frage); // Setzen Sie die RadioButtonTexts
            } else if (i == 1) {
                boxPanel2.setBorder(BorderFactory.createTitledBorder(frage.frage));
                setRadioButtonTexts(boxPanel2, frage); // Setzen Sie die RadioButtonTexts
            } else if (i == 2) {
                boxPanel3.setBorder(BorderFactory.createTitledBorder(frage.frage));
                setRadioButtonTexts(boxPanel3, frage); // Setzen Sie die RadioButtonTexts
            } else if (i == 3) {
                boxPanel4.setBorder(BorderFactory.createTitledBorder(frage.frage));
                setRadioButtonTexts(boxPanel4, frage); // Setzen Sie die RadioButtonTexts
            }
            i++;
            if (i == 4) {
                i = 0;
            }
        }
    }

    private void setRadioButtonTexts(JPanel boxPanel, Frage frage) {
        Component[] components = boxPanel.getComponents();
        String[] antworten = {frage.a1, frage.a2, frage.a3}; // Nehmen Sie die Antworten aus Ihrem Frage-Objekt

        // Mischen Sie das Array antworten in zufälliger Reihenfolge
        List<String> shuffledAntworten = Arrays.asList(antworten);
        Collections.shuffle(shuffledAntworten);
        shuffledAntworten.toArray(antworten);

        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JRadioButton && i < antworten.length) {
                JRadioButton radioButton = (JRadioButton) components[i];
                radioButton.setText(antworten[i]); // Setzen Sie den Text des aktuellen Radio-Buttons auf die entsprechende Antwort
            }
        }
    }
    
    private void restartWorldSaverUI() {
        frame.dispose(); // Schließen Sie das aktuelle JFrame
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WorldSaverUI(); // Starten Sie eine neue Instanz von WorldSaverUI
            }
        });
    }
    
    private static void reader() {
    	try {
            // Öffne die Textdatei als InputStream
            InputStream inputStream = ClassLoader.getSystemResourceAsStream("fragen.txt");

            if (inputStream != null) {
                // Verwende einen BufferedReader für effizientes Lesen
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                // Lese Zeilen aus der Datei, bis das Ende erreicht ist (null wird zurückgegeben)
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }

                // Schließe den BufferedReader und den InputStream
                bufferedReader.close();
                inputStream.close();
            } else {
                System.err.println("Die Datei wurde nicht gefunden: " + "fragen.txt");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}