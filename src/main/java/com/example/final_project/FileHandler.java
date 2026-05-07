package com.example.final_project;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private static final String FILE_NAME = "cruises.txt";

    // Save or UPDATE a cruise in the file
    public static void saveCruise(Cruise cruise) {
        List<Cruise> allCruises = exportCruise();
        boolean found = false;

        // Update existing cruise if found
        for (int i = 0; i < allCruises.size(); i++) {
            if (allCruises.get(i).getId() == cruise.getId()) {
                allCruises.set(i, cruise);
                found = true;
                break;
            }
        }

        // If not found, add new cruise
        if (!found) {
            allCruises.add(cruise);
        }

        // Write ALL cruises back to file (overwrite)
        saveAllCruises(allCruises);
    }

    // Save all cruises to file (overwrites the file)
    private static void saveAllCruises(List<Cruise> cruises) {
        try (FileWriter writer = new FileWriter(FILE_NAME, false)) { // false = overwrite, not append
            for (Cruise cruise : cruises) {
                writer.write(
                        cruise.getId() + "," +
                                cruise.getName() + "," +
                                cruise.getDestination() + "," +
                                cruise.getStartDate() + "," +
                                cruise.getEndDate() + "," +
                                cruise.getPrice() + "," +
                                cruise.getAvailableSeats() + "," +
                                cruise.getTotalSeats() + "\n"
                );
            }
            System.out.println("Saved " + cruises.size() + " cruises to file");
        } catch (IOException e) {
            System.out.println("Error while saving cruises: " + e.getMessage());
        }
    }

    public static List<Cruise> exportCruise() {
        List<Cruise> cruises = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("File " + FILE_NAME + " does not exist");
            return cruises;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    String destination = parts[2].trim();
                    LocalDate startDate = LocalDate.parse(parts[3].trim());
                    LocalDate endDate = LocalDate.parse(parts[4].trim());
                    double price = Double.parseDouble(parts[5].trim());
                    int availableSeats = Integer.parseInt(parts[6].trim());
                    int totalSeats = Integer.parseInt(parts[7].trim());

                    Cruise cruise = new Cruise(id, name, destination, startDate, endDate, price, totalSeats, availableSeats);
                    cruises.add(cruise);

                } catch (Exception e) {
                    System.out.println("error at line " + lineNumber + ": " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("error while reading file: " + e.getMessage());
        }
        return cruises;
    }

    // delete cruise by ID
    public static void deleteCruise(int cruiseId) {
        List<Cruise> allCruises = exportCruise();
        allCruises.removeIf(cruise -> cruise.getId() == cruiseId);
        saveAllCruises(allCruises);
        System.out.println("Deleted cruise with ID: " + cruiseId);
    }
}