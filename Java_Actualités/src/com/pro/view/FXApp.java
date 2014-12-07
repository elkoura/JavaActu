package com.pro.view;



import com.pro.controller.Langage;
import com.pro.model.Article;
import com.pro.model.FluxRSS;
import com.pro.model.BDD;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class FXApp extends Application {
	 private BDD conn;
	 
    public FXApp() {
        Langage.chargerFichierLangue("bin/com/pro/ressources/en.lang");
        conn = new BDD();
        System.out.println("Constructeur de FXApp");
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {
        
        //  =========  Barre du haut  =========
        StackPane topStack = new StackPane();
        topStack.setAlignment(Pos.CENTER_LEFT);
        //topStack.setPadding(new Insets(15, 12, 15, 12));

        ImageView logo = new ImageView(Langage.RESSOURCE_PATH + "images/Logo.png");
        logo.setPreserveRatio(true);
        logo.setFitWidth(150);

        // Barre des boutons à droite
        HBox topRightBox = new HBox();
        topRightBox.setId("topRightBox");
        topRightBox.setPadding(new Insets(15, 12, 15, 12));
        topRightBox.setSpacing(20);
        topRightBox.setAlignment(Pos.CENTER_RIGHT);

        Button connexion = new Button(Langage.CONNEXION);
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
                    e.printStackTrace();
                }
            }
        });

        Button newAccount = new Button(Langage.CREER_COMPTE);
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
                    e.printStackTrace();
                }
            }
        });

        topRightBox.getChildren().addAll(newAccount, connexion);
        topStack.getChildren().addAll(topRightBox, logo);


        //   =========   Crée la liste des articles et l'affichage du centre   =========
        ArrayList<Article> articleList = conn.articleList();
        // REMPLACER CES FAUSSES INSTANCES PAR LES VRAIS ARTICLES !!
   
        VBox articleBox = new VBox();
        addAllArticles(articleBox, articleList);
        articleBox.setPadding(new Insets(15, 10, 15, 10));
        articleBox.setSpacing(10);
        ScrollPane articlesView = new ScrollPane(articleBox);
        

        //  =========  Barre de gauche  =========
        ArrayList<FluxRSS> rssList = new ArrayList<>();
        rssList.add(new FluxRSS("FIRST", "hey :)"));
        rssList.add(new FluxRSS());
        rssList.add(new FluxRSS());
        rssList.add(new FluxRSS());
        rssList.add(new FluxRSS());
        rssList.add(new FluxRSS());
        rssList.add(new FluxRSS());
        rssList.add(new FluxRSS());
        rssList.add(new FluxRSS());
        rssList.add(new FluxRSS());
        Label fluxRSS = new Label(Langage.SOURCES);
        fluxRSS.setId("LabelSrcFlux");
        VBox leftBox = new VBox(fluxRSS);
        leftBox.setPrefWidth(300);
        leftBox.setPadding(new Insets(10, 10, 10, 10));
        addAllRSS(leftBox, rssList);
       
        //   =========   Met les éléments dans la fenêtre   =========
        BorderPane root = new BorderPane();
        root.setTop(topStack);
        root.setLeft(leftBox);
        root.setCenter(articlesView);

        Scene scene = new Scene(root, 1200, 675);
        scene.getStylesheets().add(FXApp.class.getResource("Style.css").toExternalForm());
        addMouseEnter_ExitEvents(scene, connexion);
        addMouseEnter_ExitEvents(scene, newAccount);
        addMouseEnter_ExitEvents(scene, articlesView);

        primaryStage.setTitle(Langage.ACTUJAVA);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest( (WindowEvent event) -> {
            System.out.println("L'application se ferme !!!");
            System.exit(0);
        });

        primaryStage.show();
    }

    private void addAllArticles(VBox articleBox, ArrayList<Article> articleList) {
        for(Article artI : articleList) {
        	String url_img = artI.getUrlImage() ;
        	if(url_img == null)
        		url_img = "http://21virages.free.fr/blog/public/Caravane/2012/indisponible.jpg";
        	javafx.scene.image.Image img = new javafx.scene.image.Image(url_img);
            ImageView image = new ImageView();
            image.setImage( img);
            image.setPreserveRatio(true);
            image.setFitWidth(120);
            image.setFitHeight(100);

            ImageView favoris = new ImageView(Langage.RESSOURCE_PATH + "images/favorisOFF.png");
            favoris.setPreserveRatio(true);
            favoris.setFitWidth(15);
            VBox vbFavoris = new VBox(favoris);
            
            Label titre = new Label(artI.getTitre());
            Label date_source = new Label(artI.getSource());
            Label description = new Label(artI.getDescription());
            VBox vbDetails = new VBox(titre, date_source, description);

            HBox articleNode = new HBox(image, vbFavoris, vbDetails);
            articleNode.setPadding(new Insets(5, 15, 5, 15));
            articleNode.setSpacing(18);
            articleNode.setId("articleNode");
            articleNode.setOnMouseClicked((MouseEvent event) -> {
                System.out.println("Je veux afficher l'Article "+artI.getTitre());
            });
            articleBox.getChildren().addAll(articleNode, new Separator(Orientation.HORIZONTAL));
        }
    }

    private void addMouseEnter_ExitEvents(Scene scene, Node node) {
        // La souris entre  => Cursor = main
        node.setOnMouseEntered((MouseEvent event) -> {
            scene.setCursor(Cursor.HAND);
        });
        
        // La souris sort  => Cursor = normal
        node.setOnMouseExited((MouseEvent event) -> {
            scene.setCursor(Cursor.DEFAULT);
        });
    }

    private void addAllRSS(VBox leftBox, ArrayList<FluxRSS> rssList) {
        for(FluxRSS flux : rssList) {
            CheckBox chkBoite = new CheckBox("labvel CHeckbox");
            chkBoite.setOnAction((ActionEvent event) -> {
                System.out.println("CheckBox"+flux.getId() + " is "+chkBoite.isSelected());
            });
            
            Label nom = new Label(flux.getNom());
            HBox box = new HBox(chkBoite, nom);
            box.setSpacing(15);
            box.setPadding(new Insets(5, 10, 5, 20));
            leftBox.getChildren().add(box);
        }
    }
   
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}