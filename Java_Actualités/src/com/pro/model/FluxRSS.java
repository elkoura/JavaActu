package com.pro.model;

public class FluxRSS {
	
	private int id;
	private String nom;
	private String url;
	
        
        /* Constructeurs de débug !!!!  */
        public FluxRSS() {
            this("toto", "rand="+new java.util.Random().toString());
        }
        
        /* Constructeurs de débug !!!!  */
        public FluxRSS(String nom, String url) {
            this.nom = nom;
            this.url = url;
        }
	
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
	
	

}
