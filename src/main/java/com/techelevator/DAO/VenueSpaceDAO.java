package com.techelevator.DAO;

import com.techelevator.classes.Space;
import com.techelevator.classes.Venue;

import java.util.Date;
import java.util.List;

public interface VenueSpaceDAO {

    public List<String> viewVenues();
    public Venue retrieveVenueDetails(long venueId);
    public List<Space> viewSpaces();
    public List<Space> searchSpaceByDateAndOccupancy(int startDate, int endDate, int occupancy );

}
