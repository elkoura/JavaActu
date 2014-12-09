package com.pro.model;

public class FluxRSS {

    private int id;
    private String nom;
    private String url;
    private String color;

    
    /* ************ Constructeurs *************
     ****************************************** */
    public FluxRSS() {
        this("toto", "rand=" + new java.util.Random().toString(), 0);
    }

    public FluxRSS(int x) {
        this("toto", "rand=" + new java.util.Random().toString(), x);
    }
    /* Constructeurs de débug !!!!  */

    public FluxRSS(String nom, String url, int id) {
        this.id = id;
        this.nom = nom;
        this.url = url;
    }

    /* ************ Getters and Setters **************
     ************************************************* */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
