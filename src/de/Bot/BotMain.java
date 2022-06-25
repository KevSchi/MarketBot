package de.Bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.*;
import java.io.*;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;

import de.model.*;

// Testimports
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

public class BotMain {

	static final String GET_URL_SUPPLIER = "https://hackathon-game.relaxdays.cloud/supplier";
	private static final String GET_URL_Player_Self = "https://hackathon-game.relaxdays.cloud/player/self";
	private static final String GET_URL_Article = "https://hackathon-game.relaxdays.cloud/article";
	private static final String POST_URL_Buy_Article = "https://hackathon-game.relaxdays.cloud/supplier/0/article/0/buy";
	// static String jsonInputString = "{\"Name\": \"Upendra\", \"Eigenschaften\":
	// \"Programmer\"}";
	static String jsonInputString_buy = "{\"count\": 10, \"price_per_unit\": 1}";
	private static final String POST_PARAMS_Buy = jsonInputString_buy;

	// private static final String POST_PARAMS = jsonInputString;
	static List<Listing> listings;
	static List<Artikel> gegenstandListe;

	static String auth = "1" + ":" + "+wUshSS8PNniFaDf7bqEul1Vk9ED/fJt";
	static String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
	public static String authHeaderValue = "Basic " + new String(encodedAuth);

	public static void main(String[] args) throws IOException, InterruptedException {
		// TEst
		DataSource ds = DataSource.getInstance();
		ds.prefillData();
		gegenstandListe = ds.getAlleArtikel();

		while (true) {
			listings = Listing.getListings();
			String Article = getArticle();
			String Test = Methods.getSupplier();
			// System.out.println(Test);

			JsonReader jsonReader = Json.createReader(new StringReader(Test));
			JsonArray objectarr = jsonReader.readArray();
			jsonReader.close();

			// Wandle Json Strin der Artikel um in Json Array/Objekt
			JsonReader jsonReader_art = Json.createReader(new StringReader(Article));
			JsonArray artarr = jsonReader_art.readArray();
			jsonReader.close();

			getMostTags(10, artarr);
			// if(objectarr.size() < gegenstandListe.size()){Adde neue Produkte}

			// System.out.println(objectarr.size());
			// System.out.println(objectarr.getJsonObject(0) + " debugfggg");
			// System.out.println((objectarr.getJsonObject(0).get("stock")) + "debugg");
			// System.out.println((objectarr.getJsonObject(0).get("stock")) + "piusse");
			// System.out.println((objectarr.getJsonObject(0).getJsonArray("stock").getJsonObject(0).get("price")));
			for (int i = 0; i < objectarr.size(); i++) {
				// System.out.println(objectarr.getJsonObject(i)+" debugfggg");
				// System.out.println((objectarr.getJsonObject(i).get("stock"))+"piusse");
				// System.out.println((objectarr.getJsonObject(i).get("stock"))+"piusse");
				JsonArray stock = objectarr.getJsonObject(i).getJsonArray("stock");
				for (int y = 0; y < stock.size(); y++) {
					for (Artikel gegenstand : gegenstandListe) { // für jedes item in items ... hinzufügen json object
						if (gegenstand.getId() == stock.getJsonObject(y).getInt(("article_id"))
								&& stock.getJsonObject(y).getJsonNumber("price").doubleValue() > gegenstand
										.getPrice()) {
							gegenstand.setId(stock.getJsonObject(y).getInt(("article_id"))); // Macht das überhaupt was?
																								// Sollte nicht die
																								// VerkäuferID gesetzt
																								// werden?
							gegenstand.setPrice((stock.getJsonObject(y).getJsonNumber("price").doubleValue()));

						}
						// gegenstand.setPrice((objectarr.getJsonObject(i).getJsonArray("stock").getJsonObject(y).get("price").toString()));
						// gegenstand.setPrice((objectarr.getJsonObject(i).getJsonArray("stock").getJsonObject(y).getJsonNumber("price").doubleValue()));
					}
					// System.out.println((objectarr.getJsonObject(i).getJsonArray("stock").getJsonObject(y).get("article_id")));
					// System.out.println((objectarr.getJsonObject(i).getJsonArray("stock").getJsonObject(y).get("price")));
				}
				// // if preis alt und preis alt 2 kleiner als neuer preis kaufen
			}

			// Klon für die analyse der vorigen Preise
			List<Artikel> gegenstandListe_Trend_voriger_0 = gegenstandListe;

			// Artikel art = new Artikel(objectarr.);
			// System.out.println("GET DONE_supp");

			sendGETplayerSelf();
			// System.out.println("GET DONE");
			// sendPOST();

			// sendPOST_buy(send2);
			System.out.println("POST DONE");
			// System.out.println(send2);

			// System.out.println(gegenstandListe.toString());
			gegenstandListe.get(0).printPrices();
			System.out.println(gegenstandListe.get(0).getMedian());
			TimeUnit.SECONDS.sleep(29);
		}
	}

	private static List<Entry<Integer, Integer>> getMostTags(int n, JsonArray artarr) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();

		for (int j = 0; j < artarr.size(); j++) {
			JsonArray tags = artarr.getJsonObject(j).getJsonArray("tags");
			// for (int y = 0; y < tags.size(); y++) {
			for (Artikel gegenstand : gegenstandListe) { // für jedes item in items ... hinzufügen json object
				if (gegenstand.getId() == artarr.getJsonObject(j).getInt("id")) {
					gegenstand.setTagcount(tags.size());
					map.put(gegenstand.getId(), tags.size());
				}
			}
			// }
		}

		List<Entry<Integer, Integer>> greatest = findGreatest(map, n);
		System.out.println("Top " + n + " entries:");

		for (Entry<Integer, Integer> entry : greatest) {
			System.out.println(entry);
			// System.out.println(entry.getKey());
			// System.out.println(entry.getValue());
		}

		return greatest;
	} // https://stackoverflow.com/questions/21465821/how-to-get-5-highest-values-from-a-hashmap

	private static <K, V extends Comparable<? super V>> List<Entry<K, V>> findGreatest(Map<K, V> map, int n) {
		Comparator<? super Entry<K, V>> comparator = new Comparator<Entry<K, V>>() {

			public int compare(Entry<K, V> e0, Entry<K, V> e1) {
				V v0 = e0.getValue();
				V v1 = e1.getValue();
				return v0.compareTo(v1);
			}
		};
		PriorityQueue<Entry<K, V>> highest = new PriorityQueue<Entry<K, V>>(n, comparator);
		for (Entry<K, V> entry : map.entrySet()) {
			highest.offer(entry);
			while (highest.size() > n) {
				highest.poll();
			}
		}

		List<Entry<K, V>> result = new ArrayList<Map.Entry<K, V>>();
		while (highest.size() > 0) {
			result.add(highest.poll());
		}
		return result;
	}

	private static String getArticle() throws IOException {
		URL obj = new URL(GET_URL_Article);
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
			// System.out.println(response.toString());
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

	/*
	 * private static void sendPOST() throws IOException {
	 * URL obj = new URL(POST_URL);
	 * HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 * con.setRequestMethod("POST");
	 * con.setRequestProperty("Content-Type", "application/json");
	 * con.setRequestProperty("Authorization", authHeaderValue);
	 * // For POST only - START
	 * con.setDoOutput(true);
	 * OutputStream os = con.getOutputStream();
	 * os.write(POST_PARAMS.getBytes());
	 * os.flush();
	 * os.close();
	 * // For POST only - END
	 * 
	 * int responseCode = con.getResponseCode();
	 * System.out.println("POST Response Code :: " + responseCode);
	 * 
	 * if (responseCode == HttpURLConnection.HTTP_OK) { //success
	 * BufferedReader in = new BufferedReader(new InputStreamReader(
	 * con.getInputStream()));
	 * String inputLine;
	 * StringBuffer response = new StringBuffer();
	 * 
	 * while ((inputLine = in.readLine()) != null) {
	 * response.append(inputLine);
	 * }
	 * in.close();
	 * 
	 * // print result
	 * System.out.println(response.toString());
	 * } else {
	 * System.out.println("POST request not worked");
	 * }
	 * }
	 */
	private static void sendPOST_buy(int count, double price_per_unit) throws IOException {
		JsonObjectBuilder builder = Json
				.createObjectBuilder();
		builder.add("count", count);
		builder.add("price_per_unit", price_per_unit);
		JsonObject send1 = builder.build();
		String send = send1.toString();

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
		System.out.println("POST Response Code :: " + responseCode + " " + outputCode);

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