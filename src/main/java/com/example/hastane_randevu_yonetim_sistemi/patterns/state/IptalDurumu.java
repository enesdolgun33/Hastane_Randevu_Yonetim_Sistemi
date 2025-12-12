package com.example.hastane_randevu_yonetim_sistemi.patterns.state;

import com.example.hastane_randevu_yonetim_sistemi.database.VeritabaniBaglantisi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IptalDurumu implements IRandevuDurum {
    @Override
    public void durumuGuncelle(int randevuId) {
        String sql = "UPDATE randevular SET durum = 'İptal' WHERE id = ?";
        try {
            Connection conn = VeritabaniBaglantisi.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, randevuId);
            ps.executeUpdate();
            System.out.println("State Deseni: Randevu #" + randevuId + " 'İptal' durumuna geçti.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}