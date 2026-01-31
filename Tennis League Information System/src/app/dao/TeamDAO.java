package app.dao;

import app.model.Team;
import app.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO {

    public List<Team> getAllTeams() {
        String sql = "SELECT TeamNumber, Name, City, ManagerName FROM Team ORDER BY TeamNumber";
        List<Team> teams = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Team t = new Team();
                t.setTeamNumber(rs.getInt("TeamNumber"));
                t.setName(rs.getString("Name"));
                t.setCity(rs.getString("City"));
                t.setManagerName(rs.getString("ManagerName"));
                teams.add(t);
            }
        } catch (Exception e) {
            System.out.println("getAllTeams() error: " + e.getMessage());
        }

        return teams;
    }

    public boolean addTeam(Team team) {
        String sql = "INSERT INTO Team (TeamNumber, Name, City, ManagerName) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, team.getTeamNumber());
            ps.setString(2, team.getName());
            ps.setString(3, team.getCity());
            ps.setString(4, team.getManagerName());

            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.out.println("addTeam() error: " + e.getMessage());
            return false;
        }
    }

    public Team getTeamByNumber(int teamNumber) {
        String sql = "SELECT TeamNumber, Name, City, ManagerName FROM Team WHERE TeamNumber = ?";

        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, teamNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Team t = new Team();
                    t.setTeamNumber(rs.getInt("TeamNumber"));
                    t.setName(rs.getString("Name"));
                    t.setCity(rs.getString("City"));
                    t.setManagerName(rs.getString("ManagerName"));
                    return t;
                }
            }
        } catch (Exception e) {
            System.out.println("getTeamByNumber() error: " + e.getMessage());
        }
        return null;
    }

    public boolean updateTeam(Team team) {
        String sql = "UPDATE Team SET Name = ?, City = ?, ManagerName = ? WHERE TeamNumber = ?";

        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, team.getName());
            ps.setString(2, team.getCity());
            ps.setString(3, team.getManagerName());
            ps.setInt(4, team.getTeamNumber());

            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.out.println("updateTeam() error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a team safely by deleting dependent rows first (FK constraints).
     * Order:
     * 1) WorkExperience for coaches on this team
     * 2) Coaches on this team
     * 3) PlayerTeamAssociation rows for this team
     * 4) Team row
     */
    public boolean deleteTeamCascade(int teamNumber) {
        String deleteWorkExp =
                "DELETE we FROM WorkExperience we " +
                "JOIN Coach c ON we.CoachID = c.CoachID " +
                "WHERE c.TeamNumber = ?";

        String deleteCoaches = "DELETE FROM Coach WHERE TeamNumber = ?";
        String deleteAssociations = "DELETE FROM PlayerTeamAssociation WHERE TeamNumber = ?";
        String deleteTeam = "DELETE FROM Team WHERE TeamNumber = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(deleteWorkExp);
                PreparedStatement ps2 = conn.prepareStatement(deleteCoaches);
                PreparedStatement ps3 = conn.prepareStatement(deleteAssociations);
                PreparedStatement ps4 = conn.prepareStatement(deleteTeam)) {

                ps1.setInt(1, teamNumber);
                ps1.executeUpdate();

                ps2.setInt(1, teamNumber);
                ps2.executeUpdate();

                ps3.setInt(1, teamNumber);
                ps3.executeUpdate();

                ps4.setInt(1, teamNumber);
                int affected = ps4.executeUpdate();

                conn.commit();
                return affected == 1;
            } catch (Exception e) {
                conn.rollback();
                System.out.println("deleteTeamCascade() rollback: " + e.getMessage());
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.out.println("deleteTeamCascade() error: " + e.getMessage());
            return false;
        }
    }

}
