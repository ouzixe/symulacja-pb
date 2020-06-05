package symulacja.silnik.mapa;


import symulacja.Symulacja;
import symulacja.silnik.tura.Dowodca;
import symulacja.silnik.obiekty.*;
import symulacja.silnik.oddzialy.*;

import java.util.*;

//Tworzenie i trzymanie listy pól.

public class Mapa {

    public static List<Pole> listaPol;
    public static Dowodca aktualnyDowodca;

    public Mapa(final List<Dowodca> listaDowodcow,
                int powtorzenie) {

        listaPol = utworzListePol();
        aktualnyDowodca = listaDowodcow.get(powtorzenie % listaDowodcow.size());
    }

    public Pole odczytajPole(Pole.Wspolrzedne wspolrzedne) {
        for(Pole pole : listaPol) {
            if (pole.wspolrzedne == wspolrzedne) return pole;
        }
        return null;
    }

    private static List<Pole> utworzListePol() {
        final List<Pole> listaPol = new ArrayList<>();
        zewnetrzna:
        for(Pole.Wspolrzedne wspolrzedne : Symulacja.listaWspolrzednych) {
            for(Obiekt obiekt : Symulacja.listaObiektow) {
                if(obiekt.wspolrzedne() == wspolrzedne) {
                    listaPol.add(Pole.utworzPole(wspolrzedne, obiekt, null));
                    continue zewnetrzna;
                }
            }
            for(Oddzial oddzial : Symulacja.listaOddzialow) {
                if(oddzial.wspolrzedne() == wspolrzedne) {
                    listaPol.add(Pole.utworzPole(wspolrzedne, null, oddzial));
                    continue zewnetrzna;
                }
            }
            listaPol.add(Pole.utworzPole(wspolrzedne, null, null));
        }
        if(Symulacja.listaOddzialow.isEmpty()) System.out.println("puste");
        return listaPol;
    }

    public static Mapa utworzPodstawowaMape(final List<Dowodca> listaDowodcow,
                                            int powtorzenie) {
        return new Mapa(listaDowodcow, powtorzenie);
    }

    public String toString(int szerokosc, int wysokosc) {
        final StringBuilder builder = new StringBuilder();
        for(int i = 0; i < szerokosc; i++) {
            for(int j = 0; j < wysokosc; j++) {
                final String poleTekst = listaPol.get(i * wysokosc + j).toString();
                builder.append(String.format("%3s", poleTekst));
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}