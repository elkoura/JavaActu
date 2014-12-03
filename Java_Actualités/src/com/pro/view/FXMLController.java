package com.pro.view;

import com.pro.controller.Langage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class FXMLController {
	@FXML private Text actiontarget;
    @FXML private Button valide;
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        actiontarget.setText(Langage.ABIENTOT);
        // ici on peut faire le traitement pour verfier les champs saisie par l'user
        // fermeture de la fenêtre aprés la connexion
        Stage stage = (Stage) valide.getScene().getWindow();
        stage.close();
    }

}