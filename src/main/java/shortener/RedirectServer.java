package shortener;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RedirectServer {

    private static final int PORT = 8080;
    private static final String REDIS_HOST = "localhost";
    private static final String PREFIX = "shorturl:";   // same key pattern

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", new RedirectHandler());
        server.start();
        System.out.println("Redirect server running on http://localhost:" + PORT);
    }

    static class RedirectHandler implements HttpHandler {
        @Override public void handle(HttpExchange ex) throws IOException {
            String code = ex.getRequestURI().getPath().substring(1);   // "/abc" → "abc"
            try (Jedis jedis = new Jedis(REDIS_HOST, 6379)) {
                String longUrl = jedis.get(PREFIX + code);
                if (longUrl != null) {
                    ex.getResponseHeaders().add("Location", longUrl);
                    ex.sendResponseHeaders(302, -1);                   // 302 Redirect, no body
                } else {
                    byte[] msg = "Short URL not found".getBytes();
                    ex.sendResponseHeaders(404, msg.length);
                    ex.getResponseBody().write(msg);
                }
            }
            ex.close();
        }
    }
}


//Make sure redis server is running
// compile: javac -d . -cp ".:lib/jedis-5.1.0.jar" RedirectServer.java
// run: java -cp ".:lib/jedis-5.1.0.jar" UrlShortner.RedirectServer

