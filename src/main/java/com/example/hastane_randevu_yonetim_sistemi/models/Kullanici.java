package com.example.hastane_randevu_yonetim_sistemi.models;

public abstract class Kullanici {
    protected int id;
    protected String ad;
    protected String soyad;
    protected String sifre;

    public Kullanici(int id, String ad, String soyad, String sifre) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.sifre = sifre;
    }

    public int getId() {
        return id;
    }
    public String getAd() {
        return ad;
    }
    public String getSoyad() {
        return soyad;
    }
    public String getSifre() {
        return sifre;
    }
}
