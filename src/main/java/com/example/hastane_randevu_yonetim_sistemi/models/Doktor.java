package com.example.hastane_randevu_yonetim_sistemi.models;

import com.example.hastane_randevu_yonetim_sistemi.patterns.observer.IGozlemci;

public class Doktor extends Kullanici implements IGozlemci {
    private String brans;
    private String tcNo;
    private String telefon;
    private String email;

    public Doktor(int id, String ad, String soyad, String brans, String sifre, String tcNo, String telefon, String email) {
        super(id, ad, soyad, sifre);
        this.brans = brans;
        this.tcNo = tcNo;
        this.telefon = telefon;
        this.email = email;
    }

    public String getBrans() {
        return brans;
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

    @Override
    public void bildirimAl(String mesaj) {
        System.out.println("🔔 Dr. " + this.ad + " " + this.soyad + " -> " + mesaj);
    }
}