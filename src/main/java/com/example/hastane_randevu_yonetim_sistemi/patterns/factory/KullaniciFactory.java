package com.example.hastane_randevu_yonetim_sistemi.patterns.factory;

import com.example.hastane_randevu_yonetim_sistemi.models.Doktor;
import com.example.hastane_randevu_yonetim_sistemi.models.Hasta;
import com.example.hastane_randevu_yonetim_sistemi.models.Kullanici;

public class KullaniciFactory {
    public static Kullanici kullaniciOlustur(String tip, int id, String ad, String soyad, String sifre, String ekstra1, String ekstra2) {
        if (tip.equalsIgnoreCase("HASTA")) {
            return new Hasta(id, ad, soyad, ekstra1, sifre, ekstra2, "", "", "");
        } else if (tip.equalsIgnoreCase("DOKTOR")) {
            return new Doktor(id, ad, soyad, ekstra1, sifre, "000", "000", "");
        }
        return null;
    }
}