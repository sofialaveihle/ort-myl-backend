package ar.com.test.loaddata;

import ar.com.mylback.utils.MylException;
import ar.com.mylback.dal.crud.DAOCardProperties;
import ar.com.test.loaddata.models.Card;
import ar.com.test.loaddata.models.CardDetails;
import ar.com.test.loaddata.models.CardsData;
import ar.com.test.loaddata.models.Properties;
import com.google.gson.Gson;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageBatchUploader {
    private static final int THREADS = 6;
    private static final int MAX_RETRIES = 3;
    private static final long BACKOFF_MS = 500;

    public static void loadImagesFromCardsInDataBase(CardsData cardsData, List<ar.com.mylback.dal.entities.Card> entityCards) {
        ImageProcessor imageProcessor = new ImageProcessor();
        ExecutorService exec = Executors.newFixedThreadPool(THREADS);
        CompletionService<Card> completion = new ExecutorCompletionService<>(exec);

        System.out.println("Loading images...");
        int total = entityCards.size();
        AtomicInteger current = new AtomicInteger(0);
        AtomicInteger uploadedImagesCountSuccess = new AtomicInteger(0);
        AtomicInteger uploadedImagesTotal = new AtomicInteger(0);

        Gson gson = new Gson();

        try {
            for (ar.com.mylback.dal.entities.Card card : entityCards) {
                completion.submit(() -> {
                    Card jsonCompleteCard = null;
                    boolean success = false;
                    try {
                        // check card exist
                        if (!imageProcessor.imageExists(card.getImageUuid())) {
                            // search in json cards with
                            // ALTER TABLE cards ADD UNIQUE unique_card (card_name(50), cost, damage, collection_id, rarity_id, type_id, race_id);
                            // excluding collections, if the result contains multiple values, search the collection(edition) name from the api
                            jsonCompleteCard = getMatchCardFromJson(card, cardsData, gson);
                            if (jsonCompleteCard != null) {
                                loadImage(jsonCompleteCard, imageProcessor, card);
                                success = true;
                                System.out.println("Successfully loaded image: " + uploadedImagesCountSuccess.incrementAndGet() + " of " + uploadedImagesTotal.incrementAndGet());
                            } else {
                                System.err.println("Failed to load image: " + card.getImageUuid());
                                uploadedImagesTotal.incrementAndGet();
                            }
                        } else {
                            success = true;
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to load image: " + card.getImageUuid());
                    } finally {
                        int progress = current.incrementAndGet();
                        System.out.print("\rProgress: " + (progress * 100 / total) + "% " + progress + " of " + total);
                    }

                    return success ? null : jsonCompleteCard;
                });
            }

            // collect results
            Set<Card> toRetry = ProcessUtils.waitAndCollectFails(total, completion);

            System.out.println("\nFinished loading images.");
            if (!toRetry.isEmpty()) {
                System.out.println("There are images to retry later.");
                ProcessUtils.saveFailsToJson(toRetry, "cards_upload_image_failed.json");
            }
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during loading images: out thread" + e.getMessage());
        } finally {
            exec.shutdown();
            try {
                exec.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                System.err.println("Error closing threads " + e.getMessage());
            }
        }
    }

    public static Card getMatchCardFromJson(ar.com.mylback.dal.entities.Card entityCard, CardsData cardsData, Gson gson) {
        // Filter by everything except collection
        List<Card> possibleCards = cardsData.getCards().stream()
                .filter(c -> c.getName().equalsIgnoreCase(entityCard.getName()))
                .filter(c -> matchValue(c.getRarity(), cardsData.getRarities(), entityCard.getRarity() != null ? entityCard.getRarity().getName() : null))
                .filter(c -> matchValue(c.getType(), cardsData.getTypes(), entityCard.getType() != null ? entityCard.getType().getName() : null))
                .filter(c -> matchValue(c.getRace(), cardsData.getRaces(), entityCard.getRace() != null ? entityCard.getRace().getName() : null))
                .filter(c -> Objects.equals(toInteger(c.getCost()), entityCard.getCost()))
                .filter(c -> Objects.equals(toInteger(c.getDamage()), entityCard.getDamage()))
                .toList();

        if (possibleCards.size() == 1) {
            return possibleCards.get(0);
        }

        // Fallback: disambiguate using API
        for (Card candidate : possibleCards) {
            String cardJson = "";
            try {
                cardJson = CardCompleteBatchFromApi.getDataFromTheApi(candidate.getEd_slug(), candidate.getSlug());
                CardDetails details = gson.fromJson(cardJson, CardDetails.class);

                if (details.getEdition() != null && details.getEdition().getTitle().equalsIgnoreCase(entityCard.getCollection().getName())) {
                    return candidate;
                }
            } catch (Exception e) {
                System.err.print("Error for candidate " + candidate.getName() + " " + candidate.getEd_slug());
                System.err.print("Fail to get candidate for ambiguous results + " + e.getMessage());
                System.err.println("Error details response body " + cardJson);
            }
        }

        return null;
    }

    private static <T extends Properties> boolean matchValue(String jsonPropId, Set<T> properties, String entityPropName) {
        if (jsonPropId == null && entityPropName == null) return true;
        if (jsonPropId == null || entityPropName == null) return false;

        return properties.stream()
                .filter(p -> p.getId().equals(jsonPropId))
                .anyMatch(e -> e.getName().equalsIgnoreCase(entityPropName));
    }

    private static Integer toInteger(String str) {
        if (str == null || str.isEmpty()) return null;
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static void loadImages(CardsData cardsData) {
        ImageProcessor imageProcessor = new ImageProcessor();
        DAOCardProperties<ar.com.mylback.dal.entities.Card, String> cardDao = new DAOCardProperties<>(ar.com.mylback.dal.entities.Card.class);

        ExecutorService exec = Executors.newFixedThreadPool(THREADS);
        CompletionService<Card> completion = new ExecutorCompletionService<>(exec);

        System.out.println("Loading images...");
        int total = cardsData.getCards().size();
        AtomicInteger current = new AtomicInteger(0);

        try {
            for (Card card : cardsData.getCards()) {
                completion.submit(() -> {
                    boolean success = false;
                    for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
                        try {
                            ar.com.mylback.dal.entities.Card entityCard = cardDao.findByName(card.getName());
                            loadImage(card, imageProcessor, entityCard);
                            success = true;
                            break;
                        } catch (Exception e) {
                            System.err.printf("Card %s failed attempt %d/%d: %s%n", card.getName(), attempt, MAX_RETRIES, e.getMessage());
                            Thread.sleep(BACKOFF_MS * attempt);
                        }
                    }
                    int progress = current.incrementAndGet();
                    System.out.print("\rProgress: " + (progress * 100 / total) + "% " + progress + " of " + total);

                    return success ? null : card;
                });
            }

            // collect results
            Set<Card> toRetry = ProcessUtils.waitAndCollectFails(total, completion);

            System.out.println("\nFinished loading images.");
            if (!toRetry.isEmpty()) {
                System.out.println("There are images to retry later.");
                ProcessUtils.saveFailsToJson(toRetry, "cards_upload_image_failed.json");
            }
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during loading images: out thread" + e.getMessage());
        } finally {
            exec.shutdown();
            try {
                exec.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                System.err.println("Error closing threads " + e.getMessage());
            }
        }
    }

    private static void loadImage(Card card, ImageProcessor imageProcessor, ar.com.mylback.dal.entities.Card entityCard) throws MylException {
        if (entityCard != null) {
            BufferedImage bufferedImage = imageProcessor.getImageFromApi(card.getEd_edid(), card.getEdid());
            if (bufferedImage != null) {
                byte[] imageWebp = imageProcessor.compressPngToWebp(bufferedImage, 0.8f);
                if (imageWebp != null) {
                    if (!imageProcessor.imageUpload(imageWebp, entityCard.getImageUuid())) {
                        throw new MylException(MylException.Type.ERROR_LOAD_IMAGE, "fail to upload image");
                    }
                } else {
                    throw new MylException(MylException.Type.ERROR_LOAD_IMAGE, "null image after compress");
                }
            } else {
                throw new MylException(MylException.Type.ERROR_LOAD_IMAGE, "null image from the api");
            }
        } else {
            throw new MylException(MylException.Type.ERROR_LOAD_IMAGE, "no card find in the DB for " + card.getName());
        }
    }
}
