package ar.com.test.loaddata;

import ar.com.mylback.dal.crud.HibernateUtil;
import ar.com.test.loaddata.deserializers.CardsDataDeserializer;
import ar.com.test.loaddata.models.Card;
import ar.com.test.loaddata.models.CardsData;
import ar.com.test.loaddata.models.KeyWord;
import ar.com.test.loaddata.models.Race;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RetryLoadDataBase {
    public static void main(String[] args) {
        String properties = "/properties.json";
        String failComplete = "/failed_api_cards_complete.json";
        String failInsert = "/cards_fail_insert.json";

        try (
                InputStream failCompleteIS = LoadDataBase.class.getResourceAsStream(failComplete);
                InputStream propertiesIS = LoadDataBase.class.getResourceAsStream(properties);
                InputStream failInsertIS = LoadDataBase.class.getResourceAsStream(failInsert)
        ) {
            if (propertiesIS == null || failCompleteIS == null || failInsertIS == null) {
                System.err.println("Resource not found on classpath: " + properties);
                System.err.println("Resource not found on classpath: " + failComplete);
                System.err.println("Resource not found on classpath: " + failInsert);
                throw new Exception("Resource not found on classpath");
            }

            try (
                    InputStreamReader isrProperties = new InputStreamReader(propertiesIS, StandardCharsets.UTF_8);
                    InputStreamReader isrFailComplete = new InputStreamReader(failCompleteIS, StandardCharsets.UTF_8);
                    InputStreamReader isrFailInsert = new InputStreamReader(failInsertIS, StandardCharsets.UTF_8);
                    JsonReader readerProperties = new JsonReader(isrProperties);
                    JsonReader readerFailComplete = new JsonReader(isrFailComplete);
                    JsonReader readerFailInsert = new JsonReader(isrFailInsert)
            ) {

                Gson gson = new GsonBuilder().registerTypeAdapter(CardsData.class, new CardsDataDeserializer()).create();

                Type listType = new TypeToken<List<Card>>(){}.getType();
                CardsData cardsData= gson.fromJson(readerProperties, CardsData.class);


                Set<Card> cards = new HashSet<>(gson.fromJson(readerFailComplete, listType));
                List<Card> failInsertCards = gson.fromJson(readerFailInsert, listType);
                cards.addAll(failInsertCards);

                cardsData.setCards(cards);

                CardCompleteBatchFromApi.completeDataFromTheApi(cardsData);

                CardInsertDbBatch.saveAllToDatabase(cardsData);
            }
        } catch (IOException e) {
            System.err.println("Error reading or deserializing JSON file: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during JSON deserialization: " + e.getMessage());
            System.exit(1);
        } finally {
            System.out.println("Close program...");
            HibernateUtil.shutdown();
        }
    }
}
