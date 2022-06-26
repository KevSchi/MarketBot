package de.model;

import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class Artikel {
	private int id;
	private double price;
	private List<Integer> supplierIds;
	private int tagcount;
	private int buyMe;

	int lookBackValue = 10;
	private double[] prices = new double[lookBackValue];
	private int index = 0;

	public Artikel(int id, double price) {
		this.id = id;
		this.price = price;
		this.prices[++index] = price;
		this.tagcount = tagcount;
		this.buyMe = buyMe;
		this.supplierIds = new ArrayList<Integer>();
	}

	public void printPrices() {
		for (double price : prices) {
			System.out.println(price);
		}
	}

	public int getBuyMe() {
		return buyMe;
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
		return "Artikel [id=" + id + ", price=" + price + ",  tagcount=" + tagcount
				+ ", buyMe=" + buyMe + "]\n";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setBuyMe(int buyMe) {
		this.buyMe = buyMe;
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
		if (index >= lookBackValue - 1)
			index = 0;
		this.prices[++index] = price;
		this.price = price;
	}

	public List<Integer> getSupplierIds() {
		return supplierIds;
	}

	public void addSupplierId(int supplierId) {
		this.supplierIds.add(supplierId);
	}

	public void removeSupplierId(int supplierId) {
		this.supplierIds.remove(supplierId);
	}

	public boolean containsSupplierId(int supplierId) {
		for (int supplier : supplierIds) {
			if (supplier == supplierId)
				return true;
		}
		return false;
	}

	// Umwandlung von Objekteigenschaften in Jason String
	public JsonValue convertToJason() {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("id", id);
		builder.add("price", price);

		return builder.build();
	}

}
