package com.techelevator;

import javax.sql.DataSource;

import com.techelevator.DAO.ReservationDAO;
import com.techelevator.DAO.VenueSpaceDAO;
import com.techelevator.JDBC.JDBCReservationDAO;
import com.techelevator.JDBC.JDBCVenueSpaceDAO;
import com.techelevator.classes.Venue;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.List;

public class ExcelsiorCLI {

	private ReservationDAO reservationDAO;
	private VenueSpaceDAO venueSpaceDAO;
	private UserInterface userInterface;
	private DataSource dataSource;

	public static void main(String[] args) {

		ExcelsiorCLI application = new ExcelsiorCLI();
		application.run();
	}

	public ExcelsiorCLI() {
		// create your DAOs here

		userInterface = new UserInterface();
		//reservationDAO = new JDBCReservationDAO(getDataSource());
		venueSpaceDAO = new JDBCVenueSpaceDAO(getDataSource());
	}

	public void run() {


		while (true) {

			String choice = userInterface.printMainMenu();


			if (choice.equals("1")) {

				List<String> listOfVenues = venueSpaceDAO.viewVenues();
				String venueChosen = userInterface.printListOfVenues(listOfVenues);
				if(venueChosen.equals("R")){
					userInterface.printMainMenu();

				}else{

					Venue venue = venueSpaceDAO.retrieveVenueDetails(Integer.parseInt(venueChosen));
					userInterface.printVenueDetails(venue);
				}




			}else{
				break;
			}
		}
	}

	private BasicDataSource getDataSource(){

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/venues");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		return dataSource;

	}
}
