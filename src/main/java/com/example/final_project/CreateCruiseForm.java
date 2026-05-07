package com.example.final_project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.function.Consumer;

public class CreateCruiseForm extends Stage {

    public CreateCruiseForm(Consumer<Cruise> onSaveCallback) {
        this.setTitle("Create New Cruise");

        this.initModality(Modality.APPLICATION_MODAL);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        TextField nameField = new TextField();
        nameField.setPromptText("e.g. Caribbean Paradise");

        TextField destField = new TextField();
        destField.setPromptText("e.g. Miami, FL");

        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();

        TextField priceField = new TextField();
        priceField.setPromptText("e.g. 1499");

        TextField seatsField = new TextField();
        seatsField.setPromptText("e.g. 150");

        grid.add(new Label("Cruise Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Destination:"), 0, 1);
        grid.add(destField, 1, 1);

        grid.add(new Label("Start Date:"), 0, 2);
        grid.add(startDatePicker, 1, 2);

        grid.add(new Label("End Date:"), 0, 3);
        grid.add(endDatePicker, 1, 3);

        grid.add(new Label("Price ($):"), 0, 4);
        grid.add(priceField, 1, 4);

        grid.add(new Label("Total Seats:"), 0, 5);
        grid.add(seatsField, 1, 5);

        Button btnSave = new Button("Save Cruise");
        btnSave.setStyle("-fx-background-color: #00bcd4; -fx-text-fill: white; -fx-font-weight: bold;");

        Button btnCancel = new Button("Cancel");

        HBox buttonBox = new HBox(10, btnSave, btnCancel);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        grid.add(buttonBox, 1, 6);

        btnCancel.setOnAction(e -> this.close());

        btnSave.setOnAction(e -> {
            try {
                String name = nameField.getText();
                String dest = destField.getText();
                LocalDate start = startDatePicker.getValue();
                LocalDate end = endDatePicker.getValue();
                int seats = Integer.parseInt(seatsField.getText());

                // we need to parse price field to double
                double priceValue = Double.parseDouble(priceField.getText());

                Cruise newCruise = new Cruise(name, dest, start, end, priceValue, seats); // using constructor without id

                onSaveCallback.accept(newCruise);
                this.close();

            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields correctly.");
                alert.showAndWait();
            }
        });

        Scene scene = new Scene(grid, 400, 350);
        this.setScene(scene);
    }
}