package symulacja.silnik.oddzialy;

import symulacja.silnik.mapa.Mapa;
import symulacja.silnik.mapa.Pole;
import symulacja.silnik.obiekty.Obiekt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OddzialPiechoty extends Oddzial {

    public OddzialPiechoty(int numer, Pole.Wspolrzedne wspolrzedne, Typ oddzialTyp) {
        super(numer, wspolrzedne, oddzialTyp);
    }


    @Override
    public String toString() { return Typ.PIECHOTA.toString(); }

}
