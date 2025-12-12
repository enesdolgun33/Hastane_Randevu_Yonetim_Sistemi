package com.example.hastane_randevu_yonetim_sistemi.models;

public class Hasta extends Kullanici {
    private String tcNo;
    private String telefon;
    private String email;
    private String cinsiyet;
    private String dogumTarihi;

    public Hasta(int id, String ad, String soyad, String tcNo, String sifre, String telefon, String email, String cinsiyet, String dogumTarihi) {
        super(id, ad, soyad, sifre);
        this.tcNo = tcNo;
        this.telefon = telefon;
        this.email = email;
        this.cinsiyet = cinsiyet;
        this.dogumTarihi = dogumTarihi;
    }

    public String getTcNo() {
        return tcNo;
    }
    public String getTelefon() {
        return telefon;
    }
    public String getEmail() {
        return email;
    }
    public String getCinsiyet() {
        return cinsiyet;
    }
    public String getDogumTarihi() {
        return dogumTarihi;
    }
}