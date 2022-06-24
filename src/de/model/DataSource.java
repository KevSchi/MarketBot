package de.model;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

	private static DataSource instance = null;

	private List<Artikel> artikel = new ArrayList<Artikel>();
	private List<Bestellung> bestellungen = new ArrayList<Bestellung>();

	private DataSource() {
	}

	public static DataSource getInstance() {
		if (instance == null) {
			instance = new DataSource();
		}
		return instance;
	}
	public void prefillData() {
		for (int i=0;i<139;i++) {
		     this.artikelHinzufuegen(i, 0);
		      }
		/*this.artikelHinzufuegen("Ball", "Rund");
		this.artikelHinzufuegen("Computer", "Schnell");
		//artikel.add(new Artikel(3,"test","Test"));	
		this.bestellungAnlegen("Buchung 1");
		this.addArtToOrder(1, 1, 1);
		this.addArtToOrder(1, 2, 20);
		this.bestellungAnlegen("Buchung 2");
		this.addArtToOrder(2, 1, 2);
		this.addArtToOrder(2, 2, 40);
		this.bestellungAnlegen("Buchung 3");
		this.addArtToOrder(3, 2, 4);
		this.addArtToOrder(3, 1, 17);
		//System.out.println(artikel); // print arraylist*/
		for (Artikel i : artikel) {
		      System.out.println(i); 
		      artikel.get(0).toString();    	
		     // Print ArrayList		           
		      }
		artikel.toString();
	}

	public Artikel artikelHinzufuegen(int id, float price) {
		Artikel gegenstand = new Artikel(id, price);
		artikel.add(gegenstand);
		//System.out.println(gegenstand); 
		//gegenstand.toString();
		return gegenstand;
	}

	public Artikel artikelNachId(int id) {
		for (Artikel gegenstand : artikel) {
			if (gegenstand.getId() == id) {
				return gegenstand;
			}
		}
		return null;
	}

	public List<Artikel> getAlleArtikel() {
		return artikel;
	}

	public Bestellung bestellungAnlegen(String name) {
		Bestellung bestellung = new Bestellung(bestellungen.size() + 1, name);
		bestellungen.add(bestellung);
		return bestellung;
	}

	public Bestellung bestellungNachId(int id) {
		for (Bestellung bestellung : bestellungen) {
			if (bestellung.getId() == id) {
				return bestellung;
			}
		}
		return null;
	}

	public List<Bestellung> getAlleBestellungen() {
		return bestellungen;
	}
	
	public void addArtToOrder(int idorder, int idart, int count) {
		Bestellung bestellung = getOrderById(idorder);
		if(bestellung == null) return;
		Artikel artikel = artikelNachId(idart);
		if(artikel == null) return;
		bestellung.hinzufuegenArtikel(artikel, count);
		//System.out.println("Hello WOrld");
	}
	
	private Bestellung getOrderById(int id) {
		for(Bestellung bestellung : bestellungen) {
			if(bestellung.getId() == id)
				return bestellung;
		}
		System.out.println("returned 0 inside getOrderbyID ");
		return null;
	}
}
