package symulacja.silnik.oddzialy;

import symulacja.silnik.mapa.Mapa;
import symulacja.silnik.mapa.Pole;
import symulacja.silnik.obiekty.Obiekt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Utworzenie, na podstawie listy zbadanych pól, listy możliwych ruchów

public class Ruch {

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

    protected Ruch(final Mapa mapa,
                 final Oddzial poruszonyOddzial,
                 final Pole docelowePole,
                 final TypRuchu typRuchu) {
        symulacja.silnik.oddzialy.Ruch.mapa = mapa;
        this.poruszonyOddzial = poruszonyOddzial;
        this.docelowePole = docelowePole;
        this.typRuchu = typRuchu;
    }

    public List<symulacja.silnik.oddzialy.Ruch> obliczRuchy(final List<Pole> listaZbadanychPol, final Oddzial poruszonyOddzial) {

        List<symulacja.silnik.oddzialy.Ruch> mozliweRuchy = new ArrayList<>();
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

    static final class Odpoczynek extends symulacja.silnik.oddzialy.Ruch {
        Odpoczynek(Mapa mapa, Oddzial oddzial, Pole docelowePole, TypRuchu typRuchu) {
            super(mapa, oddzial, docelowePole, typRuchu);
        }

        @Override
        public String toString() { return TypRuchu.ODPOCZYNEK.toString(); }
    }

    static final class Przemieszczenie extends symulacja.silnik.oddzialy.Ruch {
        Przemieszczenie(Mapa mapa, Oddzial oddzial, Pole docelowePole, TypRuchu typRuchu) {
            super(mapa, oddzial, docelowePole, typRuchu);
        }

        @Override
        public String toString() { return TypRuchu.PRZEMIESZCZENIE.toString(); }
    }

    static final class Przejecie extends symulacja.silnik.oddzialy.Ruch {

        final Obiekt docelowyObiekt;

        Przejecie(Mapa mapa, Oddzial poruszonyOddzial, Pole docelowePole, TypRuchu typRuchu) {
            super(mapa, poruszonyOddzial, docelowePole, typRuchu);
            this.docelowyObiekt = docelowePole.odczytajObiekt();
        }

        @Override
        public String toString() { return TypRuchu.PRZEJECIE.toString(); }
    }

    static final class Atak extends symulacja.silnik.oddzialy.Ruch {

    final Oddzial zaatakowanyOddzial;

    Atak(Mapa mapa, Oddzial poruszonyOddzial, Pole docelowePole, TypRuchu typRuchu) {
        super(mapa, poruszonyOddzial, docelowePole, typRuchu);
        this.zaatakowanyOddzial = docelowePole.odczytajOddzial();
    }

    @Override
    public String toString() { return TypRuchu.ATAK.toString(); }
    }
}