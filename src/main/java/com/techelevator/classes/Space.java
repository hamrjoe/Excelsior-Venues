package com.techelevator.classes;

import java.math.BigDecimal;
import java.util.Date;

public class Space {

    private long id;
    private long venue_id;
    private String name;
    private boolean is_accessible;
    private Date open_from;
    private Date open_to;
    private BigDecimal daily_rate;
    private int max_occupancy;

    public long getId() {
        return id;
    }

    public long getVenue_id() {
        return venue_id;
    }

    public String getName() {
        return name;
    }

    public boolean isIs_accessible() {
        return is_accessible;
    }

    public Date getOpen_from() {
        return open_from;
    }

    public Date getOpen_to() {
        return open_to;
    }

    public BigDecimal getDaily_rate() {
        return daily_rate;
    }

    public int getMax_occupancy() {
        return max_occupancy;
    }

    public void setIs_accessible(boolean is_accessible) {
        this.is_accessible = is_accessible;
    }

    public void setOpen_from(Date open_from) {
        this.open_from = open_from;
    }

    public void setOpen_to(Date open_to) {
        this.open_to = open_to;
    }

    public void setDaily_rate(BigDecimal daily_rate) {
        this.daily_rate = daily_rate;
    }

    public void setMax_occupancy(int max_occupancy) {
        this.max_occupancy = max_occupancy;
    }
}
