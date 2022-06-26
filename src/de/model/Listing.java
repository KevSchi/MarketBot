package de.model;

import java.net.*;
import java.util.*;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import de.Bot.BotMain;
import de.model.Artikel;
import java.io.*;

public class Listing {

    private int id;
    private int player;
    private int article;
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
        return article;
    }

    public void setArticle_id(int article) {
        this.article = article;
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

        // URL url = new URL("https://hackathon-game.relaxdays.cloud/listing");
        URL url = new URL(BotMain.BASE_URL + "/listing");
        ;
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
            /*
             * for (Listing listing : listingList) {
             * System.out.println(listing.getArticle_id());
             * }
             */

            return listingList;

        } else {
            System.out.println("GET request not worked");
            return null;
        }

    }

    public static int offer(int article_id, int quantity, double price) throws IOException {
        /**
         * Stellt vorhandene Artikel als Listing zum Kauf zur verfügung
         * POST /listing/new
         * 
         */
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("article", article_id);
        builder.add("count", quantity);
        builder.add("price", price);
        JsonObject jsonObject = builder.build();
        String send = jsonObject.toString();

        URL obj = new URL(BotMain.BASE_URL + "/listing/new");
        // URL obj = new URL("https://hackathon-game.relaxdays.cloud/listing/new");
        HttpURLConnection con = (HttpURLConnection) obj
                .openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", BotMain.authHeaderValue);
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
            return 1;
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
            return 0;
        }
    }

    public static double getBestPrice(int article_Id) {
        double lowestPrice = 100.0;
        for (int i = 0; i < BotMain.listings.size(); i++) {
            if (article_Id == BotMain.listings.get(i).getArticle_id()) {
                if ((BotMain.listings.get(i).getPrice() > 1) && lowestPrice > BotMain.listings.get(i).getPrice()
                        && BotMain.listings.get(i).getPlayer() != BotMain.PLAYER_ID) {
                    lowestPrice = BotMain.listings.get(i).getPrice() - 0.01;
                }
            }
        }
        // System.out.println("-------- id: " + article_Id + " price: " + lowestPrice +
        // "-----------");
        return lowestPrice;
    }

    public static int updateOffer(int article_id, int quantity, double price) throws IOException {
        /**
         * Passt die Eigenschaften eines bereits als Listing zum Kauf angebotenen
         * Artikels an
         * PUT /listing/{id}
         */
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("count", quantity);
        builder.add("price", price);
        JsonObject jsonObject = builder.build();
        String send = jsonObject.toString();

        URL obj = new URL(BotMain.BASE_URL + "/listing/" + article_id);
        HttpURLConnection con = (HttpURLConnection) obj
                .openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", BotMain.authHeaderValue);
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
            return 1;
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
            return 0;
        }
    }

    public static int deleteOffer(int article_id) throws IOException {
        /**
         * Löscht das Angebot eines Artikels aus
         * delete /listing/{id}
         * 
         */
        JsonObjectBuilder builder = Json.createObjectBuilder();

        JsonObject jsonObject = builder.build();
        String send = jsonObject.toString();

        URL obj = new URL(BotMain.BASE_URL + "/listing/" + article_id);
        HttpURLConnection con = (HttpURLConnection) obj
                .openConnection();
        con.setRequestMethod("DELETE");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", BotMain.authHeaderValue);
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
            return 1;
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
            return 0;
        }
    }

}
