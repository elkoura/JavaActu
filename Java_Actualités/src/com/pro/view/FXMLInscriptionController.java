package com.pro.view;

import com.pro.controller.Langage;
import com.pro.model.BDD;
import com.pro.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FXMLInscriptionController {

    @FXML
    private Text inscription, lblEnd;
    @FXML
    private Label lblNom, lblEmail, lblMdp, lblConfMdp;
    @FXML
    private TextField champNom, champEmail, champMdp, champConfirmMdp;
    @FXML
    private Button btnValide, btnAnnule, btnDac;

    
    @FXML
    public void initialize() {
        inscription.setText(Langage.INSCRIPTION);
        //auth.setText(Langage.AUTHENTIFICATION);
        lblNom.setText(Langage.NOM);
        lblEmail.setText(Langage.EMAIL);
        lblMdp.setText(Langage.MDP);
        lblConfMdp.setText(Langage.CONFIRM_MDP);
        btnValide.setText(Langage.VALIDER);
        btnAnnule.setText(Langage.ANNULER);
    }
    
    @FXML
    protected void creerCompteActionHandler(ActionEvent event) {
        lblEnd.setFill(Paint.valueOf("RED"));
        // Si mdp != confirmation
        if( ! champMdp.getText().equals(champConfirmMdp.getText()))
            lblEnd.setText(Langage.ERR_CONFIRM_MDP);
        // Si un des champs est vide
        else if(champNom.getText().equals("") ||
                champEmail.getText().equals("") ||
                champMdp.getText().equals("") ||
                champConfirmMdp.getText().equals("") ) {
            lblEnd.setText(Langage.ERR_CHAMP_VIDE);
        }
        // Enregistrement en BD d'un utilisateur
        else {
            BDD conn = new BDD();
            User user = new User();

            user.setNom(champNom.getText());
            user.setEmail(champEmail.getText());
            user.setPassword(champMdp.getText());
            conn.addUser(user);

            lblEnd.setText(Langage.ABIENTOT);
            lblEnd.setFill(Paint.valueOf("GREEN"));
            btnValide.setDisable(true);
            btnDac.setVisible(true);
            btnDac.setDefaultButton(true);
        }
    }
    
    @FXML
    protected void connectionActionHandler(ActionEvent event) {
        if(champMdp.getText().equals(champEmail.getText())) {
            //CONNECTE
        }
        else {
            lblEnd.setFill(Paint.valueOf("RED"));
            lblEnd.setText(Langage.ERR_CONNECTION);
        }
            
    }
    
    @FXML
    protected void cancelActionHandler(ActionEvent event) {
        Stage stage = (Stage) btnValide.getScene().getWindow();
        stage.close();
    }

}
