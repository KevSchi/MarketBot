package de.Bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.*;
import java.util.*;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import de.model.Artikel;

public class Methods {
    private static final double buy_price = 1.0;
    private static final String POST_URL_Buy_Article = "https://hackathon-game.relaxdays.cloud/supplier/0/article/0/buy";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLACK = "\u001B[30m";

    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";

    public static String getSupplier() throws IOException {
        URL obj = new URL(BotMain.GET_URL_SUPPLIER);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", BotMain.authHeaderValue);
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

    // Prüfen ob Artikel 60 Prozent billiger sind als der Median, Markieren zum
    // Kaufen
    public static void chooseWhatToBuy() throws IOException, InterruptedException {
        Map<Integer, Artikel> uniqueArtikel = new HashMap<Integer, Artikel>();

        double cash = BotMain.MyCash;

        for (Artikel article : BotMain.articleFromSupplier) {

            if (!uniqueArtikel.containsKey(article.getId()) && article.getPrice() <= buy_price) {

                uniqueArtikel.put(article.getId(), article);
            }
        }
        double moneyToSpendPerBuy = cash / uniqueArtikel.size();
        for (Artikel article : uniqueArtikel.values()) {
            buy(article, moneyToSpendPerBuy);
        }

    }

    public static int buy(Artikel article, double money) throws IOException, InterruptedException {
        /**
         * Kann man Dokumentation in Java automatisch generieren?
         */
        // Aktuellen Preis einholen
        double price_per_unit = 0;
        int quantity = 0;
        int supplierStock = 0;
        int article_id = article.getId();
        // article.getSupplierIds()

        String Supplier = Methods.getSupplier();
        JsonReader jsonReader = Json.createReader(new StringReader(Supplier));
        JsonArray supplier_arr = jsonReader.readArray();
        jsonReader.close();

        for (int i = 0; i < supplier_arr.getJsonObject(article.getSupplierIds().get(0)).getJsonArray("stock")
                .size(); i++) {
            if (supplier_arr.getJsonObject(article.getSupplierIds().get(0)).getJsonArray("stock").getJsonObject(i)
                    .getInt("article_id") == article_id)
                price_per_unit = supplier_arr.getJsonObject(article.getSupplierIds().get(0)).getJsonArray("stock")
                        .getJsonObject(i)
                        .getJsonNumber("price").doubleValue();
        }

        int article_to_buy = (int) (money / price_per_unit);

        for (int supplier_id : article.getSupplierIds()) {
            JsonArray stock = supplier_arr.getJsonObject(supplier_id).getJsonArray("stock");

            for (int index = 0; index < stock.size(); index++) {
                if (stock.getJsonObject(index).getJsonNumber("article_id").doubleValue() == article_id) {
                    // price_per_unit =
                    // stock.getJsonObject(index).getJsonNumber("price").doubleValue();
                    supplierStock = stock.getJsonObject(index).getInt("stock");
                }
            }

            // Wenn die Menge die der Supplier liefern kann mal dem Preis pro Stück unser
            // Geld übersteigt kaufen wir
            // soviel wie wir uns leisten können. Ansonsten kaufen wir alles was verfügbar
            // ist
            if ((supplierStock != 0) && (supplierStock > article_to_buy)) {
                quantity = article_to_buy;
            } else {
                quantity = supplierStock;
            }
            if (quantity == 0)
                continue;

            System.out.println("Methods.buy - price_per_unit: " + price_per_unit + " quantity: " + quantity + " Costs: "
                    + price_per_unit * quantity + " article_id: " + article_id + " supplier_id: " + supplier_id);

            // API ansprechen
            JsonObjectBuilder builder = Json
                    .createObjectBuilder();
            builder.add("count", quantity);
            builder.add("price_per_unit", price_per_unit);
            JsonObject send1 = builder.build();
            String send = send1.toString();

            System.out.println(send1.toString());

            URL obj = new URL(BotMain.BASE_URL + "/supplier/" + supplier_id + "/article/"
                    + article_id + "/buy");
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

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println(ANSI_GREEN_BACKGROUND + ANSI_BLACK + "Methods - Buy: Erfolgreich: " + outputCode
                        + " " + response
                        + ANSI_RESET);
                article_to_buy -= quantity;

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
                System.out.println(ANSI_YELLOW_BACKGROUND + ANSI_RED + "POST request not worked" + ANSI_RESET);

            }

            if (article_to_buy == 0)
                break;
        }

        return 0;
    }

    public static void updatePrices(JsonArray objectArray) {
        for (int i = 0; i < objectArray.size(); i++) {
            JsonArray stock = objectArray.getJsonObject(i).getJsonArray("stock");
            for (int y = 0; y < stock.size(); y++) {
                for (Artikel gegenstand : BotMain.articleFromSupplier) { // für jedes item in items ... hinzufügen json
                    // object
                    if (gegenstand.getId() == stock.getJsonObject(y).getInt("article_id")) {

                        // Der Preis des Artikels wird geupdated
                        gegenstand.setPrice(stock.getJsonObject(y).getJsonNumber("price").doubleValue());

                        // Falls der Supplier noch nicht in der Liste sein sollte wird er hinzugefügt
                        if (!gegenstand.containsSupplierId(objectArray.getJsonObject(i).getInt("id"))) {
                            gegenstand.addSupplierId(objectArray.getJsonObject(i).getInt("id"));
                        }

                    }
                }
            }
        }
    }
}
