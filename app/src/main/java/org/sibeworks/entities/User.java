package org.sibeworks.entities;

import java.util.List;

public class User {

    private String name;
    private  String password;
    private String hashPssword;
    private List<Ticket> ticketsBooked;
    private  String userId;


    public User(String name, String password, String hashPssword, List<Ticket> ticketsBooked, String userId) {
        this.name = name;
        this.password = password;
        this.hashPssword = hashPssword;
        this.ticketsBooked = ticketsBooked;
        this.userId = userId;
    }


    public String getHashPssword() {
        return hashPssword;
    }

    public void setHashPssword(String hashPssword) {
        this.hashPssword = hashPssword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ticket> getTicketsBooked() {
        return ticketsBooked;
    }

    public void printTickets() {
        for (int i = 0; i < ticketsBooked.size(); i++) {
            System.out.println(ticketsBooked.get(i).getTicketInfo());
        }
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
