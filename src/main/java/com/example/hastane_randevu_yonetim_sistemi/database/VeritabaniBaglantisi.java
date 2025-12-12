package com.example.hastane_randevu_yonetim_sistemi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class VeritabaniBaglantisi {

    private static VeritabaniBaglantisi instance = null;
    private Connection connection;

    private VeritabaniBaglantisi() {
        try {
            String url = "jdbc:sqlite:hastane.db";
            connection = DriverManager.getConnection(url);
            tablolariOlustur();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static VeritabaniBaglantisi getInstance() {
        if (instance == null) instance = new VeritabaniBaglantisi();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void tablolariOlustur() {
        try {
            Statement stmt = connection.createStatement();

            stmt.execute("CREATE TABLE IF NOT EXISTS hastalar (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ad TEXT, soyad TEXT, tc_no TEXT, sifre TEXT, telefon TEXT, " +
                    "email TEXT, cinsiyet TEXT, dogum_tarihi TEXT)");

            stmt.execute("CREATE TABLE IF NOT EXISTS doktorlar (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ad TEXT, soyad TEXT, brans TEXT, sifre TEXT, tc_no TEXT, telefon TEXT, email TEXT)");

            stmt.execute("CREATE TABLE IF NOT EXISTS randevular (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "hasta_id INTEGER, doktor_id INTEGER, tarih TEXT, saat TEXT, durum TEXT, muayene_notu TEXT)");

            stmt.execute("CREATE TABLE IF NOT EXISTS doktor_mesai (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, doktor_id INTEGER, gun_id INTEGER, baslangic TEXT, bitis TEXT, aktif INTEGER)");

            try { stmt.execute("ALTER TABLE hastalar ADD COLUMN email TEXT"); } catch (SQLException ignored) {}
            try { stmt.execute("ALTER TABLE hastalar ADD COLUMN cinsiyet TEXT"); } catch (SQLException ignored) {}
            try { stmt.execute("ALTER TABLE hastalar ADD COLUMN dogum_tarihi TEXT"); } catch (SQLException ignored) {}
            try { stmt.execute("ALTER TABLE randevular ADD COLUMN muayene_notu TEXT"); } catch (SQLException ignored) {}
            try { stmt.execute("ALTER TABLE doktorlar ADD COLUMN tc_no TEXT"); } catch (SQLException ignored) {}
            try { stmt.execute("ALTER TABLE doktorlar ADD COLUMN telefon TEXT"); } catch (SQLException ignored) {}
            try { stmt.execute("ALTER TABLE doktorlar ADD COLUMN email TEXT"); } catch (SQLException ignored) {}

            var rs = stmt.executeQuery("SELECT count(*) as sayi FROM doktorlar");
            if (rs.next() && rs.getInt("sayi") == 0) {
                stmt.execute("INSERT INTO doktorlar (ad, soyad, brans, sifre, tc_no, telefon, email) " +
                        "VALUES ('Ahmet', 'Yılmaz', 'Kardiyoloji', '123', '11111111111', '05554443322', 'ahmet@hastane.com')");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
