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
                int id = data.size() + 1; // simple auto-ID
                String title = titleField.getText().trim();
                String genre = genreField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
                double rating = Double.parseDouble(ratingField.getText().trim());

                VideoGame newGame = new VideoGame(id, title, genre, year, price, rating);
                manager.videogames.put(id, newGame);
                data.add(newGame);
                clearFields(titleField, genreField, yearField, priceField, ratingField);
                statusLabel.setText("Game added successfully!");
            } catch (Exception ex) {
                statusLabel.setText("Invalid input. Please check all fields.");
            }
        });

        // Update Game
        updateBtn.setOnAction(e -> {
            VideoGame selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                statusLabel.setText("Please select a game to update.");
                return;
            }

            try {
                selected.setTitle(titleField.getText().trim().isEmpty() ? selected.getTitle() : titleField.getText().trim());
                selected.setGenre(genreField.getText().trim().isEmpty() ? selected.getGenre() : genreField.getText().trim());
                if (!yearField.getText().trim().isEmpty())
                    selected.setReleaseYear(Integer.parseInt(yearField.getText().trim()));
                if (!priceField.getText().trim().isEmpty())
                    selected.setPrice(Double.parseDouble(priceField.getText().trim()));
                if (!ratingField.getText().trim().isEmpty())
                    selected.setRating(Double.parseDouble(ratingField.getText().trim()));

                table.refresh();
                statusLabel.setText("Game updated successfully!");
            } catch (Exception ex) {
                statusLabel.setText("Invalid input. Update failed.");
            }
        });

        // Delete Game
        deleteBtn.setOnAction(e -> {
            VideoGame selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                statusLabel.setText("Please select a game to delete.");
                return;
            }
            manager.videogames.remove(selected.getGameID());
            data.remove(selected);
            table.refresh();
            statusLabel.setText("Game deleted successfully!");
        });

        // Show All
        showAllBtn.setOnAction(e -> {
            data.setAll(manager.videogames.values());
            table.refresh();
            statusLabel.setText("All games displayed.");
        });

        // Custom Action: Average Rating
        avgBtn.setOnAction(e -> {
            if (manager.videogames.isEmpty()) {
                statusLabel.setText("No games to calculate average.");
                return;
            }
            double total = manager.videogames.values().stream().mapToDouble(VideoGame::getRating).sum();
            double avg = total / manager.videogames.size();
            statusLabel.setText(String.format("Average Rating: %.2f", avg));
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
     * Clears input fields after adding a game.
     */
    private void clearFields(TextField... fields) {
        for (TextField f : fields) {
            f.clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
