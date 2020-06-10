package symulacja;

import symulacja.gui.PasekMenu;

import java.io.*;
import java.util.Properties;

/**
 * Klasa do tworzenia, edytowania oraz odczytywania pliku konfiguracyjnego.
 */
public class PlikKonfiguracyjny {

    /**
     * Sprawdzenie czy plik konfiguracyjny istnieje.
     * @return <code>true</code> jeżeli istnieje, <code>false</code> jeżeli nie istnieje.
     */
    static boolean czyIstnieje() {
        File config = new File("zasoby/config.properties");
        return config.exists();
    }

    /**
     * Stworzenie pliku konfiguracyjnego z podstawowymi, domyślnymi wartościami.
     */
    static void stworzKonfiguracje() {
        try (OutputStream wyjscie = new FileOutputStream("zasoby/config.properties")) {

            Properties config = new Properties();

            config.setProperty("szerokosc", "15");
            config.setProperty("wysokosc", "15");
            config.setProperty("zageszczenie", "3");
            config.setProperty("liczba-oddzialow", "5");

            config.store(wyjscie, null);


        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Odczytanie wartości z pliku konfiguracyjnego.
     * @param wartosc
     * Wartość, która ma być odczytana z pliku konfiguracyjnego.
     * @return Wartość odczytana z pliku konfiguracyjnego.
     */
    public static int odczytajWartosc(String wartosc) {
        FileInputStream wejscie;
        Properties config;
        int wynik = 0;
        try {
            wejscie = new FileInputStream("zasoby/config.properties");
            config = new Properties();
            config.load(wejscie);
            wynik = Integer.parseInt(config.getProperty(wartosc));
            wejscie.close();
            return wynik;
        } catch (IOException io) {
            io.printStackTrace();
        }
        return wynik;
    }

    /**
     * Odczytanie wartości z pliku konfiguracyjnego.
     * @param wartosc
     * Wartość, która ma być zmieniona w pliku konfiguracyjnym.
     * @param wynik
     * Wartość, na którą ma być zamieniona wartość obecna w pliku konfiguracyjnym.
     */
    public static void zmienWartosc(String wartosc, String wynik) {
        FileInputStream wejscie;
        FileOutputStream wyjscie;
        Properties config = new Properties();
        try {
            wejscie = new FileInputStream("zasoby/config.properties");
            config.load(wejscie);
            wejscie.close();

            wyjscie = new FileOutputStream("zasoby/config.properties");
            int buf = Integer.parseInt(wynik);
            if(wartosc.equals("zageszczenie")) {
                if (buf > 0 && buf < 6) config.setProperty(wartosc, wynik);
                else PasekMenu.oknoBledu(wartosc);
            }
            if(wartosc.equals("szerokosc")) {
                if (buf > 4 && buf < 26) config.setProperty(wartosc, wynik);
                else PasekMenu.oknoBledu(wartosc);
            }
            if(wartosc.equals("wysokosc")) {
                if(buf > 4 && buf < 26) config.setProperty(wartosc, wynik);
                else PasekMenu.oknoBledu(wartosc);
            }
            if(wartosc.equals("liczba-oddzialow")) {
                if(buf > 1 && buf < 16) config.setProperty(wartosc, wynik);
                else PasekMenu.oknoBledu(wartosc);
            }
            config.store(wyjscie, null);
            wyjscie.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}