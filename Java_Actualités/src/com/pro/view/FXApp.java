package com.pro.view;

import com.pro.controller.Langage;
import com.pro.model.Article;
import com.pro.model.FluxRSS;
import com.pro.model.BDD;
import com.pro.model.User;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
    private User currentUser = null;
	 
    public FXApp() {
        // compl�te les champs statics de la classe Langue
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

        // Logo � gauche
        ImageView logo = new ImageView(Langage.RESSOURCE_PATH + "images/Logo.png");
        logo.setPreserveRatio(true);
        logo.setFitWidth(150);

        // Box des boutons � droite
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
            }
        });

        // Bouton Cr�er un compte
        Button newAccount = new Button(Langage.CREER_COMPTE);
        newAccount.setId("TopButtons");
        newAccount.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Je veux cr�er un compte !!!!!");

                Stage stage = new Stage();
                Inscription InscWin = new Inscription();
                try {
                    InscWin.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        topRightBox.getChildren().addAll(connexion, newAccount);
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

        //    =========   Cr�e la fen�tre principale   =========
        Scene scene = new Scene(root, 1200, 675);
        scene.getStylesheets().add(FXApp.class.getResource("Style.css").toExternalForm());
        addMouseEnter_ExitEvents(scene, connexion);
        addMouseEnter_ExitEvents(scene, newAccount);
        addMouseEnter_ExitEvents(scene, articlesView);

        // Rempli les donn�es de Flux et d'articles correspondants
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
     * Cr�e un encart d'article
     * @param articleBox la boite contenant tous les articles
     * @param articleList la liste des objets articles (DONNEES)
     */
    private void addAllArticles(VBox articleBox, Scene scene) {
        articleBox.getChildren().clear();
        // Pour chaque article de la liste de don�nes
        for(Article artI : articleList) {
            // Cr�e l'image de gauche
        	String url_img = artI.getUrlImage() ;
        	if(url_img == null)
        		url_img = Langage.RESSOURCE_PATH + "/images/indisponible.jpg";
        	javafx.scene.image.Image img = new javafx.scene.image.Image(url_img);
            ImageView image = new ImageView();
            image.setImage(img);
            image.setPreserveRatio(true);
            image.setFitWidth(120);
            image.setFitHeight(100);

            // Cr�e l'�toile des favoris
            ImageView favoris = new ImageView(Langage.RESSOURCE_PATH + "images/favorisOFF.png");
            favoris.setPreserveRatio(true);
            favoris.setFitWidth(15);
            VBox vbFavoris = new VBox(favoris);
            favoris.setOnMouseClicked(new EventHandler<MouseEvent>() {
                // FAVORIS cliqué
                    @Override
                    public void handle(MouseEvent event) {
                        //if(currentUser != null) {
                            // Teste si le favoris
                        //}
                            
                    }
                });
            
            // Titre et description de l'article
            Label titre = new Label(artI.getTitre());
            titre.setId("TitreArticle");
            
            Label date_source = new Label(artI.getSource() + ",     Le " + artI.getPubdate());
            date_source.setPadding(new Insets(0, 6, 2, 6));
            date_source.setStyle("-fx-background-color: "+ conn.getRssColor(artI.getRssId()) + ";"
                    + "-fx-background-radius: 9,8;"
                    + "-fx-text-fill: WHITE;");
            date_source.setId("Date_source");
            
            Label description = new Label(artI.getDescription());
            description.setPrefWidth(700);
            description.setWrapText(true);
            VBox vbDetails = new VBox(titre, date_source, description);
            addMouseEnter_ExitEvents(scene, vbDetails);
            vbDetails.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        //YOUNES
                    }
                });

            // cr�e l'article en assemblant les composants
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

            // Ajoute l'article et une barre de s�paration � la boite
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
            CheckBox chkBoite = new CheckBox();
            chkBoite.setId("CheckBoiteFlux");
            chkBoite.setSelected(true);
            chkBoite.setPadding(new Insets(0, 10, 0, 0));
            chkBoite.setStyle("-fx-background-color: "+ flux.getColor() + ";"
                    + "-fx-background-radius: 9,8;");
            chkBoite.setOnAction( new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //System.out.println(chkBoite.getText() + chkBoite.isSelected());
                    if(chkBoite.isSelected()) {
                        // S�lectionne les articles du flux et les affiche
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

            // Image en dessous du nom du flux
            ImageView logo = new ImageView(Langage.RESSOURCE_PATH + flux.getChemin());
            logo.setPreserveRatio(true);
            logo.setFitHeight(30);
            
            HBox rssBox = new HBox(chkBoite, logo);
            rssBox.setPadding(new Insets(10, 0, 10, 10));
            rssBox.setSpacing(15);
            
            leftBox.getChildren().add(rssBox);
        }
    }
    
    public void setUser(User u) {
        this.currentUser = u;
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

