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
import java.util.Locale;

import javax.swing.JTextArea;


public class BDD {

	private  Connection connexion;

	public BDD() {
		loadDatabase();
	}
	/*
	 * inserer un article dans la base de données
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
	 *  inserer un flux rss dans la base de données et returner son id 
	 */
	public int addRss(FluxRSS flux) throws ClassNotFoundException, SQLException,
	InstantiationException, IllegalAccessException {
		int id = 0;
		PreparedStatement req = (PreparedStatement) connexion.prepareStatement(
				"INSERT INTO rssurl VALUES (NULL, ?, ?)",
				Statement.RETURN_GENERATED_KEYS);
		req.setString(1, flux.getNom());
		req.setString(2, flux.getUrl());
		req.executeUpdate();
		ResultSet rs = req.getGeneratedKeys();
		rs.next();
		id = rs.getInt(1);
		req.close();
		

		return id;
	}

	public int addUser(User usr) throws ClassNotFoundException, SQLException,
	InstantiationException, IllegalAccessException {
		int id = 0;
		PreparedStatement req = (PreparedStatement) connexion.prepareStatement(
				"INSERT INTO rssurl VALUES (NULL, ?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS);
		req.setString(1, usr.getNom());
		req.setString(2, usr.getEmail());
		req.setString(3, usr.getPassword());
		req.executeUpdate();
		ResultSet rs = req.getGeneratedKeys();
		rs.next();
		id = rs.getInt(1);
		req.close();
		

		return id;
	}
	
	public ArrayList<Article> articleList() throws SQLException
	{
		ArrayList<Article> listArticles = new ArrayList<>();
		
		String sql = "SELECT titre,description,link,pubdate,source,url_img FROM Articles ORDER BY id DESC LIMIT 10";
		
		final Statement laRequete = connexion.createStatement();
		ResultSet leResultat = laRequete.executeQuery(sql);
		
		while (leResultat.next()) {

			
			final String titre = leResultat.getString(1);
			final String description = leResultat.getString(2);
			final String link = leResultat.getString(3);
			final String pubdate = leResultat.getString(4);
			final String source = leResultat.getString(5);
			final String url_img = leResultat.getString(6);
			Article art = new Article();
			art.setTitre(titre);
			art.setDescription(description);
			art.setLink(link);
			//art.setPubdate(stringDateToSqlDate(pubdate));
			art.setSource(source);
			art.setUrlImage(url_img);
			listArticles.add(art);
		}
		laRequete.close();
		
		return listArticles;
	}
	


	/*
	 * se connecter à la base de données 
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
	
	public void close() throws SQLException
	{
		connexion.close();
	}
	
	public java.sql.Date stringDateToSqlDate(String SDate) { 

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",
				Locale.US);
		java.util.Date d = null;

		try {
			d = formatter.parse(SDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.sql.Date d2 = new Date(d.getTime());
		return (d2);
	}


}