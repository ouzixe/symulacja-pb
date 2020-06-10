package symulacja;

import symulacja.silnik.mapa.Pole;
import symulacja.silnik.oddzialy.Oddzial;
import symulacja.silnik.oddzialy.Ruch;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Klasa do utworzenia i przetrzymywania listy wykonanych ruchów oraz do
 * zapisu tej list do pliku na końcu działania symulacji.
 */
public class PlikRaportu {

    /** Lista {@link Ruch}, które zostały wykonane. */
    public static List<Ruch> ruchy = new ArrayList<>();
    /** Lista wartości, które zostaną zapisane do pliku. */
    private static List<String> listaRuchow = new ArrayList<>();

    /**
     * Dodanie {@link Ruch} do {@link PlikRaportu#ruchy} oraz dodanie odpowiednio przygotowanej
     * pozycji w {@link PlikRaportu#listaRuchow}.
     * @param przedRuchem
     * {@link Pole.Wspolrzedne} z których {@link Oddzial} wykonał {@link Ruch}.
     * @param ruch
     * {@link Ruch} wykonany przez {@link Oddzial};
     */
    public static void dodajRuch(String przedRuchem, final Ruch ruch) {
        ruchy.add(ruch);
        String baza = ruch.odczytajPoruszonyOddzial().numer + ": " + ruch + " (" + przedRuchem + ")";
        String reszta = null;
        if(ruch.odczytajTypRuchu() != Ruch.TypRuchu.ODPOCZYNEK && ruch.odczytajTypRuchu() != Ruch.TypRuchu.ATAK) {
            reszta = " -> (" + ruch.odczytajDocelowePole().wspolrzedne.x + "," + ruch.odczytajDocelowePole().wspolrzedne.y + ")";
        }
        if(ruch.odczytajTypRuchu() == Ruch.TypRuchu.ODPOCZYNEK) reszta = "";
        if(ruch.odczytajTypRuchu() == Ruch.TypRuchu.ATAK) reszta = " -> " + ruch.odczytajDocelowePole().odczytajOddzial().numer
        + ": " + "(" + ruch.odczytajDocelowePole().wspolrzedne.x + "," + ruch.odczytajDocelowePole().wspolrzedne.y + ")";
        listaRuchow.add(baza+reszta);
    }

    /**
     * Oddzielenie występujące w celu wyszczególnienia jakiegoś zdarzenia.
     * @param c
     * Znak, z którego wykonana jest linia oddzielająca.
     */
    public static void oddzielRuchy(char c) {
        listaRuchow.add(String.valueOf(c).repeat(32));
    }

    /**
     * Zakończenie {@link PlikRaportu#listaRuchow} i zapisanie go do pliku.
     */
    public static void zakonczPlik() {
        char c = '-';
        oddzielRuchy(c);
        boolean czyZwyciezca = false;
        for(Oddzial oddzial : Symulacja.listaOddzialow) {
            if(oddzial.zycie > 0) {
                listaRuchow.add("Zwyciezyl oddzial nr " + oddzial.numer);
                czyZwyciezca = true;
            }
        }
        if(!czyZwyciezca) listaRuchow.add("Zaden oddzial nie zwyciezyl");
        listaRuchow.add("Symulacja zakonczona");
        try {
            stworzPlik();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dodaje do {@link PlikRaportu#listaRuchow} informację o porażce {@link Oddzial}.
     * @param oddzial
     * {@link Oddzial}, który poległ.
     */
    public static void oddzialPolegl(Oddzial oddzial) {
        char c = '#';
        String str = "# Polegl oddzial " + oddzial.numer + ": ";
        if(oddzial.odczytajTyp() == Oddzial.Typ.PIECHOTA) str = str + "Piechota";
        else str = str + "Zmotoryzowany";
        oddzielRuchy(c);
        listaRuchow.add(str);
        oddzielRuchy(c);
    }

    /**
     * Zapisanie wartości z {@link PlikRaportu#listaRuchow} do pliku i zapisanie go.
     * @throws IOException
     * Ponieważ jest to operacja na pliku.
     */
    public static void stworzPlik() throws IOException {
        Date data = new Date();
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        System.out.println(dataFormat.format(data));
        FileWriter zapis = new FileWriter("zasoby/raporty/" + dataFormat.format(data) + ".txt");
        for(String ruch : listaRuchow) {
            zapis.write(ruch);
            zapis.write("\n");
        }
        zapis.close();
    }

    /**
     * Wyczyszczenie {@link PlikRaportu#listaRuchow} oraz {@link PlikRaportu#ruchy}.
     */
    public static void wyczyscListy() {
        ruchy = new ArrayList<>();
        listaRuchow = new ArrayList<>();
    }
}