package com.techelevator;

import com.techelevator.JDBC.JDBCVenueSpaceDAO;
import com.techelevator.classes.Space;
import com.techelevator.classes.Venue;

import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import java.util.Formatter;
import java.util.List;

public class VenueSpaceDAOIntegrationTest extends DAOIntegrationTest{

    JdbcTemplate jdbcTemplate = null;
    private JDBCVenueSpaceDAO dao;
    long nextVenueId;
    long nextSpaceId;


    @Before
    public void setup() {
        dao = new JDBCVenueSpaceDAO(getDataSource());
        jdbcTemplate = new JdbcTemplate(getDataSource());

         nextVenueId = getNextVenueId();
         nextSpaceId = getNextSpaceId();

        String sqlInsertVenue = "INSERT INTO venue (id, name, city_id, description) VALUES (?,'venue1', 1, 'Brand new venue')";
        String sqlInsertSpace = "INSERT INTO space (id, venue_id, name, is_accessible, open_from, open_to, daily_rate, max_occupancy) VALUES (?,?, 'space1', true, 4, 9, '$100', 2)";
        String sqlInsertCategory= "INSERT INTO category_venue (venue_id, category_id) VALUES (?,5)";
        String sqlInsertCategory2= "INSERT INTO category_venue (venue_id, category_id) VALUES (?,6)";
        jdbcTemplate.update(sqlInsertVenue, nextVenueId);
        jdbcTemplate.update(sqlInsertSpace, nextSpaceId,nextVenueId);
        jdbcTemplate.update(sqlInsertCategory,nextVenueId);
        jdbcTemplate.update(sqlInsertCategory2,nextVenueId);

    }


    @Test
    public void view_venue_test(){

        List<String> allVenues = dao.viewVenues();

        assertNotNull(allVenues);
        assertTrue(allVenues.contains("venue1"));
    }

    @Test
    public void retrieve_venue_details_test(){

       Venue venue = dao.retrieveVenueDetails(nextVenueId);
       List <String> categoryNames=venue.getCategoryNames();

        assertNotNull(venue);
        assertEquals("Bona",venue.getCityName());
        assertEquals("Luxury", categoryNames.get(0) );
        assertEquals("Modern",categoryNames.get(1));
    }

    @Test
    public void view_spaces_test(){

        List<Space> allspaces = dao.viewSpaces(nextVenueId);
        Space space=allspaces.get(allspaces.size()-1);

        String newspaceName = space.getName();
        BigDecimal dailyRate= new BigDecimal(100.00).setScale(2, RoundingMode.HALF_DOWN);

        assertNotNull(allspaces);
        assertEquals("space1", newspaceName);
        assertEquals(dailyRate , space.getDaily_rate());
    }

    @Test
    public void check_available_spaces_test() {

        LocalDate startDate1 = LocalDate.parse("2021-10-15");
        LocalDate endDate1 = LocalDate.parse("2021-10-18");

        List<Space> spacesNegativeTest = dao.checkAvailableSpaces(nextVenueId, startDate1, endDate1,2 );

        assertEquals(0,spacesNegativeTest.size());

        //check against existing reservations

        LocalDate startDate2 = LocalDate.parse("2021-09-05");
        LocalDate endDate2 = LocalDate.parse("2021-09-10");
        List<Space> spacesPositiveTest = dao.checkAvailableSpaces(nextVenueId,startDate2, endDate2, 2 );
        assertEquals(1, spacesPositiveTest.size());
        assertEquals(2,spacesPositiveTest.get(0).getMax_occupancy());

        LocalDate startDate3 = LocalDate.parse("2021-10-18");
        LocalDate endDate3 = LocalDate.parse("2021-10-19");

        List<Space> spacesPositiveTest1 = dao.checkAvailableSpaces(1, startDate3, endDate3, 100 );
        assertEquals(1, spacesPositiveTest1.size());
        assertEquals(110,spacesPositiveTest1.get(0).getMax_occupancy());

    }

    @Test
    public void retrieve_space_by_id(){

        Space space = dao.retrieveSpaceById(nextSpaceId);
        BigDecimal dailyRate= new BigDecimal(100.00).setScale(2, RoundingMode.HALF_DOWN);

        assertNotNull(space);
        assertEquals("space1",space.getName());
        assertEquals(2, space.getMax_occupancy() );
        assertEquals(dailyRate,space.getDaily_rate());
        assertTrue(space.getOpen_from().equals(4));
        assertTrue(space.getOpen_to().equals(9));
    }

    private long getNextVenueId() {
        SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('venue_id_seq')");
        if (nextIdResult.next()) {
            return nextIdResult.getLong(1);
        } else {
            throw new RuntimeException("Something went wrong while getting an id for the new venue");
        }
    }

    private long getNextSpaceId() {
        SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('space_id_seq')");
        if (nextIdResult.next()) {
            return nextIdResult.getLong(1);
        } else {
            throw new RuntimeException("Something went wrong while getting an id for the new space");
        }
    }


}
