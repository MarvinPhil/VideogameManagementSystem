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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
        VBox leftPanel = buildControlPanel(primaryStage);

        // ---------- Bottom: Status Bar ----------
        statusLabel.setPadding(new Insets(10, 0, 10, 10));

        // ---------- Main Layout ----------
        BorderPane root = new BorderPane();
        root.setTop(titleLabel);
        root.setCenter(table);
        root.setLeft(leftPanel);
        root.setBottom(statusLabel);
        BorderPane.setMargin(leftPanel, new Insets(10));

        Scene scene = new Scene(root, 950, 600);
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
    private VBox buildControlPanel(Stage primaryStage) {
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
        Button loadFileBtn = new Button("Load From File");

        // ---------- Button Actions ----------

        // Add Game
        addBtn.setOnAction(e -> addGame(titleField, genreField, yearField, priceField, ratingField));

        // Update Game
        updateBtn.setOnAction(e -> updateGame(titleField, genreField, yearField, priceField, ratingField));

        // Delete Game
        deleteBtn.setOnAction(e -> deleteGame());

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

        // Load from File
        loadFileBtn.setOnAction(e -> loadFromFile(primaryStage));

        // Add all UI elements
        panel.getChildren().addAll(
                new Label("Game Details:"),
                titleField, genreField, yearField, priceField, ratingField,
                addBtn, updateBtn, deleteBtn, showAllBtn, avgBtn, loadFileBtn
        );

        return panel;
    }

    // ---------- Button Functionalities ----------

    private void addGame(TextField title, TextField genre, TextField year, TextField price, TextField rating) {
        try {
            String t = title.getText().trim();
            String g = genre.getText().trim();

            if (t.isEmpty() || g.isEmpty()) {
                showError("Title and Genre cannot be blank.");
                return;
            }

            int y = Integer.parseInt(year.getText().trim());
            if (y < 1950) {
                showError("Release year must be 1950 or later.");
                return;
            }

            double p = Double.parseDouble(price.getText().trim());
            if (p < 0) {
                showError("Price cannot be negative.");
                return;
            }

            double r = Double.parseDouble(rating.getText().trim());
            if (r < 0 || r > 10) {
                showError("Rating must be between 0 and 10.");
                return;
            }

            int id = data.size() + 1;
            VideoGame newGame = new VideoGame(id, t, g, y, p, r);
            manager.videogames.put(id, newGame);
            data.add(newGame);
            clearFields(title, genre, year, price, rating);
            showSuccess("Game added successfully!");
        } catch (NumberFormatException ex) {
            showError("Year, Price, and Rating must be numeric values.");
        } catch (Exception ex) {
            showError("Unexpected error while adding game.");
        }
    }

    private void updateGame(TextField title, TextField genre, TextField year, TextField price, TextField rating) {
        VideoGame selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a game to update.");
            return;
        }

        try {
            if (!title.getText().trim().isEmpty())
                selected.setTitle(title.getText().trim());
            if (!genre.getText().trim().isEmpty())
                selected.setGenre(genre.getText().trim());
            if (!year.getText().trim().isEmpty()) {
                int newYear = Integer.parseInt(year.getText().trim());
                if (newYear < 1950) {
                    showError("Year must be 1950 or later.");
                    return;
                }
                selected.setReleaseYear(newYear);
            }
            if (!price.getText().trim().isEmpty()) {
                double newPrice = Double.parseDouble(price.getText().trim());
                if (newPrice < 0) {
                    showError("Price cannot be negative.");
                    return;
                }
                selected.setPrice(newPrice);
            }
            if (!rating.getText().trim().isEmpty()) {
                double newRating = Double.parseDouble(rating.getText().trim());
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
    }

    private void deleteGame() {
        VideoGame selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a game to delete.");
            return;
        }

        manager.videogames.remove(selected.getGameID());
        data.remove(selected);
        table.refresh();
        showSuccess("Game deleted successfully!");
    }

    /**
     * Allows user to select a text file and loads valid data from it.
     * Handles blank, invalid, or non-existent file paths safely.
     */
    private void loadFromFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Game Data File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            showError("No file selected.");
            return;
        }

        if (!file.exists() || !file.canRead()) {
            showError("File not found or unreadable.");
            return;
        }

        int count = 0;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != 6) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String title = parts[1].trim();
                    String genre = parts[2].trim();
                    int year = Integer.parseInt(parts[3].trim());
                    double price = Double.parseDouble(parts[4].trim());
                    double rating = Double.parseDouble(parts[5].trim());

                    if (!manager.videogames.containsKey(id)) {
                        VideoGame game = new VideoGame(id, title, genre, year, price, rating);
                        manager.videogames.put(id, game);
                        data.add(game);
                        count++;
                    }
                } catch (NumberFormatException ignored) {
                    System.out.println("Skipping invalid numeric data: " + line);
                }
            }
            showSuccess(count + " game(s) loaded successfully.");
        } catch (FileNotFoundException e) {
            showError("File could not be opened.");
        }
    }

    // ---------- Utility Methods ----------

    private void clearFields(TextField... fields) {
        for (TextField f : fields) f.clear();
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red;");

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: green;");
    }

    public static void main(String[] args) {
        launch(args);
    }
}