package app.dao;

import app.model.Player;
import app.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {
    
    /**
     * View Players (one row per player) with "current/most recent" team.
     * We pick the latest association per player using AssociationID ordering.
     * If you insert a new association with YearLeft = NULL, it will be treated as "current".
     */
    public List<Player> getPlayersWithTeamSummary() {

        String sql =
            "SELECT " +
            "  p.PlayerID, p.LeagueWideNumber, p.Name AS PlayerName, p.Age, " +
            "  pta.TeamNumber, t.Name AS TeamName, pta.YearJoined, pta.YearLeft " +
            "FROM Player p " +
            "LEFT JOIN PlayerTeamAssociation pta " +
            "  ON pta.AssociationID = ( " +
            "    SELECT pta2.AssociationID " +
            "    FROM PlayerTeamAssociation pta2 " +
            "    WHERE pta2.PlayerID = p.PlayerID " +
            "    ORDER BY " +
            "      (pta2.YearLeft IS NULL) DESC, " +   // prefer current
            "      COALESCE(pta2.YearLeft, 9999) DESC, " +
            "      pta2.YearJoined DESC, " +
            "      pta2.AssociationID DESC " +
            "    LIMIT 1 " +
            "  ) " +
            "LEFT JOIN Team t ON t.TeamNumber = pta.TeamNumber " +
            "ORDER BY p.PlayerID";

            List<Player> players = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Player p = new Player();
                p.setPlayerId(rs.getInt("PlayerID"));
                p.setLeagueWideNumber(rs.getInt("LeagueWideNumber"));
                p.setName(rs.getString("PlayerName"));
                p.setAge(rs.getInt("Age"));

                Object tn = rs.getObject("TeamNumber");
                p.setTeamNumber(tn == null ? null : rs.getInt("TeamNumber"));
                p.setTeamName(rs.getString("TeamName"));

                Object yj = rs.getObject("YearJoined");
                p.setYearJoined(yj == null ? null : rs.getInt("YearJoined"));

                Object yl = rs.getObject("YearLeft");
                p.setYearLeft(yl == null ? null : rs.getInt("YearLeft"));

                players.add(p);
            }
        } catch (Exception e) {
            System.out.println("getPlayersWithTeamSummary() error: " + e.getMessage());
        }

        return players;
    }

    public Player getPlayerById(int playerId) {
        String sql = "SELECT PlayerID, LeagueWideNumber, Name, Age FROM Player WHERE PlayerID = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, playerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Player p = new Player();
                    p.setPlayerId(rs.getInt("PlayerID"));
                    p.setLeagueWideNumber(rs.getInt("LeagueWideNumber"));
                    p.setName(rs.getString("Name"));
                    p.setAge(rs.getInt("Age"));
                    return p;
                }
            }
        } catch (Exception e) {
            System.out.println("getPlayerById() error: " + e.getMessage());
        }

        return null;
    }

    /** Inserts a player and returns the new PlayerID (or -1 if failed). */
    public int addPlayer(Player player) {
        String sql = "INSERT INTO Player (LeagueWideNumber, Name, Age) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, player.getLeagueWideNumber());
            ps.setString(2, player.getName());
            ps.setInt(3, player.getAge());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("addPlayer() error: " + e.getMessage());
        }

        return -1;
    }

    /** Adds an association row (use YearLeft NULL to represent "current team"). */
    public boolean addAssociation(int playerId, int teamNumber, Integer yearJoined, Integer yearLeft) {
        String sql = "INSERT INTO PlayerTeamAssociation (PlayerID, TeamNumber, YearJoined, YearLeft) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, playerId);
            ps.setInt(2, teamNumber);

            if (yearJoined == null) ps.setNull(3, Types.INTEGER);
            else ps.setInt(3, yearJoined);

            if (yearLeft == null) ps.setNull(4, Types.INTEGER);
            else ps.setInt(4, yearLeft);

            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.out.println("addAssociation() error: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePlayer(Player player) {
        String sql = "UPDATE Player SET LeagueWideNumber = ?, Name = ?, Age = ? WHERE PlayerID = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, player.getLeagueWideNumber());
            ps.setString(2, player.getName());
            ps.setInt(3, player.getAge());
            ps.setInt(4, player.getPlayerId());

            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            System.out.println("updatePlayer() error: " + e.getMessage());
            return false;
        }
    }

    /** Deletes associations first, then player. */
    public boolean deletePlayerCascade(int playerId) {
        String deleteAssoc = "DELETE FROM PlayerTeamAssociation WHERE PlayerID = ?";
        String deletePlayer = "DELETE FROM Player WHERE PlayerID = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(deleteAssoc);
                 PreparedStatement ps2 = conn.prepareStatement(deletePlayer)) {

                ps1.setInt(1, playerId);
                ps1.executeUpdate();

                ps2.setInt(1, playerId);
                int affected = ps2.executeUpdate();

                conn.commit();
                return affected == 1;
            } catch (Exception e) {
                conn.rollback();
                System.out.println("deletePlayerCascade() rollback: " + e.getMessage());
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            System.out.println("deletePlayerCascade() error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Add player + initial association in one transaction.
     */
    public boolean addPlayerWithAssociation(Player player, int teamNumber, Integer yearJoined, Integer yearLeft) {
        String insertPlayer = "INSERT INTO Player (LeagueWideNumber, Name, Age) VALUES (?, ?, ?)";
        String insertAssoc  = "INSERT INTO PlayerTeamAssociation (PlayerID, TeamNumber, YearJoined, YearLeft) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false);

            int newPlayerId;

            try (PreparedStatement ps = conn.prepareStatement(insertPlayer, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, player.getLeagueWideNumber());
                ps.setString(2, player.getName());
                ps.setInt(3, player.getAge());
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        conn.rollback();
                        return false;
                    }
                    newPlayerId = keys.getInt(1);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(insertAssoc)) {
                ps.setInt(1, newPlayerId);
                ps.setInt(2, teamNumber);

                if (yearJoined == null) ps.setNull(3, Types.INTEGER);
                else ps.setInt(3, yearJoined);

                if (yearLeft == null) ps.setNull(4, Types.INTEGER);
                else ps.setInt(4, yearLeft);

                ps.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            System.out.println("addPlayerWithAssociation() error: " + e.getMessage());
            return false;
        }
    }
}
