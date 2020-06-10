package symulacja.silnik.obiekty;

import symulacja.silnik.mapa.Pole;
import symulacja.silnik.oddzialy.Oddzial;

/**
 * Zwiększenie {@link Oddzial#obrona} który znajduje się na tym samym polu.
 */
public class ObiektObrony extends Obiekt {

    /** Wartość {@link Oddzial#obrona} do zwiększenia. */
    private static final int wartoscObrony = 3;

    /**
     * Metoda główna {@link ObiektObrony}.
     * @param obiektPozycja
     * {@link Pole.Wspolrzedne} przypisane obiektowi.
     */
    public ObiektObrony(Pole.Wspolrzedne obiektPozycja) {
        super(obiektPozycja);
    }

    /**
     * Metoda do odczytania typu {@link Obiekt}.
     * @return {@link Obiekt.Typ#OBRONA};
     */
    @Override
    public Typ odczytajTyp() { return Typ.OBRONA; }

    /**
     * Metoda toString pomagająca odczytowi ikon obiektów.
     * @return Wartość toString {@link Obiekt.Typ}.
     */
    @Override
    public String toString() {
        return Typ.OBRONA.toString();
    }
    /**
     * Metoda dla przejęcia {@link ObiektObrony}.
     * @param oddzial
     * Zmiana wartości {@link Oddzial#obrona}.
     */
    public static void przejecie(Oddzial oddzial) {
        oddzial.resztaObrona = oddzial.resztaObrona + wartoscObrony;
    }

    /**
     * Metoda dla zejścia z {@link ObiektObrony}.
     * @param oddzial
     * Zmiana wartości {@link Oddzial#obrona}.
     */
    public static void zejscie(Oddzial oddzial) { oddzial.resztaObrona = oddzial.resztaObrona - wartoscObrony; }
}
