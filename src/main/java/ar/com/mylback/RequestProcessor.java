package ar.com.mylback;

import ar.com.mylback.controller.*;
import ar.com.mylback.utils.HttpResponse;
import ar.com.mylback.utils.InjectorProvider;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.url.PathHelper;
import ar.com.mylback.utils.url.QueryString;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

public class RequestProcessor implements Runnable {
    private static volatile boolean isRunning;
    private final BlockingQueue<HttpExchange> queue;
    private final InjectorProvider injectorProvider;

    public RequestProcessor(BlockingQueue<HttpExchange> queue, InjectorProvider injectorProvider) throws MylException {
        if (queue != null && injectorProvider != null) {
            this.queue = queue;
            this.injectorProvider = injectorProvider;
        } else {
            throw new MylException(MylException.Type.GENERIC_ERROR);
        }
    }

    public static void stop() {
        isRunning = false;
    }

    public static void start() {
        isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            HttpExchange exchange = null;
            try {
                exchange = queue.take(); // wait for next request

                String method = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();
                QueryString queryString = new QueryString(exchange.getRequestURI().getQuery());

                switch (method) {
                    case "GET" -> getVerb(path, exchange, queryString);

                    case "POST" -> postVerb(exchange, path);

                    case "PUT" -> putVerb(path, exchange);

                    case "DELETE" -> deleteVerb(path, exchange);
                }
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                System.err.println("Worker thread interrupted. Shutting down.");
                isRunning = false;
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("An unhandled error occurred while processing a request:");
                System.err.println(e.getMessage());

                if (exchange != null) {
                    try {
                        String errorResponse = "{\"error\":\"An internal server error occurred. Please try again later.\"}";
                        sendResponse(exchange, new HttpResponse(500, errorResponse));
                    } catch (Exception responseEx) {
                        System.err.println("Failed to send unhandled error to client: " + responseEx.getMessage());
                    }
                }
            }
        }
    }

    private void getVerb(String path, HttpExchange exchange, QueryString queryString) throws IOException, MylException {
        if (path.equals("/help")) {
            String response = """
                    The server is ready. Available endpoints:
                    
                    [GET]
                    /api/cards                   -> Get all cards
                    /api/card/{id}              -> Get a card by ID
                    /api/card/search            -> Search cards by name (?name=)
                    /api/collections            -> Get all collections
                    /api/rarities               -> Get all rarities
                    /api/types                  -> Get all types
                    /api/races                  -> Get all races
                    /api/formats                -> Get all formats
                    /api/keywords               -> Get all keywords
                    /api/stores                 -> Get stores (requires Authorization header)
                    /api/stores/{uuid}         -> Get specific store by UUID (requires Authorization)
                    /api/auth/me                -> Get user data (requires Authorization)
                    
                    [POST]
                    /api/players/register       -> Register a new player
                    /api/stores/register        -> Register a new store
                    /api/admin/stores/validate  -> Validate store (requires Authorization)
                    
                    [PUT]
                    /api/stores                 -> Update store (requires Authorization)
                    /api/players                -> Update player (requires Authorization)
                    
                    [DELETE]
                    /api/users                  -> Delete account (requires Authorization)
                    """;
            sendResponse(exchange, new HttpResponse(200, response));
        } else if (path.equals("/api/cards")) {

            CardController controller = new CardController(
                    injectorProvider.getGson(),
                    injectorProvider.getDaoCard(),
                    injectorProvider.getCardMapper());
            HttpResponse response = controller.getCardsEndpoint(queryString);
            sendResponse(exchange, response);

        } else if (path.matches("/api/card/\\d+")) {

            int cardId = Integer.parseInt(PathHelper.getLastPathSegment(path));
            CardController controller = new CardController(
                    injectorProvider.getGson(),
                    injectorProvider.getDaoCard(),
                    injectorProvider.getCardMapper());
            HttpResponse response = controller.getCardById(cardId);
            sendResponse(exchange, response);

        } else if (path.equals("/api/card/search")) {

            CardController controller = new CardController(
                    injectorProvider.getGson(),
                    injectorProvider.getDaoCard(),
                    injectorProvider.getCardMapper());
            HttpResponse response = controller.getCardsByName(queryString);
            sendResponse(exchange, response);

        } else if (path.equals("/api/collections")) {

            CollectionController controller = new CollectionController(injectorProvider.getGson(), injectorProvider.getCollectionMapper());
            HttpResponse response = controller.getAll();
            sendResponse(exchange, response);

        } else if (path.equals("/api/rarities")) {

            RarityController controller = new RarityController(injectorProvider.getGson(), injectorProvider.getRarityMapper());
            HttpResponse response = controller.getAll();
            sendResponse(exchange, response);

        } else if (path.equals("/api/types")) {

            TypeController controller = new TypeController(injectorProvider.getGson(), injectorProvider.getTypeMapper());
            HttpResponse response = controller.getAll();
            sendResponse(exchange, response);

        } else if (path.equals("/api/races")) {

            RaceController controller = new RaceController(injectorProvider.getGson(), injectorProvider.getRaceMapper());
            HttpResponse response = controller.getAll();
            sendResponse(exchange, response);

        } else if (path.equals("/api/formats")) {

            FormatController controller = new FormatController(injectorProvider.getGson(), injectorProvider.getFormatMapper());
            HttpResponse response = controller.getAll();
            sendResponse(exchange, response);

        } else if (path.equals("/api/keywords")) {

            KeyWordController controller = new KeyWordController(injectorProvider.getGson(), injectorProvider.getKeyWordMapper());
            HttpResponse response = controller.getAll();
            sendResponse(exchange, response);

        } else if (path.equals("/api/stores")) {

            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

            StoreController controller = new StoreController(injectorProvider.getGson(),
                    injectorProvider.getFirebaseAuthValidator(),
                    injectorProvider.getDaoStore(),
                    injectorProvider.getStoreMapper());
            HttpResponse response = controller.getStoresByValidation(authHeader, queryString);
            sendResponse(exchange, response);

        } else if (path.matches("/api/stores/[^/]+")) {

            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
            String uuid = PathHelper.getLastPathSegment(path);
            if (uuid != null) {
                StoreController controller = new StoreController(injectorProvider.getGson(),
                        injectorProvider.getFirebaseAuthValidator(),
                        injectorProvider.getDaoStore(),
                        injectorProvider.getStoreMapper());
                HttpResponse response = controller.getStoreByUuid(uuid, authHeader);
                sendResponse(exchange, response);
            } else {
                sendResponse(exchange, new HttpResponse(400, "invalid URL"));
            }

        } else if (path.equals("/api/auth/me")) {

            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
            HttpResponse response = new AuthController(injectorProvider.getGson(),
                    injectorProvider.getFirebaseAuthValidator(),
                    injectorProvider.getDaoPlayer(),
                    injectorProvider.getDaoStore(),
                    injectorProvider.getUserMapper(),
                    injectorProvider.getPlayerMapper(),
                    injectorProvider.getStoreMapper()).me(authHeader);
            sendResponse(exchange, response);

        } else {
            sendResponse(exchange, new HttpResponse(400, "Not Found"));
        }
    }

    private void postVerb(HttpExchange exchange, String path) throws IOException, MylException {
        String body = new String(exchange.getRequestBody().readAllBytes());

        switch (path) {
            case "/api/players/register" -> {

                try {
                    HttpResponse response = new AuthController(injectorProvider.getGson(),
                            injectorProvider.getFirebaseAuthValidator(),
                            injectorProvider.getDaoPlayer(),
                            injectorProvider.getDaoStore(),
                            injectorProvider.getUserMapper(),
                            injectorProvider.getPlayerMapper(),
                            injectorProvider.getStoreMapper()).registerPlayer(body);
                    sendResponse(exchange, response);
                } catch (Exception e) {
                    sendResponse(exchange, new HttpResponse(500, "Error al registrar usuario: " + e.getMessage()));
                }
            }

            case "/api/stores/register" -> {

                try {
                    HttpResponse response = new AuthController(injectorProvider.getGson(),
                            injectorProvider.getFirebaseAuthValidator(),
                            injectorProvider.getDaoPlayer(),
                            injectorProvider.getDaoStore(),
                            injectorProvider.getUserMapper(),
                            injectorProvider.getPlayerMapper(),
                            injectorProvider.getStoreMapper()).registerStore(body);
                    sendResponse(exchange, response);
                } catch (Exception e) {
                    sendResponse(exchange, new HttpResponse(500, "Error al registrar tienda: " + e.getMessage()));
                }
            }

            case "/api/admin/stores/validate" -> {

                String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
                JsonObject json = new Gson().fromJson(body, JsonObject.class);
                String uuid = json.get("uuid").getAsString();

                StoreController controller = new StoreController(injectorProvider.getGson(),
                        injectorProvider.getFirebaseAuthValidator(),
                        injectorProvider.getDaoStore(),
                        injectorProvider.getStoreMapper());
                HttpResponse response = controller.validateStore(uuid, authHeader);

                sendResponse(exchange, response);
            }

            default -> sendResponse(exchange, new HttpResponse(404, "POST endpoint not found"));
        }
    }

    private void putVerb(String path, HttpExchange exchange) throws IOException, MylException {
        switch (path) {
            case "/api/stores" -> {

                String body = new String(exchange.getRequestBody().readAllBytes());
                String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

                HttpResponse response = new StoreController(injectorProvider.getGson(),
                        injectorProvider.getFirebaseAuthValidator(),
                        injectorProvider.getDaoStore(),
                        injectorProvider.getStoreMapper()).updateStore(body, authHeader);
                sendResponse(exchange, response);
            }

            case "/api/players" -> {

                String body = new String(exchange.getRequestBody().readAllBytes());
                String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

                HttpResponse response = new UserController(injectorProvider.getGson(),
                        injectorProvider.getFirebaseAuthValidator(),
                        injectorProvider.getDaoPlayer()).updatePlayer(body, authHeader);
                sendResponse(exchange, response);
            }

            default -> sendResponse(exchange, new HttpResponse(404, "PUT endpoint not found"));
        }
    }

    private void deleteVerb(String path, HttpExchange exchange) throws IOException {
        switch (path) {
            case "/api/users" -> {
                try {
                    String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
                    HttpResponse response = new AuthController(injectorProvider.getGson(),
                            injectorProvider.getFirebaseAuthValidator(),
                            injectorProvider.getDaoPlayer(),
                            injectorProvider.getDaoStore(),
                            injectorProvider.getUserMapper(),
                            injectorProvider.getPlayerMapper(),
                            injectorProvider.getStoreMapper()).deleteAccount(authHeader);
                    sendResponse(exchange, response);
                } catch (Exception e) {
                    sendResponse(exchange, new HttpResponse(500, "{\"error\": \"Error al eliminar cuenta: " + e.getMessage() + "\"}"));
                }
            }
            default -> sendResponse(exchange, new HttpResponse(404, "PUT endpoint not found"));
        }
    }

    private void sendResponse(HttpExchange exchange, HttpResponse response) throws IOException {
        exchange.sendResponseHeaders(response.statusCode(), response.body().getBytes().length);
        try (exchange; OutputStream os = exchange.getResponseBody()) {
            os.write(response.body().getBytes());
        }
    }
}
