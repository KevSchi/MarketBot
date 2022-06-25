package de.model;

import java.net.*;
import java.util.*;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import de.model.Artikel;
import java.io.*;

public class Listing {

    private int id;
    private int player;
    private int article_id;
    private int count;
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public static List<Listing> getListings() throws IOException {

        URL url = new URL("https://hackathon-game.relaxdays.cloud/listing");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;

            inputLine = in.readLine();

            TypeToken<List<Listing>> token = new TypeToken<List<Listing>>() {
            };
            Gson gson = new Gson();
            List<Listing> listingList = gson.fromJson(inputLine, token.getType());
            in.close();

            return listingList;

        } else {
            System.out.println("GET request not worked");
            return null;
        }

    }

    public static void getMissingArticle(List<Listing> listings, List<Artikel> articles) {
        for (int i = 0; i < articles.size(); i++) {
            for (int j = 0; j < listings.size(); j++) {
                // if(listings.get(j).getArtikel(j))
            }
        }
    }

}
