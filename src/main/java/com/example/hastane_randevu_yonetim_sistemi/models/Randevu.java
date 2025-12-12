package com.example.hastane_randevu_yonetim_sistemi.models;

public class Randevu {
    private int id;
    private int hastaId;
    private int doktorId;
    private String doktorAdSoyad;
    private String tarih;
    private String saat;
    private String durum;
    private String muayeneNotu;

    public Randevu(int id, int hastaId, int doktorId, String doktorAdSoyad, String tarih, String saat, String durum, String muayeneNotu) {
        this.id = id;
        this.hastaId = hastaId;
        this.doktorId = doktorId;
        this.doktorAdSoyad = doktorAdSoyad;
        this.tarih = tarih;
        this.saat = saat;
        this.durum = durum;
        this.muayeneNotu = muayeneNotu;
    }

    public int getId() {
        return id;
    }

    public String getDoktorAdSoyad() {
        return doktorAdSoyad;
    }

    public String getTarih() {
        return tarih;
    }

    public String getSaat() {
        return saat;
    }

    public String getDurum() {
        return durum;
    }

    public String getMuayeneNotu() {
        return muayeneNotu;
    }

    public void setMuayeneNotu(String muayeneNotu) {
        this.muayeneNotu = muayeneNotu;
    }
}