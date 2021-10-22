package com.techelevator.JDBC;

import com.techelevator.DAO.ReservationDAO;
import com.techelevator.classes.Reservation;
import com.techelevator.classes.Space;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.*;

public class JDBCReservationDAO implements ReservationDAO {

    private JdbcTemplate jdbcTemplate;
    public JDBCReservationDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    @Override
    public Reservation makeReservation() {


        return null;
    }


   /* private Reservation mapRowToReservation(SqlRowSet results) {
        Reservation reservation = new Reservation();
        reservation.setReservation_id(results.getLong("reservation_id"));
        reservation.setSpace_id(results.getInt("space_id"));
        reservation.setNumber_of_attendees(results.getInt("number_of_attendees"));
        reservation.setStart_date(results.getDate("start_date").toLocalDate());
        reservation.setEnd_date(results.getDate("end_date").toLocalDate());
        reservation.setReserved_for(results.getString("reserved_for"));

        return reservation;
    }*/


}
