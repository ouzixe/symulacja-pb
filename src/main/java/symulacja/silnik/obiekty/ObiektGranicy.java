package symulacja.silnik.obiekty;

import symulacja.Symulacja;
import symulacja.silnik.mapa.Pole;

import java.util.ArrayList;
import java.util.List;

//Zmniejszanie mapy, zmuszając oddziały do ruchu

public class ObiektGranicy extends Obiekt{

    public ObiektGranicy(Pole.Wspolrzedne obiektPozycja) {
        super(obiektPozycja);
    }

    public static List<ObiektGranicy> utworzListeObiektowGranic() {

        List<ObiektGranicy> listaObiektowGranic = new ArrayList<>();

        for(Pole.Wspolrzedne wspolrzedne : Symulacja.listaWspolrzednych) {
            listaObiektowGranic.add(new ObiektGranicy(wspolrzedne));
        }
        return listaObiektowGranic;
    }

    @Override
    public String toString() {
        return Typ.GRANICA.toString();
    }

    @Override
    public Typ odczytajTyp() { return Typ.GRANICA; }

}