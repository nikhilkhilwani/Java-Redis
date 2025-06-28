package pubsub;
import redis.clients.jedis.*;
import java.util.Scanner;

public class Publisher {
    public static void main(String[] args) {

        Jedis jedis = new Jedis("localhost", 6379); // Connect to Redis
        Scanner scanner = new Scanner(System.in);
        
        String channelName;
        System.out.print("Typein Channel Name: ");
        channelName = scanner.nextLine();

        while (true) {
            System.out.print("Enter message: ");
            String message = scanner.nextLine();

            System.out.println("Type 'exit' to quit sending");

            if ("exit".equalsIgnoreCase(message)) {
                break;
            }

            jedis.publish(channelName, message);
        }
        scanner.close();
        jedis.close();
    }
}

//make sure redis-server is running
//compile : javac -cp "lib/*" Publisher.java
// run : java -cp ".:lib/jedis-5.1.0.jar" Publisher