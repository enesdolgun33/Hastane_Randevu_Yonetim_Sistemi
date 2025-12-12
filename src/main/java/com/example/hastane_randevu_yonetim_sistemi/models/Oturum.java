package com.example.hastane_randevu_yonetim_sistemi.models;

public class Oturum {
    private static Oturum instance;
    private Kullanici aktifKullanici;

    private Oturum() {}

    public static Oturum getInstance() {
        if (instance == null) {
            instance = new Oturum();
        }
        return instance;
    }

    public Kullanici getAktifKullanici() {
        return aktifKullanici;
    }

    public void setAktifKullanici(Kullanici kullanici) {
        this.aktifKullanici = kullanici;
    }
}