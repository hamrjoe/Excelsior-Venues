package com.techelevator;

import com.techelevator.JDBC.JDBCVenueSpaceDAO;
import com.techelevator.classes.Reservation;
import com.techelevator.classes.Space;
import com.techelevator.classes.Venue;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
                 + String.format("%-16s","Daily Rate")
                 + String.format("%-15s","Max Occupancy\n")
        );
        for (Space space : spacesToPrint) {
            System.out.printf("%-5s", "#" + Integer.toString(count));
            System.out.printf("%-28s", space.getName());

            if(space.getOpen_from()== 0){
                System.out.printf("%-7s", "");

            }else {
                System.out.printf("%-7s", Month.of(space.getOpen_from()).getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
            }

            if(space.getOpen_to()== 0) {
                System.out.printf("%-8s", "");

            }else {
                System.out.printf("%-8s", Month.of(space.getOpen_to()).getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
            }

            System.out.printf(String.format("$%-15.2f",space.getDaily_rate()));
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

    public LocalDate getEndDate (LocalDate startDate, int numberOfDays ){

        LocalDate endDate = startDate.plusDays(numberOfDays);
        return endDate;
    }


    public List <String> listAvailableSpaces (List<Space> spaces, int numberOfDays) {
        List<String> bookingDetails = new ArrayList<>();
        BigDecimal totalCost = new BigDecimal(0.00);

        System.out.println("The following spaces are avaialble based on your needs: \n");

        System.out.println(String.format("%-10s", "Space#")
                + String.format("%-28s", "Name")
                + String.format("%-16s", "Daily Rate")
                + String.format("%-14s", "Max Occup.")
                + String.format("%-15s", "Accessible?")
                + String.format("%-15s", "Total Cost\n"));

        for (Space space : spaces) {

            String isAccessible= "";

            if(space.isIs_accessible() == true){
                isAccessible = "Yes";
            }else{
                isAccessible = "No";
            }

            totalCost = space.getDaily_rate().multiply(BigDecimal.valueOf(numberOfDays));
            System.out.printf("%-10x", space.getId());
            System.out.printf("%-28s", space.getName());
            System.out.printf(String.format("$%-15.2f",space.getDaily_rate()));
            System.out.printf("%-14d", space.getMax_occupancy());
            System.out.printf("%-15s", isAccessible);
            System.out.printf(String.format("$%-15.2f", totalCost));


            System.out.print("\n");

        }
        System.out.println("\n Which space would you like to reserve? (enter 0 to cancel)? ");
        bookingDetails.add(scanner.nextLine());
        System.out.println("Who is this reservation for?\n");
        bookingDetails.add(scanner.nextLine());
        bookingDetails.add(totalCost.toString());

        return bookingDetails;

    }

    public void printReservation(long bookSpaceId,String venueName, String spaceName, String bookingCost, Reservation reservation){

        System.out.println("Thanks for submitting your reservation! The details for your event are listed below:");
        System.out.println("Confirmation: " + reservation.getReservation_id());
        System.out.println("Venue: " + venueName);
        System.out.println("Space: " + spaceName);
        System.out.println("Reserved for: " + reservation.getReserved_for());
        System.out.println("Attendees: " + reservation.getNumber_of_attendees());
        System.out.println("Arrival Date: " + reservation.getStart_date());
        System.out.println("Depart Date: " + reservation.getEnd_date());
        System.out.println("Total cost: $" + bookingCost);


    }


    public void printMessage (String message) {
        System.out.println(message);
    }

}
