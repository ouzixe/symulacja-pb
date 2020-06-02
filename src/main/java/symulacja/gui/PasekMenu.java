package symulacja.gui;

import symulacja.PlikKonfiguracyjny;
import symulacja.Symulacja;
import symulacja.silnik.mapa.Mapa;

import javax.swing.*;
import java.awt.*;


public class PasekMenu {
    static JMenuBar utworzPasekMenu() {
        final JMenuBar planszaPasekMenu = new JMenuBar();
        planszaPasekMenu.add(utworzMenuPlik());
        planszaPasekMenu.add(utworzMenuKonfig());
        return planszaPasekMenu;

    }

    private static JMenu utworzMenuPlik() {
        final JMenu menuPlik = new JMenu("Plik");

        final JMenuItem nowaMenu = new JMenuItem("Nowa symulacja");
        nowaMenu.addActionListener(e -> Symulacja.nowaSymulacja());
        menuPlik.add(nowaMenu);

        final JMenuItem wyjscieMenu = new JMenuItem("Wyjscie");
        wyjscieMenu.addActionListener(e -> System.exit(0));
        menuPlik.add(wyjscieMenu);

        return menuPlik;
    }

    private static JMenu utworzMenuKonfig() {
        final JMenu menuKonfig = new JMenu("Konfiguracja");

        final JMenuItem odczytMenu = new JMenuItem("Zmien konfiguracje");
        odczytMenu.addActionListener(e -> oknoKonfiguracja());
        menuKonfig.add(odczytMenu);
        return menuKonfig;
    }

    private static void oknoKonfiguracja() {

        //Okno pozwalające zmieniać konfigurację symulacji

        JFrame konfig = new JFrame("");
        konfig.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel szerokosc = new JLabel("Szerokosc (5-25): ", SwingConstants.RIGHT);
        c.anchor = GridBagConstraints.EAST;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        konfig.add(szerokosc, c);

        JLabel wysokosc = new JLabel("Wysokosc (5-25): ", SwingConstants.RIGHT);
        c.gridy = 1;
        konfig.add(wysokosc, c);

        JLabel zageszczenie = new JLabel("Zageszczenie (1-5): ", SwingConstants.RIGHT);
        c.gridy = 2;
        konfig.add(zageszczenie, c);

        JLabel liczbaOddzialow = new JLabel("Liczba oddzialow (2-15): ", SwingConstants.RIGHT);
        c.gridy = 3;
        konfig.add(liczbaOddzialow, c);

        JTextField szerokoscPole = new JTextField("" + PlikKonfiguracyjny.odczytajWartosc("szerokosc"), 2);
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 20, 0, 0);
        c.gridx = 1;
        c.gridy = 0;
        konfig.add(szerokoscPole, c);

        JTextField wysokoscPole = new JTextField("" + PlikKonfiguracyjny.odczytajWartosc("wysokosc"), 2);
        c.gridy = 1;
        konfig.add(wysokoscPole, c);

        JTextField zageszczeniePole = new JTextField("" +PlikKonfiguracyjny.odczytajWartosc("zageszczenie"), 2);
        c.gridy = 2;
        konfig.add(zageszczeniePole, c);

        JTextField liczbaOddzialowPole = new JTextField("" + PlikKonfiguracyjny.odczytajWartosc("liczba-oddzialow"), 2);
        c.gridy = 3;
        konfig.add(liczbaOddzialowPole, c);

        JButton aktualizuj = new JButton("Aktualizuj konfiguracje");
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        konfig.add(aktualizuj, c);

        JButton domyslne = new JButton("Przywroc domyslne");
        c.gridy = 5;
        konfig.add(domyslne, c);

        konfig.setSize(250, 250);
        konfig.setVisible(true);
        konfig.setLocationRelativeTo(null);
        konfig.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        domyslne.addActionListener(e -> {
            szerokoscPole.setText("15");
            wysokoscPole.setText("15");
            zageszczeniePole.setText("5");
            liczbaOddzialowPole.setText("5");
            PlikKonfiguracyjny.zmienWartosc("szerokosc", szerokoscPole.getText());
            PlikKonfiguracyjny.zmienWartosc("wysokosc", wysokoscPole.getText());
            PlikKonfiguracyjny.zmienWartosc("zageszczenie", zageszczeniePole.getText());
            PlikKonfiguracyjny.zmienWartosc("liczba-oddzialow", liczbaOddzialowPole.getText());
            JOptionPane.showMessageDialog(null, "Przywrocono wartosci domyslne", "", JOptionPane.INFORMATION_MESSAGE);
        });

        aktualizuj.addActionListener(e -> {
            PlikKonfiguracyjny.zmienWartosc("szerokosc", szerokoscPole.getText());
            PlikKonfiguracyjny.zmienWartosc("wysokosc", wysokoscPole.getText());
            PlikKonfiguracyjny.zmienWartosc("zageszczenie", zageszczeniePole.getText());
            PlikKonfiguracyjny.zmienWartosc("liczba-oddzialow", liczbaOddzialowPole.getText());
            szerokoscPole.setText("" + PlikKonfiguracyjny.odczytajWartosc("szerokosc"));
            wysokoscPole.setText("" + PlikKonfiguracyjny.odczytajWartosc("wysokosc"));
            zageszczeniePole.setText("" + PlikKonfiguracyjny.odczytajWartosc("zageszczenie"));
            liczbaOddzialowPole.setText("" + PlikKonfiguracyjny.odczytajWartosc("liczba-oddzialow"));
            JOptionPane.showMessageDialog(null, "Zmienionio konfiguracje", "", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public static void oknoBledu(String wartosc) {
        JOptionPane.showMessageDialog(null, "Wartosc " + wartosc + " poza zakresem.\nWartosc nie zostala zmieniona.", "Blad", JOptionPane.ERROR_MESSAGE);
    }
}