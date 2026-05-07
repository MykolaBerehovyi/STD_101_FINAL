package com.example.final_project;

import java.util.ArrayList;
import java.util.List;

public class Client {

    private int id;
    private String name;
    private String contactInfo;
    private List<Ticket> tickets = new ArrayList<>();

    public Client(int id, String name, String contactInfo) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public List<Ticket> getTickets() {
        return tickets;
    }
}