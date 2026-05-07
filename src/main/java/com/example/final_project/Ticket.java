package com.example.final_project;

public class Ticket {

    private int id;
    private Cruise cruise;
    private Client client;

    public Ticket(int id, Cruise cruise, Client client) {
        this.id = id;
        this.cruise = cruise;
        this.client = client;
    }

    public int getId() { return id; }
    public Cruise getCruise() { return cruise; }
    public Client getClient() { return client; }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", cruise=" + cruise.getName() +
                ", client=" + client.getName() +
                '}';
    }
}