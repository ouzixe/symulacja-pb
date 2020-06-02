package symulacja;

import symulacja.gui.PasekMenu;

import java.io.*;
import java.util.Properties;

public class PlikKonfiguracyjny {

    //Służy do utworzenia pliku konfiguracyjnego "config.properties",
    //zmian wartości w nim oraz odczytywanie ich.

    static boolean czyIstnieje() {
        File config = new File("zasoby/config.properties");
        return config.exists();
    }

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
