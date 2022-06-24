package de.model;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class Artikel {
    private int id;
    private double price;
    private int traderid;


    public Artikel(int id, double price) {
        this.id = id;
        this.price = price;
       
    }

@Override
	public String toString() {
		return "Artikel [id=" + id + ", price=" + price + ", traderid=" + traderid + "]";
	}

public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}






	public double getPrice() {
		return price;
	}






	public void setPrice(double price) {
	 
		this.price =price;
	}






	// Umwandlung von Objekteigenschaften in Jason String
	public JsonValue convertToJason() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("id", id);
        builder.add("price", price);

        return builder.build();
    }
}
