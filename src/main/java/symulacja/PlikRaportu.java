package symulacja;

import symulacja.silnik.mapa.Mapa;
import symulacja.silnik.oddzialy.Oddzial;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlikRaportu {

    public static final List<Oddzial.Ruch> ruchy = new ArrayList<>();
    private static final List<String> listaRuchow = new ArrayList<>();

    public static void dodajRuch(String przedRuchem, final Oddzial.Ruch ruch) {
        ruchy.add(ruch);
        String baza = ruch.odczytajPoruszonyOddzial().numer + ": " + ruch + " (" + przedRuchem + ")";
        String reszta = null;
        if(ruch.odczytajTypRuchu() != Oddzial.Ruch.TypRuchu.ODPOCZYNEK && ruch.odczytajTypRuchu() != Oddzial.Ruch.TypRuchu.ATAK) {
            reszta = " -> (" + ruch.odczytajDocelowePole().wspolrzedne.x + "," + ruch.odczytajDocelowePole().wspolrzedne.y + ")";
        }
        if(ruch.odczytajTypRuchu() == Oddzial.Ruch.TypRuchu.ODPOCZYNEK) reszta = "";
        if(ruch.odczytajTypRuchu() == Oddzial.Ruch.TypRuchu.ATAK) reszta = " -> " + ruch.odczytajDocelowePole().odczytajOddzial().numer
        + ": " + "(" + ruch.odczytajDocelowePole().wspolrzedne.x + "," + ruch.odczytajDocelowePole().wspolrzedne.y + ")";
        listaRuchow.add(baza+reszta);
    }

    public static void oddzielRuchy() {
        listaRuchow.add("=============");
    }

    public static void zakonczPlik() {
        listaRuchow.add("Symulacja zakonczona");
        try {
            stworzPlik();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void oddzialPolegl(Oddzial oddzial) {
        listaRuchow.add("Polegl oddzial " + oddzial.numer);
    }

    public static void stworzPlik() throws IOException {
        Date data = new Date();
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        System.out.println(dataFormat.format(data));
        FileWriter zapis = new FileWriter("raporty/" + dataFormat.format(data) + ".txt");
        for(String ruch : listaRuchow) {
            zapis.write(ruch);
            zapis.write("\n");
        }
        zapis.close();
    }
}
