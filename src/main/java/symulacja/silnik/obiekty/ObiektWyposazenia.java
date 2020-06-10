package symulacja.silnik.obiekty;

import symulacja.silnik.mapa.Pole;
import symulacja.silnik.oddzialy.Oddzial;


/**
 * Jednorazowe zwiększenie {@link Oddzial#zycie} który wejdzie w interakcję.
 */
public class ObiektWyposazenia extends Obiekt {

    /** Wartość {@link Oddzial#zycie} do zwiększenia. */
    final static int wartoscZasobow = 3;

    /**
     * Metoda główna {@link ObiektWyposazenia}.
     * @param obiektPozycja
     * {@link Pole.Wspolrzedne} przypisane obiektowi.
     */
    public ObiektWyposazenia(Pole.Wspolrzedne obiektPozycja) {
        super(obiektPozycja);
    }

    /**
     * Metoda do odczytania typu {@link Obiekt}.
     * @return {@link Obiekt.Typ#WYPOSAZENIE};
     */
    @Override
    public Typ odczytajTyp() { return Typ.WYPOSAZENIE; }

    /**
     * Metoda toString pomagająca odczytowi ikon obiektów.
     * @return Wartość toString {@link Obiekt.Typ}.
     */
    @Override
    public String toString() {
        return Typ.WYPOSAZENIE.toString();
    }

    /**
     * Metoda dla przejęcia {@link ObiektWyposazenia}.
     * @param oddzial
     * Zmiana wartości {@link Oddzial#zycie}.
     */
    public static void przejecie(Oddzial oddzial) {
        oddzial.zycie = oddzial.zycie + wartoscZasobow;
    }
}
