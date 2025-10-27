import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

/*
 * Marvin Philippe
 *
 * CEN-3024C-13950
 *
 * 10/26/2025
 *
 * This class performs tests for the methods from VideoGameManager
 * */

public class VideoGameManagerTest {

    private VideoGameManager manager;

    @BeforeEach
    void setUp() {
        manager = new VideoGameManager();
    }

    @Test
    void testAddGame1() {
        String input = "1\nHalo\nShooter\n2001\n59.99\n9.5\n";
        Scanner scn = new Scanner(input);
        String result = manager.addGame1(scn);
        assertTrue(result.contains("successfully"));
        assertEquals(1, manager.videogames.size());
    }

    @Test
    void testRemoveGame() {
        // Add a game manually
        manager.videogames.put(1, new VideoGame(1, "Halo", "Shooter", 2001, 59.99, 9.5));

        String input = "1\nyes\n"; // Game ID + confirm
        Scanner scn = new Scanner(input);
        String result = manager.removeGame(scn);

        assertTrue(result.contains("successfully"));
        assertFalse(manager.videogames.containsKey(1));
    }

    @Test
    void testAverageRating() {
        manager.videogames.put(1, new VideoGame(1, "Halo", "Shooter", 2001, 59.99, 9.5));
        manager.videogames.put(2, new VideoGame(2, "Zelda", "Adventure", 2017, 59.99, 10.0));

        String result = manager.calculateAverageRating();
        assertTrue(result.contains("9.75"));
    }

    @Test
    void testAddGame2() throws Exception {
        // Create a temporary file with valid game data
        File tempFile = File.createTempFile("games", ".txt");
        tempFile.deleteOnExit();

        try (PrintWriter writer = new PrintWriter(tempFile)) {
            writer.println("1,Halo Infinite,Shooter,2021,59.99,8.9");
            writer.println("2,Forza Horizon 5,Racing,2021,49.99,9.1");
        }

        // Simulate user typing the file path
        String input = tempFile.getAbsolutePath() + "\n";
        Scanner scn = new Scanner(input);

        String result = manager.addGame2(scn);

        // Verify that two games were added successfully
        assertTrue(result.contains("2 game"));
        assertEquals(2, manager.videogames.size());
        assertTrue(manager.videogames.containsKey(1));
        assertTrue(manager.videogames.containsKey(2));
    }

    @Test
    void testUpdateGame() {
        // Add an existing game
        manager.videogames.put(1, new VideoGame(1, "Halo", "Shooter", 2001, 59.99, 9.5));


        String input = "1\n1\nHalo 2\n";
        Scanner scn = new Scanner(input);

        String result = manager.updateGame(scn);

        assertTrue(result.contains("successfully"));
        assertEquals("Halo 2", manager.videogames.get(1).toString().contains("Halo 2") ? "Halo 2" : "");
    }
}