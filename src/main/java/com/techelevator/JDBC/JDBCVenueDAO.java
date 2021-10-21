package com.techelevator.JDBC;

import com.techelevator.DAO.VenueSpaceDAO;
import com.techelevator.classes.Space;
import com.techelevator.classes.Venue;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JDBCVenueDAO implements VenueSpaceDAO {

    private JdbcTemplate jdbcTemplate;
    public JDBCVenueDAO (DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }




    @Override

    //Creates a list of venues to show in the UI
    public List<Venue> viewVenues() {
        List<Venue> allVenues = new ArrayList<>();

        //SQL statement
        String sql = "SELECT name FROM venue";

        //calling database, executing query
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        //loop through each row from database
        while (results.next()) {

            //take items from results and put into venue object
            Venue venue = mapRowToVenue(results);

            //add venue object to list
            allVenues.add(venue);
        }

        return allVenues;
    }

    @Override
    public Venue retrieveVenueDetails(long venueId) {
        Venue venue = new Venue();

        String sql = "SELECT venue.name AS venue, venue.description , ARRAY_AGG(category.name) AS categories, city.name AS city, city.state_abbreviation AS state FROM venue " +
        "JOIN category_venue ON venue.id = category_venue.venue_id " +
        "JOIN category ON category_venue.category_id = category.id " +
        "JOIN city ON venue.city_id = city.id WHERE venue.id = ? " +
        "GROUP BY venue, venue.description, city, state";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, venueId);

        if (result.next()) {
            venue = mapRowToVenue(result);
        }

        return venue;
    }


    @Override
    public List<Space> viewSpaces() {
        List<Space> spaces = new ArrayList<>();

        String sql = "SELECT name, open_from, open_to, daily_rate,max_occupancy FROM space;";

        //calling database, executing query
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        //loop through each row from database
        while (results.next()) {

            //take items from results and put into venue object
            Space space = mapRowToSpace(results);

            //add venue object to list
            spaces.add(space);
        }

        return spaces;
    }

    @Override
    public List<Space> searchSpaceByDateAndOccupancy(Date startDate, Date endDate, int occupancy ) {
        // list object to add available spaces
        List<Space> availableSpaces = new ArrayList<>();

        // list of all spaces
        List<Space> allSpaces = viewSpaces();

        for (Space space : allSpaces) {
            if (occupancy <= space.getMax_occupancy()) {
                if (startDate.after(space.getOpen_from()) && endDate.before(space.getOpen_to())) {
                    availableSpaces.add(space);
                }
            }
        }

        return availableSpaces;
    }

    @Override
    public Space updateAvailability() {
        return null;
    }


    private Venue mapRowToVenue(SqlRowSet results) {
        Venue venue = new Venue();
        venue.setVenue_id(results.getLong("id"));
        venue.setName(results.getString("name"));
        venue.setCity_id(results.getLong("city_id"));
        venue.setDescription(results.getString("description"));
        venue.setCityName(results.getString("city"));
        venue.setState(results.getString("state"));
        venue.setCategory(results.getString("categories"));

        return venue;
    }

    private Space mapRowToSpace(SqlRowSet results) {
        Space space = new Space();
        space.setName(results.getString("name"));
        space.setOpen_from(results.getDate("open_from"));
        space.setOpen_to(results.getDate("open_to"));
        space.setDaily_rate(results.getBigDecimal("daily_rate"));
        space.setMax_occupancy(results.getInt("max_occupancy"));

        return space;
    }

}
