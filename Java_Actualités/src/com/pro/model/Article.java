package com.pro.model;

import java.sql.Date;

public class Article {

    private String titre = null;
    private String description = null;
    private String link = null;
    private Date pubdate = null;
    private String extraire_article = null;
    private String source = null;
    private String urlImage = null;
    private int rssId = 0;


    /* ************ Constructeurs *************
     ****************************************** */
    public Article(int x) {     // Constructeur Bidon de débug
        this(x + "TITRE TEST blabla", x + "DESCRIPTION blablablablablablablablablablablabla",
                x + "http://TESTLINK.mdr", new Date(new Long("1417561746190" + x)), x + "http://URLIMAGE.img", x);
    }

    public Article() {      // Constructeur Bidon de débug
        this("TITRE TEST blabla", "DESCRIPTION blablablablablablablablablablablabla",
                "http://TESTLINK.mdr", new Date(new Long("1417561746190")), "http://URLIMAGE.img", 123);
    }

    public Article(String titre, String description, String link, Date pubdate, String urlImage, int rssId) {
        this.titre = titre;
        this.description = description;
        this.link = link;
        this.pubdate = pubdate;
        this.urlImage = urlImage;
        this.rssId = rssId;
    }

    /* ************ Getters and Setters **************
     ************************************************* */
    public int getRssId() {
        return rssId;
    }

    public void setRssId(int rssId) {
        this.rssId = rssId;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getPubdate() {
        return pubdate;
    }

    public void setPubdate(Date pubdate) {
        this.pubdate = pubdate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getExtraire_article() {
        return extraire_article;
    }

    public void setExtraire_article(String extraire_article) {
        this.extraire_article = extraire_article;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    /* **********   Redéfinitiaon de toString()   ************* */
    @Override
    public String toString() {
        return "RSS" + rssId + " TITRE:" + titre;
    }
}
