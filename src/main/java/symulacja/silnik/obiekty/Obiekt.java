package symulacja.silnik.obiekty;

import symulacja.Symulacja;
import symulacja.silnik.mapa.Pole;
import symulacja.silnik.oddzialy.Oddzial;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.StrictMath.round;

public abstract class Obiekt {

    private static final int STALA_ZAGESZCZENIA = 20;
    protected final Pole.Wspolrzedne obiektPozycja;
    protected Typ obiektTyp;
    public boolean czyMoznaPrzejsc;

    public Pole.Wspolrzedne wspolrzedne() {
        return this.obiektPozycja;
    }

    public Typ odczytajTyp() {
        return this.obiektTyp;
    }

    public enum Typ {
        ATAK("A"),
        OBRONA("O"),
        WYPOSAZENIE("W"),
        TEREN("T"),
        GRANICA("G");

        public String obiektTyp;

        Typ(String obiektTyp) {
            this.obiektTyp = obiektTyp;
        }

        @Override
        public String toString() {
            return this.obiektTyp;
        }
    }

    public static List<Obiekt> utworzListeObiektow(final int zageszczenie) {

        List<Obiekt> listaObiektow = new ArrayList<>();
        int buf = (int) round(2.0 * zageszczenie * Symulacja.listaWspolrzednych.size() / STALA_ZAGESZCZENIA);
        int[] indeksy = new int[buf];
        for(int i = 0; i < buf; i++) {
            indeksy[i] = ThreadLocalRandom.current().nextInt(0, Symulacja.listaWspolrzednych.size());
        }
        for(int i = 0; i < buf; i++) {
            if ( i < 15 * buf / 100) {
                listaObiektow.add(i, new ObiektAtaku(Symulacja.listaWspolrzednych.get(indeksy[i])));
            } else {
                if ( i < 30 * buf / 100) {
                    listaObiektow.add(i, new ObiektObrony(Symulacja.listaWspolrzednych.get(indeksy[i])));
                } else {
                    if ( i < 50 * buf / 100) {
                        listaObiektow.add(i, new ObiektWyposazenia(Symulacja.listaWspolrzednych.get(indeksy[i])));
                    } else {
                        listaObiektow.add(i, new ObiektTerenu(Symulacja.listaWspolrzednych.get(indeksy[i])));
                    }
                }
            }
        }
        return listaObiektow;
    }

    Obiekt(final Pole.Wspolrzedne obiektPozycja) {
        this.obiektPozycja = obiektPozycja;
    }

}
