package ar.com.mylback;

import ar.com.mylback.auth.FirebaseAuthValidator;

import ar.com.mylback.dal.crud.cards.DAOCard;
import ar.com.mylback.dal.crud.cards.DAOCardProperties;
import ar.com.mylback.dal.crud.users.*;
import ar.com.mylback.dal.entities.cards.*;
import ar.com.mylback.utils.ImageUrlGenerator;
import ar.com.mylback.utils.InjectorProvider;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.entitydtomappers.cards.*;
import ar.com.mylback.utils.entitydtomappers.users.*;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 3000;

        // Create HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0); // TODO put 0.0.0.0 to listen all the request when public

        // Shared queue
        BlockingQueue<HttpExchange> requestQueue = new LinkedBlockingQueue<>();

        // Thread pool for worker threads
        ExecutorService workerPool = Executors.newFixedThreadPool(4); // for processing
        try {

            // singleton dependencies
            InjectorProvider injectorProvider = buildInjectorProvider();

            // Start workers
            for (int i = 0; i < 4; i++) {
                try {
                    workerPool.submit(new RequestProcessor(requestQueue, injectorProvider));
                } catch (Exception e) {
                    System.err.println("Fail to start worker thread " + (i + 1) + ": " + e.getMessage());
                }
            }
        } catch (MylException e) {
            throw new RuntimeException(e);
        }

        RequestProcessor.start();

        // Single context with router logic
        server.createContext("/", new RouterHandler(requestQueue));

        // Allow HttpServer to accept multiple incoming connections
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        System.out.println("Server started on http://localhost:" + port);



        // shut down process
        // Listen to console input in a separate thread
        // TODO call ImageUrlGenerator.close
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown signal received");
            server.stop(0);
            RequestProcessor.stop();
            workerPool.shutdown();
        }));
    }

    private static InjectorProvider buildInjectorProvider() throws MylException {
        Gson gson = new Gson();

        FirebaseAuthValidator firebaseAuthValidator = new FirebaseAuthValidator();
        ImageUrlGenerator imageUrlGenerator = new ImageUrlGenerator();

        // singleton mappers
        CollectionMapper collectionMapper   = new CollectionMapper();
        RarityMapper rarityMapper           = new RarityMapper();
        TypeMapper typeMapper               = new TypeMapper();
        RaceMapper raceMapper               = new RaceMapper();
        FormatMapper formatMapper           = new FormatMapper();
        KeyWordMapper keyWordMapper         = new KeyWordMapper();

        CardMapper cardMapper = new CardMapper(
                imageUrlGenerator,
                collectionMapper,
                rarityMapper,
                typeMapper,
                raceMapper,
                formatMapper,
                keyWordMapper
        );

        UserMapper userMapper = new UserMapper();
        DeckCardMapper deckCardMapper = new DeckCardMapper(cardMapper);
        DeckMapper deckMapper = new DeckMapper(deckCardMapper);
        PlayerCardMapper playerCardMapper = new PlayerCardMapper(cardMapper);
        PlayerMapper playerMapper = new PlayerMapper(deckMapper, playerCardMapper);
        StoreMapper storeMapper = new StoreMapper();

        return new InjectorProvider(
                () -> gson,
                () -> firebaseAuthValidator,
                DAOCard::new,
                () -> new DAOCardProperties<>(Collection.class),
                () -> new DAOCardProperties<>(Format.class),
                () -> new DAOCardProperties<>(KeyWord.class),
                () -> new DAOCardProperties<>(Race.class),
                () -> new DAOCardProperties<>(Rarity.class),
                () -> new DAOCardProperties<>(Type.class),
                DAOPlayer::new,
                DAOStore::new,
                DAODeck::new,
                DAODeckCard::new,
                DAOPlayerCard::new,
                () -> cardMapper,
                () -> userMapper,
                () -> playerMapper,
                () -> storeMapper,
                () -> collectionMapper,
                () -> rarityMapper,
                () -> formatMapper,
                () -> keyWordMapper,
                () -> raceMapper,
                () -> typeMapper,
                () -> deckMapper,
                () -> playerCardMapper
        );
    }
}
