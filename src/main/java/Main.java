
import java.net.URI;
import java.net.http.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String BASE_URL = "http://localhost:8080/api";
    private static String jwtToken = null;
    private static List<Map<String, String>> spotifyPlaylists = new ArrayList<>();
    private static List<Map<String, String>> userPlaylists = new ArrayList<>();
    private static List<Map<String, String>> playlistTracks = new ArrayList<>();

    private static void printHeader() {
        System.out.println(
                "-------------------------------------------------------------------------------------------------------------------------------");
        System.out.println(
                ".______    __          ___   ____    ____  __       __       _______.___________.        ___      .______   .______   \n"
                        + //
                        "|   _  \\  |  |        /   \\  \\   \\  /   / |  |     |  |     /       |           |       /   \\     |   _  \\  |   _  \\  \n"
                        + //
                        "|  |_)  | |  |       /  ^  \\  \\   \\/   /  |  |     |  |    |   (----`---|  |----`      /  ^  \\    |  |_)  | |  |_)  | \n"
                        + //
                        "|   ___/  |  |      /  /_\\  \\  \\_    _/   |  |     |  |     \\   \\       |  |          /  /_\\  \\   |   ___/  |   ___/  \n"
                        + //
                        "|  |      |  `----./  _____  \\   |  |     |  `----.|  | .----)   |      |  |         /  _____  \\  |  |      |  |      \n"
                        + //
                        "| _|      |_______/__/     \\__\\  |__|     |_______||__| |_______/       |__|        /__/     \\__\\ | _|      | _|      \n"
                        + //
                        "                                                                                                                 ");
        System.out.println(
                "-------------------------------------------------------------------------------------------------------------------------------");
    };

    private static void printSuccess() {
        System.out.println(
                "      ___           ___           ___                    ___           ___           ___     \n" + //
                        "     /\\  \\         /\\  \\         /\\  \\                  /\\  \\         /\\  \\         /\\  \\    \n"
                        + //
                        "    _\\:\\  \\       /::\\  \\       /::\\  \\                 \\:\\  \\       /::\\  \\       /::\\  \\   \n"
                        + //
                        "   /\\ \\:\\  \\     /:/\\:\\  \\     /:/\\:\\  \\                 \\:\\  \\     /:/\\:\\  \\     /:/\\:\\  \\  \n"
                        + //
                        "  _\\:\\ \\:\\  \\   /:/  \\:\\  \\   /:/  \\:\\  \\            ___ /::\\  \\   /:/  \\:\\  \\   /:/  \\:\\  \\ \n"
                        + //
                        " /\\ \\:\\ \\:\\__\\ /:/__/ \\:\\__\\ /:/__/ \\:\\__\\          /\\  /:/\\:\\__\\ /:/__/ \\:\\__\\ /:/__/ \\:\\__\\\n"
                        + //
                        " \\:\\ \\:\\/:/  / \\:\\  \\ /:/  / \\:\\  \\ /:/  /          \\:\\/:/  \\/__/ \\:\\  \\ /:/  / \\:\\  \\ /:/  /\n"
                        + //
                        "  \\:\\ \\::/  /   \\:\\  /:/  /   \\:\\  /:/  /            \\::/__/       \\:\\  /:/  /   \\:\\  /:/  / \n"
                        + //
                        "   \\:\\/:/  /     \\:\\/:/  /     \\:\\/:/  /              \\:\\  \\        \\:\\/:/  /     \\:\\/:/  /  \n"
                        + //
                        "    \\::/  /       \\::/  /       \\::/  /                \\:\\__\\        \\::/  /       \\::/  /   \n"
                        + //
                        "     \\/__/         \\/__/         \\/__/                  \\/__/         \\/__/         \\/__/   ");
    };

    private static final String[] OPTIONS = {
            "1. Get all playlists",
            "2. Add a new playlist",
            "3. Manage playlists",
            "4. Exit"
    };

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
            System.out.println("----------------------------------------------------------------------------");
            System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
            System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
            System.out.println(" ");
            System.out.println("Welcome back, " + username + "! You have been authenticated ✅");
            System.out.println(" ");

            System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
            System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
            System.out.println("----------------------------------------------------------------------------");
        } else {
            System.out.println("❌ Oh no...Login failed" + response.body());
            System.out.println("Please try again.");
            System.out.println("If the problem persists, please contact support (Oops I didn't plan for something).");
            System.exit(1);
        }
    }

    private static void getUsersSavedPlaylists() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/playlists"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
        JSONArray jsonResponse = new JSONArray(response.body());
        userPlaylists.clear();
        for (int i = 0; i < jsonResponse.length(); i++) {
            JSONObject item = jsonResponse.getJSONObject(i);
            Map<String, String> playlist = new HashMap<>();
            playlist.put("spotifyPlaylistId", item.getString("spotifyPlaylistId"));
            playlist.put("name", item.getString("name"));
            userPlaylists.add(playlist);
        }

        for (int i = 0; i < userPlaylists.size(); i++) {
            System.out.println("----------------------------------------------------------------------------");
            System.out.println((i + 1) + ". " + userPlaylists.get(i).get("name"));
        }
    } else {
        System.out.println("Failed to retrieve playlists: " + response.body());
    }
    }

    private static void getSpotifyPlaylists() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/playlists/spotify"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            JSONArray jsonResponse = new JSONArray(response.body());
            spotifyPlaylists.clear();
            for (int i = 0; i < jsonResponse.length(); i++) {
                JSONObject item = jsonResponse.getJSONObject(i);
                Map<String, String> playlist = new HashMap<>();
                playlist.put("id", item.getString("id"));
                playlist.put("name", item.getString("name"));
                spotifyPlaylists.add(playlist);
            }

            for (int i = 0; i < spotifyPlaylists.size(); i++) {
                System.out.println("----------------------------------------------------------------------------");
                System.out.println((i + 1) + ". " + spotifyPlaylists.get(i).get("name"));
            }
        } else {
            System.out.println("Failed to retrieve playlists: " + response.body());
        }
    }

    private static void getPlaylistTracks(String id) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/playlists/" + id))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            JSONArray jsonResponse = new JSONArray(response.body());
            playlistTracks.clear();
            for (int i = 0; i < jsonResponse.length(); i++) {
                JSONObject item = jsonResponse.getJSONObject(i);
                Map<String, String> playlist = new HashMap<>();
                playlist.put("name", item.getString("name"));
                playlist.put("artist", item.getString("artist"));
                playlistTracks.add(playlist);
            }

            for (int i = 0; i < playlistTracks.size(); i++) {
                System.out.println("----------------------------------------------------------------------------");
                System.out.println(playlistTracks.get(i).get("name") + " | " + playlistTracks.get(i).get("artist"));
            }
        } else {
            System.out.println("Failed to retrieve tracks: " + response.body());
        }
    }

    private static void addSpotifyPlaylistToLibrary(String playlistId, String playlistName) throws Exception {
        String json = String.format("{\"name\": \"%s\", \"spotifyPlaylistId\": \"%s\"}", playlistName, playlistId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL + "/playlists"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("----------------------------------------------------------------------------");
            System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
            System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
            System.out.println(" ");
            System.out.println("Woohoo, " + playlistName + " was added to your library ✅");
            System.out.println(" ");

            System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
            System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
            System.out.println("----------------------------------------------------------------------------");
        } else {
            System.out.println("❌ Oh no...adding failed" + response.body());
            System.out.println("Please try again.");
            System.out.println("If the problem persists, please contact support (Oops I didn't plan for something).");
            System.exit(1);
        }
    }

    private static void handleAddPlaylistChoice(int choice) throws Exception {
        if (choice > 0 && choice <= spotifyPlaylists.size()) {
            Map<String, String> selectedPlaylist = spotifyPlaylists.get(choice - 1);
            String playlistId = selectedPlaylist.get("id");
            String playlistName = selectedPlaylist.get("name");
            addSpotifyPlaylistToLibrary(playlistId, playlistName);
        } else {
            System.out.println("❌ Invalid choice. Please select a valid playlist number.");
        }

    }

    private static void handleGetPlaylistTracklist(int choice) throws Exception {
        if (choice > 0 && choice <= userPlaylists.size()) {
            Map<String, String> selectedPlaylist = userPlaylists.get(choice - 1);
            String playlistId = selectedPlaylist.get("spotifyPlaylistId");
            getPlaylistTracks(playlistId);
        } else {
            System.out.println("❌ Invalid choice. Please select a valid playlist number.");
        }

    }

    private static void addPlaylistsLoop(Scanner scanner) throws Exception {
        boolean stillAdding = true;

        while (stillAdding) {
            System.out.println("Retrieving Spotify's playlists...");
            Thread.sleep(2000);
            getSpotifyPlaylists();
            System.out.println("----------------------------------------------------------------------------");
            System.out.println("Which playlist would you like to add to your library? (number)");
            int choice = scanner.nextInt();
            scanner.nextLine();
            handleAddPlaylistChoice(choice);
            System.out.println();
            System.out.println("Would you like to add another playlist? (yes/no)");
            String addAnother = scanner.nextLine();

            if (addAnother.equalsIgnoreCase("no")) {
                stillAdding = false;
            }
        }
    }

    private static void managePlaylistsLoop(Scanner scanner) throws Exception {
        boolean managingPlaylists = true;

        while (managingPlaylists) {
            System.out.println("Getting your playlists...");
            Thread.sleep(2000);
            getSpotifyPlaylists(); 
            System.out.println("----------------------------------------------------------------------------");
            System.out.println("Which playlist would you like to delete from your library?");
            int choice = scanner.nextInt();
            scanner.nextLine();
            // deletePlaylist(choice); 
            System.out.println();
            System.out.println("Would you like to delete another playlist? (yes/no)");
            String deleteAnother = scanner.nextLine();

            if (deleteAnother.equalsIgnoreCase("no")) {
                managingPlaylists = false; 
            }
        }
    }

    private static void getUserPlaylistsLoop(Scanner scanner) throws Exception {
        boolean getPlaylist = true;

        while (getPlaylist) {
            System.out.println("Getting your playlists...");
            Thread.sleep(2000);
            getUsersSavedPlaylists();
            System.out.println("----------------------------------------------------------------------------");
            System.out.println("Which playlist would you like to see their tracks?");
            int choice = scanner.nextInt();
            scanner.nextLine();
            handleGetPlaylistTracklist(choice); 
            System.out.println("œœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœœ");
            System.out.println();
            System.out.println("Would you like to see another playlist? (yes/no)");
            String seeAnother = scanner.nextLine();

            if (seeAnother.equalsIgnoreCase("no")) {
                getPlaylist = false; 
            }
        }
    }

    public static void main(String[] args) throws Exception {

        // Welcome and register
        Scanner scanner = new Scanner(System.in);
        printHeader();
        System.out.println("🎵 Welcome to the Playlist Console Client!🎵");
        System.out.println("[1] Select 1 to register your account!");
        System.out.print("Choose an option: ");

        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                System.out.print("Enter username: ");
                break;
            default:
                System.out.println("❌ Invalid option.");
        }

        String username = scanner.nextLine();
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("Welcome, " + username + "!");
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        System.out.println("Password entered: " + password);
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("Registering...");
        Thread.sleep(2000);
        register(username, password);
        Thread.sleep(1000);
        System.out.println("Attempting to login...");
        Thread.sleep(2000);
        login(username, password);
        Thread.sleep(1000);

        // Adding first playlist
        printSuccess();
        System.out.println(" ");
        System.out.println("||||| Let's get started with adding your FIRST playlist! |||||");
        System.out.println();

        boolean stillAdding = true;
        while (stillAdding) {
            System.out.println("Retrieving Spotify's playlists...");
            System.out.println();
            Thread.sleep(2000);
            getSpotifyPlaylists();
            System.out.println();
            System.out.println("Which playlist would you like to add to your library? (number)");
            int choice = scanner.nextInt();
            scanner.nextLine();
            handleAddPlaylistChoice(choice);

            System.out.println("Would you like to add another playlist? (yes/no)");
            String addAnother = scanner.nextLine();

            if (addAnother.equalsIgnoreCase("no")) {
                stillAdding = false;
            }
        }

        // Main menu
        boolean mainMenu = true;
        while (mainMenu) {
            System.out.println("-------------------------------*MAIN MENU*---------------------------------------");
            System.out.println(OPTIONS[0]);
            System.out.println(OPTIONS[1]);
            System.out.println(OPTIONS[2]);
            System.out.println(OPTIONS[3]);
            System.out.println("----------------------------------------------------------------------------");

            int menuChoice = scanner.nextInt();
            scanner.nextLine();

            switch (menuChoice) {
                case 1:
                    getUserPlaylistsLoop(scanner);
                    break;
                case 2:
                    addPlaylistsLoop(scanner);
                    break;
                case 3:
                    managePlaylistsLoop(scanner);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    mainMenu = false;
                    break;
                default:
                    System.out.println("❌ Invalid option.");
            }
        }

        scanner.close();

    }
}
