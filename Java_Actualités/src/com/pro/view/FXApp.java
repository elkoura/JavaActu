package com.pro.view;

import com.pro.model.Article;
import java.net.CookieHandler;
import java.util.ArrayList;
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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class FXApp extends Application {

    public FXApp() {
        System.out.println("Constructeur");
    }

    @Override
    public void start(Stage primaryStage) {
        
        //  =========  Barre du haut  =========
        StackPane topStack = new StackPane();
        topStack.setAlignment(Pos.CENTER_LEFT);
        //topStack.setPadding(new Insets(15, 12, 15, 12));

        ImageView logo = new ImageView("com/pro/view/images/Logo.png");
        logo.setPreserveRatio(true);
        logo.setFitWidth(150);

        // Barre des boutons à droite
        HBox topRightBox = new HBox();
        topRightBox.setId("topRightBox");
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
                    e.printStackTrace();
                }
            }
        });

        topRightBox.getChildren().addAll(newAccount, connexion);
        topStack.getChildren().addAll(topRightBox, logo);


        //   =========   Crée la liste des articles   =========
        ArrayList<Article> articleList = new ArrayList<>();
        // REMPLACER CES FAUSSES INSTANCES PAR LES VRAIS ARTICLES !!
        articleList.add(new Article(1));
        articleList.add(new Article(2));
        articleList.add(new Article(3));
        articleList.add(new Article(4));
        articleList.add(new Article(5));
        articleList.add(new Article(6));

        VBox articleBox = new VBox();
        addAllArticles(articleBox, articleList);
        articleBox.setPadding(new Insets(15, 10, 15, 10));
        articleBox.setSpacing(10);
        ScrollPane articlesView = new ScrollPane(articleBox);
        

       
        //   =========   Met les éléments dans la fenêtre   =========
        BorderPane root = new BorderPane();
        root.setTop(topStack);
        root.setCenter(articlesView);

        Scene scene = new Scene(root, 1200, 675);
        scene.getStylesheets().add(FXApp.class.getResource("Style.css").toExternalForm());
        addMouseEnter_ExitEvents(scene, connexion);
        addMouseEnter_ExitEvents(scene, newAccount);
        addMouseEnter_ExitEvents(scene, articlesView);

        primaryStage.setTitle("Actu Java");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest( new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                System.out.println("L'application se ferme !!!");
                System.exit(0);
            }
        });

        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void addAllArticles(VBox articleBox, ArrayList<Article> articleList) {
        for(Article artI : articleList) {
            ImageView image = new ImageView("com/pro/view/images/articleImg.jpg");
            image.setPreserveRatio(true);
            image.setFitHeight(80);

            ImageView favoris = new ImageView("com/pro/view/images/favorisON.png");
            favoris.setPreserveRatio(true);
            favoris.setFitWidth(25);
            VBox vbFavoris = new VBox(favoris);
            
            Label titre = new Label(artI.getTitre());
            Label date_source = new Label(artI.getSource());
            Label description = new Label(artI.getDescription());
            VBox vbDetails = new VBox(titre, date_source, description);

            HBox articleNode = new HBox(image, vbFavoris, vbDetails);
            articleNode.setPadding(new Insets(5, 15, 5, 15));
            articleNode.setSpacing(18);
            articleNode.setId("articleNode");
            articleNode.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    System.out.println("Je veux afficher l'Article "+artI.getTitre());
                }
            });
            articleBox.getChildren().addAll(articleNode, new Separator(Orientation.HORIZONTAL));
        }
    }

    private void addMouseEnter_ExitEvents(Scene scene, Node node) {
        // La souris entre  => Cursor = main
        node.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                scene.setCursor(Cursor.HAND);
            }
        });
        
        // La souris sort  => Cursor = normal
        node.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                scene.setCursor(Cursor.DEFAULT);
            }
        });
    }
}


