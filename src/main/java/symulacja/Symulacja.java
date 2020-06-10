package symulacja;

import symulacja.gui.Plansza;
import symulacja.silnik.obiekty.ObiektGranicy;
import symulacja.silnik.tura.Dowodca;
import symulacja.silnik.mapa.Pole;
import symulacja.silnik.obiekty.Obiekt;
import symulacja.silnik.oddzialy.Oddzial;

import java.util.List;

/**
 * Klasa główna symulacji, zawierająca też listy {@link Pole.Wspolrzedne}, {@link Obiekt},
 * {@link Oddzial} i {@link Dowodca}.
 */
public class Symulacja {

    /** Szerokość planszy w symulacji. */
    static int szerokosc = 15;
    /** Wysokość planszy w symulacji. */
    static int wysokosc = 15;
    /** Zagęszczenie {@link Obiekt} w symulacji. */
    static int zageszczenie = 5;
    /** Liczba {@link Oddzial} w symulacji. */
    static int liczbaOddzialow = 5;

    /** Lista {@link Pole.Wspolrzedne}, używana do identyfikacji {@link Pole}, {@link Obiekt} i {@link Oddzial}. */
    public static List<Pole.Wspolrzedne> listaWspolrzednych;
    /** Lista {@link Obiekt}. */
    public static List<Obiekt> listaObiektow;
    /** Lista {@link ObiektGranicy}. */
    public static List<ObiektGranicy> listaObiektowGranicy;
    /** Lista {@link Oddzial}. */
    public static List<Oddzial> listaOddzialow;
    /** Lista {@link Dowodca}. */
    public static List<Dowodca> listaDowodcow;
    /** Powtorzenie symulacji. */
    static int powtorzenie;
    /** Status działania symulacji. */
    static boolean dzialanieSymulacji;

    /** Główna {@link Plansza}, stanowiąca interfejs symulacji. */
    private static Plansza plansza;

    /** Metoda <code>main</code> używana do pierwszego uruchomienia symulacji. */
    public static void main(String[] args){
        nowaSymulacja();
    }

    /** Metoda do pozbycia się starej symulacji i utworzenia nowej. */
    public static void nowaSymulacja() {
        if(plansza != null) plansza.dispose();
        odczytajKonfiguracje();
        listaWspolrzednych = Pole.Wspolrzedne.utworzListeWspolrzednych(szerokosc, wysokosc);
        listaObiektow = Obiekt.utworzListeObiektow(zageszczenie);
        listaOddzialow = Oddzial.utworzListeOddzialow();
        listaDowodcow = Dowodca.utworzListeDowodzcow(listaOddzialow);
        listaObiektowGranicy = ObiektGranicy.utworzListeObiektowGranic();
        PlikRaportu.wyczyscListy();
        powtorzenie = 0;
        dzialanieSymulacji = true;
        plansza = new Plansza(szerokosc, wysokosc, listaWspolrzednych, listaDowodcow, powtorzenie);
    }

    /** Odczytanie wartości z {@link PlikKonfiguracyjny} lub stworzenie nowego. */
    private static void odczytajKonfiguracje() {
        if (PlikKonfiguracyjny.czyIstnieje()) {
            szerokosc = PlikKonfiguracyjny.odczytajWartosc("szerokosc");
            wysokosc = PlikKonfiguracyjny.odczytajWartosc("wysokosc");
            zageszczenie = PlikKonfiguracyjny.odczytajWartosc("zageszczenie");
            liczbaOddzialow = PlikKonfiguracyjny.odczytajWartosc("liczba-oddzialow");
        } else {
            PlikKonfiguracyjny.stworzKonfiguracje();
        }
    }

    /**
     * Odczytanie {@link Symulacja#szerokosc}.
     * @return {@link Symulacja#szerokosc}.
     */
    public static int odczytajSzerokosc() {
        return szerokosc;
    }

    /**
     * Odczytanie {@link Symulacja#wysokosc}.
     * @return {@link Symulacja#wysokosc}.
     */
    public static int odczytajWysokosc() {
        return wysokosc;
    }
    public static void dodajPowtorzenie() { powtorzenie++; }

    /**
     * Odczytanie {@link Symulacja#powtorzenie}.
     * @return {@link Symulacja#powtorzenie}.
     */
    public static int odczytajPowtorzenie() { return powtorzenie; }

    /**
     * Odczytanie {@link Symulacja#liczbaOddzialow}.
     * @return {@link Symulacja#liczbaOddzialow}.
     */
    public static int odczytajLiczbeOddzialow() { return liczbaOddzialow; }

    /**
     * Odczytanie {@link Symulacja#dzialanieSymulacji}.
     * @return {@link Symulacja#dzialanieSymulacji}.
     */
    public static boolean czySymulacjaDziala() { return dzialanieSymulacji; }

    /** Ustawienie {@link Symulacja#dzialanieSymulacji} na <code>false</code> */
    public static void wylaczSymulacje() { dzialanieSymulacji = false; }
}