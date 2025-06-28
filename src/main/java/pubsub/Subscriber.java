package pubsub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class Subscriber
{
    public static void main(String[] args) {

        Jedis jedis = new Jedis("localhost", 6379);
        Scanner scanner = new Scanner(System.in);
        String channelName;

        JedisPubSub subscriber = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                System.out.println("Channel: " + channel);
                System.out.println("Received message: " + message);
                
                // Append to file
                try (FileWriter writer = new FileWriter("subscriber_log.txt", true)) {
                    writer.write("Channel: " + channel + "\n");
                    writer.write("Received message: " + message + "\n");
                    writer.write("-----\n");
                } catch (IOException e) {
                    System.err.println("Failed to write to file: " + e.getMessage());
                }
            }
        };

        System.out.println("Press Ctrl+C to exit");
        System.out.print("Enter Channel Name:");
        channelName = scanner.nextLine();
        jedis.subscribe(subscriber, channelName);
        scanner.close();
        jedis.close();
    }
}


//make sure redis-server is running
// compile : javac -cp "lib/*" Subscriber.java
// run : java -cp ".:lib/jedis-5.1.0.jar" Subscriber