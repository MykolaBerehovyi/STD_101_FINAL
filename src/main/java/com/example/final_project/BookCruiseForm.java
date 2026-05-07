package com.example.final_project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BookCruiseForm extends Stage {

    private Cruise cruise;

    public BookCruiseForm(Cruise cruise) {
        this.cruise = cruise;
        this.setTitle("Book Cruise: " + cruise.getName());
        this.initModality(Modality.APPLICATION_MODAL); //

        // main container
        VBox root = new VBox(20);
        root.setPadding(new Insets(20, 30, 20, 30));
        root.setAlignment(Pos.TOP_CENTER);

        // title
        Label titleLabel = new Label("Complete Your Booking");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 22));

        // cruise information (reading)
        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(15);
        detailsGrid.setVgap(10);

        detailsGrid.setStyle("-fx-background-color: #f9f9f9; -fx-padding: 15; -fx-background-radius: 5; -fx-border-color: #e0e0e0; -fx-border-radius: 5;");

        detailsGrid.add(new Label("Cruise:"), 0, 0);
        detailsGrid.add(createBoldLabel(cruise.getName()), 1, 0);

        detailsGrid.add(new Label("Destination:"), 0, 1);
        detailsGrid.add(createBoldLabel(cruise.getDestination()), 1, 1);

        detailsGrid.add(new Label("Dates:"), 0, 2);
        detailsGrid.add(createBoldLabel(cruise.getStartDate() + " — " + cruise.getEndDate()), 1, 2);

        detailsGrid.add(new Label("Price:"), 0, 3);
        detailsGrid.add(createBoldLabel(String.valueOf(cruise.getPrice())), 1, 3);

        // avaliable seats
        Label seatsLabel = new Label(cruise.getAvailableSeats() + " out of " + cruise.getTotalSeats());
        seatsLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
        detailsGrid.add(new Label("Available Seats:"), 0, 4);
        detailsGrid.add(seatsLabel, 1, 4);

        // input client data
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(15);
        inputGrid.setVgap(15);

        TextField passengerNameField = new TextField();
        passengerNameField.setPromptText("e.g. John Doe");

        TextField contactInfoField = new TextField();
        contactInfoField.setPromptText("Email or Phone Number");

        inputGrid.add(new Label("Passenger Name:"), 0, 0);
        inputGrid.add(passengerNameField, 1, 0);

        inputGrid.add(new Label("Contact Info:"), 0, 1);
        inputGrid.add(contactInfoField, 1, 1);

        Button btnConfirm = new Button("Confirm Booking");
        btnConfirm.setStyle("-fx-background-color: #00bcd4; -fx-text-fill: white; -fx-font-weight: bold;");

        Button btnCancel = new Button("Cancel");

        HBox buttonBox = new HBox(10, btnConfirm, btnCancel);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        // buttons logic
        btnCancel.setOnAction(e -> this.close());

        btnConfirm.setOnAction(e -> {
            String name = passengerNameField.getText();
            String contact = contactInfoField.getText();

            // checking that fields are not empty
            if (name.isEmpty() || contact.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill in all fields!");
                alert.showAndWait();
                return;
            }

            // trying to book seat (decrease availableSeats by 1)
            boolean bookingSuccess = cruise.bookSeat();

            if (!bookingSuccess) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, no available seats for this cruise!");
                alert.showAndWait();
                return;
            }

            // saving updated cruise to file
            FileHandler.saveCruise(cruise);

            // success message
            Alert success = new Alert(Alert.AlertType.INFORMATION, "Booking successful for " + name);
            success.setHeaderText(null);
            success.showAndWait();

            this.close();
        });

        root.getChildren().addAll(titleLabel, detailsGrid, inputGrid, buttonBox);

        Scene scene = new Scene(root, 450, 480);
        this.setScene(scene);
    }

    // additional method for fonts
    private Label createBoldLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.BOLD, 13));
        return label;
    }
}