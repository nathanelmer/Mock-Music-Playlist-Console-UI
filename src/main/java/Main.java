
import java.net.URI;
import java.net.http.*;
import java.util.Scanner;
import org.json.JSONObject;



public class Main {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String BASE_URL = "http://localhost:8080/api";
    private static String jwtToken = null;
    private static String spotifyToken = null;
    private static void printHeader() {
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
        System.out.println(".______    __          ___   ____    ____  __       __       _______.___________.        ___      .______   .______   \n" + //
                        "|   _  \\  |  |        /   \\  \\   \\  /   / |  |     |  |     /       |           |       /   \\     |   _  \\  |   _  \\  \n" + //
                        "|  |_)  | |  |       /  ^  \\  \\   \\/   /  |  |     |  |    |   (----`---|  |----`      /  ^  \\    |  |_)  | |  |_)  | \n" + //
                        "|   ___/  |  |      /  /_\\  \\  \\_    _/   |  |     |  |     \\   \\       |  |          /  /_\\  \\   |   ___/  |   ___/  \n" + //
                        "|  |      |  `----./  _____  \\   |  |     |  `----.|  | .----)   |      |  |         /  _____  \\  |  |      |  |      \n" + //
                        "| _|      |_______/__/     \\__\\  |__|     |_______||__| |_______/       |__|        /__/     \\__\\ | _|      | _|      \n" + //
                        "                                                                                                                 ");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
    };
    private static void printSuccess(){
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("                                               🎉 Success! 🎉");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
    };

    private static final String[] FIRSTLOGINOPTIONS = {
            "1. Get started with adding your first playlist!",
            "2. Exit"
    };

    private static final String[] OPTIONS = {
            "1. Get all playlists",
            "2. Add a new playlist",
            "3. Manage playlists",
            "4. Exit"
    };

    // private static void requireInput(Scanner scanner, String prompt) {
    //     while (prompt.isBlank()) {
    //         System.out.print("Response can't be empty: ");
    //         prompt = scanner.nextLine();

    //         if (prompt.isBlank()) {
    //             System.out.println("❌ Response can't be empty. Try again!");
    //             prompt = scanner.nextLine();
    //         } 
    //     }
    // }

    private static void register(String username, String password) throws Exception {
        String json = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/auth/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json)) 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("----------------------------------------------------------------------------");
            System.out.println("Welcome, " + username + "! You have been registered ✅");
            System.out.println("----------------------------------------------------------------------------");
        } else {
            System.out.println("❌ Oh no...Registration failed" + response.body());
            System.out.println("Please try again.");
            System.out.println("If the problem persists, please contact support (Oops I didn't plan for something)."); 
            System.out.println("----------------------------------------------------------------------------");
            System.exit(1);
        }
    }

    private static void login(String username, String password) throws Exception {
        String json = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json)) 
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
        JSONObject responseBody = new JSONObject(response.body());
        jwtToken = responseBody.getString("jwt");
        spotifyToken = responseBody.getString("spotifyToken");
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("Welcome back, " + username + "! You have been authenticated ✅");
        System.out.println("----------------------------------------------------------------------------");
        } else {
            System.out.println("❌ Oh no...Login failed" + response.body());
            System.out.println("Please try again.");
            System.out.println("If the problem persists, please contact support (Oops I didn't plan for something).");
            System.exit(1);
        }
    }

    // private static void getGenres() throws Exception {
    //     String json = String.format("{\"name\": \"%s\"}", name); // modify based on your backend DTO

    //     HttpRequest request = HttpRequest.newBuilder()
    //             .uri(new URI(BASE_URL))
    //             .header("Content-Type", "application/json")
    //             .POST(HttpRequest.BodyPublishers.ofString(json))
    //             .build();

    //     HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    //     System.out.println("Status: " + response.statusCode());
    //     System.out.println("Response: " + response.body());
    // }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        printHeader();
        System.out.println("🎵 Welcome to the Playlist Console Client!🎵");
        System.out.println("[1] Select 1 to register your account!");
        System.out.print("Choose an option: ");

        int option = scanner.nextInt();
        scanner.nextLine(); 

        switch (option) {
            case 1: System.out.print("Enter username: ");
            break;
            default: System.out.println("❌ Invalid option.");
        }

        String username = scanner.nextLine();
        // requireInput(scanner, username);
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("Welcome, " + username + "!");
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        System.out.println("Password entered: " + password);
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("Registering...");
        register(username, password);
        System.out.println("Attempting to login...");
        login(username, password);

        System.out.println(FIRSTLOGINOPTIONS[0]);
        System.out.println(FIRSTLOGINOPTIONS[1]);
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); 
        switch (choice) {
            case 1: {
                System.out.println("Let's Search Spotify's Playlists...");
                // call api endpoint to get genres
                // display genres
                // get user input for genre
                // call api endpoint to get playlists by genre
                // display playlists
                // get user input for playlist
                // ask user if they want to add the playlist
                // call api endpoint to add the playlist
                break;
            }
            case 2: {
                System.out.println("Exiting...");
                System.exit(1);
                break;
            }
        }

        System.out.println("Select a genre:");

        scanner.close();
    }
}
