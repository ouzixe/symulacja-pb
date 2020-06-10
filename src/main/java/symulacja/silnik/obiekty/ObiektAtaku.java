package symulacja.silnik.obiekty;

import symulacja.silnik.mapa.Pole;
import symulacja.silnik.oddzialy.Oddzial;

/**
 * Permamentne zwiększenie o x {@link Oddzial#atak} który pierwszy wejdzie w interakcję.
 */
public class ObiektAtaku extends Obiekt {

    /** Wartość {@link Oddzial#atak} do zwiększenia. */
    static final int wartoscAtaku = 3;

    /**
     * Metoda główna {@link ObiektAtaku}.
     * @param obiektPozycja
     * {@link Pole.Wspolrzedne} przypisane obiektowi.
     */
    public ObiektAtaku(Pole.Wspolrzedne obiektPozycja) {
        super(obiektPozycja);
    }

    /**
     * Metoda toString pomagająca odczytowi ikon obiektów.
     * @return Wartość toString {@link Obiekt.Typ}.
     */
    @Override
    public String toString() {
        return Typ.ATAK.toString();
    }

    /**
     * Metoda do odczytania typu {@link Obiekt}.
     * @return {@link Obiekt.Typ#ATAK};
     */
    @Override
    public Typ odczytajTyp() { return Typ.ATAK; }

    /**
     * Metoda dla przejęcia {@link ObiektAtaku}.
     * @param oddzial
     * Zmiana wartości {@link Oddzial#atak}.
     */
    public static void przejecie(Oddzial oddzial) {
        oddzial.resztaAtak = oddzial.resztaAtak + wartoscAtaku;
    }
}