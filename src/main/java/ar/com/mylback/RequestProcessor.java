package ar.com.mylback;

import ar.com.mylback.controller.*;
import ar.com.mylback.dal.entities.cards.*;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.QueryString;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
                    if (path.equals("/help")) {
                        String response = "the server is ready. Available endpoints: \n";
                        response += "Request Cards GET: api/cards\n";
                        sendResponse(exchange, 200, response);
                    } else if (path.equals("/api/cards")) {
                        try {
                            CardsController controller = new CardsController();
                            String response = controller.getCardsEndpoint(queryString);
                            sendResponse(exchange, 200, response);
                        } catch (Exception e) {
                            sendResponse(exchange, 500, "Error parser cards: " + e.getMessage());
                        }
                    } else if (path.matches("/api/card/\\d+")) {
                        try {
                            String[] parts = path.split("/");
                            if (parts.length >= 4) {
                                CardsController controller = new CardsController();
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
                            CardsController controller = new CardsController();
                            String response = controller.getCardsByName(queryString);
                            sendResponse(exchange, 200, response);
                        } catch (Exception e) {
                            sendResponse(exchange, 500, "Error parser cards: " + e.getMessage());
                        }
                    } else if (path.equals("/api/collections")) {
                        try {
                            CardPropertiesController<Collection> controller = new CardPropertiesController<>();
                            String response = controller.getAllEndpoint(Collection.class);
                            sendResponse(exchange, 200, response);
                        } catch (Exception e) {
                            sendResponse(exchange, 500, "Error parser collections: " + e.getMessage());
                        }
                    } else if (path.equals("/api/rarities")) {
                        try {
                            CardPropertiesController<Rarity> controller = new CardPropertiesController<>();
                            String response = controller.getAllEndpoint(Rarity.class);
                            sendResponse(exchange, 200, response);
                        } catch (Exception e) {
                            sendResponse(exchange, 500, "Error parser rarities: " + e.getMessage());
                        }
                    } else if (path.equals("/api/types")) {
                        try {
                            CardPropertiesController<Type> controller = new CardPropertiesController<>();
                            String response = controller.getAllEndpoint(Type.class);
                            sendResponse(exchange, 200, response);
                        } catch (Exception e) {
                            sendResponse(exchange, 500, "Error parser types: " + e.getMessage());
                        }
                    } else if (path.equals("/api/races")) {
                        try {
                            CardPropertiesController<Race> controller = new CardPropertiesController<>();
                            String response = controller.getAllEndpoint(Race.class);
                            sendResponse(exchange, 200, response);
                        } catch (Exception e) {
                            sendResponse(exchange, 500, "Error parser races: " + e.getMessage());
                        }
                    } else if (path.equals("/api/formats")) {
                        try {
                            CardPropertiesController<Format> controller = new CardPropertiesController<>();
                            String response = controller.getAllEndpoint(Format.class);
                            sendResponse(exchange, 200, response);
                        } catch (Exception e) {
                            sendResponse(exchange, 500, "Error parser formats: " + e.getMessage());
                        }
                    } else if (path.equals("/api/keywords")) {
                        try {
                            CardPropertiesController<KeyWord> controller = new CardPropertiesController<>();
                            String response = controller.getAllEndpoint(KeyWord.class);
                            sendResponse(exchange, 200, response);
                        } catch (Exception e) {
                            sendResponse(exchange, 500, "Error parser keywords: " + e.getMessage());
                        }

                    } else if (path.equals("/api/stores")) {
                        try {
                            String query = exchange.getRequestURI().getQuery();
                            boolean isValid = true; // default
                            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

                            if (query != null) {
                                for (String param : query.split("&")) {
                                    String[] keyValue = param.split("=");
                                    if (keyValue.length == 2 && keyValue[0].equals("valid")) {
                                        isValid = Boolean.parseBoolean(keyValue[1]);
                                    }
                                }
                            }

                            StoreController controller = new StoreController();
                            String response = controller.getStoresByValidation(authHeader, isValid);
                            sendResponse(exchange, 200, response);

                        } catch (Exception e) {
                            sendResponse(exchange, 500, "Error al obtener tiendas: " + e.getMessage());
                        }
                    } else if (path.matches("/api/stores/[^/]+")) {
                        try {
                            String[] parts = path.split("/");
                            if (parts.length >= 4) {
                                String uuid = parts[3];
                                String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
                                StoreController controller = new StoreController();
                                String response = controller.getStoreByUuid(uuid, authHeader);
                                sendResponse(exchange, 200, response);
                            } else {
                                sendResponse(exchange, 400, "UUID inválido");
                            }
                        } catch (Exception e) {
                            sendResponse(exchange, 500, "Error al obtener tienda por UUID: " + e.getMessage());
                        }
                    }
                    else {
                        sendResponse(exchange, 404, "404 Not Found");
                    }

                } else if (method.equals("POST")) {
                    String body = new String(exchange.getRequestBody().readAllBytes());

                    switch (path) {
                        case "/api/players/register" -> {
                            try {
                                String response = new AuthController().registerPlayer(body);
                                int statusCode = response.contains("\"error\"") ? 400 : 200;
                                sendResponse(exchange, statusCode, response);
                            } catch (Exception e) {
                                sendResponse(exchange, 500, "Error al registrar usuario: " + e.getMessage());
                            }
                        }

                        case "/api/stores/register" -> {
                            try {
                                String response = new AuthController().registerStore(body);
                                int statusCode = response.contains("\"error\"") ? 400 : 200;
                                sendResponse(exchange, statusCode, response);
                            } catch (Exception e) {
                                sendResponse(exchange, 500, "Error al registrar tienda: " + e.getMessage());
                            }
                        }

                        case "/api/auth/login" -> {
                            try {
                                String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
                                String response = new AuthController().loginUser(authHeader);
                                int statusCode = response.contains("\"error\"") ? 400 : 200;
                                sendResponse(exchange, statusCode, response);
                            } catch (Exception e) {
                                sendResponse(exchange, 500, "{\"error\": \"Error en login: " + e.getMessage() + "\"}");
                            }
                        }

                        case "/api/admin/stores/validate" -> {
                            try {
                                String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
                                JsonObject json = new Gson().fromJson(body, JsonObject.class);
                                String uuid = json.get("uuid").getAsString();

                                StoreController controller = new StoreController();
                                String response = controller.validateStore(uuid, authHeader);

                                int statusCode = response.contains("\"error\"") ? 400 : 200;
                                sendResponse(exchange, statusCode, response);

                            } catch (Exception e) {
                                sendResponse(exchange, 500, "{\"error\": \"Error al validar tienda: " + e.getMessage() + "\"}");
                            }
                        }

                        default -> sendResponse(exchange, 404, "POST endpoint not found");
                    }

                }  else if (method.equals("PUT")) {
                    switch (path) {
                        case "/api/stores" -> {
                            String body = new String(exchange.getRequestBody().readAllBytes());
                            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

                            try {
                                String response = new StoreController().updateStore(body, authHeader);
                                int statusCode = response.contains("\"error\"") ? 400 : 200;
                                sendResponse(exchange, statusCode, response);
                            } catch (Exception e) {
                                sendResponse(exchange, 500, "{\"error\": \"Error inesperado al actualizar tienda: " + e.getMessage() + "\"}");
                            }
                        }
                        case "/api/players" -> {
                            String body = new String(exchange.getRequestBody().readAllBytes());
                            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

                            try {
                                String response = new UserController().updatePlayer(body, authHeader);
                                int statusCode = response.contains("\"error\"") ? 400 : 200;
                                sendResponse(exchange, statusCode, response);
                            } catch (Exception e) {
                                sendResponse(exchange, 500, "{\"error\": \"Error inesperado al actualizar jugador: " + e.getMessage() + "\"}");
                            }
                        }
                        default -> sendResponse(exchange, 404, "PUT endpoint not found");
                    }
                } else if (method.equals("DELETE")) {
                    switch (path) {
                        case "/api/users" -> {
                            try {
                                String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
                                String response = new AuthController().deleteAccount(authHeader);
                                int statusCode = response.contains("\"error\"") ? 400 : 200;
                                sendResponse(exchange, statusCode, response);
                            } catch (Exception e) {
                                sendResponse(exchange, 500, "{\"error\": \"Error al eliminar cuenta: " + e.getMessage() + "\"}");
                            }
                        }
                        default -> sendResponse(exchange, 404, "PUT endpoint not found");
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
