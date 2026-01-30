package app;

import java.util.List;
import java.util.Scanner;

import app.dao.TeamDAO;
import app.dao.PlayerDAO;
import app.dao.CoachDAO;
import app.model.Team;
import app.model.Player;
import app.model.Coach; 

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
        System.out.println("Tennis League Information System started...");

        while (true) {
            System.out.println();
            System.out.println("===== MAIN MENU =====");
            System.out.println("1. View Records");
            System.out.println("2. Team Management");
            System.out.println("3. Player Management");
            System.out.println("4. Coach Management");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": viewRecordsMenu(); break;
                case "2": teamManagementMenu(); break;
                case "3": playerManagementMenu(); break;
                case "4": coachManagementMenu(); break;
                case "0":
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return;
                default: System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    // View Records Menu
    private static void viewRecordsMenu() {
        while (true) {
            System.out.println();
            System.out.println("===== VIEW RECORDS =====");
            System.out.println("1. View Teams");
            System.out.println("2. View Players");
            System.out.println("3. View Coaches");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": viewTeams(); break;
                case "2": viewPlayers(); break;
                case "3": viewCoaches(); break;
                case "0": return;
                default: System.out.println("Invalid option. Please try again."); 
            }
        }
    }

    private static void viewTeams() {
        System.out.println("View Teams - Functionality to be implemented.");
    }

    private static void viewPlayers() {
        System.out.println("View Players - Functionality to be implemented.");
    }

    private static void viewCoaches() {
        System.out.println("View Coaches - Functionality to be implemented.");
    }

    // Team Management Menu
    private static void teamManagementMenu() {
        System.out.println("Team Management - Functionality to be implemented.");
    }

    // Player Management Menu
    private static void playerManagementMenu() {
        System.out.println("Player Management - Functionality to be implemented.");
    }

    // Coach Management Menu
    private static void coachManagementMenu() {
        System.out.println("Coach Management - Functionality to be implemented.");
    }
}
