package symulacja.silnik.tura;

import symulacja.PlikRaportu;
import symulacja.Symulacja;
import symulacja.silnik.mapa.Mapa;
import symulacja.silnik.mapa.Pole;
import symulacja.silnik.obiekty.*;
import symulacja.silnik.oddzialy.Oddzial;
import symulacja.silnik.oddzialy.Ruch;

import java.util.ArrayList;
import java.util.List;

//Przeprowadzenie operacji ruchu - zmiana listy pól i statystyk oddziałów

public class WykonanieRuchu {

    private static final List<Pole> listaPolGranicy = new ArrayList<>();

    public static void wykonanieRuchu() {
        for(Dowodca dowodca : Symulacja.listaDowodcow) {
            Mapa.aktualnyDowodca = dowodca;
            if(!dowodca.czyOddzialZywy()) continue;
            String przedRuchem = dowodca.oddzial.odczytajPole().wspolrzedne.x + "," + dowodca.oddzial.odczytajPole().wspolrzedne.y;
            final Ruch ruch = dowodca.decyzja();
            PlikRaportu.dodajRuch(przedRuchem, ruch);
            zmienMapePoRuchu(ruch);
        }
        Symulacja.dodajPowtorzenie();
        otoczMapeGranica();
        Dowodca.czyOstatniOddzial();
    }

    private static void zmienMapePoRuchu(final Ruch ruch) {

        Pole poczatkowePole = ruch.odczytajPoruszonyOddzial().odczytajPole();
        Pole docelowePole = ruch.odczytajDocelowePole();

        List<Pole> nowaListaPol = new ArrayList<>();

        if(ruch.odczytajTypRuchu() == Ruch.TypRuchu.ODPOCZYNEK) {
            if(poczatkowePole.odczytajOddzial().sila == 4) poczatkowePole.odczytajOddzial().sila = poczatkowePole.odczytajOddzial().sila + 1;
            if(poczatkowePole.odczytajOddzial().sila >= 0 && poczatkowePole.odczytajOddzial().sila <= 3) {
                poczatkowePole.odczytajOddzial().sila = poczatkowePole.odczytajOddzial().sila + 2;
            }

            poczatkowePole = Pole.utworzPole(ruch.odczytajPoruszonyOddzial().odczytajPole().wspolrzedne, ruch.odczytajPoruszonyOddzial().odczytajPole().odczytajObiekt(), ruch.odczytajPoruszonyOddzial());
            docelowePole = poczatkowePole;
        }
        if(ruch.odczytajTypRuchu() == Ruch.TypRuchu.PRZEMIESZCZENIE) {
            if(ruch.odczytajPoruszonyOddzial().odczytajTyp() == Oddzial.Typ.PIECHOTA &&
                    ruch.odczytajPoruszonyOddzial().odczytajPole().odczytajObiekt() != null &&
                    ruch.odczytajPoruszonyOddzial().odczytajPole().odczytajObiekt().odczytajTyp() == Obiekt.Typ.OBRONA) {
                ObiektObrony.zejscie(ruch.odczytajPoruszonyOddzial());
            }
            ruch.odczytajPoruszonyOddzial().sila = ruch.odczytajPoruszonyOddzial().sila - 1;

            poczatkowePole = Pole.utworzPole(ruch.odczytajPoruszonyOddzial().odczytajPole().wspolrzedne, ruch.odczytajPoruszonyOddzial().odczytajPole().odczytajObiekt(), null);
            docelowePole = Pole.utworzPole(ruch.odczytajDocelowePole().wspolrzedne, ruch.odczytajDocelowePole().odczytajObiekt(), ruch.odczytajPoruszonyOddzial());
        }
        if(ruch.odczytajTypRuchu() == Ruch.TypRuchu.PRZEJECIE) {
            if(ruch.odczytajPoruszonyOddzial().odczytajTyp() == Oddzial.Typ.PIECHOTA &&
                    ruch.odczytajPoruszonyOddzial().odczytajPole().odczytajObiekt() != null &&
                    ruch.odczytajPoruszonyOddzial().odczytajPole().odczytajObiekt().odczytajTyp() == Obiekt.Typ.OBRONA) {
                ObiektObrony.zejscie(ruch.odczytajPoruszonyOddzial());
            }
            if(ruch.odczytajDocelowePole().odczytajObiekt().odczytajTyp() == Obiekt.Typ.ATAK) ObiektAtaku.przejecie(ruch.odczytajPoruszonyOddzial());
            if(ruch.odczytajDocelowePole().odczytajObiekt().odczytajTyp() == Obiekt.Typ.OBRONA) ObiektObrony.przejecie(ruch.odczytajPoruszonyOddzial());
            if(ruch.odczytajDocelowePole().odczytajObiekt().odczytajTyp() == Obiekt.Typ.WYPOSAZENIE) ObiektWyposazenia.przejecie(ruch.odczytajPoruszonyOddzial());

            ruch.odczytajPoruszonyOddzial().sila = ruch.odczytajPoruszonyOddzial().sila - 2;

            poczatkowePole.odczytajOddzial().przeliczStatystyki(poczatkowePole.odczytajOddzial());
            poczatkowePole = Pole.utworzPole(ruch.odczytajPoruszonyOddzial().odczytajPole().wspolrzedne, ruch.odczytajPoruszonyOddzial().odczytajPole().odczytajObiekt(), null);

            if(ruch.odczytajDocelowePole().odczytajObiekt().odczytajTyp() == Obiekt.Typ.OBRONA) {
                docelowePole = Pole.utworzPole(ruch.odczytajDocelowePole().wspolrzedne, ruch.odczytajDocelowePole().odczytajObiekt(), ruch.odczytajPoruszonyOddzial());
            } else {
                docelowePole = Pole.utworzPole(ruch.odczytajDocelowePole().wspolrzedne, null, ruch.odczytajPoruszonyOddzial());
            }
        }
        if(ruch.odczytajTypRuchu() == Ruch.TypRuchu.ATAK) {
            int docelowyAtak = docelowePole.odczytajOddzial().atak;
            int docelowyObrona = docelowePole.odczytajOddzial().obrona;
            int poczatkowyAtak = poczatkowePole.odczytajOddzial().atak;
            int poczatkowyObrona = poczatkowePole.odczytajOddzial().obrona;

            docelowePole.odczytajOddzial().zycie = docelowePole.odczytajOddzial().zycie - ((poczatkowyAtak * poczatkowyAtak) / (poczatkowyAtak + docelowyObrona));
            poczatkowePole.odczytajOddzial().zycie = poczatkowePole.odczytajOddzial().zycie - ((docelowyAtak * docelowyAtak) / (docelowyAtak + poczatkowyObrona));

            ruch.odczytajPoruszonyOddzial().sila = ruch.odczytajPoruszonyOddzial().sila - 2;
            ruch.odczytajDocelowePole().odczytajOddzial().sila = ruch.odczytajDocelowePole().odczytajOddzial().sila - 1;

            docelowePole.odczytajOddzial().przeliczStatystyki(docelowePole.odczytajOddzial());
            poczatkowePole.odczytajOddzial().przeliczStatystyki(poczatkowePole.odczytajOddzial());

            if(poczatkowePole.odczytajOddzial().zycie <= 0) {
                poczatkowePole.odczytajOddzial().wyzerujStatystyki();
                PlikRaportu.oddzialPolegl(poczatkowePole.odczytajOddzial());
                poczatkowePole = Pole.utworzPole(poczatkowePole.wspolrzedne, poczatkowePole.odczytajObiekt(), null);
            } else poczatkowePole = Pole.utworzPole(poczatkowePole.wspolrzedne, poczatkowePole.odczytajObiekt(), poczatkowePole.odczytajOddzial());

            if(docelowePole.odczytajOddzial().zycie <= 0) {
                docelowePole.odczytajOddzial().wyzerujStatystyki();
                PlikRaportu.oddzialPolegl(docelowePole.odczytajOddzial());
                docelowePole = Pole.utworzPole(docelowePole.wspolrzedne, docelowePole.odczytajObiekt(), null);
            } else docelowePole = Pole.utworzPole(docelowePole.wspolrzedne, docelowePole.odczytajObiekt(), docelowePole.odczytajOddzial());
        }

        for(Pole pole : Mapa.listaPol) {
            if(pole.wspolrzedne == poczatkowePole.wspolrzedne) {
                nowaListaPol.add(poczatkowePole);
                continue;
            }
            if(pole.wspolrzedne == docelowePole.wspolrzedne) {
                nowaListaPol.add(docelowePole);
                continue;
            }
            nowaListaPol.add(pole);
        }
        Mapa.listaPol = nowaListaPol;
    }

    private static void otoczMapeGranica() {

        if(Symulacja.odczytajPowtorzenie() % 5 == 0) {
            List<Pole> nowaListaPol = new ArrayList<>();
            boolean warunek;
            int odleglosc = Symulacja.odczytajPowtorzenie() / 5;

            for(Pole pole : Mapa.listaPol) {
                warunek = false;
                if((pole.wspolrzedne.x == odleglosc || pole.wspolrzedne.x == Symulacja.odczytajSzerokosc() - odleglosc + 1) && pole.wspolrzedne.y != odleglosc) {
                    if(pole.odczytajOddzial() != null) {
                        pole.odczytajOddzial().wyzerujStatystyki();
                        PlikRaportu.oddzialPolegl(pole.odczytajOddzial());
                    }
                    warunek = true;
                } else {
                    if((pole.wspolrzedne.y == odleglosc || pole.wspolrzedne.y == Symulacja.odczytajWysokosc() - odleglosc + 1) && pole.wspolrzedne.x != odleglosc) {
                        if(pole.odczytajOddzial() != null) {
                            pole.odczytajOddzial().wyzerujStatystyki();
                            PlikRaportu.oddzialPolegl(pole.odczytajOddzial());
                        }
                        warunek = true;
                    } else {
                        if(pole.wspolrzedne.x == odleglosc) {
                            if(pole.odczytajOddzial() != null) {
                                pole.odczytajOddzial().wyzerujStatystyki();
                                PlikRaportu.oddzialPolegl(pole.odczytajOddzial());
                            }
                            warunek = true;
                        }
                    }
                }

                if(warunek) {
                    for (ObiektGranicy obiektGranicy : Symulacja.listaObiektowGranicy) {
                        if (obiektGranicy.wspolrzedne() == pole.wspolrzedne) {
                            listaPolGranicy.add(Pole.utworzPole(pole.wspolrzedne, obiektGranicy, null));
                        }
                    }
                }
            }
            zewnetrzna:
            for(Pole pole : Mapa.listaPol) {
                for(Pole poleGranicy : listaPolGranicy) {
                    if(poleGranicy.wspolrzedne == pole.wspolrzedne) {
                        nowaListaPol.add(poleGranicy);
                        continue zewnetrzna;
                    }
                }
                nowaListaPol.add(pole);
            }
            Mapa.listaPol = nowaListaPol;
        }
    }
}