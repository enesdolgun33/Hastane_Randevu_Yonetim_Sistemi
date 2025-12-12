package com.example.hastane_randevu_yonetim_sistemi.controllers;

import com.example.hastane_randevu_yonetim_sistemi.database.VeritabaniBaglantisi;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class KayitController {

    @FXML private TextField adField;
    @FXML private TextField soyadField;
    @FXML private TextField tcField;
    @FXML private TextField telefonField;
    @FXML private TextField emailField;
    @FXML private PasswordField sifreField;
    @FXML private ComboBox<String> cinsiyetCombo;
    @FXML private DatePicker dogumTarihiSecici;
    @FXML private Label sonucLabel;

    @FXML
    public void initialize() {
        cinsiyetCombo.getItems().addAll("Erkek", "Kadın", "Diğer");
    }

    @FXML
    protected void kayitOlButonunaBasildi() {
        String ad = adField.getText();
        String soyad = soyadField.getText();
        String tc = tcField.getText();
        String telefon = telefonField.getText();
        String email = emailField.getText();
        String sifre = sifreField.getText();
        String cinsiyet = cinsiyetCombo.getValue();
        java.time.LocalDate dogumTarihi = dogumTarihiSecici.getValue();

        if (ad.isEmpty() || soyad.isEmpty() || tc.isEmpty() || sifre.isEmpty() ||
                cinsiyet == null || dogumTarihi == null) {
            sonucLabel.setText("Lütfen tüm zorunlu alanları doldurunuz!");
            return;
        }

        veritabaninaKaydet(ad, soyad, tc, sifre, telefon, email, cinsiyet, dogumTarihi.toString());
    }

    private void veritabaninaKaydet(String ad, String soyad, String tc, String sifre,
                                    String tel, String email, String cinsiyet, String dogum) {

        String sql = "INSERT INTO hastalar (ad, soyad, tc_no, sifre, telefon, email, cinsiyet, dogum_tarihi) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection baglanti = VeritabaniBaglantisi.getInstance().getConnection();
            PreparedStatement ps = baglanti.prepareStatement(sql);

            ps.setString(1, ad);
            ps.setString(2, soyad);
            ps.setString(3, tc);
            ps.setString(4, sifre);
            ps.setString(5, tel);
            ps.setString(6, email);
            ps.setString(7, cinsiyet);
            ps.setString(8, dogum);

            if (ps.executeUpdate() > 0) {
                sonucLabel.setText("Kayıt Başarılı! Giriş ekranına dönebilirsiniz.");
                temizle();
                girisEkraninaDon();
            }
        } catch (SQLException e) {
            sonucLabel.setText("Hata: " + e.getMessage());
        }
    }

    private void temizle() {
        adField.clear();
        soyadField.clear();
        tcField.clear();
        telefonField.clear();
        emailField.clear();
        sifreField.clear();
        cinsiyetCombo.setValue(null);
        dogumTarihiSecici.setValue(null);
    }

    @FXML
    protected void girisEkraninaDon() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/hastane_randevu_yonetim_sistemi/hello-view.fxml")
            );
            Scene scene = new Scene(loader.load(), 900, 600);
            Stage stage = (Stage) adField.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
