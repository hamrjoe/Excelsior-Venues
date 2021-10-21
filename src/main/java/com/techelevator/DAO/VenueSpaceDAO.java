package com.techelevator.DAO;

import com.techelevator.classes.Space;
import com.techelevator.classes.Venue;

import java.util.Date;
import java.util.List;

public interface VenueSpaceDAO {

    public List<Venue> viewVenues();
    public Venue retrieveVenueDetails(long venueId);
    public List<Space> viewSpaces();


    public List<Space> searchSpaceByDateAndOccupancy(Date startDate, Date endDate, int occupancy );

    public Space updateAvailability();
}
