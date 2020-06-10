package symulacja.silnik.oddzialy;

import symulacja.Symulacja;
import symulacja.silnik.mapa.Mapa;
import symulacja.silnik.mapa.Pole;
import symulacja.silnik.obiekty.Obiekt;
import symulacja.silnik.obiekty.ObiektAtaku;
import symulacja.silnik.obiekty.ObiektObrony;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Klasa do tworzenia oddziałów oraz listy oddziałów.
 */
public abstract class Oddzial {

    /** Stała używana do przypisania początkowej wartości {@link Oddzial#zycie}. */
    private static final int STALA_ZYCIE = 10;
    /** Stała używana do przypisania początkowej wartości {@link Oddzial#atak}. */
    private static final int STALA_ATAK = 10;
    /** Stała używana do przypisania początkowej wartości {@link Oddzial#obrona}. */
    private static final int STALA_OBRONA = 10;
    /** Stała używana do przypisania początkowej wartości {@link Oddzial#sila}. */
    private static final int STALA_SILA = 5;

    /** Wartość określająca zdolność {@link Oddzial} do ruchu. */
    public int zycie;
    /** Wartość określająca zdolność atakowania {@link Oddzial}. */
    public int atak;
    /** Wartość określająca zdolność obrony {@link Oddzial}. */
    public int obrona;
    /** Wartość określająca zmęczenie {@link Oddzial}. */
    public int sila;

    /** Wartość określająca atak dodany przez {@link ObiektAtaku}. */
    public int resztaAtak;
    /** Wartość określająca obronę dodaną przez {@link ObiektObrony}. */
    public int resztaObrona;

    /** Numer oddziału pomagający w identyfikacji. */
    public final int numer;
    /** {@link Typ} oddziału regulujący jego ruchy. */
    protected final Typ typOddzialu;
    /** {@link Pole.Wspolrzedne} oddziału. */
    protected final Pole.Wspolrzedne wspolrzedne;

    /**
     * Metoda główna oddziału.
     * @param numer
     * Numer pomagający w identyfikacji oddziału.
     * @param wspolrzedne
     * {@link Pole.Wspolrzedne} pomagające w przypisaniu {@link Pole}.
     * @param typOddzialu
     * {@link Typ} oddziału regulujący jego ruchy.
     */
    Oddzial(final int numer, final Pole.Wspolrzedne wspolrzedne, final Typ typOddzialu) {
        this.zycie = STALA_ZYCIE;
        this.atak = STALA_ATAK;
        this.obrona = STALA_OBRONA;
        this.sila = STALA_SILA;
        this.resztaAtak = 0;
        this.resztaObrona = 0;
        this.numer = numer;
        this.wspolrzedne = wspolrzedne;
        this.typOddzialu = typOddzialu;
    }

    /**
     * Odczytywanie {@link Pole.Wspolrzedne}.
     * @return {@link Pole.Wspolrzedne} dane {@link Oddzial}.
     */
    public Pole.Wspolrzedne wspolrzedne() {
        return this.wspolrzedne;
    }

    /**
     * Typ wyliczeniowy służący określeniu rodzaju {@link Oddzial}.
     */
    public enum Typ {

        PIECHOTA("P"),
        ZMOTORYZOWANY("Z");

        private final String oddzialTyp;

        Typ(String oddzialTyp) {
            this.oddzialTyp = oddzialTyp;
        }
        @Override
        public String toString() {
            return this.oddzialTyp;
        }
    }

    /**
     * Tworzenie listy {@link Oddzial} używanej później w symulacji.
     * @return Lista {@link Oddzial}.
     */
    public static List<Oddzial> utworzListeOddzialow() {
        List<Oddzial> listaOddzialow = new ArrayList<>();

        int[] indeksy = new int[Symulacja.odczytajLiczbeOddzialow()];
        zewnetrzna:
        for(int i = 0; i < Symulacja.odczytajLiczbeOddzialow(); i++) {
            indeksy[i] = ThreadLocalRandom.current().nextInt(0, Symulacja.listaWspolrzednych.size() - 1);
            for(Obiekt obiekt : Symulacja.listaObiektow) {
                if(Symulacja.listaWspolrzednych.get(indeksy[i]) == obiekt.wspolrzedne()) {
                    i--;
                    continue zewnetrzna;
                }
            }
            for(Oddzial oddzial : listaOddzialow) {
                if(Symulacja.listaWspolrzednych.get(indeksy[i]) == oddzial.wspolrzedne) {
                    i--;
                    continue zewnetrzna;
                }
            }
            if(i < 3 * Symulacja.odczytajLiczbeOddzialow() / 4) {
                listaOddzialow.add(new OddzialPiechoty(i+1, Symulacja.listaWspolrzednych.get(indeksy[i]), Typ.PIECHOTA));
            } else {
                listaOddzialow.add(new OddzialZmotoryzowany(i+1, Symulacja.listaWspolrzednych.get(indeksy[i]), Typ.ZMOTORYZOWANY));
            }
        }
        return listaOddzialow;
    }

    /**
     * Odczytywanie {@link Pole.Wspolrzedne}.
     * @return {@link Pole.Wspolrzedne} dane {@link Oddzial}.
     */
    public Pole odczytajPole() {
        for(Pole pole : Mapa.listaPol) {
            if(pole.odczytajOddzial() == this) return pole;
        }
        return null;
    }

    /** Odczytywanie {@link Oddzial.Typ}.
     * @return {@link Oddzial.Typ} danego {@link Oddzial}.
     */
    public Typ odczytajTyp() {
        return this.typOddzialu;
    }

    /** Ustawienie wszystkich statystyk {@link Oddzial} na 0 */
    public void wyzerujStatystyki() {
        this.zycie = 0;
        this.obrona = 0;
        this.atak = 0;
        this.sila = 0;
    }

    /**
     * Obliczanie możliwych {@link Ruch} dla {@link Oddzial}.
     * @param listaZbadanychPol
     * Lista {@link Pole} zbadanych przez {@link Zwiad}.
     * @param poruszonyOddzial
     * {@link Oddzial} dla którego są obliczane ruchy.
     * @return Lista {@link Ruch}, które może wykonać {@link Oddzial}.
     */
    public List<Ruch> obliczRuchy(final List<Pole> listaZbadanychPol, final Oddzial poruszonyOddzial) { return null; }

    /**
     * Przeliczanie statystyk {@link Oddzial} na podstawie {@link Oddzial#zycie}.
     * @param oddzial
     * {@link Oddzial}, którego statystyki są przeliczane.
     */
    public void przeliczStatystyki(Oddzial oddzial) { }
}