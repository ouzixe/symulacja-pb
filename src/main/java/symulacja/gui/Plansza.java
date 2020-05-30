package symulacja.gui;

import symulacja.Symulacja;
import symulacja.silnik.tura.Dowodca;
import symulacja.silnik.mapa.Mapa;
import symulacja.silnik.mapa.Pole;
import symulacja.silnik.obiekty.Obiekt;
import symulacja.silnik.oddzialy.Oddzial;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Plansza {

    private final JFrame plansza;
    private final PasekBoczny pasekBoczny;
    protected static MapaPanel mapaPanel;
    public static Mapa mapa;

    private static final Color jasnyKolorPola = Color.decode("#228B22");
    private static final Color ciemnyKolorPola = Color.decode("#006400");

    private final static Dimension ROZMIAR_POLA = new Dimension(10, 10);


    private static String sciezkaIkon = "ikony/";


    public Plansza(final int szerokosc, final int wysokosc,
                   final List<Pole.Wspolrzedne> listaWspolrzednych,
                   final List<Obiekt> listaObiektow,
                   final List<Oddzial> listaOddzialow,
                   final List<Dowodca> listaDowodcow,
                   int powtorzenie) {

        //Graficzna reprezentacja Symulacji

        this.plansza = new JFrame("Symulacja");
        this.plansza.setLayout(new BorderLayout());
        final JMenuBar planszaPasekMenu = PasekMenu.utworzPasekMenu();
        this.plansza.setJMenuBar(planszaPasekMenu);
        this.plansza.setSize(new Dimension(1024,  768));
        this.plansza.setResizable(false);
        this.pasekBoczny = new PasekBoczny();
        mapa = Mapa.utworzPodstawowaMape(listaWspolrzednych, listaObiektow, listaOddzialow, listaDowodcow, powtorzenie);
        mapaPanel = new MapaPanel(szerokosc, wysokosc, listaWspolrzednych);
        this.plansza.add(mapaPanel,  BorderLayout.CENTER);
        this.plansza.add(this.pasekBoczny, BorderLayout.EAST);
        this.plansza.setLocationRelativeTo(null);
        this.plansza.setVisible(true);
        this.plansza.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public static void koniecSymulacji() {
        Symulacja.wylaczSymulacje();
        JOptionPane.showMessageDialog(null, "Symulacja zakonczona", "", JOptionPane.INFORMATION_MESSAGE);
    }


    protected static class MapaPanel extends JPanel {

        //Wyrysowanie siatki pól

        final List<PolePanel> polaNaMapie;

        MapaPanel(final int szerokosc, final int wysokosc, final List<Pole.Wspolrzedne> listaWspolrzednych) {
            super(new GridLayout(szerokosc, wysokosc));
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
            this.setPreferredSize(new Dimension(40, 40));
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
                    //add(new JLabel(new ImageIcon(obrazek)));
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

        private void przypiszKolorPolu(final int i) {
            if (i % 2 == 0) setBackground(jasnyKolorPola);
            else if (i % 2 == 1) setBackground(ciemnyKolorPola);
        }
    }

}
