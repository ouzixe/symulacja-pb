package symulacja.silnik.obiekty;

import symulacja.silnik.mapa.Pole;
import symulacja.silnik.oddzialy.Oddzial;

/**
 * Ograniczenie terenu dostępnego dla {@link Oddzial}.
 */
public class ObiektTerenu extends Obiekt{

    /**
     * Metoda główna {@link ObiektTerenu}.
     * @param obiektPozycja
     * {@link Pole.Wspolrzedne} przypisane obiektowi.
     */
    public ObiektTerenu(Pole.Wspolrzedne obiektPozycja) {
        super(obiektPozycja);
    }

    /**
     * Metoda toString pomagająca odczytowi ikon obiektów.
     * @return Wartość toString {@link Obiekt.Typ}.
     */
    @Override
    public String toString() {
        return Typ.TEREN.toString();
    }

    /**
     * Metoda do odczytania typu {@link Obiekt}.
     * @return {@link Obiekt.Typ#TEREN};
     */
    @Override
    public Typ odczytajTyp() { return Typ.TEREN; }
}