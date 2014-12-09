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
    public static String AUTHENTIFICATION;
    public static String NOM;
    public static String EMAIL;
    public static String MDP;
    public static String CONFIRM_MDP;
    public static String VALIDER;
    public static String ANNULER;
    public static String ERR_CONFIRM_MDP;
    public static String ERR_CHAMP_VIDE;
    public static String ERR_CONNECTION;
    public static String CONNECTE;
    public static String ABIENTOT;
    
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
            if((AUTHENTIFICATION = br.readLine()) == null)  throw new RuntimeException("BAD TONGUE FILE");
            if((NOM = br.readLine()) == null)               throw new RuntimeException("BAD TONGUE FILE");
            if((EMAIL = br.readLine()) == null)             throw new RuntimeException("BAD TONGUE FILE");
            if((MDP = br.readLine()) == null)               throw new RuntimeException("BAD TONGUE FILE");
            if((CONFIRM_MDP = br.readLine()) == null)       throw new RuntimeException("BAD TONGUE FILE");
            if((VALIDER = br.readLine()) == null)           throw new RuntimeException("BAD TONGUE FILE");
            if((ANNULER = br.readLine()) == null)           throw new RuntimeException("BAD TONGUE FILE");
            if((ERR_CONFIRM_MDP = br.readLine()) == null)   throw new RuntimeException("BAD TONGUE FILE");
            if((ERR_CHAMP_VIDE = br.readLine()) == null)    throw new RuntimeException("BAD TONGUE FILE");
            if((ERR_CONNECTION = br.readLine()) == null)    throw new RuntimeException("BAD TONGUE FILE");
            if((CONNECTE = br.readLine()) == null)          throw new RuntimeException("BAD TONGUE FILE");
            ABIENTOT = br.readLine();               // P as de vérif. OSEF si il y a qqch après !!

            br.close();
        }		
        catch (RuntimeException | IOException  e){
            System.err.println("Couillonade dans Langage.java !!!!!!!!!!!" + e.getMessage());
            System.exit(-1);
        }
    }
}
