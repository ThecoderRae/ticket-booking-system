package org.sibeworks;

import org.sibeworks.entities.Train;
import org.sibeworks.entities.User;
import org.sibeworks.services.TrainService;
import org.sibeworks.services.UserBookingService;
import org.sibeworks.util.UserServiceUtil;

import java.io.IOException;
import java.util.*;

public class App {

    public static void main(String[] args) {
        System.out.println("Running Train Booking System!");
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        UserBookingService userBookingService;
        Train trainSelectedForBooking = null; // Initialize to null
        User loggedInUser = null; // Keep track of logged-in user

        try {
            userBookingService = new UserBookingService();

        } catch (IOException e) {
            System.err.println("Error loading user data: " + e.getMessage());
            return;
        }

        while (choice != 7) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a seat");
            System.out.println("6. Cancel my Booking");
            System.out.println("7. Exit the App");

            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("Invalid input! Please enter a number between 1 and 7.");
                scanner.next(); // Clear invalid input
                continue;
            }

            switch (choice) {
                case 1: // Sign Up
                    System.out.println("Enter username to sign up:");
                    String nameToSignUp = scanner.next();
                    System.out.println("Enter the password to sign up:");
                    String passwordToSignUp = scanner.next();

                    User userToSignUp = new User(nameToSignUp, passwordToSignUp, UserServiceUtil.hashPassword(passwordToSignUp), new ArrayList<>(), UUID.randomUUID().toString());
                    if (userBookingService.signUp(userToSignUp)) {
                        System.out.println("Sign-up successful!");
                    } else {
                        System.err.println("Sign-up failed.");
                    }
                    break;

                case 2: // Login
                    System.out.println("Enter username to login:");
                    String nameToLogin = scanner.next();
                    System.out.println("Enter the password to login:");
                    String passwordToLogin = scanner.next();

                    User userToLogin = new User(nameToLogin, passwordToLogin, UserServiceUtil.hashPassword(passwordToLogin), new ArrayList<>(), UUID.randomUUID().toString());
                    if (userBookingService.loginUser()) {
                        loggedInUser = userToLogin;
                        System.out.println("Login successful!");
                    } else {
                        System.err.println("Invalid username or password.");
                    }
                    break;

                case 3: // Fetch Bookings
                    if (loggedInUser == null) {
                        System.err.println("You need to log in first.");
                        break;
                    }
                    System.out.println("Fetching your bookings...");
                    userBookingService.fetchBooking();
                    break;

                case 4: // Search Trains
                    System.out.println("Enter your source station:");
                    String source = scanner.next().trim().toLowerCase();  // Trim and convert to lowercase
                    System.out.println("Enter your destination station:");
                    String destination = scanner.next().trim().toLowerCase();  // Trim and convert to lowercase

                    // Search for trains using the TrainService (replace userBookingService with TrainService)
                    List<Train> trains =  new TrainService().getTrainList();  // Assuming trainService is your TrainService object
                    System.out.println("Trains inside trains: " + trains);
                    if (trains.isEmpty()) {
                        System.out.println("No trains found for the given route.");
                    } else {
                        System.out.println("Available trains:");
                        for (int i = 0; i < trains.size(); i++) {
                            Train train = trains.get(i);
                            System.out.println((i + 1) + ". Train ID: " + train.getTrainId());
                            train.getStationTimes().forEach((station, time) -> System.out.println(" - " + station + ": " + time));
                        }

                        System.out.println("Select a train by typing its number:");
                        try {
                            int trainIndex = scanner.nextInt() - 1;
                            if (trainIndex < 0 || trainIndex >= trains.size()) {
                                System.err.println("Invalid train selection.");
                                break;
                            }
                            trainSelectedForBooking = trains.get(trainIndex);
                            System.out.println("Train selected: " + trainSelectedForBooking.getTrainId());
                        } catch (InputMismatchException e) {
                            System.err.println("Invalid input! Please enter a valid number.");
                            scanner.next();
                        }
                    }
                    break;

                case 5: // Book a Seat
                    if (trainSelectedForBooking == null) {
                        System.err.println("You need to select a train first.");
                        break;
                    }

                    System.out.println("Available seats:");
                    List<List<Integer>> seats = trainSelectedForBooking.getSeats();
                    for (int i = 0; i < seats.size(); i++) {
                        System.out.println("Row " + (i + 1) + ": " + seats.get(i));
                    }

                    System.out.println("Enter the row number:");
                    int row;
                    int column;
                    try {
                        row = scanner.nextInt() - 1;
                        System.out.println("Enter the column number:");
                        column = scanner.nextInt() - 1;

                        if (row < 0 || row >= seats.size() || column < 0 || column >= seats.get(row).size()) {
                            System.err.println("Invalid seat selection.");
                            break;
                        }

                        if (seats.get(row).get(column) == 1) {
                            System.err.println("Seat is already booked.");
                            break;
                        }

                        seats.get(row).set(column, 1); // Mark the seat as booked
                        System.out.println("Seat booked successfully!");
                    } catch (InputMismatchException e) {
                        System.err.println("Invalid input! Please enter valid row and column numbers.");
                        scanner.next(); // Clear invalid input
                    }
                    break;

                case 6: // Cancel Booking
                    System.out.println("Feature not yet implemented.");
                    break;

                case 7: // Exit
                    System.out.println("Exiting the app. Goodbye!");
                    break;

                default:
                    System.err.println("Invalid option! Please choose a valid menu option.");
            }
        }
        scanner.close();
    }
}
