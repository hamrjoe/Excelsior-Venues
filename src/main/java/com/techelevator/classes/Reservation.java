package com.techelevator.classes;

import java.util.Date;

public class Reservation {

    private long reservation_id;
    private long space_id;
    private int number_of_attendees;
    private Date start_date;
    private Date end_date;
    private String reserved_for;

    public long getReservation_id() {
        return reservation_id;
    }

    public long getSpace_id() {
        return space_id;
    }

    public int getNumber_of_attendees() {
        return number_of_attendees;
    }

    public Date getStart_date() {
        return start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public String getReserved_for() {
        return reserved_for;
    }

    public void setNumber_of_attendees(int number_of_attendees) {
        this.number_of_attendees = number_of_attendees;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public void setReserved_for(String reserved_for) {
        this.reserved_for = reserved_for;
    }
}





