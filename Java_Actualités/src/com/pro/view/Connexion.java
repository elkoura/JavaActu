package com.pro.view;

import com.pro.controller.Langage;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Connexion extends Application {
    private FXApp mainStage;
    
    public Connexion(FXApp mainStage) {
        this.mainStage = mainStage;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Connection.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        Scene scene = new Scene(fxmlLoader.getRoot(), 330, 280);
    
        stage.setTitle(Langage.INSCRIPTION);
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                // Get the Controller
                FXMLConnectionController controller = (FXMLConnectionController) fxmlLoader.getController();
                mainStage.setUser(controller.getUser());
            }
        });
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
