package ar.com.mylback;

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
                            sendResponse(exchange, 500, "Error parser");
                        }
                    } else {
                        sendResponse(exchange, 404, "404 Not Found");
                    }
                } else {
                    sendResponse(exchange, 404, "404 Not Found");
                }


            } catch (Exception e) {
                e.printStackTrace();
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
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body.getBytes());
        } finally {
            exchange.close();
        }
    }
}
