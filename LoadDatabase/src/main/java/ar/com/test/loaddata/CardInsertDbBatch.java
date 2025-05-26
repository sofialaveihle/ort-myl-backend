package ar.com.test.loaddata;

import ar.com.mylback.dal.crud.cards.DAO;
import ar.com.test.loaddata.models.Card;
import ar.com.test.loaddata.models.CardsData;

import java.util.*;

public class CardInsertDbBatch {

    private static final int BATCH_SIZE  = 100;
    private static final int MAX_RETRIES = 3;

    public static void saveAllToDatabase(CardsData cardsData) throws Exception {
        List<Set<Card>> chunks = chunkListSet(cardsData.getCards(), BATCH_SIZE);
        DAO<ar.com.mylback.dal.entities.cards.Card, Integer> daoCard = new DAO<>(ar.com.mylback.dal.entities.cards.Card.class);
        Set<Card> toRetry = new HashSet<>();
        int total = chunks.size();
        int progress = 0;
        System.out.println("Init save cards into DB...");

        for (Set<Card> chunk : chunks) {
            boolean success = false;
            for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
                try {
                    List<ar.com.mylback.dal.entities.cards.Card> entityCards = MapToEntities.mapCardToEntity(chunk, cardsData);
                    daoCard.save(entityCards);
                    success = true;
                    break;
                } catch (Exception e) {
                    System.err.printf("Chunk: INSERT attempt %d/%d failed: %s%n", attempt, MAX_RETRIES, e.getMessage());

                    System.err.println("An unexpected error occurred during insert process: " + e.getMessage());
                }
            }
            progress++;
            System.out.print("\rProgress: " + (progress * 100 / total) + "% " + progress + " of " + total);

            if (!success) {
                toRetry.addAll(chunk);
            }
        }

        System.out.println("\nFinish save cards into DB.");
        if (!toRetry.isEmpty()) {
            System.out.println("There are " + toRetry.size() + " fail cards");
            ProcessUtils.saveFailsToJson(toRetry, "cards_fail_insert.json");
        }
    }

    public static <T> List<Set<T>> chunkListSet(Set<T> input, int batchSize) {
        List<Set<T>> result = new ArrayList<>();
        Iterator<T> it = input.iterator();

        while (it.hasNext()) {
            Set<T> chunk = new HashSet<>(batchSize);
            for (int i = 0; i < batchSize && it.hasNext(); i++) {
                chunk.add(it.next());
            }
            result.add(chunk);
        }

        return result;
    }
}
