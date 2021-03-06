package com.pro.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BDD {

    private Connection connexion;

    public BDD() {
        loadDatabase();
    }
    /*
     * inserer un article dans la base de donn�es
     */

    public void addArticle(Article art) throws ClassNotFoundException,
            SQLException, IOException {

        PreparedStatement req = (PreparedStatement) connexion
                .prepareStatement("INSERT INTO articles VALUES (NULL,?,?,?,?,?,?,?,?)");
        req.setString(1, art.getTitre());
        req.setString(2, art.getDescription());
        req.setString(3, art.getLink());
        req.setDate(4, art.getPubdate());
        req.setString(5, art.getSource());
        req.setString(6, art.getExtraire_article());
        req.setInt(7, art.getRssId());
        req.setString(8, art.getUrlImage());
        req.execute();
        req.close();
    }

    /*
     *  inserer un flux rss dans la base de donn�es et returner son id 
     */
    public int getIdRss(String _url) throws ClassNotFoundException, SQLException,
            InstantiationException, IllegalAccessException {
        String sql = "SELECT id FROM rssurl WHERE url = '" + _url + "'";
        final Statement laRequete = connexion.createStatement();
        ResultSet leResultat = laRequete.executeQuery(sql);

        while (leResultat.next()) {
            final int rssid = leResultat.getInt(7);
            return rssid;
        }
        laRequete.close();

        return -1;
    }

    public int addUser(User usr) {
        int id = -1;
        PreparedStatement req;

        try {
            req = (PreparedStatement) connexion.prepareStatement(
                    "INSERT INTO user VALUES (null, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            req.setString(1, usr.getNom());
            req.setString(2, usr.getEmail());
            req.setString(3, usr.getPassword());
            req.executeUpdate();
            ResultSet rs = req.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
            req.close();
        } catch (SQLException e) {
            System.err.println("L'ajout d'un utlisateur A FOIRE !!!! " + e.getMessage());
        }

        return id;
    }

    /* r�cup�re les 10 derniers articles de chaque flux dans la BDD */
    public ArrayList<Article> articleList() throws SQLException {
        ArrayList<Article> listArticles = new ArrayList<>();

        String sql = "SELECT id, titre,description,link,pubdate,source,url_img, rss_id FROM Articles ORDER BY id DESC";

        final Statement laRequete = connexion.createStatement();
        ResultSet leResultat = laRequete.executeQuery(sql);

        while (leResultat.next()) {

            final int    id = leResultat.getInt(1);
            final String titre = leResultat.getString(2);
            final String description = leResultat.getString(3);
            final String link = leResultat.getString(4);
            final String pubdate = leResultat.getString(5);
            final String source = leResultat.getString(6);
            final String url_img = leResultat.getString(7);
            final int rssid = leResultat.getInt(8);
            Article art = new Article();
            art.setId(id);
            art.setTitre(titre);
            art.setDescription(description);
            art.setLink(link);
            art.setPubdate(stringDateToSqlDate(pubdate));
            art.setSource(source);
            art.setUrlImage(url_img);
            art.setRssId(rssid);
            listArticles.add(art);
        }
        laRequete.close();

        return listArticles;
    }

    /* R�cup�re tous les flux de la BDD */
    public ArrayList<FluxRSS> rssList() throws SQLException {

        ArrayList<FluxRSS> listRss = new ArrayList<>();
        String sql = "SELECT id, nom, url, couleur, chemin FROM rssurl";
        final Statement laRequete = connexion.createStatement();
        ResultSet leResultat = laRequete.executeQuery(sql);

        while (leResultat.next()) {
            FluxRSS flux = new FluxRSS();
            flux.setId(leResultat.getInt(1));
            flux.setNom(leResultat.getString(2));
            flux.setUrl(leResultat.getString(3));
            flux.setColor(leResultat.getString(4));
            flux.setChemin(leResultat.getString(5));
            listRss.add(flux);
        }
        laRequete.close();

        return listRss;
    }

    /* R�cup�re la couleur d'un flux � partir de son ID */
    public String getRssColor(int id) {
        List<FluxRSS> listRss = null;

        try {
            listRss = rssList();
        } catch (SQLException e) {
            System.err.println("SQL EXCEPTION !!!!!!!!!!!!!!! ON A PAS PU RECUP rssList -___-");
        }

        for (FluxRSS flux : listRss) {
            if (flux.getId() == id) {
                return flux.getColor();
            }
        }
        return "#000000";    // NOIR
    }

    public User getUserByEmail(String email) throws SQLException {
        PreparedStatement requete;
        ResultSet rs = null;
        if(email == null) return null;

        try {
            requete = (PreparedStatement) connexion.prepareStatement(
                    "SELECT DISTINCT * FROM user WHERE email=?");
            requete.setString(1, email);
            requete.executeQuery();
            rs = requete.getResultSet();
            rs.next();
            return new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }
    
    public boolean userHasFavorite(User user, Article art) throws SQLException {
        if(user == null) return false;
        PreparedStatement requete;
        ResultSet rs = null;

        try {
            requete = (PreparedStatement) connexion.prepareStatement(
                    "SELECT * FROM likearticle WHERE id_user=? AND id_article=?");
            requete.setString(1, "" + user.getId());
            requete.setString(2, "" + art.getId());
            requete.executeQuery();
            rs = requete.getResultSet();
            return rs.first();      // Retourne false si le résultSet est vide
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }
    
    public int userSetFavorite(User user, Article art) {
        int id = -1;
        PreparedStatement req;

        try {
            req = (PreparedStatement) connexion.prepareStatement(
                    "INSERT INTO likearticle VALUES (null, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            req.setInt(1, art.getId());
            req.setInt(2, user.getId());
            req.executeUpdate();
            ResultSet rs = req.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
            req.close();
        } catch (SQLException e) {
            System.err.println("SetFavoris A FOIRE !!!! " + e.getMessage());
        }

        return id;
    }

    public void userUnSetFavorite(User user, Article art) {
        PreparedStatement req;

        try {
            req = (PreparedStatement) connexion.prepareStatement(
                    "DELETE FROM likearticle WHERE id_article=? AND id_user=?",
                    Statement.RETURN_GENERATED_KEYS);
            req.setInt(1, art.getId());
            req.setInt(2, user.getId());
            req.executeQuery();
            req.close();
        } catch (SQLException e) {
            System.err.println("UnSetFavoris A FOIRE !!!! " + e.getMessage());
        }
    }

    
    public boolean articleExisteByTitre(String titre) throws SQLException {
        PreparedStatement requete;
        ResultSet rs = null;

        try {
            requete = (PreparedStatement) connexion.prepareStatement(
                    "SELECT * FROM articles WHERE titre=?");
            requete.setString(1, titre);
            requete.executeQuery();
            rs = requete.getResultSet();
            return rs.first();      // Retourne false si le résultSet est vide
        }
        finally {
            if (rs != null) {
                rs.close();
            }
        }
    }
    
        public String bodyArticle(String _link) throws SQLException {
     String body = null;

        String sql = "SELECT extraire_article FROM Articles WHERE link='"+_link+"'";

        final Statement laRequete = connexion.createStatement();
        ResultSet leResultat = laRequete.executeQuery(sql);

        while (leResultat.next()) {
         body = leResultat.getString(1);
        }
        laRequete.close();

        return body;
    }





    /*
     * se connecter � la base de donn�es 
     */
    private void loadDatabase() {
        // Chargement du driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
        }
        try {
            connexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/javanews", "root", "azert");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() throws SQLException {
        connexion.close();
    }

    public java.sql.Date stringDateToSqlDate(String SDate) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        java.util.Date d = null;

        try {
            d = formatter.parse(SDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        java.sql.Date d2 = new Date(d.getTime());
        return (d2);
    }

}
