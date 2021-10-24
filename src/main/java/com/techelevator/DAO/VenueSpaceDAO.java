package com.techelevator.DAO;

import com.techelevator.classes.Space;
import com.techelevator.classes.Venue;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.List;

public interface VenueSpaceDAO {

    public List<String> viewVenues();
    public Venue retrieveVenueDetails(long venueId);
    public List<Space> viewSpaces(long venueId);
    public List<Space> checkAvailableSpaces(long venueId, LocalDate startDate, LocalDate endDate, int occupancy);
    public Space retrieveSpaceById(long spaceId);

}
