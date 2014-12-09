package com.pro.view;

import com.pro.controller.Langage;
import com.pro.model.Article;
import com.pro.model.FluxRSS;
import com.pro.model.BDD;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
    private List<Article> allArticleList;
    private List<Article> articleList;
    //private final HashMap<Integer, String> couleurDuFlux;
    private final BDD conn;
	 
    public FXApp() {
        // complète les champs statics de la classe Langue
        Langage.chargerFichierLangue("build/classes/com/pro/ressources/en.lang");

        conn = new BDD();
        System.out.println("Constructeur de FXApp");
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {
        
        //  =========  Barre du haut  (bleue) =========
        StackPane topStack = new StackPane();
        topStack.setAlignment(Pos.CENTER_LEFT);
        //topStack.setPadding(new Insets(15, 12, 15, 12));

        // Logo à gauche
        ImageView logo = new ImageView(Langage.RESSOURCE_PATH + "images/Logo.png");
        logo.setPreserveRatio(true);
        logo.setFitWidth(150);

        // Box des boutons à droite
        HBox topRightBox = new HBox();
        topRightBox.setId("topRightBox");
        topRightBox.setPadding(new Insets(15, 12, 15, 12));
        topRightBox.setSpacing(20);
        topRightBox.setAlignment(Pos.CENTER_RIGHT);

        // Bouton connexion
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Bouton Créer un compte
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        topRightBox.getChildren().addAll(newAccount, connexion);
        topStack.getChildren().addAll(topRightBox, logo);

        //   =========   Va chercher la liste des articles en BD et l'affiche au centre   =========
        allArticleList = conn.articleList();
        // Trie la liste par date de parution
        allArticleList.sort(new Comparator<Article>() {
            @Override
            public int compare(Article o1, Article o2) {
                return o1.getPubdate().compareTo(o2.getPubdate());
            }
        });
        articleList = new ArrayList<>(allArticleList);
        
        // Partie centrale qui contient les articles
        VBox articleBox = new VBox();
        articleBox.setPrefWidth(850);
        articleBox.setPadding(new Insets(15, 10, 15, 10));
        articleBox.setSpacing(10);
        ScrollPane articlesView = new ScrollPane(articleBox);
        
        //  =========  Barre de gauche avec la liste des flux RSS  =========
        ArrayList<FluxRSS> rssList = conn.rssList();
        Label fluxRSS = new Label(Langage.SOURCES);
        fluxRSS.setPadding(new Insets(20, 0, 10, 0));
        fluxRSS.setId("LabelSrcFlux");
        VBox leftBox = new VBox(fluxRSS);
        leftBox.setSpacing(10);
	leftBox.setPrefWidth(300);
        leftBox.setPadding(new Insets(10, 10, 10, 10));
       
        //   =========   Organise les conteneurs dans un BorderPane   =========
        BorderPane root = new BorderPane();
        root.setTop(topStack);
        root.setLeft(leftBox);
        root.setCenter(articlesView);

        //    =========   Crée la fenêtre principale   =========
        Scene scene = new Scene(root, 1200, 675);
        scene.getStylesheets().add(FXApp.class.getResource("Style.css").toExternalForm());
        addMouseEnter_ExitEvents(scene, connexion);
        addMouseEnter_ExitEvents(scene, newAccount);
        addMouseEnter_ExitEvents(scene, articlesView);

        // Rempli les données de Flux et d'articles correspondants
        addAllRSS(leftBox, articleBox, rssList, scene);
        addAllArticles(articleBox, scene);

        primaryStage.setTitle(Langage.ACTUJAVA);
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
     * Crée un encart d'article
     * @param articleBox la boite contenant tous les articles
     * @param articleList la liste des objets articles (DONNEES)
     */
    private void addAllArticles(VBox articleBox, Scene scene) {
        articleBox.getChildren().clear();
        // Pour chaque article de la liste de donénes
        for(Article artI : articleList) {
            // Crée l'image de gauche
        	String url_img = artI.getUrlImage() ;
        	if(url_img == null)
        		url_img = "http://21virages.free.fr/blog/public/Caravane/2012/indisponible.jpg";
        	javafx.scene.image.Image img = new javafx.scene.image.Image(url_img);
            ImageView image = new ImageView();
            image.setImage( img);
            image.setPreserveRatio(true);
            image.setFitWidth(120);
            image.setFitHeight(100);

            // Crée l'étoile des favoris
            ImageView favoris = new ImageView(Langage.RESSOURCE_PATH + "images/favorisOFF.png");
            favoris.setPreserveRatio(true);
            favoris.setFitWidth(15);
            VBox vbFavoris = new VBox(favoris);
            
            // Titre et description de l'article
            Label titre = new Label(artI.getTitre());
            titre.setId("TitreArticle");
            
            Label date_source = new Label(artI.getSource());
            date_source.setPadding(new Insets(0, 6, 2, 6));
            date_source.setStyle("-fx-background-color: blue;");
            date_source.setStyle("-fx-background-color: "+ conn.getRssColor(artI.getRssId()) + ";"
                    + "-fx-background-radius: 9,8;");
            date_source.setId("Date_source");
            
            Label description = new Label(artI.getDescription());
            description.setPrefWidth(700);
            description.setWrapText(true);
            VBox vbDetails = new VBox(titre, date_source, description);

            // crée l'article en assemblant les composants
            HBox articleNode = new HBox(image, vbFavoris, vbDetails);
            articleNode.setPadding(new Insets(5, 15, 5, 15));
            articleNode.setSpacing(18);
            articleNode.setId("articleNode");
            articleNode.setOnMouseClicked( new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println("clic sur "+artI.getTitre());
                }
            });

            // Ajoute l'article et une barre de séparation à la boite
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
        node.setOnMouseExited( new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                scene.setCursor(Cursor.DEFAULT);
            }
        });
    }

    private void addAllRSS(VBox leftBox, VBox articleBox, ArrayList<FluxRSS> rssList, Scene scene) {
        for(FluxRSS flux : rssList) {
            CheckBox chkBoite = new CheckBox(flux.getNom());
            chkBoite.setPadding(new Insets(0, 20, 20, 10));
            chkBoite.setSelected(true);
            chkBoite.setOnAction( new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //System.out.println(chkBoite.getText() + chkBoite.isSelected());
                    if(chkBoite.isSelected()) {
                        // Sélectionne les articles du flux et les affiche
                        for(Article art : allArticleList) {
                            if(art.getRssId() == flux.getId())
                                articleList.add(art);
                        }
                    }
                    else {
                        for( int i=0; i < articleList.size(); i++) {
                            if(articleList.get(i).getRssId() == flux.getId()) {
                                articleList.remove(i);
                                i--;
                            }
                        }
                    }
                    articleList.sort(new Comparator<Article>() {
                        @Override
                        public int compare(Article o1, Article o2) {
                            return o1.getPubdate().compareTo(o2.getPubdate());
                        }
                    });
                    addAllArticles(articleBox, scene);
                }
            });

            chkBoite.setStyle("-fx-font-size: 11pt;"
                    + "-fx-background-color: "+ flux.getColor() + ";"
                    + "-fx-background-radius: 9,8;");
            leftBox.getChildren().add(chkBoite);
        }
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

