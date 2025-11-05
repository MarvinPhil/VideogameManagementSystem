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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class VideoGameManagerGUI extends Application {

    private VideoGameDAO db = new VideoGameDAO(); // DAO
    private ObservableList<VideoGame> data = FXCollections.observableArrayList();
    private TableView<VideoGame> table = new TableView<>();
    private Label statusLabel = new Label("Ready.");


    /*
     * Method Name: start
     * Purpose: Initializes and displays the main GUI window. Prompts the user to select a database
     * and sets up the UI components including panels, table, and control buttons.
     * argument primaryStage The main window for the JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {

        // Ask user for SQLite DB file
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select SQLite Database (.db)");
        File dbFile = chooser.showOpenDialog(primaryStage);

        if (dbFile == null || !db.connectToDatabase(dbFile.getAbsolutePath())) {
            showAlert("Error", "Failed to connect to database. Program will close.");
            return;
        }

        // Load DB data
        data.setAll(db.getAllGames());

        // UI Layout
        BorderPane root = new BorderPane();
        root.setTop(makeTitle());
        root.setCenter(makeTable());
        root.setLeft(makeControlPanel());
        root.setBottom(statusLabel);
        BorderPane.setMargin(root.getLeft(), new Insets(10));

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Video Game Manager");
        primaryStage.show();
    }

    /*
     * Method Name: makeTitle
     * Purpose: Creates and returns the title label for the application UI.
     * return A styled Label displaying the application title.
     */
    private Label makeTitle() {
        Label title = new Label("Video Game Manager");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-padding: 10;");
        return title;
    }

    /*
     * Method Name: makeTable
     * Purpose: Configures and returns the TableView that displays video game records.
     * return A TableView displaying a list of VideoGame objects.
     */
    private TableView<VideoGame> makeTable() {
        TableColumn<VideoGame, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("gameID"));

        TableColumn<VideoGame, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<VideoGame, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<VideoGame, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));

        TableColumn<VideoGame, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<VideoGame, Double> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));

        table.getColumns().setAll(idCol, titleCol, genreCol, yearCol, priceCol, ratingCol);
        table.setItems(data);
        return table;
    }

    /*
     * Method Name: makeControlPanel
     * Purpose: Creates and returns the left-side control panel containing input fields and
     * buttons for adding, updating, deleting, displaying, and analyzing game data.
     * return A VBox containing all control UI elements.
     */
    private VBox makeControlPanel() {

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

        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button showAllBtn = new Button("Show All");
        Button avgBtn = new Button("Average Rating");

        addBtn.setOnAction(e -> {
            try {
                String title = titleField.getText().trim();
                String genre = genreField.getText().trim();

                if (title.isEmpty() || genre.isEmpty()) {
                    statusLabel.setText("Title and Genre cannot be blank.");
                    return;
                }

                int year = Integer.parseInt(yearField.getText().trim());
                if (year < 1950 || year > 2025) {
                    statusLabel.setText("Year must be between 1950 and 2025.");
                    return;
                }

                double price = Double.parseDouble(priceField.getText().trim());
                if (price < 0) {
                    statusLabel.setText("Price cannot be negative.");
                    return;
                }

                double rating = Double.parseDouble(ratingField.getText().trim());
                if (rating < 0 || rating > 10) {
                    statusLabel.setText("Rating must be 0 to 10.");
                    return;
                }

                if (db.addGame(title, genre, year, price, rating)) {
                    refreshTable();
                    clear(titleField, genreField, yearField, priceField, ratingField);
                    statusLabel.setText("Game Added!");
                }

            } catch (Exception ex) {
                statusLabel.setText("Invalid input. Check your fields.");
            }
        });

        updateBtn.setOnAction(e -> {
            VideoGame selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) { statusLabel.setText("Select a game first."); return; }

            try {
                if (db.updateGame(selected.getGameID(),
                        titleField.getText(), genreField.getText(),
                        Integer.parseInt(yearField.getText()), Double.parseDouble(priceField.getText()),
                        Double.parseDouble(ratingField.getText()))) {

                    refreshTable();
                    statusLabel.setText("Game Updated!");
                } else {
                    statusLabel.setText("Update Failed.");
                }
            } catch (Exception ex) { statusLabel.setText("Invalid input."); }
        });

        deleteBtn.setOnAction(e -> {
            VideoGame selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) { statusLabel.setText("Select game first."); return; }

            db.deleteGame(selected.getGameID());
            refreshTable();
            statusLabel.setText("Game Deleted.");
        });

        showAllBtn.setOnAction(e -> refreshTable());

        avgBtn.setOnAction(e -> statusLabel.setText("Average Rating: " + String.format("%.2f", db.calculateAverageRating())));

        VBox v = new VBox(8, titleField, genreField, yearField, priceField, ratingField, addBtn, updateBtn, deleteBtn, showAllBtn, avgBtn);
        v.setPadding(new Insets(10));
        return v;
    }

    /**
     * Method Name: refreshTable
     * Purpose: Reloads the TableView with the most current database data.
     */
    private void refreshTable() { data.setAll(db.getAllGames()); table.refresh(); }

    /**
     * Method Name: clear
     * Purpose: Clears all text fields passed into the method.
     * argument fields One or more TextField objects that should be cleared.
     */
    private void clear(TextField... fields) { for (TextField f : fields) f.clear(); }

    /**
     * Method Name: showAlert
     * Purpose: Displays an error message in a pop-up window.
     * argument title The title of the alert dialog.
     * argument msg The message to display inside the alert.
     * return No return value (void).
     */
    private void showAlert(String title, String msg) { new Alert(Alert.AlertType.ERROR, msg).show(); }

    /*
     * Method Name: main
     * Purpose: Launches the JavaFX GUI application.
     */
    public static void main(String[] args) { launch(args); }
}