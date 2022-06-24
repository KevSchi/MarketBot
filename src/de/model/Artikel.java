package de.model;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class Artikel {
    private String URL = "http://localhost:8080/";
    private int id;
    private String Name;
    private String Eigenschaften;
    private String link;

    public Artikel(int id, String name, String Eigenschaften) {
        this.id = id;
        this.Name = name;
        this.Eigenschaften = Eigenschaften;
        this.link = URL + "Artikel/" + id;
    }



    public String getURL() {
		return URL;
	}



	public void setURL(String uRL) {
		URL = uRL;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getName() {
		return Name;
	}



	public void setName(String name) {
		Name = name;
	}



	public String getEigenschaften() {
		return Eigenschaften;
	}



	public void setEigenschaften(String eigenschaften) {
		Eigenschaften = eigenschaften;
	}



	public String getLink() {
		return link;
	}



	public void setLink(String link) {
		this.link = link;
	}


// Umwandlung von Objekteigenschaften in Jason String
	public JsonValue convertToJason() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("id", id);
        builder.add("Name", Name);
        builder.add("Eigenschaften", Eigenschaften);
        builder.add("link", link);
        return builder.build();
    }
}
