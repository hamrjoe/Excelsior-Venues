package com.techelevator.JDBC;

import com.techelevator.DAO.VenueSpaceDAO;
import com.techelevator.classes.Space;
import com.techelevator.classes.Venue;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JDBCVenueSpaceDAO implements VenueSpaceDAO {

    private JdbcTemplate jdbcTemplate;
    public JDBCVenueSpaceDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<String> viewVenues() {

        List<String> allVenues = new ArrayList<>();

        //Executing SQL query to get all venue names and looping through each one to add them to a list and returning the list.
        String sql = "SELECT name from venue";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {

           String venueName = mapRowToVenueName(results);

           allVenues.add(venueName);
        }

        return allVenues;
    }

    @Override
    public Venue retrieveVenueDetails(long venueId) {

        Venue venue = new Venue();
        List<String> categoryNameList = new ArrayList<>();

        //Executing multiple SQL queries to get the venue details with the city details
        // and category and mapping the details into a venue object and returning the venue object.
        String sql1 = "SELECT venue.id, venue.name AS venueName, venue.city_id, venue.description, city.name AS city, city.state_abbreviation AS state FROM venue JOIN city ON venue.city_id = city.id WHERE venue.id = ?";
        SqlRowSet result1= jdbcTemplate.queryForRowSet(sql1, venueId);

        String sql2 = "SELECT category.name FROM category_venue JOIN category ON category.id = category_venue.category_id WHERE venue_id = ?";
        SqlRowSet result2 = jdbcTemplate.queryForRowSet(sql2, venueId);

        //taking the results from the SQL query2 and adding it to the categoryName list.
        categoryNameList = mapRowToCategoryNameList(result2);

        //Mapping the results from SQL query1  and categoryNameList list retrieved from SQL query2 to a venue object.
        if (result1.next()) {

                venue = mapRowToVenue(result1, categoryNameList);
        }

        return venue;
    }

    @Override
    public List<Space> viewSpaces(long venueId) {

        List<Space> spaces = new ArrayList<>();

        //Executing SQL query to get all the attributes of a space for a chosen venue
        // and looping through each space and adding them to a list and returning the list.
        String sql = "SELECT id, name, open_from, open_to,(SELECT CAST (daily_rate AS Decimal))  ,max_occupancy FROM space WHERE venue_id=?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,venueId);
        while (results.next()) {

            Space space = mapRowToSpace(results);
            spaces.add(space);
        }

        return spaces;
    }


    public List<Space> checkAvailableSpaces(long venueId, LocalDate startDate, LocalDate endDate, int occupancy) {

        List<Space> availableSpaces = new ArrayList<>();

        //This query is getting all available spaces for a chosen venue that meets the user criteria for dates and occupancy
        //It validates the open and close date range and max occupancy to retrieve available spaces
        String availableSpaceSql = "SELECT space.id, space.venue_id, space.name, space.is_accessible, space.open_from, space.open_to, space.daily_rate::money::numeric::float8, space.max_occupancy  FROM space " +
                "WHERE space.venue_id = ? " +
                "AND space.id NOT IN (SELECT space.id FROM space " +
                "LEFT JOIN reservation ON space.id = reservation.space_id " +
                "WHERE (? <= reservation.end_date " +
                "AND ? >= reservation.start_date) " +
                "OR EXTRACT(MONTH FROM CAST(? AS DATE)) < space.open_from " +
                "OR EXTRACT(MONTH FROM CAST(? AS DATE)) > space.open_to " +
                "OR space.max_occupancy < ?) " +
                "ORDER BY space.daily_rate DESC " +
                "LIMIT 5 ";

        //Maps SQL result to a list
        SqlRowSet results = jdbcTemplate.queryForRowSet(availableSpaceSql, venueId, startDate, endDate, startDate, endDate, occupancy);
        while(results.next()) {

            Space space = mapRowToSpace(results);
            availableSpaces.add(space);
        }

        return availableSpaces;
    }

    public Space retrieveSpaceById(long spaceID){

        Space space = new Space();

        //Executing SQL query to get space details by its spaceId and mapping the details into a space object
        String sql = "SELECT id, name, is_accessible, open_from, open_to,(SELECT CAST (daily_rate AS Decimal))  , max_occupancy FROM space WHERE id=?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, spaceID);
        if(results.next()) {

            space = mapRowToSpace(results);

        }

        return space;
    }

    //method that maps venue name from SQL query result
    private String mapRowToVenueName(SqlRowSet results) {

        String venueName = results.getString("name");

        return venueName;
    }

    //method that maps a venue object from multiple SQL query results
    private Venue mapRowToVenue(SqlRowSet result1, List<String> categoryNameList){

        Venue venue = new Venue();
        venue.setVenue_id(result1.getLong("id"));
        venue.setName(result1.getString("venueName"));
        venue.setCity_id(result1.getLong("city_id"));
        venue.setDescription(result1.getString("description"));
        venue.setCityName(result1.getString("city"));
        venue.setState(result1.getString("state"));
        venue.setCategoryNames(categoryNameList);

        return venue;
    }

    //method that maps the category name from a SQL query
    // and adds the category names to a list and return the category name list
    private List<String> mapRowToCategoryNameList(SqlRowSet result2){

        List<String> categoryNameList = new ArrayList<>();

        while (result2.next()) {

            categoryNameList.add(result2.getString("name"));

        }

        return  categoryNameList;

    }

    //method that maps SQL query results of space details into a space object
    private Space mapRowToSpace(SqlRowSet results) {

        Space space = new Space();
        space.setId(results.getLong("id"));
        space.setName(results.getString("name"));
        space.setOpen_from(results.getInt("open_from"));
        space.setOpen_to(results.getInt("open_to"));
        space.setDaily_rate(results.getBigDecimal("daily_rate"));
        space.setMax_occupancy(results.getInt("max_occupancy"));

        return space;
    }

}
