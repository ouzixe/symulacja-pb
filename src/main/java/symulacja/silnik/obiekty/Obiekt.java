package symulacja.silnik.obiekty;

import symulacja.Symulacja;
import symulacja.silnik.mapa.Pole;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.StrictMath.round;

/**
 * Klasa do tworzenia obiektów oraz listy obiektów.
 */
public abstract class Obiekt {

    /** Stała używana do obliczania ilości {@link Obiekt}. */
    private static final int STALA_ZAGESZCZENIA = 20;
    /** Pozycja {@link Obiekt} używana do przypisania go do {@link Pole}. */
    protected final Pole.Wspolrzedne obiektPozycja;
    /** Typ {@link Obiekt}. */
    protected Typ obiektTyp;

    /** Odczytywanie {@link Pole.Wspolrzedne}.
     * @return {@link Pole.Wspolrzedne} danego {@link Obiekt}.
     */
    public Pole.Wspolrzedne wspolrzedne() {
        return this.obiektPozycja;
    }

    /** Odczytywanie {@link Obiekt.Typ}.
     * @return {@link Obiekt.Typ} danego {@link Obiekt}.
     */
    public Typ odczytajTyp() {
        return this.obiektTyp;
    }

    /**
     * Typ wyliczeniowy służący określeniu rodzaju {@link Obiekt}.
     */
    public enum Typ {
        ATAK("A"),
        OBRONA("O"),
        WYPOSAZENIE("W"),
        TEREN("T"),
        GRANICA("G");

        public final String obiektTyp;

        Typ(String obiektTyp) {
            this.obiektTyp = obiektTyp;
        }

        @Override
        public String toString() {
            return this.obiektTyp;
        }
    }

    /**
     * Tworzenie listy {@link Obiekt} używanej później w symulacji.
     * @param zageszczenie
     * Zmiana ilości obiektów na mapie dla wartości w zakresie (1 - 5).
     * @return Lista {@link Obiekt}.
     */
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
                    if ( i < 45 * buf / 100) {
                        listaObiektow.add(i, new ObiektWyposazenia(Symulacja.listaWspolrzednych.get(indeksy[i])));
                    } else {
                        listaObiektow.add(i, new ObiektTerenu(Symulacja.listaWspolrzednych.get(indeksy[i])));
                    }
                }
            }
        }
        return listaObiektow;
    }

    /**
     * Metoda główna {@link Obiekt}.
     * @param obiektPozycja
     * {@link Pole.Wspolrzedne} przypisane obiektowi.
     */
    Obiekt(final Pole.Wspolrzedne obiektPozycja) {
        this.obiektPozycja = obiektPozycja;
    }
}