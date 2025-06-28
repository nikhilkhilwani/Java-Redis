package shortener;
import java.util.Scanner;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class URL_Shortener {

    private static final String URL_PREFIX = "shorturl:";
    private static final String COUNTER_KEY = "urlcounter";
    private static final String DOMAIN = "http://localhost:8080/";

    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("localhost", 6379);
                Scanner scanner = new Scanner(System.in)) {

            System.out.println("Welcome to Java URL Shortner using Redis!!");

            while (true) {
                System.out.println("1. Shorten URL");
                System.out.println("2. See Actual Path of Short URL");
                System.out.println("3. Exit");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        System.out.println("Enter Full URL: ");
                        String longUrl = scanner.nextLine();
                        String shortCode = generateShortCode(jedis);
                        jedis.set(URL_PREFIX + shortCode, longUrl);
                        System.out.println("Shortened URL: " + DOMAIN + shortCode);
                        break;

                    case 2:
                        Set<String> keys = jedis.keys(URL_PREFIX + "*"); // shorturl:*
                        if (keys.isEmpty()) {
                            System.out.println("No short URLs stored yet.");
                            continue;
                        }
                        System.out.println("Available short codes:");
                        keys.stream()
                                .map(k -> k.substring(URL_PREFIX.length()))
                                .sorted()
                                .forEach(code -> System.out.print(code + "  "));
                        System.out.println("\n"); 
                        System.out.print("Enter short code to visit: ");
                        String code = scanner.nextLine().trim();
                        String original = jedis.get(URL_PREFIX + code);
                        if (original != null) {
                            System.out.println("Redirecting to: " + original);
                        } else {
                            System.out.println("Short code not found.");
                        }
                        break;

                    case 3:
                        System.out.println("Goodbye!!");
                        System.exit(0);

                    default:
                        System.out.println("Wrong Choice. Please Select a valid Number!");
                        break;
                }
            }
        }
    }

    private static String generateShortCode(Jedis jedis) {
        long id = jedis.incr(COUNTER_KEY);
        return toBase62(id);
    }

    private static String toBase62(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            int rem = (int) (num % 62);
            sb.append(BASE62.charAt(rem));
            num /= 62;
        }
        return sb.reverse().toString();
    }
}

// Make sure Redis-Server is running
// compile: javac -d . -cp ".:lib/jedis-5.1.0.jar" URL_Shortener.java
// run: java -cp ".:lib/jedis-5.1.0.jar" UrlShortner.URL_Shortener
