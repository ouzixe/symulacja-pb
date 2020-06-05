package symulacja.silnik.obiekty;

import symulacja.silnik.mapa.Pole;
import symulacja.silnik.oddzialy.Oddzial;

//Jednorazowe zwiększenie "życia" pierwszego Oddziału który wejdzie w interakcję

public class ObiektWyposazenia extends Obiekt {

    final static int wartoscZasobow = 3;

    public ObiektWyposazenia(Pole.Wspolrzedne obiektPozycja) {
        super(obiektPozycja);
    }

    @Override
    public String toString() {
        return Typ.WYPOSAZENIE.toString();
    }

    @Override
    public Typ odczytajTyp() { return Typ.WYPOSAZENIE; }

    public static void przejecie(Oddzial oddzial) {
        oddzial.zycie = oddzial.zycie + wartoscZasobow;
    }
}
