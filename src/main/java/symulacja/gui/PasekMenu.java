package symulacja.gui;

import symulacja.PlikKonfiguracyjny;
import symulacja.Symulacja;
import symulacja.silnik.mapa.Mapa;

import javax.swing.*;


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
        nowaMenu.addActionListener(e -> {
            //TODO Możliwość uruchomienia nowej symulacji w trakcie/ po zakończeniu poprzedniej
        });
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

        JButton aktualizuj = new JButton("Aktualizuj konfiguracje");
        aktualizuj.setBounds(10, 200, 170, 40);
        JButton domyslne = new JButton("Przywroc domyslne");
        domyslne.setBounds(10, 170, 170, 20);

        JLabel szerokosc = new JLabel();
        szerokosc.setText("Szerokosc (5-25): ");
        szerokosc.setBounds(10, 10, 150, 30);
        JLabel wysokosc = new JLabel();
        wysokosc.setText("Wysokosc (5-25): ");
        wysokosc.setBounds(10, 50, 150, 30);
        JLabel zageszczenie = new JLabel();
        zageszczenie.setText("Zageszczenie (1-5): ");
        zageszczenie.setBounds(10, 90, 150, 30);
        JLabel liczbaOddzialow = new JLabel();
        liczbaOddzialow.setText("Liczba oddzialow (2-15): ");
        liczbaOddzialow.setBounds(10, 130, 150, 30);

        JTextField szerokoscPole = new JTextField("" + PlikKonfiguracyjny.odczytajWartosc("szerokosc"));
        szerokoscPole.setBounds(140, 10, 40, 30);
        JTextField wysokoscPole = new JTextField("" + PlikKonfiguracyjny.odczytajWartosc("wysokosc"));
        wysokoscPole.setBounds(140, 50, 40, 30);
        JTextField zageszczeniePole = new JTextField("" +PlikKonfiguracyjny.odczytajWartosc("zageszczenie"));
        zageszczeniePole.setBounds(140, 90, 40, 30);
        JTextField liczbaOddzialowPole = new JTextField("" + PlikKonfiguracyjny.odczytajWartosc("liczba-oddzialow"));
        liczbaOddzialowPole.setBounds(140, 130, 40, 30);

        konfig.add(aktualizuj);
        konfig.add(domyslne);
        konfig.add(szerokosc);
        konfig.add(wysokosc);
        konfig.add(liczbaOddzialow);
        konfig.add(zageszczenie);
        konfig.add(szerokoscPole);
        konfig.add(wysokoscPole);
        konfig.add(zageszczeniePole);
        konfig.add(liczbaOddzialowPole);
        konfig.setSize(205, 290);
        konfig.setLayout(null);
        konfig.setVisible(true);
        konfig.setResizable(false);
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