package symulacja.gui;

import symulacja.PlikRaportu;
import symulacja.Symulacja;
import symulacja.silnik.oddzialy.Ruch;
import symulacja.silnik.tura.WykonanieRuchu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static symulacja.gui.Plansza.mapa;
import static symulacja.gui.Plansza.mapaPanel;

/**
 * Wyświetlanie tabeli z ruchami oddziałów, tabeli z ich statystykami oraz panelu kontrolnego symulacji.
 */
public class PasekBoczny extends JPanel {

    /** Model tablicy ruchów. */
    private final LogiModel modelLog;
    /** ScrollBar do tablicy ruchów. */
    private static JScrollBar sb;
    /** Model tablicy statystyk. */
    private final StatyModel modelStats;

    /** Rozmiary tablicy ruchów. */
    private static final Dimension ROZMIARY_TABELI_LOGOW = new Dimension(250, 400);
    /** Rozmiary tablicy statystyk. */
    private static final Dimension ROZMIARY_TABELI_STATYSTYK = new Dimension(250, 150);
    /** Rozmiary przycisków. */
    private static final Dimension ROZMIARY_PRZYCISKU = new Dimension(200, 30);

    /**
     * Metoda wyświetlania paska bocznego (tabeli: logów i statystyk, przycisków: następna tura i
     * automatyczne przeprowadzenie symulacji).
     */
    PasekBoczny() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        this.modelLog = new LogiModel();
        this.modelStats = new StatyModel();

        final JTable tabelaLogow = new JTable(modelLog);
        tabelaLogow.setRowHeight(15);
        JScrollPane scrollPaneLogi = new JScrollPane(tabelaLogow);
        scrollPaneLogi.setColumnHeaderView(tabelaLogow.getTableHeader());
        scrollPaneLogi.setPreferredSize(ROZMIARY_TABELI_LOGOW);
        sb = scrollPaneLogi.getVerticalScrollBar();
        modelLog.wypelnijTabele();
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        this.add(scrollPaneLogi, c);

        final JTable tabelaStats = new JTable(modelStats);
        tabelaStats.setRowHeight(15);
        JScrollPane scrollPaneStaty = new JScrollPane(tabelaStats);
        scrollPaneStaty.setColumnHeaderView(tabelaStats.getTableHeader());
        scrollPaneStaty.setPreferredSize(ROZMIARY_TABELI_STATYSTYK);
        modelStats.wypelnijTabele();
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 1;
        this.add(scrollPaneStaty, c);

        final JButton nastepnaTura = new JButton("Nastepna tura");
        nastepnaTura.setPreferredSize(ROZMIARY_PRZYCISKU);
        c.weighty = 0.0;
        c.gridx = 0;
        c.gridy = 2;
        this.add(nastepnaTura, c);

        final JButton automatycznie = new JButton("Przeprowadz automatycznie");
        automatycznie.setPreferredSize(ROZMIARY_PRZYCISKU);
        c.gridx = 0;
        c.gridy = 3;
        this.add(automatycznie, c);

        tabelaLogow.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaLogow.getColumnModel().getColumn(1).setPreferredWidth(200);
        this.setVisible(true);

        nastepnaTura.addActionListener(e -> {
            if(Symulacja.czySymulacjaDziala()) {
                WykonanieRuchu.wykonanieRuchu();
                modelStats.wypelnijTabele();
                modelLog.wypelnijTabele();
                modelLog.fireTableDataChanged();
                mapaPanel.rysujMape(mapa);
                validate();
                aktualizujScroll();
            }
        });

        automatycznie.addActionListener(e -> {
            while(Symulacja.czySymulacjaDziala()) {
                WykonanieRuchu.wykonanieRuchu();
            }
            modelStats.wypelnijTabele();
            modelLog.wypelnijTabele();
            modelLog.fireTableDataChanged();
            mapaPanel.rysujMape(mapa);
            validate();
            aktualizujScroll();
        });
    }

    /** Ustawianie paska przewijania na dole, gdzie znajdują sie najnowsze ruchy. */
    private static void aktualizujScroll() {
        sb.setValue( sb.getMaximum() );
    }

    /** Klasa dla modelu tablicy ruchów. */
    private static class LogiModel extends DefaultTableModel {

        /** Wartości wpisane w tablicę ruchów */
        private final List<Rzad> wartosci;
        /** Nazwy kolumn tablicy ruchów. */
        private static final String[] NAZWY = {"Oddzial", "Ruch"};

        /** Utworzenie pustej listy {@link LogiModel#wartosci}. */
        LogiModel() {
            this.wartosci = new ArrayList<>();
        }

        /**
         * Odczytanie ilości rzędów.
         * @return Ilość rzędów.
         */
        @Override
        public int getRowCount() {
            if(this.wartosci == null) return 0;
            return this.wartosci.size();
        }

        /**
         * Odczytanie ilości kolumn.
         * @return Ilość kolumn.
         */
        @Override
        public int getColumnCount() {
            return NAZWY.length;
        }

        /**
         * Odczytanie wartości w określonym miejscu tabeli ruchów.
         * @param rzad
         * rząd w którym znajduje się komórka docelowa
         * @param kolumna
         * kolumna w której znajduje się komórka docelowa
         * @return Wartość znajdująca się w komórce docelowej, lub <code>null</code> jeżeli komórka jest pusta.
         */
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

        /**
         * Ustawienie wartości w określonym miejscu tabeli ruchów.
         * @param wartosc
         * wartość która będzie wpisana w ustaloną komórkę
         * @param rzad
         * rząd w którym znajduje się komórka docelowa
         * @param kolumna
         * kolumna w której znajduje się komórka docelowa
         */
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
                aktualnyRzad.ustawOddzial(String.valueOf(wartosc));
            }
            if(kolumna == 1) {
                aktualnyRzad.ustawRuch((String)wartosc);
            }
        fireTableCellUpdated(rzad, kolumna);
        }

        /**
         * Odczytanie klasy w danej kolumnie.
         * @param kolumna
         * Kolumna do odczytania.
         * @return {@link Class} znajdująca się w kolumnie.
         */
        @Override
        public Class<?> getColumnClass(final int kolumna) {
            return Ruch.class;
        }

        /**
         * Odczytanie nazwy danej kolumny.
         * @param kolumna
         * Kolumna do odczytania.
         * @return Nazwa danej kolumny.
         */
        @Override
        public String getColumnName(final int kolumna) {
            return NAZWY[kolumna];
        }

        /**
         * Wypełnianie tabeli wartościami z {@link PlikRaportu#ruchy}.
         */
        private void wypelnijTabele() {
            for(int i = 0; i <  PlikRaportu.ruchy.size(); i++) {
                setValueAt(PlikRaportu.ruchy.get(i).odczytajPoruszonyOddzial().numer, i, 0);
                setValueAt(PlikRaportu.ruchy.get(i).toString(), i, 1);
            }
        }
    }

    private static class StatyModel extends DefaultTableModel {

        /** Wartości wpisane w tablicę statystyk */
        private final List<Rzad> wartosci;
        /** Nazwy kolumn tablicy statystyk */
        private static final String[] NAZWY = {"Oddzial", "Zdrowie", "Atak", "Obrona", "Sila"};

        /** Utworzenie pustej listy {@link StatyModel#wartosci} */
        StatyModel() { this.wartosci = new ArrayList<>(); }

        /**
         * Odczytanie ilości rzędów.
         * @return Ilość rzędów.
         */
        @Override
        public int getRowCount() {
            if(this.wartosci == null) return 0;
            return this.wartosci.size();
        }

        /**
         * Odczytanie ilości kolumn.
         * @return Ilość kolumn.
         */
        @Override
        public int getColumnCount() {
            return NAZWY.length;
        }

        /**
         * Odczytanie wartości w określonym miejscu tabeli statystyk.
         * @param rzad
         * rząd w którym znajduje się komórka docelowa
         * @param kolumna
         * kolumna w której znajduje się komórka docelowa
         * @return Wartość znajdująca się w komórce docelowej, lub <code>null</code> jeżeli komórka jest pusta.
         */
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

        /**
         * Ustawienie wartości w określonym miejscu tabeli statystyk.
         * @param wartosc
         * wartość która będzie wpisana w ustaloną komórkę
         * @param rzad
         * rząd w którym znajduje się komórka docelowa
         * @param kolumna
         * kolumna w której znajduje się komórka docelowa
         */
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
            }
            if(kolumna == 2) {
                aktualnyRzad.ustawAtak((String)wartosc);
            }
            if(kolumna == 3) {
                aktualnyRzad.ustawObrone((String)wartosc);
            }
            if(kolumna == 4) {
                aktualnyRzad.ustawSile((String)wartosc);
            }
            fireTableCellUpdated(rzad, kolumna);
        }

        /**
         * Odczytanie klasy w danej kolumnie.
         * @param kolumna
         * Kolumna do odczytania.
         * @return {@link Class} znajdująca się w kolumnie.
         */
        @Override
        public Class<?> getColumnClass(final int kolumna) {
            return Ruch.class;
        }

        /**
         * Odczytanie nazwy danej kolumny.
         * @param kolumna
         * Kolumna do odczytania.
         * @return Nazwa danej kolumny.
         */
        @Override
        public String getColumnName(final int kolumna) {
            return NAZWY[kolumna];
        }

        /**
         * Wypełnianie tabeli wartościami z {@link Symulacja#listaOddzialow}.
         */
        private void wypelnijTabele() {
            for(int i = 0; i < Symulacja.listaOddzialow.size(); i++) {
                setValueAt(String.valueOf(Symulacja.listaOddzialow.get(i).numer), i, 0);
                setValueAt(String.valueOf(Symulacja.listaOddzialow.get(i).zycie), i, 1);
                setValueAt(String.valueOf(Symulacja.listaOddzialow.get(i).atak), i, 2);
                setValueAt(String.valueOf(Symulacja.listaOddzialow.get(i).obrona), i, 3);
                setValueAt(String.valueOf(Symulacja.listaOddzialow.get(i).sila), i, 4);
            }
        }
    }

    /**
     * Klasa rzędu, używana w {@link LogiModel} oraz {@link StatyModel}.
     */
    private static class Rzad {

        /** Numer oddziału. */
        private String oddzial;
        /** Wykonany ruch. */
        private String ruch;
        /** Wartość zdrowia oddziału. */
        private String zdrowie;
        /** Wartość ataku oddziału. */
        private String atak;
        /** Wartość obrony oddziału. */
        private String obrona;
        /** Wartość siły oddziału. */
        private String sila;

        /** Odczytanie numeru oddziału. */
        public String odczytajOddzial() { return this.oddzial; }
        /** Odczytanie wykonanego ruchu. */
        public String odczytajRuch() { return this.ruch; }
        /** Odczytanie wartości zdrowia. */
        public String odczytajZdrowie() { return this.zdrowie; }
        /** Odczytanie wartości ataku. */
        public String odczytajAtak() { return this.atak; }
        /** Odczytanie wartości obrony. */
        public String odczytajObrone() { return this.obrona; }
        /** Odczytanie wartości siły. */
        public String odczytajSile() { return this.sila; }

        /** Ustawienie numeru oddziału. */
        public void ustawOddzial(final String oddzial) { this.oddzial = oddzial; }
        /** Ustawienie wykonanego ruchu. */
        public void ustawRuch(final String ruch) { this.ruch = ruch; }
        /** Ustawienie wartości zdrowia. */
        public void ustawZdrowie(final String zdrowie) { this.zdrowie = zdrowie; }
        /** Ustawienie wartości ataku. */
        public void ustawAtak(final String atak) { this.atak = atak; }
        /** Ustawienie wartości obrony. */
        public void ustawObrone(final String obrona) { this.obrona = obrona; }
        /** Ustawienie wartości siły. */
        public void ustawSile(final String sila) { this.sila = sila; }
    }
}