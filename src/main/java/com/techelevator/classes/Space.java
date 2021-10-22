package com.techelevator.classes;

import java.math.BigDecimal;
import java.util.Date;

public class Space {

    private long id;
    private long venue_id;
    private String name;
    private boolean is_accessible;
    private Integer open_from;
    private Integer open_to;
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

    public Integer getOpen_from() {
        return open_from;
    }

    public Integer getOpen_to() {
        return open_to;
    }

    public BigDecimal getDaily_rate() {
        return daily_rate;
    }

    public int getMax_occupancy() {
        return max_occupancy;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setVenue_id(long venue_id) {
        this.venue_id = venue_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIs_accessible(boolean is_accessible) {
        this.is_accessible = is_accessible;
    }

    public void setOpen_from(Integer open_from) {
        this.open_from = open_from;
    }

    public void setOpen_to(Integer open_to) {
        this.open_to = open_to;
    }

    public void setDaily_rate(BigDecimal daily_rate) {
        this.daily_rate = daily_rate;
    }

    public void setMax_occupancy(int max_occupancy) {
        this.max_occupancy = max_occupancy;
    }
}
