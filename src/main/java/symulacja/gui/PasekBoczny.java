package symulacja.gui;

import symulacja.PlikRaportu;
import symulacja.Symulacja;
import symulacja.silnik.mapa.Mapa;
import symulacja.silnik.mapa.Pole;
import symulacja.silnik.obiekty.Obiekt;
import symulacja.silnik.oddzialy.Oddzial;
import symulacja.silnik.tura.WykonanieRuchu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static symulacja.gui.Plansza.mapa;
import static symulacja.gui.Plansza.mapaPanel;

public class PasekBoczny extends JPanel {

    private final LogiModel modelLog;
    private final StatyModel modelStats;
    private final JScrollPane scrollPaneLogi;
    private final JScrollPane scrollPaneStaty;
    private static final Dimension ROZMIARY_TABELI_LOGOW = new Dimension(250, 400);
    private static final Dimension ROZMIARY_TABELI_STATYSTYK = new Dimension(250, 100);
    private static final Dimension ROZMIARY_PRZYCISKU = new Dimension(200, 50);

    PasekBoczny() {
        this.setLayout(new BorderLayout());
        this.modelLog = new LogiModel();
        this.modelStats = new StatyModel();
        final JTable tabelaLogow = new JTable(modelLog);
        final JTable tabelaStats = new JTable(modelStats);
        final JButton nastepnaTura = new JButton("Nastepna tura");
        nastepnaTura.setPreferredSize(ROZMIARY_PRZYCISKU);
        tabelaLogow.setRowHeight(15);
        tabelaStats.setRowHeight(15);
        this.scrollPaneLogi = new JScrollPane(tabelaLogow);
        this.scrollPaneStaty = new JScrollPane(tabelaStats);
        scrollPaneLogi.setColumnHeaderView(tabelaLogow.getTableHeader());
        scrollPaneLogi.setPreferredSize(ROZMIARY_TABELI_LOGOW);
        scrollPaneStaty.setColumnHeaderView(tabelaStats.getTableHeader());
        scrollPaneStaty.setPreferredSize(ROZMIARY_TABELI_STATYSTYK);
        modelStats.wypelnijTabele();
        this.add(scrollPaneLogi, BorderLayout.NORTH);
        this.add(scrollPaneStaty, BorderLayout.CENTER);
        this.add(nastepnaTura, BorderLayout.SOUTH);
        this.setVisible(true);

        nastepnaTura.addActionListener(e -> {
            if(Symulacja.czySymulacjaDziala()) {
                WykonanieRuchu.wykonanieRuchu();
                modelStats.wypelnijTabele();
                modelLog.wypelnijTabele();
                mapaPanel.rysujMape(mapa);
            }
        });
    }

    void odswiez(final Mapa mapa, final PlikRaportu plikRaportu) {
        int aktualnyRzad = 0;
        this.modelLog.wyczysc();
        for(final Oddzial.Ruch ruch : PlikRaportu.ruchy) {
            final String tekstOddzialu = ruch.odczytajPoruszonyOddzial().toString();
            final String tekstRuchu = ruch.toString();
            this.modelLog.setValueAt(tekstOddzialu, aktualnyRzad, 0);
            this.modelLog.setValueAt(tekstRuchu, aktualnyRzad, 1);
            aktualnyRzad++;
        }

        if(PlikRaportu.ruchy.size() > 0) {
            final Oddzial.Ruch ostatniRuch = PlikRaportu.ruchy.get(plikRaportu.rozmiar() - 1);
            final String tekstOddzialu = ostatniRuch.odczytajPoruszonyOddzial().toString();
            final String tekstRuchu = ostatniRuch.toString();
            this.modelLog.setValueAt(tekstOddzialu, aktualnyRzad - 1, 0);
            this.modelLog.setValueAt(tekstRuchu, aktualnyRzad - 1, 1);
        }

        final JScrollBar pionowy = scrollPaneLogi.getVerticalScrollBar();
        pionowy.setValue(pionowy.getMaximum());
    }


    private static class LogiModel extends DefaultTableModel {

        private final List<Rzad> wartosci;
        private static final String[] NAZWY = {"Oddzial", "Ruch"};

        LogiModel() {
            this.wartosci = new ArrayList<>();
        }

        public void wyczysc() {
            this.wartosci.clear();
            setRowCount(0);
        }

        @Override
        public int getRowCount() {
            if(this.wartosci == null) return 0;
            return this.wartosci.size();
        }

        @Override
        public int getColumnCount() {
            return NAZWY.length;
        }

        @Override
        public Object getValueAt(final int rzad, final int kolumna) {
            final Rzad aktualnyRzad = this.wartosci.get(rzad);
            if(kolumna == 0) {
                return aktualnyRzad.odczytajOddzial();
            } else if(kolumna == 1) {
                return aktualnyRzad.odczytajRuch();
            }
            return null;
        }

        @Override
        public void setValueAt(final Object wartosc, final int rzad, final int kolumna) {
            final Rzad aktualnyRzad;
            if(this.wartosci.size() <= rzad) {
                aktualnyRzad = new Rzad();
                this.wartosci.add(aktualnyRzad);
            } else {
                aktualnyRzad = this.wartosci.get(rzad);
            }
            if(kolumna == 0) {
                aktualnyRzad.ustawOddzial((String)wartosc);
            } else if(kolumna == 1) {
                aktualnyRzad.ustawRuch((String)wartosc);
                fireTableCellUpdated(rzad, kolumna);
            }
        }

        @Override
        public Class<?> getColumnClass(final int kolumna) {
            return Oddzial.Ruch.class;
        }

        @Override
        public String getColumnName(final int kolumna) {
            return NAZWY[kolumna];
        }

        private void wypelnijTabele() {
            for(int i = 0; i <  PlikRaportu.ruchy.size(); i++) {
                setValueAt(String.valueOf(PlikRaportu.ruchy.get(i).odczytajPoruszonyOddzial().numer), i, 0);
                setValueAt((PlikRaportu.ruchy.get(i).toString()), i, 1);
            }
        }
    }

    private static class StatyModel extends DefaultTableModel {

        private final List<Rzad> wartosci;
        private static final String[] NAZWY = {"Oddzial", "Zdrowie", "Atak", "Obrona", "Sila"};

        StatyModel() { this.wartosci = new ArrayList<>(); }

        public void wyczysc() {
            this.wartosci.clear();
            setRowCount(0);
        }

        @Override
        public int getRowCount() {
            if(this.wartosci == null) return 0;
            return this.wartosci.size();
        }

        @Override
        public int getColumnCount() {
            return NAZWY.length;
        }

        @Override
        public Object getValueAt(final int rzad, final int kolumna) {
            final Rzad aktualnyRzad = this.wartosci.get(rzad);
            if(kolumna == 0) {
                return aktualnyRzad.odczytajOddzial();
            }
            if(kolumna == 1) {
                return aktualnyRzad.odczytajZdrowie();
            }
            if(kolumna == 2) {
                return aktualnyRzad.odczytajAtak();
            }
            if(kolumna == 3) {
                return aktualnyRzad.odczytajObrone();
            }
            if(kolumna == 4) {
                return aktualnyRzad.odczytajSile();
            }
            return null;
        }

        @Override
        public void setValueAt(final Object wartosc, final int rzad, final int kolumna) {
            final Rzad aktualnyRzad;
            if(this.wartosci.size() <= rzad) {
                aktualnyRzad = new Rzad();
                this.wartosci.add(aktualnyRzad);
            } else {
                aktualnyRzad = this.wartosci.get(rzad);
            }
            if(kolumna == 0) {
                aktualnyRzad.ustawOddzial((String)wartosc);
            }
            if(kolumna == 1) {
                aktualnyRzad.ustawZdrowie((String)wartosc);
                fireTableCellUpdated(rzad, kolumna);
            }
            if(kolumna == 2) {
                aktualnyRzad.ustawAtak((String)wartosc);
                fireTableCellUpdated(rzad, kolumna);
            }
            if(kolumna == 3) {
                aktualnyRzad.ustawObrone((String)wartosc);
                fireTableCellUpdated(rzad, kolumna);
            }
            if(kolumna == 4) {
                aktualnyRzad.ustawSile((String)wartosc);
                fireTableCellUpdated(rzad, kolumna);
            }
        }

        @Override
        public Class<?> getColumnClass(final int kolumna) {
            return Oddzial.Ruch.class;
        }

        @Override
        public String getColumnName(final int kolumna) {
            return NAZWY[kolumna];
        }

        private void wypelnijTabele() {
            for(int i = 0; i < Symulacja.listaOddzialow.size(); i++) {
                setValueAt(String.valueOf(i+1), i, 0);
                setValueAt(String.valueOf(Symulacja.listaOddzialow.get(i).zycie), i, 1);
                setValueAt(String.valueOf(Symulacja.listaOddzialow.get(i).atak), i, 2);
                setValueAt(String.valueOf(Symulacja.listaOddzialow.get(i).obrona), i, 3);
                setValueAt(String.valueOf(Symulacja.listaOddzialow.get(i).sila), i, 4);
            }
        }
    }

    private static class Rzad {

        private String oddzial;
        private String ruch;
        private String zdrowie;
        private String atak;
        private String obrona;
        private String sila;

         Rzad() {}

        public String odczytajOddzial() { return this.oddzial; }
        public String odczytajRuch() { return this.ruch; }
        public String odczytajZdrowie() { return this.zdrowie; }
        public String odczytajAtak() { return this.atak; }
        public String odczytajObrone() { return this.obrona; }
        public String odczytajSile() { return this.sila; }

        public void ustawOddzial(final String oddzial) { this.oddzial = oddzial; }
        public void ustawRuch(final String ruch) { this.ruch = ruch; }
        public void ustawZdrowie(final String zdrowie) { this.zdrowie = zdrowie; }
        public void ustawAtak(final String atak) { this.atak = atak; }
        public void ustawObrone(final String obrona) { this.obrona = obrona; }
        public void ustawSile(final String sila) { this.sila = sila; }

    }
}
