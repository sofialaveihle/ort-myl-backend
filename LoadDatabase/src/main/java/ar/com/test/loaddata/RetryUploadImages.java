package ar.com.test.loaddata;

import ar.com.mylback.dal.crud.DAOCard;
import ar.com.mylback.dal.entities.Card;
import ar.com.test.loaddata.deserializers.CardsDataDeserializer;
import ar.com.test.loaddata.models.CardsData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class RetryUploadImages {
    public static void main(String[] args) {
        String resourcePath = "/cards.json";

        try (
                // Get the InputStream for the resource from the classpath
                InputStream is = LoadDataBase.class.getResourceAsStream(resourcePath)
        ) {
            if (is == null) {
                System.err.println("Resource not found on classpath: " + resourcePath);
                throw new Exception("Resource not found on classpath: " + resourcePath);
            }

            try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                 JsonReader reader = new JsonReader(isr)) {

                Gson gson = new GsonBuilder().registerTypeAdapter(CardsData.class, new CardsDataDeserializer()).create();

                // get all the cards from the json
                CardsData cardsData = gson.fromJson(reader, CardsData.class);

                // get all the cards from the DB
                DAOCard<ar.com.mylback.dal.entities.Card, Integer> cardDAO = new DAOCard<>(ar.com.mylback.dal.entities.Card.class);
                List<Card> entityCards = cardDAO.getAllCardsWithCollections();

                ImageBatchUploader.loadImagesFromCardsInDataBase(cardsData, entityCards);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
