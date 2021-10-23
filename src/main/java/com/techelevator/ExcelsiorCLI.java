package com.techelevator;

import javax.sql.DataSource;

import com.techelevator.DAO.ReservationDAO;
import com.techelevator.DAO.VenueSpaceDAO;
import com.techelevator.JDBC.JDBCReservationDAO;
import com.techelevator.JDBC.JDBCVenueSpaceDAO;
import com.techelevator.classes.Reservation;
import com.techelevator.classes.Space;
import com.techelevator.classes.Venue;
import org.apache.commons.dbcp2.BasicDataSource;

import java.time.LocalDate;
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
				while (true) {

					List<String> listOfVenues = venueSpaceDAO.viewVenues();
					String venueChosen = userInterface.printListOfVenues(listOfVenues);
					if (!venueChosen.equalsIgnoreCase("R")) {
						while (true) {
							Venue venue = venueSpaceDAO.retrieveVenueDetails(Integer.parseInt(venueChosen));
							String userChoice = userInterface.printVenueDetails(venue);

							if (userChoice.equals("1")) {
								while (true) {
									List<Space> listOfSpaces = venueSpaceDAO.viewSpaces(venue.getVenue_id());

									String spaceChoice=userInterface.printListOfSpaces(listOfSpaces, venue.getName());


									if(spaceChoice.equals("1")){
										List <String> reservationDetails=userInterface.getReservationDetails();
										LocalDate startDate=userInterface.getStartDate(reservationDetails);
										LocalDate endDate=userInterface.getEndDate(reservationDetails);
										int numberOfOccupants= userInterface.getoccupancy(reservationDetails);
										int numberOfDays=Integer.parseInt(reservationDetails.get(1));
										long chosenVenue=venue.getVenue_id();
										while (true){
											List <Space> allSpaces=venueSpaceDAO.checkAvailableSpaces(chosenVenue, startDate,endDate,numberOfOccupants);

											userInterface.listAvailableSpaces(allSpaces,numberOfDays);
										}
									}

								}

							} else if (userChoice.equalsIgnoreCase("R")) {
								break;
							}
							else userInterface.printMessage("Invalid Selection");
						}

					} else if (venueChosen.equalsIgnoreCase("R")) {
						break;
					} else userInterface.printMessage("Invalid Selection");


				}

			}
			else if( choice.equalsIgnoreCase("q")){
				break;
			}else userInterface.printMessage("Invalid Selection");
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
