import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Scanner;

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
}