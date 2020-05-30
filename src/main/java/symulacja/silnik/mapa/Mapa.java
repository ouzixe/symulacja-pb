package symulacja.silnik.mapa;


import symulacja.Symulacja;
import symulacja.silnik.tura.Dowodca;
import symulacja.silnik.obiekty.*;
import symulacja.silnik.oddzialy.*;

import java.util.*;

public class Mapa {

    public static List<Pole> listaPol;
    public static Dowodca aktualnyDowodca;

    //Trzymanie listę Pól - ich współrzędnych oraz Obiektów i Oddziałów
    //znajdujących się na nich. Będzie też zajmować się ruchami Oddziałów - sprawdzać, czy są
    //możliwe oraz co się na tych polach znajduje

    public Mapa(final List<Pole.Wspolrzedne> listaWspolrzednych,
                final List<Obiekt> listaObiektow,
                final List<Oddzial> listaOddzialow,
                final List<Dowodca> listaDowodcow,
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

    public static Dowodca przypiszDowodce() {
        Dowodca dowodca = Symulacja.listaDowodcow.get(Symulacja.odczytajPowtorzenie() % Symulacja.listaDowodcow.size());
        for(int i = 0; i < Symulacja.listaDowodcow.size(); i++) {
            if (dowodca.odczytajOddzial().zycie != 0) return dowodca;
            else dowodca = Symulacja.listaDowodcow.get(Symulacja.odczytajPowtorzenie() % Symulacja.listaDowodcow.size());
        } return null;
    }

    public static Mapa utworzPodstawowaMape(final List<Pole.Wspolrzedne> listaWspolrzednych,
                                            final List<Obiekt> listaObiektow,
                                            final List<Oddzial> listaOddzialow,
                                            final List<Dowodca> listaDowodcow,
                                            int powtorzenie) {
        return new Mapa(listaWspolrzednych, listaObiektow, listaOddzialow, listaDowodcow, powtorzenie);
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
