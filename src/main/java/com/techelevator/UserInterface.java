package com.techelevator;

import com.techelevator.JDBC.JDBCVenueSpaceDAO;
import com.techelevator.classes.Venue;

import javax.sql.DataSource;
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









}
