import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

/*
 * Marvin Philippe
 *
 * CEN-3024C-13950
 *
 * 10/09/2025
 *
 * Class Name: VideoGameManager
 *
 * Description:
 * This class manages the collection of VideoGame objects in memory for the project.
 * It implements all CRUD operations (Create, Read, Update, Delete) and one custom feature:
 * calculating the average rating of all games. It also handles file reading and
* */

public class VideoGameManager {
    Map<Integer, VideoGame> videogames = new HashMap<>();

     //manually add games
    public String addGame1(Scanner scn){
        while(true){
            try{
                System.out.println("Enter Game ID");
                int gameID = scn.nextInt();
                scn.nextLine();


        // Check for duplicate ID
        if (videogames.containsKey(gameID)) {
            System.out.println("A game with that ID already exists. Please try again.\n");
            continue;
        }
                //make sure a game name is inputted
                String gameName = "";
                while (gameName.isEmpty()){
                    System.out.println("Enter Game Name: ");
                    gameName = scn.nextLine().trim();
                    if (gameName.isEmpty()){
                        System.out.println("Game Name cannot be empty. ");
                    }
                }

                //make sure a game genre is inputted
                String gameGenre = "";
                while (gameGenre.isEmpty()){
                    System.out.println("Enter Game Genre: ");
                    gameGenre = scn.nextLine().trim();
                    if (gameGenre.isEmpty()){
                        System.out.println("Game Genre cannot be empty. ");
                    }
                }


                System.out.println("Enter Game ReleaseYear: ");
                int gameReleaseYear = scn.nextInt();
                scn.nextLine();

                if (gameReleaseYear < 1950) {
                    System.out.println("Invalid release year. No video game existed before 1950.\n");
                    continue;
                }

                System.out.println("Enter Game Price: ");
                double gamePrice = scn.nextDouble();
                scn.nextLine();

                if (gamePrice < 0) {
                    System.out.println("Price cannot be negative.\n");
                    continue;
                }

                System.out.println("Enter Game Rating: ");
                double gameRating = scn.nextDouble();
                scn.nextLine();

                if (gameRating < 0 || gameRating > 10) {
                    System.out.println("Rating must be between 0 and 10.\n");
                    continue;
                }


                VideoGame entry = new VideoGame(gameID, gameName, gameGenre, gameReleaseYear, gamePrice, gameRating);
                videogames.put(gameID, entry);

                return "Game added successfully\n";

            }catch (InputMismatchException e){
                System.out.println("\nInvalid input type. Please try again.\n");
                scn.nextLine();
            }

        }



    }
     public String removeGame(Scanner scn){
        while(true){
            try{

                if (videogames.isEmpty()) {
                    System.out.println("No games available to delete.\n");
                    return "Returning to main menu...\n";
                }

                System.out.println("Enter the Game ID of the game you would like to remove.");
                int gameID = scn.nextInt();
                scn.nextLine();

                if(!videogames.containsKey(gameID)){
                    System.out.println("Game with that ID does not exist. Please try again.\n");
                    continue;
                }

                return "Game removed successfully\n";

            } catch (InputMismatchException e){
                System.out.println("\nInvalid input type. Please try again.\n");
                scn.nextLine();
            }
        }
     }
     public String updateGame(Scanner scn){
        while(true){
            try{

                if (videogames.isEmpty()) {
                    System.out.println("No games available to update.\n");
                    return "Returning to main menu...\n";
                }

                System.out.println("Enter the Game ID of the game you would like to update.");
                int gameID = scn.nextInt();
                scn.nextLine();

                if(!videogames.containsKey(gameID)){
                    System.out.println("Game with that ID does not exist. Please try again.\n");
                    continue;
                }

                VideoGame game = videogames.get(gameID);
                System.out.println("\nCurrent details:");
                System.out.println(game);

                System.out.println("\nWhich field would you like to update?");
                System.out.println("1. Title");
                System.out.println("2. Genre");
                System.out.println("3. Release Year");
                System.out.println("4. Price");
                System.out.println("5. Rating");
                System.out.println("0. Cancel");

                System.out.print("Enter your choice: ");
                int fieldChoice = scn.nextInt();
                scn.nextLine();

                switch (fieldChoice) {
                    case 1:
                        System.out.print("Enter new Title: ");
                        String newTitle = scn.nextLine().trim();
                        if (!newTitle.isBlank()) {
                            game.setTitle(newTitle);
                        } else {
                            System.out.println("Title cannot be blank.");
                        }
                        break;

                    case 2:
                        System.out.print("Enter new Genre: ");
                        String newGenre = scn.nextLine().trim();
                        if (!newGenre.isBlank()) {
                            game.setGenre(newGenre);
                        } else {
                            System.out.println("Genre cannot be blank.");
                        }
                        break;

                    case 3:
                        System.out.print("Enter new Release Year: ");
                        int newYear = scn.nextInt();
                        scn.nextLine();
                        if (newYear >= 1970 && newYear <= 2025) {
                            game.setReleaseYear(newYear);
                        } else {
                            System.out.println("Invalid year. Must be 1970–2025.");
                        }
                        break;

                    case 4:
                        System.out.print("Enter new Price: ");
                        double newPrice = scn.nextDouble();
                        scn.nextLine();
                        if (newPrice >= 0) {
                            game.setPrice(newPrice);
                        } else {
                            System.out.println("Price cannot be negative.");
                        }
                        break;

                    case 5:
                        System.out.print("Enter new Rating (0–10): ");
                        double newRating = scn.nextDouble();
                        scn.nextLine();
                        if (newRating >= 0 && newRating <= 10) {
                            game.setRating(newRating);
                        } else {
                            System.out.println("Rating must be between 0 and 10.");
                        }
                        break;

                    case 0:
                        System.out.println("Update canceled.\n");
                        return "Returning to main menu...\n";

                    default:
                        System.out.println("Invalid choice.");
                        continue;
                }

                System.out.println("\nUpdated details:");
                System.out.println(game);

                return "Game updated successfully\n";

            }catch (InputMismatchException e){
                System.out.println("\nInvalid input type. Please try again.\n");
                scn.nextLine();
            }
        }
     }

    public String viewAllGames() {
        if (videogames.isEmpty()) {
            return "No games available to display.\n";
        }

        System.out.println("\n--- List of Video Games ---");
        for (VideoGame g : videogames.values()) {
            System.out.println(g);
        }
        System.out.println("----------------------------\n");
        return "Displayed all games.\n";
    }

    // add games from file
     public String addGame2(Scanner scn) {
         System.out.print("Enter the file path: ");
         String filePath = scn.nextLine().trim();

         if (filePath.isBlank()) {
             return "File path cannot be blank.\n";
         }

         File file = new File(filePath);
         if (!file.exists()) {
             return "File not found. Please check the file name and try again.\n";
         }

         int count = 0;
         try (Scanner fileScanner = new Scanner(file)) {
             while (fileScanner.hasNextLine()) {
                 String line = fileScanner.nextLine();
                 if (line.isBlank()) continue;

                 String[] parts = line.split(",");
                 if (parts.length != 6) {
                     System.out.println("Invalid line format, skipping: " + line);
                     continue;
                 }

                 try {
                     int id = Integer.parseInt(parts[0].trim());
                     String title = parts[1].trim();
                     String genre = parts[2].trim();
                     int year = Integer.parseInt(parts[3].trim());
                     double price = Double.parseDouble(parts[4].trim());
                     double rating = Double.parseDouble(parts[5].trim());

                     if (videogames.containsKey(id)) {
                         System.out.println("Duplicate ID found, skipping: " + id);
                         continue;
                     }

                     VideoGame vg = new VideoGame(id, title, genre, year, price, rating);
                     videogames.put(id, vg);
                     count++;

                 } catch (NumberFormatException ex) {
                     System.out.println("Invalid data type in line, skipping: " + line);
                 }
             }
         } catch (FileNotFoundException e) {
             return "Error: Could not open the file.\n";
         }

         return count + " game(s) added successfully from file.\n";
     }

    public String calculateAverageRating() {
        if (videogames.isEmpty()) {
            return "No games available to calculate average rating.\n";
        }

        double total = 0;
        for (VideoGame vg : videogames.values()) {
            total += vg.getRating();
        }

        double avg = total / videogames.size();
        return String.format("Average Rating of All Games: %.2f\n", avg);
    }
}
