package com.example.hastane_randevu_yonetim_sistemi.database;

import com.example.hastane_randevu_yonetim_sistemi.models.Randevu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RandevuDAO {

    private Connection conn;

    public RandevuDAO() {
        this.conn = VeritabaniBaglantisi.getInstance().getConnection();
    }

    public List<Randevu> getRandevularByDoktor(int doktorId) {
        List<Randevu> liste = new ArrayList<>();
        String sql = "SELECT r.id, r.hasta_id, r.doktor_id, r.tarih, r.saat, r.durum, r.muayene_notu, h.ad, h.soyad " +
                "FROM randevular r JOIN hastalar h ON r.hasta_id = h.id WHERE r.doktor_id = ? ORDER BY r.tarih DESC, r.saat ASC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, doktorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String hastaAdSoyad = rs.getString("ad") + " " + rs.getString("soyad");
                String not = rs.getString("muayene_notu");
                if(not == null) not = "";

                liste.add(new Randevu(
                        rs.getInt("id"), rs.getInt("hasta_id"), rs.getInt("doktor_id"),
                        hastaAdSoyad, rs.getString("tarih"), rs.getString("saat"), rs.getString("durum"), not
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    public boolean notGuncelle(int randevuId, String not) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE randevular SET muayene_notu = ? WHERE id = ?");
            ps.setString(1, not);
            ps.setInt(2, randevuId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}