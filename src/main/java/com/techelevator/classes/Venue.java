package com.techelevator.classes;

public class Venue {

    private long venue_id;
    private String name;
    private long city_id;
    private String description;
    private String cityName;
    private String state;
    private String category;

    public long getVenue_id() {
        return venue_id;
    }

    public String getName() {
        return name;
    }

    public long getCity_id() {
        return city_id;
    }

    public String getDescription() {
        return description;
    }

    public String getCityName() {
        return cityName;
    }

    public String getState() {
        return state;
    }

    public String getCategory() {
        return category;
    }

    public void setVenue_id(long venue_id) {
        this.venue_id = venue_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity_id(long city_id) {
        this.city_id = city_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
