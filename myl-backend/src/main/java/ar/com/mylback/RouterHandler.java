package ar.com.mylback;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.util.concurrent.BlockingQueue;

class RouterHandler implements HttpHandler {
    private final BlockingQueue<HttpExchange> queue;

    public RouterHandler(BlockingQueue<HttpExchange> queue) {
        this.queue = queue;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            // Just enqueue the request for processing
            queue.put(exchange); // Blocking, but won't block main thread
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
