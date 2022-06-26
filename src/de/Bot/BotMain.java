package de.Bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.*;

import javax.json.Json;
import javax.json.JsonArray;
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

	public static final String BASE_URL = "http://192.168.178.47:8080";
	static final String GET_URL_SUPPLIER = BASE_URL + "/supplier";
	private static final String GET_URL_Player_Self = BASE_URL + "/player/self";
	public static final int PLAYER_ID = 0;

	// private static final String GET_URL_Article =
	// "https://hackathon-game.relaxdays.cloud/article";

	private static final String GET_URL_Article = BASE_URL + "/article";

	static String jsonInputString_buy = "{\"count\": 10, \"price_per_unit\": 1}";
	private static final String POST_PARAMS_Buy = jsonInputString_buy;
	public static List<Listing> listings;
	public static List<Artikel> articleFromSupplier;

	static String authRelexdays = PLAYER_ID + ":" + "+wUshSS8PNniFaDf7bqEul1Vk9ED/fJt";
	static String auth = PLAYER_ID + ":" + "jPjGYW7ErWG0knJB5a6Gxih+tHi2E5hT";

	// static String encodedAuth =
	// Base64.getEncoder().encodeToString(authRelaxdays.getBytes());
	static String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
	public static String authHeaderValue = "Basic " + new String(encodedAuth);
	public static String SelfInfo = "0";
	public static double MyCash = 0;
	public static int runCount = 1;
	public static String Article;
	public static String Supplier;

	public static void main(String[] args) throws IOException, InterruptedException {

		DataSource ds = DataSource.getInstance();
		ds.prefillData();

		articleFromSupplier = ds.getAlleArtikel();

		while (true) {
			listings = Listing.getListings();
			Article = getArticle();
			Supplier = Methods.getSupplier();

			JsonReader jsonReader = Json.createReader(new StringReader(Supplier));
			JsonArray supplierArray = jsonReader.readArray();
			jsonReader.close();

			// Wandle Json Strin der Artikel um in Json Array/Objekt
			JsonReader jsonReader_art = Json.createReader(new StringReader(Article));
			JsonArray articleArray = jsonReader_art.readArray();
			jsonReader.close();

			// System.out.println("BotMain - articleArray.size(): " + articleArray.size());

			for (int i = 0; i < articleArray.size(); i++) {
				Listing.getBestPrice(i);
			}

			// getMostTags(10, artarr);

			Methods.updatePrices(supplierArray);

			// Einlesen self Infostring und convert 2 Json um Cash auszulesen

			Methods.chooseWhatToBuy();

			SelfInfo = sendGETplayerSelf();
			JsonReader jsonReader_cash = Json.createReader(new StringReader(SelfInfo));
			JsonArray Self_json = jsonReader_cash.readArray();
			jsonReader.close();
			MyCash = Self_json.getJsonObject(0).getJsonNumber("money").doubleValue();

			System.out.println("Der Bot lief " + runCount++ + " Mal durch");
			System.out.println("Dies ist mein Geld:  " + MyCash);

			// [{"id":1,"money":0.8045645,"stock":[{"article_id":0,"stock":1},{"article_id":12,"stock":7},{"article_id":33,"stock":1},{"article_id":34,"stock":132},{"article_id":49,"stock":80},{"article_id":67,"stock":109},{"article_id":84,"stock":198},{"article_id":96,"stock":254}]}]

			// https://hackathon-game.relaxdays.cloud/player/self

			JsonArray itemsInStock = Self_json.getJsonObject(0).getJsonArray("stock");

			for (int index = 0; index < itemsInStock.size(); index++) {
				int artid = itemsInStock.getJsonObject(index).getJsonNumber("article_id").intValue();
				int quantity = itemsInStock.getJsonObject(index).getJsonNumber("stock").intValue();
				// price_per_unit =
				// stock.getJsonObject(index).getJsonNumber("price").doubleValue();

				double median = 0;
				for (Artikel gegenstand : articleFromSupplier) {
					if (gegenstand.getId() == artid) {
						median = gegenstand.getMedian();
					}
				}
				if (Listing.getBestPrice(artid) == 1) {
					Listing.offer(artid, quantity, Listing.getBestPrice((int) median));
				} else {
					Listing.offer(artid, quantity, Listing.getBestPrice(artid));
				}

				// System.out.println(Listing.getBestPrice(artid) + " Werde ich hier auch
				// arm?");
			}

			TimeUnit.SECONDS.sleep(30);

		}
	}

	static List<Entry<Integer, Integer>> getMostTags(int n, JsonArray artarr) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();

		for (int j = 0; j < artarr.size(); j++) {
			JsonArray tags = artarr.getJsonObject(j).getJsonArray("tags");
			// for (int y = 0; y < tags.size(); y++) {
			for (Artikel gegenstand : articleFromSupplier) { // für jedes item in items ... hinzufügen json object
				if (gegenstand.getId() == artarr.getJsonObject(j).getInt("id")) {
					gegenstand.setTagcount(tags.size());
					map.put(gegenstand.getId(), tags.size());
				}
			}
			// }
		}

		List<Entry<Integer, Integer>> greatest = findGreatest(map, n);
		// System.out.println("Top " + n + " entries:");

		for (Entry<Integer, Integer> entry : greatest) {
			// System.out.println(entry);
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

	static String getArticle() throws IOException {
		URL obj = new URL(GET_URL_Article);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", authHeaderValue);
		int responseCode = con.getResponseCode();
		// System.out.println("GET Response Code :: " + responseCode);
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

	private static String sendGETplayerSelf() throws IOException {
		URL obj = new URL(GET_URL_Player_Self);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", authHeaderValue);
		int responseCode = con.getResponseCode();
		String outputCode = con.getResponseMessage();
		// System.out.println("GET Response Code :: " + responseCode + outputCode);
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
			return response.toString();
		} else {
			System.out.println("GET request not worked");
		}
		return null;

	}

}