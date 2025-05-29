package ar.com.mylback;

import ar.com.mylback.controller.*;
import ar.com.mylback.utils.InjectorProvider;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.QueryString;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

public class RequestProcessor implements Runnable {
    private final BlockingQueue<HttpExchange> queue;
    private static volatile boolean isRunning;
    private final InjectorProvider injectorProvider;

    public RequestProcessor(BlockingQueue<HttpExchange> queue, InjectorProvider injectorProvider) throws MylException {
        if (queue != null && injectorProvider != null) {
            this.queue = queue;
            this.injectorProvider = injectorProvider;
        } else {
            throw new MylException(MylException.Type.GENERIC_ERROR);
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                HttpExchange exchange = queue.take(); // wait for next request

                String method = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();
                QueryString queryString = new QueryString(exchange.getRequestURI().getQuery());

                if (method.equals("GET")) {
                    if (path.equals("/help")) {
                        String response = "the server is ready. Available endpoints: \n";
                        response += "Request Cards GET: api/cards\n";
                        sendResponse(exchange, 200, response);
                    } else if (path.equals("/api/cards")) {
                        try {
                            CardController controller = new CardController(
                                    injectorProvider.getGsonSupplier(),
                                    injectorProvider.getDaoCardSupplier(),
                                    injectorProvider.getCardMapperSupplier());
                            String response = controller.getCardsEndpoint(queryString);
                            sendResponse(exchange, 200, response);
                        } catch (Exception e) {
                            sendResponse(exchange, 500, "Error parser cards: " + e.getMessage());
                        }
                    } else if (path.matches("/api/card/\\d+")) {
                        try {
                            String[] parts = path.split("/");
                            if (parts.length >= 4) {
                                CardController controller = new CardController(
                                        injectorProvider.getGsonSupplier(),
                                        injectorProvider.getDaoCardSupplier(),
                                        injectorProvider.getCardMapperSupplier());
                                String response = controller.getCardByIDEndpoint(Integer.parseInt(parts[3]));
                                sendResponse(exchange, 200, response);
                            } else {
                                sendResponse(exchange, 500, "Error parsing url");
                            }
                        } catch (Exception e) {
                            sendResponse(exchange, 500, "Error getting card: " + e.getMessage());
                        }
                    } else if (path.equals("/api/card/search")) {
                        try {
                            CardController controller = new CardController(
                                    injectorProvider.getGsonSupplier(),
                                    injectorProvider.getDaoCardSupplier(),
                                    injectorProvider.getCardMapperSupplier());
                            String response = controller.getCardsByName(queryString);
                            sendResponse(exchange, 200, response);
                        } catch (Exception e) {
                            sendResponse(exchange, 500, "Error parser cards: " + e.getMessage());
                        }
                    } else if (path.equals("/api/collections")) {
                        try {
                            CollectionController controller = new CollectionController(injectorProvider.getCollectionMapperSupplier());
                            String response = controller.getAll();
                            sendResponse(exchange, 200, response);
                        } catch (Exception e) {
                            sendResponse(exchange, 500, "Error parser collections: " + e.getMessage());
                        }
                    } else if (path.equals("/api/rarities")) {
                        try {
                            RarityController controller = new RarityController(injectorProvider.getRarityMapperSupplier());
                            String response = controller.getAll();
                            sendResponse(exchange, 200, response);
                        } catch (Exception e) {
                            sendResponse(exchange, 500, "Error parser rarities: " + e.getMessage());
                        }
                    } else if (path.equals("/api/types")) {
                        try {
                            TypeController controller = new TypeController(injectorProvider.getTypeMapperSupplier());
                            String response = controller.getAll();
                            sendResponse(exchange, 200, response);
                        } catch (Exception e) {
                            sendResponse(exchange, 500, "Error parser types: " + e.getMessage());
                        }
                    } else if (path.equals("/api/races")) {
                        try {
                            RaceController controller = new RaceController(injectorProvider.getRaceMapperSupplier());
                            String response = controller.getAll();
                            sendResponse(exchange, 200, response);
                        } catch (Exception e) {
                            sendResponse(exchange, 500, "Error parser races: " + e.getMessage());
                        }
                    } else if (path.equals("/api/formats")) {
                        try {
                            FormatController controller = new FormatController(injectorProvider.getFormatMapperSupplier());
                            String response = controller.getAll();
                            sendResponse(exchange, 200, response);
                        } catch (Exception e) {
                            sendResponse(exchange, 500, "Error parser formats: " + e.getMessage());
                        }
                    } else if (path.equals("/api/keywords")) {
                        try {
                            KeyWordController controller = new KeyWordController(injectorProvider.getKeyWordMapperSupplier());
                            String response = controller.getAll();
                            sendResponse(exchange, 200, response);
                        } catch (Exception e) {
                            sendResponse(exchange, 500, "Error parser keywords: " + e.getMessage());
                        }
                    } else {
                        sendResponse(exchange, 404, "404 Not Found");
                    }
                } else if (method.equals("POST")) {
                    String body = new String(exchange.getRequestBody().readAllBytes());

                    switch (path) {
                        case "/api/users/register" -> {
                            try {
                                String response = new UserController().registerUser(body);
                                sendResponse(exchange, 200, response);
                            } catch (Exception e) {
                                sendResponse(exchange, 500, "Error al registrar usuario: " + e.getMessage());
                            }
                        }
                        case "/api/stores/register" -> {
                            try {
                                String response = new StoreController().registerStore(body);
                                sendResponse(exchange, 200, response);
                            } catch (Exception e) {
                                sendResponse(exchange, 500, "Error al registrar tienda: " + e.getMessage());
                            }
                        }
                        default -> sendResponse(exchange, 404, "POST endpoint not found");
                    }
                }


            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static void stop() {
        isRunning = false;
    }

    public static void start() {
        isRunning = true;
    }

    private void sendResponse(HttpExchange exchange, int status, String body) throws IOException {
        exchange.sendResponseHeaders(status, body.getBytes().length);
        try (exchange; OutputStream os = exchange.getResponseBody()) {
            os.write(body.getBytes());
        }
    }
}
