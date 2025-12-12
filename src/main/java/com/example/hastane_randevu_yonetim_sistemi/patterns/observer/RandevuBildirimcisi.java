package com.example.hastane_randevu_yonetim_sistemi.patterns.observer;

import java.util.ArrayList;
import java.util.List;

public class RandevuBildirimcisi {
    private List<IGozlemci> gozlemciler = new ArrayList<>();

    public void aboneEkle(IGozlemci gozlemci) {
        gozlemciler.add(gozlemci);
    }


    public void aboneCikar(IGozlemci gozlemci) {
        gozlemciler.remove(gozlemci);
    }

    public void herkeseBildir(String mesaj) {
        for (IGozlemci gozlemci : gozlemciler) {
            gozlemci.bildirimAl(mesaj);
        }
    }
}