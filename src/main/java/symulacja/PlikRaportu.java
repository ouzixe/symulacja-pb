package symulacja;

import symulacja.silnik.oddzialy.Oddzial;

import java.util.ArrayList;
import java.util.List;

public class PlikRaportu {

    public static final List<Oddzial.Ruch> ruchy = new ArrayList<>();

    public static void dodajRuch(String przedRuchem, final Oddzial.Ruch ruch) {
        ruchy.add(ruch);
        System.out.print(ruch.odczytajPoruszonyOddzial().numer + ": " + ruch + " (" + przedRuchem + ")");
        if(ruch.odczytajTypRuchu() != Oddzial.Ruch.TypRuchu.ODPOCZYNEK) {
            System.out.println(" -> (" + ruch.odczytajDocelowePole().wspolrzedne.x + "," + ruch.odczytajDocelowePole().wspolrzedne.y + ")");
        } else System.out.println();

    }

    public int rozmiar() {
        return ruchy.size();
    }


    //TODO Otrzymanie historii przebiegu symulacji i wygenerowanie raportu do pliku .txt

}
