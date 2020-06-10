package symulacja.silnik.mapa;

import symulacja.silnik.obiekty.*;
import symulacja.silnik.oddzialy.Oddzial;

import java.util.*;

/**
 * Klasa odpowiedzialna za tworzenie współrzędnych oraz pól.
 */

public class Pole {

    /**
     * Klasa odpowiedzialna za tworzenie współrzędnych.
     */
    public static class Wspolrzedne {
        public final int x;
        public final int y;
        public Wspolrzedne(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Metoda odpowiedzialna za tworzenie stałej listy {@link Wspolrzedne}.
         */
        public static List<Wspolrzedne> utworzListeWspolrzednych(int szerokosc, int wysokosc) {
            final List<Pole.Wspolrzedne> listaWspolrzednych = new ArrayList<>();
            for(int i = 0; i < wysokosc; i++) {
                for(int j = 0; j < szerokosc; j++) {
                    Pole.Wspolrzedne wspolrzedne = new Pole.Wspolrzedne(j+1, wysokosc-i);
                    listaWspolrzednych.add(i * szerokosc + j, wspolrzedne);
                }
            }
            return listaWspolrzednych;
        }
    }

    /** {@link Wspolrzedne} pola. */
    public final Wspolrzedne wspolrzedne;
    /** {@link Obiekt} pola. */
    private final Obiekt obiektNaPolu;
    /** {@link Oddzial} pola. */
    private final Oddzial oddzialNaPolu;

    /**
     * Metoda utworzenia jednego pola.
     * @param wspolrzedne
     * {@link Wspolrzedne}.
     * @param obiekt
     * {@link Obiekt}.
     * @param oddzial
     * {@link Oddzial}.
     */
    public Pole(final Wspolrzedne wspolrzedne, final Obiekt obiekt, final Oddzial oddzial) {
        this.wspolrzedne = wspolrzedne;
        this.obiektNaPolu = obiekt;
        this.oddzialNaPolu = oddzial;
    }

    /** Odczytanie {@link Obiekt} na {@link Pole}.
     * @return {@link Obiekt} znajdujący się na {@link Pole}.
     */
    public Obiekt odczytajObiekt() {
        return this.obiektNaPolu;
    }

    /** Odczytanie {@link Oddzial} na {@link Pole}.
     * @return {@link Oddzial} znajdujący się na {@link Pole}.
     */
    public Oddzial odczytajOddzial() {
        return this.oddzialNaPolu;
    }

    /** Sprawdzenie czy {@link Obiekt} na danym {@link Pole} to {@link ObiektGranicy}.
     * @return true jeśli na {@link Pole} znajduje się {@link ObiektGranicy}, false jeżeli nie
     */
    public boolean czyGranica() {
        return this.obiektNaPolu != null && this.obiektNaPolu.odczytajTyp() == Obiekt.Typ.GRANICA;
    }
}