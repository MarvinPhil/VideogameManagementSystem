/*
* Marvin Philippe
*
* CEN-3024C-13950
*
* 10/26/2025
*
* This class provides a graphical user interface (GUI) for the Video Game Management System.
* */

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class VideoGameManagerGUI extends Application {

    private VideoGameManager manager = new VideoGameManager();
    private TableView<VideoGame> table = new TableView<>();
    private ObservableList<VideoGame> data = FXCollections.observableArrayList();
    private Label statusLabel = new Label("Ready.");

    @Override
    public void start(Stage primaryStage) {
        // ---------- Top: App Title ----------
        Label titleLabel = new Label("Video Game Manager");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-padding: 10;");

        // ---------- Center: TableView ----------
        setupTable();

        // ---------- Left Panel: Form + Buttons ----------
        VBox leftPanel = buildControlPanel();

        // ---------- Bottom: Status Bar ----------
        statusLabel.setPadding(new Insets(10, 0, 10, 10));

        // ---------- Main Layout ----------
        BorderPane root = new BorderPane();
        root.setTop(titleLabel);
        root.setCenter(table);
        root.setLeft(leftPanel);
        root.setBottom(statusLabel);
        BorderPane.setMargin(leftPanel, new Insets(10));

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Video Game Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Sets up the TableView columns to display VideoGame objects.
     */
    private void setupTable() {
        TableColumn<VideoGame, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTitle()));

        TableColumn<VideoGame, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getGenre()));

        TableColumn<VideoGame, Number> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getReleaseYear()));

        TableColumn<VideoGame, Number> priceCol = new TableColumn<>("Price ($)");
        priceCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPrice()));

        TableColumn<VideoGame, Number> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getRating()));

        table.getColumns().addAll(titleCol, genreCol, yearCol, priceCol, ratingCol);
        table.setItems(data);
    }

    /**
     * Builds the left control panel with input fields and buttons.
     */
    private VBox buildControlPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField genreField = new TextField();
        genreField.setPromptText("Genre");

        TextField yearField = new TextField();
        yearField.setPromptText("Year");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        TextField ratingField = new TextField();
        ratingField.setPromptText("Rating");

        // Buttons
        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button showAllBtn = new Button("Show All");
        Button avgBtn = new Button("Average Rating");

        // ---------- Button Actions ----------

        // Add Game
        addBtn.setOnAction(e -> {
            try {
                String title = titleField.getText().trim();
                String genre = genreField.getText().trim();

                if (title.isEmpty() || genre.isEmpty()) {
                    showError("Title and Genre cannot be blank.");
                    return;
                }

                int year = Integer.parseInt(yearField.getText().trim());
                if (year < 1950) {
                    showError("Release year must be 1950 or later.");
                    return;
                }

                double price = Double.parseDouble(priceField.getText().trim());
                if (price < 0) {
                    showError("Price cannot be negative.");
                    return;
                }

                double rating = Double.parseDouble(ratingField.getText().trim());
                if (rating < 0 || rating > 10) {
                    showError("Rating must be between 0 and 10.");
                    return;
                }

                int id = data.size() + 1; // simple auto-ID
                VideoGame newGame = new VideoGame(id, title, genre, year, price, rating);
                manager.videogames.put(id, newGame);
                data.add(newGame);
                clearFields(titleField, genreField, yearField, priceField, ratingField);
                showSuccess("Game added successfully!");

            } catch (NumberFormatException ex) {
                showError("Year, Price, and Rating must be numeric values.");
            } catch (Exception ex) {
                showError("Unexpected error while adding game.");
            }
        });

        // Update Game
        updateBtn.setOnAction(e -> {
            VideoGame selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Please select a game to update.");
                return;
            }

            try {
                if (!titleField.getText().trim().isEmpty())
                    selected.setTitle(titleField.getText().trim());
                if (!genreField.getText().trim().isEmpty())
                    selected.setGenre(genreField.getText().trim());
                if (!yearField.getText().trim().isEmpty()) {
                    int newYear = Integer.parseInt(yearField.getText().trim());
                    if (newYear < 1950) {
                        showError("Year must be 1950 or later.");
                        return;
                    }
                    selected.setReleaseYear(newYear);
                }
                if (!priceField.getText().trim().isEmpty()) {
                    double newPrice = Double.parseDouble(priceField.getText().trim());
                    if (newPrice < 0) {
                        showError("Price cannot be negative.");
                        return;
                    }
                    selected.setPrice(newPrice);
                }
                if (!ratingField.getText().trim().isEmpty()) {
                    double newRating = Double.parseDouble(ratingField.getText().trim());
                    if (newRating < 0 || newRating > 10) {
                        showError("Rating must be between 0 and 10.");
                        return;
                    }
                    selected.setRating(newRating);
                }

                table.refresh();
                showSuccess("Game updated successfully!");
            } catch (NumberFormatException ex) {
                showError("Invalid numeric input.");
            }
        });

        // Delete Game
        deleteBtn.setOnAction(e -> {
            VideoGame selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Please select a game to delete.");
                return;
            }

            manager.videogames.remove(selected.getGameID());
            data.remove(selected);
            table.refresh();
            showSuccess("Game deleted successfully!");
        });

        // Show All
        showAllBtn.setOnAction(e -> {
            if (manager.videogames.isEmpty()) {
                showError("No games available to display.");
                return;
            }
            data.setAll(manager.videogames.values());
            table.refresh();
            showSuccess("All games displayed.");
        });

        // Custom Action: Average Rating
        avgBtn.setOnAction(e -> {
            if (manager.videogames.isEmpty()) {
                showError("No games available to calculate average rating.");
                return;
            }
            double total = manager.videogames.values().stream().mapToDouble(VideoGame::getRating).sum();
            double avg = total / manager.videogames.size();
            showSuccess(String.format("Average Rating: %.2f", avg));
        });

        // Add all UI elements
        panel.getChildren().addAll(
                new Label("Game Details:"),
                titleField, genreField, yearField, priceField, ratingField,
                addBtn, updateBtn, deleteBtn, showAllBtn, avgBtn
        );

        return panel;
    }

    /**
     * Clears input fields after adding or updating a game.
     */
    private void clearFields(TextField... fields) {
        for (TextField f : fields) {
            f.clear();
        }
    }

    /**
     * Displays an error message in red text and optionally with an alert box.
     */
    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red;");

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a success message in green text.
     */
    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: green;");
    }

    public static void main(String[] args) {
        launch(args);
    }
}