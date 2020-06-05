package symulacja.gui;

import symulacja.PlikRaportu;
import symulacja.Symulacja;
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

//Główne okno GUI. Wyświetla aktualny stan symulacji.

public class Plansza {

    private final JFrame plansza;
    protected static MapaPanel mapaPanel;
    public static Mapa mapa;

    private static final Color jasnyKolorPola = Color.decode("#228B22");
    private static final Color ciemnyKolorPola = Color.decode("#006400");


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
        mapa = Mapa.utworzPodstawowaMape(listaDowodcow, powtorzenie);
        mapaPanel = new MapaPanel(szerokosc, wysokosc, listaWspolrzednych);
        mapaPanel.setSize(Symulacja.odczytajSzerokosc()*40, Symulacja.odczytajWysokosc()*40);
        this.plansza.add(mapaPanel);
        this.plansza.add(pasekBoczny);
        this.plansza.setLocationRelativeTo(null);
        this.plansza.setVisible(true);
        this.plansza.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public static void koniecSymulacji() {
        Symulacja.wylaczSymulacje();
        PlikRaportu.zakonczPlik();
        JOptionPane.showMessageDialog(null, "Symulacja zakonczona", "", JOptionPane.INFORMATION_MESSAGE);
    }

    public void dispose() {
        plansza.dispose();
    }

    private Dimension obliczWymiary() {
        return new Dimension(Symulacja.odczytajSzerokosc() * 50 + 280, Symulacja.odczytajWysokosc() * 50 + 70);
    }

    protected static class MapaPanel extends JPanel {

        //Wyrysowanie siatki pól

        final List<PolePanel> polaNaMapie;

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

    private static class PolePanel extends JPanel {

        //Wyrysowanie jednego pola

        private final Pole.Wspolrzedne wspolrzedne;

        PolePanel(final Pole.Wspolrzedne wspolrzedne, final int i) {
            this.wspolrzedne = wspolrzedne;
            this.setPreferredSize(new Dimension(50, 50));
            przypiszKolorPolu(i);
            przypiszIkony(mapa);
            validate();
        }

        public void rysujPole(final Mapa mapa, final int i) {
            przypiszKolorPolu(i);
            przypiszIkony(mapa);
            validate();
            repaint();
        }

        private void przypiszIkony(final Mapa mapa) {
            this.removeAll();
            BufferedImage obiekt = null;
            BufferedImage oddzial = null;
            String sciezkaIkon = "zasoby/ikony/";
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

        static boolean parzystyRzad = true;

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
