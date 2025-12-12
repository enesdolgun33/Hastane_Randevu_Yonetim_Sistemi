package com.example.hastane_randevu_yonetim_sistemi.controllers;

import com.example.hastane_randevu_yonetim_sistemi.database.VeritabaniBaglantisi;
import com.example.hastane_randevu_yonetim_sistemi.models.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.sql.*;

public class HelloController {

    @FXML private TextField tcField;
    @FXML private PasswordField sifreField;
    @FXML private ComboBox<String> rolComboBox;
    @FXML private Label hataLabel;

    @FXML
    public void initialize() {
        rolComboBox.getItems().addAll("Hasta", "Doktor");
        rolComboBox.getSelectionModel().select("Hasta");
    }

    @FXML
    protected void girisYapButonunaBasildi() {
        String tcNo = tcField.getText();
        String sifre = sifreField.getText();
        String rol = rolComboBox.getValue();

        if (tcNo.isEmpty() || sifre.isEmpty() || rol == null) {
            hataLabel.setText("Eksik bilgi!");
            return;
        }

        if (girisKontrol(rol, tcNo, sifre)) {
            oturumuBaslat(rol, tcNo);
            try {
                String hedef = rol.equals("Hasta")
                        ? "/com/example/hastane_randevu_yonetim_sistemi/hasta-panel.fxml"
                        : "/com/example/hastane_randevu_yonetim_sistemi/doktor-panel.fxml";

                Scene scene = new Scene(new FXMLLoader(getClass().getResource(hedef)).load(), 1280, 800);
                Stage stage = (Stage) tcField.getScene().getWindow();
                stage.setScene(scene);
                stage.centerOnScreen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            hataLabel.setText("Hatalı bilgi!");
        }
    }

    private boolean girisKontrol(String rol, String tc, String sifre) {
        String sql = rol.equals("Hasta")
                ? "SELECT * FROM hastalar WHERE tc_no=? AND sifre=?"
                : "SELECT * FROM doktorlar WHERE tc_no=? AND sifre=?";

        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tc);
            ps.setString(2, sifre);
            return ps.executeQuery().next();
        } catch (Exception e) {
            return false;
        }
    }

    private void oturumuBaslat(String rol, String tc) {
        String sql = rol.equals("Hasta")
                ? "SELECT * FROM hastalar WHERE tc_no=?"
                : "SELECT * FROM doktorlar WHERE tc_no=?";

        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tc);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Kullanici k;

                if (rol.equals("Hasta")) {
                    k = new Hasta(
                            rs.getInt("id"), rs.getString("ad"), rs.getString("soyad"),
                            rs.getString("tc_no"), rs.getString("sifre"), rs.getString("telefon"),
                            rs.getString("email"), rs.getString("cinsiyet"), rs.getString("dogum_tarihi")
                    );
                } else {
                    k = new Doktor(
                            rs.getInt("id"), rs.getString("ad"), rs.getString("soyad"),
                            rs.getString("brans"), rs.getString("sifre"), rs.getString("tc_no"),
                            rs.getString("telefon"), rs.getString("email")
                    );
                }

                Oturum.getInstance().setAktifKullanici(k);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void kayitOlSayfasinaGit() {
        try {
            Stage stage = (Stage) tcField.getScene().getWindow();
            Scene scene = new Scene(
                    new FXMLLoader(getClass().getResource(
                            "/com/example/hastane_randevu_yonetim_sistemi/kayit-view.fxml"
                    )).load(),
                    900, 600
            );
            stage.setScene(scene);
        } catch (Exception ignore) {}
    }
}
