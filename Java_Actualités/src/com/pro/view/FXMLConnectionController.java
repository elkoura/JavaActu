package com.pro.view;

import com.pro.controller.Langage;
import com.pro.model.BDD;
import com.pro.model.User;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class FXMLConnectionController {

    @FXML
    private Text auth, lblEnd;
    @FXML
    private Label lblEmail, lblMdp;
    @FXML
    private TextField champEmail, champMdp;
    @FXML
    private Button btnValide, btnAnnule;
    
    private User user;

    
    @FXML
    public void initialize() {
        auth.setText(Langage.AUTHENTIFICATION);
        lblEmail.setText(Langage.EMAIL);
        lblMdp.setText(Langage.MDP);
        btnValide.setText(Langage.VALIDER);
        btnAnnule.setText(Langage.ANNULER);
    }

    @FXML
    protected void connectionActionHandler(ActionEvent event) {
        BDD conn = new BDD();
        try {
            user = conn.getUserByEmail(champEmail.getText());
        } catch (SQLException e) {
            System.err.println("Pb de récup de l'utilisateur en BDD -____-\n"+
                  e.getMessage());
        }
                
        // Si la BD a trouvé une correpondance de email/mdp
        if(!champEmail.getText().equals("") && 
                !champMdp.getText().equals("") &&
                user.getPassword().equals(champMdp.getText()) ) {
            // CONNEXION OK
            lblEnd.setFill(Paint.valueOf("GREEN"));
            lblEnd.setText(Langage.CONNECTE);
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
    
    public User getUser() {
        return user;
    }

}
