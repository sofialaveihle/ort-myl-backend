package ar.com.mylback;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;
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

        // Start workers
        for (int i = 0; i < 4; i++) {
            try {
                workerPool.submit(new RequestProcessor(requestQueue));
            } catch (Exception e) {
                System.err.println("Fail to start worker thread " + (i + 1) + ": " + e.getMessage());
            }
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
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                if ("exit".equalsIgnoreCase(line.trim())) {
                    server.stop(0);
                    RequestProcessor.stop();
                    workerPool.shutdown();
                    System.out.println("Server stopped");
                    break;
                }
            }
        }).start();
    }
}
