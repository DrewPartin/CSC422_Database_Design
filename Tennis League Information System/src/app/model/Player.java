package app.model;

public class Player {
    
    private int playerId;
    private int leagueWideNumber;
    private String name;
    private int age;

    // "Current/Most recent" team info (derived via JOIN)
    private Integer teamNumber;      // nullable
    private String teamName;         // nullable
    private Integer yearJoined;      // nullable
    private Integer yearLeft;        // nullable

    public Player() {}

    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }

    public int getLeagueWideNumber() { return leagueWideNumber; }
    public void setLeagueWideNumber(int leagueWideNumber) { this.leagueWideNumber = leagueWideNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public Integer getTeamNumber() { return teamNumber; }
    public void setTeamNumber(Integer teamNumber) { this.teamNumber = teamNumber; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public Integer getYearJoined() { return yearJoined; }
    public void setYearJoined(Integer yearJoined) { this.yearJoined = yearJoined; }

    public Integer getYearLeft() { return yearLeft; }
    public void setYearLeft(Integer yearLeft) { this.yearLeft = yearLeft; }
}
