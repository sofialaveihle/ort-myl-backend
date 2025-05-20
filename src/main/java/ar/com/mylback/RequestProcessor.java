package ar.com.mylback;

import ar.com.mylback.controller.CardPropertiesController;
import ar.com.mylback.dal.entities.*;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.controller.CardsController;
import ar.com.mylback.utils.QueryString;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;

public class RequestProcessor implements Runnable {
    private final BlockingQueue<HttpExchange> queue;
    private static volatile boolean isRunning;

    public RequestProcessor(BlockingQueue<HttpExchange> queue) throws MylException {
        if (queue != null) {
            this.queue = queue;
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
                    switch (path) {
                        case "/help" -> {
                            String response = "the server is ready. Available endpoints: \n";
                            response += "Request Cards GET: api/cards\n";
                            sendResponse(exchange, 200, response);
                        }
                        case "/api/cards" -> {
                            try {
                                CardsController controller = new CardsController();
                                String response = controller.getCardsEndpoint(queryString);
                                sendResponse(exchange, 200, response);
                            } catch (Exception e) {
                                sendResponse(exchange, 500, "Error parser cards: " + e.getMessage());
                            }
                        }
                        case "/api/collections" -> {
                            try {
                                CardPropertiesController<Collection> controller = new CardPropertiesController<>();
                                String response = controller.getAll(Collection.class);
                                sendResponse(exchange, 200, response);
                            } catch (Exception e) {
                                sendResponse(exchange, 500, "Error parser collections: " + e.getMessage());
                            }
                        }
                        case "/api/rarities" -> {
                            try {
                                CardPropertiesController<Rarity> controller = new CardPropertiesController<>();
                                String response = controller.getAll(Rarity.class);
                                sendResponse(exchange, 200, response);
                            } catch (Exception e) {
                                sendResponse(exchange, 500, "Error parser rarities: " + e.getMessage());
                            }
                        }
                        case "/api/types" -> {
                            try {
                                CardPropertiesController<Type> controller = new CardPropertiesController<>();
                                String response = controller.getAll(Type.class);
                                sendResponse(exchange, 200, response);
                            } catch (Exception e) {
                                sendResponse(exchange, 500, "Error parser types: " + e.getMessage());
                            }
                        }
                        case "/api/races" -> {
                            try {
                                CardPropertiesController<Race> controller = new CardPropertiesController<>();
                                String response = controller.getAll(Race.class);
                                sendResponse(exchange, 200, response);
                            } catch (Exception e) {
                                sendResponse(exchange, 500, "Error parser races: " + e.getMessage());
                            }
                        }
                        case "/api/formats" -> {
                            try {
                                CardPropertiesController<Format> controller = new CardPropertiesController<>();
                                String response = controller.getAll(Format.class);
                                sendResponse(exchange, 200, response);
                            } catch (Exception e) {
                                sendResponse(exchange, 500, "Error parser formats: " + e.getMessage());
                            }
                        }
                        case "/api/keywords" -> {
                            try {
                                CardPropertiesController<KeyWord> controller = new CardPropertiesController<>();
                                String response = controller.getAll(KeyWord.class);
                                sendResponse(exchange, 200, response);
                            } catch (Exception e) {
                                sendResponse(exchange, 500, "Error parser keywords: " + e.getMessage());
                            }
                        }
                        default -> sendResponse(exchange, 404, "404 Not Found");
                    }
                } else {
                    sendResponse(exchange, 404, "404 Not Found");
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
