package symulacja.silnik.oddzialy;

import symulacja.Symulacja;
import symulacja.silnik.mapa.Mapa;
import symulacja.silnik.mapa.Pole;
import symulacja.silnik.obiekty.Obiekt;
import symulacja.silnik.obiekty.ObiektGranicy;
import symulacja.silnik.obiekty.ObiektObrony;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Klasa do odczytywania listy {@link Pole} wokół innego {@link Pole}.
 */
public abstract class Zwiad {

    /** {@link Mapa} na której jest przeprowadzane sprawdzanie. */
    final Mapa mapa;
    /** {@link Oddzial} wokół którego jest sprawdzany teren. */
    final Oddzial oddzial;
    /** Lista {@link Pole} które zostały sprawdzone. */
    static List<Pole> zbadanePola;

    /**
     * Metoda główna klasy {@link Zwiad}.
     * @param mapa
     * {@link Mapa}, która jest sprawdzana.
     * @param oddzial
     * {@link Oddzial}, wokół którego teren jest sprawdzany.
     */
    private Zwiad(final Mapa mapa, final Oddzial oddzial) {
        this.mapa = mapa;
        this.oddzial = oddzial;
        zbadanePola = zbadajPola(oddzial.odczytajPole());
    }

    /**
     * Badanie wszystkich {@link Pole} dookoła zadanego {@link Pole}.
     * @param badanePole
     * {@link Pole}, wokół którego jest przeprowadzane sprawdzanie.
     * @return Lista {@link Pole}, które zostały sprawdzone.
     */
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

    /**
     * Metoda do sprawdzania czy jest na zbadanych polach {@link Oddzial}
     * który jest silniejszy niż przesuwany {@link Oddzial}.
     * @param listaZbadanychPol
     * Lista {@link Pole} na których szukany jest {@link Oddzial}.
     * @param oddzial
     * {@link Oddzial} który jest poruszany.
     * @return <code>true</code> jeżeli na zbadanych {@link Pole} jest silniejszy {@link Oddzial},
     * w przeciwnym wypadku <code>false</code>.
     */
    public static boolean czySilniejszyPrzeciwnik(List<Pole> listaZbadanychPol, final Oddzial oddzial) {
        for(Pole pole : listaZbadanychPol) {
            if(pole.odczytajOddzial() != null && pole.odczytajOddzial().atak > oddzial.obrona + 1) return true;
        }
        return false;
    }

    /**
     * Metoda do sprawdzania czy jest na zbadanych polach {@link Oddzial}
     * który jest słabszy niż przesuwany {@link Oddzial}.
     * @param listaZbadanychPol
     * Lista {@link Pole} na których szukany jest {@link Oddzial}.
     * @param oddzial
     * {@link Oddzial} który jest poruszany.
     * @return <code>true</code> jeżeli na zbadanych {@link Pole} jest słabszy {@link Oddzial},
     * w przeciwnym wypadku <code>false</code>.
     */
    public static boolean czySlabszyPrzeciwnik(List<Pole> listaZbadanychPol, final Oddzial oddzial) {
        for(Pole pole : listaZbadanychPol) {
            if(pole.odczytajOddzial() != null && pole.odczytajOddzial().obrona <= oddzial.atak) return true;
        }
        return false;
    }

    /**
     * Metoda do sprawdzania czy przesuwany oddział jest w bezpiecznym miejscu.
     * @param listaZbadanychPol
     * Lista {@link Pole} do sprawdzenia czy obok jest silniejszy {@link Oddzial} lub {@link ObiektGranicy}.
     * @param oddzial
     * {@link Oddzial} który jest przesuwany.
     * @return <code>true</code> jeżeli nie ma silniejszego {@link Oddzial},
     * nie ma obok {@link ObiektGranicy} a {@link Oddzial} jest na {@link ObiektObrony}.
     * W przeciwnym wypadku <code>false</code>.
     */
    public static boolean czyOddzialBezpieczny(final List<Pole> listaZbadanychPol, final Oddzial oddzial) {
        if (oddzial.odczytajTyp() == Oddzial.Typ.ZMOTORYZOWANY) return false;
        if (oddzial.odczytajPole().odczytajObiekt() != null && oddzial.odczytajPole().odczytajObiekt().odczytajTyp() == Obiekt.Typ.OBRONA) {
            if (czyObokGranica(listaZbadanychPol)) {
                return !czySilniejszyPrzeciwnik(listaZbadanychPol, oddzial);
            } else return false;
        } else return false;
    }

    /**
     * Metoda do sprawdzania czy jest na zbadanych polach {@link Obiekt}.
     * @param listaZbadanychPol
     * Lista {@link Pole} na których szukany jest {@link Obiekt}.
     * @return <code>true</code>, jeżeli jest na zbadanych {@link Pole} {@link Obiekt}, inaczej <code>false</code>.
     */
    public static boolean czyObokObiekty(List<Pole> listaZbadanychPol) {
        for(Pole pole : listaZbadanychPol) {
            if(pole.odczytajObiekt() != null && pole.odczytajObiekt().odczytajTyp() != Obiekt.Typ.TEREN) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metoda do sprawdzania czy jest na zbadanych polach {@link ObiektGranicy}.
     * @param listaZbadanychPol
     * Lista {@link Pole} na których szukany jest {@link ObiektGranicy}.
     * @return <code>true</code>, jeżeli jest na zbadanych {@link Pole} {@link ObiektGranicy}, inaczej <code>false</code>.
     */
    public static boolean czyObokGranica(List<Pole> listaZbadanychPol) {
        for(Pole pole : listaZbadanychPol) {
            if(pole.odczytajObiekt() != null && pole.odczytajObiekt().odczytajTyp() == Obiekt.Typ.GRANICA) return false;
        }
        return true;
    }

    /**
     * Metoda do określenia czy {@link Oddzial} musi wykonywać {@link Ruch}.
     * @param listaZbadanychPol
     * Lista {@link Pole} potrzebna innym metodom do sprawdzenia {@link Pole}.
     * @param oddzial
     * {@link Oddzial} który jest przesuwany.
     * @return <code>true</code> jeżeli {@link Oddzial} jest w niebezpieczeństwie lub może przejąć
     * {@link Obiekt}, w przeciwnym wypadku <code>false</code>.
     */
    public static boolean czyPotrzebaRuchu(final List<Pole> listaZbadanychPol, final Oddzial oddzial) {
        if(czyOddzialBezpieczny(listaZbadanychPol, oddzial)) {
            return czyObokObiekty(listaZbadanychPol);
        } else return true;
    }
}