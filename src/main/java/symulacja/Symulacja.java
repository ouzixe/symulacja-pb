package symulacja;

import symulacja.gui.Plansza;
import symulacja.silnik.obiekty.ObiektGranicy;
import symulacja.silnik.tura.Dowodca;
import symulacja.silnik.mapa.Pole;
import symulacja.silnik.obiekty.Obiekt;
import symulacja.silnik.oddzialy.Oddzial;

import java.util.List;

//Baza danych - listy współrzędnych, obiektów, oddziałów i dowódców. Uruchomienie symulacji.

public class Symulacja {

    static int szerokosc = 15;
    static int wysokosc = 15;
    static int zageszczenie = 5;
    static int liczbaOddzialow = 5;

    public static List<Pole.Wspolrzedne> listaWspolrzednych;
    public static List<Obiekt> listaObiektow;
    public static List<ObiektGranicy> listaObiektowGranicy;
    public static List<Oddzial> listaOddzialow;
    public static List<Dowodca> listaDowodcow;
    static int powtorzenie;
    static boolean dzialanieSymulacji;

    private static Plansza plansza;

    public static void main(String[] args){
        nowaSymulacja();
    }

    //Utworzenie nowej symulacji
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

    //Odczytanie konfiguracji z pliku, jezeli plik konfiguracyjny istnieje
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
