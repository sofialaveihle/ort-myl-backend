package ar.com.test.loaddata;

import ar.com.test.loaddata.models.Card;
import ar.com.test.loaddata.models.CardDetails;
import ar.com.test.loaddata.models.CardsData;
import ar.com.test.loaddata.models.Collection;
import com.google.gson.Gson;
import okhttp3.*;
import com.google.common.util.concurrent.RateLimiter;


import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CardCompleteBatchFromApi {
    private static final int THREADS = 5;
    private static final int MAX_RETRIES = 3;
    private static final long BACKOFF_MS = 1000;
    private static final RateLimiter rateLimiter = RateLimiter.create(5.0);

    private final static OkHttpClient client = new OkHttpClient.Builder()
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES)) // reuse up to 5 connections
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    public static void completeDataFromTheApi(final CardsData cardsData) {
        ExecutorService exec = Executors.newFixedThreadPool(THREADS);
        CompletionService<Card> completion = new ExecutorCompletionService<>(exec);

        try {
            Gson gson = new Gson();
            Set<Collection> collections = Collections.synchronizedSet(new HashSet<>());
            int total = cardsData.getCards().size();

            System.out.println("Complete data from the API");
            AtomicInteger current = new AtomicInteger(0);

            for (Card card : cardsData.getCards()) {
                completion.submit(() -> {
                    boolean success = false;
                    for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
                        try {
                            String cardDetailsJson = getDataFromTheApi(card.getEd_slug(), card.getSlug());

                            if (cardDetailsJson != null) {
                                CardDetails cardDetails = gson.fromJson(cardDetailsJson, CardDetails.class);

                                if (cardDetails != null) {
                                    if (cardDetails.getValid_formats() != null) {
                                        card.setFormats(cardDetails.getValid_formats());
                                    } else {
                                        throw new Exception("error getting valid formats");
                                    }
                                    if (cardDetails.getEdition() != null) {
                                        collections.add(cardDetails.getEdition());
                                    } else {
                                        throw new Exception("error collecting edition");
                                    }
                                    success = true;
                                } else {
                                    throw new Exception("Error json parsing");
                                }
                            } else {
                                throw new Exception("Empty json from api or error response");
                            }
                        } catch (Exception e) {
                            System.err.printf("Card %s: API attempt %d/%d failed: %s%n", card.getName(), attempt, MAX_RETRIES, e.getMessage());
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

            System.out.println("\nAPI completion done. Successful: " + (total - toRetry.size()) + " of " + total + ", Failed: " + toRetry.size());

            if (!toRetry.isEmpty()) {
                ProcessUtils.saveFailsToJson(toRetry, "failed_api_cards_complete.json");
                System.out.println("Wrote " + toRetry.size() + " failed cards to failed_api_cards.json");
            }

            cardsData.setCollections(collections);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during complete images process: out thread" + e.getMessage());
        } finally {
            exec.shutdown();
            try {
                exec.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                System.err.println("Error closing threads " + e.getMessage());
            }
        }
    }

    private static String getDataFromTheApi(String editionSlug, String cardSlug) throws Exception {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.myl.cl")
                .addPathSegment("cards")
                .addPathSegment("profile")
                .addPathSegment(editionSlug)
                .addPathSegment(cardSlug)
                .build();

        rateLimiter.acquire(); // limit to not overload the api
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                // mimic a modern browser
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "Chrome/112.0.0.0 Safari/537.36")
                // close the connection after each call
                .addHeader("Connection", "close")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Unexpected HTTP "
                        + response.code() + " "
                        + response.message()
                        + " from " + url);
            }
            return response.body().string();
        }
    }
}
