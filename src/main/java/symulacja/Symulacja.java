package symulacja;

import symulacja.gui.Plansza;
import symulacja.silnik.obiekty.ObiektGranicy;
import symulacja.silnik.tura.Dowodca;
import symulacja.silnik.mapa.Pole;
import symulacja.silnik.obiekty.Obiekt;
import symulacja.silnik.oddzialy.Oddzial;

import java.util.List;

public class Symulacja {

    static int szerokosc = 15;
    static int wysokosc = 15;
    static int zageszczenie = 5;
    static int liczbaOddzialow = 5;

    //Pełni rolę bazy danych, trzymając listy Współrzędnych, Obiektów i Oddziałów.
    //Odpowiada za samo uruchomienie symulacji.

    public static List<Pole.Wspolrzedne> listaWspolrzednych;
    public static List<Obiekt> listaObiektow;
    public static List<ObiektGranicy> listaObiektowGranicy;
    public static List<Oddzial> listaOddzialow;
    public static List<Dowodca> listaDowodcow;
    static int powtorzenie;
    static boolean dzialanieSymulacji;

    public static void main(String[] args){

        if (PlikKonfiguracyjny.czyIstnieje()) {
            szerokosc = PlikKonfiguracyjny.odczytajWartosc("szerokosc");
            wysokosc = PlikKonfiguracyjny.odczytajWartosc("wysokosc");
            zageszczenie = PlikKonfiguracyjny.odczytajWartosc("zageszczenie");
            liczbaOddzialow = PlikKonfiguracyjny.odczytajWartosc("liczba-oddzialow");
        } else {
            PlikKonfiguracyjny.stworzKonfiguracje();
        }

        //Tworzenie podstawowych list dla dzialania symulacji
        listaWspolrzednych = Pole.Wspolrzedne.utworzListeWspolrzednych(szerokosc, wysokosc);
        listaObiektow = Obiekt.utworzListeObiektow(zageszczenie);
        listaOddzialow = Oddzial.utworzListeOddzialow();
        listaDowodcow = Dowodca.utworzListeDowodzcow(listaOddzialow);

        listaObiektowGranicy = ObiektGranicy.utworzListeObiektowGranic();
        powtorzenie = 0;
        dzialanieSymulacji = true;


        //Wygenerowanie mapy i wyświetlenie jej
        Plansza plansza = new Plansza(szerokosc, wysokosc, listaWspolrzednych, listaObiektow, listaOddzialow, listaDowodcow, powtorzenie);

    }

    public static int odczytajSzerokosc() {
        return szerokosc;
    }
    public static int odczytajWysokosc() {
        return wysokosc;
    }
    public static void dodajPowtorzenie() { powtorzenie++; }
    public static int odczytajPowtorzenie() { return powtorzenie; }
    public static int odczytajLiczbeOddzialow() { return liczbaOddzialow; }
    public static boolean czySymulacjaDziala() { return dzialanieSymulacji; }
    public static void wylaczSymulacje() { dzialanieSymulacji = false; }
}
