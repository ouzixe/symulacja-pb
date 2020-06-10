package symulacja.silnik.tura;

import symulacja.PlikRaportu;
import symulacja.Symulacja;
import symulacja.gui.Plansza;
import symulacja.silnik.mapa.Pole;
import symulacja.silnik.obiekty.ObiektGranicy;
import symulacja.silnik.oddzialy.Oddzial;
import symulacja.silnik.oddzialy.Ruch;
import symulacja.silnik.oddzialy.Zwiad;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

//"Inteligencja" oddziału, podejmowanie decyzji co do następnego ruchu

/**
 * Podejmowanie decyzji o następnym ruchu {@link Oddzial}.
 */
public class Dowodca {

    /** Dowodzony {@link Oddzial}. */
    protected final Oddzial oddzial;
    /** {@link Ruch} możliwe dla {@link Oddzial} */
    protected final List<Ruch> mozliweRuchy;

    /** Metoda główna dowódcy. */
    public Dowodca(final Oddzial oddzial, final List<Ruch> mozliweRuchy) {
        this.oddzial = oddzial;
        this.mozliweRuchy = mozliweRuchy;
    }

    /**
     * Utworzenie listy {@link Dowodca}.
     * @param listaOddzialow
     * Przypisanie {@link Oddzial} do {@link Dowodca}.
     * @return Lista {@link Dowodca}.
     */
    public static List<Dowodca> utworzListeDowodzcow(final List<Oddzial> listaOddzialow) {
        List<Dowodca> listaDowodzcow = new ArrayList<>();
        for(int i = 0; i < listaOddzialow.size(); i++) {
            listaDowodzcow.add(i, new Dowodca(listaOddzialow.get(i),null));
        }
        return listaDowodzcow;
    }

    /**
     * Przedzielenie {@link PlikRaportu} jeżeli żyje więcej niż jeden {@link Oddzial}.
     */
    public static void czyOstatniOddzial() {
        int licznik = 0;
        for(Oddzial oddzial : Symulacja.listaOddzialow) {
            if(oddzial.zycie > 0) licznik++;
        }
        if(licznik <= 1) Plansza.koniecSymulacji();
        else {
            char c = '=';
            PlikRaportu.oddzielRuchy(c);
        }
    }

    /**
     * Sprawdzenie czy {@link Oddzial} może wykonać ruch.
     * @return <code>true</code> jeżeli może wykonać ruch, w przeciwnym wypadku <code>false</code>.
     */
    public boolean czyOddzialZywy() {
        if(oddzial.odczytajPole() == null) return false;
        if(oddzial.odczytajPole().czyGranica()) return false;
        return this.oddzial.zycie > 0;
    }

    /**
     * Zbadanie pól dookoła oddziału, obliczenie możliwych ruchów oraz wybranie
     * najbardziej korzystnego ruchu w zależności od otoczenia.
     * @return Wybrany przez {@link Dowodca} {@link Ruch}.
     */
    public Ruch decyzja() {

        List<Pole> zbadanePola = Zwiad.zbadajPola(this.oddzial.odczytajPole());
        List<Ruch> mozliweRuchy = this.oddzial.obliczRuchy(zbadanePola, this.oddzial);

        if(zbadanePola.isEmpty() || this.oddzial.sila <= 0 || !Zwiad.czyPotrzebaRuchu(zbadanePola, this.oddzial)) {
            return mozliweRuchy.get(0);
        }
        if(this.oddzial.sila > 1) {
            if(Zwiad.czySilniejszyPrzeciwnik(zbadanePola, this.oddzial) && Zwiad.czyObokObiekty(zbadanePola)) {
                for(Ruch ruch : mozliweRuchy) {
                    if(ruch.odczytajTypRuchu() == Ruch.TypRuchu.PRZEJECIE) return ruch;
                }
            }
            if(Zwiad.czySlabszyPrzeciwnik(zbadanePola, this.oddzial)) {
                for(Ruch ruch : mozliweRuchy) {
                    if(ruch.odczytajTypRuchu() == Ruch.TypRuchu.ATAK) {
                        if(ruch.odczytajDocelowePole().odczytajOddzial().obrona <= this.oddzial.atak) return ruch;
                    }
                }
            }
            if(Zwiad.czyObokObiekty(zbadanePola)) {
                for(Ruch ruch : mozliweRuchy) {
                    if(ruch.odczytajTypRuchu() == Ruch.TypRuchu.PRZEJECIE) return ruch;
                }
            }
        }
        List<Ruch> przemieszczenia = new ArrayList<>();
        for(Ruch ruch : mozliweRuchy) {
            if(ruch.odczytajTypRuchu() == Ruch.TypRuchu.PRZEMIESZCZENIE) przemieszczenia.add(ruch);
        }
        if(przemieszczenia.isEmpty()) return mozliweRuchy.get(0);
        for(Ruch ruch : przemieszczenia) {
            Pole pole = ruch.odczytajDocelowePole();
            List<Pole> wokolDocelowego = Zwiad.zbadajPola(pole);
            if(Zwiad.czyObokGranica(wokolDocelowego)) return ruch;
        }
        int rand = ThreadLocalRandom.current().nextInt(0, przemieszczenia.size());
        return przemieszczenia.get(rand);
    }
}