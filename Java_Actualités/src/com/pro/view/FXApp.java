package com.pro.view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FXApp extends Application {

    public FXApp() {
        System.out.println("Constructeur");
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane topStack = new StackPane();
        topStack.setAlignment(Pos.CENTER_LEFT);
        topStack.setPadding(new Insets(15, 12, 15, 12));

        ImageView logo = new ImageView("com/pro/view/images/Logo.png");
        logo.setPreserveRatio(true);
        logo.setFitWidth(150);

        HBox topRightBox = new HBox();
        topRightBox.setStyle("-fx-background-color: #336699;");
        topRightBox.setPadding(new Insets(15, 12, 15, 12));
        topRightBox.setSpacing(20);
        topRightBox.setAlignment(Pos.CENTER_RIGHT);

        Button connexion = new Button("Connection");
        connexion.setId("TopButtons");
        connexion.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Je veux me connecter !!!!!");
                Stage stage = new Stage();
                Connexion ConnWin = new Connexion();
                try {
					ConnWin.start(stage);
					stage.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        Button newAccount = new Button("Créer un compte");
        newAccount.setId("TopButtons");
        newAccount.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Je veux créer un compte !!!!!");
                
                Stage stage = new Stage();
                Inscription InscWin = new Inscription();
                try {
					InscWin.start(stage);
					stage.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        topRightBox.getChildren().addAll(newAccount, connexion);
        topStack.getChildren().addAll(topRightBox, logo);

        BorderPane root = new BorderPane();
        root.setTop(topStack);

        Scene scene = new Scene(root, 800, 450);
        primaryStage.setTitle("Actu Java");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
