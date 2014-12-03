package com.pro.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import org.xml.sax.SAXException;

import com.pro.model.*;

public class RssFeed {

	// parser les flux rss
	private Article article;
	private FluxRSS rss;

	public RssFeed()
	{
		article = new Article();
		rss = new FluxRSS();
	}


	public String parseRss(String _nom, String _url) 
			throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

		BDD conn = new BDD();
		rss.setNom(_nom);
		rss.setUrl(_url);
		rss.setId(conn.addRss(rss));

		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();

			URL url = new URL(rss.getUrl());
			Document doc = builder.parse(url.openStream());
			NodeList nodes = null;
			Node element = null;

			/*
			 * Titre du flux
			 */
			nodes = doc.getElementsByTagName("channel");
			Node node = doc.getDocumentElement();
			System.out.println(" methode parse 1 --> Flux RSS: "
					+ this.readNode(node, "channel|title"));
			System.out.println();

			/* 
			 * elements du flux rss
			 */

			nodes = doc.getElementsByTagName("item");
			int l = 0;
			for (int i = 0; i < nodes.getLength(); i++) {
				
				element = nodes.item(i);
				
				/*
				 * extraire la partie du date pour construire le SQL.date
				 */
				String pubDateString = readNode(element, "pubDate");
				pubDateString = pubDateString.replaceAll(" ", "");
				pubDateString = pubDateString.substring(4, 6) + "-"
						+ pubDateString.substring(6, 9) + "-"
						+ pubDateString.substring(9, 13);

				/* 
				 * construire les champs de l'aticle
				 */
				
				
				article.setTitre(this.readNode(element, "title"));
				article.setDescription(this.readNode(element, "description").trim());
				article.setLink(this.readNode(element, "link"));
				article.setPubdate(stringDateToSqlDate(pubDateString));
				article.setExtraire_article(extractLink(article.getLink()));
				article.setSource(rss.getNom());
				article.setUrlImage(extractUrlImage(article.getLink()));
				article.setRssId(rss.getId());
				
				/* -------------------------------------------------- */
				
				
				System.out.println("Titre: " + readNode(element, "title"));
				System.out.println("Lien: " + readNode(element, "link"));
				System.out.println("Description: " + readNode(element, "description").trim());
				

				/* ******** découper l'url et extraire que la premier partie 
				 *   pour la source
				 * ex : http:\\lefigaro.fr
				 */

				String s = readNode(element, "link");
				int j = 0;
				int compte = 0;

				while (compte < 3) {
					if (s.charAt(j) == '/')
						compte++ ;
					j++;
				}

				System.out.println("Source: " + s.substring(0, j - 1));
				System.out.println("Date de publication: " + readNode(element, "pubDate"));
				System.out.println(pubDateString);
				System.out.println("le corps :  " +extractLink(article.getLink()));
				System.out.println(article.getUrlImage());
				
				
				//System.out.println(extractLink(article.getLink()));
				//System.out.println("le corps de l'article"+ readNode(element, "content:encoded")+"\n\n");

				//article.setExtraire_article(extractLink(article.getLink()));
				
				//article.setExtraire_article("vide pour le test");
				//article.setUrlImage(extractUrlImage(article.getLink()));
				//article.setRssId(rss.getId());

				conn.addArticle(article);
				l = l + 1;
			}



		} catch (SAXException ex) {
			System.out.println("erreur SAXException");
		} catch (IOException ex) {
			System.out.println("erreur IOException");
		} catch (ParserConfigurationException ex) {
			System.out.println("erreur ParserConfigurationException");
		}
		
		finally{
			conn.close();
		}

		return null;
	}


	public String readNode(Node _node, String _path) {

		String[] paths = _path.split("\\|");// divise la chaine paths en
		// fonction \\
		Node node = null;

		if (paths != null && paths.length > 0) {
			node = _node;

			for (int i = 0; i < paths.length; i++) {
				node = getChildByName(node, paths[i]);// paths[i].trim() pour
				// travailler sur copie
				// de chaine de
				// caractere
			}
		}

		if (node != null) {
			return node.getTextContent();
		} else {
			return "";
		}
	}

	public Node getChildByName(Node _node, String _name) {
		if (_node == null) {
			return null;
		}
		NodeList listChild = _node.getChildNodes();

		if (listChild != null) {
			for (int i = 0; i < listChild.getLength(); i++) {
				Node child = listChild.item(i);
				if (child != null) {
					if ((child.getNodeName() != null && (_name.equals(child
							.getNodeName())))
							|| (child.getLocalName() != null && (_name
									.equals(child.getLocalName())))) {
						return child;
					}
				}
			}
		}
		return null;
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


	// Extraction du body de l'article 
	public String extractLink(String _link) throws IOException {
		org.jsoup.nodes.Document document = Jsoup.connect(_link).get();

		Element ele, ele1, ele2, ele3, ele4, ele5, ele6, ele7, ele8, ele9, ele10, ele11, ele12, ele13, ele14, ele15,ele16 = null;

		ele = document.select("div .texte").first(); // le parisien
		ele1 = document.select("div .texte-global clearfix").first();
		ele2 = document.select("div .texte-global").first();
		ele3 = document.select("div .articleBody").first(); // l'équipe
		ele4 = document.select("div .article_content").first();
		ele5 = document.select("div .mna-body").first();
		ele6 = document.select("div .fig-article-body").first(); // le figaro
		ele7 = document.select("div .contenu_article").first();
		ele8 = document.select("div .mod").select("div .article-body mod")
				.first();
		ele9 = document.select("div .entry-content").first();
		ele10 = document.select("div .content.clearfix").first();
		ele11 = document.select("div .article-body.mod").first();
		ele12 = document.select("div .paragr.paragraf1").first();
		ele13 = document.select("div .paragr.paragraf2.paragimage").first();
		ele14 = document.select("div .paragr.paragraf2").first();
		ele15 = document.select("div .figaro-content-body-col").first();
		ele16 = document.select("div .entry").first(); // pour le site lapresse.ca

		if (ele != null) {
			String text = ele.text();
			return text;
		}
		if (ele1 != null) {
			String texte1 = ele1.text();
			return texte1;
		}
		if (ele2 != null) {
			String texte2 = ele2.text();
			return texte2;
		}
		if (ele3 != null) {
			String texte2 = ele3.text();
			return texte2;
		}
		if (ele4 != null) {
			String texte2 = ele4.text();
			return texte2;
		}
		if (ele5 != null) {
			String texte2 = ele5.text();
			return texte2;
		}
		if (ele6 != null) {
			String texte2 = ele6.text();
			return texte2;
		}
		if (ele7 != null) {
			String texte2 = ele7.text();
			return texte2;
		}
		if (ele8 != null) {
			String texte2 = ele8.text();
			return texte2;
		}
		if (ele9 != null) {
			String texte2 = ele9.text();
			return texte2;
		}
		if (ele10 != null) {
			String texte2 = ele10.text();
			return texte2;
		}
		if (ele11 != null) {
			String texte2 = ele11.text();
			return texte2;
		}
		if (ele12 != null || ele13 != null || ele14 != null) {
			StringBuffer texte = new StringBuffer(ele12.text());
			try {
				texte = texte.append(ele14.text());
				texte = texte.append(ele13.text());
			} catch (NullPointerException e) {
			}

			return texte.toString();
		}
		if (ele15 != null) {
			String texte2 = ele15.text();
			return texte2;
		}
		if (ele16 != null) {
			String texte2 = ele16.text();
			return texte2;
		}else {
			return null;
		}
	}

	public String extractUrlImage(String _link) throws IOException {
		org.jsoup.nodes.Document document = Jsoup.connect(_link).get();
		Element image = null;
		Element imageNYT = null;
		Element imageNYT2 = null;
		Element imageNYT3 = null;
		Element imageNYT4 = null;
		Element imageLiberation = null;
		Element imageFigaro = null;
		Element imageMonde = null;
		Element imageM = null;
		Element imageHumanite = null;
		Element imageRue89 = null;
		Element imageEchos = null;
		Element imageEchos2 = null;
		Element image20Minutes = null;
		Element imageEquipe = null;
		Element imageLactualite = null;

		image = document.select("div .visuelMain img").first();
		imageNYT = document.select("div .image a img").first();
		imageNYT2 = document.select(
				"span[itemtype = http://schema.org/ImageObject] img").first();
		imageNYT3 = document.select("div .w592 img").first();
		imageNYT4 = document.select("div .w190.right img").first();
		imageLiberation = document.select(
				"figure[itemtype = http://schema.org/ImageObject] img").first();
		imageFigaro = document.select("div .fig-main-col .fig-main-media")
				.select(".fig-photo img").first();
		imageMonde = document.select("div .contenu_article")
				.select(".illustration_haut img").first();
		imageM = document.select("div .wp-caption aligncenter")
				.select("img .size-large wp-image-3647").first();
		imageHumanite = document.select("div .article_content a img").first();
		imageRue89 = document.select("div .image.fullsize").select("a[img]")
				.first();// marche po
		imageEchos = document.select("div .g-photo a img").first();
		imageEchos2 = document.select("div .m-photo.Left a img").first();
		image20Minutes = document.select("div .mna-image aside img").first();
		imageEquipe = document.select("div .bigleaderpix img").first();
		imageLactualite = document.select("div .image-block").select("img").first();

		if (image != null) {
			String url = image.absUrl("src");
			return url;
		}
		if (imageNYT != null) {
			String url = imageNYT.absUrl("src");
			return url;
		}
		if (imageLiberation != null) {
			String url = imageLiberation.absUrl("src");
			return url;
		}
		if (imageFigaro != null) {
			String url = imageFigaro.absUrl("src");
			return url;
		}
		if (imageMonde != null) {
			String url = imageMonde.absUrl("src");
			return url;
		}
		if (imageM != null) {
			String url = imageM.absUrl("src");
			return url;
		}
		if (imageHumanite != null) {
			String url = imageHumanite.absUrl("src");
			return url;
		}
		if (imageRue89 != null) {
			String url = imageRue89.absUrl("src");
			return url;
		}
		if (imageEchos != null) {
			String url = imageEchos.absUrl("src");
			return url;
		}
		if (imageEchos2 != null) {
			String url = imageEchos2.absUrl("src");
			return url;
		}
		if (image20Minutes != null) {
			String url = image20Minutes.absUrl("src");
			return url;
		}
		if (imageNYT2 != null) {
			String url = imageNYT2.absUrl("src");
			return url;
		}
		if (imageNYT3 != null) {
			String url = imageNYT3.absUrl("src");
			return url;
		}
		if (imageNYT4 != null) {
			String url = imageNYT4.absUrl("src");
			return url;
		}
		if (imageEquipe != null) {
			String url = imageEquipe.absUrl("src");
			return url;
		}
		if (imageLactualite != null) {
			String url = imageLactualite.absUrl("src");
			return url;
		}else {
			return null;
		}
	}
	
//	public void affiche() throws SQLException
//	{
//		BDD conn = new BDD();
//		 ArrayList<Article> articleList = conn.articleList();
//		 
//		 for(Article artI : articleList)
//		 {
//			 System.out.println(artI.getTitre());
//		 }
//	}


	public static void main(String[] args) throws ClassNotFoundException,
	SQLException, InstantiationException, IllegalAccessException {

		RssFeed r = new RssFeed();

		r.parseRss("Le parisien",
				"http://www.lequipe.fr/rss/actu_rss.xml"); // OK
		
		


	}


}