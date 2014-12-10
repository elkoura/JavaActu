package com.pro.view;



import com.pro.controller.Langage;
import com.pro.model.Article;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

// la classe principale !

public class BodyArt extends Application {
	
	private Article art;
	
    
    public BodyArt(Article art) {
    	
    	this.art = art;
		
	}

	@Override
    public void start(Stage primaryStage) {
       
        
        StackPane root = new StackPane();
       
        
        Scene scene = new Scene(root, 700, 460);
        
        Text body = new Text();
      
        body.setWrappingWidth(680);
        body.setTextAlignment(TextAlignment.JUSTIFY);
        body.setText(art.getExtraire_article());
        body.setFont(new Font(16));
        
        javafx.scene.image.Image img;
        if(art.getUrlImage() == null)
            img = new javafx.scene.image.Image(Langage.RESSOURCE_PATH + "images/indisponible.jpg");
        else
            img = new javafx.scene.image.Image(art.getUrlImage());
        ImageView image = new ImageView();
        image.setImage(img);
        image.setPreserveRatio(true);
        image.setFitWidth(250);
        image.setFitHeight(180);
        image.setPreserveRatio(true);
        
        Text titre = new Text(art.getTitre());
        titre.setId("TitreArticle");
        titre.setFont(Font.font ("Verdana", 15));
        titre.setWrappingWidth(350);
        titre.setFill(Color.BLUE);

        Label source = new Label("Source : "+art.getSource());
        Label date_source = new Label("Publie le : "+ art.getPubdate().toString());
        
        VBox infos = new VBox(titre,date_source,source);
        HBox topbox = new HBox(image,infos);
        topbox.setPadding(new Insets(10, 10, 10, 10));
        infos.setPadding(new Insets(0, 5, 5, 15));
        VBox mainBox = new VBox(topbox,body);
        mainBox.setPadding(new Insets(5, 5, 5, 5));
        
       
        
       
        ScrollPane bodyView = new ScrollPane(mainBox);
        
        root.getChildren().add(bodyView);
        
        
        primaryStage.setTitle(art.getSource()+" - "+ art.getTitre());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//        launch(args);
//    }
    
}
