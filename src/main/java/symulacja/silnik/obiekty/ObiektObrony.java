package symulacja.silnik.obiekty;

import symulacja.silnik.mapa.Pole;
import symulacja.silnik.oddzialy.Oddzial;

public class ObiektObrony extends Obiekt {

    //Zwiększenie obrony Oddziału który znajduje się na tym samym polu
    public static final int wartoscObrony = 5;

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
        oddzial.obrona = oddzial.resztaObrona + wartoscObrony;
    }
}
