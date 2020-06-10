package symulacja.silnik.oddzialy;

import symulacja.silnik.mapa.Pole;
import symulacja.silnik.obiekty.Obiekt;
import symulacja.silnik.obiekty.ObiektObrony;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static symulacja.gui.Plansza.mapa;

/**
 * Klasa do tworzenia oddziałów zmotoryzowanych, jednostek z wyższym atakiem
 * i niższą obroną niż standardowe oddziały. Nie mogą one zajmować {@link ObiektObrony}.
 */
public class OddzialZmotoryzowany extends Oddzial {

    /**
     * Metoda główna {@link OddzialZmotoryzowany}.
     * @param numer
     * Numer pomagający w identyfikacji oddziału.
     * @param wspolrzedne
     * {@link Pole.Wspolrzedne} pomagające w przypisaniu {@link Pole}.
     * @param oddzialTyp
     * {@link Typ} oddziału regulujący jego ruchy.
     */
    public OddzialZmotoryzowany(int numer, Pole.Wspolrzedne wspolrzedne, Typ oddzialTyp) {
        super(numer, wspolrzedne, oddzialTyp);
    }

    /** Stała używana do przypisania początkowej wartości {@link Oddzial#atak}. */
    private static final int STALA_ATAK = 13;
    /** Stała używana do przypisania początkowej wartości {@link Oddzial#obrona}. */
    private static final int STALA_OBRONA = 7;

    /**
     * Metoda toString pomagająca odczytowi ikon oddziałów.
     * @return Wartość toString {@link Oddzial.Typ}.
     */
    @Override
    public String toString() { return Typ.ZMOTORYZOWANY.toString(); }

    /**
     * Przeliczanie statystyk {@link OddzialZmotoryzowany} na podstawie {@link OddzialZmotoryzowany#zycie}.
     * @param oddzial
     * {@link OddzialZmotoryzowany}, którego statystyki są przeliczane.
     */
    @Override
    public void przeliczStatystyki(Oddzial oddzial) {
        oddzial.atak = STALA_ATAK * oddzial.zycie / 10 + oddzial.resztaAtak;
        oddzial.obrona = STALA_OBRONA * oddzial.zycie / 10 + oddzial.resztaObrona;
    }

    /**
     * Obliczanie możliwych {@link Ruch} dla {@link OddzialZmotoryzowany}.
     * @param listaZbadanychPol
     * Lista {@link Pole} zbadanych przez {@link Zwiad}.
     * @param poruszonyOddzial
     * {@link OddzialZmotoryzowany} dla którego są obliczane ruchy.
     * @return Lista {@link Ruch}, które może wykonać {@link OddzialZmotoryzowany}.
     */
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