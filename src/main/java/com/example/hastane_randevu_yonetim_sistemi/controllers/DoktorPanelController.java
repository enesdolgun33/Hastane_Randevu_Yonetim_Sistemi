package com.example.hastane_randevu_yonetim_sistemi.controllers;

import com.example.hastane_randevu_yonetim_sistemi.database.VeritabaniBaglantisi;
import com.example.hastane_randevu_yonetim_sistemi.models.Oturum;
import com.example.hastane_randevu_yonetim_sistemi.models.Randevu;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoktorPanelController {

    @FXML private TableView<Randevu> randevuTablosu;
    @FXML private TableColumn<Randevu, String> colHasta, colTarih, colSaat, colDurum, colNot;
    @FXML private Label durumLabel;
    @FXML private DatePicker baslangicTarihSecici, bitisTarihSecici;
    @FXML private TextArea muayeneNotuAlani;
    @FXML private CheckBox chkIptalleriGoster;
    @FXML private TextField txtHastaAra;
    @FXML private TableView<com.example.hastane_randevu_yonetim_sistemi.models.Hasta> tblHastaArama;
    @FXML private TableColumn<com.example.hastane_randevu_yonetim_sistemi.models.Hasta, String> colHAd, colHSoyad, colHTc, colHTel;
    @FXML private TextField profilAdField, profilSoyadField, profilBransField, profilSifreField, profilTcField, profilTelefonField, profilEmailField;
    @FXML private Label profilDurumLabel;
    @FXML private GridPane mesaiGrid;
    @FXML private Label mesaiDurumLabel;
    @FXML private TabPane anaTabPane;
    @FXML private Label lblProfilHarfler;
    @FXML private TableColumn<com.example.hastane_randevu_yonetim_sistemi.models.Hasta, String> colHCinsiyet, colHDogum;

    private List<MesaiSatiri> mesaiSatirlari = new ArrayList<>();

    class MesaiSatiri {
        int gunId;
        CheckBox chkAktif;
        ComboBox<String> cmbBaslangic, cmbBitis;

        public MesaiSatiri(int gunId, String gunAdi, List<String> saatler) {
            this.gunId = gunId;
            this.chkAktif = new CheckBox(gunAdi);
            this.chkAktif.setPrefWidth(100);
            this.cmbBaslangic = new ComboBox<>();
            this.cmbBaslangic.getItems().addAll(saatler);
            this.cmbBaslangic.setValue("09:00");
            this.cmbBitis = new ComboBox<>();
            this.cmbBitis.getItems().addAll(saatler);
            this.cmbBitis.setValue("17:00");
        }
    }

    @FXML
    public void initialize() {
        colHasta.setCellValueFactory(new PropertyValueFactory<>("doktorAdSoyad"));
        colTarih.setCellValueFactory(new PropertyValueFactory<>("tarih"));
        colSaat.setCellValueFactory(new PropertyValueFactory<>("saat"));
        colDurum.setCellValueFactory(new PropertyValueFactory<>("durum"));
        colNot.setCellValueFactory(new PropertyValueFactory<>("muayeneNotu"));
        colHAd.setCellValueFactory(new PropertyValueFactory<>("ad"));
        colHSoyad.setCellValueFactory(new PropertyValueFactory<>("soyad"));
        colHTc.setCellValueFactory(new PropertyValueFactory<>("tcNo"));
        colHTel.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        colHCinsiyet.setCellValueFactory(new PropertyValueFactory<>("cinsiyet"));
        colHDogum.setCellValueFactory(new PropertyValueFactory<>("dogumTarihi"));

        randevulariListele();
        profilBilgileriniYukle();
        mesaiTablosunuOlustur();

        randevuTablosu.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) muayeneNotuAlani.setText(newVal.getMuayeneNotu());
            else muayeneNotuAlani.clear();
        });

        randevuTablosu.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && randevuTablosu.getSelectionModel().getSelectedItem() != null) {
                seciliHastaBilgisiniGoster();
            }
        });

        if (anaTabPane != null) {
            anaTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
                if (newTab.getText().equals("Profilim")) profilBilgileriniYukle();
            });
        }
    }

    @FXML
    protected void seciliHastaBilgisiniGoster() {
        Randevu secilen = randevuTablosu.getSelectionModel().getSelectedItem();
        if (secilen == null) {
            durumLabel.setText("Lütfen önce bir randevu seçiniz.");
            return;
        }

        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();

            String rSql = "SELECT hasta_id FROM randevular WHERE id = ?";
            PreparedStatement psR = conn.prepareStatement(rSql);
            psR.setInt(1, secilen.getId());
            ResultSet rsR = psR.executeQuery();

            if (rsR.next()) {
                int hId = rsR.getInt("hasta_id");

                String sql = "SELECT ad, soyad, tc_no, telefon, email, cinsiyet, dogum_tarihi FROM hastalar WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, hId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String email = rs.getString("email");
                    if (email == null || email.isEmpty()) email = "Belirtilmemiş";

                    String cinsiyet = rs.getString("cinsiyet");
                    if (cinsiyet == null || cinsiyet.isEmpty()) cinsiyet = "Belirtilmemiş";

                    String dogum = rs.getString("dogum_tarihi");
                    if (dogum == null || dogum.isEmpty()) dogum = "Belirtilmemiş";

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Hasta Detay Kartı");
                    alert.setHeaderText("Sayın " + rs.getString("ad") + " " + rs.getString("soyad"));
                    alert.setContentText(
                            "KİMLİK BİLGİLERİ\n--------------------------------\n" +
                                    "TC Kimlik No : " + rs.getString("tc_no") + "\n" +
                                    "Cinsiyet     : " + cinsiyet + "\n" +
                                    "Doğum Tarihi : " + dogum + "\n\n" +
                                    "İLETİŞİM BİLGİLERİ\n--------------------------------\n" +
                                    "Telefon      : " + rs.getString("telefon") + "\n" +
                                    "E-Posta      : " + email + "\n\n" +
                                    "RANDEVU BİLGİSİ\n--------------------------------\n" +
                                    "Tarih/Saat   : " + secilen.getTarih() + " " + secilen.getSaat()
                    );
                    alert.showAndWait();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            durumLabel.setText("Hasta bilgisi çekilemedi: " + e.getMessage());
        }
    }

    @FXML
    public void randevulariListele() {
        randevuTablosu.getItems().clear();
        int aktifDoktorId = Oturum.getInstance().getAktifKullanici().getId();
        String sql = "SELECT r.id, r.hasta_id, r.doktor_id, r.tarih, r.saat, r.durum, r.muayene_notu, h.ad, h.soyad " +
                "FROM randevular r JOIN hastalar h ON r.hasta_id = h.id WHERE r.doktor_id = ?";

        if (baslangicTarihSecici.getValue() != null && bitisTarihSecici.getValue() != null) sql += " AND r.tarih BETWEEN ? AND ?";
        else if (baslangicTarihSecici.getValue() != null) sql += " AND r.tarih >= ?";

        if (!chkIptalleriGoster.isSelected()) sql += " AND r.durum != 'İptal'";
        sql += " ORDER BY r.tarih DESC, r.saat ASC";

        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, aktifDoktorId);
            int p = 2;

            if (baslangicTarihSecici.getValue() != null)
                ps.setString(p++, baslangicTarihSecici.getValue().toString());
            if (baslangicTarihSecici.getValue() != null && bitisTarihSecici.getValue() != null)
                ps.setString(p++, bitisTarihSecici.getValue().toString());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String not = rs.getString("muayene_notu") != null ? rs.getString("muayene_notu") : "";
                randevuTablosu.getItems().add(new Randevu(
                        rs.getInt("id"), rs.getInt("hasta_id"), rs.getInt("doktor_id"),
                        rs.getString("ad") + " " + rs.getString("soyad"),
                        rs.getString("tarih"), rs.getString("saat"),
                        rs.getString("durum"), not
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void mesaiTablosunuOlustur() {
        mesaiGrid.getChildren().clear();
        mesaiSatirlari.clear();

        List<String> saatler = Arrays.asList("09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
                "12:00", "12:30", "13:30", "14:00", "14:30", "15:00", "15:30",
                "16:00", "16:30", "17:00");

        String[] gunler = {"Pazartesi", "Salı", "Çarşamba", "Perşembe",
                "Cuma", "Cumartesi", "Pazar"};

        mesaiGrid.add(new Label("Gün"), 0, 0);
        mesaiGrid.add(new Label("Başlangıç"), 1, 0);
        mesaiGrid.add(new Label("Bitiş"), 2, 0);

        int row = 1;
        for (int i = 0; i < 7; i++) {
            MesaiSatiri satir = new MesaiSatiri(i + 1, gunler[i], saatler);
            mesaiSatirlari.add(satir);
            mesaiGrid.add(satir.chkAktif, 0, row);
            mesaiGrid.add(satir.cmbBaslangic, 1, row);
            mesaiGrid.add(satir.cmbBitis, 2, row);
            row++;
        }

        veritabanindanMesaiyiYukle();
    }

    private void veritabanindanMesaiyiYukle() {
        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM doktor_mesai WHERE doktor_id = ?");
            ps.setInt(1, Oturum.getInstance().getAktifKullanici().getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int gunId = rs.getInt("gun_id");
                if (gunId >= 1 && gunId <= 7) {
                    MesaiSatiri satir = mesaiSatirlari.get(gunId - 1);
                    satir.chkAktif.setSelected(rs.getInt("aktif") == 1);
                    satir.cmbBaslangic.setValue(rs.getString("baslangic"));
                    satir.cmbBitis.setValue(rs.getString("bitis"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    protected void mesaiKaydet() {
        int dokId = Oturum.getInstance().getAktifKullanici().getId();
        Connection conn = VeritabaniBaglantisi.getInstance().getConnection();

        try {
            conn.prepareStatement("DELETE FROM doktor_mesai WHERE doktor_id = " + dokId).executeUpdate();
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO doktor_mesai (doktor_id, gun_id, baslangic, bitis, aktif) VALUES (?, ?, ?, ?, ?)");

            for (MesaiSatiri satir : mesaiSatirlari) {
                ps.setInt(1, dokId);
                ps.setInt(2, satir.gunId);
                ps.setString(3, satir.cmbBaslangic.getValue());
                ps.setString(4, satir.cmbBitis.getValue());
                ps.setInt(5, satir.chkAktif.isSelected() ? 1 : 0);
                ps.executeUpdate();
            }

            mesaiDurumLabel.setText("Program kaydedildi!");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    protected void notuKaydet() {
        Randevu secilen = randevuTablosu.getSelectionModel().getSelectedItem();
        if (secilen == null) return;

        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE randevular SET muayene_notu = ? WHERE id = ?");
            ps.setString(1, muayeneNotuAlani.getText());
            ps.setInt(2, secilen.getId());

            if (ps.executeUpdate() > 0) {
                durumLabel.setText("Not kaydedildi.");
                secilen.setMuayeneNotu(muayeneNotuAlani.getText());
                randevuTablosu.refresh();
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    protected void tumunuGoster() {
        baslangicTarihSecici.setValue(null);
        bitisTarihSecici.setValue(null);
        chkIptalleriGoster.setSelected(false);
        randevulariListele();
    }

    @FXML
    protected void hastaAra() {
        String aranan = txtHastaAra.getText().trim();
        tblHastaArama.getItems().clear();

        String sql = "SELECT * FROM hastalar WHERE ad LIKE ? OR soyad LIKE ? OR tc_no LIKE ?";

        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            String d = "%" + aranan + "%";

            ps.setString(1, d);
            ps.setString(2, d);
            ps.setString(3, d);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tblHastaArama.getItems().add(new com.example.hastane_randevu_yonetim_sistemi.models.Hasta(
                        rs.getInt("id"), rs.getString("ad"), rs.getString("soyad"),
                        rs.getString("tc_no"), rs.getString("sifre"), rs.getString("telefon"),
                        rs.getString("email"), rs.getString("cinsiyet"), rs.getString("dogum_tarihi")
                ));
            }

        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void profilBilgileriniYukle() {
        com.example.hastane_randevu_yonetim_sistemi.models.Kullanici k = Oturum.getInstance().getAktifKullanici();

        if (k instanceof com.example.hastane_randevu_yonetim_sistemi.models.Doktor d) {

            profilAdField.setText(d.getAd());
            profilSoyadField.setText(d.getSoyad());
            profilBransField.setText(d.getBrans());
            profilTcField.setText(d.getTcNo());
            profilTelefonField.setText(d.getTelefon());
            profilEmailField.setText(d.getEmail());
            profilSifreField.setText(d.getSifre());

            if (d.getAd() != null && d.getSoyad() != null && !d.getAd().isEmpty()) {
                String harfler = d.getAd().substring(0, 1) + d.getSoyad().substring(0, 1);
                lblProfilHarfler.setText(harfler.toUpperCase());
            } else {
                lblProfilHarfler.setText("DR");
            }
        }
    }

    @FXML
    protected void profilGuncelle() {
        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE doktorlar SET sifre=?, telefon=?, email=? WHERE id=?");

            ps.setString(1, profilSifreField.getText());
            ps.setString(2, profilTelefonField.getText());
            ps.setString(3, profilEmailField.getText());
            ps.setInt(4, Oturum.getInstance().getAktifKullanici().getId());

            if (ps.executeUpdate() > 0)
                profilDurumLabel.setText("Bilgiler Güncellendi!");

        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    protected void tamamlandiIsaretle() {
        durumGuncelle(new com.example.hastane_randevu_yonetim_sistemi.patterns.state.TamamlandiDurumu());
    }

    @FXML
    protected void gelmediIsaretle() {
        durumGuncelle(new com.example.hastane_randevu_yonetim_sistemi.patterns.state.GelmediDurumu());
    }

    private void durumGuncelle(com.example.hastane_randevu_yonetim_sistemi.patterns.state.IRandevuDurum yeniDurum) {
        Randevu s = randevuTablosu.getSelectionModel().getSelectedItem();
        if (s != null) {
            yeniDurum.durumuGuncelle(s.getId());
            randevulariListele();
        }
    }

    @FXML
    protected void cikisYap() {
        try {
            Oturum.getInstance().setAktifKullanici(null);
            ((javafx.stage.Stage) randevuTablosu.getScene().getWindow()).setScene(
                    new javafx.scene.Scene(
                            new javafx.fxml.FXMLLoader(
                                    getClass().getResource("/com/example/hastane_randevu_yonetim_sistemi/hello-view.fxml")
                            ).load(),
                            800, 600
                    )
            );
        } catch (Exception e) { }
    }
}
