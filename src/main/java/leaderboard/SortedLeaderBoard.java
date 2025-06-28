package leaderboard;
import java.util.List;
import java.util.Scanner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.resps.Tuple;

public class SortedLeaderBoard {

    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("localhost", 6379);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Ping Response: " + jedis.ping());

            String leaderboardKey = "game:leaderboard";
            jedis.del(leaderboardKey); // Clear any existing leaderboard

            while (true) {
                System.out.print("Enter player name (or type 'exit' to quit): ");
                String playerName = scanner.nextLine().trim();

                if (playerName.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting leaderboard program.");
                    break;
                }

                System.out.print("Enter score for " + playerName + ": ");
                double score;
                try {
                    score = Double.parseDouble(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid score. Try again.");
                    continue;
                }

                jedis.zadd(leaderboardKey, score, playerName);

                List<Tuple> leaderboard = jedis.zrevrangeWithScores(leaderboardKey, 0, -1);
                System.out.println("\n=== Leaderboard ===");
                int rank = 1;
                for (Tuple entry : leaderboard) {
                    System.out.printf("%d. %s => %.0f\n", rank++, entry.getElement(), entry.getScore());
                }
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//make sure redis-server is running
// compile : javac -cp "lib/jedis-5.1.0.jar" SortedLeaderBoard.java
// run : java -cp ".:lib/jedis-5.1.0.jar" SortedLeaderBoard