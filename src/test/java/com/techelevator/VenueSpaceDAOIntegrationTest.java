package com.techelevator;

import com.techelevator.JDBC.JDBCVenueSpaceDAO;
import com.techelevator.classes.Venue;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

public class VenueSpaceDAOIntegrationTest{

    private static SingleConnectionDataSource dataSource;
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    private JDBCVenueSpaceDAO dao;
    long nextVenueId = getNextVenueId();
    long nextSpaceId = getNextSpaceId();


    @BeforeClass
    public static void setupDataSource() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/venues");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");
        dataSource.setAutoCommit(false);
    }



    @AfterClass
    public static void closeDataSource() throws SQLException {
        dataSource.destroy();
    }

    @Before
    public void setup() {
        dao = new JDBCVenueSpaceDAO(dataSource);

        String sqlInsertVenue = "INSERT INTO venue (id, name, city_id, description) VALUES (?,'venue1', 1, 'Brand new venue')";
        String sqlInsertSpace = "INSERT INTO space (id, venue_id, name, is_accessible, open_from, open_to, daily_rate, max_occupancy) VALUES (?,?, 'space1', true, 4, 9, '$100', 2)";

        jdbcTemplate.update(sqlInsertVenue, nextVenueId);
        jdbcTemplate.update(sqlInsertSpace, nextSpaceId,nextVenueId);


    }


    @After
    public void rollback() throws SQLException {
        dataSource.getConnection().rollback();
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


        assertNotNull(venue);
        assertEquals("Bona",venue.getCityName());
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
