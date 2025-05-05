package ar.com.test.loaddata;

import ar.com.mylback.dal.crud.DAO;
import ar.com.mylback.dal.crud.HibernateUtil;
import ar.com.test.loaddata.deserializers.CardsDataDeserializer;
import ar.com.test.loaddata.models.CardsData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class LoadDataBase {
    private static final Scanner scanner = new Scanner(System.in);

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

            // Wrap the InputStream in an InputStreamReader with UTF-8 encoding
            try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                 // Wrap the Reader in a JsonReader (optional but recommended for streams)
                 JsonReader reader = new JsonReader(isr)) {

                Gson gson = new GsonBuilder().registerTypeAdapter(CardsData.class, new CardsDataDeserializer()).create();

                CardsData data = gson.fromJson(reader, CardsData.class);
                System.out.print("Parse json complete. Continue Y/N: ");
                String next = scanner.nextLine().toUpperCase();
                if (next.equals("Y")) {
                    // complete collections from the api
                    CardCompleteBatchFromApi.completeDataFromTheApi(data);
                    System.out.println();
                } else {
                    throw new Exception("Finish execution by user");
                }


                // convert and load to database.
                // load first dependent objects to generate the ids in the DB
                System.out.print("Complete card data from API complete. Continue Y/N: ");
                next = scanner.nextLine().toUpperCase();
                if (next.equals("Y")) {
                    DAO<ar.com.mylback.dal.entities.Collection, Integer> daoCollection = new DAO<>(ar.com.mylback.dal.entities.Collection.class);
                    daoCollection.save(MapToEntities.mapCollectionToEntity(data));

                    DAO<ar.com.mylback.dal.entities.Format, Integer> daoFormat = new DAO<>(ar.com.mylback.dal.entities.Format.class);
                    daoFormat.save(MapToEntities.mapFormatToEntity());

                    DAO<ar.com.mylback.dal.entities.KeyWord, Integer> daoKeyWord = new DAO<>(ar.com.mylback.dal.entities.KeyWord.class);
                    daoKeyWord.save(MapToEntities.mapKeyWordsToEntity(data));

                    DAO<ar.com.mylback.dal.entities.Race, Integer> daoRace = new DAO<>(ar.com.mylback.dal.entities.Race.class);
                    daoRace.save(MapToEntities.mapRaceToEntity(data));

                    DAO<ar.com.mylback.dal.entities.Rarity, Integer> daoRarity = new DAO<>(ar.com.mylback.dal.entities.Rarity.class);
                    daoRarity.save(MapToEntities.mapRarityToEntity(data));

                    DAO<ar.com.mylback.dal.entities.Type, Integer> daoType = new DAO<>(ar.com.mylback.dal.entities.Type.class);
                    daoType.save(MapToEntities.mapTypeToEntity(data));

                    // load the cards with the reference to the loaded objets
                    CardInsertDbBatch.saveAllToDatabase(data);
                } else {
                    throw new Exception("Finish execution by user");
                }

                System.out.print("Insert data into DB complete. Continue Y/N: ");
                next = scanner.nextLine().toUpperCase();
                if (next.equals("Y")) {
                    ImageBatchUploader.loadImages(data);
                } else {
                    throw new Exception("Finish execution by user");
                }
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
            scanner.close();
        }
    }
}
