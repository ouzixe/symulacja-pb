package symulacja.silnik.obiekty;

import symulacja.silnik.mapa.Pole;
import symulacja.silnik.oddzialy.Oddzial;

public class ObiektAtaku extends Obiekt {

    //Permamentne zwiększenie o x ataku Oddziału który pierwszy wejdzie w interakcję
    static final int wartoscAtaku = 10;

    public ObiektAtaku(Pole.Wspolrzedne obiektPozycja) {
        super(obiektPozycja);
    }

    @Override
    public String toString() {
        return Typ.ATAK.toString();
    }

    @Override
    public Typ odczytajTyp() { return Typ.ATAK; }

    public static void przejecie(Oddzial oddzial) {
        oddzial.atak = oddzial.resztaAtak + wartoscAtaku;
    }
}
