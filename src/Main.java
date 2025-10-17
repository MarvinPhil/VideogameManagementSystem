import java.util.InputMismatchException;
import java.util.Scanner;

/*
*
* C:\Users\phili\OneDrive\Documents\video_game_samples.txt
*
* Marvin Philippe
*
* Version 1.0
*
* 10/09/2025
*
* CEN-3024C-13950
*
* This program is a database management system for video games
* This program will allow users to:
* - Add new video games to the database(before phase 4, data will be stored in hashmap) as text file or manually
* - Delete video game entries with ID number
* - See full list of video game entries
* - Update/Change video game entries
*
* Version 1.1:
* -delete function fixed
*
* */

public class Main {

    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        //Map<Integer, String> videoGameDB = new HashMap<>();
        VideoGameManager manager = new VideoGameManager();

        System.out.println("**************** Welcome to the VideoGame Management System ****************");

        int choice;

        do{
            try{

                System.out.println("--- Menu Options ---");
                System.out.println("1. Add video game entry manually");
                System.out.println("2. Add video game entry through text file");
                System.out.println("3. Update video game entry");
                System.out.println("4. Delete video game entry");
                System.out.println("5. View full list of video game entries");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");

                choice = scn.nextInt();
                scn.nextLine();
                switch (choice){
                    case 1:
                        String result;
                        System.out.println("\nAdd video game entry\n");
                        result = manager.addGame1(scn);
                        System.out.println(result);
                        break;
                    case 2:
                        String result2;
                        System.out.println("\nAdd video game entry through text file\n");
                        result2 = manager.addGame2(scn);
                        System.out.println(result2);
                        break;
                    case 3:
                        String result3;
                        System.out.println("\nUpdate video game entry\n");
                        result3 = manager.updateGame(scn);
                        System.out.println(result3);
                        break;
                    case 4:
                        String result4;
                        System.out.println("\nDelete video game entry\n");
                        result4 = manager.removeGame(scn);
                        System.out.println(result4);
                        break;
                    case 5:
                        String result5;
                        System.out.println("\nView full list of video game entries\n");
                        result5 = manager.viewAllGames();
                        System.out.println(result5);
                        break;
                    case 6:
                        String result6;
                        System.out.println("Calculate the average of all game entries\n");
                        result6 = manager.calculateAverageRating();
                        System.out.println(result6);
                    case 0:
                        System.out.println("\nEnding Program...\n");
                        break;
                    default:
                        System.out.println("\nInvalid choice");
                        break;
                }
            }catch (InputMismatchException e){
                System.out.println("\nInvalid choice, enter a number from 0-5\n");
                scn.nextLine();
                choice = -1;
            }




        }while(choice != 0);

        scn.close();
    }
}