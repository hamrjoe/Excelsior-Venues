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

        String sql1 = "SELECT venue.id, venue.name AS venueName, venue.city_id, venue.description, city.name AS city, city.state_abbreviation AS state FROM venue JOIN city ON venue.city_id = city.id WHERE venue.id = ?";
        SqlRowSet result1= jdbcTemplate.queryForRowSet(sql1, venueId);

        String sql2 = "SELECT category.name FROM category_venue JOIN category ON category.id = category_venue.category_id WHERE venue_id = ?";
        SqlRowSet result2 = jdbcTemplate.queryForRowSet(sql2, venueId);


            categoryNameList = mapRowToCategoryNameList(result2);


        if (result1.next()) {

                venue = mapRowToVenue1(result1, categoryNameList);
        }


       return venue;
    }


    @Override
    public List<Space> viewSpaces(long venueId) {
        List<Space> spaces = new ArrayList<>();

        String sql = "SELECT name, open_from, open_to,(SELECT CAST (daily_rate AS Decimal))  ,max_occupancy FROM space WHERE venue_id=?;";

        //calling database, executing query
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,venueId);

        //loop through each row from database
        while (results.next()) {

            //take items from results and put into venue object
            Space space = mapRowToSpace(results);

            //add venue object to list
            spaces.add(space);
        }

        return spaces;
    }

 /*   @Override
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
    }*/


    public List<Space> checkAvailableSpaces(long venueId, LocalDate startDate, LocalDate endDate, int occupancy) {
        List<Space> availableSpaces = new ArrayList<>();

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

        SqlRowSet results = jdbcTemplate.queryForRowSet(availableSpaceSql, venueId, startDate, endDate, startDate, endDate, occupancy);

        while(results.next()) {
            Space space = mapRowToSpace(results);
            availableSpaces.add(space);
        }

        return availableSpaces;
    }

    private String mapRowToVenueName(SqlRowSet results) {

        String venueName = results.getString("name");
        return venueName;
    }

    private Venue mapRowToVenue1(SqlRowSet result1, List<String> categoryNameList){
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

    private List<String> mapRowToCategoryNameList(SqlRowSet result2){
        List<String> categoryNameList = new ArrayList<>();
        while (result2.next()) {
            categoryNameList.add(result2.getString("name"));
        }
        return  categoryNameList;

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
