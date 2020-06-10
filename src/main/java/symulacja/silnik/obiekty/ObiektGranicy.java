package symulacja.silnik.obiekty;

import symulacja.Symulacja;
import symulacja.silnik.mapa.Mapa;
import symulacja.silnik.mapa.Pole;
import symulacja.silnik.oddzialy.Oddzial;

import java.util.ArrayList;
import java.util.List;

/**
 * Regulowanie rozmiaru mapy, zmuszając {@link Oddzial} do ruchu.
 */
public class ObiektGranicy extends Obiekt{

    /**
     * Metoda główna {@link ObiektGranicy}.
     * @param obiektPozycja
     * {@link Pole.Wspolrzedne} przypisane obiektowi.
     */
    public ObiektGranicy(Pole.Wspolrzedne obiektPozycja) {
        super(obiektPozycja);
    }

    /**
     * Utworzenie listy {@link ObiektGranicy} dla całej {@link Mapa}.
     * @return Lista {@link ObiektGranicy}.
     */
    public static List<ObiektGranicy> utworzListeObiektowGranic() {

        List<ObiektGranicy> listaObiektowGranic = new ArrayList<>();

        for(Pole.Wspolrzedne wspolrzedne : Symulacja.listaWspolrzednych) {
            listaObiektowGranic.add(new ObiektGranicy(wspolrzedne));
        }
        return listaObiektowGranic;
    }

    /**
     * Metoda toString pomagająca odczytowi ikon obiektów.
     * @return Wartość toString {@link Obiekt.Typ}.
     */
    @Override
    public String toString() {
        return Typ.GRANICA.toString();
    }

    /**
     * Metoda do odczytania typu {@link Obiekt}.
     * @return {@link Obiekt.Typ#GRANICA};
     */
    @Override
    public Typ odczytajTyp() { return Typ.GRANICA; }

}