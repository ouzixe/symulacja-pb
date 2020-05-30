package symulacja.silnik.tura;

import symulacja.Symulacja;
import symulacja.gui.Plansza;
import symulacja.silnik.mapa.Mapa;
import symulacja.silnik.mapa.Pole;
import symulacja.silnik.obiekty.Obiekt;
import symulacja.silnik.obiekty.ObiektGranicy;
import symulacja.silnik.oddzialy.Oddzial;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Dowodca {

    protected Oddzial oddzial;
    protected final List<Oddzial.Ruch> mozliweRuchy;

    //Dowódca ma odpowiadać za "inteligencję" oddziałów -
    // podejmuje decyzje co do następnych ruchów
    public Dowodca(final Oddzial oddzial, final List<Oddzial.Ruch> mozliweRuchy) {
        this.oddzial = oddzial;
        this.mozliweRuchy = mozliweRuchy;
    }

    public static List<Dowodca> utworzListeDowodzcow(final List<Oddzial> listaOddzialow) {
        List<Dowodca> listaDowodzcow = new ArrayList<>();
        for(int i = 0; i < listaOddzialow.size(); i++) {
            listaDowodzcow.add(i, new Dowodca(listaOddzialow.get(i),null));
        }
        return listaDowodzcow;
    }

    public static void czyOstatniOddzial() {
        int licznik = 0;
        for(Oddzial oddzial : Symulacja.listaOddzialow) {
            if(oddzial.zycie > 0) licznik++;
        }
        if(licznik <= 1) Plansza.koniecSymulacji();
    }

    public boolean czyOddzialZywy() {
        if(oddzial.odczytajPole() == null) return false;
        if(oddzial.odczytajPole().czyGranica()) return false;
        return this.oddzial.zycie > 0;
    }

    public Oddzial odczytajOddzial() { return this.oddzial; }

    public Oddzial.Ruch decyzja() {

        List<Pole> zbadanePola = Oddzial.Zwiad.zbadajPola(this.oddzial);
        List<Oddzial.Ruch> mozliweRuchy = Oddzial.Ruch.obliczRuchy(zbadanePola, this.oddzial);

        if(zbadanePola.isEmpty() || this.oddzial.sila <= 0 || !Oddzial.Zwiad.czyPotrzebaRuchu(zbadanePola, this.oddzial)) {
            return mozliweRuchy.get(0);
        }
        if(this.oddzial.sila > 1) {
            if(Oddzial.Zwiad.czySilniejszyPrzeciwnik(zbadanePola, this.oddzial) && Oddzial.Zwiad.czyObokObiekty(zbadanePola)) {
                for(Oddzial.Ruch ruch : mozliweRuchy) {
                    if(ruch.odczytajTypRuchu() == Oddzial.Ruch.TypRuchu.PRZEJECIE) return ruch;
                }
            }
            if(Oddzial.Zwiad.czySlabszyPrzeciwnik(zbadanePola, this.oddzial)) {
                for(Oddzial.Ruch ruch : mozliweRuchy) {
                    if(ruch.odczytajTypRuchu() == Oddzial.Ruch.TypRuchu.ATAK) {
                        if(ruch.odczytajDocelowePole().odczytajOddzial().obrona <= this.oddzial.atak) return ruch;
                    }
                }
            }
            if(Oddzial.Zwiad.czyObokObiekty(zbadanePola)) {
                for(Oddzial.Ruch ruch : mozliweRuchy) {
                    if(ruch.odczytajTypRuchu() == Oddzial.Ruch.TypRuchu.PRZEJECIE) return ruch;
                }
            }
        }
        List<Oddzial.Ruch> przemieszczenia = new ArrayList<>();
        for(Oddzial.Ruch ruch : mozliweRuchy) {
            if(ruch.odczytajTypRuchu() == Oddzial.Ruch.TypRuchu.PRZEMIESZCZENIE) przemieszczenia.add(ruch);
        }
        if(przemieszczenia.isEmpty()) return mozliweRuchy.get(0);
        int rand = ThreadLocalRandom.current().nextInt(0, przemieszczenia.size());
        return przemieszczenia.get(rand);
    }
}
