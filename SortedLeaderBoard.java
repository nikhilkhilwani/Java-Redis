import redis.clients.jedis.Jedis;

public class SortedLeaderBoard {
    public static void main(String[] args) {
        // Connect to Redis running on localhost (default port 6379)
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            // Ping the server
            String response = jedis.ping();
            System.out.println("Ping Response: " + response);

            // Set a key
            jedis.set("name", "Nikhil");

            // Get the value
            String value = jedis.get("name");
            System.out.println("Stored name: " + value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//make sure redis-server is running
//compile : javac -cp ".:lib/jedis-5.1.0.jar" SortedLeaderBoard.java
// run : java -cp ".:lib/jedis-5.1.0.jar" SortedLeaderBoard
