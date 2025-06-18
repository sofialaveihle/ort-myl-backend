package ar.com.test.loaddata;

import ar.com.test.loaddata.models.Card;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;

public class ProcessUtils {

    public static Set<Card> waitAndCollectFails(int total,  CompletionService<Card> completionService) throws Exception {
        Set<Card> toRetry = Collections.synchronizedSet(new HashSet<>());
        for (int i = 0; i < total; i++) {
            Future<Card> future = completionService.take();
            Card failed = future.get();   // null on success
            if (failed != null) {
                toRetry.add(failed);
            }
        }
        return toRetry;
    }

    public static void saveFailsToJson(Set<Card> toRetry, String fileName) throws Exception {
        URL resUrl = ProcessUtils.class.getResource("/");
        if (resUrl == null) {
            throw new RuntimeException("Resources folder not found on the classpath!");
        }
        Path outFile;
        outFile = Paths.get(resUrl.toURI()).resolve(fileName);


        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        try (BufferedWriter writer = Files.newBufferedWriter(outFile, StandardCharsets.UTF_8)) {
            gson.toJson(toRetry, writer);
            System.out.println("Wrote failed_cards.json to: " + outFile);
        } catch (IOException ioe) {
            System.err.println("Error writing to resources folder: " + ioe.getMessage());
        }
    }
}
