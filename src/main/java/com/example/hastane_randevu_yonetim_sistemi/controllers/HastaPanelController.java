package com.example.hastane_randevu_yonetim_sistemi.controllers;

import com.example.hastane_randevu_yonetim_sistemi.database.VeritabaniBaglantisi;
import com.example.hastane_randevu_yonetim_sistemi.models.Oturum;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class HastaPanelController {

    @FXML private ComboBox<String> poliklinikCombo;
    @FXML private ComboBox<String> doktorCombo;
    @FXML private ComboBox<String> saatCombo;
    @FXML private DatePicker tarihSecici;
    @FXML private Label durumLabel;
    @FXML private TableView<com.example.hastane_randevu_yonetim_sistemi.models.Randevu> randevuTablosu;
    @FXML private TableColumn<com.example.hastane_randevu_yonetim_sistemi.models.Randevu, String> colDoktor;
    @FXML private TableColumn<com.example.hastane_randevu_yonetim_sistemi.models.Randevu, String> colTarih;
    @FXML private TableColumn<com.example.hastane_randevu_yonetim_sistemi.models.Randevu, String> colSaat;
    @FXML private TableColumn<com.example.hastane_randevu_yonetim_sistemi.models.Randevu, String> colDurum;
    @FXML private Label listeDurumLabel;
    @FXML private TextField profilAdField;
    @FXML private TextField profilSoyadField;
    @FXML private TextField profilTcField;
    @FXML private TextField profilTelefonField;
    @FXML private TextField profilSifreField;
    @FXML private Label profilDurumLabel;
    @FXML private TextField aramaCubugu;
    @FXML private TableView<com.example.hastane_randevu_yonetim_sistemi.models.Doktor> aramaTablosu;
    @FXML private TableColumn<com.example.hastane_randevu_yonetim_sistemi.models.Doktor, String> colAraAd;
    @FXML private TableColumn<com.example.hastane_randevu_yonetim_sistemi.models.Doktor, String> colAraSoyad;
    @FXML private TableColumn<com.example.hastane_randevu_yonetim_sistemi.models.Doktor, String> colAraBrans;
    @FXML private TabPane anaTabPane;
    @FXML private DatePicker guncelleTarihSecici;
    @FXML private ComboBox<String> guncelleSaatCombo;
    @FXML private TextField profilEmailField;
    @FXML private TextField profilCinsiyetField;
    @FXML private TextField profilDogumField;

    @FXML
    public void initialize() {
        poliklinikleriYukle();
        colDoktor.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("doktorAdSoyad"));
        colTarih.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("tarih"));
        colSaat.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("saat"));
        colDurum.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("durum"));

        randevulariListele();
        profilBilgileriniYukle();

        colAraAd.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("ad"));
        colAraSoyad.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("soyad"));
        colAraBrans.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("brans"));
        doktorAra();

        tarihSecici.valueProperty().addListener((observable, oldValue, newValue) -> musaitSaatleriGuncelle());
        doktorCombo.valueProperty().addListener((observable, oldValue, newValue) -> musaitSaatleriGuncelle());

        anaTabPane.getSelectionModel().selectedItemProperty().addListener((obs, eski, yeni) -> {
            if (yeni != null) {
                String sekme = yeni.getText();
                if (sekme.equals("Randevularım")) randevulariListele();
                else if (sekme.equals("Doktor Ara")) doktorAra();
                else if (sekme.equals("Profilim")) profilBilgileriniYukle();
            }
        });

        guncelleTarihSecici.valueProperty().addListener((obs, oldv, newv) -> guncelleMusaitSaatleriGetir());
    }

    @FXML
    protected void poliklinikSecildi() {
        String secilenBrans = poliklinikCombo.getValue();
        if (secilenBrans != null) doktorlariYukle(secilenBrans);
    }

    @FXML
    protected void randevuAlButonunaBasildi() {
        String doktorAdSoyad = doktorCombo.getValue();
        String saat = saatCombo.getValue();
        LocalDate tarih = tarihSecici.getValue();

        if (doktorAdSoyad == null || saat == null || tarih == null) {
            durumLabel.setText("Lütfen tüm seçimleri yapınız!");
            return;
        }

        if (tarih.isBefore(LocalDate.now())) {
            durumLabel.setText("Geçmiş bir tarihe randevu alamazsınız!");
            return;
        }

        String[] isimler = doktorAdSoyad.split(" ");
        String dokAd = isimler[0];

        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
            PreparedStatement psDok = conn.prepareStatement("SELECT id FROM doktorlar WHERE ad = ? LIMIT 1");
            psDok.setString(1, dokAd);
            ResultSet rsDok = psDok.executeQuery();

            int doktorId = 0;
            if (rsDok.next()) doktorId = rsDok.getInt("id");
            else {
                durumLabel.setText("Doktor bulunamadı!");
                return;
            }

            PreparedStatement psKontrol = conn.prepareStatement(
                    "SELECT id FROM randevular WHERE doktor_id = ? AND tarih = ? AND saat = ? AND durum != 'İptal'"
            );
            psKontrol.setInt(1, doktorId);
            psKontrol.setString(2, tarih.toString());
            psKontrol.setString(3, saat);
            ResultSet rsKontrol = psKontrol.executeQuery();

            if (rsKontrol.next()) {
                durumLabel.setText("Bu saatte doktorun başka randevusu var!");
                return;
            }

            int hastaId = Oturum.getInstance().getAktifKullanici().getId();

            PreparedStatement psIns = conn.prepareStatement(
                    "INSERT INTO randevular (hasta_id, doktor_id, tarih, saat, durum) VALUES (?, ?, ?, ?, ?)"
            );
            psIns.setInt(1, hastaId);
            psIns.setInt(2, doktorId);
            psIns.setString(3, tarih.toString());
            psIns.setString(4, saat);
            psIns.setString(5, "Bekliyor");

            if (psIns.executeUpdate() > 0) {
                durumLabel.setText("Randevu oluşturuldu! (" + tarih + " " + saat + ")");

                com.example.hastane_randevu_yonetim_sistemi.patterns.observer.RandevuBildirimcisi bildirimci =
                        new com.example.hastane_randevu_yonetim_sistemi.patterns.observer.RandevuBildirimcisi();

                com.example.hastane_randevu_yonetim_sistemi.models.Doktor ilgiliDoktor =
                        new com.example.hastane_randevu_yonetim_sistemi.models.Doktor(
                                doktorId, isimler[0], (isimler.length > 1 ? isimler[1] : ""), "Genel",
                                "000", "", "", ""
                        );

                bildirimci.aboneEkle(ilgiliDoktor);
                bildirimci.herkeseBildir("Hastanız " + tarih + " saat " + saat + " için randevu aldı.");

                doktorCombo.getSelectionModel().clearSelection();
                saatCombo.getSelectionModel().clearSelection();
                saatCombo.getItems().clear();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            durumLabel.setText("Hata: " + e.getMessage());
        }
    }

    private void poliklinikleriYukle() {
        poliklinikCombo.getItems().clear();
        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT brans FROM doktorlar");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) poliklinikCombo.getItems().add(rs.getString("brans"));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void doktorlariYukle(String brans) {
        doktorCombo.getItems().clear();
        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT ad, soyad FROM doktorlar WHERE brans = ?");
            ps.setString(1, brans);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                doktorCombo.getItems().add(rs.getString("ad") + " " + rs.getString("soyad"));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void musaitSaatleriGuncelle() {
        saatCombo.getItems().clear();
        String secilenDoktor = doktorCombo.getValue();
        LocalDate secilenTarih = tarihSecici.getValue();
        if (secilenDoktor == null || secilenTarih == null) return;

        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();

            String[] isimler = secilenDoktor.split(" ");
            PreparedStatement psDok = conn.prepareStatement("SELECT id FROM doktorlar WHERE ad = ?");
            psDok.setString(1, isimler[0]);
            ResultSet rsDok = psDok.executeQuery();

            if (!rsDok.next()) return;
            int doktorId = rsDok.getInt("id");

            int gunIndex = secilenTarih.getDayOfWeek().getValue();

            PreparedStatement psMesai = conn.prepareStatement(
                    "SELECT baslangic, bitis, aktif FROM doktor_mesai WHERE doktor_id = ? AND gun_id = ?"
            );
            psMesai.setInt(1, doktorId);
            psMesai.setInt(2, gunIndex);
            ResultSet rsMesai = psMesai.executeQuery();

            String baslangic = "09:00";
            String bitis = "17:00";
            boolean calisiyor = true;

            if (rsMesai.next()) {
                baslangic = rsMesai.getString("baslangic");
                bitis = rsMesai.getString("bitis");
                calisiyor = rsMesai.getInt("aktif") == 1;
            }

            if (!calisiyor) {
                saatCombo.setPromptText("Doktor Bugün Çalışmıyor");
                return;
            }

            java.util.List<String> tumSaatler = new java.util.ArrayList<>();
            java.util.List<String> referans = java.util.Arrays.asList(
                    "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00",
                    "12:30", "13:30", "14:00", "14:30", "15:00", "15:30",
                    "16:00", "16:30", "17:00"
            );

            for (String s : referans)
                if (s.compareTo(baslangic) >= 0 && s.compareTo(bitis) <= 0)
                    tumSaatler.add(s);

            PreparedStatement psSaat = conn.prepareStatement(
                    "SELECT saat FROM randevular WHERE doktor_id = ? AND tarih = ? AND durum != 'İptal'"
            );
            psSaat.setInt(1, doktorId);
            psSaat.setString(2, secilenTarih.toString());
            ResultSet rsSaat = psSaat.executeQuery();

            while (rsSaat.next()) tumSaatler.remove(rsSaat.getString("saat"));

            saatCombo.getItems().addAll(tumSaatler);

            if (tumSaatler.isEmpty()) saatCombo.setPromptText("Randevular Dolu");
            else saatCombo.setPromptText("Uygun Saatler");

        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    public void randevulariListele() {
        randevuTablosu.getItems().clear();
        int aktifHastaId = Oturum.getInstance().getAktifKullanici().getId();

        String sql = "SELECT r.id, r.hasta_id, r.doktor_id, r.tarih, r.saat, r.durum, d.ad, d.soyad " +
                "FROM randevular r JOIN doktorlar d ON r.doktor_id = d.id " +
                "WHERE r.hasta_id = ? AND r.durum != 'İptal'";

        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, aktifHastaId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String doktorTamAd = rs.getString("ad") + " " + rs.getString("soyad");
                com.example.hastane_randevu_yonetim_sistemi.models.Randevu r =
                        new com.example.hastane_randevu_yonetim_sistemi.models.Randevu(
                                rs.getInt("id"), rs.getInt("hasta_id"), rs.getInt("doktor_id"),
                                doktorTamAd, rs.getString("tarih"), rs.getString("saat"),
                                rs.getString("durum"), ""
                        );
                randevuTablosu.getItems().add(r);
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    protected void randevuIptalEt() {
        com.example.hastane_randevu_yonetim_sistemi.models.Randevu secilen =
                randevuTablosu.getSelectionModel().getSelectedItem();

        if (secilen == null) {
            listeDurumLabel.setText("Lütfen listeden iptal edilecek randevuyu seçiniz!");
            return;
        }

        if (secilen.getDurum().equals("İptal")) {
            listeDurumLabel.setText("Bu randevu zaten iptal edilmiş.");
            return;
        }

        com.example.hastane_randevu_yonetim_sistemi.patterns.state.IRandevuDurum durum =
                new com.example.hastane_randevu_yonetim_sistemi.patterns.state.IptalDurumu();

        durum.durumuGuncelle(secilen.getId());
        listeDurumLabel.setText("Randevu iptal edildi.");
        randevulariListele();
    }

    private void profilBilgileriniYukle() {
        com.example.hastane_randevu_yonetim_sistemi.models.Hasta h =
                (com.example.hastane_randevu_yonetim_sistemi.models.Hasta)
                        Oturum.getInstance().getAktifKullanici();

        profilAdField.setText(h.getAd());
        profilSoyadField.setText(h.getSoyad());
        profilTcField.setText(h.getTcNo());
        profilTelefonField.setText(h.getTelefon());
        profilSifreField.setText(h.getSifre());

        if (profilEmailField != null) profilEmailField.setText(h.getEmail());
        if (profilCinsiyetField != null) profilCinsiyetField.setText(h.getCinsiyet());
        if (profilDogumField != null) profilDogumField.setText(h.getDogumTarihi());
    }

    @FXML
    protected void profilGuncelle() {
        String yeniTel = profilTelefonField.getText();
        String yeniSifre = profilSifreField.getText();
        String yeniEmail = profilEmailField.getText();

        int id = Oturum.getInstance().getAktifKullanici().getId();

        String sql = "UPDATE hastalar SET telefon = ?, sifre = ?, email = ? WHERE id = ?";

        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, yeniTel);
            ps.setString(2, yeniSifre);
            ps.setString(3, yeniEmail);
            ps.setInt(4, id);

            if (ps.executeUpdate() > 0) profilDurumLabel.setText("Bilgileriniz başarıyla güncellendi!");
        } catch (SQLException e) {
            e.printStackTrace();
            profilDurumLabel.setText("Hata: " + e.getMessage());
        }
    }

    @FXML
    protected void cikisYap() {
        Oturum.getInstance().setAktifKullanici(null);

        try {
            javafx.fxml.FXMLLoader loader =
                    new javafx.fxml.FXMLLoader(getClass().getResource("/com/example/hastane_randevu_yonetim_sistemi/hello-view.fxml"));

            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load(), 900, 600);
            javafx.stage.Stage stage = (javafx.stage.Stage) profilDurumLabel.getScene().getWindow();

            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    protected void doktorAra() {
        String aranan = aramaCubugu.getText().trim();
        aramaTablosu.getItems().clear();

        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM doktorlar WHERE ad LIKE ? OR soyad LIKE ? OR brans LIKE ?"
            );

            String pattern = "%" + aranan + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                com.example.hastane_randevu_yonetim_sistemi.models.Doktor d =
                        new com.example.hastane_randevu_yonetim_sistemi.models.Doktor(
                                rs.getInt("id"), rs.getString("ad"), rs.getString("soyad"),
                                rs.getString("brans"), rs.getString("sifre"),
                                rs.getString("tc_no"), rs.getString("telefon"),
                                rs.getString("email")
                        );
                aramaTablosu.getItems().add(d);
            }

        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    protected void randevuGuncelle() {
        com.example.hastane_randevu_yonetim_sistemi.models.Randevu secilen =
                randevuTablosu.getSelectionModel().getSelectedItem();

        LocalDate yeniTarih = guncelleTarihSecici.getValue();
        String yeniSaat = guncelleSaatCombo.getValue();

        if (secilen == null || yeniTarih == null || yeniSaat == null) {
            listeDurumLabel.setText("Eksik bilgi!");
            return;
        }

        if (yeniTarih.isBefore(LocalDate.now())) {
            listeDurumLabel.setText("Geçmiş tarih seçilemez!");
            return;
        }

        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE randevular SET tarih = ?, saat = ?, durum = 'Bekliyor' WHERE id = ?"
            );

            ps.setString(1, yeniTarih.toString());
            ps.setString(2, yeniSaat);
            ps.setInt(3, secilen.getId());

            if (ps.executeUpdate() > 0) {
                listeDurumLabel.setText("Randevu güncellendi!");
                randevulariListele();
                guncelleSaatCombo.getItems().clear();
                guncelleTarihSecici.setValue(null);
            }

        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void guncelleMusaitSaatleriGetir() {
        guncelleSaatCombo.getItems().clear();

        com.example.hastane_randevu_yonetim_sistemi.models.Randevu secilen =
                randevuTablosu.getSelectionModel().getSelectedItem();

        LocalDate tarih = guncelleTarihSecici.getValue();

        if (secilen == null || tarih == null) return;

        java.util.List<String> tumSaatler = new java.util.ArrayList<>(java.util.Arrays.asList(
                "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
                "13:30", "14:00", "14:30", "15:00", "15:30", "16:00"
        ));

        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();

            String[] isimler = secilen.getDoktorAdSoyad().split(" ");
            PreparedStatement psId = conn.prepareStatement("SELECT id FROM doktorlar WHERE ad = ?");
            psId.setString(1, isimler[0]);

            ResultSet rsId = psId.executeQuery();

            if (rsId.next()) {
                int doktorId = rsId.getInt("id");

                PreparedStatement ps = conn.prepareStatement(
                        "SELECT saat FROM randevular WHERE doktor_id = ? AND tarih = ? AND durum != 'İptal'"
                );
                ps.setInt(1, doktorId);
                ps.setString(2, tarih.toString());

                ResultSet rsSaat = ps.executeQuery();

                while (rsSaat.next())
                    tumSaatler.remove(rsSaat.getString("saat"));

                guncelleSaatCombo.getItems().addAll(tumSaatler);
            }

        } catch (SQLException e) { e.printStackTrace(); }
    }
}
