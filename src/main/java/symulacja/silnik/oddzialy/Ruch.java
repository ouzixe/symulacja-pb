package symulacja.silnik.oddzialy;

import symulacja.silnik.mapa.Mapa;
import symulacja.silnik.mapa.Pole;
import symulacja.silnik.obiekty.Obiekt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Klasa do obliczania możliwych ruchów.
 */
public class Ruch {

    /** {@link Mapa} do której obliczane są ruchy. */
    static Mapa mapa;
    /** {@link Oddzial} dla którego obliczane są ruchy. */
    final Oddzial poruszonyOddzial;
    /** {@link Pole} do którego obliczany jest ruch. */
    final Pole docelowePole;
    /** Typ wykonywanego ruchu, zależny od {@link Pole}. */
    final TypRuchu typRuchu;

    /** Typ wyliczeniowy służący określeniu rodzaju {@link Ruch}. */
    public enum TypRuchu {
        ODPOCZYNEK("ODPOCZYNEK"),
        PRZEMIESZCZENIE("PRZEMIESZCZENIE"),
        PRZEJECIE("PRZEJECIE"),
        ATAK("ATAK");

        private final String typRuchu;

        TypRuchu(String typRuchu) { this.typRuchu = typRuchu; }

        @Override
        public String toString() { return this.typRuchu; }
    }

    /**
     * Metoda główna klasy {@link Ruch}.
     * @param mapa
     * {@link Mapa} do której obliczany jest ruch.
     * @param poruszonyOddzial
     * {@link Oddzial} którego ruch jest obliczany.
     * @param docelowePole
     * {@link Pole} do którego ruch jest obliczany.
     * @param typRuchu
     * {@link TypRuchu} który jest wykonywany.
     */
    protected Ruch(final Mapa mapa,
                 final Oddzial poruszonyOddzial,
                 final Pole docelowePole,
                 final TypRuchu typRuchu) {
        Ruch.mapa = mapa;
        this.poruszonyOddzial = poruszonyOddzial;
        this.docelowePole = docelowePole;
        this.typRuchu = typRuchu;
    }

    /**
     * Odczytanie {@link TypRuchu}.
     * @return {@link TypRuchu}.
     */
    public TypRuchu odczytajTypRuchu() { return typRuchu; }

    /**
     * Odczytanie poruszonego {@link Oddzial}.
     * @return Poruszony {@link Oddzial}.
     */
    public Oddzial odczytajPoruszonyOddzial() { return poruszonyOddzial; }

    /**
     * Odczytanie docelowego {@link Pole}.
     * @return Docelowe {@link Pole}.
     */
    public Pole odczytajDocelowePole() { return docelowePole; }

    /**
     * Klasa definiująca {@link TypRuchu#ODPOCZYNEK}.
     */
    static final class Odpoczynek extends symulacja.silnik.oddzialy.Ruch {
        Odpoczynek(Mapa mapa, Oddzial oddzial, Pole docelowePole, TypRuchu typRuchu) {
            super(mapa, oddzial, docelowePole, typRuchu);
        }

        @Override
        public String toString() { return TypRuchu.ODPOCZYNEK.toString(); }
    }

    /**
     * Klasa definiująca {@link TypRuchu#PRZEMIESZCZENIE}.
     */
    static final class Przemieszczenie extends symulacja.silnik.oddzialy.Ruch {
        Przemieszczenie(Mapa mapa, Oddzial oddzial, Pole docelowePole, TypRuchu typRuchu) {
            super(mapa, oddzial, docelowePole, typRuchu);
        }

        @Override
        public String toString() { return TypRuchu.PRZEMIESZCZENIE.toString(); }
    }

    /**
     * Klasa definiująca {@link TypRuchu#PRZEJECIE}.
     */
    static final class Przejecie extends symulacja.silnik.oddzialy.Ruch {

        /** {@link Obiekt} który jest przejmowany. */
        final Obiekt docelowyObiekt;

        Przejecie(Mapa mapa, Oddzial poruszonyOddzial, Pole docelowePole, TypRuchu typRuchu) {
            super(mapa, poruszonyOddzial, docelowePole, typRuchu);
            this.docelowyObiekt = docelowePole.odczytajObiekt();
        }

        @Override
        public String toString() { return TypRuchu.PRZEJECIE.toString(); }
    }

    /**
     * Klasa definiująca {@link TypRuchu#ATAK}.
     */
    static final class Atak extends symulacja.silnik.oddzialy.Ruch {

        /** Atakowany {@link Oddzial}. */
        final Oddzial zaatakowanyOddzial;

        Atak(Mapa mapa, Oddzial poruszonyOddzial, Pole docelowePole, TypRuchu typRuchu) {
            super(mapa, poruszonyOddzial, docelowePole, typRuchu);
            this.zaatakowanyOddzial = docelowePole.odczytajOddzial();
        }

        @Override
        public String toString() { return TypRuchu.ATAK.toString(); }
    }
}