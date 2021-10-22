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

public class JDBCVenueSpaceDAO implements VenueSpaceDAO {

    private JdbcTemplate jdbcTemplate;
    public JDBCVenueSpaceDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }




    @Override

    public List<String> viewVenues() {
        List<String> allVenues = new ArrayList<>();


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

        List<String> categoryIdList = new ArrayList<>();

        String sql1 = "SELECT venue.id, venue.name AS venueName, venue.city_id, venue.description, city.name AS city, city.state_abbreviation AS state FROM venue JOIN city ON venue.city_id = city.id WHERE venue.id = ?";
        SqlRowSet result1= jdbcTemplate.queryForRowSet(sql1, venueId);

        String sql2 = "SELECT * FROM category_venue WHERE venue_id = ?";
        SqlRowSet result2 = jdbcTemplate.queryForRowSet(sql2, venueId);

        if(result2.next()){
            categoryIdList = mapRowToCategoryIdList(result2);
        }

        if (result1.next()) {

                venue = mapRowToVenue1(result1, categoryIdList);
        }




        //String sql = "SELECT venue.name AS venue, venue.description , ARRAY_AGG(category.name) AS categories, city.name AS city, city.state_abbreviation AS state FROM venue " +
       // "JOIN category_venue ON venue.id = category_venue.venue_id " +
       // "JOIN category ON category_venue.category_id = category.id " +
        //"JOIN city ON venue.city_id = city.id WHERE venue.id = ? " +
       // "GROUP BY venue, venue.description, city, state";

        //SqlRowSet result = jdbcTemplate.queryForRowSet(sql, venueId);

        //if (result.next()) {


         //   venue = mapRowToVenue(result);
       // }

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
    public List<Space> searchSpaceByDateAndOccupancy(int openingMonth, int closingMonth, int occupancy ) {
        // list object to add available spaces
        List<Space> availableSpaces = new ArrayList<>();

        // list of all spaces
        List<Space> allSpaces = viewSpaces();

        for (Space space : allSpaces) {
            if (occupancy <= space.getMax_occupancy()) {
                if (space.getOpen_from() == null ) {
                    availableSpaces.add(space);

                }else if((space.getOpen_from() <= openingMonth) && (space.getOpen_to() >= closingMonth)){
                    availableSpaces.add(space);
                }

            }
        }

        return availableSpaces;
    }


    private String mapRowToVenueName(SqlRowSet results) {

        String venueName = results.getString("name");
        return venueName;
    }

    private Venue mapRowToVenue1(SqlRowSet result1, List<String> categoryIdList){
        Venue venue = new Venue();
        venue.setVenue_id(result1.getLong("id"));
        venue.setName(result1.getString("venueName"));
        venue.setCity_id(result1.getLong("city_id"));
        venue.setDescription(result1.getString("description"));
        venue.setCityName(result1.getString("city"));
        venue.setState(result1.getString("state"));
        venue.setCategory(categoryIdList);
        return venue;
    }

    private List<String> mapRowToCategoryIdList(SqlRowSet result2){
        List<String> categoryIdList = new ArrayList<>();
        categoryIdList.add(result2.getString("category_id"));
        return  categoryIdList;

    }


    private Venue mapRowToVenue(SqlRowSet results) {
        Venue venue = new Venue();
        venue.setVenue_id(results.getLong("id"));
        venue.setName(results.getString("name"));
        venue.setCity_id(results.getLong("city_id"));
        venue.setDescription(results.getString("description"));
        venue.setCityName(results.getString("city"));
        venue.setState(results.getString("state"));
        //venue.setCategory(results.getString("categories"));

        return venue;
    }

    private Space mapRowToSpace(SqlRowSet results) {
        Space space = new Space();
        space.setName(results.getString("name"));
        space.setOpen_from(results.getInt("open_from"));
        space.setOpen_to(results.getInt("open_to"));
        space.setDaily_rate(results.getBigDecimal("daily_rate"));
        space.setMax_occupancy(results.getInt("max_occupancy"));

        return space;
    }

}
