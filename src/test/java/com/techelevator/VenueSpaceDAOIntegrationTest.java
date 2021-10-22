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
import java.util.ArrayList;
import java.util.Formatter;
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
        String sqlInsertCategory= "INSERT INTO category_venue (venue_id, category_id) VALUES (?,5)";
        String sqlInsertCategory2= "INSERT INTO category_venue (venue_id, category_id) VALUES (?,6)";
        jdbcTemplate.update(sqlInsertVenue, nextVenueId);
        jdbcTemplate.update(sqlInsertSpace, nextSpaceId,nextVenueId);
        jdbcTemplate.update(sqlInsertCategory,nextVenueId);
        jdbcTemplate.update(sqlInsertCategory2,nextVenueId);

    }


    @After
    public void rollback() throws SQLException {
        dataSource.getConnection().rollback();
    }


    @Test

    public void view_venue_test(){

        List<String> allVenues = dao.viewVenues();

        assertNotNull(allVenues);
        assertTrue(allVenues.contains("space1"));
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

        List<Space> allspaces = dao.viewSpaces();
        Space space=allspaces.get(allspaces.size()-1);

        String newspaceName = space.getName();
        BigDecimal dailyRate= new BigDecimal(100.00).setScale(2, RoundingMode.HALF_DOWN);


        assertNotNull(allspaces);
        assertEquals("space1", newspaceName);
        assertEquals(dailyRate , space.getDaily_rate());
    }

    @Test
    public void search_space_by_date_and_occupancy_test(){
        List<Space> availableSpaces= dao.searchSpaceByDateAndOccupancy(8,8,2);
        List<Space> checkAvailable= dao.searchSpaceByDateAndOccupancy(9,10,2);

        Space space=availableSpaces.get(availableSpaces.size()-1);

        List <String> availableSpaceNames= getSpaceNames(availableSpaces);
        List <String> checkAvailableName=getSpaceNames(checkAvailable);


        assertNotNull(availableSpaces);
        assertEquals("space1",space.getName());
        assertTrue(availableSpaceNames.contains("space1") );
        assertFalse(checkAvailableName.contains("space1"));

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
    private List<String> getSpaceNames(List<Space> spaces){
        List <Space>allSpaces=spaces;
        List <String> spaceNames=new ArrayList<>();

        for (Space space:allSpaces){
           spaceNames.add(space.getName());
        }

       return spaceNames;
    }


}
