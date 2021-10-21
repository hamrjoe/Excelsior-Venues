package com.techelevator.JDBC;

import com.techelevator.DAO.VenueSpaceDAO;
import com.techelevator.classes.Space;
import com.techelevator.classes.Venue;

import java.util.List;

public class JDBCVenueDAO implements VenueSpaceDAO {
    @Override
    public List<Venue> viewVenues() {
        return null;
    }

    @Override
    public Venue retrieveVenueDetails() {
        return null;
    }

    @Override
    public List<Space> viewSpaces() {
        return null;
    }

    @Override
    public boolean spaceIsAvailable() {
        return false;
    }

    @Override
    public Space updateAvailability() {
        return null;
    }
}
