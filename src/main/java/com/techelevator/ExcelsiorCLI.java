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

import java.math.BigDecimal;
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
//constructor
	public ExcelsiorCLI() {
		// create your DAOs here

		userInterface = new UserInterface();
		reservationDAO = new JDBCReservationDAO(getDataSource());
		venueSpaceDAO = new JDBCVenueSpaceDAO(getDataSource());
	}

	public void run() {

       // Main menu
		while (true) {

			//Calling PrintMainMenu method from user

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
										LocalDate startDate = LocalDate.parse(reservationDetails.get(0));
										int lengthOfStay = Integer.parseInt(reservationDetails.get(1));
										int numberOfAttendees = Integer.parseInt(reservationDetails.get(2));
										LocalDate endDate=userInterface.getEndDate(startDate,lengthOfStay);
										long chosenVenue=venue.getVenue_id();

										List <Space> allSpaces=venueSpaceDAO.checkAvailableSpaces(chosenVenue, startDate,endDate,numberOfAttendees);
										List<String> userDetailsForReservation = userInterface.listAvailableSpaces(allSpaces,lengthOfStay);

										if (userDetailsForReservation.size() == 0) {
											break;
										}
										Long bookSpaceId= Long.parseLong(userDetailsForReservation.get(0));
										String userName = userDetailsForReservation.get(1);
										String bookingCost = userDetailsForReservation.get(2);

										Reservation reservation = reservationDAO.makeReservation(bookSpaceId,numberOfAttendees,startDate,endDate,userName);
										String spaceName = venueSpaceDAO.retrieveSpaceById(bookSpaceId).getName();
										userInterface.printReservation(bookSpaceId,venue.getName(), spaceName,bookingCost, reservation);
										System.exit(0);
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
