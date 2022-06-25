package de.model;

import java.util.List;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class Artikel {
	private int id;
	private double price;
	private int supplierId;
	private int tagcount;
	private List<Integer> avilableSupplier;

	int lookBackValue = 10;
	private double[] prices = new double[lookBackValue];
	private int index = 0;

	public Artikel(int id, double price) {
		this.id = id;
		this.price = price;
		this.prices[++index] = price;
		this.tagcount = tagcount;
	}

	public void addPrice(double price) {
		// if (index >= lookBackValue)
		// index = 0;
		// this.prices[++index] = price;
	}

	public void printPrices() {
		for (double price : prices) {
			System.out.println(price);
		}
	}

	public double getMedian() {
		int lookBackValueKopie = lookBackValue;

		double erg = 0;
		for (double value : prices) {
			if (value != 0)
				erg += value;
			else
				lookBackValueKopie -= 1;
		}
		return erg / lookBackValueKopie;
	}

	@Override
	public String toString() {
		return "Artikel [id=" + id + ", price=" + price + ", traderid=" + supplierId + ", tagcount=" + tagcount + "]\n";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTagcount(int tagcount) {
		this.tagcount = tagcount;
	}

	public double getPrice() {
		return price;
	}

	public int getTagcount() {
		return tagcount;
	}

	public void setPrice(double price) {
		if (index >= lookBackValue)
			index = 0;
		this.prices[++index] = price;
		this.price = price;
	}

	// Umwandlung von Objekteigenschaften in Jason String
	public JsonValue convertToJason() {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("id", id);
		builder.add("price", price);

		return builder.build();
	}
}
