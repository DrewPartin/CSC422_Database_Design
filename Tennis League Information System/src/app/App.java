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

    private static final TeamDAO teamDao = new TeamDAO();

    private static void viewTeams() {

        List<Team> teams = teamDao.getAllTeams();

        System.out.println();
        System.out.println("TeamNumber | Name | City | ManagerName");
        System.out.println("----------------------------------------------------");
        for (Team t : teams) {
            System.out.printf("%d | %s | %s | %s%n",
                t.getTeamNumber(),
                t.getName(),
                t.getCity(),
                t.getManagerName());
        }
    }

    private static void viewPlayers() {
        System.out.println("View Players - Functionality to be implemented.");
    }

    private static final CoachDAO coachDao = new CoachDAO();

    private static void viewCoaches() {
        
        List<Coach> coaches = coachDao.getAllCoachesWithTeamName();

        System.out.println();
        System.out.println("CoachID | Name | TelephoneNumber | TeamNumber | TeamName");
        System.out.println("--------------------------------------------------------");
        for (Coach c : coaches) {
            System.out.printf("%d | %s | %s | %d | %s%n",
                    c.getCoachID(),
                    c.getName(),
                    c.getTelephoneNumber(),
                    c.getTeamNumber(),
                    c.getTeamName());
        }
    }

    // Team Management Menu
    private static void teamManagementMenu() {  

        while (true) {
            System.out.println();
            System.out.println("===== TEAM MANAGEMENT =====");
            System.out.println("1) Add Team");
            System.out.println("2) Edit Team");
            System.out.println("3) Delete Team");
            System.out.println("0) Back");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": addTeam(); break;
                case "2": editTeam(); break;
                case "3": deleteTeam(); break;
                case "0": return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void addTeam() {

        try {
            System.out.print("TeamNumber (int): ");
            int teamNumber = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("City: ");
            String city = scanner.nextLine().trim();

            System.out.print("ManagerName: ");
            String managerName = scanner.nextLine().trim();

            Team t = new Team(teamNumber, name, city, managerName);
            boolean ok = teamDao.addTeam(t);

            System.out.println(ok ? "Team added." : "Failed to add team.");

        } catch (Exception e) {
            System.out.println("Invalid input. Team not added.");
        }
    }

    private static void editTeam() {

        try {
            System.out.print("Enter TeamNumber to edit: ");
            int teamNumber = Integer.parseInt(scanner.nextLine().trim());

            Team existing = teamDao.getTeamByNumber(teamNumber);
            if (existing == null) {
                System.out.println("Team not found.");
                return;
            }

            System.out.println("Leave blank to keep current value.");

            System.out.print("Name (" + existing.getName() + "): ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) existing.setName(name);

            System.out.print("City (" + existing.getCity() + "): ");
            String city = scanner.nextLine().trim();
            if (!city.isEmpty()) existing.setCity(city);

            System.out.print("ManagerName (" + existing.getManagerName() + "): ");
            String manager = scanner.nextLine().trim();
            if (!manager.isEmpty()) existing.setManagerName(manager);

            boolean ok = teamDao.updateTeam(existing);
            System.out.println(ok ? "Team updated." : "Failed to update team.");

        } catch (Exception e) {
            System.out.println("Invalid input. Team not updated.");
        }
    }

    private static void deleteTeam() {

        try {
            System.out.print("Enter TeamNumber to delete: ");
            int teamNumber = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("This will also remove dependent records (coaches, work experience, player associations). Continue? (y/n): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (!confirm.equals("y")) {
                System.out.println("Cancelled.");
                return;
            }

            boolean ok = teamDao.deleteTeamCascade(teamNumber);
            System.out.println(ok ? "Team deleted." : "Failed to delete team (not found or constraint issue).");

        } catch (Exception e) {
            System.out.println("Invalid input. Team not deleted.");
        }
    }

    // Player Management Menu
    private static void playerManagementMenu() {
        while (true) {
            System.out.println();
            System.out.println("===== PLAYER MANAGEMENT =====");
            System.out.println("1) Add Player");
            System.out.println("2) Edit Player");
            System.out.println("3) Delete Player");
            System.out.println("0) Back");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": addPlayer(); break;
                case "2": editPlayer(); break;
                case "3": deletePlayer(); break;
                case "0": return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void addPlayer() {
        System.out.println("[TODO] Add Player");
        // Later: prompt for LeagueWideNumber, Name, Age
        // optionally: prompt for team association + year joined/left
        // Call playerDao.addPlayerAndAssociate(...)
    }

    private static void editPlayer() {
        System.out.println("[TODO] Edit Player");
        // Later: prompt for PlayerID, then fields to update
        // Call playerDao.updatePlayer(...)
    }

    private static void deletePlayer() {
        System.out.println("[TODO] Delete Player");
        // Later: prompt for PlayerID
        // Call playerDao.deletePlayerCascade(...)
    }

    // Coach Management Menu
    private static void coachManagementMenu() {
        while (true) {
            System.out.println();
            System.out.println("===== COACH MANAGEMENT =====");
            System.out.println("1) Add Coach");
            System.out.println("2) Edit Coach");
            System.out.println("3) Delete Coach");
            System.out.println("0) Back");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": addCoach(); break;
                case "2": editCoach(); break;
                case "3": deleteCoach(); break;
                case "0": return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void addCoach() {
        
        try {
            System.out.print("Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("TelephoneNumber: ");
            String phone = scanner.nextLine().trim();

            System.out.print("TeamNumber (int): ");
            int teamNumber = Integer.parseInt(scanner.nextLine().trim());

            Coach c = new Coach();
            c.setName(name);
            c.setTelephoneNumber(phone);
            c.setTeamNumber(teamNumber);

            boolean ok = coachDao.addCoach(c);
            System.out.println(ok ? "Coach added." : "Failed to add coach.");

        } catch (Exception e) {
            System.out.println("Invalid input. Coach not added.");
        }
    }

    private static void editCoach() {
        
        try {
            System.out.print("Enter CoachID to edit: ");
            int coachId = Integer.parseInt(scanner.nextLine().trim());

            Coach existing = coachDao.getCoachById(coachId);
            if (existing == null) {
                System.out.println("Coach not found.");
                return;
            }

            System.out.println("Leave blank to keep current value.");

            System.out.print("Name (" + existing.getName() + "): ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) existing.setName(name);

            System.out.print("TelephoneNumber (" + existing.getTelephoneNumber() + "): ");
            String phone = scanner.nextLine().trim();
            if (!phone.isEmpty()) existing.setTelephoneNumber(phone);

            System.out.print("TeamNumber (" + existing.getTeamNumber() + "): ");
            String tn = scanner.nextLine().trim();
            if (!tn.isEmpty()) existing.setTeamNumber(Integer.parseInt(tn));

            boolean ok = coachDao.updateCoach(existing);
            System.out.println(ok ? "Coach updated." : "Failed to update coach.");

        } catch (Exception e) {
            System.out.println("Invalid input. Coach not updated.");
        }
    }

    private static void deleteCoach() {
        
        try {
            System.out.print("Enter CoachID to delete: ");
            int coachId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("This will also delete related WorkExperience records. Continue? (y/n): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (!confirm.equals("y")) {
                System.out.println("Cancelled.");
                return;
            }

            boolean ok = coachDao.deleteCoachCascade(coachId);
            System.out.println(ok ? "Coach deleted." : "Failed to delete coach (not found).");
            
        } catch (Exception e) {
            System.out.println("Invalid input. Coach not deleted.");
        }
    }
}
