package symulacja.silnik.oddzialy;

import symulacja.Symulacja;
import symulacja.silnik.mapa.Mapa;
import symulacja.silnik.mapa.Pole;
import symulacja.silnik.obiekty.Obiekt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

//Odpowiada za utworzenie listy Oddziałów


public abstract class Oddzial {

    private static final int STALA_ZYCIE = 10;
    private static final int STALA_ATAK = 10;
    private static final int STALA_OBRONA = 10;
    private static final int STALA_SILA = 5;

    public int zycie;
    public int atak;
    public int obrona;
    public int sila;

    public int resztaAtak;
    public int resztaObrona;

    public final int numer;
    protected final Typ typOddzialu;
    protected final Pole.Wspolrzedne wspolrzedne;

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

    public Pole.Wspolrzedne wspolrzedne() {
        return this.wspolrzedne;
    }

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

    public Pole odczytajPole() {
        for(Pole pole : Mapa.listaPol) {
            if(pole.odczytajOddzial() == this) return pole;
        }
        return null;
    }

    public Typ odczytajTyp() {
        return this.typOddzialu;
    }

    public void wyzerujStatystyki() {
        this.zycie = 0;
        this.obrona = 0;
        this.atak = 0;
        this.sila = 0;
    }

    public List<Ruch> obliczRuchy(final List<Pole> listaZbadanychPol, final Oddzial poruszonyOddzial) {
        return null;
    }

    public void przeliczStatystyki(Oddzial oddzial) { }
}
