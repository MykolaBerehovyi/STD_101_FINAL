package com.example.final_project;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.scene.layout.FlowPane;

import java.util.List;
import java.time.temporal.ChronoUnit;

public class OceanicApp extends Application {

    private ObservableList<Cruise> allCruises;
    private FlowPane cardsContainer; // instead of hbox in order to be able store many elements on the page
    private VBox root;
    private Queue<Integer> createdCruisesQueue;  // queue for created cruises

    @Override
    public void start(Stage primaryStage) {
        createdCruisesQueue = new Queue<>(5); // initializing queue

        // Main Container
        root = new VBox();
        root.setStyle("-fx-background-color: #f4f6f8;"); // Light gray background for the app

        // Create Hero Section
        StackPane heroSection = createHeroSection();

        // creating card section
        cardsContainer = new FlowPane();
        cardsContainer.setPadding(new Insets(40, 50, 40, 50));
        cardsContainer.setHgap(20);
        cardsContainer.setVgap(20);
        cardsContainer.setAlignment(Pos.CENTER_LEFT);

        // initialize storage for cruises
        allCruises = FXCollections.observableArrayList();

        // load cruises from file
        List<Cruise> loadedCruises = FileHandler.exportCruise();
        if (loadedCruises != null) {
            allCruises.addAll(loadedCruises);
        }

        // show cruises
        refreshCruisesDisplay();

        // footer
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(10, 50, 40, 0));

        Button btnSaveCruises = new Button("Save Cruises");
        btnSaveCruises.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 25; -fx-font-size: 14px; -fx-background-radius: 5;");

        btnSaveCruises.setOnAction(e -> {
            for (Cruise cruise : allCruises) {
                FileHandler.saveCruise(cruise);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "All cruises saved to file!");
            alert.showAndWait();
        });

        footer.getChildren().add(btnSaveCruises);

        // Add everything to root
        root.getChildren().addAll(heroSection, cardsContainer, footer);

        // wrap in scrollpane just in case the screen is small
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        Scene scene = new Scene(scrollPane, 1200, 800);
        primaryStage.setTitle("Oceanic Cruise App");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void refreshCruisesDisplay() {
        cardsContainer.getChildren().clear();

        for (Cruise cruise : allCruises) {
            VBox card = createCruiseCardFromCruise(cruise);
            cardsContainer.getChildren().add(card);
        }
    }

    private VBox createCruiseCardFromCruise(Cruise cruise) {
        // extract data from cruise
        String titleText = cruise.getName();
        String locationText = cruise.getDestination();
        String dateStr = cruise.getStartDate().toString();
        long days = ChronoUnit.DAYS.between(cruise.getStartDate(), cruise.getEndDate()); // calculate difference
        String duration = days + " Days";
        String from = cruise.getDestination();
        String slots = cruise.getAvailableSeats() + " Slots";
        String priceStr = "$" + String.format("%.0f", cruise.getPrice()); // formating digits to a string
        String description = "Experience " + cruise.getName() +
                " in " + cruise.getDestination() +
                ". Departing on " + cruise.getStartDate();

        // creating card
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        card.setPrefWidth(320);
        card.setMaxWidth(320);

        // top Image Area (Placeholder)
        StackPane imagePane = new StackPane();
        imagePane.setMinHeight(180);
        imagePane.setStyle("-fx-background-color: #81d4fa; -fx-background-radius: 8 8 0 0;");

        Label featuredBadge = new Label("Featured");
        featuredBadge.setStyle("-fx-background-color: #00bcd4; -fx-text-fill: white; -fx-padding: 5 10; -fx-font-weight: bold;");
        StackPane.setAlignment(featuredBadge, Pos.TOP_RIGHT);
        StackPane.setMargin(featuredBadge, new Insets(10));

        VBox titleBox = new VBox(5);
        titleBox.setPadding(new Insets(10));
        titleBox.setAlignment(Pos.BOTTOM_LEFT);
        Label cardTitle = new Label(titleText);
        cardTitle.setTextFill(Color.WHITE);
        cardTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        Label cardLoc = new Label(locationText);
        cardLoc.setTextFill(Color.WHITE);
        cardLoc.setFont(Font.font("System", 12));
        titleBox.getChildren().addAll(cardTitle, cardLoc);
        StackPane.setAlignment(titleBox, Pos.BOTTOM_LEFT);

        imagePane.getChildren().addAll(featuredBadge, titleBox);

        // Bottom content area
        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(20));

        Label desc = new Label(description);
        desc.setWrapText(true);
        desc.setTextFill(Color.web("#666666"));
        desc.setMinHeight(60);

        // Details grid
        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(20);
        detailsGrid.setVgap(15);

        detailsGrid.add(createDetailItem("Departure", dateStr), 0, 0);
        detailsGrid.add(createDetailItem("Duration", duration), 1, 0);
        detailsGrid.add(createDetailItem("From", from), 0, 1);
        detailsGrid.add(createDetailItem("Available", slots), 1, 1);

        contentBox.getChildren().addAll(desc, detailsGrid);

        // combined footer area
        VBox cardFooter = new VBox(15);
        cardFooter.setPadding(new Insets(0, 20, 20, 20));

        //  price and Book Now button
        HBox bookingRow = new HBox();
        bookingRow.setAlignment(Pos.CENTER_LEFT);

        VBox priceBox = new VBox();
        Label startingFrom = new Label("Starting from");
        startingFrom.setTextFill(Color.web("#666666"));
        startingFrom.setFont(Font.font("System", 10));
        Label priceLabel = new Label(priceStr + " / person");
        priceLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        priceLabel.setTextFill(Color.BLACK);
        priceBox.getChildren().addAll(startingFrom, priceLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // button book now
        Button btnBook = new Button("Book Now");
        btnBook.setStyle("-fx-background-color: #00bcd4; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");

        btnBook.setOnAction(event -> {
            BookCruiseForm bookForm = new BookCruiseForm(cruise);
            bookForm.showAndWait();
            refreshCruisesDisplay();
        });

        bookingRow.getChildren().addAll(priceBox, spacer, btnBook);

        // Edit and Delete buttons
        HBox adminRow = new HBox(10);
        Button btnEdit = new Button("Edit");
        btnEdit.setOnAction(event -> {
            EditCruiseForm editForm = new EditCruiseForm(cruise, () -> {
                // refresh cruises after changes
                refreshCruisesDisplay();
                // save changes to file
                FileHandler.saveCruise(cruise);
            });
            editForm.showAndWait();
        });
        btnEdit.setStyle("-fx-background-color: transparent; -fx-border-color: #ccc; -fx-padding: 8 25; -fx-font-weight: bold;");

        Button btnDelete = new Button("Delete cruise");
        btnDelete.setStyle("-fx-background-color: transparent; -fx-border-color: #ccc; -fx-padding: 8 25; -fx-font-weight: bold;");

        btnDelete.setOnAction(event -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete " + cruise.getName() + "?",
                    ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    allCruises.remove(cruise);
                    FileHandler.deleteCruise(cruise.getId());
                    refreshCruisesDisplay();
                }
            });
        });

        adminRow.getChildren().addAll(btnEdit, btnDelete);
        cardFooter.getChildren().addAll(bookingRow, adminRow);

        card.getChildren().addAll(imagePane, contentBox, cardFooter);
        return card;
    }

    private StackPane createHeroSection() {
        StackPane heroPane = new StackPane();
        heroPane.setStyle("-fx-background-color: linear-gradient(to bottom, #4a6b8c, #1a2a3a);");
        heroPane.setMinHeight(350);

        VBox content = new VBox(15);
        content.setPadding(new Insets(50));
        content.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Sail Beyond\nYour Dreams");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("System", FontWeight.BOLD, 48));

        Label subtitle = new Label("Discover extraordinary destinations aboard luxury cruise ships.\nCreate memories that last a lifetime.");
        subtitle.setTextFill(Color.WHITE);
        subtitle.setFont(Font.font("System", 16));
        subtitle.setOpacity(0.9);

        // Buttons Box
        HBox buttonBox = new HBox(15);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        // Create Cruise Button
        Button btnCreate = new Button("Create Cruise");
        btnCreate.setStyle("-fx-background-color: #00bcd4; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");

        btnCreate.setOnAction(event -> {
            CreateCruiseForm form = new CreateCruiseForm(newCruise -> {
                allCruises.add(newCruise);
                FileHandler.saveCruise(newCruise);

                // adding created cruise to a queue
                createdCruisesQueue.add(newCruise.getId());

                refreshCruisesDisplay();
            });
            form.showAndWait();
        });

        // Undo Button
        Button btnUndo = new Button("↩Undo");
        btnUndo.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");

        btnUndo.setOnAction(event -> {
            Integer lastCreatedId = createdCruisesQueue.pop();

            if (lastCreatedId != null) {
                // fing cruise with this id
                Cruise toDelete = null;
                for (Cruise cruise : allCruises) {
                    if (cruise.getId() == lastCreatedId) {
                        toDelete = cruise;
                        break;
                    }
                }

                if (toDelete != null) {
                    allCruises.remove(toDelete);
                    FileHandler.deleteCruise(lastCreatedId);
                    refreshCruisesDisplay();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION,
                            "Undo: Removed " + toDelete.getName());
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "no cruises to undo");
                alert.showAndWait();
            }
        });

        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("Enter cruise ID");
        searchField.setStyle("-fx-background-color: white; -fx-padding: 10 15; -fx-pref-width: 180; -fx-font-size: 13px;");

        // Show All Cruises Button
        Button btnShowAll = new Button("Show All");
        btnShowAll.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        btnShowAll.setOnAction(event -> {
            List<Cruise> loadedCruises = FileHandler.exportCruise();
            allCruises.clear();
            if (loadedCruises != null) {
                allCruises.addAll(loadedCruises);
            }
            refreshCruisesDisplay();
            searchField.clear(); // Delete search label
        });

        // Search button
        Button btnSearch = new Button("Search");
        btnSearch.setStyle("-fx-background-color: #00bcd4; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");

        // Search logic
        btnSearch.setOnAction(event -> {
            String input = searchField.getText().trim();

            if (input.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a cruise ID");
                alert.showAndWait();
                return;
            }

            try {
                int searchId = Integer.parseInt(input);
                Cruise foundCruise = null;

                // Binary search developed by us
                int left = 0;
                int right = allCruises.size() - 1;

                while (left <= right) {
                    int mid = left + (right - left) / 2;
                    int midId = allCruises.get(mid).getId();

                    if (midId == searchId) {
                        foundCruise = allCruises.get(mid);
                        break;
                    } else if (midId < searchId) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }

                if (foundCruise != null) {
                    allCruises.clear();
                    allCruises.add(foundCruise);
                    refreshCruisesDisplay();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "no cruise found with ID: " + searchId);
                    alert.showAndWait();
                }

            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid id, Please enter a number.");
                alert.showAndWait();
            }
        });

        searchField.setOnAction(event -> btnSearch.fire());

        buttonBox.getChildren().addAll(btnCreate, btnUndo, btnShowAll, searchField, btnSearch);
        content.getChildren().addAll(title, subtitle, buttonBox);
        heroPane.getChildren().add(content);

        return heroPane;
    }



    private VBox createDetailItem(String title, String value) {
        VBox box = new VBox(3);

        Label titleLabel = new Label(title);
        titleLabel.setTextFill(Color.gray(0.6));
        titleLabel.setFont(Font.font("System", 10));
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        valueLabel.setTextFill(Color.BLACK); // necessary part never delete (!) or ban and lots of bugs NEVER DELETE (spend hour)
        box.getChildren().addAll(titleLabel, valueLabel);
        return box;
    }

    public static void main(String[] args) {
        launch(args);
    }
}