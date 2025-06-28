package todolist;
import redis.clients.jedis.Jedis;
import java.util.*;

public class TodoApp {

    private static Jedis jedis;
    private static String userId;

    public static void main(String[] args) {
        // Initialize Redis connection
        jedis = new Jedis("localhost", 6379);

        // Get user id (simple multi‑tenant support)
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your user id: ");
        userId = scanner.nextLine().trim();
        if (userId.isEmpty()) {
            userId = "default";
        }

        // Main menu loop
        while (true) {
            showMenu();
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addTask(scanner);
                    break;
                case "2":
                    listTasks();
                    break;
                case "3":
                    markDone(scanner);
                    break;
                case "4":
                    deleteTask(scanner);
                    break;
                case "5":
                    System.out.println("Goodbye!");
                    jedis.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    /* ---------------- Redis key helpers ---------------- */

    private static String userTaskListKey() {
        return "user:" + userId + ":tasks";
    }

    /* ---------------- Menu actions ---------------- */

    // 1. Add Task
    private static void addTask(Scanner scanner) {
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Description: ");
        String desc = scanner.nextLine().trim();

        String taskId = "task:" + UUID.randomUUID();
        Map<String, String> taskData = new HashMap<>();
        taskData.put("title", title);
        taskData.put("desc", desc);
        taskData.put("status", "pending");
        taskData.put("created_at", Long.toString(System.currentTimeMillis()));

        jedis.hmset(taskId, taskData);
        jedis.lpush(userTaskListKey(), taskId);

        System.out.println("Task added with id " + taskId);
    }

    // 2. List Tasks
    private static void listTasks() {
        List<String> taskIds = jedis.lrange(userTaskListKey(), 0, -1);
        if (taskIds.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        System.out.println("\n---- Your Tasks ----");
        for (String taskId : taskIds) {
            Map<String, String> task = jedis.hgetAll(taskId);
            if (task.isEmpty()) continue; // Task may have been deleted
            System.out.printf("ID: %s%n", taskId);
            System.out.printf("Title: %s%n", task.get("title"));
            System.out.printf("Description: %s%n", task.get("desc"));
            System.out.printf("Status: %s%n", task.get("status"));
            System.out.println("--------------------");
        }
    }

    // 3. Mark Task as Done
    private static void markDone(Scanner scanner) {
        System.out.print("Enter task id to mark done: ");
        String taskId = scanner.nextLine().trim();
        if (!jedis.exists(taskId)) {
            System.out.println("Task not found.");
            return;
        }
        jedis.hset(taskId, "status", "done");
        System.out.println("Task marked as done.");
    }

    // 4. Delete Task
    private static void deleteTask(Scanner scanner) {
        System.out.print("Enter task id to delete: ");
        String taskId = scanner.nextLine().trim();

        Long removed = jedis.lrem(userTaskListKey(), 0, taskId);
        jedis.del(taskId);

        if (removed > 0) {
            System.out.println("Task deleted.");
        } else {
            System.out.println("Task id not in your list (or already deleted).");
        }
    }

    /* ---------------- Helpers ---------------- */

    private static void showMenu() {
        System.out.println("\n==== In‑Memory To‑Do List ====");
        System.out.println("1. Add Task");
        System.out.println("2. List Tasks");
        System.out.println("3. Mark Task as Done");
        System.out.println("4. Delete Task");
        System.out.println("5. Exit");
    }
}
