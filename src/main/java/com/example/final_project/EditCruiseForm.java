package com.example.final_project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EditCruiseForm {
    private final Cruise cruise;
    private final Runnable onSaveCallback;

    public EditCruiseForm(Cruise cruise, Runnable onSaveCallback) {
        this.cruise = cruise;
        this.onSaveCallback = onSaveCallback;
    }

    public void showAndWait() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Edit Cruise: " + cruise.getName());

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f4f6f8;");

        Label header = new Label("Edit Cruise");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(10));

        // Name field
        Label nameLabel = new Label("Cruise Name:");
        nameLabel.setStyle("-fx-font-weight: bold;");
        TextField nameField = new TextField(cruise.getName());
        nameField.setPrefWidth(250);

        // Destination field
        Label destLabel = new Label("Destination:");
        destLabel.setStyle("-fx-font-weight: bold;");
        TextField destField = new TextField(cruise.getDestination());
        destField.setPrefWidth(250);

        // Start Date
        Label startLabel = new Label("Start Date (YYYY-MM-DD):");
        startLabel.setStyle("-fx-font-weight: bold;");
        TextField startField = new TextField(cruise.getStartDate().toString());

        // End Date
        Label endLabel = new Label("End Date (YYYY-MM-DD):");
        endLabel.setStyle("-fx-font-weight: bold;");
        TextField endField = new TextField(cruise.getEndDate().toString());

        // Price
        Label priceLabel = new Label("Price ($):");
        priceLabel.setStyle("-fx-font-weight: bold;");
        TextField priceField = new TextField(String.valueOf(cruise.getPrice()));

        // Total Seats
        Label seatsLabel = new Label("Total Seats:");
        seatsLabel.setStyle("-fx-font-weight: bold;");
        TextField seatsField = new TextField(String.valueOf(cruise.getTotalSeats()));

        // Available Seats
        Label availLabel = new Label("Available Seats:");
        availLabel.setStyle("-fx-font-weight: bold;");
        TextField availField = new TextField(String.valueOf(cruise.getAvailableSeats()));

        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);
        form.add(destLabel, 0, 1);
        form.add(destField, 1, 1);
        form.add(startLabel, 0, 2);
        form.add(startField, 1, 2);
        form.add(endLabel, 0, 3);
        form.add(endField, 1, 3);
        form.add(priceLabel, 0, 4);
        form.add(priceField, 1, 4);
        form.add(seatsLabel, 0, 5);
        form.add(seatsField, 1, 5);
        form.add(availLabel, 0, 6);
        form.add(availField, 1, 6);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-wrap-text: true;");
        errorLabel.setVisible(false);

        Button saveBtn = new Button("💾 Save Changes");
        saveBtn.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 25; -fx-background-radius: 5;");

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 25; -fx-background-radius: 5;");

        HBox buttonBox = new HBox(15, saveBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER);

        saveBtn.setOnAction(e -> {
            try {
                // Get new values
                String newName = nameField.getText().trim();
                String newDest = destField.getText().trim();

                if (newName.isEmpty() || newDest.isEmpty()) {
                    throw new IllegalArgumentException("Name and destination cannot be empty");
                }

                LocalDate newStartDate = LocalDate.parse(startField.getText().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                LocalDate newEndDate = LocalDate.parse(endField.getText().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                double newPrice = Double.parseDouble(priceField.getText().trim());
                int newTotalSeats = Integer.parseInt(seatsField.getText().trim());
                int newAvailableSeats = Integer.parseInt(availField.getText().trim());

                // Validate
                if (newEndDate.isBefore(newStartDate)) {
                    throw new IllegalArgumentException("End date cannot be before start date");
                }
                if (newPrice < 0) {
                    throw new IllegalArgumentException("Price cannot be negative");
                }
                if (newTotalSeats < 0 || newAvailableSeats < 0) {
                    throw new IllegalArgumentException("Seat counts cannot be negative");
                }
                if (newAvailableSeats > newTotalSeats) {
                    throw new IllegalArgumentException("Available seats cannot exceed total seats");
                }

                // update the cruise object using setters
                cruise.setName(newName);
                cruise.setDestination(newDest);
                cruise.setStartDate(newStartDate);
                cruise.setEndDate(newEndDate);
                cruise.setPrice(newPrice);
                cruise.setTotalSeats(newTotalSeats);
                cruise.setAvailableSeats(newAvailableSeats);

                errorLabel.setVisible(false);

                // save to file AND refresh display
                FileHandler.saveCruise(cruise);

                if (onSaveCallback != null) {
                    onSaveCallback.run();
                }

                stage.close();

                Alert success = new Alert(Alert.AlertType.INFORMATION, "Cruise updated successfully!");
                success.showAndWait();

            } catch (DateTimeParseException ex) {
                errorLabel.setText("Error: Invalid date format. Use YYYY-MM-DD (e.g., 2025-12-31)");
                errorLabel.setVisible(true);
            } catch (NumberFormatException ex) {
                errorLabel.setText("Error: Invalid number format. Check price and seats.");
                errorLabel.setVisible(true);
            } catch (IllegalArgumentException ex) {
                errorLabel.setText("Error: " + ex.getMessage());
                errorLabel.setVisible(true);
            } catch (Exception ex) {
                errorLabel.setText("Unexpected error: " + ex.getMessage());
                errorLabel.setVisible(true);
                ex.printStackTrace();
            }
        });

        cancelBtn.setOnAction(e -> stage.close());

        root.getChildren().addAll(header, form, errorLabel, buttonBox);

        Scene scene = new Scene(root, 580, 600);
        stage.setScene(scene);
        stage.showAndWait();
    }
}