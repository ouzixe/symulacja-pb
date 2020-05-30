package symulacja.silnik.oddzialy;

import symulacja.Symulacja;
import symulacja.silnik.mapa.Mapa;
import symulacja.silnik.mapa.Pole;
import symulacja.silnik.obiekty.Obiekt;

import java.net.SocketOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

//Odpowiada za utworzenie listy Oddziałów, zachowań Oddziałów (Zwiad i Ruch) oraz
//warunków które pomagają Dowódcy podjąć decyzję


public abstract class Oddzial {

    private static final int STALA_ZYCIE = 10;
    private static final int STALA_ATAK = 10;
    private static final int STALA_OBRONA = 10;
    private static final int STALA_SILA = 5;

    public int zycie;
    public int atak;
    public int obrona;
    public int sila;

    public int resztaAtak;
    public int resztaObrona;

    public int numer;
    protected final Typ typOddzialu;
    protected final Pole.Wspolrzedne wspolrzedne;

    Oddzial(final int numer, final Pole.Wspolrzedne wspolrzedne, final Typ typOddzialu) {
        this.zycie = STALA_ZYCIE;
        this.atak = STALA_ATAK;
        this.obrona = STALA_OBRONA;
        this.sila = STALA_SILA;
        this.resztaAtak = 0;
        this.resztaObrona = 0;
        this.numer = numer;
        this.wspolrzedne = wspolrzedne;
        this.typOddzialu = typOddzialu;
    }

    public Pole.Wspolrzedne wspolrzedne() {
        return this.wspolrzedne;
    }

    enum Typ {

        PIECHOTA("P"),
        ZMOTORYZOWANY("Z");

        private final String oddzialTyp;

        Typ(String oddzialTyp) {
            this.oddzialTyp = oddzialTyp;
        }
        @Override
        public String toString() {
            return this.oddzialTyp;
        }

    }

    public static List<Oddzial> utworzListeOddzialow() {
        List<Oddzial> listaOddzialow = new ArrayList<>();

        int[] indeksy = new int[Symulacja.odczytajLiczbeOddzialow()];
        zewnetrzna:
        for(int i = 0; i < Symulacja.odczytajLiczbeOddzialow(); i++) {
            indeksy[i] = ThreadLocalRandom.current().nextInt(0, Symulacja.listaWspolrzednych.size() - 1);
            for(Obiekt obiekt : Symulacja.listaObiektow) {
                if(Symulacja.listaWspolrzednych.get(indeksy[i]) == obiekt.wspolrzedne()) {
                    i--;
                    continue zewnetrzna;
                }
            }
            for(Oddzial oddzial : listaOddzialow) {
                if(Symulacja.listaWspolrzednych.get(indeksy[i]) == oddzial.wspolrzedne) {
                    i--;
                    continue zewnetrzna;
                }
            }
            listaOddzialow.add(new OddzialPiechoty(i+1, Symulacja.listaWspolrzednych.get(indeksy[i]), Typ.PIECHOTA));
        }
        return listaOddzialow;
    }

    public Pole odczytajPole() {
        for(Pole pole : Mapa.listaPol) {
            if(pole.odczytajOddzial() == this) return pole;
        }
        return null;
    }

    public static abstract class Zwiad {

        final Mapa mapa;
        final Oddzial oddzial;
        static List<Pole> zbadanePola;

        private Zwiad(final Mapa mapa, final Oddzial oddzial) {
            this.mapa = mapa;
            this.oddzial = oddzial;
            zbadanePola = zbadajPola(oddzial);
        }

        public static List<Pole> zbadajPola(final Oddzial oddzial) {
            final List<Pole> listaZbadanychPol = new ArrayList<>();
            boolean x, y;
            for(Pole pole : Mapa.listaPol) {
                x = false;
                y = false;
                if(pole.wspolrzedne.x > Symulacja.odczytajSzerokosc() || pole.wspolrzedne.x < 1) continue;
                if(pole.wspolrzedne.y > Symulacja.odczytajWysokosc() || pole.wspolrzedne.y < 1) continue;
                if(pole.wspolrzedne.x == oddzial.odczytajPole().wspolrzedne.x - 1) x = true;
                if(pole.wspolrzedne.x == oddzial.odczytajPole().wspolrzedne.x) x = true;
                if(pole.wspolrzedne.x == oddzial.odczytajPole().wspolrzedne.x + 1) x = true;
                if(pole.wspolrzedne.y == oddzial.odczytajPole().wspolrzedne.y - 1) y = true;
                if(pole.wspolrzedne.y == oddzial.odczytajPole().wspolrzedne.y) y = true;
                if(pole.wspolrzedne.y == oddzial.odczytajPole().wspolrzedne.y + 1) y = true;
                if(pole == oddzial.odczytajPole()) continue;
                if(x && y) listaZbadanychPol.add(pole);
            }
            return Collections.unmodifiableList(listaZbadanychPol);
        }

        public static boolean czySilniejszyPrzeciwnik(List<Pole> listaZbadanychPol, final Oddzial oddzial) {
            for(Pole pole : listaZbadanychPol) {
                if(pole.odczytajOddzial() != null && pole.odczytajOddzial().atak > oddzial.obrona + 1) return true;
            }
            return false;
        }

        public static boolean czySlabszyPrzeciwnik(List<Pole> listaZbadanychPol, final Oddzial oddzial) {
            for(Pole pole : listaZbadanychPol) {
                if(pole.odczytajOddzial() != null && pole.odczytajOddzial().obrona <= oddzial.atak) return true;
            }
            return false;
        }

        public static boolean czyOddzialBezpieczny(final List<Pole> listaZbadanychPol, final Oddzial oddzial) {
            if (oddzial.odczytajPole().odczytajObiekt() != null && oddzial.odczytajPole().odczytajObiekt().odczytajTyp() == Obiekt.Typ.OBRONA) {
                return !czySilniejszyPrzeciwnik(listaZbadanychPol, oddzial);
            } else return false;
        }

        public static boolean czyObokObiekty(List<Pole> listaZbadanychPol) {
            for(Pole pole : listaZbadanychPol) {
                if(pole.odczytajObiekt() != null && pole.odczytajObiekt().odczytajTyp() != Obiekt.Typ.TEREN) {
                    return true;
                }
            }
            return false;
        }

        public static boolean czyPotrzebaRuchu(final List<Pole> listaZbadanychPol, final Oddzial oddzial) {
            if(czyOddzialBezpieczny(listaZbadanychPol, oddzial)) {
                return czyObokObiekty(listaZbadanychPol);
            } else return true;
        }
    }

    public static class Ruch {

        static Mapa mapa;
        final Oddzial poruszonyOddzial;
        final Pole docelowePole;
        final TypRuchu typRuchu;

        public enum TypRuchu {
            ODPOCZYNEK("ODPOCZYNEK"),
            PRZEMIESZCZENIE("PRZEMIESZCZENIE"),
            PRZEJECIE("PRZEJECIE"),
            ATAK("ATAK");

            private final String typRuchu;

            TypRuchu(String typRuchu) { this.typRuchu = typRuchu; }

            @Override
            public String toString() { return this.typRuchu; }

        }

        private Ruch(final Mapa mapa,
                     final Oddzial poruszonyOddzial,
                     final Pole docelowePole,
                     final TypRuchu typRuchu) {
            Ruch.mapa = mapa;
            this.poruszonyOddzial = poruszonyOddzial;
            this.docelowePole = docelowePole;
            this.typRuchu = typRuchu;
        }

        public static List<Ruch> obliczRuchy(final List<Pole> listaZbadanychPol, final Oddzial poruszonyOddzial) {

            List<Ruch> mozliweRuchy = new ArrayList<>();
            mozliweRuchy.add(new Odpoczynek(mapa, poruszonyOddzial, poruszonyOddzial.odczytajPole(), TypRuchu.ODPOCZYNEK));
            for (Pole pole : listaZbadanychPol) {

                if (pole.odczytajOddzial() != null) {
                    mozliweRuchy.add(new Atak(mapa, poruszonyOddzial, pole, TypRuchu.ATAK));
                    continue;
                }
                if (pole.odczytajObiekt() != null) {
                    if (pole.odczytajObiekt().odczytajTyp() != Obiekt.Typ.TEREN && pole.odczytajObiekt().odczytajTyp() != Obiekt.Typ.GRANICA) {
                        mozliweRuchy.add(new Przejecie(mapa, poruszonyOddzial, pole, TypRuchu.PRZEJECIE));
                    }
                    continue;
                }
                mozliweRuchy.add(new Przemieszczenie(mapa, poruszonyOddzial, pole, TypRuchu.PRZEMIESZCZENIE));
            }
            return Collections.unmodifiableList(mozliweRuchy);
        }

        public TypRuchu odczytajTypRuchu() { return typRuchu; }
        public Oddzial odczytajPoruszonyOddzial() { return poruszonyOddzial; }
        public Pole odczytajDocelowePole() { return docelowePole; }

        private static final class Odpoczynek extends Ruch {
            Odpoczynek(Mapa mapa, Oddzial oddzial, Pole docelowePole, TypRuchu typRuchu) {
                super(mapa, oddzial, docelowePole, typRuchu);
            }

            @Override
            public String toString() { return TypRuchu.ODPOCZYNEK.toString(); }
        }

        private static final class Przemieszczenie extends Ruch {
            Przemieszczenie(Mapa mapa, Oddzial oddzial, Pole docelowePole, TypRuchu typRuchu) {
                super(mapa, oddzial, docelowePole, typRuchu);
            }

            @Override
            public String toString() { return TypRuchu.PRZEMIESZCZENIE.toString(); }
        }

        private static final class Przejecie extends Ruch {

            Obiekt docelowyObiekt;

            Przejecie(Mapa mapa, Oddzial poruszonyOddzial, Pole docelowePole, TypRuchu typRuchu) {
                super(mapa, poruszonyOddzial, docelowePole, typRuchu);
                this.docelowyObiekt = docelowePole.odczytajObiekt();
            }

            @Override
            public String toString() { return TypRuchu.PRZEJECIE.toString(); }
        }

        private static final class Atak extends Ruch {

            Oddzial zaatakowanyOddzial;

            Atak(Mapa mapa, Oddzial poruszonyOddzial, Pole docelowePole, TypRuchu typRuchu) {
                super(mapa, poruszonyOddzial, docelowePole, typRuchu);
                this.zaatakowanyOddzial = docelowePole.odczytajOddzial();
            }

            @Override
            public String toString() { return TypRuchu.ATAK.toString(); }
        }
    }

    public static void przeliczStatystyki(Oddzial oddzial) {
        oddzial.atak = STALA_ATAK * oddzial.zycie / 10 + oddzial.resztaAtak;
        oddzial.obrona = STALA_OBRONA * oddzial.zycie / 10 + oddzial.resztaObrona;
    }
}
