package symulacja;

import symulacja.silnik.oddzialy.Oddzial;
import symulacja.silnik.oddzialy.Ruch;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//Utworzenie i trzymanie listy logów, po zakończeniu symulacji wygenerowanie z nich pliku.

public class PlikRaportu {

    public static List<Ruch> ruchy = new ArrayList<>();
    private static List<String> listaRuchow = new ArrayList<>();

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

    public static void oddzielRuchy(char c) {
        listaRuchow.add(String.valueOf(c).repeat(32));
    }

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

    public static void oddzialPolegl(Oddzial oddzial) {
        char c = '#';
        String str = "# Polegl oddzial " + oddzial.numer + ": ";
        if(oddzial.odczytajTyp() == Oddzial.Typ.PIECHOTA) str = str + "Piechota";
        else str = str + "Zmotoryzowany";
        oddzielRuchy(c);
        listaRuchow.add(str);
        oddzielRuchy(c);
    }

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

    public static void wyczyscListy() {
        ruchy = new ArrayList<>();
        listaRuchow = new ArrayList<>();
    }
}