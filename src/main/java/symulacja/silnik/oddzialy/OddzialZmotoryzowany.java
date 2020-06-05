package symulacja.silnik.oddzialy;

import symulacja.silnik.mapa.Pole;
import symulacja.silnik.obiekty.Obiekt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static symulacja.gui.Plansza.mapa;

//Oddziały specjalne, z wyższym atakiem i niższą obroną. Nie mogą zajmować obiektów obrony

public class OddzialZmotoryzowany extends Oddzial {

    public OddzialZmotoryzowany(int numer, Pole.Wspolrzedne wspolrzedne, Typ oddzialTyp) {
        super(numer, wspolrzedne, oddzialTyp);
    }

    private static final int STALA_ATAK = 13;
    private static final int STALA_OBRONA = 7;

    @Override
    public String toString() { return Typ.ZMOTORYZOWANY.toString(); }

    @Override
    public void przeliczStatystyki(Oddzial oddzial) {
        oddzial.atak = STALA_ATAK * oddzial.zycie / 10 + oddzial.resztaAtak;
        oddzial.obrona = STALA_OBRONA * oddzial.zycie / 10 + oddzial.resztaObrona;
    }

    @Override
    public List<Ruch> obliczRuchy(final List<Pole> listaZbadanychPol, final Oddzial poruszonyOddzial) {

        List<symulacja.silnik.oddzialy.Ruch> mozliweRuchy = new ArrayList<>();
        mozliweRuchy.add(new Ruch.Odpoczynek(mapa, poruszonyOddzial, poruszonyOddzial.odczytajPole(), Ruch.TypRuchu.ODPOCZYNEK));
        for (Pole pole : listaZbadanychPol) {
            if (pole.odczytajOddzial() != null) {
                mozliweRuchy.add(new Ruch.Atak(mapa, poruszonyOddzial, pole, Ruch.TypRuchu.ATAK));
                continue;
            }
            if (pole.odczytajObiekt() != null) {
                if (pole.odczytajObiekt().odczytajTyp() != Obiekt.Typ.TEREN &&
                        pole.odczytajObiekt().odczytajTyp() != Obiekt.Typ.GRANICA &&
                        pole.odczytajObiekt().odczytajTyp() != Obiekt.Typ.OBRONA) {
                    mozliweRuchy.add(new Ruch.Przejecie(mapa, poruszonyOddzial, pole, Ruch.TypRuchu.PRZEJECIE));
                }
                if(pole.odczytajObiekt().odczytajTyp() == Obiekt.Typ.OBRONA) {
                    mozliweRuchy.add(new Ruch.Przemieszczenie(mapa, poruszonyOddzial, pole, Ruch.TypRuchu.PRZEMIESZCZENIE));
                }
                continue;
            }
            mozliweRuchy.add(new Ruch.Przemieszczenie(mapa, poruszonyOddzial, pole, Ruch.TypRuchu.PRZEMIESZCZENIE));
        }
        return Collections.unmodifiableList(mozliweRuchy);
    }
}