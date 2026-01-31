package app.dao;

import app.model.Coach;
import app.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CoachDAO {
    
    public List<Coach> getAllCoachesWithTeamName() {

        String sql =
                "SELECT c.CoachID, c.Name, c.TelephoneNumber, c.TeamNumber, t.Name AS TeamName " +
                "FROM Coach c " +
                "JOIN Team t ON c.TeamNumber = t.TeamNumber " +
                "ORDER BY c.CoachID";

        List<Coach> coaches = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Coach c = new Coach();
                c.setCoachID(rs.getInt("CoachID"));
                c.setName(rs.getString("Name"));
                c.setTelephoneNumber(rs.getString("TelephoneNumber"));
                c.setTeamNumber(rs.getInt("TeamNumber"));
                c.setTeamName(rs.getString("TeamName"));
                coaches.add(c);
            }
        } catch (Exception e) {
            System.out.println("getAllCoachesWithTeamName() error: " + e.getMessage());
        }

        return coaches;
    }

    public Coach getCoachById(int coachId) {

        String sql = "SELECT CoachID, Name, TelephoneNumber, TeamNumber FROM Coach WHERE CoachID = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, coachId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Coach c = new Coach();
                    c.setCoachID(rs.getInt("CoachID"));
                    c.setName(rs.getString("Name"));
                    c.setTelephoneNumber(rs.getString("TelephoneNumber"));
                    c.setTeamNumber(rs.getInt("TeamNumber"));
                    return c;
                }
            }
        } catch (Exception e) {
            System.out.println("getCoachById() error: " + e.getMessage());
        }

        return null;
    }

    public boolean addCoach(Coach coach) {

        String sql = "INSERT INTO Coach (Name, TelephoneNumber, TeamNumber) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, coach.getName());
            ps.setString(2, coach.getTelephoneNumber());
            ps.setInt(3, coach.getTeamNumber());

            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.out.println("addCoach() error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCoach(Coach coach) {

        String sql = "UPDATE Coach SET Name = ?, TelephoneNumber = ?, TeamNumber = ? WHERE CoachID = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, coach.getName());
            ps.setString(2, coach.getTelephoneNumber());
            ps.setInt(3, coach.getTeamNumber());
            ps.setInt(4, coach.getCoachID());

            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.out.println("updateCoach() error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCoachCascade(int coachId) {
        
        String deleteWorkExp = "DELETE FROM WorkExperience WHERE CoachID = ?";
        String deleteCoach = "DELETE FROM Coach WHERE CoachID = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(deleteWorkExp);
                 PreparedStatement ps2 = conn.prepareStatement(deleteCoach)) {

                ps1.setInt(1, coachId);
                ps1.executeUpdate();

                ps2.setInt(1, coachId);
                int affected = ps2.executeUpdate();

                conn.commit();
                return affected == 1;
            } catch (Exception e) {
                conn.rollback();
                System.out.println("deleteCoachCascade() rollback: " + e.getMessage());
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.out.println("deleteCoachCascade() error: " + e.getMessage());
            return false;
        }
    }

}
