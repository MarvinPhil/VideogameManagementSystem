
/**
 *Marvin Philippe
 *
 * CEN-3024C-13950
 *
 * 10/09/2025
 *
 * Class Name: VideoGame
 *
 * Description:
 * This class defines the structure of a VideoGame object used in the project.
 * Each VideoGame object represents one record in the system and contains attributes such as ID,
 * title, genre, release year, price, and rating. The class provides only the needed getter and setter methods
 * to allow controlled access and modification of these attributes but limit the amount of void methods.
 **/

public class VideoGame {
    private int gameID;
    private String title;
    private String genre;
    private int releaseYear;
    private double price;
    private double rating;

    /**
     * Constructor for initializing video game entries/objects
     *
     * @param gameID the game's unique Identification number
     * @param title name of the game
     * @param genre games's genre
     * @param releaseYear when the game came out
     * @param price how much the game cost
     * @param rating rating from 1-10, how good is the game
     */
    VideoGame(int gameID, String title, String genre, int releaseYear, double price, double rating) {
        this.gameID = gameID;
        this.title = title;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.price = price;
        this.rating = rating;
    }


    /**
     * initialize or change the param
     * @param Title
     */
    public void setTitle(String Title) {
        this.title = Title;
    }

    /**
     * initialize or change the param
     * @param Genre
     */
    public void setGenre(String Genre) {
        this.genre = Genre;
    }

    /**
     * initialize or change the param
     * @param Year
     */
    public void setReleaseYear(int Year) {
        this.releaseYear = Year;
    }

    /**
     * initialize or change the param
     * @param Price
     */
    public void setPrice(double Price) {
        this.price = Price;
    }

    /**
     * initialize or change the param
     * @param Rating
     */
    public void setRating(double Rating) {
        this.rating = Rating;
    }

    /**
     *
     * @return the attributes of this video game/entry
     */
    @Override
    public String toString() {
        return String.format(
                "ID: %d | Title: %s | Genre: %s | Year: %d | Price: $%.2f | Rating: %.1f",
                gameID, title, genre, releaseYear, price, rating
        );
    }

    /**
     *
     * @return
     */
    public double getRating() {
        return rating;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return
     */
    public String getGenre() {
        return genre;
    }

    /**
     *
     * @return
     */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     *
     * @return
     */
    public double getPrice() {
        return price;
    }

    /**
     *
     * @return
     */
    public Integer getGameID() {
        return gameID;
    }
}
