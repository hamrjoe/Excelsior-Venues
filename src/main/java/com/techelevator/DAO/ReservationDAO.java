package com.techelevator.DAO;

import com.techelevator.classes.Reservation;
import com.techelevator.classes.Space;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public interface ReservationDAO {

    public Reservation makeReservation(long space_id, int numberOfAttendees, LocalDate start_date, LocalDate end_date, String reserved_for);


}


