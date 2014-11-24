package com.pro.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.pro.model.*;


public class BDD {
	
	private Connection connexion;
	
	public BDD() {
		loadDatabase();
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
			connexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdd_jee", "root", "azert");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}