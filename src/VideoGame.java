
/*
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

    VideoGame(int gameID, String title, String genre, int releaseYear, double price, double rating) {
        this.gameID = gameID;
        this.title = title;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.price = price;
        this.rating = rating;
    }


    public void setTitle(String Title) {
        this.title = Title;
    }

    public void setGenre(String Genre) {
        this.genre = Genre;
    }

    public void setReleaseYear(int Year) {
        this.releaseYear = Year;
    }

    public void setPrice(double Price) {
        this.price = Price;
    }

    public void setRating(double Rating) {
        this.rating = Rating;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d | Title: %s | Genre: %s | Year: %d | Price: $%.2f | Rating: %.1f",
                gameID, title, genre, releaseYear, price, rating
        );
    }

    public double getRating() {
        return rating;
    }
}
