package symulacja.silnik.obiekty;

import symulacja.silnik.mapa.Pole;
import symulacja.silnik.oddzialy.Oddzial;

public class ObiektTerenu extends Obiekt{

    //Ograniczenie terenu dostępnego dla Oddziałów

    public ObiektTerenu(Pole.Wspolrzedne obiektPozycja) {
        super(obiektPozycja);
    }

    @Override
    public String toString() {
        return Typ.TEREN.toString();
    }

    @Override
    public Typ odczytajTyp() { return Typ.TEREN; }

}
