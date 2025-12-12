module com.example.hastane_randevu_yonetim_sistemi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.example.hastane_randevu_yonetim_sistemi to javafx.fxml;

    // YENİ EKLEDİĞİMİZ SATIR BU:
    opens com.example.hastane_randevu_yonetim_sistemi.models to javafx.base;
    // Controller dosyaları ayrı klasörde olduğu için onlara da izin veriyoruz:
    opens com.example.hastane_randevu_yonetim_sistemi.controllers to javafx.fxml;


    exports com.example.hastane_randevu_yonetim_sistemi;
    exports com.example.hastane_randevu_yonetim_sistemi.controllers;
}