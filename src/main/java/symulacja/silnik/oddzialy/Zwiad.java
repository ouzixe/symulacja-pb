package symulacja.silnik.oddzialy;

import symulacja.Symulacja;
import symulacja.silnik.mapa.Mapa;
import symulacja.silnik.mapa.Pole;
import symulacja.silnik.obiekty.Obiekt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Utworzenie listy pól znajdujących się wokół badanego pola

public abstract class Zwiad {

    final Mapa mapa;
    final Oddzial oddzial;
    static List<Pole> zbadanePola;

    private Zwiad(final Mapa mapa, final Oddzial oddzial) {
        this.mapa = mapa;
        this.oddzial = oddzial;
        zbadanePola = zbadajPola(oddzial.odczytajPole());
    }

    public static List<Pole> zbadajPola(final Pole badanePole) {
        final List<Pole> listaZbadanychPol = new ArrayList<>();
        boolean x, y;
        for(Pole pole : Mapa.listaPol) {
            x = false;
            y = false;
            if(pole.wspolrzedne.x > Symulacja.odczytajSzerokosc() || pole.wspolrzedne.x < 1) continue;
            if(pole.wspolrzedne.y > Symulacja.odczytajWysokosc() || pole.wspolrzedne.y < 1) continue;
            if(pole.wspolrzedne.x == badanePole.wspolrzedne.x - 1) x = true;
            if(pole.wspolrzedne.x == badanePole.wspolrzedne.x) x = true;
            if(pole.wspolrzedne.x == badanePole.wspolrzedne.x + 1) x = true;
            if(pole.wspolrzedne.y == badanePole.wspolrzedne.y - 1) y = true;
            if(pole.wspolrzedne.y == badanePole.wspolrzedne.y) y = true;
            if(pole.wspolrzedne.y == badanePole.wspolrzedne.y + 1) y = true;
            if(pole == badanePole) continue;
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
        if (oddzial.odczytajTyp() == Oddzial.Typ.ZMOTORYZOWANY) return false;
        if (oddzial.odczytajPole().odczytajObiekt() != null && oddzial.odczytajPole().odczytajObiekt().odczytajTyp() == Obiekt.Typ.OBRONA) {
            if (czyObokGranica(listaZbadanychPol)) {
                return !czySilniejszyPrzeciwnik(listaZbadanychPol, oddzial);
            } else return false;
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

    public static boolean czyObokGranica(List<Pole> listaZbadanychPol) {
        for(Pole pole : listaZbadanychPol) {
            if(pole.odczytajObiekt() != null && pole.odczytajObiekt().odczytajTyp() == Obiekt.Typ.GRANICA) return false;
        }
        return true;
    }

    public static boolean czyPotrzebaRuchu(final List<Pole> listaZbadanychPol, final Oddzial oddzial) {
        if(czyOddzialBezpieczny(listaZbadanychPol, oddzial)) {
            return czyObokObiekty(listaZbadanychPol);
        } else return true;
    }
}