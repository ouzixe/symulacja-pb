package symulacja.silnik.oddzialy;

import symulacja.silnik.mapa.Pole;
import symulacja.silnik.obiekty.Obiekt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static symulacja.gui.Plansza.mapa;

/**
 * Klasa do tworzenia oddziałów piechoty, jednostek ze średnim atakiem i obroną.
 */
public class OddzialPiechoty extends Oddzial {

    /**
     * Metoda główna {@link OddzialPiechoty}.
     * @param numer
     * Numer pomagający w identyfikacji oddziału.
     * @param wspolrzedne
     * {@link Pole.Wspolrzedne} pomagające w przypisaniu {@link Pole}.
     * @param oddzialTyp
     * {@link Typ} oddziału regulujący jego ruchy.
     */
    public OddzialPiechoty(int numer, Pole.Wspolrzedne wspolrzedne, Typ oddzialTyp) {
        super(numer, wspolrzedne, oddzialTyp);
    }


    /** Stała używana do przypisania początkowej wartości {@link Oddzial#atak}. */
    private static final int STALA_ATAK = 10;
    /** Stała używana do przypisania początkowej wartości {@link Oddzial#obrona}. */
    private static final int STALA_OBRONA = 10;

    /**
     * Metoda toString pomagająca odczytowi ikon oddziałów.
     * @return Wartość toString {@link Oddzial.Typ}.
     */
    @Override
    public String toString() { return Typ.PIECHOTA.toString(); }

    /**
     * Przeliczanie statystyk {@link OddzialPiechoty} na podstawie {@link OddzialPiechoty#zycie}.
     * @param oddzial
     * {@link OddzialPiechoty}, którego statystyki są przeliczane.
     */
    @Override
    public void przeliczStatystyki(Oddzial oddzial) {
        oddzial.atak = STALA_ATAK * oddzial.zycie / 10 + oddzial.resztaAtak;
        oddzial.obrona = STALA_OBRONA * oddzial.zycie / 10 + oddzial.resztaObrona;
    }


    /**
     * Obliczanie możliwych {@link Ruch} dla {@link OddzialPiechoty}.
     * @param listaZbadanychPol
     * Lista {@link Pole} zbadanych przez {@link Zwiad}.
     * @param poruszonyOddzial
     * {@link OddzialPiechoty} dla którego są obliczane ruchy.
     * @return Lista {@link Ruch}, które może wykonać {@link OddzialPiechoty}.
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
                if (pole.odczytajObiekt().odczytajTyp() != Obiekt.Typ.TEREN && pole.odczytajObiekt().odczytajTyp() != Obiekt.Typ.GRANICA) {
                    mozliweRuchy.add(new Ruch.Przejecie(mapa, poruszonyOddzial, pole, Ruch.TypRuchu.PRZEJECIE));
                }
                continue;
            }
            mozliweRuchy.add(new Ruch.Przemieszczenie(mapa, poruszonyOddzial, pole, Ruch.TypRuchu.PRZEMIESZCZENIE));
        }
        return Collections.unmodifiableList(mozliweRuchy);
    }
}