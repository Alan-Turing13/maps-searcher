package org.example;

import okhttp3.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PlaceSearcher {


    public static final MediaType JSON = MediaType.get("application/json");
    public static String SEARCH_QUERY = "\"Comedy clubs London\"";
    public static String API_KEY;
    public static Properties properties = new Properties();

    public static void main(String[] args) throws Exception{

        try (InputStream inStr = new FileInputStream("src/main/resources/application.properties")){
            properties.load(inStr);
            API_KEY = properties.getProperty("api.key");
        } catch (IOException e){
            System.err.println("Error finding properties file");
        }

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(JSON, "{\"textQuery\": " + SEARCH_QUERY + "}");

        Request request = new Request.Builder()
                .url("https://places.googleapis.com/v1/places:searchText")
                .post(requestBody)
                .header("Content-Type", "application/json")
                .addHeader("X-Goog-Api-Key", API_KEY)
                .addHeader( "X-Goog-FieldMask", "places.displayName,places.formattedAddress,places.priceLevel")
                .build();

        try (Response response = client.newCall(request).execute()){
            System.out.println(response.body().string());
        } catch (Exception e){
            System.err.println("No dice.");
        }

    }
}