package symulacja.silnik.obiekty;

import symulacja.silnik.mapa.Pole;

//Ograniczenie terenu dostępnego dla Oddziałów

public class ObiektTerenu extends Obiekt{

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
