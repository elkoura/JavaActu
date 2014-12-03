package com.pro.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Langage {
    public static String RESSOURCE_PATH;
    public static String ACTUJAVA;
    public static String CONNEXION;
    public static String CREER_COMPTE;
    public static String SOURCES;
    public static String INSCRIPTION;
    public static String NOM;
    public static String EMAIL;
    public static String MDP;
    public static String VALIDER;
    public static String ABIENTOT;
    public static String AUTHENTIFICATION;
    
    public Langage() {
    }
    
    public static void chargerFichierLangue(String nomFichier) {
        try {
            InputStream ips = new FileInputStream(nomFichier); 
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            
            if((RESSOURCE_PATH = br.readLine()) == null)    throw new RuntimeException("BAD TONGUE FILE");
            if((ACTUJAVA = br.readLine()) == null)          throw new RuntimeException("BAD TONGUE FILE");
            if((CONNEXION = br.readLine()) == null)         throw new RuntimeException("BAD TONGUE FILE");
            if((CREER_COMPTE = br.readLine()) == null)      throw new RuntimeException("BAD TONGUE FILE");
            if((SOURCES = br.readLine()) == null)           throw new RuntimeException("BAD TONGUE FILE");
            if((INSCRIPTION = br.readLine()) == null)       throw new RuntimeException("BAD TONGUE FILE");
            if((NOM = br.readLine()) == null)               throw new RuntimeException("BAD TONGUE FILE");
            if((EMAIL = br.readLine()) == null)             throw new RuntimeException("BAD TONGUE FILE");
            if((MDP = br.readLine()) == null)               throw new RuntimeException("BAD TONGUE FILE");
            if((VALIDER = br.readLine()) == null)           throw new RuntimeException("BAD TONGUE FILE");
            if((ABIENTOT = br.readLine()) == null)           throw new RuntimeException("BAD TONGUE FILE");
            AUTHENTIFICATION = br.readLine();       // Pas de vérif. OSEF si il y a qqch après !!

            br.close();
        
        
        
        }		
        catch (RuntimeException | IOException  e){
            System.err.println("Couillonade dans Langage.java !!!!!!!!!!!" + e.getMessage());
            System.exit(-1);
        }
    }
}
