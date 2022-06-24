package de.model;

import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class Bestellung {  
    private int id;
    private String Name;
    private List<Bestellformular> Bestellte_Artikel = new ArrayList<Bestellformular>();
    private String link;
    private String URL = "http://localhost:8081/";
    
    public class Bestellformular {
        private Artikel Art;
        private int menge;
        public Bestellformular(Artikel Art, int quant) {
        this.setArt(Art);
        this.menge = quant;
        }
		public Artikel getArt() {
			return Art;
		}
		public void setArt(Artikel art) {
			Art = art;
		}
		public int getMenge() {
			// TODO Auto-generated method stub
			return this.menge;
		}
    }
    public Bestellung(int id, String name) {
       this.id = id;
       this.Name = name;
       this.link = URL + "Bestellungen/" + id;
    }

    public int getId() {
        return id;
    }

    public String getname() {
        return Name;
    }

    public List<Bestellformular> getBestellung() {
        return Bestellte_Artikel;
    }

    public void hinzufuegenArtikel(Artikel item, int quant) {
        for (Bestellformular Bestellter_Artikel : Bestellte_Artikel) {
            if (Bestellter_Artikel.getArt().getId() == item.getId()) {
            	Bestellter_Artikel.menge += quant;
                return;
            }
        }
        Bestellte_Artikel.add(new Bestellformular(item, quant));
    }

    public JsonValue convertToJason() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("Link", link);  
        builder.add("id", id);
        builder.add("Name", Name);
    
        JsonArrayBuilder array_builder = Json.createArrayBuilder();
        for (Bestellformular Bestellter_Artikel : Bestellte_Artikel) {
            JsonObjectBuilder item_builder = Json.createObjectBuilder();
            item_builder.add("id", Bestellter_Artikel.getArt().getId());
            item_builder.add("Name", Bestellter_Artikel.getArt().getPrice());
           // item_builder.add("Beschreibung", Bestellter_Artikel.getArt().getEigenschaften());
            item_builder.add("Menge", Bestellter_Artikel.menge);
            array_builder.add(item_builder.build());
        }
        builder.add("Bestellte_Artikel", array_builder.build());					// Hinzufügen von Bestellte_Artikel: dann Array Artikel
        return builder.build();
    }

}
