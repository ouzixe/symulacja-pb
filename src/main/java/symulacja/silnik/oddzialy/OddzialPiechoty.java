package symulacja.silnik.oddzialy;

import symulacja.silnik.mapa.Pole;

public class OddzialPiechoty extends Oddzial {

    public OddzialPiechoty(int numer, Pole.Wspolrzedne wspolrzedne, Typ oddzialTyp) {
        super(numer, wspolrzedne, oddzialTyp);
    }


    @Override
    public String toString() { return Typ.PIECHOTA.toString(); }

}