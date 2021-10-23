package com.techelevator;

import com.techelevator.JDBC.JDBCReservationDAO;
import com.techelevator.JDBC.JDBCVenueSpaceDAO;
import com.techelevator.classes.Reservation;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.SQLException;
import java.time.LocalDate;
import static org.junit.Assert.*;

public class ReservationDAOTest extends DAOIntegrationTest{

    //private static SingleConnectionDataSource dataSource;
    JdbcTemplate jdbcTemplate = null;
    private JDBCReservationDAO dao;



    @Before
    public void setup() {
        dao = new JDBCReservationDAO(getDataSource());
        jdbcTemplate = new JdbcTemplate(getDataSource());

    }


    @Test

    public void make_reservation_test() {

        LocalDate startDate1 = LocalDate.parse("2021-10-15");
        LocalDate endDate1 = LocalDate.parse("2021-10-18");

        Reservation reservation = dao.makeReservation(2, 100, startDate1, endDate1, "Matt Watson");
        assertEquals(2, reservation.getSpace_id());
    }


//    private long getNextReservationId() {
//        SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('reservation_reservation_id_seq')");
//        if (nextIdResult.next()) {
//            return nextIdResult.getLong(1);
//        } else {
//            throw new RuntimeException("Something went wrong while getting an id for the new reservation");
//        }
//    }
}
