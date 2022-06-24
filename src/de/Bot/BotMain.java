package de.Bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;

import de.model.DataSource;
import de.model.Artikel;
import de.model.Bestellung.Bestellformular;

public class BotMain {
	
	private static final String USER_AGENT = "Mozilla/5.0";

	private static final String GET_URL = "https://hackathon-game.relaxdays.cloud/supplier";
	private static final String GET_URL_Player_Self = "https://hackathon-game.relaxdays.cloud/player/self";
	private static final String POST_URL = "http://localhost:8081/Artikel";
	private static final String POST_URL_Buy_Article = "https://hackathon-game.relaxdays.cloud/supplier/0/article/0/buy";
	//static 	String jsonInputString = "{\"Name\": \"Upendra\", \"Eigenschaften\": \"Programmer\"}";
	static 	String jsonInputString_buy = "{\"count\": 10, \"price_per_unit\": 1}";

	//private static final String POST_PARAMS = jsonInputString;
	private static final String POST_PARAMS_Buy = jsonInputString_buy;
	static String auth = "1" + ":" + "+wUshSS8PNniFaDf7bqEul1Vk9ED/fJt";
	static String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
	static String authHeaderValue = "Basic " + new String(encodedAuth);

	public static void main(String[] args) throws IOException {
		DataSource ds = DataSource.getInstance();
	    ds.prefillData();
	    List<Artikel> gegenstandListe = ds.getAlleArtikel();

		String Test = sendGET();
		System.out.println(Test);

		JsonReader jsonReader = Json.createReader(new StringReader(Test));
		JsonArray objectarr = jsonReader.readArray();
		jsonReader.close();
		System.out.println(objectarr.toString()+" debug");
		
		objectarr.getJsonObject(0);
		
		objectarr.size();
		System.out.println(objectarr.size());
		System.out.println(objectarr.getJsonObject(0)+" debugfggg");
		System.out.println((objectarr.getJsonObject(0).get("stock"))+"debugg");
		System.out.println((objectarr.getJsonObject(0).get("stock"))+"piusse");
		System.out.println((objectarr.getJsonObject(0).getJsonArray("stock").getJsonObject(0).get("price")));
		for(int i =0;i<objectarr.size();i++) {
			//System.out.println(objectarr.getJsonObject(i)+" debugfggg");
			//System.out.println((objectarr.getJsonObject(i).get("stock"))+"piusse");
			//System.out.println((objectarr.getJsonObject(i).get("stock"))+"piusse");
			for(int y =0;y<objectarr.getJsonObject(i).getJsonArray("stock").size();y++) {
				
				
				for (Artikel gegenstand : gegenstandListe) { // für jedes item in items ... hinzufügen json object
					if (gegenstand.getId()==objectarr.getJsonObject(i).getJsonArray("stock").getJsonObject(y).getInt(("article_id"))&&objectarr.getJsonObject(i).getJsonArray("stock").getJsonObject(y).getJsonNumber("price").doubleValue()>gegenstand.getPrice()) {
			          gegenstand.setId(objectarr.getJsonObject(i).getJsonArray("stock").getJsonObject(y).getInt(("article_id")));
			          gegenstand.setPrice((objectarr.getJsonObject(i).getJsonArray("stock").getJsonObject(y).getJsonNumber("price").doubleValue()));
					}
								          //gegenstand.setPrice((objectarr.getJsonObject(i).getJsonArray("stock").getJsonObject(y).get("price").toString()));
					
			         // gegenstand.setPrice((objectarr.getJsonObject(i).getJsonArray("stock").getJsonObject(y).getJsonNumber("price").doubleValue()));
			        }			
			//	System.out.println((objectarr.getJsonObject(i).getJsonArray("stock").getJsonObject(y).get("article_id")));				
			//	System.out.println((objectarr.getJsonObject(i).getJsonArray("stock").getJsonObject(y).get("price")));
				
			}
	
			if preis alt und preis alt 2 kleiner als neuer preis kaufen
		}
		// Klon für die analyse der vorigen Preise		
		List<Artikel> gegenstandListe_trend =gegenstandListe;
	
		
		//Artikel art = new Artikel(objectarr.);
		System.out.println("GET DONE_supp");
		sendGETplayerSelf();
		System.out.println("GET DONE");
		//sendPOST();
		
	     JsonObjectBuilder builder = Json.createObjectBuilder();
	        builder.add("count", 2);
	        builder.add("price_per_unit", 17.592428);
	        JsonObject send = builder.build();
	        String send2 = send.toString();
	        sendPOST_buy(send2);
	        System.out.println("POST DONE");
	        System.out.println(send2);
	      //  System.out.println(ds.artikelNachId(1).toString());
			System.out.println(gegenstandListe.toString());
	}

	private static String sendGET() throws IOException {
		URL obj = new URL(GET_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", authHeaderValue);
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
			return response.toString();
			
		} else {
			System.out.println("GET request not worked");
		}
		return "error";

	}
	private static void sendGETplayerSelf() throws IOException {
		URL obj = new URL(GET_URL_Player_Self);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", authHeaderValue);
		int responseCode = con.getResponseCode();
		String outputCode = con.getResponseMessage();
		System.out.println("GET Response Code :: " + responseCode + outputCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("GET request not worked");
		}

	}

/*	private static void sendPOST() throws IOException {
		URL obj = new URL(POST_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", authHeaderValue);
		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(POST_PARAMS.getBytes());
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("POST request not worked");
		}
	}
	*/
	private static void sendPOST_buy(String send) throws IOException {
		URL obj = new URL(POST_URL_Buy_Article);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", authHeaderValue);
		con.setRequestProperty("Accept", "*/*");
		con.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
		con.setRequestProperty("Connection", "keep-alive");
		
		
		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(send.getBytes());
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();
		String outputCode = con.getResponseMessage();
		System.out.println("POST Response Code :: " + responseCode + " "+ outputCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} else {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getErrorStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println(response.toString());
			System.out.println("POST request not worked");
		}
	}

}