import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/*
*VideoGameDAO
*This class is a simple Data Access Object for the videogames table in SQLite.
*
*
*
* */
public class VideoGameDAO {
    private Connection conn = null;

    /**
     * Connects to an SQLite database file. The path must be provided by the user.
     */
    public void connect(String dbFilePath) throws SQLException {
        if (conn != null && !conn.isClosed()) return;
        String url = "jdbc:sqlite:" + dbFilePath;
        conn = DriverManager.getConnection(url);
        // ensure table exists
        createTableIfNotExists();
    }

    public void close() {
        try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
    }

    public void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS videogames (\n"
                + " gameid INTEGER PRIMARY KEY,\n"
                + " title TEXT NOT NULL,\n"
                + " genre TEXT NOT NULL,\n"
                + " releaseyear INTEGER,\n"
                + " price REAL,\n"
                + " rating REAL\n"
                + ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public List<VideoGame> listAll() throws SQLException {
        List<VideoGame> list = new ArrayList<>();
        String sql = "SELECT gameid, title, genre, releaseyear, price, rating FROM videogames ORDER BY gameid";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                VideoGame g = new VideoGame(
                        rs.getInt("gameid"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getInt("releaseyear"),
                        rs.getDouble("price"),
                        rs.getDouble("rating")
                );
                list.add(g);
            }
        }
        return list;
    }

    public boolean add(VideoGame g) throws SQLException {
        String sql = "INSERT INTO videogames(gameid, title, genre, releaseyear, price, rating) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, g.getGameID());
            ps.setString(2, g.getTitle());
            ps.setString(3, g.getGenre());
            ps.setInt(4, g.getReleaseYear());
            ps.setDouble(5, g.getPrice());
            ps.setDouble(6, g.getRating());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean update(VideoGame g) throws SQLException {
        String sql = "UPDATE videogames SET title=?, genre=?, releaseyear=?, price=?, rating=? WHERE gameid=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, g.getTitle());
            ps.setString(2, g.getGenre());
            ps.setInt(3, g.getReleaseYear());
            ps.setDouble(4, g.getPrice());
            ps.setDouble(5, g.getRating());
            ps.setInt(6, g.getGameID());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int gameId) throws SQLException {
        String sql = "DELETE FROM videogames WHERE gameid = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, gameId);
            return ps.executeUpdate() == 1;
        }
    }

    public double averageRating() throws SQLException {
        String sql = "SELECT AVG(rating) AS avgRating FROM videogames";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.getDouble("avgRating");
        }
    }

    // convenience: check if ID exists
    public boolean exists(int gameId) throws SQLException {
        String sql = "SELECT 1 FROM videogames WHERE gameid = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, gameId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
