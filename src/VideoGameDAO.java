import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
*
* Marvin Philippe
*
* CEN-3024C-13950
*
* 11/05/2025
*
* This class acts as an intermediary between the GUI and database and separates
* the data access logic from the business logic in the system, to help keep in line with the MVC architecture.
* Taking the place and job of VideoGameManager when working with databases instead of hashmaps.
*
* */

public class VideoGameDAO {

    private Connection conn;

    /*
     * connectToDatabase
     * Purpose: Connects to the SQLite database using the user-provided file path.
     * dbPath - File path entered by the user.
     * return true if connection successful, false otherwise.
     */
    public boolean connectToDatabase(String dbPath) {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /*
     * getAllGames
     * Purpose: Returns all game records from the database.
     * returns an observable list of videogame objects
     */
    public ObservableList<VideoGame> getAllGames() {
        ObservableList<VideoGame> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM videogames";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new VideoGame(
                        rs.getInt("gameid"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getInt("releaseyear"),
                        rs.getDouble("price"),
                        rs.getDouble("rating")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /*
     * addGame
     * Inserts a new video game into the database.
     * arguments are the game's 6 attributes
     * returns true or false
     */
    public boolean addGame(String title, String genre, int year, double price, double rating) {
        String sql = "INSERT INTO videogames(title, genre, releaseyear, price, rating) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, genre);
            stmt.setInt(3, year);
            stmt.setDouble(4, price);
            stmt.setDouble(5, rating);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /*
     * updateGame
     * arguments are the game's 6 attributes
     * Updates an existing game record.
     * returns true or false
     */
    public boolean updateGame(int id, String title, String genre, int year, double price, double rating) {
        String sql = "UPDATE videogames SET title=?, genre=?, releaseyear=?, price=?, rating=? WHERE gameid=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.setString(2, genre);
            stmt.setInt(3, year);
            stmt.setDouble(4, price);
            stmt.setDouble(5, rating);
            stmt.setInt(6, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    /*
     * deleteGame
     * arguments is game id
     * returns true or false
     */
    public boolean deleteGame(int id) {
        String sql = "DELETE FROM videogames WHERE gameid=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /*
     * calculateAverageRating
     * performs caluclations for average in the database
     * returns a decimal point value
     */
    public double calculateAverageRating() {
        String sql = "SELECT AVG(rating) AS avg FROM videogames";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return rs.getDouble("avg");

        } catch (SQLException e) {
            return -1;
        }
    }
}
