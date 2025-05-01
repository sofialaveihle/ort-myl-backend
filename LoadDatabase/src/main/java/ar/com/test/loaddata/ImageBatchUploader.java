package ar.com.test.loaddata;

import ar.com.mylback.MylException;
import ar.com.mylback.dal.crud.DAOCardProperties;
import ar.com.test.loaddata.models.Card;
import ar.com.test.loaddata.models.CardsData;

import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageBatchUploader {
    private static final int THREADS = 6;
    private static final int MAX_RETRIES = 3;
    private static final long BACKOFF_MS = 500;

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
                            loadImage(card, imageProcessor, cardDao);
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

    private static void loadImage(Card card, ImageProcessor imageProcessor, DAOCardProperties<ar.com.mylback.dal.entities.Card, String> cardDao) throws MylException {
        ar.com.mylback.dal.entities.Card entityCard = cardDao.findByName(card.getName());

        if (entityCard != null) {
            BufferedImage bufferedImage = imageProcessor.getImageFromApi(card.getEd_edid(), card.getEdid());
            if (bufferedImage != null) {
                byte[] imageWebp = imageProcessor.compressPngToWebp(bufferedImage, 0.8f);
                if (imageWebp != null) {
                    if (!imageProcessor.imageUpload(imageWebp, entityCard.getImageUuid(), entityCard)) {
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
