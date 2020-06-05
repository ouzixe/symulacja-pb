package symulacja.silnik.obiekty;

import symulacja.silnik.mapa.Pole;
import symulacja.silnik.oddzialy.Oddzial;

//Zwiększenie obrony Oddziału który znajduje się na tym samym polu

public class ObiektObrony extends Obiekt {

    private static final int wartoscObrony = 3;

    public ObiektObrony(Pole.Wspolrzedne obiektPozycja) {
        super(obiektPozycja);
    }

    @Override
    public String toString() {
        return Typ.OBRONA.toString();
    }

    @Override
    public Typ odczytajTyp() { return Typ.OBRONA; }

    public static void przejecie(Oddzial oddzial) {
        oddzial.resztaObrona = oddzial.resztaObrona + wartoscObrony;
    }

    public static void zejscie(Oddzial oddzial) { oddzial.resztaObrona = oddzial.resztaObrona - wartoscObrony; }
}
