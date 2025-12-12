package com.example.hastane_randevu_yonetim_sistemi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HastaneRYS_Application extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HastaneRYS_Application.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("Hastane Randevu Yonetim Sistemi!");
        stage.setScene(scene);
        com.example.hastane_randevu_yonetim_sistemi.database.VeritabaniBaglantisi.getInstance();
        stage.show();
    }
}
