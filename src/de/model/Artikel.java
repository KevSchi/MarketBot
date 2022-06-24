package de.model;

import java.util.List;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class Artikel {
    private int id;
    private double price;
    private int traderid;

	int lookBackValue = 10;
	private double[] prices = new double[lookBackValue];
	private int index = 0;

    public Artikel(int id, double price) {
        this.id = id;
        this.price = price;
		this.prices[++index] = price;
    }

	public void addPrice(double price){
		if(index >= 10) index = 0;
		this.prices[++index] = price;
	}

	public double getMedian(){
		//problematisch wäre es solange Prices nichts voll ist if(value != 0) erg+=value; else lookBackValueKopie - 1?
		double erg = 0;
		for(double value : prices){
			erg += value;
		}
		return erg/lookBackValue;
	}

@Override
	public String toString() {
		return "Artikel [id=" + id + ", price=" + price + ", traderid=" + traderid + "]\n";
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
