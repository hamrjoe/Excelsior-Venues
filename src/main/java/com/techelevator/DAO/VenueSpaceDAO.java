package com.techelevator.DAO;

import com.techelevator.classes.Space;
import com.techelevator.classes.Venue;

import java.util.List;

public interface VenueSpaceDAO {

    public List<Venue> viewVenues();
    public Venue retrieveVenueDetails();
    public List<Space> viewSpaces();


    public boolean spaceIsAvailable();

    public Space updateAvailability();
}
