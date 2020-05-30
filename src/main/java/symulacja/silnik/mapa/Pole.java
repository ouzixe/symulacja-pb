package symulacja.silnik.mapa;

import symulacja.silnik.obiekty.Obiekt;
import symulacja.silnik.oddzialy.Oddzial;

import java.util.*;

public abstract class Pole {

    public static class Wspolrzedne {
        public int x;
        public int y;
        public Wspolrzedne(int x, int y) {
            this.x = x;
            this.y = y;
        }

        //Tworzy stałą listę współrzędnych - na jej podstawie odnajdywać się będą Oddziały i Obiekty
        public static List<Wspolrzedne> utworzListeWspolrzednych(int szerokosc, int wysokosc) {
            final List<Pole.Wspolrzedne> listaWspolrzednych = new ArrayList<>();
            for(int i = 0; i < wysokosc; i++) {
                for(int j = 0; j < szerokosc; j++) {
                    Pole.Wspolrzedne wspolrzedne = new Pole.Wspolrzedne(j+1, wysokosc-i);
                    listaWspolrzednych.add(i * szerokosc + j, wspolrzedne);
                }
            }
            return listaWspolrzednych;
        }

        public static boolean czyPoprawneWspolrzedne(Pole.Wspolrzedne wspolrzedne, int szerokosc, int wysokosc) {
            return wspolrzedne.x > 0 && wspolrzedne.x < szerokosc - 1 && wspolrzedne.y > 0 && wspolrzedne.y < wysokosc - 1;
        }
    }

    public Wspolrzedne wspolrzedne;

    //Tworzy jedno pole - w zależności od parametrów, Puste lub Zajęte
    public static Pole utworzPole(final Wspolrzedne wspolrzedne, final Obiekt obiekt, final Oddzial oddzial) {
        if (obiekt != null || oddzial != null) return new ZajetePole(wspolrzedne, obiekt, oddzial);
        else return new PustePole(wspolrzedne);
    }

    private Pole(final Wspolrzedne wspolrzedne) {
        this.wspolrzedne = wspolrzedne;
    }
    public abstract boolean czyPoleZajete();
    public abstract Obiekt odczytajObiekt();
    public abstract Oddzial odczytajOddzial();
    public abstract boolean czyGranica();
    public static final class PustePole extends Pole {

        private PustePole(final Wspolrzedne wspolrzedne) {
            super(wspolrzedne);
        }

        @Override
        public String toString() {
            return "-";
        }

        @Override
        public boolean czyPoleZajete() {
            return false;
        }

        @Override
        public Obiekt odczytajObiekt() {
            return null;
        }

        @Override
        public Oddzial odczytajOddzial() {
            return null;
        }

        @Override
        public boolean czyGranica() {
            return false;
        }
    }

    public static final class ZajetePole extends Pole {

        private final Obiekt obiektNaPolu;
        private final Oddzial oddzialNaPolu;
        ZajetePole(final Wspolrzedne wspolrzedne, Obiekt obiektNaPolu, Oddzial oddzialNaPolu) {
            super(wspolrzedne);
            this.obiektNaPolu = obiektNaPolu;
            this.oddzialNaPolu = oddzialNaPolu;
        }

        @Override
        public String toString() {
            if(odczytajObiekt() == null) {
                return odczytajOddzial().toString();
            } else {
                if(odczytajOddzial() == null) {
                    return odczytajObiekt().toString();
                } else return "#";
            }
        }

        @Override
        public boolean czyPoleZajete() {
            return true;
        }

        @Override
        public Obiekt odczytajObiekt() {
            return this.obiektNaPolu;
        }

        @Override
        public Oddzial odczytajOddzial() {
            return this.oddzialNaPolu;
        }

        @Override
        public boolean czyGranica() {
            return this.obiektNaPolu != null && this.obiektNaPolu.odczytajTyp() == Obiekt.Typ.GRANICA;
        }
    }
}