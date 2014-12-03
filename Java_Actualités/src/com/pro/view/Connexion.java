package com.pro.view;

import com.pro.controller.Langage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Connexion extends Application {
    
	@Override
    public void start(Stage stage) throws Exception {
       Parent root = FXMLLoader.load(getClass().getResource("Connexion.fxml"));
    
        Scene scene = new Scene(root, 300, 275);
    
        stage.setTitle(Langage.CONNEXION);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
