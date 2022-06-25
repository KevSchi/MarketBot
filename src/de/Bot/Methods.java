package de.Bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.*;
import java.util.*;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class Methods {

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
}