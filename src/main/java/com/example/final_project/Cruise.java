package com.example.final_project;

import java.time.LocalDate;

public class Cruise {
    private int id;
    private String name;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private double price;
    private int availableSeats;
    private int totalSeats;

    // static field for tracking next avaliable id
    private static int nextId = 1;

    // default constructor for new cruises (without id)
    public Cruise(String name, String destination,
                  LocalDate startDate, LocalDate endDate,
                  double price, int totalSeats) {
        this.id = nextId++;  // automatically assign a value
        this.name = name;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
    }

    // constructor for exporting from file with existing id
    public Cruise(int id, String name, String destination,
                  LocalDate startDate, LocalDate endDate,
                  double price, int totalSeats, int availableSeats) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;

        // updating nextId so that other new cruises would not be created with the same id
        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    public boolean bookSeat() {
        if (availableSeats > 0) {
            availableSeats--;
            return true;
        }
        return false;
    }

    // getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDestination() { return destination; }
    public double getPrice() { return price; }
    public int getAvailableSeats() { return availableSeats; }
    public int getTotalSeats() { return totalSeats; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }

    // setter for avaliableseats (we use it while booking and updating)
    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    // setter for name
    public void setName(String name) {
        this.name = name;
    }

    // setter for destination
    public void setDestination(String destination) {
        this.destination = destination;
    }

    // setter for startDate
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    // setter for endDate
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    // setter for price
    public void setPrice(double price) {
        this.price = price;
    }

    // setter for totalSeats
    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
        if (this.availableSeats > totalSeats) {
            this.availableSeats = totalSeats;
        }
    }

    @Override
    public String toString() {
        return "Cruise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", destination='" + destination + '\'' +
                ", price=" + price +
                ", availableSeats=" + availableSeats +
                '}';
    }
}