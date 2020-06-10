package symulacja.gui;

import symulacja.PlikRaportu;
import symulacja.Symulacja;
import symulacja.silnik.obiekty.Obiekt;
import symulacja.silnik.oddzialy.Oddzial;
import symulacja.silnik.tura.Dowodca;
import symulacja.silnik.mapa.Mapa;
import symulacja.silnik.mapa.Pole;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Główne okno interfejsu graficznego. Wyświetla aktualny stan symulacji.
 */
public class Plansza {

    /** Główne okno interfejsu graficznego. */
    private final JFrame plansza;
    /** Panel wyświetlający aktualny stan symulacji. */
    protected static MapaPanel mapaPanel;
    /** Mapa przekazywana do {@link Plansza#mapaPanel}. */
    public static Mapa mapa;

    /** Jasny kolor pola. */
    private static final Color jasnyKolorPola = Color.decode("#228B22");
    /** Ciemny kolor pola. */
    private static final Color ciemnyKolorPola = Color.decode("#006400");

    /**
     * Metoda główna {@link Plansza}.
     * @param szerokosc
     * Używana do określania rozmiarów {@link Plansza#plansza}.
     * @param wysokosc
     * Używana do określania rozmiarów {@link Plansza#plansza}.
     * @param listaWspolrzednych
     * Używana do tworzenia {@link Plansza#mapaPanel}.
     * @param listaDowodcow
     * Używana do tworzenia {@link Mapa}.
     * @param powtorzenie
     * Używana do tworzenia {@link Mapa}.
     */
    public Plansza(final int szerokosc, final int wysokosc,
                   final List<Pole.Wspolrzedne> listaWspolrzednych,
                   final List<Dowodca> listaDowodcow,
                   int powtorzenie) {

        //Graficzna reprezentacja Symulacji

        this.plansza = new JFrame("Symulacja");
        this.plansza.setLayout(new FlowLayout(FlowLayout.LEADING));
        plansza.setSize(obliczWymiary());
        final JMenuBar planszaPasekMenu = PasekMenu.utworzPasekMenu();
        this.plansza.setJMenuBar(planszaPasekMenu);
        this.plansza.setResizable(false);
        PasekBoczny pasekBoczny = new PasekBoczny();
        mapa = new Mapa(listaDowodcow, powtorzenie);
        mapaPanel = new MapaPanel(szerokosc, wysokosc, listaWspolrzednych);
        mapaPanel.setSize(Symulacja.odczytajSzerokosc()*40, Symulacja.odczytajWysokosc()*40);
        this.plansza.add(mapaPanel);
        this.plansza.add(pasekBoczny);
        this.plansza.setLocationRelativeTo(null);
        this.plansza.setVisible(true);
        this.plansza.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    /**
     * Zakończenie symulacji wyłączając funkcjonalność przycisków kontrolnych,
     * zapisując plik raportu i wyświetlając stosowny komunikat.
     */
    public static void koniecSymulacji() {
        Symulacja.wylaczSymulacje();
        PlikRaportu.zakonczPlik();
        JOptionPane.showMessageDialog(null, "Symulacja zakonczona", "", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Pozbycie się okna głównego {@link Plansza#plansza}.
     */
    public void dispose() {
        plansza.dispose();
    }

    /**
     * Obliczenie wymiarów okna głównego {@link Plansza#plansza}.
     * @return Wymiary dla {@link Plansza#plansza}.
     */
    private Dimension obliczWymiary() {
        int y = Symulacja.odczytajWysokosc() * 50 + 70;
        if (y < 700) y = 700;
        return new Dimension(Symulacja.odczytajSzerokosc() * 50 + 280, y);
    }

    /**
     * Klasa do wyświetlania aktualnego stanu symulacji.
     */
    protected static class MapaPanel extends JPanel {

        /** Lista wyświetlanych {@link PolePanel}. */
        final List<PolePanel> polaNaMapie;

        /**
         * Metoda główna {@link MapaPanel}.
         * @param szerokosc
         * Określenie szerokości używanego {@link GridLayout}.
         * @param wysokosc
         * Określenie wysokości używanego {@link GridLayout}.
         * @param listaWspolrzednych
         * Na podstawie współrzędnych określenie pozycji {@link PolePanel}.
         */
        MapaPanel(final int szerokosc, final int wysokosc, final List<Pole.Wspolrzedne> listaWspolrzednych) {
            super(new GridLayout(wysokosc, szerokosc));
            this.polaNaMapie = new ArrayList<>();
            for (int i = 0; i < szerokosc * wysokosc; i++) {
                final PolePanel polePanel = new PolePanel(listaWspolrzednych.get(i), i);
                this.polaNaMapie.add(polePanel);
                add(polePanel);
            }
            validate();
        }

        /**
         * Wyrysowanie {@link Mapa}.
         * @param mapa
         * Rysowana {@link Mapa}.
         */
        public void rysujMape(final Mapa mapa) {
            int i = 0;
            removeAll();
            for(final PolePanel polePanel : polaNaMapie) {
                polePanel.rysujPole(mapa, i);
                add(polePanel);
                i++;
            }
            validate();
            repaint();
        }
    }

    /**
     * Klasa do wyświetlenia jednego pola.
     */
    private static class PolePanel extends JPanel {

        /**
         * {@link Pole.Wspolrzedne} na podstawie których jest wybierane {@link Pole} do wyrysowania.
         */
        private final Pole.Wspolrzedne wspolrzedne;

        /**
         * Metoda główna do stworzenia {@link PolePanel}.
         * @param wspolrzedne
         * Używane do znajdywania pola w {@link Mapa#listaPol}.
         * @param i
         * Używane do przypistywania koloru.
         */
        PolePanel(final Pole.Wspolrzedne wspolrzedne, final int i) {
            this.wspolrzedne = wspolrzedne;
            this.setPreferredSize(new Dimension(50, 50));
            przypiszKolorPolu(i);
            przypiszIkony(mapa);
            validate();
        }

        /**
         * Metoda do aktualizacji {@link PolePanel}.
         * @param mapa
         * Odczytanie {@link Pole} z {@link Mapa#listaPol}.
         * @param i
         * Używane do przypisywania koloru.
         */
        public void rysujPole(final Mapa mapa, final int i) {
            przypiszKolorPolu(i);
            przypiszIkony(mapa);
            validate();
            repaint();
        }

        /**
         * Przypisanie ikon {@link Obiekt} i {@link Oddzial} do {@link PolePanel}.
         * @param mapa
         * Odczytanie {@link Pole} z {@link Mapa#listaPol}.
         */
        private void przypiszIkony(final Mapa mapa) {
            this.removeAll();
            BufferedImage obiekt = null;
            BufferedImage oddzial = null;
            String sciezkaIkon = "src/main/java/symulacja/gui/ikony/";
            if(mapa.odczytajPole(this.wspolrzedne).odczytajObiekt() != null) {
                try {
                    obiekt = ImageIO.read(new File(sciezkaIkon +
                                    mapa.odczytajPole(this.wspolrzedne).odczytajObiekt().toString() + ".png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mapa.odczytajPole(this.wspolrzedne).odczytajOddzial() != null) {
                try {
                    oddzial = ImageIO.read(new File(sciezkaIkon +
                                    mapa.odczytajPole(this.wspolrzedne).odczytajOddzial().toString() + ".png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(obiekt != null) {
                final BufferedImage obrazek = new BufferedImage(
                        obiekt.getWidth(), obiekt.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = obrazek.createGraphics();
                g.drawImage(obiekt, 0, 0, null);
                if(oddzial != null) {
                    g.drawImage(oddzial, 0, 0, null);
                }
                g.dispose();
                add(new JLabel(new ImageIcon(obrazek)));
            }
            if(oddzial != null) {
                final BufferedImage obrazek = new BufferedImage(
                        oddzial.getWidth(), oddzial.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = obrazek.createGraphics();
                g.drawImage(oddzial, 0, 0, null);
                g.dispose();
                add(new JLabel(new ImageIcon(obrazek)));
            }
        }

        /**
         * Używana przy przypisaniu koloru.
         */
        static boolean parzystyRzad = true;

        /**
         * Przypisanie koloru {@link PolePanel} tak, aby nigdy obok siebie nie znalazł się ten sam kolor.
         * @param i
         * Przekazuje numer aktualnie kolorowanego {@link PolePanel}.
         */
        private void przypiszKolorPolu(final int i) {
            if(i % Symulacja.odczytajSzerokosc() == 0) {
                parzystyRzad = !parzystyRzad;
            }
            if(Symulacja.odczytajSzerokosc() % 2 == 0) {
                if (parzystyRzad) {
                    if (i % 2 == 0) setBackground(jasnyKolorPola);
                    else setBackground(ciemnyKolorPola);
                } else {
                    if (i % 2 == 1) setBackground(jasnyKolorPola);
                    else setBackground(ciemnyKolorPola);
                }
            }
            else {
                if (i % 2 == 0) setBackground(jasnyKolorPola);
                else setBackground(ciemnyKolorPola);
            }
        }
    }
}