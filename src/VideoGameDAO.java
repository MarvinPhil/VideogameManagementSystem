import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
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

    /**
     * connectToDatabase
     * Purpose: Connects to the SQLite database using the user-provided file path.
     * @param dbPath - File path entered by the user.
     * @return true if connection successful, false otherwise.
     */
    public boolean connectToDatabase(String dbPath) {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * getAllGames
     * Purpose: Returns all game records from the database.
     * @return an observable list of videogame objects
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

    /**
     * addGame
     * Inserts a new video game into the database.
     * arguments are the game's 6 attributes
     * @param title
     * @param genre
     * @param year
     * @param price
     * @param rating
     * @return true or false
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

    /**
     * updateGame
     * Updates an existing game record.
     * @param title
     * @param genre
     * @param year
     * @param price
     * @param rating
     * @return true or false
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

    /**
     * updateGamePartial
     * Purpose: Updates only the fields the user actually filled in.
     * @param id     - ID of the game to update
     * @param title  - new title OR null/blank to skip
     * @param genre  - new genre OR null/blank to skip
     * @param year   - new release year OR null/negative to skip
     * @param price  - new price OR negative to skip
     * @param rating - new rating OR negative to skip
     * @return true if update successful, false otherwise
     */
    public boolean updateGamePartial(int id, String title, String genre,
                                     Integer year, Double price, Double rating) {

        try {
            // Build SQL dynamically based on which fields are valid
            StringBuilder sql = new StringBuilder("UPDATE videogames SET ");
            boolean needsComma = false;

            if (title != null && !title.isBlank()) {
                sql.append("title=?");
                needsComma = true;
            }
            if (genre != null && !genre.isBlank()) {
                if (needsComma) sql.append(", ");
                sql.append("genre=?");
                needsComma = true;
            }
            if (year != null && year >= 1950 && year <= 2025) {
                if (needsComma) sql.append(", ");
                sql.append("releaseyear=?");
                needsComma = true;
            }
            if (price != null && price >= 0) {
                if (needsComma) sql.append(", ");
                sql.append("price=?");
                needsComma = true;
            }
            if (rating != null && rating >= 0 && rating <= 10) {
                if (needsComma) sql.append(", ");
                sql.append("rating=?");
            }

            sql.append(" WHERE gameid=?");

            PreparedStatement stmt = conn.prepareStatement(sql.toString());

            // Fill in parameters in correct order
            int index = 1;

            if (title != null && !title.isBlank()) stmt.setString(index++, title);
            if (genre != null && !genre.isBlank()) stmt.setString(index++, genre);
            if (year != null && year >= 1950 && year <= 2025) stmt.setInt(index++, year);
            if (price != null && price >= 0) stmt.setDouble(index++, price);
            if (rating != null && rating >= 0 && rating <= 10) stmt.setDouble(index++, rating);

            stmt.setInt(index, id);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * deleteGame
     * @param id
     * @return true or false
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

    /**
     * calculateAverageRating
     * performs caluclations for average in the database
     * @return a decimal point value
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
