package symulacja.silnik.mapa;


import symulacja.Symulacja;
import symulacja.silnik.tura.Dowodca;
import symulacja.silnik.obiekty.*;
import symulacja.silnik.oddzialy.*;

import java.util.*;

/** Klasa do tworzenia i trzymania listy pól. */

public class Mapa {
    /** Lista aktualnych pól. */
    public static List<Pole> listaPol;
    /** Dowódca wykonujący aktualny ruch na mapie. */
    public static Dowodca aktualnyDowodca;

    /**
     * Utworzenie nowej mapy.
     *
     * @param listaDowodcow
     * Lista z dowódcami.
     * @param powtorzenie
     * Numer powtórzenia, potrzebny do wyboru dowódcy.
     */
    public Mapa(final List<Dowodca> listaDowodcow,
                int powtorzenie) {
        listaPol = utworzListePol();
        aktualnyDowodca = listaDowodcow.get(powtorzenie % listaDowodcow.size());
    }

    /**
     * Metoda zwraca {@link Pole} z {@link Mapa#listaPol} na podstawie współrzędnych.
     * Zwraca <code>null</code> jeżeli nie w liście pola z podanymi współrzędnymi.
     *
     * @param wspolrzedne
     * {@link Pole.Wspolrzedne} na podstawie których jest szukane {@link Pole}.
     * @return {@link Pole}, jeżeli istnieje w {@link Mapa#listaPol} dla {@link Pole.Wspolrzedne},
     * <code>null</code> jeżeli nie istnieje.
     */
    public Pole odczytajPole(Pole.Wspolrzedne wspolrzedne) {
        for(Pole pole : listaPol) {
            if (pole.wspolrzedne == wspolrzedne) return pole;
        }
        return null;
    }

    /**
     * Metoda tworzy {@link Mapa#listaPol}, pobierając dane o współrzędnych,
     * obiektach i oddziałach z list w {@link Symulacja}.
     * @return {@link Mapa#listaPol}.
     */
    private static List<Pole> utworzListePol() {
        final List<Pole> listaPol = new ArrayList<>();
        zewnetrzna:
        for(Pole.Wspolrzedne wspolrzedne : Symulacja.listaWspolrzednych) {
            for(Obiekt obiekt : Symulacja.listaObiektow) {
                if(obiekt.wspolrzedne() == wspolrzedne) {
                    listaPol.add(new Pole(wspolrzedne, obiekt, null));
                    continue zewnetrzna;
                }
            }
            for(Oddzial oddzial : Symulacja.listaOddzialow) {
                if(oddzial.wspolrzedne() == wspolrzedne) {
                    listaPol.add(new Pole(wspolrzedne, null, oddzial));
                    continue zewnetrzna;
                }
            }
            listaPol.add(new Pole(wspolrzedne, null, null));
        }
        if(Symulacja.listaOddzialow.isEmpty()) System.out.println("puste");
        return listaPol;
    }
}