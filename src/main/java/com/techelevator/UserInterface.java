package com.techelevator;

import com.techelevator.JDBC.JDBCVenueSpaceDAO;
import com.techelevator.classes.Space;
import com.techelevator.classes.Venue;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInterface {

    private Scanner scanner;


    public UserInterface(){
        scanner= new Scanner(System.in);
    }
     public String printMainMenu(){
         System.out.println("Excelsior Venue Booking App\n");

         System.out.println("What would you like to do?\n");

         System.out.println("1) List Venues");
         System.out.println("Q) Quit");
         return scanner.nextLine();

     }

    public String printListOfVenues(List<String> venuesToPrint) {

        System.out.println("Which venue would you like to view?\n");

        int count =1;

        for (String venueName : venuesToPrint) {

            System.out.println(count + ") " + venueName);
            count++;

        }

        System.out.println("R) Return to Previous screen");

        return scanner.nextLine();
    }

    public String printVenueDetails(Venue venue){

        System.out.println(venue.getName());
        System.out.println("Location: "+ venue.getCityName() + ", " + venue.getState());
        System.out.println("Categories: " + venue.getCategoryNames() + "\n");
        System.out.println(venue.getDescription() + "\n");

        System.out.println("What would you like to do next?");
        System.out.println("1) View Spaces");
        //System.out.println("2) Search fo Reservation -> Non-MVP");
        System.out.println("R) Return to Previous Screen\n");

        return scanner.nextLine();
    }
    public String printListOfSpaces(List<Space> spacesToPrint, String venueName) {
       int count=1;
        System.out.println(venueName +" Spaces\n");

        System.out.println(String.format("%-5s", "")
                 + String.format("%-28s", "Name"  )
                 + String.format("%-7s","Open")
                 + String.format("%-8s","Close")
                 + String.format("%-15s","Daily Rate")
                 + String.format("%-15s","Max Occupancy\n")
        );
        for (Space space : spacesToPrint) {
            System.out.printf("%-5s", "#" + Integer.toString(count));
            System.out.printf("%-28s", space.getName());
            System.out.printf("%-7d", space.getOpen_from());
            System.out.printf("%-8d", space.getOpen_to());
            System.out.printf("%-15.2f", "$" +space.getDaily_rate());
            System.out.printf("%-15d", space.getMax_occupancy());

            System.out.print("\n");
            count++;
        }

        System.out.println("\nWhat would you like to do next?\n");


        System.out.println("1) Reserve a Space");
        System.out.println("R) Return to Previous Screen\n");

        return scanner.nextLine();
    }

    public List<String> getReservationDetails(){
        List <String> reservationDetails=new ArrayList<>();

        System.out.println("When do you need the space?  Please enter in date format yyyy-mm-dd");
        String startDate=scanner.nextLine();
        reservationDetails.add(startDate);
        System.out.println("How many days will you need the space? Please enter as a number");
        String lengthOfStay=scanner.nextLine();
        reservationDetails.add(lengthOfStay);
        System.out.println("How many people will be in attendance? Please enter as a number");
        String numberOfAttendees=scanner.nextLine();
        reservationDetails.add(numberOfAttendees);

        return reservationDetails;
    }

    public LocalDate getEndDate (List <String> reservationDetails ){
        LocalDate startDate=LocalDate.parse(getReservationDetails().get(0));
        int numberOfDays=Integer.parseInt(getReservationDetails().get(1));
        LocalDate endDate=startDate.plusDays(numberOfDays);

        return endDate;
    }

    public LocalDate getStartDate (List <String> reservationDetails){
        LocalDate startDate=LocalDate.parse(getReservationDetails().get(0));

        return startDate;
    }
    public int numberOfDays (List<String>  reservationDetails){
        int numberOfDays=Integer.parseInt(getReservationDetails().get(1));

        return numberOfDays;
    }

    public int getoccupancy (List <String> reservationDetails){
        int attendess=Integer.parseInt(getReservationDetails().get(2));

        return attendess;
    }

    public List <String> listAvailableSpaces (List<Space> spaces, int numberOfDays) {
        List<String> bookingDetails = new ArrayList<>();

        System.out.println("The following spaces are avaialble based on your needs: \n");

        System.out.println(String.format("%-5s", "Space #")
                + String.format("%-28s", "Name")
                + String.format("%-7s", "Daily Rate")
                + String.format("%-8s", "Max Occup.")
                + String.format("%-15s", "Accessible?")
                + String.format("%-15s", "Total Cost\n"));

        for (Space space : spaces) {
            BigDecimal totalCost = space.getDaily_rate().multiply(BigDecimal.valueOf(numberOfDays));
            System.out.printf("%-5s", space.getId());
            System.out.printf("%-28s", space.getName());
            System.out.printf("%-7d", space.getDaily_rate());
            System.out.printf("%-8d", space.isIs_accessible());
            System.out.printf("%-15.2f", "$" + totalCost);


            System.out.print("\n");

        }
        System.out.println("Which space would you like to reserve? (enter 0 to cancel)? ");
        bookingDetails.add(scanner.nextLine());
        System.out.println("Who is this reservation for?\n");
        bookingDetails.add(scanner.nextLine());

        return bookingDetails;

    }






    public void printMessage (String message) {
        System.out.println(message);
    }

}
